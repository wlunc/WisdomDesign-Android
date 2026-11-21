package io.github.wlunc.wisdom.components.primitives.WDDivider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.theme.WDTheme

/** 装饰线：1 物理像素以 0 宽 hairline 表达，颜色用 border.hairline（符号令牌）。 */
internal val WD_DIVIDER_THICKNESS: androidx.compose.ui.unit.Dp get() = WDSpacing.s1

/** 内缩起点 = 文本区起点（16 + 34 + 12），全部由 spacing 阶梯表达。 */
private val ZERO_INSET: androidx.compose.ui.unit.Dp get() = WDSpacing.s1 - WDSpacing.s1

internal val WD_DIVIDER_INSET: androidx.compose.ui.unit.Dp
    get() = WDSpacing.s5 + (WDSpacing.s7 + WDSpacing.s2) + WDSpacing.s4

/** WDDivider 主入口（签名冻结于 android/docs/M2-SIGNATURE-FREEZE.md §2.6）。 */
@Composable
public fun WDDivider(
    style: WDDividerStyle = WDDividerStyle.Full,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(WD_DIVIDER_THICKNESS)
            .padding(start = if (style == WDDividerStyle.Inset) WD_DIVIDER_INSET else ZERO_INSET)
            .background(WDTheme.colors.borderHairline)
            .clearAndSetSemantics {},
    )
}