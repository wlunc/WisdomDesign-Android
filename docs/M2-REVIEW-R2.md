# M2 独立评审 Round-2 报告（Android · t10 findings 闭合复核）

> 评审任务：t12 · review-round-2（reviewed task = t11 repair-round-2）｜评审时点：2026-11-21（Asia/Shanghai）
> 评审人：android-reviewer（独立，未参与 t11 修复）
> 复核对象：t11 对 t10 两条 finding（R-01 high / R-02 low）的修复 + M2 全量交付物回归。
> 评审依据：`android/AGENTS.md` §6/§7/§10、`android/docs/M2-SIGNATURE-FREEZE.md`、`wisdomdesign/contracts/<Component>.yaml`（11 份，只读）、`android/docs/M2-REVIEW.md`（round-1，138 行，只读）、修复后源码/测试/门禁（只读）。
> 铁律遵守：只读 wisdomdesign/ 与 iOS/；未手改任何源码、测试、门禁脚本、构建脚本、api 基线、round-1 报告；未 git commit；唯一写入 = 本报告 `android/docs/M2-REVIEW-R2.md`（任务 in/out scope 字段为空，沿用 `DEV-PLAN-REVIEW-R2.md` 的 R2 报告惯例与 round-1 同目录位置）。

## 0. Verdict

**verdict = pass**（**0 条 findings**）。

t10 的两条 finding 均已按锚点实质闭合且无回归：R-01（U6 loading 误用 clickable disabled）在 WDButton/WDIconButton 源码改为 `enabled = enabled` + onClick 内 `if (!loading)` 吞 action，并新增/收紧 4 组测试断言；R-02（G1-③ m3 计数口径）在 build.gradle 增加双口径独立集合与精确断言，并在 WDM3ImportCountTest 增加全限定名反例。11 件签名/参数/默认值/枚举 case 保持 0 不一致；AGENTS §10 十八条反模式全部复核为合规（round-1 的 #7 违规已清除）；静态门禁全绿；t11 对所有 Gradle 未验证项如实标注，未写成通过。

**0 条 findings 的依据**：见 §1（两条 finding 逐条闭合取证）、§2（11 件签名回归）、§3（18 条反模式全绿）、§4（门禁/测试复跑）、§5（未验证标注核对）——每一条可能的 risk 均有独立证据且未发现新的不一致。

---

## 1. t10 findings 闭合复核

### 1.1 R-01（high，U6）— ✅ 已闭合

| 检查点 | round-1 状态 | round-2 实测 | 结论 |
| --- | --- | --- | --- |
| WDButton clickable.enabled | `acceptInput = enabled && !loading` → `enabled = acceptInput` | `WDButton.kt:109 enabled = enabled,`；`acceptInput` 全仓 0 命中；`enabled = !loading` 0 命中 | ✅ |
| WDButton loading 吞 action | onClick 直接调 onClick() | `WDButton.kt:115 if (!loading) onClick()`，触觉仍在组合期取/点击时调 | ✅ |
| WDButton loading 无障碍表达 | `Modifier.semantics { stateDescription = loadingLabel }` + `alpha(0f)` | 保留（`:120`，`:163 Modifier.alpha(0f)`），节点不 disabled、不 clearAndSetSemantics | ✅ |
| WDIconButton clickable.enabled | `enabled = !loading` | `WDIconButton.kt:89 enabled = enabled, // U6: 仅真实 disabled 传 false` | ✅ |
| WDIconButton loading 吞 action | onClick 直接调 onClick() | `WDIconButton.kt:93 if (!loading) onClick() // loading 吞 action，但节点保留（U6）` | ✅ |
| WDIconButton loading 无障碍表达 | stateDescription + alpha(0f) | `WDIconButton.kt:98-101 if (loading) { stateDescription = accessibilityLabel }` + `:128 if (loading) Modifier.alpha(0f) else Modifier` | ✅ |
| 测试收紧 | 仅字面断言，漏 `acceptInput` 组合 | `WDButtonSignatureTest.loadingSwallowsInputButStaysInTree`（6 断言）+ 新增 `loadingDoesNotProduceDisabledSemantics`（7 断言）；`WDIconButtonSignatureTest.loadingSwallowsActionButKeepsEnabledSemantics`（7 断言）+ `loadingDoesNotProduceDisabledSemantics`（6 断言） | ✅ |

**独立求值（非仅信 t11 自报）**：用源码文本逐条求值 4 组共 **26 条断言 → 26 pass / 0 fail**（含否定断言 `!contains("acceptInput")`、`!contains("enabled = !loading")`、`!contains("semantics { disabled")` 等）；且全仓 components 目录 `enabled = !loading` = 0、`acceptInput` = 0。WDButton/WDIconButton/WDButtonSignatureTest/WDIconButtonSignatureTest/WDM3ImportCountTest/build.gradle.kts 去字符串后的花括号/圆括号计数均 0 差（结构平衡）。

