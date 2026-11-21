package io.github.wlunc.wisdom.components.primitives.WDListRow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.Dp
import io.github.wlunc.wisdom.foundation.accessibility.WDSemantics
import io.github.wlunc.wisdom.foundation.accessibility.rememberWDHaptics
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.layout.WDLayout
import io.github.wlunc.wisdom.foundation.layout.WDLayoutDensity
import io.github.wlunc.wisdom.foundation.layout.wdTouchTarget
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.typography.toTextStyle

/** U5 行盒: max(密度×缩放, natural)，不用固定高度包文字。 */
internal object WDListRowLayout {
    internal fun densityLayoutHeight(density: WDLayoutDensity): Dp = when (density) {
        WDLayoutDensity.Comfortable -> WDSize.rowHeightComfortable
        WDLayoutDensity.Compact -> WDSize.touchTargetMin
    }
    internal fun slotHeight(hasSubtitle: Boolean, hasTrailing: Boolean): Dp =
        if (hasSubtitle && hasTrailing) WDSize.touchTargetMin + WDSize.rowHeightCompact - WDSpacing.s1
        else Dp.Unspecified
    internal fun rowHeight(density: WDLayoutDensity, hasSubtitle: Boolean, hasTrailing: Boolean): Dp {
        val base = densityLayoutHeight(density)
        val slot = slotHeight(hasSubtitle, hasTrailing)
        return if (slot == Dp.Unspecified) base else maxOf(base, slot)
    }
}

/** U6 状态解析。 */
@Immutable
internal data class WDListRowVisualState(
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
}

/** saver 三条: 未知版本 / 缺项 / 非法值均返回 null，不抛。 */
public object WDListRowStateSaver {
    internal const val CURRENT_VERSION: Int = 1
    public fun save(selected: Boolean): Any = listOf(CURRENT_VERSION, selected)
    @Suppress("UNCHECKED_CAST")
    public fun restore(saved: Any?): Boolean? {
        val list = saved as? List<Any?> ?: return null
        if (list.size != 2) return null
        if (list[0] != CURRENT_VERSION) return null
        return list[1] as? Boolean
    }
}

/** WDListRow 主入口（签名 = 冻结文档 §2.9）。 */
@Composable
public fun WDListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    showsSeparator: Boolean = true,
    accessibilityLabel: String? = null,
    density: WDLayoutDensity? = null,
) {
    val resolvedDensity = density ?: WDTheme.layout.density
    val visual = WDListRowVisualState(!enabled, false, false, false, false)
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberWDHaptics()
    val contentColor = if (visual.visible == WDListRowVisualState.Visible.Disabled) {
        WDTheme.colors.textDisabled
    } else {
        WDTheme.colors.textPrimary
    }
    val label = accessibilityLabel ?: WDSemantics.join(WDSpacing.s3.value.toString(), title, subtitle.orEmpty())
    val content = @Composable {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = WDTheme.layout.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leading != null) {
                Box(modifier = Modifier.width(WDSize.iconLg)) { leading() }
                Spacer(modifier = Modifier.width(WDSpacing.s3))
            }
            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = title,
                    style = WDType.body.toTextStyle().copy(color = contentColor),
                    maxLines = Int.MAX_VALUE,
                )
                if (subtitle != null) {
                    BasicText(
                        text = subtitle,
                        style = WDType.footnote.toTextStyle().copy(color = WDTheme.colors.textSecondary),
                        maxLines = Int.MAX_VALUE,
                    )
                }
            }
            if (trailingText != null || trailing != null) {
                Spacer(modifier = Modifier.width(WDSpacing.s3))
                if (trailingText != null) {
                    BasicText(
                        text = trailingText,
                        style = WDType.footnote.toTextStyle().copy(color = contentColor),
                        maxLines = Int.MAX_VALUE,
                    )
                }
                if (trailing != null) {
                    Box(modifier = Modifier.padding(start = WDSpacing.s3)) { trailing() }
                }
            }
        }
    }
    val rowHeight = WDListRowLayout.rowHeight(
        resolvedDensity,
        subtitle != null,
        trailingText != null || trailing != null,
    )
    val baseModifier = modifier
        .fillMaxWidth()
        .height(rowHeight)
        .wdTouchTarget()
        .background(if (selected) WDTheme.colors.surfaceTint else WDTheme.colors.surfaceCardSolid)
        .semantics(mergeDescendants = true) {
            this.selected = selected
            contentDescription = label
        }
    val finalModifier = if (onClick != null && visual.clickEnabled) {
        baseModifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = Role.Button,
            onClick = { haptics.press(); onClick() },
        )
    } else if (onClick == null) {
        baseModifier
    } else {
        baseModifier.clearAndSetSemantics {}
    }
    Box(modifier = finalModifier, contentAlignment = Alignment.Center) { content() }
    if (showsSeparator) {
        Box(
            modifier = Modifier.fillMaxWidth().height(WDSpacing.s1)
                .background(WDTheme.colors.borderHairline)
                .clearAndSetSemantics {},
        )
    }
}
