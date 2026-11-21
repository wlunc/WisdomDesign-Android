package io.github.wlunc.wisdom.components.primitives.WDAvatar

import androidx.compose.runtime.Immutable
import io.github.wlunc.wisdom.foundation.generated.WDSize

/** A-11 回退链结果；JVM 可测，不持有 Compose 状态。 */
@Immutable
public enum class WDAvatarResolved { Photo, Label, Icon, Empty }

/** B2 五档几何：直径全部走令牌，禁止 WDAvatarXxl（不在契约）。 */
internal object WDAvatarGeometry {
    internal fun diameter(size: WDAvatarSize): androidx.compose.ui.unit.Dp = when (size) {
        WDAvatarSize.Xs -> WDSize.avatarXs
        WDAvatarSize.Sm -> WDSize.avatarSm
        WDAvatarSize.Md -> WDSize.avatarMd
        WDAvatarSize.Lg -> WDSize.avatarLg
        WDAvatarSize.Xl -> WDSize.avatarXl
    }

    /** 设计锁定首字号不走动态字体：用令牌字阶近似四档（同一字阶集合，不裸 sp）。 */
    internal fun initialsTextStyle(size: WDAvatarSize): androidx.compose.ui.text.TextStyle =
        when (size) {
            WDAvatarSize.Xs, WDAvatarSize.Sm -> io.github.wlunc.wisdom.foundation.generated.WDType.caption2
            WDAvatarSize.Md -> io.github.wlunc.wisdom.foundation.generated.WDType.caption1
            WDAvatarSize.Lg -> io.github.wlunc.wisdom.foundation.generated.WDType.footnote
            WDAvatarSize.Xl -> io.github.wlunc.wisdom.foundation.generated.WDType.subheadline
        }.let { io.github.wlunc.wisdom.foundation.typography.WDTextStyle(
            size = it.size,
            lineHeight = it.lineHeight,
            weight = it.weight,
            letterSpacing = it.letterSpacing,
        ).toTextStyle() }

    /** 硬顺序：photo → label → icon → empty。 */
    internal fun resolve(value: WDAvatarValue): WDAvatarResolved = when {
        value.hasPhoto -> WDAvatarResolved.Photo
        value.label != null -> WDAvatarResolved.Label
        value.icon != null -> WDAvatarResolved.Icon
        else -> WDAvatarResolved.Empty
    }

    /** U6：头像无 loading 入参；disabled 最高，pressed 维度由调用方 pointer 决定。 */
    internal fun visualState(
        isDisabled: Boolean = false,
        pressed: Boolean = false,
        focused: Boolean = false,
        hovered: Boolean = false,
    ): WDAvatarVisualState = when {
        isDisabled -> WDAvatarVisualState.Disabled
        pressed -> WDAvatarVisualState.Pressed
        focused -> WDAvatarVisualState.Focused
        hovered -> WDAvatarVisualState.Hover
        else -> WDAvatarVisualState.Default
    }
}

internal enum class WDAvatarVisualState { Disabled, Pressed, Focused, Hover, Default }