**结论**：U6 全序 `disabled > loading > pressed > focused > hover > default` 一致；loading 不再产生 disabled 语义，符合 AGENTS §6 #20 派生② + §10 #7。R-01 闭合。

### 1.2 R-02（low，G1-③ 口径）— ✅ 已闭合

| 检查点 | round-1 状态 | round-2 实测 | 结论 |
| --- | --- | --- | --- |
| build.gradle G1 计数 | 只扫 import 行（`if (!line.startsWith("import ")) return`），全限定名漏判 | `wisdom-ui/build.gradle.kts:153-180` 新增 `m3ImportHits`（仅 import 行）与 `m3AnyLineHits`（全部行，含全限定名直用），两个独立集合、两条 `check(...==listOf(allowedM3))` 精确断言 + `println` 双口径数字 | ✅ |
| 反例自测覆盖 | 仅 import 反例 | `WDM3ImportCountTest:55-63` 新增 `G1_反例_全限定名直用_仅 import 口径漏判但全行口径判失败`：`check(fqcn, true)==true`（按定义漏判）+ `check(fqcn, false)==false`（全行判失败） | ✅ |
| 手工判据一致 | §5.2 `grep -rl` 口径未对齐 | 新增 `grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin` 实跑 = 1 文件（WDMaterialScheme.kt）；双口径断言保证 import 漏判时全行口径兜底 | ✅ |

**独立逻辑模拟**：`positive/default=true`、`negative/default=false`、`fqcn/importOnly=true`（口径差按定义记录）、`fqcn/anyLine=false`——与新增测试期望逐条相符。R-02 闭合。

---

## 2. 11 件签名 / 参数 / 默认值 / 枚举 case 回归（仍 0 不一致）

| # | 组件 | public fun 形参数（冻结 → 现文） | 受控值名 | 枚举 case（归一化） | 结果 |
| --- | --- | --- | --- | --- | --- |
| 1 | WDAvatar | 6 → 6 | 无 | WDAvatarSize{Xs,Sm,Md,Lg,Xl} / WDMemberTone{Tone1..4} | ✅ |
| 2 | WDBadge | 4 → 4 | 无 | WDBadgeVariant 6 case | ✅ |
| 3 | WDButton | 13 → 13 | loading | WDButtonVariant 6 / WDButtonSize 3 | ✅ |
| 4 | WDCard | 5 → 5 | 无 | WDCardStyle 5（含 Media） | ✅ |
| 5 | WDCheckbox | 4 主+重载 → 同 | checked（例外） | WDCheckboxSize{Standard,Compact} | ✅ |
| 6 | WDDivider | 2 → 2 | 无 | WDDividerStyle{Full,Inset} | ✅ |
| 7 | WDIcon | 5 → 5 | 无 | WDIconSize{Sm,Md,Lg,Xl}（注入辅助） | ✅ |
| 8 | WDIconButton | 8 → 8 | loading | WDIconButtonShape{Circle,RoundedSquare}；复用 WDButtonVariant 子集 | ✅ |
| 9 | WDListRow | 12 → 12 | selected | swipeAction 未暴露（冻结容许） | ✅ |
| 10 | WDSwitch | 6 主+重载 → 同 | on | WDSwitchSize{Standard,Compact} | ✅ |
| 11 | WDTextField | 21 → 21 | text | WDTextFieldVariant{Inset,Outline,Glass} | ✅ |

t11 仅改 loading 实现与测试/门禁，未触碰任何签名或枚举定义；grep 现文 public fun / public enum class 集合与 round-1 记录完全一致。**结论：0 处不一致。**

---

## 3. AGENTS §10 十八条反模式复核（round-1 #7 已转绿）

| # | 反模式 | round-2 结论 | 证据 |
| --- | --- | --- | --- |
| 1 | 裸值 Color(0x)/#RRGGBB/裸 dp·sp | ✅ | G2 实跑 exit 0 |
| 2 | 绕过 WDGlass.resolve 自选档 | ✅ | 11 件无自判 |
| 3 | 扩热区覆盖相邻行 | ✅ | wdTouchTarget 布局盒法不变 |
| 4 | 手改生成物 / api 基线 | ✅ | G7 实跑 exit 0；api 211 行未扩写（无 M2 组件），仅 t9/t11 明示待 apiDump |
| 5 | Modifier.composed 读 local | ✅ | 0 命中 |
| **6** | 自造按下态 / 第二 interactionSource | ✅ | 单 source 不变 |
| **7** | `clickable(enabled=false)` 表 loading / 手写 disabled 语义 | ✅ **已修复** | 5 个交互组件（WDButton/WDIconButton/WDSwitch/WDCheckbox/WDTextField）`enabled = enabled`，loading 通过 onClick 守卫吞 action；`enabled = !loading`/`acceptInput` 0 命中 |
| 8 | 读 isSystemInDarkTheme / 静态 scheme | ✅ | components 0 命中 |
| 9 | Modifier.blur / renderEffect / 离屏层 | ✅ | 0 命中 |
| 10 | Mode.Fixed / lineHeightMultiple / @ScaledMetric / 固定高度包文字 | ✅ | U5 实现不变 |
| 11 | remember 缓存派生值 | ✅ | 未见 |
| 12 | 组件层新增 public CompositionLocal | ✅ | components 0 命中 |
| 13 | @JvmOverloads/@JvmName/HIDDEN | ✅ | main 0 命中 |
| 14 | 既有 public 加默认参数（$default） | ✅ | WDCheckbox 重载拆分不变 |
| 15 | scale(-1f) 双重镜像 | ✅ | 仅注释禁令；icons 44 条一致 |
| 16 | 库内文案/资源/内置分隔符（L-B） | ✅ | G13 命中 0；WDSemantics.join 无默认分隔符 |
| 17 | 新增白名单外依赖 | ✅ | build.gradle 依赖面未变 |
| 18 | 改批次 / 跳过批前冻结 | ✅ | 11 件 = 冻结 11 件 |

