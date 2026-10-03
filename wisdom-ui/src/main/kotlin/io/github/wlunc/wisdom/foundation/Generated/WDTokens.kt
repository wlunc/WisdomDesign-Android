// 本文件由 wisdomdesign/tools/token-build/build.js 生成，请勿手改。
// 修改请编辑 wisdomdesign/tokens/wisdom.tokens.json 后重新生成。


package io.github.wlunc.wisdom.foundation

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 语义色。界面只允许引用这一层。 */
@Immutable
public class WDColors(
    public val bgCanvas: Color,
    public val bgGrouped: Color,
    public val surfaceCard: Color,
    public val surfaceCardSolid: Color,
    public val surfaceCardSunken: Color,
    public val surfaceGlass: Color,
    public val surfaceGlassStrong: Color,
    public val textPrimary: Color,
    public val textSecondary: Color,
    public val textTertiary: Color,
    public val textOnSoft: Color,
    public val textOnFill: Color,
    public val textOnDark: Color,
    public val textBrand: Color,
    public val borderHairline: Color,
    public val borderHairlineStrong: Color,
    public val borderGlassTop: Color,
    public val fillField: Color,
    public val fillPressed: Color,
    public val statusSuccess: Color,
    public val statusInfo: Color,
    public val statusWarning: Color,
    public val statusDanger: Color,
    public val statusSoftSuccess: Color,
    public val statusSoftInfo: Color,
    public val statusSoftWarning: Color,
    public val statusSoftDanger: Color,
)

internal val wdLightColors: WDColors = WDColors(
    bgCanvas = Color(0xFFF1F8FA),
    bgGrouped = Color(0xFFE8F2F7),
    surfaceCard = Color(0xF0FFFFFF),
    surfaceCardSolid = Color(0xFFFFFFFF),
    surfaceCardSunken = Color(0xFFEDF6F9),
    surfaceGlass = Color(0xB3FFFFFF),
    surfaceGlassStrong = Color(0xDBFFFFFF),
    textPrimary = Color(0xFF0A1B24),
    textSecondary = Color(0xFF3E5764),
    textTertiary = Color(0xFF5B7784),
    textOnSoft = Color(0xFF123F5C),
    textOnFill = Color(0xFF0E3A55),
    textOnDark = Color(0xFFFFFFFF),
    textBrand = Color(0xFF1677B3),
    borderHairline = Color(0x140A1B24),
    borderHairlineStrong = Color(0x290A1B24),
    borderGlassTop = Color(0xCCFFFFFF),
    fillField = Color(0x0D0A1B24),
    fillPressed = Color(0x120A1B24),
    statusSuccess = Color(0xFF2A7F5C),
    statusInfo = Color(0xFF1677B3),
    statusWarning = Color(0xFF97651F),
    statusDanger = Color(0xFFB8564D),
    statusSoftSuccess = Color(0xFFE3F5EC),
    statusSoftInfo = Color(0xFFE1F0F8),
    statusSoftWarning = Color(0xFFFFF4DC),
    statusSoftDanger = Color(0xFFFBE7E4),
)

internal val wdDarkColors: WDColors = WDColors(
    bgCanvas = Color(0xFF0A1B2A),
    bgGrouped = Color(0xFF0D2334),
    surfaceCard = Color(0xD114283A),
    surfaceCardSolid = Color(0xFF0C2130),
    surfaceCardSunken = Color(0xFF0A1E2C),
    surfaceGlass = Color(0x8C0A1A28),
    surfaceGlassStrong = Color(0xC70C1E2E),
    textPrimary = Color(0xFFE6F1F6),
    textSecondary = Color(0xFFA4BCCB),
    textTertiary = Color(0xFF839EB0),
    textOnSoft = Color(0xFFB0D5DF),
    textOnFill = Color(0xFFDCEEF4),
    textOnDark = Color(0xFFFFFFFF),
    textBrand = Color(0xFF7EC4CF),
    borderHairline = Color(0x1AFFFFFF),
    borderHairlineStrong = Color(0x2EFFFFFF),
    borderGlassTop = Color(0x29FFFFFF),
    fillField = Color(0x12FFFFFF),
    fillPressed = Color(0x17FFFFFF),
    statusSuccess = Color(0xFF7FD3B4),
    statusInfo = Color(0xFF8FC4E8),
    statusWarning = Color(0xFFEFC683),
    statusDanger = Color(0xFFEDA79E),
    statusSoftSuccess = Color(0xFF0D2E24),
    statusSoftInfo = Color(0xFF0C2740),
    statusSoftWarning = Color(0xFF33260E),
    statusSoftDanger = Color(0xFF3A211E),
)

