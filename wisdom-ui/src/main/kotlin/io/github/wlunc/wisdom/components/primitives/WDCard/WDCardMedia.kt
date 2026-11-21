package io.github.wlunc.wisdom.components.primitives.WDCard

import androidx.compose.ui.graphics.ImageBitmap

/**
 * A-13 的卡片媒体值对象（参数，不是槽位词表成员）。
 *
 * 库内零资源：图片位图由调用方提供；状态由调用方驱动；库不发起网络/磁盘加载。
 * - [image] 为 null：加载中或失败，一律灰底占位、无骨架、无微光；
 * - [accessibilityLabel] 为 null：图片按装饰处理（clearAndSetSemantics）；非 null：进无障碍树。
 */
public data class WDCardMedia(
    public val image: ImageBitmap?,
    public val accessibilityLabel: String? = null,
)
