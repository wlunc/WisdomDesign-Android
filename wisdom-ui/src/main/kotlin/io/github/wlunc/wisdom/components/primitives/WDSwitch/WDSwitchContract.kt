package io.github.wlunc.wisdom.components.primitives.WDSwitch

import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing

/** C-15 #05 canonical. */
public const val WDSwitchControlledValueName: String = "on"

/** 主签名默认尺寸档。 */
internal val WDSwitchDefaultSize: WDSwitchSize = WDSwitchSize.Standard

/** 轨道/滑块几何（dpi 值从令牌组合得到；unit 测试按 dp 数值断言）。 */
internal data class WDSwitchGeometry(val trackWidth: Float, val trackHeight: Float, val thumb: Float, val padding: Float)

internal fun WDSwitchSize.geometry(): WDSwitchGeometry = when (this) {
    WDSwitchSize.Standard -> WDSwitchGeometry(
        WDSize.controlLg.value - WDSpacing.s1.value,
        WDSize.controlSm.value - WDSpacing.s2.value,
        WDSize.avatarSm.value - WDSpacing.s1.value,
        WDSpacing.s1.value,
    )
    WDSwitchSize.Compact -> WDSwitchGeometry(
        WDSize.touchTargetMin.value - WDSpacing.s10.value + WDSpacing.s8.value + WDSpacing.s1.value,
        WDSize.controlSm.value - WDSpacing.s10.value + WDSpacing.s9.value - WDSpacing.s2.value,
        WDSize.iconXl.value - WDSpacing.s2.value,
        WDSpacing.s1.value,
    )
}

/** 状态优先级：disabled > loading > pressed > focused > hover > default。 */
internal val WDSwitchStateOrder: List<WDSwitchVisualState> = listOf(
    WDSwitchVisualState.Disabled,
    WDSwitchVisualState.Loading,
    WDSwitchVisualState.Pressed,
    WDSwitchVisualState.Focused,
    WDSwitchVisualState.Hover,
    WDSwitchVisualState.Default,
)