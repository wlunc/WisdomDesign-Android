package io.github.wlunc.wisdom.components.primitives.WDBadge

/** 可 JVM 单测的几何/文案例：所有尺寸都来自生成令牌，组件只消费此对象。 */
internal object WDBadgeGeometry {
    internal const val MAX_COUNT: Int = 99

    internal fun countText(number: Int): String =
        if (number > MAX_COUNT) "$MAX_COUNT+" else number.toString()

    internal fun isVisible(value: WDBadgeValue?): Boolean = when (val content = value?.content) {
        is WDBadgeValue.Content.Count -> content.number > 0
        is WDBadgeValue.Content.Label, WDBadgeValue.Content.Dot -> true
    }

    internal fun accessibilityText(value: WDBadgeValue?): String? = when (val content = value?.content) {
        is WDBadgeValue.Content.Label -> content.text
        is WDBadgeValue.Content.Count -> {
            val number = content.number
            if (number > 0) countText(number) else null
        }
        WDBadgeValue.Content.Dot, null -> null
    }

    /** U6：本组件无 disabled/loading 入参，状态串固定落在 default（pressed 走 indication）。 */
    internal fun resolveVisualState(
        disabled: Boolean = false,
        loading: Boolean = false,
        pressed: Boolean = false,
        focused: Boolean = false,
        hovered: Boolean = false,
    ): WDBadgeVisualState = when {
        disabled -> WDBadgeVisualState.Disabled
        loading -> WDBadgeVisualState.Loading
        pressed -> WDBadgeVisualState.Pressed
        focused -> WDBadgeVisualState.Focused
        hovered -> WDBadgeVisualState.Hover
        else -> WDBadgeVisualState.Default
    }
}

internal enum class WDBadgeVisualState { Disabled, Loading, Pressed, Focused, Hover, Default }

/** 状态色解析：只从 WDTheme.colors 取语义色，不读静态 scheme。 */
internal object WDBadgePalette {
    internal fun background(variant: WDBadgeVariant): String = when (variant) {
        WDBadgeVariant.Shared -> "surfaceTint"
        WDBadgeVariant.Schedule -> "statusInfo"
        WDBadgeVariant.Pending -> "surfaceGlassStrong"
        WDBadgeVariant.Success -> "statusSoftSuccess"
        WDBadgeVariant.Warning -> "statusSoftWarning"
        WDBadgeVariant.Danger -> "statusSoftDanger"
    }

    /** 对比度优先：soft 用 textOnSoft，pending 用 textBrand，schedule 用实底上的文字色。 */
    internal fun text(variant: WDBadgeVariant): String = when (variant) {
        WDBadgeVariant.Shared, WDBadgeVariant.Success, WDBadgeVariant.Warning, WDBadgeVariant.Danger -> "textOnSoft"
        WDBadgeVariant.Pending -> "textBrand"
        WDBadgeVariant.Schedule -> "textOnDark"
    }

}