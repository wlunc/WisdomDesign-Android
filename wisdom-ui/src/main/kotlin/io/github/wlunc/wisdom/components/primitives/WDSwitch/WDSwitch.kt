package io.github.wlunc.wisdom.components.primitives.WDSwitch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.roundToPx
import io.github.wlunc.wisdom.foundation.accessibility.WDSemantics
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDComponent
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDState
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.layout.wdTouchTarget
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.typography.toTextStyle

internal data class WDSwitchMetrics(
    val trackWidth: Dp,
    val trackHeight: Dp,
    val thumbSize: Dp,
    val horizontalPadding: Dp,
    val focusWidth: Dp,
) {
    /** 相对轨道左上角的滑块 x 偏移。 */
    val thumbOffsetOn: Dp get() = trackWidth - thumbSize - horizontalPadding

    companion object {
        // 标准 51×31 / thumb 27 / 内距 2；紧凑 40×24 / thumb 20 / 内距 2。
        // 均由既有令牌组合表达，不新增/手改 generated。
        val Standard = WDSwitchMetrics(
            trackWidth = WDSize.controlLg - WDSpacing.s1,
            trackHeight = WDSize.controlSm - WDSpacing.s2,
            thumbSize = WDSize.avatarSm - WDSpacing.s1,
            horizontalPadding = WDSpacing.s1,
            focusWidth = WDState.focusRingWidth,
        )
        val Compact = WDSwitchMetrics(
            trackWidth = WDSize.touchTargetMin - WDSpacing.s10 + WDSpacing.s8 + WDSpacing.s1,
            trackHeight = WDSize.controlSm - WDSpacing.s10 + WDSpacing.s9 - WDSpacing.s2,
            thumbSize = WDSize.iconXl - WDSpacing.s2,
            horizontalPadding = WDSpacing.s1,
            focusWidth = WDState.focusRingWidth,
        )
    }
}

internal fun WDSwitchSize.metrics(): WDSwitchMetrics = when (this) {
    WDSwitchSize.Standard -> WDSwitchMetrics.Standard
    WDSwitchSize.Compact -> WDSwitchMetrics.Compact
}

/** U6 状态优先级：loading 不进 enabled=false；focused/hover 为叠加维度。 */
internal enum class WDSwitchVisualState { Default, Pressed, Focused, Hover, Loading, Disabled }

internal fun wdSwitchVisualState(
    enabled: Boolean,
    loading: Boolean,
    pressed: Boolean,
    focused: Boolean,
    hovered: Boolean,
): WDSwitchVisualState = when {
    !enabled -> WDSwitchVisualState.Disabled
    loading -> WDSwitchVisualState.Loading
    pressed -> WDSwitchVisualState.Pressed
    focused -> WDSwitchVisualState.Focused
    hovered -> WDSwitchVisualState.Hover
    else -> WDSwitchVisualState.Default
}

/** 契约主签名（t2 冻结 §2.10）：受控值名 on；无 enabled 参数。 */
@Composable
public fun WDSwitch(
    on: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    description: String? = null,
    size: WDSwitchSize = WDSwitchSize.Standard,
) {
    WDSwitchImpl(
        on = on,
        onCheckedChange = onCheckedChange,
        label = label,
        modifier = modifier,
        description = description,
        size = size,
        enabled = true,
        loading = false,
    )
}

/** Android 既有重载：enabled/loading 仅作附加参数，不进契约主签名。 */
@Composable
public fun WDSwitch(
    on: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String?,
    modifier: Modifier,
    description: String?,
    size: WDSwitchSize,
    enabled: Boolean,
    loading: Boolean = false,
) {
    WDSwitchImpl(on, onCheckedChange, label, modifier, description, size, enabled, loading)
}

@Composable
private fun WDSwitchImpl(
    on: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String?,
    modifier: Modifier,
    description: String?,
    size: WDSwitchSize,
    enabled: Boolean,
    loading: Boolean,
) {
    val colors = WDTheme.colors
    val haptics = rememberWDHaptics()
    val metrics = size.metrics()
    val state = wdSwitchVisualState(enabled, loading, false, false, false)
    val trackColor: Color = when {
        state == WDSwitchVisualState.Disabled -> colors.fillField
        on -> colors.textBrand
        else -> colors.borderHairlineStrong
    }
    val thumbColor: Color = when {
        state == WDSwitchVisualState.Disabled -> colors.textDisabled
        on -> colors.textOnDark
        else -> colors.surfaceCardSolid
    }
    val combined = WDSemantics.join(", ", label, description)

    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Switch,
                onClick = {
                    if (!loading) {
                        haptics.toggle()
                        onCheckedChange(!on)
                    }
                },
            )
            .semantics { stateDescription = combined }
            .wdTouchTarget(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WDSpacing.s3),
    ) {
        Box(
            modifier = Modifier
                .width(metrics.trackWidth)
                .height(metrics.trackHeight)
                .clip(RoundedCornerShape(WDRadius.full))
                .background(trackColor)
                .then(
                    if (state == WDSwitchVisualState.Focused) {
                        Modifier.border(WDState.focusRingWidth, colors.textBrand, RoundedCornerShape(WDRadius.full))
                    } else Modifier,
                ),
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        androidx.compose.ui.unit.IntOffset(
                            x = (if (on) metrics.trackWidth - metrics.thumbSize - metrics.horizontalPadding else metrics.horizontalPadding).roundToPx(),
                            y = 0,
                        )
                    }
                    .size(metrics.thumbSize)
                    .clip(RoundedCornerShape(WDRadius.full))
                    .background(thumbColor)
                    .scale(if (state == WDSwitchVisualState.Pressed) WDComponent.pressScale else 1f),
            )
        }
        if (label != null || description != null) {
            Column(verticalArrangement = Arrangement.spacedBy(WDSpacing.s1)) {
                if (label != null) {
                    BasicText(
                        text = label,
                        style = WDType.subheadline.toTextStyle().copy(
                            color = if (state == WDSwitchVisualState.Disabled) colors.textDisabled else colors.textPrimary,
                        ),
                    )
                }
                if (description != null) {
                    BasicText(
                        text = description,
                        style = WDType.footnote.toTextStyle().copy(
                            color = if (state == WDSwitchVisualState.Disabled) colors.textDisabled else colors.textSecondary,
                        ),
                    )
                }
            }
        }
    }
}