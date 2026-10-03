package io.github.wlunc.wisdom.foundation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * 把设计里的 CSS 角度渐变转成 Compose 画刷。
 *
 * Brush.linearGradient 不支持任意角度（它只知道起始点），所以这里用 ShaderBrush
 * 在测量阶段按实际尺寸算渐变轴长度，避免不同尺寸下渐变被拉伸变形。
 */
public fun WDGradientSpec.brush(): Brush = WDLinearGradientBrush(this)

internal class WDLinearGradientBrush(
    private val spec: WDGradientSpec,
) : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        // CSS 角度：0° 指向正上方，顺时针增加
        val radians = Math.toRadians(spec.angleDegrees - 90.0)
        val dx = cos(radians).toFloat()
        val dy = sin(radians).toFloat()
        val length = abs(size.width * dx) + abs(size.height * dy)
        val center = Offset(size.width / 2f, size.height / 2f)
        val half = Offset(dx * length / 2f, dy * length / 2f)

        return LinearGradientShader(
            from = center - half,
            to = center + half,
            colors = spec.stops.map { it.first },
            colorStops = spec.stops.map { it.second },
            tileMode = TileMode.Clamp,
        )
    }
}
