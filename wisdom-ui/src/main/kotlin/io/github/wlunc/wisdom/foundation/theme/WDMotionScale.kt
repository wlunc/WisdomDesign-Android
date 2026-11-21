package io.github.wlunc.wisdom.foundation.theme

import androidx.compose.animation.core.MotionDurationScale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 动画缩放值（AR-56 / AR-73）。value class 上不标 @Immutable。
 * 唯一动画缩放入口 = [rememberWDMotionScale]（local × 系统 MotionDurationScale）。
 */
@JvmInline
public value class WDMotionScale(public val value: Float) {
    public companion object {
        public val Normal: WDMotionScale = WDMotionScale(1f)
        public val Off: WDMotionScale = WDMotionScale(0f)
    }
}

/** 默认 Normal；组件不得覆写系统动画设置。 */
public val LocalWDMotionScale: ProvidableCompositionLocal<WDMotionScale> =
    staticCompositionLocalOf { WDMotionScale.Normal }

/**
 * 唯一动画缩放入口：Local 值 × 系统 MotionDurationScale。
 * 属性动画路径不得调用本函数（会双重降级）；scale==0f 时调用方必须画静态替代。
 */
@Composable
public fun rememberWDMotionScale(): Float {
    val system = rememberCoroutineScope().coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    return LocalWDMotionScale.current.value * system
}
