package io.github.wlunc.wisdom.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

/** 当前语义色。组件一律通过 WDTheme.colors 取值，不直接引用生成常量。 */
public val LocalWDColors: ProvidableCompositionLocal<WDColors> =
    staticCompositionLocalOf { wdLightColors }

/** 设计令牌入口。对标 MaterialTheme，但只承载 Wisdom 自己的令牌。 */
public object WDTheme {
    public val colors: WDColors
        @Composable @ReadOnlyComposable get() = LocalWDColors.current
}

/**
 * 组件库主题入口。会把 Wisdom 的语义色桥接到 Material 3 的 ColorScheme，
 * 这样 Material 组件在和 Wisdom 组件混用时不会跳色。
 */
@Composable
public fun WDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) wdDarkColors else wdLightColors
    CompositionLocalProvider(LocalWDColors provides colors) {
        MaterialTheme(
            colorScheme = if (darkTheme) colors.toDarkMaterialScheme() else colors.toLightMaterialScheme(),
            content = content,
        )
    }
}

/** 把设计字阶转成 Compose 的 TextStyle。 */
public fun WDTextStyle.toTextStyle(): TextStyle = TextStyle(
    fontSize = size,
    lineHeight = lineHeight,
    fontWeight = weight,
)

private fun WDColors.toLightMaterialScheme() = lightColorScheme(
    primary = textBrand,
    onPrimary = textOnDark,
    background = bgCanvas,
    onBackground = textPrimary,
    surface = surfaceCardSolid,
    onSurface = textPrimary,
    surfaceVariant = surfaceCardSunken,
    onSurfaceVariant = textSecondary,
    outline = borderHairlineStrong,
    error = statusDanger,
)

private fun WDColors.toDarkMaterialScheme() = darkColorScheme(
    primary = textBrand,
    onPrimary = bgCanvas,
    background = bgCanvas,
    onBackground = textPrimary,
    surface = surfaceCardSolid,
    onSurface = textPrimary,
    surfaceVariant = surfaceCardSunken,
    onSurfaceVariant = textSecondary,
    outline = borderHairlineStrong,
    error = statusDanger,
)
