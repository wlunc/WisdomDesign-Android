# WisdomDesign-Android

Wisdom Design System 的 Jetpack Compose 实现。

## 安装

发布到 Maven Central，`groupId` 是 `io.github.wlunc.wisdom`。

```toml
# gradle/libs.versions.toml
[versions]
wisdom = "1.0.0"

[libraries]
wisdom-ui = { module = "io.github.wlunc.wisdom:wisdom-ui", version.ref = "wisdom" }
```

```kotlin
implementation(libs.wisdom.ui)
```

要求 minSdk 24。Android 12+ 走背景模糊，以下降级为纯色 + 描边。

## 命名

公开 API 一律 `WD` 前缀，与 iOS 端保持同名：

```kotlin
import io.github.wlunc.wisdom.foundation.WDTheme
import io.github.wlunc.wisdom.foundation.WDType

WDTheme {
    Text("今天的任务", style = WDType.headline.toTextStyle())
}
```

令牌通过 `WDTheme.colors` 取，静态常量在 `WDColor` / `WDSpacing` / `WDRadius` / `WDSize` /
`WDGradient` / `WDMotion` 里。

## 令牌来源

`wisdom-ui/src/main/kotlin/.../foundation/Generated/WDTokens.kt` 由设计仓库生成，**不要手改**：

```bash
# 在 wisdomdesign 仓库
node tools/token-build/build.js
```

## 开发

```bash
./gradlew :wisdom-ui:build          # 编译 + 单测 + apiCheck
./gradlew :wisdom-ui:apiDump        # 公开 API 有变化时更新基线
./gradlew :wisdom-ui:publishToMavenLocal   # 本地验证发布产物
```
