package io.github.wlunc.wisdom.foundation.accessibility

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView

/** 触觉网关（AR-75）。M2 只公开 press/toggle；threshold/warning 随 M4 加入。 */
public interface WDHaptics {
    public fun press()
    public fun toggle()
}

/**
 * 注入点：未注入（null）⇒ 平台实现（**不是 no-op、不是无触觉**）；
 * 注入用于测试捕获器或自定义实现。
 */
public val LocalWDHaptics: ProvidableCompositionLocal<WDHaptics?> =
    staticCompositionLocalOf { null }

/** 组合期取网关；未注入时回落到平台 View 触觉。 */
@Composable
public fun rememberWDHaptics(): WDHaptics {
    val override = LocalWDHaptics.current
    val view = LocalView.current
    return override ?: remember(view) { PlatformWDHaptics(view) }
}

private class PlatformWDHaptics(private val view: View) : WDHaptics {
    override fun press() = feedback()
    override fun toggle() = feedback()

    private fun feedback() {
        val constant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            HapticFeedbackConstants.TEXT_HANDLE_MOVE
        else
            HapticFeedbackConstants.LONG_PRESS
        view.performHapticFeedback(constant)
    }
}
