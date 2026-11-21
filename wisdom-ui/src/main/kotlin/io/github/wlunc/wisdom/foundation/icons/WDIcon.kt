package io.github.wlunc.wisdom.foundation.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import io.github.wlunc.wisdom.foundation.generated.WDIconName
import io.github.wlunc.wisdom.foundation.generated.WDSize

/** 图标尺寸档（对应设计规范 16/20/24/28 四档）。 */
public enum class WDIconSize {
    Sm,
    Md,
    Lg,
    Xl,
}

/** 把档位解析为令牌尺寸；禁止在组件里裸写 dp。 */
public val WDIconSize.dp: Dp
    @Composable @ReadOnlyComposable get() = when (this) {
        WDIconSize.Sm -> WDSize.iconSm
        WDIconSize.Md -> WDSize.iconMd
        WDIconSize.Lg -> WDSize.iconLg
        WDIconSize.Xl -> WDSize.iconXl
    }

/**
 * 图标解析结果：由注入面给组件本体的唯一输入。
 * [vector] 为 null 表示缺失 —— 组件必须保留 [placeholderSize] 的占位尺寸，
 * 不得画替代图标；[mirrorsInRTL] 仅作语义标记，实际镜像看 [ImageVector.autoMirror]。
 */
public data class WDIconResolve(
    public val name: WDIconName,
    public val vector: ImageVector?,
    public val placeholderSize: Dp,
    public val mirrorsInRTL: Boolean,
) {
    /** 是否成功注入了图标。 */
    public val present: Boolean get() = vector != null
}

/**
 * WDIcon 组件本体的注入辅助：把语义名解析成 [WDIconResolve]。
 * 只读 LocalWDIcons，不提供任何默认图标，也不构造绘制内容（组件本体由 ui-a 实现）。
 */
@Composable @ReadOnlyComposable
public fun rememberWDIconResolve(name: WDIconName, size: WDIconSize): WDIconResolve {
    val vector = LocalWDIcons.current[name]
    return WDIconResolve(
        name = name,
        vector = vector,
        placeholderSize = size.dp,
        mirrorsInRTL = name.mirrorsInRTL,
    )
}
