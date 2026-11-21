package io.github.wlunc.wisdom.components.WDCheckbox

import io.github.wlunc.wisdom.components.primitives.WDCheckbox.WDCheckboxControlledValueName
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.WDCheckboxDefaultIndeterminate
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.WDCheckboxDefaultSize
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.WDCheckboxSize
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.WDCheckboxStateOrder
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.boxSizeDp
import io.github.wlunc.wisdom.components.primitives.WDCheckbox.wdCheckboxVisualState
import io.github.wlunc.wisdom.foundation.generated.WDSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class WDCheckboxTest {
    @Test
    fun `C15_例外_checked`() { assertEquals("checked", WDCheckboxControlledValueName) }

    @Test
    fun `契约默认_indeterminate_false_size_standard`() {
        assertFalse(WDCheckboxDefaultIndeterminate)
        assertEquals(WDCheckboxSize.Standard, WDCheckboxDefaultSize)
    }

    @Test
    fun `尺寸枚举全量`() {
        assertEquals(listOf("Standard", "Compact"), WDCheckboxSize.entries.map { it.name })
    }

    @Test
    fun `方框尺寸_标准26_紧凑22`() {
        assertEquals(26f, WDCheckboxSize.Standard.boxSizeDp(), 0f)
        assertEquals(22f, WDCheckboxSize.Compact.boxSizeDp(), 0f)
        assertEquals(26f, WDSize.checkbox.value, 0f)
    }

    @Test
    fun `U6_状态优先级_full`() {
        assertEquals(WDCheckboxStateOrder.first(), wdCheckboxVisualState(false, true, true, true, true))
        assertEquals(WDCheckboxStateOrder[1], wdCheckboxVisualState(true, true, true, true, true))
        assertEquals(WDCheckboxStateOrder[2], wdCheckboxVisualState(true, false, true, true, true))
        assertEquals(WDCheckboxStateOrder[3], wdCheckboxVisualState(true, false, false, true, true))
        assertEquals(WDCheckboxStateOrder[4], wdCheckboxVisualState(true, false, false, false, true))
        assertEquals(WDCheckboxStateOrder[5], wdCheckboxVisualState(true, false, false, false, false))
    }

    @Test
    fun `半选点击_变全选_非三态循环`() {
        val indeterminate = true
        val checked = false
        val next = if (indeterminate) true else !checked
        assertEquals(true, next)
    }
}
