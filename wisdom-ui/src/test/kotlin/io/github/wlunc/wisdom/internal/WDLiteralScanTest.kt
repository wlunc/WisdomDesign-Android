package io.github.wlunc.wisdom.internal

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * G2 令牌字面量扫描的反例自测（§1.5.4-I）。
 *
 * 只测纯逻辑判定：手写文件命中裸值必须 fail；同一内容放进 generated/ 路径应被扫描根排除。
 * 这里不依赖 Gradle/Compose，可在 JVM 单测环境直接跑。
 */
internal object WDLiteralScanRule {
    private val BARE_COLOR = Regex("Color\(0x[0-9A-Fa-f]+\)")
    private val BARE_HEX = Regex("#[0-9A-Fa-f]{6}\b")
    private val BARE_DP_SP_EM = Regex("(?<![A-Za-z0-9_.])\d+(?:\.\d+)?\.(?:dp|sp|em)\b")
    private val G2_EXCLUDED = Regex("(^|/)foundation/generated/")

    fun isExcluded(relativePath: String): Boolean =
        G2_EXCLUDED.containsMatchIn(relativePath.replace('\\', '/'))

    fun violations(relativePath: String, source: String): List<String> {
        if (isExcluded(relativePath)) return emptyList()
        return buildList {
            if (BARE_COLOR.containsMatchIn(source)) add("Color(0x...)")
            if (BARE_HEX.containsMatchIn(source)) add("#RRGGBB")
            if (BARE_DP_SP_EM.containsMatchIn(source)) add("bare dp/sp/em")
        }
    }
}

class WDLiteralScanTest {
    @Test
    fun `G2 反例_手写文件裸值判失败`() {
        val violations = WDLiteralScanRule.violations(
            "src/main/kotlin/io/github/wlunc/wisdom/components/X.kt",
            "val c = Color(0xFF000000)\nval p = 16.dp", // wd-token-check:disable G2 — G2 反例夹具
        )
        assertTrue(violations.contains("Color(0x...)"))
        assertTrue(violations.contains("bare dp/sp/em"))
    }

    @Test
    fun `G2 正例_generated 路径排除生效`() {
        val excluded = WDLiteralScanRule.isExcluded(
            "src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDTokens.kt",
        )
        assertTrue(excluded)
        assertTrue(
            WDLiteralScanRule.violations(
                "src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDTokens.kt",
                "Color(0xFF000000)\n16.dp", // wd-token-check:disable G2 — G2 正例夹具（generated 排除验证）
            ).isEmpty(),
        )
        assertFalse(
            WDLiteralScanRule.isExcluded(
                "src/main/kotlin/io/github/wlunc/wisdom/components/X.kt",
            ),
        )
    }
}
