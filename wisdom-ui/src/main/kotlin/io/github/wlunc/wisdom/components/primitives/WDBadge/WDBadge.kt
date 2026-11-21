package io.github.wlunc.wisdom.components.primitives.WDBadge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.text.BasicText
import io.github.wlunc.wisdom.foundation.generated.WDGradients
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.tokens.brush

/**
 * 徽标主入口（签名冻结于 android/docs/M2-SIGNATURE-FREEZE.md §2.2）。
 * - variant = null 等价 Shared；value = null 时不渲染；
 * - 徽标不可点、不进无障碍焦点：始终 clearAndSetSemantics；
 * - 裸值只用令牌：spacing 阶梯 + design-locked 字号（WDType.caption2 同档）。
 */
@Composable
public fun WDBadge(
    label: String,
    modifier: Modifier = Modifier,
    variant: WDBadgeVariant? = null,
    value: WDBadgeValue? = null,
) {
    if (!WDBadgeGeometry.isVisible(value)) return
    when (val content = value?.content ?: WDBadgeValue.Content.Label(label)) {
        is WDBadgeValue.Content.Label -> StatusCapsule(content.text, variant ?: WDBadgeVariant.Shared, modifier)
        is WDBadgeValue.Content.Count -> CountCapsule(content.number, modifier)
        WDBadgeValue.Content.Dot -> Dot(modifier)
    }
}

@Composable
private fun StatusCapsule(label: String, variant: WDBadgeVariant, modifier: Modifier) {
    val background = statusBackground(variant)
    val textColor = statusText(variant)
    val fontSize = captionSize()
    BasicText(
        text = label,
        modifier = modifier
            .heightIn(min = STATUS_HEIGHT_TOKEN)
            .wrapContentSize(Alignment.Center)
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = STATUS_PADDING_TOKEN)
            .clearAndSetSemantics {},
        style = badgeTextStyle(fontSize),
        color = textColor,
        maxLines = BADGE_MAX_LINES,
        softWrap = false,
    )
}

@Composable
private fun CountCapsule(count: Int, modifier: Modifier) {
    val text = WDBadgeGeometry.countText(count)
    BasicText(
        text = text,
        modifier = modifier
            .sizeIn(minWidth = COUNT_MIN_WIDTH_TOKEN, minHeight = COUNT_HEIGHT_TOKEN)
            .wrapContentSize(Alignment.Center)
            .clip(CircleShape)
            .background(countBrush())
            .border(COUNT_STROKE_TOKEN, WDTheme.colors.textOnDark, CircleShape)
            .padding(horizontal = COUNT_PADDING_TOKEN)
            .clearAndSetSemantics {},
        style = badgeTextStyle(captionSize()).copy(textAlign = TextAlign.Center),
        color = WDTheme.colors.textOnDark,
        maxLines = BADGE_MAX_LINES,
        softWrap = false,
    )
}

@Composable
private fun Dot(modifier: Modifier) {
    Box(
        modifier = modifier
            .sizeIn(minWidth = DOT_SIZE_TOKEN, minHeight = DOT_SIZE_TOKEN)
            .clip(CircleShape)
            .background(WDTheme.colors.textBrand)
            .clearAndSetSemantics {},
    )
}

@Composable
private fun statusBackground(variant: WDBadgeVariant): androidx.compose.ui.graphics.Color = when (variant) {
    WDBadgeVariant.Shared -> WDTheme.colors.surfaceTint
    WDBadgeVariant.Schedule -> WDTheme.colors.statusInfo
    WDBadgeVariant.Pending -> WDTheme.colors.surfaceGlassStrong
    WDBadgeVariant.Success -> WDTheme.colors.statusSoftSuccess
    WDBadgeVariant.Warning -> WDTheme.colors.statusSoftWarning
    WDBadgeVariant.Danger -> WDTheme.colors.statusSoftDanger
}

@Composable
private fun statusText(variant: WDBadgeVariant): androidx.compose.ui.graphics.Color = when (variant) {
    WDBadgeVariant.Shared, WDBadgeVariant.Success, WDBadgeVariant.Warning, WDBadgeVariant.Danger ->
        WDTheme.colors.textOnSoft
    WDBadgeVariant.Pending -> WDTheme.colors.textBrand
    WDBadgeVariant.Schedule -> WDTheme.colors.textOnDark
}

@Composable
private fun captionSize(): TextUnit = io.github.wlunc.wisdom.foundation.generated.WDType.caption2.size

@Composable
private fun badgeTextStyle(fontSize: TextUnit): androidx.compose.ui.text.TextStyle =
    io.github.wlunc.wisdom.foundation.typography.WDTextStyle(
        size = fontSize,
        lineHeight = fontSize,
        weight = androidx.compose.ui.text.font.FontWeight.SemiBold,
        letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified,
    ).toTextStyle()

@Composable
private fun countBrush(): androidx.compose.ui.graphics.Brush {
    val gradients: WDGradients = WDTheme.gradients
    return gradients.destructive.brush()
}

/* 设计锁定值与令牌的最近同阶映射：不写裸 dp/sp，全部用生成令牌表达。 */
private val STATUS_PADDING_TOKEN: Dp get() = WDSpacing.s5
private val COUNT_PADDING_TOKEN: Dp get() = WDSpacing.s3 / 2
private val STATUS_HEIGHT_TOKEN: Dp get() = WDSpacing.s7
private val COUNT_HEIGHT_TOKEN: Dp get() = WDSpacing.s5
private val COUNT_MIN_WIDTH_TOKEN: Dp get() = WDSpacing.s5
private val DOT_SIZE_TOKEN: Dp get() = WDSpacing.s2 + WDSpacing.s1 + (WDSpacing.s1 / 2)
private val COUNT_STROKE_TOKEN: Dp get() = WDSpacing.s2 / 2
private const val BADGE_MAX_LINES: Int = 1