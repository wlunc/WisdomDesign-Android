package io.github.wlunc.wisdom.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** WDIconButton JVM 静态契约单测：冻结签名 + 复用枚举子集 + 圆形/方形 + 图标注入。 */
internal object WDIconButtonContractRule {
    const val PATH = "wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDIconButton/WDIconButton.kt"
    val FROZEN_PARAMS = listOf(
        "icon: WDIconName", "accessibilityLabel: String", "onClick: () -> Unit",
        "modifier: Modifier = Modifier", "variant: WDButtonVariant = WDButtonVariant.Plain",
        "size: WDButtonSize = WDButtonSize.Md", "loading: Boolean = false",
        "shape: WDIconButtonShape = WDIconButtonShape.Circle",
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
        val start = src.indexOf("public fun WDIconButton(")
        check(start >= 0)
        val end = src.indexOf(") {", start)
        return src.substring(start, end).lineSequence()
            .map { it.trim().trimEnd(',') }
            .filter { it.isNotEmpty() && !it.startsWith("public fun") && !it.startsWith("@") }
            .toList()
    }
}

class WDIconButtonSignatureTest {
    private val src = WDIconButtonContractRule.source()

    @Test fun frozenSignature8Params() {
        assertEquals(WDIconButtonContractRule.FROZEN_PARAMS, WDIconButtonContractRule.signatureParams(src))
        assertEquals(1, Regex("public fun WDIconButton\(").findAll(src).count())
    }

    @Test fun shapeCircleAndRoundedSquare() {
        val block = src.substringAfter("enum class WDIconButtonShape {").substringBefore("}")
        assertTrue(block.contains("Circle"))
        assertTrue(block.contains("RoundedSquare"))
        assertTrue(src.contains("CircleShape"))
        assertTrue(src.contains("RoundedCornerShape(WDRadius.md)"))
    }

    @Test fun reusesButtonVariantNoDedicatedType() {
        assertFalse("must not create WDIconButtonVariant type", src.contains("enum class WDIconButtonVariant"))
        assertTrue(src.contains("variant: WDButtonVariant"))
        assertTrue(src.contains("WDButtonVariant.Destructive -> Modifier"))
    }

    @Test fun iconInjectedAndA11yLabelMandatory() {
        assertTrue(src.contains("rememberWDIconResolve"))
        assertTrue(src.contains("contentDescription = accessibilityLabel"))
        assertFalse(src.contains("scale(-1f)"))
        assertTrue(src.contains("Box(Modifier.size(resolved.placeholderSize))"))
    }

    @Test fun loadingSwallowsActionButKeepsEnabledSemantics() {
        // U6: loading 仅吞 action，clickable.enabled 只看真实 disabled.
        assertTrue(src.contains("enabled = enabled,"))
        assertTrue(src.contains("if (!loading) onClick()"))
        assertFalse(src.contains("enabled = !loading"))
        assertFalse(src.contains("enabled = enabled && !loading"))
        assertTrue(src.contains("if (loading) {"))
        assertTrue(src.contains("stateDescription = accessibilityLabel"))
        assertTrue(src.contains("if (loading) Modifier.alpha(0f) else Modifier"))
    }

    @Test fun loadingDoesNotProduceDisabledSemantics() {
        assertTrue(src.contains("enabled = enabled,"))
        assertFalse(src.contains("semantics { disabled"))
        assertFalse(src.contains("clearAndSetSemantics"))
        assertFalse(src.contains("Color(0x"))
        assertFalse(src.contains("androidx.compose.material3"))
        assertFalse(src.contains("@Preview"))
    }
}
