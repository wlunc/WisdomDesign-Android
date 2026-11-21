package io.github.wlunc.wisdom.foundation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.github.wlunc.wisdom.foundation.generated.WDColors

/**
 * 全库唯一的 material3 import 点（G1-③）。
 * 把 Wisdom 语义色桥接到 m3 ColorScheme，仅供 WDTheme 内部使用；
 * 公开签名不得出现 m3 类型（所有函数都是 internal）。
 */
internal fun WDColors.toLightMaterialScheme(): ColorScheme = lightColorScheme(
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

internal fun WDColors.toDarkMaterialScheme(): ColorScheme = darkColorScheme(
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

/** WDTheme 内部桥接：把 m3 类型完全挡在本文件内（G1-③ / G9）。 */
@Composable
internal fun WDMaterialTheme(
    colors: WDColors,
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) colors.toDarkMaterialScheme() else colors.toLightMaterialScheme(),
        content = content,
    )
}