冻结值专项：U5 行盒 `max(base,slot)`✅｜热区 48/44 由 `WDSize.touchTargetMin/rowHeightCompact` + wdTouchTarget 布局盒✅｜字段盒 46（`WDSize.fieldHeight`，单测）✅｜U12 32 槽位生成物 ✅｜图标 44 名/注入面/autoMirror（icons 脚本 exit 0）✅｜G9 无 m3 公开签名（exit 0）✅｜m3 import==1 且唯一 WDMaterialScheme.kt（grep -rl count=1）✅｜generated/** 零手改（G7 + 内容核对）✅｜api 基线由 apiDump 生成（当前旧基线未手改，待可跑 Gradle 环境生成）✅。

---

## 4. 门禁 / 单测复跑（静态可跑项）

| 命令 | 结果 |
| --- | --- |
| `bash tools/check-token-literals.sh` | exit 0 · G2 PASS |
| `bash tools/check-public-types.sh` | exit 0 · G9 PASS |
| `bash tools/check-generated-self-proof.sh` | exit 0 · G7 PASS |
| `bash tools/check-literal-language.sh` | exit 0 · G13 warning 命中 0 |
| `bash tools/check-icons-contract.sh` | exit 0 · 44/44 逐条一致 |
| `bash -n tools/check-*.sh`（7 个） | 全 OK |
| `grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin` | count=1，唯一 WDMaterialScheme.kt |
| 26 条新增 U6 断言独立求值 | 26 pass / 0 fail |
| R-02 双口径逻辑模拟 | 4/4 与期望相符 |
| 修复文件结构平衡（去字符串） | 6/6 braces=0 parens=0 |

单测覆盖：11/11 件各有 ≥1 条有意义断言；t4 G2/G1 反例自测在 t11 后仍存在且方向正确；新增 U6/全限定名断言见 §1。**【未验证·环境阻塞】** Gradle 编译、`testDebugUnitTest`、`wisdomGate` 全量、`apiDump` 配套 `git diff`：复现 `cd android && timeout 5s ./gradlew --offline --version` → exit 1（`.gradle/wrapper/.../gradle-8.14.5-bin.zip.lck Operation not permitted`），本报告同样不写成通过。

---

## 5. t11 未验证项标注核对 + 无手改背书

- t11 回执对三条 verify 中的全部 Gradle 命令均标【未验证·环境阻塞】，并给出与 t1–t10 一致的 .lck 报错摘要；**未发现把 Gradle 项写成"已通过"的表述**。✅
- `android/tools/deprecated-baseline.txt` = `0`（人工基线，非 CI 自动改写），G4 缺基线即 fail 的保护保留。✅
- api 基线仍 211 行、M2 组件 0 命中、`WDThemeKt.toTextStyle` 仍在（WDTextMetricsKt 迁移待 apiDump）；属 t9 明示的待生成状态，非手改。✅
- 本轮未修改任何被审文件：wisdomdesign/ 与 iOS/ 只读；源码/测试/build.gradle/tools/api/冻结文档/round-1 报告全部只读；唯一写入本报告。✅

---

## 6. 结论

- **verdict = pass**，**0 条 findings**。t10 的 R-01/R-02 均实质闭合，M2 交付物对冻结契约、U6/U5/热区/字段盒/U12/图标/L-B/m3 冻结值及 AGENTS §10 全部十八条反模式符合；单测与门禁反例自测完整；未验证项如实标注。
- **后续（不构成本轮 finding）**：M0-4/M3 封板前仍须在可跑 Gradle 的环境执行 `apiDump` 并同提交新基线、跑绿 `wisdomGate` 与单测；此待办继承 t9/t11 已明示的环境阻塞，不是新增缺陷。
- 本报告不替代编译/单测/门禁实测；在这些命令可跑并通过前，结论仅限静态评审维度。
