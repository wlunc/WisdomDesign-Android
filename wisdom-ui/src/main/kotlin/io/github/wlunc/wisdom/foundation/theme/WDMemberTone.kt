package io.github.wlunc.wisdom.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import io.github.wlunc.wisdom.foundation.generated.WDMemberToneColors
import io.github.wlunc.wisdom.foundation.generated.wdDarkMemberTones
import io.github.wlunc.wisdom.foundation.generated.wdLightMemberTones

/**
 * 成员头像四色语义名（A-11）。generated 里只有 WDMemberToneColors（8 个 fill/ink），
 * 没有本枚举；本文件手写补上，供 WDAvatar 使用。不修改 generated/。
 */
public enum class WDMemberTone {
    Tone1,
    Tone2,
    Tone3,
    Tone4,
    ;

    public companion object {
        /**
         * 按 memberID 稳定分配：FNV1a32(UTF-8, init 0x811C9DC5, prime 0x01000193, 32 位回绕) mod 4。
         * 与 iOS 同实现；fixture u_1001→Tone4 / u_1002→Tone3 / u_1003→Tone2 / u_1004→Tone1 / u_1005→Tone4。
         */
        public fun assign(memberID: String): WDMemberTone {
            var hash = 0x811C9DC5.toInt()
            for (byte in memberID.toByteArray(Charsets.UTF_8)) {
                hash = (hash xor (byte.toInt() and 0xFF)) * 0x01000193
            }
            val bucket = (hash and 0x7FFFFFFF) % entries.size
            return entries[bucket]
        }
    }
}

/** 当前 scheme 的成员四色；由 WDTheme 按 darkTheme 提供（internal，不进公开签名）。 */
internal val LocalWDMemberTones: ProvidableCompositionLocal<WDMemberToneColors> =
    staticCompositionLocalOf { wdLightMemberTones }

/** 解析当前 scheme 下该档的填充色。 */
public val WDMemberTone.fill: Color
    @Composable @ReadOnlyComposable get() {
        val tones = LocalWDMemberTones.current
        return when (this) {
            WDMemberTone.Tone1 -> tones.tone1Fill
            WDMemberTone.Tone2 -> tones.tone2Fill
            WDMemberTone.Tone3 -> tones.tone3Fill
            WDMemberTone.Tone4 -> tones.tone4Fill
        }
    }

/** 解析当前 scheme 下该档的文字色。 */
public val WDMemberTone.ink: Color
    @Composable @ReadOnlyComposable get() {
        val tones = LocalWDMemberTones.current
        return when (this) {
            WDMemberTone.Tone1 -> tones.tone1Ink
            WDMemberTone.Tone2 -> tones.tone2Ink
            WDMemberTone.Tone3 -> tones.tone3Ink
            WDMemberTone.Tone4 -> tones.tone4Ink
        }
    }
