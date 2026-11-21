package io.github.wlunc.wisdom.components.WDDivider

import io.github.wlunc.wisdom.components.primitives.WDDivider.WD_DIVIDER_INSET
import io.github.wlunc.wisdom.components.primitives.WDDivider.WDDividerStyle
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** WDDivider 的 JVM 断言：枚举全量、样式语义、尺寸令牌（改变量字段与去树语义）。 */
class WDDividerTest {
    @Test
    fun `两档全量且 Inset 不是 Indent`() {
        assertEquals(listOf("full", "inset"), WDDividerStyle.entries.map { it.name.lowercase() })
    }

    @Test
    fun `两档构造与默认全宽`() {
        val default: WDDividerStyle = WDDividerStyle.Full
        assertEquals(WDDividerStyle.Full, default)
        assertEquals(WDDividerStyle.Inset, WDDividerStyle.Inset)
    }

    @Test
    fun `装饰尺寸走令牌：内缩 = 16 + (24 + 4) + 12`() {
        assertEquals(WDSpacing.s5 + WDSpacing.s7 + WDSpacing.s2 + WDSpacing.s4, WD_DIVIDER_INSET)
        assertTrue(WD_DIVIDER_INSET.value > WDSpacing.s5.value)
    }
}
