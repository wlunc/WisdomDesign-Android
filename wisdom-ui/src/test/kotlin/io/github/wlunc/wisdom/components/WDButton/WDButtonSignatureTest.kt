package io.github.wlunc.wisdom.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** WDButton JVM 静态契约单测：冻结签名 + U6 + 令牌/门禁约束。 */
internal object WDButtonContractRule {
    const val PATH = "wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDButton/WDButton.kt"
    val FROZEN_PARAMS = listOf(
        "text: String", "onClick: () -> Unit", "modifier: Modifier = Modifier",
        "variant: WDButtonVariant = WDButtonVariant.Filled", "size: WDButtonSize = WDButtonSize.Md",
        "enabled: Boolean = true", "loading: Boolean = false", "loadingLabel: String? = null",
        "leadingIcon: (@Composable () -> Unit)? = null", "trailingIcon: (@Composable () -> Unit)? = null",
        "onClickLabel: String? = null", "role: Role? = Role.Button",
        "interactionSource: MutableInteractionSource? = null",
    )
    val VARIANTS = listOf("Filled", "Tonal", "Glass", "Outline", "Plain", "Destructive")
    val SIZES = listOf("Sm", "Md", "Lg")
    fun source(): String {
        var dir = File(".").absoluteFile
        repeat(6) {
            val candidate = File(dir, PATH)
            if (candidate.exists()) return candidate.readText()
            dir = dir.parentFile ?: return@repeat
        }
        error("missing source file: $PATH (searched upward from cwd)")
    }
    fun signatureParams(src: String): List<String> {
        val start = src.indexOf("public fun WDButton(")
        check(start >= 0)
        val end = src.indexOf(") {", start)
        check(end > start)
        return src.substring(start, end).lineSequence()
            .map { it.trim().trimEnd(',') }
            .filter { it.isNotEmpty() && !it.startsWith("public fun") && !it.startsWith("@") }
            .toList()
    }
    fun hasBareValue(src: String): Boolean =
        Regex("Color\(0x[0-9A-Fa-f]+\)").containsMatchIn(src) ||
            Regex("(^|[^A-Za-z0-9_.])[0-9]+(\.[0-9]+)?\.(dp|sp|em)\b").containsMatchIn(src)
}

class WDButtonSignatureTest {
    private val src = WDButtonContractRule.source()

    @Test fun frozenSignature13Params() {
        assertEquals(WDButtonContractRule.FROZEN_PARAMS, WDButtonContractRule.signatureParams(src))
        assertEquals(1, Regex("public fun WDButton\(").findAll(src).count())
    }

    @Test fun enumSixVariantsThreeSizes() {
        val variantBlock = src.substringAfter("enum class WDButtonVariant {").substringBefore("}")
        WDButtonContractRule.VARIANTS.forEach { assertTrue(variantBlock.contains(it)) }
        val sizeBlock = src.substringAfter("enum class WDButtonSize {").substringBefore("}")
        WDButtonContractRule.SIZES.forEach { assertTrue(sizeBlock.contains(it)) }
    }

    @Test fun loadingSwallowsInputButStaysInTree() {
        // U6: clickable 只看真实 enabled；loading 在 onClick 内吞 action，不标 disabled.
        assertTrue(src.contains("enabled = enabled,"))
        assertTrue(src.contains("if (!loading) onClick()"))
        assertFalse("must not derive clickable enabled from loading", src.contains("acceptInput"))
        assertFalse("must not use enabled = !loading", src.contains("enabled = !loading"))
        assertTrue(src.contains("Modifier.semantics { stateDescription = loadingLabel }"))
        assertTrue(src.contains("Modifier.alpha(0f)"))
    }

    @Test fun loadingDoesNotProduceDisabledSemantics() {
        assertTrue(src.contains("enabled = enabled,"))
        assertFalse(src.contains("enabled = enabled && !loading"))
        assertFalse(src.contains("enabled = acceptInput"))
        assertFalse(src.contains("semantics { disabled"))
        assertFalse(src.contains("Modifier.alpha(0f).clearAndSetSemantics"))
        // 节点保留：stateDescription + alpha(0f) 表达 loading，而非移除/禁用节点.
        assertTrue(src.contains("stateDescription = loadingLabel"))
        assertTrue(src.contains("if (loading) Modifier.alpha(0f) else Modifier"))
    }

    @Test fun noBareValuesM3OrPreview() {
        assertFalse(WDButtonContractRule.hasBareValue(src))
        assertFalse(src.contains("androidx.compose.material3"))
        assertFalse(src.contains("@Preview"))
        assertFalse(src.contains("androidx.compose.ui.tooling"))
    }

    @Test fun touchTargetChainOrder() {
        val idx = src.indexOf(".wdTouchTarget()")
        assertTrue(idx > 0)
        assertTrue(src.indexOf(".clickable(", idx) > idx)
        assertTrue(src.indexOf(".clickable(", idx) < src.indexOf(".clip(", idx))
    }
}
