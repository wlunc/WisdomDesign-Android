import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "io.github.wlunc.wisdom"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    // 不加 public 就不暴露，避免内部实现变成事实上的公开 API
    explicitApi()
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    api(composeBom)
    api(libs.compose.foundation)
    implementation(libs.compose.material3)
    api(libs.compose.ui)
    api(libs.compose.ui.graphics)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)

    testImplementation(libs.junit)
}

mavenPublishing {
    // 0.34 起 Central Portal 成为默认目标，不再需要 SonatypeHost
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.wlunc.wisdom",
        artifactId = "wisdom-ui",
        version = "1.0.0-SNAPSHOT",
    )

    pom {
        name.set("Wisdom UI")
        description.set("Wisdom Design System 的 Jetpack Compose 实现")
        url.set("https://github.com/wlunc/WisdomDesign-Android")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }
        developers {
            developer {
                id.set("wlunc")
                name.set("ChenWL")
            }
        }
        scm {
            url.set("https://github.com/wlunc/WisdomDesign-Android")
            connection.set("scm:git:https://github.com/wlunc/WisdomDesign-Android.git")
        }
    }
}
// ---------------------------------------------------------------------------
// G1–G13 + wisdomGate：门禁任务段（F3 / AGENTS §5.2 / SPEC §1.5.2）
// 说明：ktlint/detekt/lintRelease 插件暂未接入，暂不 dependsOn，待后续补。
// ---------------------------------------------------------------------------

private data class DepRule(
    val fromPath: Regex,
    val forbiddenImport: Regex,
    val allowlist: Set<String> = emptySet(),
)

private val WISDOM_SRC_ROOTS = listOf("src/main/kotlin", "src/debug/kotlin")

/** 把所有脚本型检查包装成 Gradle Exec 任务。 */
fun Project.wdExecTask(name: String, script: String, vararg inputPaths: String) =
    tasks.register<Exec>(name) {
        group = "verification"
        description = "WisdomDesign gate: $name"
        commandLine("bash", layout.projectDirectory.file("../tools/$script").asFile.absolutePath)
        this.inputs.files(inputPaths.map { layout.projectDirectory.file(it) })
    }

val wisdomCheckPublicTypes = wdExecTask("wisdomCheckPublicTypes", "check-public-types.sh", "api/wisdom-ui.api")

val wisdomCheckComposeMetrics = wdExecTask(
    "wisdomCheckComposeMetrics", "check-compose-metrics.sh",
    "build/compose-metrics", "build/compose-metrics/baseline.json",
)

val wisdomCheckGeneratedSelfProof = wdExecTask(
    "wisdomCheckGeneratedSelfProof", "check-generated-self-proof.sh",
    "src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDTokens.kt",
    "src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDTokensVersion.kt",
)

val wisdomCheckReleaseSymbols = wdExecTask(
    "wisdomCheckReleaseSymbols", "check-release-symbols.sh",
    "build/outputs/aar/wisdom-ui-release.aar", "build/outputs/aar/wisdom-ui-debug.aar",
)
wisdomCheckReleaseSymbols.configure { dependsOn("assembleRelease", "assembleDebug") }

val wisdomCheckLiteralLanguage = wdExecTask(
    "wisdomCheckLiteralLanguage", "check-literal-language.sh", "src/main/kotlin", "src/debug/kotlin",
)

val wisdomCheckTokenLiterals = wdExecTask(
    "wisdomCheckTokenLiterals", "check-token-literals.sh", "src/main/kotlin", "src/debug/kotlin",
)

fun Project.wdHandwrittenKotlinFiles(): List<Pair<String, File>> {
    val moduleDir = layout.projectDirectory.asFile
    val roots = WISDOM_SRC_ROOTS.map { File(moduleDir, it) }
    return roots.flatMap { root ->
        if (!root.exists()) emptyList()
        else root.walkTopDown().filter { it.isFile && it.extension == "kt" }
            .map { it.relativeTo(moduleDir).invariantSeparatorsPath to it }.toList()
    }
}

