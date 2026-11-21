package io.github.wlunc.wisdom.foundation.typography

import androidx.compose.ui.text.TextStyle
import io.github.wlunc.wisdom.foundation.generated.WDTextStyle

/**
 * 设计字阶 → Compose TextStyle。
 * ABI 所有者已从 WDThemeKt 迁至 WDTextMetricsKt（本文件），需在 CHANGELOG/apiDump 同批体现。
 */
public fun WDTextStyle.toTextStyle(): TextStyle = TextStyle(
    fontSize = size,
    lineHeight = lineHeight,
    fontWeight = weight,
)
