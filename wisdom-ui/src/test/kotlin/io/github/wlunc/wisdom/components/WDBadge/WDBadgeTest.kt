package io.github.wlunc.wisdom.components.WDBadge

import io.github.wlunc.wisdom.components.primitives.WDBadge.WDBadgeGeometry
import io.github.wlunc.wisdom.components.primitives.WDBadge.WDBadgeValue
import io.github.wlunc.wisdom.components.primitives.WDBadge.WDBadgeVariant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** WDBadge 的 JVM 断言：签名 enum 全量、值语义、计数文案、状态优先级（语义/尺寸/状态）。 */
class WDBadgeTest {
    @Test
    fun `六变体全量且顺序冻结`() {
        assertEquals(
            listOf("shared", "schedule", "pending", "success", "warning", "danger"),
            WDBadgeVariant.entries.map { it.name.lowercase() },
        )
    }

    @Test
    fun `值形态可构造且相等`() {
        assertEquals(WDBadgeValue.label("3"), WDBadgeValue.label("3"))
        assertEquals(WDBadgeValue.count(3), WDBadgeValue.count(3))
        assertEquals(WDBadgeValue.Dot, WDBadgeValue.Dot)
    }

    @Test
    fun `可读文本与零隐藏规则`() {
        assertEquals("已逾期", WDBadgeGeometry.accessibilityText(WDBadgeValue.label("已逾期")))
        assertEquals("1", WDBadgeGeometry.accessibilityText(WDBadgeValue.count(1)))
        assertEquals("99", WDBadgeGeometry.accessibilityText(WDBadgeValue.count(99)))
        assertEquals("99+", WDBadgeGeometry.accessibilityText(WDBadgeValue.count(100)))
        assertEquals(null, WDBadgeGeometry.accessibilityText(WDBadgeValue.count(0)))
        assertEquals(null, WDBadgeGeometry.accessibilityText(WDBadgeValue.Dot))
        assertEquals(null, WDBadgeGeometry.accessibilityText(null))
    }

    @Test
    fun `count 零隐藏与封顶文案`() {
        assertFalse(WDBadgeGeometry.isVisible(WDBadgeValue.count(0)))
        assertTrue(WDBadgeGeometry.isVisible(WDBadgeValue.count(1)))
        assertEquals("99+", WDBadgeGeometry.countText(1000))
    }

    @Test
    fun `U6 状态优先级 disabled 最高且 loading 非 disabled`() {
        val all = WDBadgeGeometry.resolveVisualState(
            disabled = true, loading = true, pressed = true, focused = true, hovered = true,
        )
        assertEquals("Disabled", all.name)
        assertEquals("Loading", WDBadgeGeometry.resolveVisualState(loading = true, pressed = true).name)
        assertEquals("Pressed", WDBadgeGeometry.resolveVisualState(pressed = true, focused = true).name)
        assertEquals("Focused", WDBadgeGeometry.resolveVisualState(focused = true, hovered = true).name)
        assertEquals("Hover", WDBadgeGeometry.resolveVisualState(hovered = true).name)
        assertEquals("Default", WDBadgeGeometry.resolveVisualState().name)
    }
}
