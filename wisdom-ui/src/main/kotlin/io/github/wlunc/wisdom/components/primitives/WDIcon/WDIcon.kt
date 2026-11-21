package io.github.wlunc.wisdom.components.primitives

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.github.wlunc.wisdom.foundation.generated.WDIconName
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.icons.LocalWDIcons
import io.github.wlunc.wisdom.foundation.theme.WDTheme

/**
 * 语义名图标.签名 = M2-SIGNATURE-FREEZE §2.7（契约 params 为准的语义键入口）.
 *
 * F14：图标由调用方通过 LocalWDIcons 注入 ImageVector / 库内零图标资源.
 * 缺图时保留占位尺寸（不画替代图标 / 不得零尺寸） / 镜像以 ImageVector.autoMirror 为准 / 
 * 严禁 scale(-1f)（双重镜像反模式 #15）.
 */
@Composable
public fun WDIcon(
    icon: WDIconName,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = WDSize.iconMd,
) {
    // 从注入面按语义名取图标 / 缺图 vector=null 时保留占位尺寸（F14 / AR-41）.
    val vector = LocalWDIcons.current[icon]
    val colors = WDTheme.colors
    val resolvedTint = if (tint == Color.Unspecified) colors.textPrimary else tint
    val semanticsModifier = if (contentDescription == null) {
        // 装饰性图标：清语义 / 避免读屏朗读无意义节点.
        Modifier.clearAndSetSemantics { }
    } else {
        Modifier.semantics { this.contentDescription = contentDescription }
    }

    if (vector != null) {
        Image(
            imageVector = vector,
            contentDescription = null,
            modifier = modifier
                .size(size)
                .then(semanticsModifier),
            colorFilter = ColorFilter.tint(resolvedTint),
        )
    } else {
        // 缺图保留占位尺寸 / 不画替代图标（AR-41）.
        Box(modifier.size(size).then(semanticsModifier))
    }
}