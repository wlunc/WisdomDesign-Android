package io.github.wlunc.wisdom.foundation.layout

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Immutable
import io.github.wlunc.wisdom.foundation.generated.WDSize

/** 密度档：两端统一的可见内容行高由 WDSize 单值键给出。 */
public enum class WDLayoutDensity { Comfortable, Compact }

/**
 * 密度值对象（值等 + companion 单例，AR-45）。
 * rowHeight = 可见内容高度（Comfortable 60 / Compact 44，两端同值）；
 * Android 的 48 布局盒由 Modifier.wdTouchTarget() 在 44 上下各加 2dp 透明内边距实现，
 * 不新增令牌键、不改写 rowHeight。
 */
@Immutable
public class WDLayout internal constructor(public val density: WDLayoutDensity) {
    /** 可见内容行高：compact=44、comfortable=60（±0.5 两端统一）。 */
    public val rowHeight: Dp
        get() = when (density) {
            WDLayoutDensity.Comfortable -> WDSize.rowHeightComfortable
            WDLayoutDensity.Compact -> WDSize.rowHeightCompact
        }

    public val cardPadding: Dp
        get() = when (density) {
            WDLayoutDensity.Comfortable -> WDSize.cardPaddingComfortable
            WDLayoutDensity.Compact -> WDSize.cardPaddingCompact
        }

    override fun equals(other: Any?): Boolean = other is WDLayout && other.density == density
    override fun hashCode(): Int = density.hashCode()

    public companion object {
        public val Comfortable: WDLayout = WDLayout(WDLayoutDensity.Comfortable)
        public val Compact: WDLayout = WDLayout(WDLayoutDensity.Compact)
    }
}

/** 当前密度；默认 Comfortable。 */
public val LocalWDLayout: ProvidableCompositionLocal<WDLayout> =
    staticCompositionLocalOf { WDLayout.Comfortable }
