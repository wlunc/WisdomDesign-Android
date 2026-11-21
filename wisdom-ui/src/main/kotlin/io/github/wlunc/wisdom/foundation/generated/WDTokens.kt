// 本文件由 wisdomdesign/tools/token-build/build.js 生成，请勿手改。
// 修改请编辑 wisdomdesign/tokens/wisdom.tokens.json 后重新生成。
// tokens v1.1.0 · sha256:953044a85a8e

package io.github.wlunc.wisdom.foundation.generated

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 语义色。界面只允许引用这一层。U12 = 32 槽位。 */
@Immutable
public class WDColors internal constructor(
    public val bgCanvas: Color,
    public val bgGrouped: Color,
    public val surfaceCard: Color,
    public val surfaceCardSolid: Color,
    public val surfaceCardSunken: Color,
    public val surfaceTint: Color,
    public val surfaceGlass: Color,
    public val surfaceGlassStrong: Color,
    public val textPrimary: Color,
    public val textSecondary: Color,
    public val textTertiary: Color,
    public val textDisabled: Color,
    public val textOnLightPrimary: Color,
    public val textOnLightSecondary: Color,
    public val textOnLightTertiary: Color,
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

/** scheme light（appearance = light）。 */
internal val wdLightColors: WDColors = WDColors(
    bgCanvas = Color(0xFFF1F8FA),
    bgGrouped = Color(0xFFE8F2F7),
    surfaceCard = Color(0xF0FFFFFF),
    surfaceCardSolid = Color(0xFFFFFFFF),
    surfaceCardSunken = Color(0xFFEDF6F9),
    surfaceTint = Color(0xFFD9E9F3),
    surfaceGlass = Color(0xB3FFFFFF),
    surfaceGlassStrong = Color(0xDBFFFFFF),
    textPrimary = Color(0xFF0A1B24),
    textSecondary = Color(0xFF3E5764),
    textTertiary = Color(0xFF4C616D),
    textDisabled = Color(0xFF6A7B85),
    textOnLightPrimary = Color(0xFF0A1B24),
    textOnLightSecondary = Color(0xFF3E5764),
    textOnLightTertiary = Color(0xFF4C616D),
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

/** scheme dark（appearance = dark）。 */
internal val wdDarkColors: WDColors = WDColors(
    bgCanvas = Color(0xFF0A1B26),
    bgGrouped = Color(0xFF0D2331),
    surfaceCard = Color(0xD1132836),
    surfaceCardSolid = Color(0xFF0C212D),
    surfaceCardSunken = Color(0xFF0A1E2A),
    surfaceTint = Color(0xFF123348),
    surfaceGlass = Color(0x9E0C202C),
    surfaceGlassStrong = Color(0xC70C202C),
    textPrimary = Color(0xFFE6F1F6),
    textSecondary = Color(0xFFA4BCCB),
    textTertiary = Color(0xFF839EB0),
    textDisabled = Color(0xFF6E8798),
    textOnLightPrimary = Color(0xFF0A1B24),
    textOnLightSecondary = Color(0xFF3E5764),
    textOnLightTertiary = Color(0xFF4C616D),
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

/** 渐变。用 stops 描述，角度沿用 CSS 口径。F33：WDGradientSpec 保留公开构造器。 */
@Immutable
public class WDGradientSpec(
    public val angleDegrees: Float,
    public val stops: List<Pair<Color, Float>>,
)

/** 当前主题下的全部渐变。Compose 不做浅深自动解析，所以每套 scheme 各出一套。 */
@Immutable
public class WDGradients internal constructor(
    public val surface: WDGradientSpec,
    public val fill: WDGradientSpec,
    public val mid: WDGradientSpec,
    public val destructive: WDGradientSpec,
    public val sunrise: WDGradientSpec,
)

/** scheme light 的渐变。 */
internal val wdLightGradients: WDGradients = WDGradients(
    surface = WDGradientSpec(135f, listOf(Color(0xFFB0D5DF) to 0.00f, Color(0xFF7EC4CF) to 1.00f)),
    fill = WDGradientSpec(135f, listOf(Color(0xFF8FCFDD) to 0.00f, Color(0xFF63BAD2) to 1.00f)),
    mid = WDGradientSpec(135f, listOf(Color(0xFF4FB0C8) to 0.00f, Color(0xFF1685A9) to 1.00f)),
    destructive = WDGradientSpec(135f, listOf(Color(0xFFB8564D) to 0.00f, Color(0xFFA94A42) to 1.00f)),
    sunrise = WDGradientSpec(135f, listOf(Color(0xFFFFE7C2) to 0.00f, Color(0xFFFFD2C4) to 1.00f)),
)

/** scheme dark 的渐变。 */
internal val wdDarkGradients: WDGradients = WDGradients(
    surface = WDGradientSpec(135f, listOf(Color(0xFF123449) to 0.00f, Color(0xFF0F4055) to 1.00f)),
    fill = WDGradientSpec(135f, listOf(Color(0xFF1677B3) to 0.00f, Color(0xFF2A5CAA) to 1.00f)),
    mid = WDGradientSpec(135f, listOf(Color(0xFF4FB0C8) to 0.00f, Color(0xFF1685A9) to 1.00f)),
    destructive = WDGradientSpec(135f, listOf(Color(0xFFB8564D) to 0.00f, Color(0xFFA94A42) to 1.00f)),
    sunrise = WDGradientSpec(135f, listOf(Color(0xFFFFE7C2) to 0.00f, Color(0xFFFFD2C4) to 1.00f)),
)

/** 成员头像四色（A-11）。组件私有调色板，不进 `WDColors`（U12 槽位冻结在 32）。 */
@Immutable
public class WDMemberToneColors(
    public val tone1Fill: Color, public val tone1Ink: Color,
    public val tone2Fill: Color, public val tone2Ink: Color,
    public val tone3Fill: Color, public val tone3Ink: Color,
    public val tone4Fill: Color, public val tone4Ink: Color,
)

/** scheme light 的成员四色。 */
internal val wdLightMemberTones: WDMemberToneColors = WDMemberToneColors(
    tone1Fill = Color(0xFFB0D5DF), tone1Ink = Color(0xFF0A1B24),
    tone2Fill = Color(0xFF7EC4CF), tone2Ink = Color(0xFF0A1B24),
    tone3Fill = Color(0xFF1A7BA3), tone3Ink = Color(0xFFFFFFFF),
    tone4Fill = Color(0xFF065279), tone4Ink = Color(0xFFFFFFFF),
)

/** scheme dark 的成员四色。 */
internal val wdDarkMemberTones: WDMemberToneColors = WDMemberToneColors(
    tone1Fill = Color(0xFF97CBD8), tone1Ink = Color(0xFF0A1B24),
    tone2Fill = Color(0xFF6BBBC7), tone2Ink = Color(0xFF0A1B24),
    tone3Fill = Color(0xFF4193B4), tone3Ink = Color(0xFF0A1B24),
    tone4Fill = Color(0xFF356997), tone4Ink = Color(0xFFFFFFFF),
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

/** 组件尺寸。U8：触控只生成本端常量（Android 48）；`$type: number` 无单位（Float），`$type: duration` 为毫秒（Int）。 */
public object WDSize {
    public val controlSm: Dp = 32.dp
    public val controlMd: Dp = 44.dp
    public val controlLg: Dp = 52.dp
    public val touchTargetMin: Dp = 48.dp
    public val rowHeightComfortable: Dp = 60.dp
    public val rowHeightCompact: Dp = 44.dp
    public val cardPaddingComfortable: Dp = 16.dp
    public val cardPaddingCompact: Dp = 12.dp
    public val fieldHeight: Dp = 46.dp
    public val fieldMinWidth: Dp = 190.dp
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
    public val avatarRing: Dp = 2.dp
    public val cardMediaAspectRatio: Float = 1.78f
    public val cardMediaMinHeight: Dp = 96.dp
    public val tooltipHeight: Dp = 28.dp
    public val tooltipMaxWidth: Dp = 200.dp
    public val tooltipOffset: Dp = 8.dp
    public val tooltipPressDuration: Int = 500
    public val sheetDetentHalf: Float = 0.5f
    public val sheetDetentLarge: Float = 0.92f
    public val sheetMaxWidth: Dp = 480.dp
    public val sheetCornerRadius: Dp = 32.dp
    public val sheetHandleWidth: Dp = 36.dp
    public val sheetHandleHeight: Dp = 5.dp
    public val sheetHandleTopOffset: Dp = 8.dp
    public val tabbarHeight: Dp = 56.dp
    public val navbarCompact: Dp = 44.dp
    public val navbarStandard: Dp = 56.dp
}

/** 字阶。size 与 lineHeight 分开给，行高比由设计决定，不交给系统默认。 */
@Immutable
public class WDTextStyle internal constructor(
    public val size: TextUnit,
    public val lineHeight: TextUnit,
    public val weight: FontWeight,
    public val letterSpacing: TextUnit,
) {
    /** 派生只读：真源是绝对行盒高，比值不单独存令牌（TYP-1）。 */
    public val lineHeightRatio: Float get() = if (size.value == 0f) 0f else lineHeight.value / size.value

    override fun equals(other: Any?): Boolean =
        other is WDTextStyle &&
            other.size == size &&
            other.lineHeight == lineHeight &&
            other.weight == weight &&
            other.letterSpacing == letterSpacing

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + lineHeight.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + letterSpacing.hashCode()
        return result
    }

    override fun toString(): String =
        "WDTextStyle(size=$size, lineHeight=$lineHeight, weight=$weight, letterSpacing=$letterSpacing)"
}

public object WDType {
    public val largeTitle: WDTextStyle = WDTextStyle(34.sp, 41.sp, FontWeight.Bold, 0.sp)
    public val title1: WDTextStyle = WDTextStyle(28.sp, 34.sp, FontWeight.Bold, 0.sp)
    public val title2: WDTextStyle = WDTextStyle(22.sp, 28.sp, FontWeight.SemiBold, 0.sp)
    public val title3: WDTextStyle = WDTextStyle(20.sp, 25.sp, FontWeight.SemiBold, 0.sp)
    public val headline: WDTextStyle = WDTextStyle(17.sp, 22.sp, FontWeight.SemiBold, 0.sp)
    public val body: WDTextStyle = WDTextStyle(17.sp, 22.sp, FontWeight.Normal, 0.sp)
    public val callout: WDTextStyle = WDTextStyle(16.sp, 21.sp, FontWeight.Normal, 0.sp)
    public val subheadline: WDTextStyle = WDTextStyle(15.sp, 20.sp, FontWeight.Normal, 0.sp)
    public val footnote: WDTextStyle = WDTextStyle(13.sp, 18.sp, FontWeight.Normal, 0.sp)
    public val caption1: WDTextStyle = WDTextStyle(12.sp, 16.sp, FontWeight.Normal, 0.sp)
    public val caption2: WDTextStyle = WDTextStyle(11.sp, 13.sp, FontWeight.Medium, 0.sp)
    public val overline: WDTextStyle = WDTextStyle(12.sp, 16.sp, FontWeight.Medium, 0.6.sp)
}

/** 高度。每层阴影按顺序叠加，禁止单层重阴影。出口档位 level0…level3 + brand（近似映射见 F43）。 */
@Immutable
public class WDElevationLayer internal constructor(
    public val x: Dp,
    public val y: Dp,
    public val blur: Dp,
    public val color: Color,
)

@Immutable
public class WDElevationSpec internal constructor(
    public val layers: List<WDElevationLayer>,
)

public object WDElevation {
    public val level0: WDElevationSpec = WDElevationSpec(emptyList())
    public val level1: WDElevationSpec = WDElevationSpec(
        listOf(
            WDElevationLayer(0.dp, 1.dp, 2.dp, Color(0x0A0A3C64)),
            WDElevationLayer(0.dp, 6.dp, 16.dp, Color(0x0F0A3C64))
        ),
    )
    public val level2: WDElevationSpec = WDElevationSpec(
        listOf(
            WDElevationLayer(0.dp, 2.dp, 6.dp, Color(0x0F0A3C64)),
            WDElevationLayer(0.dp, 14.dp, 32.dp, Color(0x170A3C64))
        ),
    )
    public val level3: WDElevationSpec = WDElevationSpec(
        listOf(
            WDElevationLayer(0.dp, 8.dp, 20.dp, Color(0x170A3C64)),
            WDElevationLayer(0.dp, 28.dp, 56.dp, Color(0x210A3C64))
        ),
    )
    public val brand: WDElevationSpec = WDElevationSpec(
        listOf(
            WDElevationLayer(0.dp, 6.dp, 16.dp, Color(0x2E1677B3))
        ),
    )
}

/** 组件私有描画/节奏参数。12 键（AR-61）；交互动效时长在 WDMotion，禁止互相搬运。 */
public object WDComponent {
    public val pressScale: Float = 0.97f
    public val pressOverlayAlpha: Float = 0.06f
    public val checkboxPressScale: Float = 0.9f
    public val checkboxDrawMillis: Int = 260
    public val checkboxGlowMillis: Int = 400
    public val switchKnobMillis: Int = 300
    public val switchTrackMillis: Int = 260
    public val progressEaseOutMillis: Int = 3200
    public val toastInMillis: Int = 320
    public val listStaggerMillis: Int = 20
    public val skeletonShimmerMillis: Int = 2200
    public val layoutBreakFontScale: Float = 1.3f
}

/** 动效。进场慢、出场快；位移越长时长越长。 */
/** 弹簧 canonical = response + dampingRatio；stiffness 由 μ = 1 派生：stiffness = μ·(2π/response)²。 */
public object WDMotion {
    public val durationInstant: Int = 100
    public val durationFast: Int = 160
    public val durationBase: Int = 240
    public val durationSlow: Int = 340
    public val durationSlower: Int = 500
    public val durationReduced: Int = 150

    public val springGentle: SpringSpec<Float> = spring<Float>(dampingRatio = 0.85f, stiffness = 246.74f)
    public val springSnappy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.82f, stiffness = 503.55f)
    public val springBouncy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.68f, stiffness = 223.80f)
}

/** 交互状态视觉值（U6 / F45）。百分数按 0–100 原样给，使用时除 100。 */
public object WDState {
    public val hoverBrightness: Float = 98f
    public val pressedBrightness: Float = 96f
    public val focusRingWidth: Dp = 3.dp
    public val focusRingAlpha: Float = 32f
    public val disabledAlpha: Float = 40f
}
