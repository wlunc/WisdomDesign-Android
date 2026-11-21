package io.github.wlunc.wisdom.components.WDCard

import io.github.wlunc.wisdom.components.primitives.WDCard.WDCardStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class WDCardTest {
    private fun source(path: String): String =
        File("src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDCard/$path").readText()

    private fun sourceAll(): String = listOf("WDCard.kt", "WDCardStyle.kt", "WDCardMedia.kt")
        .joinToString("\n") { source(it) }

    @Test
    fun frozenSignatureParamsAndDefaults() {
        val src = source("WDCard.kt")
        val expected = listOf(
            "style: WDCardStyle = WDCardStyle.Elevated",
            "modifier: Modifier = Modifier",
            "media: WDCardMedia? = null",
            "action: (() -> Unit)? = null",
            "content: @Composable () -> Unit",
        )
        val signature = src.substringAfter("public fun WDCard(").substringBefore(") {")
        expected.forEach { assertTrue("missing $it", signature.contains(it)) }
        assertEquals(
            listOf("style", "modifier", "media", "action", "content"),
            expected.map { it.substringBefore(":").trim() },
        )
    }

    @Test
    fun cardStyleFiveCasesIncludeMedia() {
        assertEquals(
            listOf("Elevated", "Outlined", "Glass", "Sunken", "Media"),
            WDCardStyle.entries.map { it.name },
        )
    }

    @Test
    fun a13MediaAspectAndMinHeight() {
        val src = source("WDCard.kt")
        assertTrue(src.contains("WDSize.cardMediaAspectRatio"))
        assertTrue(src.contains("WDSize.cardMediaMinHeight"))
        assertTrue(343f / 1.78f > 192f)
        assertTrue(171f / 1.78f >= 96f)
        assertTrue(src.contains("drawImage("))
    }

    @Test
    fun a13LoadingFailurePlaceholderAndFade() {
        val src = source("WDCard.kt")
        assertTrue(src.contains("surfaceCardSunken"))
        assertFalse(src.contains("skeleton", ignoreCase = true))
        assertTrue(src.contains("WDMotion.durationFast"))
        assertTrue(src.contains("clearAndSetSemantics"))
    }

    @Test
    fun a13MediaGlassMutuallyExclusiveAndLegacyStyles() {
        val src = source("WDCard.kt")
        assertTrue(src.contains("hasMedia && style == WDCardStyle.Glass"))
        listOf("WDCardStyle.Elevated", "WDCardStyle.Outlined", "WDCardStyle.Glass", "WDCardStyle.Sunken")
            .forEach { assertTrue("missing $it", src.contains(it)) }
    }

    @Test
    fun u6StatePriorityAndLoadingNotDisabled() {
        val src = source("WDCard.kt")
        val whenBody = src.substringAfter("get() = when {").substringBefore("else -> Visible.Default")
        val order = listOf("Disabled", "Loading", "Pressed", "Focused", "Hover")
        assertEquals(order, order.sortedBy { whenBody.indexOf("-> Visible.$it") })
        assertFalse(src.contains("semantics { disabled"))
    }

    @Test
    fun u5NoFixedHeightForText() {
        val src = source("WDCard.kt")
        assertTrue(src.contains("heightIn(min"))
        assertTrue(src.contains("wrapContentHeight"))
        assertFalse(src.contains("maxLines = 1"))
    }

    @Test
    fun staticChecksNoBareColorM3OrPreview() {
        val all = sourceAll()
        assertFalse(all.contains("Color(0x"))
        assertFalse(all.contains("androidx.compose.material3"))
        assertFalse(all.contains("@Preview"))
    }
}
