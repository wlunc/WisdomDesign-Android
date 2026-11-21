package io.github.wlunc.wisdom.internal

/**
 * G1-③ m3 import 计数的反例自测（§1.5.4-H）。
 *
 * 判定规则与 shell 侧 wisdomCheckPackageDeps 保持一致：
 * 合法触点恰好 1 个且必须是 WDMaterialScheme.kt；注入第二个触点必须被判失败。
 */
internal object WDM3ImportCountRule {
    const val ALLOWED = "src/main/kotlin/io/github/wlunc/wisdom/foundation/theme/WDMaterialScheme.kt"

    fun isM3Import(line: String): Boolean {
        val stripped = line.substringBefore("//").trim()
        return stripped.startsWith("import ") && "androidx.compose.material3" in stripped
    }

    fun check(relativePathToImports: Map<String, List<String>>): Boolean =
        check(relativePathToImports, useImportLinesOnly = true)

    /** 与 build.gradle.kts G1-③ 双口径一致：true=仅 import 行，false=全部行（覆盖全限定名直用）。 */
    fun check(
        relativePathToLines: Map<String, List<String>>,
        useImportLinesOnly: Boolean,
    ): Boolean {
        val hits = relativePathToLines.flatMap { (path, lines) ->
            lines.filter { raw ->
                val line = raw.substringBefore("//").trim()
                val matches = if (useImportLinesOnly) isM3Import(line) else line.contains("androidx.compose.material3")
                matches
            }.map { path }
        }.distinct().sorted()
        return hits == listOf(ALLOWED)
    }
}

class WDM3ImportCountTest {
    private val positive = mapOf(
        WDM3ImportCountRule.ALLOWED to listOf("import androidx.compose.material3.MaterialTheme"),
        "src/main/kotlin/io/github/wlunc/wisdom/components/WDButton.kt" to listOf("import androidx.compose.foundation.clickable"),
    )
    private val negative = positive + mapOf(
        "src/main/kotlin/io/github/wlunc/wisdom/components/WDCard.kt" to listOf("import androidx.compose.material3.Button"),
    )

    @org.junit.Test
    fun `G1_正例_唯一 m3 触点通过`() {
        org.junit.Assert.assertTrue(WDM3ImportCountRule.check(positive))
    }

    @org.junit.Test
    fun `G1_反例_第二个 m3 触点判失败`() {
        org.junit.Assert.assertFalse(WDM3ImportCountRule.check(negative))
    }

    @org.junit.Test
    fun `G1_反例_全限定名直用_仅 import 口径漏判但全行口径判失败`() {
        val fqcn = positive + mapOf(
            "src/main/kotlin/io/github/wlunc/wisdom/components/WDCard.kt" to
                listOf("val x = androidx.compose.material3.Button()"),
        )
        org.junit.Assert.assertTrue("import-only 口径按定义不覆盖全限定名", WDM3ImportCountRule.check(fqcn, useImportLinesOnly = true))
        org.junit.Assert.assertFalse("grep -rl 全行口径必须判失败", WDM3ImportCountRule.check(fqcn, useImportLinesOnly = false))
    }
}
