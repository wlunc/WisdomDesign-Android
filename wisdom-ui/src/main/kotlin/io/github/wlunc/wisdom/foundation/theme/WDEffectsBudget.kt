package io.github.wlunc.wisdom.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Immutable

/**
 * 效果配额值对象（AR-74）；全屏同屏限制用 blurBudget / ambient 字段表达。
 * 默认：允许模糊、模糊面预算 1、允许环境动画；
 * 动画环每屏 1、触觉每次操作 1 —— 这些计数由 internal 计数器/调用约定保证。
 */
@Immutable
public class WDEffectsBudget internal constructor(
    public val allowBlur: Boolean,
    public val blurBudget: Int,
    public val allowAmbientAnimation: Boolean,
) {
    override fun equals(other: Any?): Boolean =
        other is WDEffectsBudget &&
            other.allowBlur == allowBlur &&
            other.blurBudget == blurBudget &&
            other.allowAmbientAnimation == allowAmbientAnimation

    override fun hashCode(): Int = (allowBlur.hashCode() * 31 + blurBudget) * 31 + allowAmbientAnimation.hashCode()

    public companion object {
        public val Default: WDEffectsBudget = WDEffectsBudget(true, 1, true)
    }
}

/** 当前效果预算；默认 [WDEffectsBudget.Default]。 */
public val LocalWDEffectsBudget: ProvidableCompositionLocal<WDEffectsBudget> =
    staticCompositionLocalOf { WDEffectsBudget.Default }

/** 组合期取当前预算（组件只读 local，不自行判断系统能力）。 */
@Composable
public fun rememberWDEffectsBudget(): WDEffectsBudget = LocalWDEffectsBudget.current
