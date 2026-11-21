package io.github.wlunc.wisdom.components.WDAvatar

import io.github.wlunc.wisdom.components.primitives.WDAvatar.WDAvatarGeometry
import io.github.wlunc.wisdom.components.primitives.WDAvatar.WDAvatarResolved
import io.github.wlunc.wisdom.components.primitives.WDAvatar.WDAvatarSize
import io.github.wlunc.wisdom.components.primitives.WDAvatar.WDAvatarValue
import io.github.wlunc.wisdom.components.primitives.WDAvatar.WDAvatarVisualState
import io.github.wlunc.wisdom.foundation.generated.WDIconName
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.theme.WDMemberTone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** WDAvatar 的 JVM 断言：五档直径、回退链、tone 分配 fixture、状态优先级。 */
class WDAvatarTest {
    @Test
    fun `五档全量且直径逐档走令牌`() {
        assertEquals(listOf("xs", "sm", "md", "lg", "xl"), WDAvatarSize.entries.map { it.name.lowercase() })
        assertEquals(WDSize.avatarXs, WDAvatarGeometry.diameter(WDAvatarSize.Xs))
        assertEquals(WDSize.avatarSm, WDAvatarGeometry.diameter(WDAvatarSize.Sm))
        assertEquals(WDSize.avatarMd, WDAvatarGeometry.diameter(WDAvatarSize.Md))
        assertEquals(WDSize.avatarLg, WDAvatarGeometry.diameter(WDAvatarSize.Lg))
        assertEquals(WDSize.avatarXl, WDAvatarGeometry.diameter(WDAvatarSize.Xl))
    }

    @Test
    fun `回退链 photo 到 label 到 icon 到 empty`() {
        val label = WDAvatarValue.label("妈")
        val icon = WDAvatarValue.icon(WDIconName.Profile)
        assertEquals(WDAvatarResolved.Photo, WDAvatarGeometry.resolve(WDAvatarValue.photo(androidx.compose.ui.graphics.ImageBitmap(1, 1), "妈")))
        assertEquals(WDAvatarResolved.Label, WDAvatarGeometry.resolve(label))
        assertEquals(WDAvatarResolved.Icon, WDAvatarGeometry.resolve(icon))
        assertEquals(label, WDAvatarValue.photoOrFallback(null, label = "妈"))
        assertNotEquals(label, WDAvatarValue.icon(WDIconName.Profile))
    }

    @Test
    fun `tone 分配 fixture 与撞色规则`() {
        val expected = mapOf(
            "u_1001" to WDMemberTone.Tone4,
            "u_1002" to WDMemberTone.Tone3,
            "u_1003" to WDMemberTone.Tone2,
            "u_1004" to WDMemberTone.Tone1,
            "u_1005" to WDMemberTone.Tone4,
        )
        expected.forEach { (id, tone) -> assertEquals("$id -> $tone", tone, WDMemberTone.assign(id)) }
        assertEquals(WDMemberTone.assign("成员-甲"), WDMemberTone.assign("成员-甲"))
        assertTrue(WDMemberTone.entries.contains(WDMemberTone.assign("")))
    }

    @Test
    fun `U6 状态优先级 disabled 最高`() {
        assertEquals(WDAvatarVisualState.Disabled, WDAvatarGeometry.visualState(true, true, true, true))
        assertEquals(WDAvatarVisualState.Pressed, WDAvatarGeometry.visualState(false, true, true, true))
        assertEquals(WDAvatarVisualState.Default, WDAvatarGeometry.visualState())
    }
}