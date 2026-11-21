package io.github.wlunc.wisdom.foundation.layout

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.roundToPx
import io.github.wlunc.wisdom.foundation.generated.WDSize

/**
 * 案 B 触控热区：可见内容 44dp 上下各加 2dp 透明内边距 → 布局盒 48dp。
 *
 * 实现要点：
 * 1. 目标高度 = max(内容测量高度, WDSize.rowHeightCompact)；再保证至少
 *    WDSize.touchTargetMin（48dp），两者相差以对称的上下内边距补齐；
 * 2. 热区 = 布局盒本身，内容垂直居中；内边距在**本节点内**，不越界
 *    覆盖相邻行（行高类组件行高即热区，禁止扩热区压到邻居）；
 * 3. 内边距是透明的（只改变布局盒与命中区，不绘制任何内容）；
 * 4. 宽度原样透传，不改水平方向命中区。
 *
 * 数值全部来自令牌：WDSize.touchTargetMin / WDSize.rowHeightCompact；不裸写 dp。
 */
public fun Modifier.wdTouchTarget(): Modifier = layout { measurable, constraints ->
    val contentPlaceable = measurable.measure(constraints)
    val minBox = WDSize.touchTargetMin.roundToPx()
    val contentMin = WDSize.rowHeightCompact.roundToPx()
    val boxHeight = maxOf(contentPlaceable.height, contentMin, minBox)
    val extra = (boxHeight - contentPlaceable.height).coerceAtLeast(0)
    val top = extra / 2
    layout(contentPlaceable.width, boxHeight) {
        contentPlaceable.placeRelative(0, top)
    }
}
