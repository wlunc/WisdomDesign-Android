# M2 独立评审报告（Android · 11 件基础批）

> 评审任务：t10 · 独立评审（不参与实现）｜评审时点：2026-11-21（Asia/Shanghai）
> 评审人：android-reviewer（独立）
> 评审依据：`android/AGENTS.md` §6 冻结值 / §7 未验证清单 / §10 反模式 18 条、`android/docs/M2-SIGNATURE-FREEZE.md`（435 行）、`wisdomdesign/contracts/<Component>.yaml`（11 份，只读）、`android/docs/M2-REVIEW.md` 之外的全部 M2 交付物（只读）。
> 铁律遵守：只读 wisdomdesign/ 与 iOS/；未手改任何源码、门禁脚本、构建脚本、冻结文档、api 基线；未 git commit；唯一写入 = 本报告。

## 0. Verdict

**verdict = needs_revision**（2 条 findings：1 high / 1 low）。

M2 11 件组件的**签名、参数名、默认值、枚举 case 与冻结文档/契约逐件一致（0 不一致）**；单测每件至少 1 条有意义断言；t4 门禁有 G1/G2 反例自测；t9 的所有 Gradle 相关未验证项均如实标注【未验证·环境阻塞】。但发现 **U6 冻结值违反**：WDButton / WDIconButton 在 loading 时把 `clickable(enabled = false)` 作为吞输入手段（AGENTS §6 #20 派生②、§10 #7 明令禁止），且同批 WDSwitch/WDCheckbox 使用了正确模式，构成 M2 内部不一致，阻断 M2 出口判据③的语义断言。详见 §5。

---

## 1. 11 件签名 / 参数 / 默认值 / 枚举 case 逐件核对

核对方法：逐件读取实现文件 public 函数形参列表与 `enum class`，与冻结文档 §2.1–§2.11 冻结签名块 + 契约 `params[].name/default` + `enums` 全量 case 逐行对照；C-15 受控值名按 §5 自检表复核。归一化规则 = `name.lowercase()` 对契约 lowerCamel（DIR-1 大小写差异不计）。

| # | 组件 | 主签名 params 数（实现 / 冻结） | 受控值名 | 枚举 case 全量（实现 vs 契约） | 结果 |
| --- | --- | --- | --- | --- | --- |
| 1 | WDAvatar | 6 / 6 | 无（C-15 #12 = —） | WDAvatarSize{Xs,Sm,Md,Lg,Xl} = [xs,sm,md,lg,xl]；WDMemberTone{Tone1..Tone4} = [tone1..tone4] | ✅ |
| 2 | WDBadge | 4 / 4 | 无（C-15 #11 = —） | WDBadgeVariant{Shared,Schedule,Pending,Success,Warning,Danger} 6/6 | ✅ |
| 3 | WDButton | 13 / 13 | loading（C-15 #01） | WDButtonVariant 6/6；WDButtonSize{Sm,Md,Lg} 3/3 | ✅ |
| 4 | WDCard | 5 / 5 | 无（C-15 #17 = —） | WDCardStyle{Elevated,Outlined,Glass,Sunken,Media} 5/5（A-13 Media 本批纳入） | ✅ |
| 5 | WDCheckbox | 4 主 + 7/9 重载（冻结文档 §2.5 注记允许重载拆分） | checked（C-15 例外） | WDCheckboxSize{Standard,Compact} 2/2 | ✅ |
| 6 | WDDivider | 2 / 2 | 无（C-15 #14 = —） | WDDividerStyle{Full,Inset} 2/2（Inset 非 Indent） | ✅ |
| 7 | WDIcon | 5 / 5 | 无（C-15 #20 = —） | WDIconSize 映射 foundation/icons/WDIcon.kt {Sm,Md,Lg,Xl} 4/4 | ✅ |
| 8 | WDIconButton | 8 / 8 | loading（C-15 #02） | WDIconButtonShape{Circle,RoundedSquare} 2/2；复用 WDButtonVariant 子集（无独立 WDIconButtonVariant 类型）✅ | ✅ |
| 9 | WDListRow | 12 / 12 | selected（C-15 #18，iOS isSelected 例外） | swipeAction 枚举未暴露（冻结 §4-9 明确容许）；leading/trailing 以组合 lambda 承载 | ✅ |
| 10 | WDSwitch | 6 主 + 8 重载（冻结 §2.10 容许 enabled 附加重载） | on（C-15 #05） | WDSwitchSize{Standard,Compact} 2/2 | ✅ |
| 11 | WDTextField | 21 / 21 | text（C-15 #03） | WDTextFieldVariant{Inset,Outline,Glass} 3/3；appearance 对象解构为具体参数（冻结 §2.11） | ✅ |