val wisdomCheckPackageDeps by tasks.registering {
    group = "verification"
    description = "G1: 依赖方向 + 包图无环 + 全库 m3 import 计数 == 1"
    doLast {
        val rules = listOf(
            DepRule(Regex("(foundation|internal)/"), Regex("""^import\s+io\.github\.wlunc\.wisdom\.components\.""" )),
            DepRule(Regex("components/primitives/"), Regex("""^import\s+io\.github\.wlunc\.wisdom\.components\.composites\.""" )),
            DepRule(Regex(".*"), Regex("""^import\s+androidx\.compose\.material3\.""" ), setOf("foundation/theme/WDMaterialScheme.kt")),
        )
        val violations = mutableListOf<String>()
        val m3ImportHits = mutableListOf<String>()
        val m3AnyLineHits = mutableListOf<String>() // AGENTS §5.2 grep -rl 口径：任意行命中
        wdHandwrittenKotlinFiles().forEach { (rel, file) ->
            file.readLines().withIndex().forEach { (idx, raw) ->
                val line = raw.substringBefore("//").trim()
                if (line.startsWith("import ")) {
                    rules.forEach { rule ->
                        if (rule.fromPath.containsMatchIn(rel) &&
                            rule.forbiddenImport.containsMatchIn(line) &&
                            rule.allowlist.none { rel.endsWith(it) }
                        ) violations += "G1 $rel:" + (idx + 1) + " $line"
                    }
                    if (line.contains("androidx.compose.material3")) m3ImportHits += rel
                }
                // 口径 B：独立累计全部行（含全限定名直用），与 grep -rl 手工判据一致
                if (line.contains("androidx.compose.material3")) m3AnyLineHits += rel
            }
        }
        val allowedM3 = "src/main/kotlin/io/github/wlunc/wisdom/foundation/theme/WDMaterialScheme.kt"
        val importSet = m3ImportHits.distinct().sorted()
        val anyLineSet = m3AnyLineHits.distinct().sorted()
        check(importSet == listOf(allowedM3)) {
            "G1-③ import 口径 m3 触点必须恰好 1 个（$allowedM3），实测：$importSet"
        }
        check(anyLineSet == listOf(allowedM3)) {
            "G1-③ grep -rl 口径（全部行）m3 触点必须恰好 1 个（$allowedM3），实测：$anyLineSet"
        }
        println("G1-③ m3 双口径：import 文件数=${importSet.size} / grep -rl 全部行文件数=${anyLineSet.size}，集合=$anyLineSet")
        check(violations.isEmpty()) { "G1 依赖方向违例：\n${violations.joinToString("\n")}" }
    }
}

val wisdomCheckMainNoPreview by tasks.registering {
    group = "verification"
    description = "G3: src/main 不得出现 @Preview / ui-tooling"
    doLast {
        val bad = mutableListOf<String>()
        wdHandwrittenKotlinFiles()
            .filter { (rel, _) -> rel.startsWith("src/main/kotlin/") }
            .forEach { (rel, file) ->
                file.useLines { lines ->
                    lines.forEachIndexed { idx, line ->
                        if (line.contains("@Preview") || line.contains("androidx.compose.ui.tooling")) {
                            bad += "G3 $rel:" + (idx + 1) + " $line"
                        }
                    }
                }
            }
        check(bad.isEmpty()) { "G3 src/main 出现预览/tooling：\n${bad.joinToString("\n")}" }
    }
}

val wisdomCheckInsetsWhitelist by tasks.registering {
    group = "verification"
    description = "G10: WindowInsets 组件白名单（6 个）"
    doLast {
        val allowed = setOf("WDNavigationBar", "WDTabBar", "WDToast", "WDBottomSheet", "WDActionSheet", "WDAlert")
        val bad = mutableListOf<String>()
        wdHandwrittenKotlinFiles().forEach { (rel, file) ->
            if ("WindowInsets" in file.readText() && allowed.none { rel.contains(it) }) bad += "G10 $rel"
        }
        check(bad.isEmpty()) { "G10 WindowInsets 越界：\n${bad.joinToString("\n")}" }
    }
}