/** 定稿色卡的十二个色，只作对照，界面不直接用。 */
public object WDPalette {
    public val lake: Color = Color(0xFFB0D5DF)
    public val mist: Color = Color(0xFF7EC4CF)
    public val slate: Color = Color(0xFF4C8DAE)
    public val cyan: Color = Color(0xFF1685A9)
    public val blue: Color = Color(0xFF1677B3)
    public val indigo: Color = Color(0xFF2A5CAA)
    public val navy: Color = Color(0xFF065279)
    public val navyDeep: Color = Color(0xFF1E3B7A)
    public val abyss: Color = Color(0xFF080F40)
}

/** 由色卡派生、经验证对比度的填充与文字色。 */
public object WDDerived {
    public val fillLight1: Color = Color(0xFF8FCFDD)
    public val fillLight2: Color = Color(0xFF63BAD2)
    public val fillMid1: Color = Color(0xFF4FB0C8)
    public val fillMid2: Color = Color(0xFF1685A9)
    public val textOnSoft: Color = Color(0xFF123F5C)
    public val textOnFill: Color = Color(0xFF0E3A55)
}

/** 中性色阶。 */
public object WDNeutral {
    public val su0: Color = Color(0xFFFFFFFF)
    public val su50: Color = Color(0xFFF1F8FA)
    public val su100: Color = Color(0xFFE4F0F5)
    public val su200: Color = Color(0xFFD2E4EB)
    public val su300: Color = Color(0xFFB9D2DC)
    public val su400: Color = Color(0xFF8FA9B5)
    public val su500: Color = Color(0xFF6B8794)
    public val su600: Color = Color(0xFF3E5764)
    public val su700: Color = Color(0xFF2C4250)
    public val su800: Color = Color(0xFF1B2E38)
    public val su900: Color = Color(0xFF0A1B24)
    public val su950: Color = Color(0xFF061017)
}

/** 渐变。用 stops 描述，角度沿用 CSS 口径。 */
@Immutable
public class WDGradientSpec(
    public val angleDegrees: Float,
    public val stops: List<Pair<Color, Float>>,
)

/** 当前主题下的全部渐变。Compose 不做浅深自动解析，所以深浅各出一套。 */
@Immutable
public class WDGradients(
    public val surface: WDGradientSpec,
    public val fill: WDGradientSpec,
    public val mid: WDGradientSpec,
    public val destructive: WDGradientSpec,
    public val sunrise: WDGradientSpec,
)

internal val wdLightGradients: WDGradients = WDGradients(
    surface = WDGradientSpec(135f, listOf(Color(0xFFB0D5DF) to 0.00f, Color(0xFF7EC4CF) to 1.00f)),
    fill = WDGradientSpec(135f, listOf(Color(0xFF8FCFDD) to 0.00f, Color(0xFF63BAD2) to 1.00f)),
    mid = WDGradientSpec(135f, listOf(Color(0xFF4FB0C8) to 0.00f, Color(0xFF1685A9) to 1.00f)),
    destructive = WDGradientSpec(135f, listOf(Color(0xFFB8564D) to 0.00f, Color(0xFFA94A42) to 1.00f)),
    sunrise = WDGradientSpec(135f, listOf(Color(0xFFFFE7C2) to 0.00f, Color(0xFFFFD2C4) to 1.00f)),
)

internal val wdDarkGradients: WDGradients = WDGradients(
    surface = WDGradientSpec(135f, listOf(Color(0xFF123449) to 0.00f, Color(0xFF0F4055) to 1.00f)),
    fill = WDGradientSpec(135f, listOf(Color(0xFF1677B3) to 0.00f, Color(0xFF2A5CAA) to 1.00f)),
    mid = WDGradientSpec(135f, listOf(Color(0xFF4FB0C8) to 0.00f, Color(0xFF1685A9) to 1.00f)),
    destructive = WDGradientSpec(135f, listOf(Color(0xFFB8564D) to 0.00f, Color(0xFFA94A42) to 1.00f)),
    sunrise = WDGradientSpec(135f, listOf(Color(0xFFFFE7C2) to 0.00f, Color(0xFFFFD2C4) to 1.00f)),
)

