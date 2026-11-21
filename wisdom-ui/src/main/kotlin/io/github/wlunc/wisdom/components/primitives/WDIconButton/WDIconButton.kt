package io.github.wlunc.wisdom.components.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.ripple
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDIconName
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.icons.WDIconSize
import io.github.wlunc.wisdom.foundation.icons.rememberWDIconResolve
import io.github.wlunc.wisdom.foundation.layout.wdTouchTarget
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.tokens.brush

/** WDIconButton 外形（B8 增量：circle / roundedSquare）. */
public enum class WDIconButtonShape {
    Circle, RoundedSquare,
}

/**
 * 图标按钮.签名 = M2-SIGNATURE-FREEZE §2.8.
 *
 * variant 复用 WDButtonVariant 子集 {Plain, Filled, Tonal, Glass, Outline}
 * （Destructive 不适用）.accessibilityLabel 必填（F-08 / 库内零文案）.
 */
@Composable
public fun WDIconButton(
    icon: WDIconName,
    accessibilityLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: WDButtonVariant = WDButtonVariant.Plain,
    size: WDButtonSize = WDButtonSize.Md,
    loading: Boolean = false,
    shape: WDIconButtonShape = WDIconButtonShape.Circle,
) {
    val colors = WDTheme.colors
    val gradients = WDTheme.gradients
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val haptics = rememberWDHaptics()

    val visualSize = when (size) {
        WDButtonSize.Sm -> WDSize.controlSm
        WDButtonSize.Md -> WDSize.controlMd
        WDButtonSize.Lg -> WDSize.controlLg
    }
    val iconSize = when (size) {
        WDButtonSize.Sm -> WDIconSize.Sm
        WDButtonSize.Md -> WDIconSize.Md
        WDButtonSize.Lg -> WDIconSize.Lg
    }
    val clipShape = when (shape) {
        WDIconButtonShape.Circle -> CircleShape
        WDIconButtonShape.RoundedSquare -> RoundedCornerShape(WDRadius.md)
    }

    // 从注入面解析图标 / 缺失时 vector=null 但保留占位尺寸（F14）.
    val resolved = rememberWDIconResolve(name = icon, size = iconSize)

    Box(
        modifier = modifier
            .wdTouchTarget()
            .clickable(
                interactionSource = source,
                indication = ripple(bounded = false),
                enabled = enabled, // U6: 仅真实 disabled 传 false；loading 不标 disabled
                role = Role.Button,
                onClick = {
                    haptics.press()
                    if (!loading) onClick() // loading 吞 action，但节点保留（U6）
                },
            )
            .semantics {
                contentDescription = accessibilityLabel
                if (loading) {
                    // loading 时 label 留在无障碍树 / 状态由调用方标签承载（库内零文案）.
                    stateDescription = accessibilityLabel
                }
            }
            .clip(clipShape)
            .then(
                when (variant) {
                    WDButtonVariant.Filled -> Modifier.background(
                        if (pressed) colors.fillPressed else colors.textBrand,
                    )
                    WDButtonVariant.Tonal -> Modifier.background(gradients.fill.brush())
                    WDButtonVariant.Glass -> Modifier.background(gradients.surface.brush())
                    WDButtonVariant.Outline -> Modifier.background(colors.surfaceCard)
                        .border(WDSpacing.s1, colors.borderHairlineStrong, clipShape)
                    WDButtonVariant.Plain -> Modifier
                    WDButtonVariant.Destructive -> Modifier // 契约子集不含 Destructive / 防御性留空
                },
            )
            .size(visualSize)
            .padding(WDSpacing.s2),
        contentAlignment = Alignment.Center,
    ) {
        val vector = resolved.vector
        if (vector != null) {
            Image(
                imageVector = vector,
                contentDescription = null, // 读屏标签由按钮语义承载 / 避免双重朗读
                modifier = Modifier
                    .size(resolved.placeholderSize)
                    .then(if (loading) Modifier.alpha(0f) else Modifier),
            )
        } else {
            // 缺图保留占位尺寸 / 不画替代图标（AR-41：不得零尺寸）.
            Box(Modifier.size(resolved.placeholderSize))
        }
    }
}