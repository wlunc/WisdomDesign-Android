package io.github.wlunc.wisdom.components.WDTextField

import io.github.wlunc.wisdom.components.primitives.WDTextField.WDTextFieldControlledValueName
import io.github.wlunc.wisdom.components.primitives.WDTextField.WDTextFieldFieldHeight
import io.github.wlunc.wisdom.components.primitives.WDTextField.WDTextFieldStateOrder
import io.github.wlunc.wisdom.components.primitives.WDTextField.WDTextFieldVariant
import io.github.wlunc.wisdom.components.primitives.WDTextField.wdTextFieldVisualState
import io.github.wlunc.wisdom.foundation.generated.WDSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WDTextFieldTest {
    @Test
    fun `C15_受控值名_text`() {
        assertEquals("text", WDTextFieldControlledValueName)
    }

    @Test
    fun `字段盒高度恒为_46dp_且等于令牌`() {
        assertEquals(WDSize.fieldHeight.value, WDTextFieldFieldHeight.value, 0f)
        assertEquals(46f, WDTextFieldFieldHeight.value, 1f)
    }

    @Test
    fun `三种变体全量`() {
        assertEquals(listOf("Inset", "Outline", "Glass"), WDTextFieldVariant.entries.map { it.name })
    }

    @Test
    fun `U6_状态优先级_disabled_吞输入`() {
        assertEquals(
            WDTextFieldStateOrder.first(),
            wdTextFieldVisualState(enabled = false, isFocused = true, hasError = true, readOnly = true),
        )
    }

    @Test
    fun `U6_状态优先级_error_先于_focused`() {
        assertEquals(
            WDTextFieldStateOrder[1],
            wdTextFieldVisualState(enabled = true, isFocused = true, hasError = true, readOnly = true),
        )
    }

    @Test
    fun `U6_默认态`() {
        assertEquals(
            WDTextFieldStateOrder.last(),
            wdTextFieldVisualState(enabled = true, isFocused = false, hasError = false, readOnly = false),
        )
    }

    @Test
    fun `B6_能力参数签名存在性`() {
        assertTrue(WDTextFieldVariant.entries.size == 3)
        assertTrue(WDSize.fieldMinWidth.value > 0f)
    }
}
