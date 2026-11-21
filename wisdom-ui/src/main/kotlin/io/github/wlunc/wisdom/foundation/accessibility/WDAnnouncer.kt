package io.github.wlunc.wisdom.foundation.accessibility

import android.view.View
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView

/** 播报优先级；Assertive 在 Android 用窗口状态变更事件近似（V2 待真机核准）。 */
public enum class WDAnnouncePriority { Polite, Assertive }

/** 播报网关（AR-75）。 */
public interface WDAnnouncer {
    public fun announce(message: String, priority: WDAnnouncePriority = WDAnnouncePriority.Polite)
}

/**
 * 注入点：未注入（null）⇒ 平台实现（**不是 no-op、不是静默**）；
 * 注入用于测试捕获器或自定义实现。
 */
public val LocalWDAnnouncer: ProvidableCompositionLocal<WDAnnouncer?> =
    staticCompositionLocalOf { null }

/** 组合期取网关；未注入时回落到平台无障碍播报。 */
@Composable
public fun rememberWDAnnouncer(): WDAnnouncer {
    val override = LocalWDAnnouncer.current
    val view = LocalView.current
    return override ?: remember(view) { PlatformWDAnnouncer(view) }
}

private class PlatformWDAnnouncer(private val view: View) : WDAnnouncer {
    override fun announce(message: String, priority: WDAnnouncePriority) {
        view.announceForAccessibility(message)
    }
}

/** 顶层函数，不读 local（AR-75）；stateDescription 由调用方传入，库内零文案。 */
public fun Modifier.wdLiveRegion(stateDescription: String): Modifier =
    this.semantics {
        liveRegion = LiveRegionMode.Polite
        this.stateDescription = stateDescription
    }
