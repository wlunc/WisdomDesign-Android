package io.github.wlunc.wisdom.components.WDSwitch

import io.github.wlunc.wisdom.components.primitives.WDSwitch.WDSwitchControlledValueName
import io.github.wlunc.wisdom.components.primitives.WDSwitch.WDSwitchSize
import io.github.wlunc.wisdom.components.primitives.WDSwitch.WDSwitchStateOrder
import io.github.wlunc.wisdom.components.primitives.WDSwitch.geometry
import io.github.wlunc.wisdom.components.primitives.WDSwitch.wdSwitchVisualState
import io.github.wlunc.wisdom.foundation.generated.WDSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WDSwitchTest {
    @Test
    fun `C15_受控值名_on`() { assertEquals("on", WDSwitchControlledValueName) }

    @Test
    fun `尺寸枚举全量`() {
        assertEquals(listOf("Standard", "Compact"), WDSwitchSize.entries.map { it.name })
    }

    @Test
    fun `标准轨道几何_51x31_thumb27`() {
        val g = WDSwitchSize.Standard.geometry()
        assertEquals(51f, g.trackWidth, 0.01f)
        assertEquals(31f, g.trackHeight, 0.01f)
        assertEquals(27f, g.thumb, 0.01f)
    }

    @Test
    fun `紧凑轨道几何_40x24_thumb20`() {
        val g = WDSwitchSize.Compact.geometry()
        assertEquals(40f, g.trackWidth, 0.01f)
        assertEquals(24f, g.trackHeight, 0.01f)
        assertEquals(20f, g.thumb, 0.01f)
    }

    @Test
    fun `热区令牌至少_48dp`() { assertTrue(WDSize.touchTargetMin.value >= 48f) }

    @Test
    fun `U6_状态优先级_full`() {
        assertEquals(
            WDSwitchStateOrder.first(),
            wdSwitchVisualState(enabled = false, loading = true, pressed = true, focused = true, hovered = true),
        )
        assertEquals(
            WDSwitchStateOrder[1],
            wdSwitchVisualState(enabled = true, loading = true, pressed = true, focused = true, hovered = true),
        )
        assertEquals(
            WDSwitchStateOrder[2],
            wdSwitchVisualState(enabled = true, loading = false, pressed = true, focused = true, hovered = true),
        )
        assertEquals(
            WDSwitchStateOrder[3],
            wdSwitchVisualState(enabled = true, loading = false, pressed = false, focused = true, hovered = true),
        )
        assertEquals(
            WDSwitchStateOrder[4],
            wdSwitchVisualState(enabled = true, loading = false, pressed = false, focused = false, hovered = true),
        )
        assertEquals(
            WDSwitchStateOrder[5],
            wdSwitchVisualState(enabled = true, loading = false, pressed = false, focused = false, hovered = false),
        )
    }
}