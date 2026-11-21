package io.github.wlunc.wisdom.components.primitives.WDCheckbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import io.github.wlunc.wisdom.foundation.accessibility.WDSemantics
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDComponent
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDState
import io.github.wlunc.wisdom.foundation.generated.WDTextStyle
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.layout.wdTouchTarget
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.typography.toTextStyle

internal data class WDCheckboxMetrics(
    val boxSize: Dp,
    val cornerRadius: Dp,
    val labelStyle: WDTextStyle,
) {
    companion object {
        val Standard = WDCheckboxMetrics(WDSize.checkbox, WDRadius.checkbox, WDType.subheadline)
        val Compact = WDCheckboxMetrics(WDSize.controlSm, WDRadius.xs, WDType.footnote)
    }
}

internal fun WDCheckboxSize.metrics(): WDCheckboxMetrics = when (this) {
    WDCheckboxSize.Standard -> WDCheckboxMetrics.Standard
    WDCheckboxSize.Compact -> WDCheckboxMetrics.Compact
}

/** U6 状态优先级：disabled > loading > pressed > focused > hover > default。 */
internal enum class WDCheckboxVisualState { Default, Hover, Focused, Pressed, Loading, Disabled }

internal fun wdCheckboxVisualState(
    enabled: Boolean,
    loading: Boolean,
    pressed: Boolean,
    focused: Boolean,
    hovered: Boolean,
): WDCheckboxVisualState = when {
    !enabled -> WDCheckboxVisualState.Disabled
    loading -> WDCheckboxVisualState.Loading
    pressed -> WDCheckboxVisualState.Pressed
    focused -> WDCheckboxVisualState.Focused
    hovered -> WDCheckboxVisualState.Hover
    else -> WDCheckboxVisualState.Default
}

/** 契约主签名（t2 冻结 §2.5）：新增带默认参数集中在重载，保持 $default ABI 不漂移。 */
@Composable
public fun WDCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
) {
    WDCheckboxImpl(checked, onCheckedChange, label, modifier, false, WDCheckboxSize.Standard, null, true, false)
}

/** B7/B8 增量重载：indeterminate / size / accessibilityLabel。 */
@Composable
public fun WDCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String?,
    modifier: Modifier,
    indeterminate: Boolean,
    size: WDCheckboxSize,
    accessibilityLabel: String?,
) {
    WDCheckboxImpl(checked, onCheckedChange, label, modifier, indeterminate, size, accessibilityLabel, true, false)
}

/** Android 既有附加重载：enabled/loading 不进契约主签名。 */
@Composable
public fun WDCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String?,
    modifier: Modifier,
    indeterminate: Boolean,
    size: WDCheckboxSize,
    accessibilityLabel: String?,
    enabled: Boolean,
    loading: Boolean,
) {
    WDCheckboxImpl(checked, onCheckedChange, label, modifier, indeterminate, size, accessibilityLabel, enabled, loading)
}

@Composable
private fun WDCheckboxImpl(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String?,
    modifier: Modifier,
    indeterminate: Boolean,
    size: WDCheckboxSize,
    accessibilityLabel: String?,
    enabled: Boolean,
    loading: Boolean,
) {
    val colors = WDTheme.colors
    val haptics = rememberWDHaptics()
    val metrics = size.metrics()
    val state = wdCheckboxVisualState(enabled, loading, false, false, false)
    val selected = checked || indeterminate
    val boxColor = when {
        state == WDCheckboxVisualState.Disabled -> colors.fillField
        selected -> colors.textBrand
        else -> colors.fillField
    }
    val outlineColor = when {
        state == WDCheckboxVisualState.Disabled -> colors.textDisabled
        selected -> Color.Transparent
        else -> colors.borderHairlineStrong
    }
    val readText = WDSemantics.join(", ", accessibilityLabel ?: label)

    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Checkbox,
                onClick = {
                    if (!loading) {
                        haptics.toggle()
                        onCheckedChange(if (indeterminate) true else !checked)
                    }
                },
            )
            .semantics { stateDescription = readText }
            .wdTouchTarget(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WDSpacing.s3),
    ) {
        Box(
            modifier = Modifier
                .size(metrics.boxSize)
                .clip(RoundedCornerShape(metrics.cornerRadius))
                .background(boxColor)
                .border(WDSpacing.s1, outlineColor, RoundedCornerShape(metrics.cornerRadius))
                .scale(if (state == WDCheckboxVisualState.Pressed) WDComponent.checkboxPressScale else 1f)
                .graphicsLayer { alpha = if (state == WDCheckboxVisualState.Disabled) WDState.disabledAlpha / 100f else 1f }
                .drawBehind { drawCheckboxMark(selected, indeterminate, colors.textOnFill) },
        )
        if (label != null) {
            BasicText(
                text = label,
                style = metrics.labelStyle.toTextStyle().copy(
                    color = if (state == WDCheckboxVisualState.Disabled) colors.textDisabled else colors.textPrimary,
                ),
            )
        }
    }
}

/** 勾为 Path 描边；半选为横杠。形状是主信号，颜色仅辅助。 */
private fun DrawScope.drawCheckboxMark(selected: Boolean, indeterminate: Boolean, mark: Color) {
    if (!selected) return
    if (indeterminate) {
        drawLine(
            color = mark,
            start = Offset(WDSpacing.s3.toPx(), center.y),
            end = Offset(size.width - WDSpacing.s3.toPx(), center.y),
            strokeWidth = WDSpacing.s1.toPx(),
            cap = StrokeCap.Round,
        )
        return
    }
    val path = Path().apply {
        moveTo(WDSpacing.s4.toPx(), center.y)
        lineTo(WDSpacing.s5.toPx(), WDSpacing.s6.toPx())
        lineTo(WDSize.checkbox.toPx(), WDSpacing.s3.toPx())
    }
    drawPath(path, mark, style = Stroke(width = WDSpacing.s1.toPx(), cap = StrokeCap.Round))
}