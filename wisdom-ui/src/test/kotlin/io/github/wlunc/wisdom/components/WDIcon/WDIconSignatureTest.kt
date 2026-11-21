package io.github.wlunc.wisdom.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** WDIcon JVM 静态契约单测：冻结签名 + 注入面 + 缺图占位 + autoMirror + 无裸值。 */
internal object WDIconContractRule {
    const val PATH = "wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDIcon/WDIcon.kt"
    val FROZEN_PARAMS = listOf(
        "icon: WDIconName", "contentDescription: String? = null", "modifier: Modifier = Modifier",
        "tint: Color = Color.Unspecified", "size: Dp = WDSize.iconMd",
    )
    fun source(): String {
        var dir = File(".").absoluteFile
        repeat(6) {
            val candidate = File(dir, PATH)
            if (candidate.exists()) return candidate.readText()
            dir = dir.parentFile ?: return@repeat
        }
        error("missing source file: $PATH")
    }
    fun signatureParams(src: String): List<String> {
        val start = src.indexOf("public fun WDIcon(")
        check(start >= 0)
        val end = src.indexOf(") {", start)
        return src.substring(start, end).lineSequence()
            .map { it.trim().trimEnd(',') }
            .filter { it.isNotEmpty() && !it.startsWith("public fun") && !it.startsWith("@") }
            .toList()
    }
}

class WDIconSignatureTest {
    private val src = WDIconContractRule.source()

    @Test fun frozenSignature5Params() {
        assertEquals(WDIconContractRule.FROZEN_PARAMS, WDIconContractRule.signatureParams(src))
        assertEquals(1, Regex("public fun WDIcon\(").findAll(src).count())
    }

    @Test fun injectionAndMissingPlaceholder() {
        assertTrue(src.contains("LocalWDIcons.current[icon]"))
        assertTrue("missing icon must keep placeholder box", src.contains("Box(modifier.size(size).then(semanticsModifier))"))
        assertFalse("must not draw substitute icon on miss", src.contains("Icons.Default"))
    }

    @Test fun noDoubleMirror() {
        assertFalse("scale(-1f) double-mirror ban", src.contains("scale(-1f)"))
        assertTrue("autoMirror must be documented", src.contains("autoMirror"))
    }

    @Test fun tintAndSemantics() {
        assertTrue(src.contains("Color.Unspecified"))
        assertTrue(src.contains("colors.textPrimary"))
        assertTrue(src.contains("clearAndSetSemantics"))
        assertTrue(src.contains("contentDescription = null"))
    }

    @Test fun noBareValuesM3OrPreview() {
        assertFalse(src.contains("Color(0x"))
        assertFalse(src.contains("androidx.compose.material3"))
        assertFalse(src.contains("@Preview"))
    }
}
