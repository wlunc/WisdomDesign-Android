package io.github.wlunc.wisdom.components.WDListRow

import io.github.wlunc.wisdom.components.primitives.WDListRow.WDListRowStateSaver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class WDListRowTest {
    private fun src(): String =
        File("src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDListRow/WDListRow.kt").readText()

    @Test
    fun frozenSignatureTwelveParams() {
        val signature = src().substringAfter("public fun WDListRow(").substringBefore(") {")
        val expected = listOf(
            "title: String",
            "modifier: Modifier = Modifier",
            "subtitle: String? = null",
            "trailingText: String? = null",
            "onClick: (() -> Unit)? = null",
            "enabled: Boolean = true",
            "selected: Boolean = false",
            "leading: (@Composable () -> Unit)? = null",
            "trailing: (@Composable () -> Unit)? = null",
            "showsSeparator: Boolean = true",
            "accessibilityLabel: String? = null",
            "density: WDLayoutDensity? = null",
        )
        expected.forEach { assertTrue("missing $it", signature.contains(it)) }
        assertTrue(signature.contains("selected: Boolean = false"))
        assertTrue(signature.contains("density: WDLayoutDensity? = null"))
    }

    @Test
    fun rowBoxFormulaFourCells() {
        val s = src()
        assertTrue(s.contains("WDListRowLayout.rowHeight("))
        assertTrue(s.contains("maxOf(base, slot)"))
        assertEquals(76f, (48 + 44 - 16).toFloat(), 0f)
    }

    @Test
    fun touchTarget48LayoutBox() {
        val s = src()
        assertTrue(s.contains(".wdTouchTarget()"))
        assertTrue(s.contains("WDSize.touchTargetMin"))
        assertFalse(s.contains(".height(48"))
    }

    @Test
    fun twoStatesSameWidthSelectedOnlyChangesBackground() {
        val s = src()
        assertTrue(s.contains(".fillMaxWidth()"))
        assertTrue(s.contains("if (selected) WDTheme.colors.surfaceTint else WDTheme.colors.surfaceCardSolid"))
    }

    @Test
    fun saverThreeRulesReturnNull() {
        assertEquals(true, WDListRowStateSaver.restore(WDListRowStateSaver.save(true)))
        assertEquals(false, WDListRowStateSaver.restore(WDListRowStateSaver.save(false)))
        assertNull(WDListRowStateSaver.restore(listOf(99, true)))
        assertNull(WDListRowStateSaver.restore(listOf(1)))
        assertNull(WDListRowStateSaver.restore("bad"))
        assertNull(WDListRowStateSaver.restore(listOf(1, "x")))
    }

    @Test
    fun u6AndAr34NonInteractiveAndDisabledSwallowsInput() {
        val s = src()
        val whenBody = s.substringAfter("get() = when {").substringBefore("else -> Visible.Default")
        val order = listOf("Disabled", "Loading", "Pressed", "Focused", "Hover")
        assertEquals(order, order.sortedBy { whenBody.indexOf("-> Visible.$it") })
        assertTrue(s.contains("onClick != null && visual.clickEnabled"))
        assertTrue(s.contains("onClick == null"))
        assertTrue(s.contains("clearAndSetSemantics {}"))
    }

    @Test
    fun zeroCopyUsesWDSemanticsJoin() {
        val s = src()
        assertTrue(s.contains("WDSemantics.join("))
        assertTrue(s.contains("accessibilityLabel"))
        assertFalse(s.contains("stringResource"))
    }

    @Test
    fun staticChecksNoBareColorM3OrPreview() {
        val s = src()
        assertFalse(s.contains("Color(0x"))
        assertFalse(s.contains("androidx.compose.material3"))
        assertFalse(s.contains("@Preview"))
    }
}