val wisdomCheckThemeDefaults by tasks.registering {
    group = "verification"
    description = "G11: theme 默认参数不得是构造调用"
    doLast {
        val allowed = setOf("WDLayout.Comfortable", "WDEffectsBudget.Default", "WDIconSet.Empty", "WDMotionScale.Normal")
        val ctor = Regex("""=\s*[A-Z]\w*\(""")
        val bad = mutableListOf<String>()
        wdHandwrittenKotlinFiles()
            .filter { (rel, _) -> "/foundation/theme/" in rel }
            .forEach { (rel, file) ->
                file.useLines { lines ->
                    lines.forEachIndexed { idx, line ->
                        val stripped = line.substringBefore("//").trim()
                        if (ctor.containsMatchIn(stripped) && allowed.none { it in stripped }) {
                            bad += "G11 $rel:" + (idx + 1) + " $stripped"
                        }
                    }
                }
            }
        check(bad.isEmpty()) { "G11 默认参数构造调用：\n${bad.joinToString("\n")}" }
    }
}

val wisdomCheckSingleCallSite by tasks.registering {
    group = "verification"
    description = "G12: wdSystemGestureExclusion 调用点 == 1"
    doLast {
        val hits = mutableListOf<String>()
        wdHandwrittenKotlinFiles().forEach { (rel, file) ->
            file.useLines { lines ->
                lines.forEachIndexed { idx, line ->
                    if ("wdSystemGestureExclusion(" in line && "fun wdSystemGestureExclusion" !in line) {
                        hits += "G12 $rel:" + (idx + 1)
                    }
                }
            }
        }
        check(hits.size == 1) { "G12 期望恰好 1 个调用点，实测 ${hits.size}：${hits.joinToString(", ")}" }
    }
}

val wisdomCheckDeprecatedBaseline by tasks.registering {
    group = "verification"
    description = "G4: deprecated 成员数不增长"
    doLast {
        val baseline = File(layout.projectDirectory.asFile, "../tools/deprecated-baseline.txt")
        check(baseline.exists()) { "G4 缺 deprecated 基线文件（禁止门禁自动改写）: ${baseline.absolutePath}" }
        val count = wdHandwrittenKotlinFiles()
            .filter { (rel, _) -> rel.startsWith("src/main/kotlin/") }
            .sumOf { (_, file) -> file.readText().split("@Deprecated").size - 1 }
        val limit = baseline.readText().trim().toIntOrNull() ?: 0
        check(count <= limit) { "G4 @Deprecated 计数 $count 超过基线 $limit" }
    }
}

val wisdomCheckStabilityFields by tasks.registering {
    group = "verification"
    description = "G5: api 基线 \$stable 存在性"
    doLast {
        val api = File(layout.projectDirectory.asFile, "api/wisdom-ui.api")
        check(api.exists()) { "G5 缺 API 基线: ${api.absolutePath}" }
        val lines = api.readLines()
        val missing = mutableListOf<String>()
        lines.forEachIndexed { idx, line ->
            if (line.startsWith("public final class ") || line.startsWith("public class ")) {
                val window = lines.drop(idx + 1).take(3)
                if (window.none { it.contains("\$stable") }) missing += line
            }
        }
        check(missing.isEmpty()) { "G5 缺 \$stable：\n${missing.joinToString("\n")}" }
    }
}

val wisdomGate by tasks.registering {
    group = "verification"
    description = "Android PR 必过门禁：G1–G13 + apiCheck + assembleRelease/Debug + testDebugUnitTest"
    dependsOn(
        "apiCheck",
        "assembleRelease",
        "assembleDebug",
        "testDebugUnitTest",
        wisdomCheckPackageDeps,
        wisdomCheckTokenLiterals,
        wisdomCheckMainNoPreview,
        wisdomCheckDeprecatedBaseline,
        wisdomCheckStabilityFields,
        wisdomCheckComposeMetrics,
        wisdomCheckGeneratedSelfProof,
        wisdomCheckReleaseSymbols,
        wisdomCheckPublicTypes,
        wisdomCheckInsetsWhitelist,
        wisdomCheckThemeDefaults,
        wisdomCheckSingleCallSite,
        wisdomCheckLiteralLanguage,
    )
}
