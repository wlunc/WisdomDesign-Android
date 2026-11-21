package io.github.wlunc.wisdom.components.primitives.WDAvatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.github.wlunc.wisdom.foundation.generated.WDState
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.icons.rememberWDIconResolve
import io.github.wlunc.wisdom.foundation.theme.WDMemberTone
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.theme.fill
import io.github.wlunc.wisdom.foundation.theme.ink

/** A-11 照片值快照：位图由调用方给；无障碍文案为零文案口径下的可选注入。 */
@Immutable
public data class WDAvatarPhoto internal constructor(
    internal val image: androidx.compose.ui.graphics.ImageBitmap,
    internal val accessibilityLabel: String? = null,
) {
    public companion object {
        public fun of(
            image: androidx.compose.ui.graphics.ImageBitmap,
            accessibilityLabel: String? = null,
        ): WDAvatarPhoto = WDAvatarPhoto(image, accessibilityLabel)
    }
}

/**
 * 头像主入口（签名冻结于 android/docs/M2-SIGNATURE-FREEZE.md §2.1）。
 * - 回退链 photo → label → icon；值里没有照片时直接走回退，不画破图/占位帧；
 * - tone 默认 null = surfaceTint + textOnSoft；显式档走 WDMemberTone.fill/ink；
 * - 成员四色分配入口 = WDMemberTone.assign(memberID)（FNV1a32 mod 4）；
 * - 在线点为叠加层，不改变直径；disabled 用令牌 alpha 表达。
 */
@Composable
public fun WDAvatar(
    value: WDAvatarValue,
    modifier: Modifier = Modifier,
    size: WDAvatarSize = WDAvatarSize.Md,
    isOnline: Boolean = false,
    isDisabled: Boolean = false,
    tone: WDMemberTone? = null,
) {
    val diameter = WDAvatarGeometry.diameter(size)
    val resolved = WDAvatarGeometry.resolve(value)
    val photo = (value as? WDAvatarValue.Photo)?.photo
    val background = tone?.fill ?: WDTheme.colors.surfaceTint
    val foreground = tone?.ink ?: WDTheme.colors.textOnSoft
    val visual = WDAvatarGeometry.visualState(isDisabled = isDisabled)
    val alpha = if (visual == WDAvatarVisualState.Disabled) WDState.disabledAlpha / 100f else 1f

    Box(
        modifier = modifier
            .size(diameter)
            .clip(CircleShape)
            .alpha(alpha)
            .then(if (resolved != WDAvatarResolved.Photo) Modifier.background(background) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        when (resolved) {
            WDAvatarResolved.Photo -> {
                PhotoContent(photo, diameter, value.label, foreground)
            }
            WDAvatarResolved.Label -> {
                val text = value.label.orEmpty()
                if (text.isEmpty()) {
                    Box(modifier = Modifier.clearAndSetSemantics {})
                } else {
                    BasicText(
                        text = text,
                        modifier = Modifier.clearAndSetSemantics {},
                        style = WDAvatarGeometry.initialsTextStyle(size),
                        color = foreground,
                        maxLines = AVATAR_MAX_LINES,
                        softWrap = false,
                    )
                }
            }
            WDAvatarResolved.Icon -> {
                val icon = value.icon
                if (icon != null) {
                    val resolve = rememberWDIconResolve(icon, iconSize(size))
                    val vector = resolve.vector
                    if (vector != null) {
                        Image(
                            imageVector = vector,
                            contentDescription = null,
                            modifier = Modifier.size(resolve.placeholderSize).clearAndSetSemantics {},
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(foreground),
                            alpha = DefaultAlpha,
                        )
                    } else {
                        Box(modifier = Modifier.size(resolve.placeholderSize).clearAndSetSemantics {})
                    }
                }
            }
            WDAvatarResolved.Empty -> Box(modifier = Modifier.clearAndSetSemantics {})
        }
        if (isOnline) {
            OnlineDot(diameter, Modifier.align(Alignment.BottomEnd))
        }
    }
}

/** 照片载荷渲染：WDAvatarValue 只记 hasPhoto，真实照片由可选槽装配；缺失时回落到文字。 */
@Composable
private fun PhotoContent(
    photo: WDAvatarPhoto?,
    diameter: Dp,
    fallbackLabel: String?,
    foreground: androidx.compose.ui.graphics.Color,
) {
    if (photo != null) {
        val label = photo.accessibilityLabel
        val semantic = if (label != null) {
            Modifier.semantics { contentDescription = label }
        } else {
            Modifier.clearAndSetSemantics {}
        }
        Image(
            bitmap = photo.image,
            contentDescription = null,
            modifier = Modifier.size(diameter).clip(CircleShape).then(semantic),
            contentScale = ContentScale.Crop,
            colorFilter = null,
            alpha = DefaultAlpha,
        )
        return
    }
    // 回退链保底：hasPhoto 为真但没有位图载荷时显示调用方文字，不画破图。
    if (fallbackLabel != null) {
        BasicText(
            text = fallbackLabel,
            modifier = Modifier.clearAndSetSemantics {},
            style = androidx.compose.ui.text.TextStyle.Default,
            color = foreground,
            maxLines = AVATAR_MAX_LINES,
        )
    } else {
        Box(modifier = Modifier.size(diameter).clearAndSetSemantics {})
    }
}

@Composable
private fun OnlineDot(diameter: Dp, modifier: Modifier) {
    val dot = AVATAR_ONLINE_DOT
    Box(
        modifier = modifier
            .offset(x = -diameter / ONLINE_OFFSET_DIVISOR, y = diameter / ONLINE_OFFSET_DIVISOR)
            .size(dot)
            .clip(CircleShape)
            .background(WDTheme.colors.statusSuccess)
            .border(AVATAR_RING_TOKEN, WDTheme.colors.surfaceCardSolid, CircleShape)
            .clearAndSetSemantics {},
    )
}

private fun iconSize(size: WDAvatarSize): io.github.wlunc.wisdom.foundation.icons.WDIconSize = when (size) {
    WDAvatarSize.Xs, WDAvatarSize.Sm -> io.github.wlunc.wisdom.foundation.icons.WDIconSize.Sm
    WDAvatarSize.Md -> io.github.wlunc.wisdom.foundation.icons.WDIconSize.Md
    WDAvatarSize.Lg, WDAvatarSize.Xl -> io.github.wlunc.wisdom.foundation.icons.WDIconSize.Lg
}

private val AVATAR_RING_TOKEN: Dp get() = WDSize.avatarRing
private val AVATAR_ONLINE_DOT: Dp get() = WDSpacing.s5 / 2
private const val ONLINE_OFFSET_DIVISOR: Int = 4
private const val AVATAR_MAX_LINES: Int = 1