**逐件默认值抽检**：WDButton variant=Filled/size=Md/enabled=true/loading=false/role=Role.Button；WDIconButton variant=Plain/size=Md/loading=false/shape=Circle、accessibilityLabel 无默认；WDCheckbox indeterminate=false/size=Standard/accessibilityLabel=null；WDCard style=Elevated/media=null/action=null；WDSwitch description=null/size=Standard；WDTextField variant=Inset/readOnly=false/singleLine=false/maxLines=if(singleLine)1 else MAX/characterLimit=null。均与冻结文档/契约一致。

**结论**：签名/参数名/默认值/枚举 case 合计 **0 处不一致**。

---

## 2. AGENTS §10 全部 18 条反模式 + 冻结值逐项检查结论

| # | 反模式 / 冻结值 | 检查结论 | 证据锚点 |
| --- | --- | --- | --- |
| 1 | 裸值 Color(0x)/#RRGGBB/裸 dp·sp（G2） | ✅ 手写源 0 命中；实跑 `bash tools/check-token-literals.sh` exit 0（generated/** 已排除） | WDTokens.kt 生成物内 Color(0x…) 属生成物，不计 |
| 2 | 绕过 WDGlass.resolve 自选档 | ✅ 本批 11 件无玻璃档自判；WDCard Glass 走 scheme 色 | WDCard.kt:164 |
| 3 | 扩热区覆盖相邻行（案 B） | ✅ `wdTouchTarget()` 用 `layout{}` 在本节点内加对称透明内边距；max(content,44,48)，不越界 | foundation/layout/WDTouchTarget.kt:21-31 |
| 4 | 手改生成物 / api 基线（G7） | ✅ 生成物 3 文件仅含 token 生成 banner；api 基线 211 行旧快照、0 个 M2 组件命中（属 t9 明示的 apiDump 待办，非手改痕迹）；`bash tools/check-generated-self-proof.sh` exit 0 | WDTokens.kt:1-3；api/wisdom-ui.api:1-211 |
| 5 | Modifier.composed 读主题/local | ✅ 0 命中；wdTouchTarget 是纯 `layout{}` | grep 0 |
| 6 | 自造按下态 / 第二 interactionSource | ✅ 各组件单一 MutableInteractionSource；无 LocalIndication 覆写 | WDButton/WDCard/WDSwitch 等 |
| 7 | `clickable(enabled=false)` 表达 loading / 手写 semantics disabled | ❌ **违反**（见 finding R-01）：WDButton:76,110 acceptInput 含 !loading 传给 clickable；WDIconButton:89 直接 `enabled = !loading`。其余 WDSwitch/WDCheckbox 正确 | 见 §5 |
| 8 | 组件读 isSystemInDarkTheme / 静态 scheme 入口 | ✅ components 目录 0 命中 | grep 0 |
| 9 | Modifier.blur / renderEffect / 离屏层 | ✅ 0 命中 | grep 0 |
| 10 | Mode.Fixed/lineHeightMultiple/@ScaledMetric/固定高度包文字/正文 maxLines=1（U5） | ✅ 0 禁用 API；WDCard 用 heightIn(min)+wrapContentHeight；WDListRow 用 max(base,slot)；WDTextField 盒高恒 46 且正文不固定包高 | WDListRow.kt:37-51；WDTextField.kt:127-142 |
| 11 | remember 缓存派生值（derivedStateOf 除外） | ✅ 未见；`remember{ TextFieldState } / remember(characterLimit){ InputTransformation } / remember{ MutableInteractionSource }` 均为正常实例缓存 | WDTextField.kt:217,226 |
| 12 | 组件层新增 public CompositionLocal / 高频 local | ✅ components 目录 0 命中；LocalWDMemberTones / LocalWDIcons 在 foundation 且 staticCompositionLocalOf，不承载高频值 | grep 0 |
| 13 | @JvmOverloads/@JvmName/DeprecationLevel.HIDDEN/生成类 copy·componentN | ✅ src/main 0 命中（WDTextStyle.copy 为生成物既有 API，apiDump 待办） | grep 0 |
| 14 | 给既有 public 函数加带默认值参数（$default） | ✅ WDCheckbox 用重载拆分而非改既有 3 参；冻结文档 §2.5 注记一致 | WDCheckbox.kt:76-113 |
| 15 | 无条件 scale(-1f) 镜像（双重镜像） | ✅ `scale(-1f)` 仅出现在注释禁令中；图标镜像以 autoMirror 为准（LocalWDIcons.kt:13-14 文档 + icons.json 5 条 mirrorsInRTL=true） | WDIcon.kt:24；check-icons-contract.sh exit 0 |
| 16 | 库内文案/资源/内置分隔符（L-B，G13） | ✅ WDSemantics.join(separator, vararg) 无默认分隔符；components 无 stringResource/R.string/字体/strings.xml；`bash tools/check-literal-language.sh` 命中 0（M0–M2 warning 口径） | WDSemantics.kt:10-11 |
| 17 | 引新依赖（activity-compose/window/material-icons-extended 等） | ✅ build.gradle.kts 依赖批仅 material3→implementation（t9 收缩）与 tooling-preview→debugImplementation；未见白名单外新增 | build.gradle.kts:41 |
| 18 | 改批次/加组件/跳过批前冻结 | ✅ 实现 11 件 = 冻结 11 件 = 契约批次 11 件；冻结文档 §2 完整，批前签名冻结已闭合 | AGENTS §4.2 M2；冻结文档 §2 |

