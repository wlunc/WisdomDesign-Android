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
    api(libs.compose.material3)
    api(libs.compose.ui)
    api(libs.compose.ui.graphics)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

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
