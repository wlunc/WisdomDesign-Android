package io.github.wlunc.wisdom.foundation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.wlunc.wisdom.foundation.generated.WDColors
import io.github.wlunc.wisdom.foundation.generated.WDGradients
import io.github.wlunc.wisdom.foundation.generated.wdDarkColors
import io.github.wlunc.wisdom.foundation.generated.wdDarkGradients
import io.github.wlunc.wisdom.foundation.generated.wdLightColors
import io.github.wlunc.wisdom.foundation.generated.wdLightGradients
import io.github.wlunc.wisdom.foundation.generated.wdDarkMemberTones
import io.github.wlunc.wisdom.foundation.generated.wdLightMemberTones
import io.github.wlunc.wisdom.foundation.icons.LocalWDIcons
import io.github.wlunc.wisdom.foundation.icons.WDIconSet
import io.github.wlunc.wisdom.foundation.layout.LocalWDLayout
import io.github.wlunc.wisdom.foundation.layout.WDLayout

/** 当前语义色。组件一律通过 WDTheme.colors 取值，不直接引用生成常量。 */
public val LocalWDColors: ProvidableCompositionLocal<WDColors> =
    staticCompositionLocalOf { wdLightColors }

/** 当前渐变。Compose 不做浅深自动解析，所以和颜色一样由主题提供。 */
public val LocalWDGradients: ProvidableCompositionLocal<WDGradients> =
    staticCompositionLocalOf { wdLightGradients }

/** 设计令牌入口。对标 MaterialTheme，但只承载 Wisdom 自己的令牌。 */
public object WDTheme {
    public val colors: WDColors
        @Composable @ReadOnlyComposable get() = LocalWDColors.current

    public val gradients: WDGradients
        @Composable @ReadOnlyComposable get() = LocalWDGradients.current

    public val layout: WDLayout
        @Composable @ReadOnlyComposable get() = LocalWDLayout.current

    public val budget: WDEffectsBudget
        @Composable @ReadOnlyComposable get() = LocalWDEffectsBudget.current
}

/**
 * 保留既有入口：只切深浅，颜色/渐变走预设 scheme。
 * 完整重载见下方带 colors 的版本（WDTheme(colors = …) 换 scheme）。
 */
@Composable
public fun WDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    WDTheme(
        darkTheme = darkTheme,
        colors = if (darkTheme) wdDarkColors else wdLightColors,
        gradients = if (darkTheme) wdDarkGradients else wdLightGradients,
        content = content,
    )
}

/**
 * 换 scheme 入口：colors/gradients 由调用方传入；其余基础 local 也有默认值。
 * Material3 桥接在 WDMaterialScheme.kt（全库唯一 m3 import 点）。
 */
@Composable
public fun WDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: WDColors = if (darkTheme) wdDarkColors else wdLightColors,
    gradients: WDGradients = if (darkTheme) wdDarkGradients else wdLightGradients,
    layout: WDLayout = WDLayout.Comfortable,
    budget: WDEffectsBudget = WDEffectsBudget.Default,
    icons: WDIconSet = WDIconSet.Empty,
    motionScale: WDMotionScale = WDMotionScale.Normal,
    content: @Composable () -> Unit,
) {
    // AR-45①：颜色方案随 colors/darkTheme 变化才重算，不因 WDTheme 自身重组重算。
    val materialColors = remember(colors, darkTheme) { colors }
    CompositionLocalProvider(
        LocalWDColors provides materialColors,
        LocalWDGradients provides gradients,
        LocalWDMemberTones provides (if (darkTheme) wdDarkMemberTones else wdLightMemberTones),
        LocalWDLayout provides layout,
        LocalWDEffectsBudget provides budget,
        LocalWDIcons provides icons,
        LocalWDMotionScale provides motionScale,
    ) {
        WDMaterialTheme(colors = materialColors, darkTheme = darkTheme, content = content)
    }
}
