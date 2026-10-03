plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.binary.compatibility)
}

// 公开 API 快照：./gradlew apiDump 生成，apiCheck 校验。
// 两端锁同一版本号，Swift 侧由源码可见性保证，Kotlin 侧靠这里兜底。
apiValidation {
    validationDisabled = false
}
