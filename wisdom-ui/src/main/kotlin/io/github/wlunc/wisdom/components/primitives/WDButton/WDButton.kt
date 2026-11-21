package io.github.wlunc.wisdom.components.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.ripple
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.BasicText
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.layout.wdTouchTarget
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.typography.toTextStyle
import io.github.wlunc.wisdom.foundation.tokens.brush

/** WDButton 视觉档位（契约 WDButtonVariant 六档全量）. */
public enum class WDButtonVariant {
    Filled, Tonal, Glass, Outline, Plain, Destructive,
}

/** WDButton 控件档（契约 WDButtonSize 三档全量）. */
public enum class WDButtonSize {
    Sm, Md, Lg,
}

/**
 * 基础按钮.签名 = M2-SIGNATURE-FREEZE §2.3（沿用 SPEC §2.2①）.
 *
 * U6：disabled 由 clickable(enabled) 表达并吞输入 / loading 不使用 enabled=false / 
 * 只吞 onClick 且通过 stateDescription 留在无障碍树.
 */
@Composable
public fun WDButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: WDButtonVariant = WDButtonVariant.Filled,
    size: WDButtonSize = WDButtonSize.Md,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingLabel: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClickLabel: String? = null,
    role: Role? = Role.Button,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = WDTheme.colors
    val gradients = WDTheme.gradients
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val haptics = rememberWDHaptics()

    val visualHeight = when (size) {
        WDButtonSize.Sm -> WDSize.controlSm
        WDButtonSize.Md -> WDSize.controlMd
        WDButtonSize.Lg -> WDSize.controlLg
    }
    val horizontalPadding = when (size) {
        WDButtonSize.Sm -> WDSpacing.s3
        WDButtonSize.Md -> WDSpacing.s4
        WDButtonSize.Lg -> WDSpacing.s5
    }
    val shape = RoundedCornerShape(WDRadius.md)
    val textStyle = when (size) {
        WDButtonSize.Sm -> WDType.footnote
        WDButtonSize.Md, WDButtonSize.Lg -> WDType.callout
    }.toTextStyle()

    // 内容色在组件体内解析（默认参数表达式不在 Composable 上下文求值）.
    val contentColor = when (variant) {
        WDButtonVariant.Filled, WDButtonVariant.Destructive -> colors.textOnFill
        WDButtonVariant.Tonal, WDButtonVariant.Glass, WDButtonVariant.Plain -> colors.textOnSoft
        WDButtonVariant.Outline -> colors.textBrand
    }
    val resolvedContent = if (enabled) contentColor else colors.textDisabled

    // 内部链顺序（SPEC §2.5 终稿 LR-23）：
    // [调用方 modifier] . wdTouchTarget() . clickable(...) . clip . background . padding . [内容]
    Box(
        modifier = modifier
            .wdTouchTarget()
            .clickable(
                interactionSource = source,
                indication = ripple(),
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = role,
                onClick = {
                    // loading 吞 action 但保留 enabled 语义（U6） / 触觉在组合期取 / 点击时调（AR-75）.
                    haptics.press()
                    if (!loading) onClick()
                },
            )
            .then(
                if (loadingLabel != null && loading) {
                    Modifier.semantics { stateDescription = loadingLabel }
                } else {
                    Modifier
                },
            )
            .clip(shape)
            .then(
                when (variant) {
                    WDButtonVariant.Filled -> Modifier.background(
                        if (pressed && enabled) colors.fillPressed else colors.textBrand,
                    )
                    WDButtonVariant.Tonal -> Modifier.background(gradients.fill.brush())
                    WDButtonVariant.Glass -> Modifier.background(gradients.surface.brush())
                    WDButtonVariant.Outline -> Modifier.background(colors.surfaceCard)
                        .border(WDSpacing.s1, colors.borderHairlineStrong, shape)
                    WDButtonVariant.Plain -> Modifier
                    WDButtonVariant.Destructive -> Modifier.background(colors.statusDanger)
                },
            )
            .then(
                if (variant == WDButtonVariant.Filled || variant == WDButtonVariant.Tonal) {
                    // 1px 顶部内高光（契约同步注记：border.glass-top）
                    Modifier.border(WDSpacing.s1, colors.borderGlassTop, shape)
                } else {
                    Modifier
                },
            )
            .height(visualHeight)
            .defaultMinSize(minWidth = horizontalPadding * 2)
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WDSpacing.s2),
        ) {
            if (leadingIcon != null) {
                Box(Modifier.size(WDSize.iconMd), contentAlignment = Alignment.Center) { leadingIcon() }
            }
            BasicText(
                text = text,
                // loading 保留原标签供读屏（alpha=0f 占位 / 不移除节点）.
                modifier = if (loading) Modifier.alpha(0f) else Modifier,
                color = resolvedContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = textStyle,
            )
            if (trailingIcon != null) {
                Box(Modifier.size(WDSize.iconMd), contentAlignment = Alignment.Center) { trailingIcon() }
            }
        }
    }
}