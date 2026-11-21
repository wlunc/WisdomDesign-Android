package io.github.wlunc.wisdom.components.primitives.WDCard

/**
 * 卡片外观档位。
 *
 * 契约全量 case：elevated/outlined/glass/sunken/media。
 * [Media] 为 A-13 增量（源级 breaking），仅用于顶部通栏图片带；与 [Glass] 互斥。
 */
public enum class WDCardStyle {
    Elevated,
    Outlined,
    Glass,
    Sunken,
    Media,
}
