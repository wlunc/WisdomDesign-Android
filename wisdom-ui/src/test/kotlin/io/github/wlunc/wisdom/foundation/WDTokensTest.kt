package io.github.wlunc.wisdom.foundation

import io.github.wlunc.wisdom.foundation.generated.WDDerived
import io.github.wlunc.wisdom.foundation.generated.WDPalette
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.generated.wdDarkColors
import io.github.wlunc.wisdom.foundation.generated.wdLightColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WDTokensTest {
    @Test
    fun `间距阶梯递增`() {
        val spacing = listOf(
            WDSpacing.s1, WDSpacing.s2, WDSpacing.s3, WDSpacing.s4, WDSpacing.s5,
            WDSpacing.s6, WDSpacing.s7, WDSpacing.s8, WDSpacing.s9, WDSpacing.s10,
        )
        assertEquals(spacing.sortedBy { it.value }, spacing)
    }

    @Test
    fun `圆角阶梯递增`() {
        val radius = listOf(
            WDRadius.xs, WDRadius.sm, WDRadius.md,
            WDRadius.lg, WDRadius.xl, WDRadius.xxl,
        )
        assertEquals(radius.sortedBy { it.value }, radius)
    }

    @Test
    fun `字阶的行高不小于字号`() {
        val styles = listOf(
            WDType.largeTitle, WDType.title1, WDType.title2, WDType.title3,
            WDType.headline, WDType.body, WDType.callout, WDType.subheadline,
            WDType.footnote, WDType.caption1, WDType.caption2,
        )
        styles.forEach { style ->
            assertTrue(
                "行高 ${style.lineHeight} 小于字号 ${style.size}",
                style.lineHeight.value >= style.size.value,
            )
        }
    }

    @Test
    fun `深浅两套语义色都齐备且不同`() {
        assertNotEquals(wdLightColors.bgCanvas, wdDarkColors.bgCanvas)
        assertNotEquals(wdLightColors.textPrimary, wdDarkColors.textPrimary)
        assertEquals(wdLightColors.statusDanger, wdLightColors.statusDanger)
    }

    @Test
    fun `组件填充色与背景层不同`() {
        assertNotEquals(WDDerived.fillLight2, WDPalette.mist)
    }
}
