package io.github.wlunc.wisdom.components.primitives.WDCheckbox

import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing

/** C-15 例外：Android 保留 checked。 */
public const val WDCheckboxControlledValueName: String = "checked"

/** 契约默认（冻结 §2.5）。 */
internal const val WDCheckboxDefaultIndeterminate: Boolean = false
internal val WDCheckboxDefaultSize: WDCheckboxSize = WDCheckboxSize.Standard

/** 方框几何：标准 26dp / 紧凑 22dp（由既有令牌组合表达）。 */
internal fun WDCheckboxSize.boxSizeDp(): Float = when (this) {
    WDCheckboxSize.Standard -> WDSize.checkbox.value
    WDCheckboxSize.Compact -> WDSize.controlSm.value - WDSpacing.s1.value
}

/** 状态优先级：disabled > loading > pressed > focused > hover > default。 */
internal val WDCheckboxStateOrder: List<WDCheckboxVisualState> = listOf(
    WDCheckboxVisualState.Disabled,
    WDCheckboxVisualState.Loading,
    WDCheckboxVisualState.Pressed,
    WDCheckboxVisualState.Focused,
    WDCheckboxVisualState.Hover,
    WDCheckboxVisualState.Default,
)