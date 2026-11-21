package io.github.wlunc.wisdom.components.primitives.WDBadge

/**
 * 徽标值形态（M5 增量并集，供 WDListRow.trailing.badge 等宿主复用）。
 * 库内零文案：Label 只能是调用方传入的可朗读字符串；Count 的封顶标记由纯函数产出。
 */
public data class WDBadgeValue internal constructor(
    internal val content: Content,
) {
    internal sealed interface Content {
        data class Label(val text: String) : Content
        data class Count(val number: Int) : Content
        data object Dot : Content
    }

    public companion object {
        /** 状态胶囊：文字由调用方给。 */
        public fun label(text: String): WDBadgeValue = WDBadgeValue(Content.Label(text))

        /** 计数胶囊：count <= 0 不渲染；> 99 显示 99+。 */
        public fun count(number: Int): WDBadgeValue = WDBadgeValue(Content.Count(number))

        /** 纯装饰圆点。 */
        public val Dot: WDBadgeValue = WDBadgeValue(Content.Dot)
    }
}
