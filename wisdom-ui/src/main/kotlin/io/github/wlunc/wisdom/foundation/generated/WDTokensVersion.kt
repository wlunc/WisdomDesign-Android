// 本文件由 wisdomdesign/tools/token-build/build.js 生成，请勿手改。
// 修改请编辑 wisdomdesign/tokens/wisdom.tokens.json 后重新生成。
// tokens v1.1.0 · sha256:953044a85a8e

package io.github.wlunc.wisdom.foundation.generated

/** 令牌变更集自证（G7 / U14）：banner（生成物第 3 行）与 dist/tokens.manifest.json 同值。 */
/** `sha256` = banner 里的 12 位前缀（= 令牌源文件 SHA-256 前 12 位）；G7 断言它 == banner hash。 */
public object WDTokensVersion {
    public val version: String = "1.1.0"
    public val sha256: String = "953044a85a8e"
    /** 已生成的 scheme 清单（M0-1 schemes 维度）。 */
    public val schemes: List<String> = listOf("light", "dark")
    /** 默认 scheme（schemes.<name>.isDefault == true）。 */
    public val defaultScheme: String = "light"
    /** U12 语义色槽位数（生成器 --check 断言 == 32）。 */
    public val colorSlotCount: Int = 32
}