/** 间距，基准 4dp。 */
public object WDSpacing {
    public val s1: Dp = 2.dp
    public val s2: Dp = 4.dp
    public val s3: Dp = 8.dp
    public val s4: Dp = 12.dp
    public val s5: Dp = 16.dp
    public val s6: Dp = 20.dp
    public val s7: Dp = 24.dp
    public val s8: Dp = 32.dp
    public val s9: Dp = 40.dp
    public val s10: Dp = 48.dp
}

/** 圆角阶梯。 */
public object WDRadius {
    public val xs: Dp = 8.dp
    public val sm: Dp = 10.dp
    public val md: Dp = 14.dp
    public val lg: Dp = 18.dp
    public val xl: Dp = 24.dp
    public val xxl: Dp = 32.dp
    public val full: Dp = 999.dp
    public val checkbox: Dp = 9.dp
    public val fab: Dp = 19.dp
}

/** 组件尺寸。 */
public object WDSize {
    public val controlSm: Dp = 32.dp
    public val controlMd: Dp = 44.dp
    public val controlLg: Dp = 52.dp
    public val touchTargetMin: Dp = 44.dp
    public val checkbox: Dp = 26.dp
    public val iconSm: Dp = 16.dp
    public val iconMd: Dp = 20.dp
    public val iconLg: Dp = 24.dp
    public val iconXl: Dp = 28.dp
    public val avatarXs: Dp = 20.dp
    public val avatarSm: Dp = 28.dp
    public val avatarMd: Dp = 32.dp
    public val avatarLg: Dp = 40.dp
    public val avatarXl: Dp = 56.dp
    public val avatarXxl: Dp = 72.dp
    public val tabbarHeight: Dp = 56.dp
    public val navbarCompact: Dp = 44.dp
    public val navbarStandard: Dp = 56.dp
}

/** 字阶。size 与 lineHeight 分开给，行高比由设计决定，不交给系统默认。 */
public data class WDTextStyle(val size: TextUnit, val lineHeight: TextUnit, val weight: FontWeight)

public object WDType {
    public val largeTitle: WDTextStyle = WDTextStyle(34.sp, 41.sp, FontWeight.Bold)
    public val title1: WDTextStyle = WDTextStyle(28.sp, 34.sp, FontWeight.Bold)
    public val title2: WDTextStyle = WDTextStyle(22.sp, 28.sp, FontWeight.SemiBold)
    public val title3: WDTextStyle = WDTextStyle(20.sp, 25.sp, FontWeight.SemiBold)
    public val headline: WDTextStyle = WDTextStyle(17.sp, 22.sp, FontWeight.SemiBold)
    public val body: WDTextStyle = WDTextStyle(17.sp, 22.sp, FontWeight.Normal)
    public val callout: WDTextStyle = WDTextStyle(16.sp, 21.sp, FontWeight.Normal)
    public val subheadline: WDTextStyle = WDTextStyle(15.sp, 20.sp, FontWeight.Normal)
    public val footnote: WDTextStyle = WDTextStyle(13.sp, 18.sp, FontWeight.Normal)
    public val caption1: WDTextStyle = WDTextStyle(12.sp, 16.sp, FontWeight.Normal)
    public val caption2: WDTextStyle = WDTextStyle(11.sp, 13.sp, FontWeight.Medium)
    public val overline: WDTextStyle = WDTextStyle(12.sp, 16.sp, FontWeight.Medium)
}

/** 动效。进场慢、出场快；位移越长时长越长。 */
public object WDMotion {
    public const val durationInstant: Int = 100
    public const val durationFast: Int = 160
    public const val durationBase: Int = 240
    public const val durationSlow: Int = 340
    public const val durationSlower: Int = 500

    public val springGentle: SpringSpec<Float> = spring<Float>(dampingRatio = 0.85f, stiffness = 380f)
    public val springSnappy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.82f, stiffness = 900f)
    public val springBouncy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.68f, stiffness = 300f)
}
