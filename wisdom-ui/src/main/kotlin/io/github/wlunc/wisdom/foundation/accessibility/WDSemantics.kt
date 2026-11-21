package io.github.wlunc.wisdom.foundation.accessibility

/**
 * 语义拼接帮助函数（D5 / AR-84）。
 * 分隔符与词序全部由调用方传——库内零文案、零标点（L-B / G13）。
 * 已删除 stateValue（恒等函数，无价值）。
 */
public object WDSemantics {
    /** 拼接受检字符串；**无默认分隔符**，调用方必须显式传 separator。 */
    public fun join(separator: String, vararg parts: String): String =
        parts.filter { it.isNotEmpty() }.joinToString(separator)

    /** 位置语义：标签与位置文本用调用方给的分隔符拼接。 */
    public fun positional(label: String, positionText: String, separator: String): String =
        join(separator, label, positionText)
}
