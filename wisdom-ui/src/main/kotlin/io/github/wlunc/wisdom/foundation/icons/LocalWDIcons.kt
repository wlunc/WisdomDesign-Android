package io.github.wlunc.wisdom.foundation.icons

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.wlunc.wisdom.foundation.generated.WDIconName

/**
 * 调用方注入的图标集合：把 44 条 [WDIconName] 语义名映射到 [ImageVector]。
 *
 * 库内零图标资源，也不打包任何 drawable；图标由消费方提供并注入。
 * 查不到名字时 [get] 返回 null —— 调用方应保留占位尺寸，不得用其他图标顶替
 * （避免语义错位）。[mirrorsInRTL] 只是语义标记；真正的镜像以
 * [ImageVector.autoMirror] 为准，注入时请为需要 RTL 镜像的图标设置该属性。
 */
public class WDIconSet private constructor(
    private val icons: Map<WDIconName, ImageVector>,
) {
    /** 按语义名取图标；缺失时返回 null，由调用方保留占位尺寸。 */
    public operator fun get(name: WDIconName): ImageVector? = icons[name]

    /** 该语义名是否已注入图标。 */
    public operator fun contains(name: WDIconName): Boolean = icons.containsKey(name)

    /** 已注入的图标名集合（供诊断/测试）。 */
    public val names: Set<WDIconName> get() = icons.keys

    /** 在该集合基础上增量构造新集合，方便按组注入。 */
    public fun plus(other: WDIconSet): WDIconSet = WDIconSet(this.icons + other.icons)

    /** 增量构造：追加单条映射，返回新的集合。 */
    public fun plus(name: WDIconName, vector: ImageVector): WDIconSet =
        WDIconSet(this.icons + (name to vector))

    public companion object {
        /** 默认空集：任何名字都查不到，调用方保留占位尺寸。 */
        @JvmField
        public val Empty: WDIconSet = WDIconSet(emptyMap())

        /** 从映射构造集合；复制入参以免外部后续修改影响不可变语义。 */
        public fun of(icons: Map<WDIconName, ImageVector>): WDIconSet = WDIconSet(icons.toMap())

        /** 从键值对构造集合。 */
        public fun of(vararg pairs: Pair<WDIconName, ImageVector>): WDIconSet = WDIconSet(pairs.toMap())
    }
}

/**
 * 图标注入点。默认 [WDIconSet.Empty]（空集，查图即缺失）；
 * 消费方在树的上层用 CompositionLocalProvider(LocalWDIcons provides myIconSet) 注入。
 */
public val LocalWDIcons: ProvidableCompositionLocal<WDIconSet> =
    staticCompositionLocalOf { WDIconSet.Empty }
