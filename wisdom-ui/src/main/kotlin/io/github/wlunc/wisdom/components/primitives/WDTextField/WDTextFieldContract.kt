package io.github.wlunc.wisdom.components.primitives.WDTextField

import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDState

/** C-15 #03 canonical. */
public const val WDTextFieldControlledValueName: String = "text"

/** 字段盒高度恒为 46dp；所有变体、所有状态不变（U5 / 冻结验收）。 */
internal val WDTextFieldFieldHeight = WDSize.fieldHeight

/** 聚焦光环宽度来自状态令牌，不从系统/静态 scheme 读取。 */
internal val WDTextFieldFocusWidth = WDState.focusRingWidth

/** 状态优先级：disabled > error > focused > read-only > default。 */
internal val WDTextFieldStateOrder: List<WDTextFieldVisualState> = listOf(
    WDTextFieldVisualState.Disabled,
    WDTextFieldVisualState.Error,
    WDTextFieldVisualState.Focused,
    WDTextFieldVisualState.ReadOnly,
    WDTextFieldVisualState.Default,
)
