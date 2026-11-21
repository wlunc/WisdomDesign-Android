package io.github.wlunc.wisdom.components.primitives.WDAvatar

import androidx.compose.ui.graphics.ImageBitmap
import io.github.wlunc.wisdom.foundation.generated.WDIconName

/**
 * 头像值形态（A-11 照片 → B2 文字/图标回退链）。
 * 库内零资源零网络：图片是否就绪由调用方判定；图未就绪时直接构造 label/icon 形态，不画占位帧。
 * 相等语义只看「有没有照片」+ 回退文字/图标，不比较照片位图内容。
 */
public sealed class WDAvatarValue internal constructor(
    internal val icon: WDIconName?,
    internal val label: String?,
    internal val hasPhoto: Boolean,
) {
    /** 纯文字形态。 */
    public class Label internal constructor(internal val text: String) : WDAvatarValue(null, text, false)

    /** 图标形态。 */
    public class IconValue internal constructor(internal val iconName: WDIconName) : WDAvatarValue(iconName, null, false)

    /** 照片形态：hasPhoto 只做回退链开关，载荷在 [photo] 中。 */
    public class Photo internal constructor(
        internal val photo: WDAvatarPhoto,
        label: String?,
        icon: WDIconName?,
    ) : WDAvatarValue(icon, label, true)

    public companion object {
        public fun icon(icon: WDIconName): WDAvatarValue = IconValue(icon)
        public fun label(text: String): WDAvatarValue = Label(text)

        /** 照片就绪时构造；label/icon 是回退内容。 */
        public fun photo(
            image: ImageBitmap,
            label: String? = null,
            icon: WDIconName? = null,
        ): WDAvatarValue = Photo(WDAvatarPhoto.of(image), label, icon)

        /** 取图失败的调用入口：直接给 image=null，语义等同纯回退形态。 */
        public fun photoOrFallback(
            image: ImageBitmap?,
            label: String? = null,
            icon: WDIconName? = null,
        ): WDAvatarValue = if (image != null) photo(image, label, icon) else fallback(icon, label)

        internal fun fallback(icon: WDIconName?, label: String?): WDAvatarValue =
            if (label != null) Label(label) else if (icon != null) IconValue(icon) else WDAvatarValue(null, null, false)
    }
}
