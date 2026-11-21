package io.github.wlunc.wisdom.components.primitives.WDCard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawImage
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDComponent
import io.github.wlunc.wisdom.foundation.generated.WDElevation
import io.github.wlunc.wisdom.foundation.generated.WDMotion
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDState
import io.github.wlunc.wisdom.foundation.layout.WDLayout
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.theme.rememberWDMotionScale

/** U5: 可见内容盒 = max(密度档下限, natural)，不固定高度包文字。 */
internal object WDCardLayout {
    internal fun naturalSlotHeight(hasMedia: Boolean): Dp = when {
        hasMedia -> WDSize.cardMediaMinHeight + WDLayout.Comfortable.cardPadding * 2
        else -> WDLayout.Comfortable.cardPadding * 2
    }
    internal fun minCardHeight(density: WDLayout, hasMedia: Boolean): Dp =
        maxOf(density.rowHeight, naturalSlotHeight(hasMedia))
}

/** A-13 媒体带几何。width/aspect 受 minHeight 保护。 */
internal object WDCardMediaGeometry {
    internal const val ASPECT_RATIO: Float = WDSize.cardMediaAspectRatio
    internal val MIN_HEIGHT: Dp = WDSize.cardMediaMinHeight
    internal fun heightForWidth(width: Dp): Dp {
        val aspect = WDSize.cardMediaAspectRatio
        return if (aspect <= 0f) WDSize.cardMediaMinHeight else maxOf(width.value / aspect, 0f).dp
    }
    internal fun resolvedHeight(width: Dp): Dp = maxOf(heightForWidth(width), WDSize.cardMediaMinHeight)
}

/** A-13 媒体带语义快照（纯 JVM 可断言）。 */
@Immutable
internal data class WDCardMediaState(
    internal val isReady: Boolean,
    internal val fadeAlpha: Float,
    internal val isDecorative: Boolean,
)

/** U6 状态优先级: disabled > loading > pressed > focused > hover > default。 */
@Immutable
internal data class WDCardVisualState(
    internal val disabled: Boolean,
    internal val loading: Boolean,
    internal val pressed: Boolean,
    internal val focused: Boolean,
    internal val hovered: Boolean,
) {
    internal enum class Visible { Disabled, Loading, Pressed, Focused, Hover, Default }
    internal val visible: Visible
        get() = when {
            disabled -> Visible.Disabled
            loading -> Visible.Loading
            pressed -> Visible.Pressed
            focused -> Visible.Focused
            hovered -> Visible.Hover
            else -> Visible.Default
        }
    internal val clickEnabled: Boolean get() = !disabled
    internal val contentAlpha: Float
        get() = when (visible) {
            Visible.Disabled -> WDState.disabledAlpha / 100f
            else -> 1f
        }
    internal val overlayAlpha: Float
        get() = when (visible) {
            Visible.Pressed -> WDComponent.pressOverlayAlpha
            else -> 0f
        }
}

/** WDCard 主入口（签名 = 冻结文档 §2.4）。 */
@Composable
public fun WDCard(
    style: WDCardStyle = WDCardStyle.Elevated,
    modifier: Modifier = Modifier,
    media: WDCardMedia? = null,
    action: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val hasMedia = media != null
    val effectiveStyle = if (hasMedia && style == WDCardStyle.Glass) WDCardStyle.Media else style
    val radius = if (effectiveStyle == WDCardStyle.Media) WDRadius.xl else WDRadius.md
    val shape = RoundedCornerShape(radius)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val visual = WDCardVisualState(false, false, pressed, false, false)
    val haptics = rememberWDHaptics()
    val clickableModifier = if (action != null && visual.clickEnabled) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = Role.Button,
            onClick = { haptics.press(); action() },
        )
    } else Modifier
    val elevation = when (effectiveStyle) {
        WDCardStyle.Elevated, WDCardStyle.Media -> WDElevation.level1
        else -> WDElevation.level0
    }
    val cardModifier = modifier
        .shadow(elevation = elevation, shape = shape)
        .clip(shape)
        .then(cardBackground(effectiveStyle))
        .then(cardBorder(effectiveStyle, shape))
        .then(clickableModifier)
        .drawBehind {
            val overlay = visual.overlayAlpha
            if (overlay > 0f) drawRect(color = WDTheme.colors.fillPressed, alpha = overlay)
        }
        .alpha(visual.contentAlpha)
        .heightIn(min = WDCardLayout.minCardHeight(WDTheme.layout, hasMedia))
        .wrapContentHeight(Alignment.Top)
        .semantics(mergeDescendants = true) {}
    Column(modifier = cardModifier) {
        if (media != null && effectiveStyle == WDCardStyle.Media) WDCardMediaBand(media)
        Box(modifier = Modifier.fillMaxWidth().padding(WDTheme.layout.cardPadding)) { content() }
    }
}

@Composable
private fun cardBackground(style: WDCardStyle): Modifier {
    val color = when (style) {
        WDCardStyle.Elevated, WDCardStyle.Media -> WDTheme.colors.surfaceCard
        WDCardStyle.Outlined -> WDTheme.colors.bgCanvas
        WDCardStyle.Glass -> WDTheme.colors.surfaceGlassStrong
        WDCardStyle.Sunken -> WDTheme.colors.surfaceCardSunken
    }
    return Modifier.background(color)
}

@Composable
private fun cardBorder(style: WDCardStyle, shape: RoundedCornerShape): Modifier = when (style) {
    WDCardStyle.Elevated, WDCardStyle.Outlined, WDCardStyle.Media ->
        Modifier.border(Dp.Zero, WDTheme.colors.borderHairline, shape)
    else -> Modifier
}

/** A-13: 通栏、上圆角、下不圆；ready 160ms 淡入；loading/failure 灰底无骨架。 */
@Composable
private fun WDCardMediaBand(media: WDCardMedia) {
    val ready = media.image != null
    val scale = rememberWDMotionScale()
    val animatedAlpha by animateFloatAsState(
        targetValue = if (ready) 1f else 0f,
        animationSpec = tween(durationMillis = (WDMotion.durationFast * scale).toInt().coerceAtLeast(0)),
        label = "wdCardMediaFade",
    )
    val state = WDCardMediaState(ready, animatedAlpha, media.accessibilityLabel == null)
    val topShape = RoundedCornerShape(
        topStart = WDRadius.xl, topEnd = WDRadius.xl,
        bottomStart = Dp.Zero, bottomEnd = Dp.Zero,
    )
    val bandModifier = Modifier
        .fillMaxWidth()
        .heightIn(min = WDCardMediaGeometry.MIN_HEIGHT)
        .clip(topShape)
        .background(WDTheme.colors.surfaceCardSunken)
    val semanticsModifier = if (state.isDecorative) {
        Modifier.clearAndSetSemantics {}
    } else {
        Modifier.semantics { media.accessibilityLabel?.let { contentDescription = it } }
    }
    Box(
        modifier = bandModifier
            .then(semanticsModifier)
            .graphicsLayer { alpha = state.fadeAlpha }
            .drawBehind {
                val image: ImageBitmap? = media.image
                if (image != null) {
                    val imgScale = maxOf(size.width / image.width, size.height / image.height)
                    val drawWidth = image.width * imgScale
                    val drawHeight = image.height * imgScale
                    drawImage(
                        image = image,
                        dstOffset = IntOffset(
                            ((size.width - drawWidth) / 2).toInt(),
                            ((size.height - drawHeight) / 2).toInt(),
                        ),
                        dstSize = IntSize(drawWidth.toInt(), drawHeight.toInt()),
                    )
                }
            },
    )
}