**冻结值专项**：
- U5 行盒公式：WDListRow `max(base,slot)`（48/44/76 令牌）、WDCard `heightIn(min){max(density.rowHeight,naturalSlotHeight)}`、WDTextField 盒高 46dp 全状态不变（WDTextField.kt:130 / Contract.kt:10）✅
- 热区 48/44：`WDSize.touchTargetMin=48`、`rowHeightCompact=44`、`wdTouchTarget()` 布局盒法；G2 不写裸 `.height(48` ✅
- 字段盒 46：`WDSize.fieldHeight=46dp`；单测 `字段盒高度恒为_46dp_且等于令牌` ✅
- U12 32 槽位：生成物 WDColors 构造器 32 个 public val（WDTokens.kt:20-51）✅（注意 api 基线仍 31 getter，属 apiDump 待办）
- 图标 44 名/注入面/autoMirror：`bash tools/check-icons-contract.sh` → contract 44 / generated 44，id/android 名/语义名/mirrorsInRTL 逐条一致 exit 0；LocalWDIcons 默认空集、缺图占位尺寸（WDIcon.kt:56 Box(size)）✅
- G9 无 m3 公开签名：`bash tools/check-public-types.sh` exit 0；api 基线未含 material3 类型描述符 ✅
- m3 import == 1：grep 全 src/main 仅 WDMaterialScheme.kt 4 行 import；G1-③ 校验通过（但存在覆盖口径缺口，见 finding R-02）✅
- generated/** 零手改：合规（无 git 仓库可复现 `git diff`，改用 banner/内容静态核对 + 基线 211 行未被扩写背书）✅
- api 基线由 apiDump 生成：当前基线 211 行、0 个 M2 组件、缺 toTextStyle 迁移后的 WDTextMetricsKt 属主；t9 已如实登记为“待 apiDump 生成、环境阻塞”。**本项不是手改违规**，但是 M3 封板前必须闭合（见 §4）

---

## 3. 单测覆盖与门禁反例自测

| 组件/项 | 测试文件 | 至少一条有意义断言 | 结论 |
| --- | --- | --- | --- |
| WDAvatar | WDAvatarTest | 五档直径走令牌 + 回退链 photo→label→icon→empty + tone 5 fixture + U6 disabled 最高 | ✅ |
| WDBadge | WDBadgeTest | 六变体枚举全量 + count 0 隐藏/>99 封顶 + U6 全序 | ✅ |
| WDButton | WDButtonSignatureTest | 13 参冻结签名逐字 + 六 variant/三 size + loading 吞输入断言 + touchTarget 链序 | ✅（但 loading 断言口径未覆盖 finding R-01，见 §5） |
| WDCard | WDCardTest | 5 参签名 + WDCardStyle 5 case 含 Media + A-13 媒体几何/占位/淡入 + U5/U6 | ✅ |
| WDCheckbox | WDCheckboxTest | C-15 checked + 默认值 + 两档 26/22dp + U6 全序 + 半选点击 | ✅ |
| WDDivider | WDDividerTest | 两档全量 + Inset 非 Indent + 内缩尺寸令牌 | ✅ |
| WDIcon | WDIconSignatureTest | 5 参签名 + LocalWDIcons 注入 + 缺图占位 + 无 scale(-1f) | ✅ |
| WDIconButton | WDIconButtonSignatureTest | 8 参签名 + Circle/RoundedSquare + 复用 WDButtonVariant + a11y 必填 | ✅（同 R-01 覆盖缺口） |
| WDListRow | WDListRowTest | 12 参签名 + 行盒 4 格 + 热区 48 + saver 三条 + U6 + WDSemantics.join | ✅ |
| WDSwitch | WDSwitchTest | C-15 on + 两档几何 51×31/40×24 + U6 全序 | ✅ |
| WDTextField | WDTextFieldTest | C-15 text + 46dp + 三变体 + U6 disabled/error 优先 | ✅ |
| t4 G2 反例自测 | WDLiteralScanTest | 手写裸值判失败 + generated 排除正例 | ✅ |
| t4 G1 反例自测 | WDM3ImportCountTest | 唯一 m3 触点通过 + 第二触点判失败 | ✅ |

**结论**：11/11 件各有 ≥1 条有意义断言；t4 门禁反例自测存在且断言方向正确。

---

## 4. t9 未验证项标注核对

| t9 项 | 标注 | 核对 |
| --- | --- | --- |
| ./gradlew assembleRelease/testDebugUnitTest | 【未验证·环境阻塞】 | ✅ 如实；依赖问题复现（.lck Operation not permitted）与报告一致 |
| ./gradlew wisdomGate 全量门禁 | 【未验证·环境阻塞】 | ✅ 如实；仅静态替代项写通过（bash -n / G2/G7/G9/G13 实跑 exit 0） |
| ./gradlew apiDump && git diff --exit-code | 【未验证·环境阻塞】 | ✅ 如实；当前 api 211 行旧基线，M2 组件 0 命中，明确列入 M0-4/M3 封板待办 |
| 环境阻塞复现命令/堆栈 | 完整给出 | ✅ 与全员告警一致 |

核对结论：**未发现把【未验证】写成"已通过"的表述**。本评审同样不把 Gradle 相关项写成通过。

---

## 5. Findings（结构化）

### R-01 · severity=high · U6 冻结值违反：loading 用 `clickable(enabled = false)` 表达

- **file/line**：
  - `android/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDIconButton/WDIconButton.kt:89`（`enabled = !loading`）
  - `android/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDButton/WDButton.kt:76,110`（`acceptInput = enabled && !loading` → `enabled = acceptInput`）
- **problem**：AGENTS §6 #20 派生②“loading 忽略 action 但留在无障碍树（**不用 enabled=false 表达**）”与 §10 #7“用 `clickable(enabled = false)` 表达 loading / 手写 `semantics { disabled() }`”均明令禁止。实现在 loading=true 且 enabled=true 时把 clickable 的 enabled 置 false → 该节点被语义标记为 disabled，与“loading ≠ disabled”冲突；M2 出口判据③明确要跑 `assertIsNotEnabled` 类语义断言。同批 WDSwitch(WDSwitch.kt:160,163)、WDCheckbox(WDCheckbox.kt:147,150) 使用正确模式（`enabled = enabled` 仅表真实 disabled，onClick 内 `if (!loading)` 吞 action），构成 M2 内部不一致。
- **requiredFix**：两处改为
  `clickable(enabled = enabled /* 仅真实 disabled 传 false */, onClick = { haptics.press(); if (!loading) onClick() /* WDIconButton 同理 */ })`；
  loading 通过 `stateDescription` + `Modifier.alpha(0f)`（节点保留、不标 disabled）表达。同步收紧断言：`WDButtonSignatureTest.loadingSwallowsInputButStaysInTree` 当前 `assertFalse(src.contains("enabled = !loading"))` 仅挡字面量，未挡 `acceptInput` 组合；应改为校验 `enabled = enabled` + `if (!loading)` 的 onClick 守卫，并加一条 loading 不产生 disabled 语义的断言。
- **resolved**：false

### R-02 · severity=low · G1-③ m3 计数只扫 import 行，与 §5.2 手工判据 grep -rl 口径不一致

- **file/line**：`android/wisdom-ui/build.gradle.kts:157,164`（`if (!line.startsWith("import ")) return@forEach` 之后才 `if (line.contains("androidx.compose.material3")) m3Hits += rel`）
- **problem**：AGENTS §5.2 的“手工判据（G1-③ 本地形态；review 要贴）”是 `grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin | wc -l` 必须 == 1（任意行命中）。当前 G1 只统计 import 行；若代码出现全限定名直用（如 `androidx.compose.material3.Button(...)` 无 import），G1 不报错而手工判据报错，门禁存在漏判面。
- **requiredFix**：在 `wisdomCheckPackageDeps` 的 doLast 内对全部行（不要求 startsWith import）独立累计 m3 文件集合，按 `{WDMaterialScheme.kt}` 精确断言；或在门禁输出中同时打印“import 口径”与“grep -rl 口径”两条数字，确保 §5.2 手工判据可被 review 直接贴用。
- **resolved**：false

---

## 6. 结论与依据

- **verdict = needs_revision**。签名/契约/枚举/单测/未验证标注均通过；R-01 是 AGENTS §6 #20 冻结值 + §10 #7 反模式的实质违反，影响 WDButton、WDIconButton 两件 M2 组件，阻断 M2 出口判据③；R-02 为门禁口径缺口（low）。
- **不是 0 条 findings**：本轮发现 2 条，依据如上。
- **复核通过项**（可直接作为后续 re-review 的基线）：11 件签名/参数/枚举 0 不一致；§10 其余 17 条反模式零命中；静态门禁 G2/G7/G9/G13/icons 实跑 exit 0；m3 import==1 且唯一文件 WDMaterialScheme.kt；L-B 零文案零资源；U5/U12/热区 48-44/字段盒 46 消费方式正确；11/11 件单测有意义断言 + t4 G1/G2 反例自测；t9 所有 Gradle 未验证项如实标注。
- **重审触发条件**：R-01/R-02 修复后，须重新提交独立评审；编译/单测/wisdomGate/apiDump 在可跑 Gradle 的环境通过前，本报告不把任何 Gradle 项写成已通过。
