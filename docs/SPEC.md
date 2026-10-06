# 迁移说明与外部引用映射（本文件由跨端工作区迁入）

> **迁移事实**：本文件是 **Android 端详细规格**。迁移前位于**跨端工作区文档集（已退役）** 的计划子目录，文件名同为 `12-android-spec.md`。
> **迁移原因**：跨端工作区目录不再保留；两端规格按端进仓（Android → 本文件；iOS 侧同批迁入其仓）。
> **正文起点（导航）**：本节之后、以 `# 12 · Android 实现规格收敛稿` 开头的**正文**（本文件第 89 行起）；本节及 §1–§5 只用于“解引用”，**不属于规格正文**。
> **正文零改写声明**：除下面两项**机械处理**外，**正文逐节原样保留**（含历史台账、验收命令、行号引用）——
> ① **移除路径前缀**：正文里指向**跨端工作区文档集**的路径被改为**裸文件名**（如 `02-ios-spec.md`、`07-summary.md`、`06:122-131`、`30-dev-plan.md`），**语义不变、仅去掉目录部分**；设计仓（`wisdomdesign/...`）与 android 仓内路径**原样保留**（它们未退役）。
> ② **一处空引用的语义补全**：原文有一句"未改动 … 下的任何入库文件"，其目录名随前缀一起被移除后会留下空代码段，已改写为描述式说法（`及跨端工作区文档集（已退役）下的任何入库文件`），**语义不变**。
> ③ **顶部新增本节**：本节及其子节只用 `#` / `###` 级标题，**不新增任何 `##` 级标题**，以免改变正文的章节计数（正文 `##` 级标题数 = **11**，与迁移前一致）。
> **已知渲染边界（本次登记）**：正文 `:898`/`:903` 处有单元格内嵌多行 kotlin 围栏（源文风格 ⇒ 表格块被围栏中断，**属检测边界、非缺陷**，只登记不改；该两处**未改动**，故本行插入后其行号顺延为 `:899`/`:904`）；原 `:1974` 的**孤立空行**缺陷已于本次修复（F48–F50 三行回到 F34–F47 同一张表，**零文字变更**，仅删 1 行空行）。
> **效力与优先级**：正文若与本仓手册 / 计划冲突，**以 [`../AGENTS.md`](../AGENTS.md)、[`DEV-PLAN.md`](DEV-PLAN.md) 为准**（逐处登记见 §3 口径注记）。
> **本文件的自检（可复制；模式用断字写法，避免本行自命中）**：
> ```bash
> test -s SPEC.md
> test "$(grep -c '^## ' SPEC.md)" -eq 11                                                             # 与迁移前一致
> test "$(grep -cE 'd[o]cs/impl-plan|d[o]cs/structure-discussion|[.][.]/[.][.]/docs' SPEC.md)" -eq 0  # 路径引用清零
> test "$(grep -cE '^#{2,3} .*#{2,3} ' SPEC.md)" -eq 0                                                # 标题无粘连
> ```

### 1. 数字代号对照表（正文里每个外部代号 → 本仓等价物）

> 读法：**"原是什么"= 迁移前的文档身份；"现在在哪"= 可执行结论在本仓的位置**。本表就是正文全部 `NN §x` 引用的**解引用入口**；裸节号（如 `§3.1.2`）一律在本文件内部，节号与迁移前**完全一致**。

| 代号 | 原是什么（迁移前） | 正文里的引用形态 | **现在在本仓哪里**（可执行结论） |
| --- | --- | --- | --- |
| `00` | iOS 侧初稿（`00-ios-draft.md`） | 定点事实引用 | 仅追溯：[`../AGENTS.md`](../AGENTS.md) §12（台账索引）、[`ARCHITECTURE.md`](ARCHITECTURE.md) §16（修订台账） |
| `01` | 设计：基础（颜色 / 语义色 / 玻璃材质，`01-foundation.md`） | §7.2 六档玻璃、§3.3 语义色 | [`../AGENTS.md`](../AGENTS.md) **§6.1.1**（玻璃六档矩阵）+ [`ARCHITECTURE.md`](ARCHITECTURE.md) **§8.1** |
| `01-basic` / `02-advanced` | 设计：组件规格（仍在设计仓 `specs/` 内） | 组件几何与交互出处 | **未退役**（设计仓真源，可直接查）；本仓落点 = `../AGENTS.md` §6 + 本文件 §2 |
| `02` | iOS 侧规格（`02-ios-spec.md`） | §2.10 批次列、§2.11 枚举命名 | [`DEV-PLAN.md`](DEV-PLAN.md) **§3**（批次分配 = "批次"列的落端口径）+ **§5 第 21 行**（三枚举 DIR-1） |
| `03` | 设计：平台映射 / 性能与发布（`03-platform-mapping.md`、`03-perf-release.md`） | §1.2 配额（硬上限） | [`../AGENTS.md`](../AGENTS.md) **§6.1.2**（配额 7 条）+ [`ARCHITECTURE.md`](ARCHITECTURE.md) **§8.2** |
| `04` | 设计：架构（`04-architecture.md`） | 命名真源 / Modifier 位置 / 版本策略 | [`ARCHITECTURE.md`](ARCHITECTURE.md)（§2 分层、§5 主题、§6 组件模型、§10 版本） |
| `06` | 设计：无障碍（`06-accessibility.md`） | §5.1/§5.2 对比度、§6 减弱动态、§8 状态形状 | [`../AGENTS.md`](../AGENTS.md) **§6.1.3**（对比度门槛）；[`ARCHITECTURE.md`](ARCHITECTURE.md) **§9.1a**（不达标组合）+ **§7.1 节**（减弱动态四类点名） |
| `07` | 跨端契约终稿（`07-summary.md`：U1–U14 / F1–F20 / 三层门禁 / 路线图） | `07 §1.2`、`07 U5`、`07 §6.4` | [`DEV-PLAN.md`](DEV-PLAN.md) **§9.2**（U/F 清单与跨端依赖）+ **§4**（三层门禁）+ **§5**（U 系列冻结值） |
| `08` | 用户决策（`08-decisions.md`：10 项决策 + 8 条实现硬约束） | `08 U4`、`08 §2-8`、`08 §3-3` | [`DEV-PLAN.md`](DEV-PLAN.md) **§5**（冻结值）+ **§2.1.1**（A1+A3 六步 = `08 §2-8` 的落地）+ **§8.2**（未验证项 = `08 §3-x`） |
| `09` | 设计：布局（`09-layout.md`：密度 60/44、触控目标、表单页规则） | §4 规则块、§96/§101 | [`ARCHITECTURE.md`](ARCHITECTURE.md) **§8.3**（密度三规则）+ [`../AGENTS.md`](../AGENTS.md) **§6 第 1–2 行** |
| `10` / `11` | Android 初稿（`10-android-draft.md`）与逐条裁决（`11-android-review.md`：AR 编号来源） | 文首"收敛输入"、AR/LR 条目 | [`ARCHITECTURE.md`](ARCHITECTURE.md) **§16**（AR/LR 条目处置台账）+ [`../AGENTS.md`](../AGENTS.md) **§12**（台账索引） |
| `12` | **本端规格 = 本文件**（迁移前的同名文件） | `12 §3.1.2`、`12 §2.10` | **本文件自身**；**节号与迁移前完全一致**（正文零改写），无需换算 |
| `20` | iOS 侧 leader review（`20-ios-leader-review.md`：**F 编号唯一分配表** §3 + §3-附记） | `20 §2.4-2`（F47）、`20 §3` | [`DEV-PLAN.md`](DEV-PLAN.md) **§9.2**（F 系列节选：F41/42/43/45/47/51）+ [`../AGENTS.md`](../AGENTS.md) **§6 第 21 行**（F47）、**§2 唯一真源表**（F 注册表行） |
| `21` | Android 侧 leader review（`21-android-leader-review.md`：LR-01–LR-28 + D1–D9 裁决） | `21 §1`、`21 §2.1/§2.2` | [`DEV-PLAN.md`](DEV-PLAN.md) **§7.1**（M0-1 清单现值 = 裁决落地）+ **§5**；[`../AGENTS.md`](../AGENTS.md) **§12** |
| `22` | 设计：玻璃补充（`12-b22-glass.md` / `22-glass.md`：生成期断言、深色 Tab 方案 A） | §3/§4/§7 | [`../AGENTS.md`](../AGENTS.md) **§6.1.1** + [`ARCHITECTURE.md`](ARCHITECTURE.md) **§8.1** |
| `30` | 跨端总计划（`30-dev-plan.md`：进程骨架 / 批次 / 冻结值口径 / 回写清单 / 待给值 / 风险 / 发布） | `30 §3.0`、`30 §5.5`、`30 §5.9`、`30 §5.10`、`30 §6.3.4` | 见下方 **§2 逐节换算表**（全部落在 [`DEV-PLAN.md`](DEV-PLAN.md)） |
| `40` | C-15 受控值参数名唯一表（`40-contract-names.md`） | `40 §1`、`40 §2.1`、`40 §5`、`40 §6` | [`../AGENTS.md`](../AGENTS.md) **§6 第 15 行**（14 行 Android 改名 + 2 个例外）+ [`DEV-PLAN.md`](DEV-PLAN.md) **§5 第 14 行**；落库形态 = 契约 `contracts/<component>.yaml` 的 `params[].name` |
| `25` | 评审 / 裁决报告（设计与跨端侧，已退役） | `25 §3`（S-3 裁定） | 仅追溯：[`ARCHITECTURE.md`](ARCHITECTURE.md) §16 + [`../AGENTS.md`](../AGENTS.md) §12（台账）；**无动作依赖** |
| `24` / `26` / `27` / `62` / `63` / `64` / `66` | 其他评审 / 复核报告（设计与跨端侧） | **本文件正文未出现** | 无需换算；若在别处遇到：`ARCHITECTURE.md` §16 + `../AGENTS.md` §12（台账）、`DEV-PLAN.md` §7（DF/XR 编号） |

### 2. `30`（跨端总计划）逐节换算表

| 迁移前 | 内容 | 本仓等价物 |
| --- | --- | --- |
| `30 §2` | 里程碑表 M0–M6 | [`DEV-PLAN.md`](DEV-PLAN.md) §2.1–§2.7 |
| `30 §3.0` | 进程骨架 I-1…I-5 / 出口 / 门禁 | [`DEV-PLAN.md`](DEV-PLAN.md) §2.0 + §4 |
| `30 §3.1` | 37 件批次分配与关键路径 | [`DEV-PLAN.md`](DEV-PLAN.md) §3 |
| `30 §3.3` | 批前签名冻结门 | [`DEV-PLAN.md`](DEV-PLAN.md) §3.3 |
| `30 §5.5` / `§5.8` | 用户决策后的冻结值口径 / 换肤（层一） | [`DEV-PLAN.md`](DEV-PLAN.md) §5（第 1–18 行）+ [`../AGENTS.md`](../AGENTS.md) §6 |
| `30 §5.9` | 回写清单 P7–P12 | [`DEV-PLAN.md`](DEV-PLAN.md) §6 |
| `30 §5.10` | 设计待给值 / 待签发 D-1…D-16 | [`DEV-PLAN.md`](DEV-PLAN.md) §7 + §7.1 |
| `30 §6` | 风险登记 + 未闭合项台账 | [`DEV-PLAN.md`](DEV-PLAN.md) §8（R 系列 + V1–V18） |
| `30 §7` | 变更与发布节奏 | [`DEV-PLAN.md`](DEV-PLAN.md) §10 |
| `30 §8.2` | 「勘误（t50）」：封板批 12 件 / 20-20 于 M5 出口 | [`DEV-PLAN.md`](DEV-PLAN.md) §3.1 + [`../AGENTS.md`](../AGENTS.md) §4.2 |

### 3. 口径注记（**正文不改**；读时以第三列为准）

| 正文位置 | 历史说法 | **以本仓哪节为准** |
| --- | --- | --- |
| §3.1.2 标题与 LR-11 行 | 自称"**唯一一张三仓 M0-1 清单**" | [`../AGENTS.md`](../AGENTS.md) **§2 唯一真源表**（XR-04 处置：本仓与规格均不自称唯一）；判据仍是"清单 **21 行**" |
| §8.1 D1 行 / §8.2 C1 行 | 案 B 前的行高口径（已在 `t63` 就地修正） | [`DEV-PLAN.md`](DEV-PLAN.md) **§5 第 1–3 行** + **§6 的 P7 行** |
| §3.2.2 品牌换肤 | 已由 `t63` 改为"已决 = 层一" | [`DEV-PLAN.md`](DEV-PLAN.md) **§5 第 5 行** + [`../AGENTS.md`](../AGENTS.md) **§6 第 5 行** |
| 文首"收敛输入"中的 `00` / `10` / `11` | 迁移前工作稿 | 仅追溯（见 §1）；**无动作依赖** |

### 4. 自包含抽查（6 处关键结论：撤掉跨端文档集后是否仍有等价表述）

| # | 关键结论 | 本仓等价表述（三份文档均可查） |
| --- | --- | --- |
| 1 | 行高**可见内容 44**（单值键）+ **Android 布局盒 48** = 44 + 上下各 2dp | `../AGENTS.md` **§6 第 1–2 行**；`DEV-PLAN.md` **§5 第 1–2 行**；`ARCHITECTURE.md` **§9.3** |
| 2 | **AR-67 是补充解释**：原两条断言（`(compact,1)==48±0.5`、`(compact,2)==76±0.5`）原样成立，另**新增**"可见内容 44 ± 0.5" | `../AGENTS.md` **§8**（含 AR-67 写法）；`DEV-PLAN.md` **§6 的 P7 行**；`ARCHITECTURE.md` **§9.3 / §15**（**结论等价**：布局盒 48 + 可见内容 44 ± 0.5；该文档未使用 AR-67 编号） |
| 3 | **U12 = 32 槽位**（含 `text.disabled`），浅深成对 | `../AGENTS.md` **§6 第 4 行**；`DEV-PLAN.md` **§5 第 4 行**；`ARCHITECTURE.md` **§4** |
| 4 | **`schemes` 层一三条"不支持"**（运行时任意令牌 / 服务端下发 / 逐槽位覆盖）+ `--schemes` | `../AGENTS.md` **§6 第 5 行**；`DEV-PLAN.md` **§5 第 5 行**；`ARCHITECTURE.md` **§5** |
| 5 | **`F51`** = 列表行距差异（Android 48 / iOS 44），U = 可见内容 44 ± 0.5 | `../AGENTS.md` **§6 第 3 行**；`DEV-PLAN.md` **§5 第 18 行**；`ARCHITECTURE.md` **§9.3 / §15** |
| 6 | **`apiCheck` 当前为红**（27 getter vs 源码 31）+ **A1 + A3 同提交的 6 步转绿** | `../AGENTS.md` **§9.3**（六步表）；`DEV-PLAN.md` **§2.1.1**（六步）；`ARCHITECTURE.md` **§11**（只给 gate 拓扑与 `apiCheck` 项，**未写六步**） |

**结论**：**6/6 均有等价表述**（抽查命令与计数见交付报告）。

### 5. 无本仓等价物、仅供追溯的输入（不阻塞施工）

| 对象 | 说明 |
| --- | --- |
| `00-ios-draft.md` / `10-android-draft.md` / `11-android-review.md` | 迁移前的工作稿与逐条裁决；结论已落本文件与 `ARCHITECTURE.md` §16 台账；**不再单独迁入** |
| 设计仓的组件规格（`specs/01-basic.md`、`specs/02-advanced.md`） | **未退役**，仍在设计仓内可直接查 |
| 设计仓其余文档（颜色 / 布局 / 无障碍 / 玻璃等） | **未退役**；本仓只引用其结论（见 §1 的 `01`/`03`/`06`/`09`/`22` 行） |

---

# 12 · Android 实现规格收敛稿：可直接开工的 Android 实现规格

> 任务：`t6 [android-spec]` → **`t11 [repair-round-2]`** ｜ 作者：**android-dev**（Android 技术开发）｜ 本轮 attempt：`d10a65c7-abe8-49e3-9cbd-95534981eea4`
> **本轮修复输入**：`21-android-leader-review.md`（LR-01–LR-28：blocker 3 / high 8 / medium 11 / low 6）+ 船长路由的 `20-ios-leader-review.md` §3（**F 编号唯一分配表**）与 §2.4（**两处显式改名**）。**F 编号已按 `20` §3 重编号，存疑项 2 条（见 §4.1.1）**。
> 收敛输入：`10-android-draft.md`（1819 行，我方初稿）+ `11-android-review.md`（604 行，android-lead 的逐条裁决：接受 38 / 加约束 32 / 反驳 16 = 86 条，blocker 8 / high 20）+ `07-summary.md` 与 `08-decisions.md`（生效裁决）。
> 效力：**本稿是 Android 侧的实现规格（可直接开工）**。凡与 `08-decisions.md` 冲突以 `08` 为准；凡 `11-android-review.md` 判为 blocker/high 的，本稿**全部按裁决落地**（不接受"再讨论一轮"）；`10-android-draft.md` 中被本稿改写的地方，一律以本稿为准。
> 纪律：① 本轮只写本文件，未改动 `iOS/`、`android/`、`wisdomdesign/` 及**跨端工作区文档集（已退役）**下的任何入库文件（验收命令见文末 §10.4）；② 每条结论给**签名级 Kotlin / 命令 / 断言**与**落点文件路径**；③ 转引评审证据标 `【11 §0.1 Ex】`，我本轮亲手核过的仓库事实标 **[读]**；④ 未闭合项标 **【待实测】**。

---

## 0. 本稿的地位、编号体系与"从旧稿到本稿的 delta"

### 0.1 与三份输入的关系

| 输入 | 本稿怎么用它 |
| --- | --- |
| `11-android-review.md` | **主导**。§6 逐条回应 AR-01–AR-86；§6.2 给 28 条 blocker/high 的"改后签名/命令/断言" |
| `10-android-draft.md` | 结构与覆盖面被继承；被裁决改写的地方在 §0.3 列出 delta |
| `07-summary.md` / `08-decisions.md` | 不可协商的上位约束；§5 逐条核对 8 条硬约束，§4 逐行登记 F/U 边界 |

### 0.2 编号体系（**解决旧稿"R"编号一名多用的问题**，采纳 AR-06）

| 前缀 | 含义 | 定义位置 |
| --- | --- | --- |
| **G1–G13** | 门禁规则（机器检查 task / 单测 / 脚本）；G13 = L-B 标点/词序字面量（LR-08③） | §1.5.2 / §1.5.3 |
| **L1–L8** | 组件布局与测量规则 | §2.8.1 |
| **C1–C15** | 组件行为规则（"组件不该管" 12 条 + 3 条禁则） | §2.8.2 |
| **D1–D6** | `11-android-review.md` §6.2 报备的需上位裁决项 | §8.1 |
| **V1–V14** | 未闭合验证项 | §9.1 |
| **A-1…A-12** | 本稿的未决项（含归属角色 + 是否阻塞 M0） | §9.2 |
| **F21–F50** | 与 iOS **允许不同**的差异登记——**编号由研发 Leader 在 `20-ios-leader-review.md` §3 + §3-附记集中分配**（F1–F20 不动，F21 起两端不得自行取号） | §4.1 |
| **U1–U17** | 必须统一项（U1–U14 沿用；U15–U17 进契约） | §4.2 |

### 0.3 从 `10-android-draft.md` 到本稿的 delta（按裁决改写的地方）

| # | 旧稿 | 本稿 | 依据 |
| --- | --- | --- | --- |
| Δ1 | `Modifier.wdTouchTarget()` 调 M3 `minimumInteractiveComponentSize()` | **自研 `layout` 修饰符**（零 m3 依赖） | AR-66 / B1 |
| Δ2 | R3 白名单 = "唯一桥接点 `WDMaterialScheme.kt`"，但触控另有一处 m3 | **全库 m3 import 计数 == 1**，由 **G1 的第三条子断言（G1-③）** 断言 | AR-03 / B1 |
| Δ3 | `WDHaptics` 是 `@Composable` 函数集合；`WDAnnouncer` 是无 Context 的 `object` | **接口 + `rememberWDHaptics()` / `rememberWDAnnouncer()` + 可注入 local** | AR-75 / B2 |
| Δ4 | `compact` 行高 44，用扩热区补足 | **案 B（用户裁决）**：**可见内容 44（两端同值）+ Android 布局盒 48（含上下各 2dp 透明内边距，由 `Modifier.wdTouchTarget()` 实现，热区 = 布局盒、不覆盖相邻行）** | AR-67（**补充解释**，非取代）/ D1 |
| Δ5 | `WDComponent` 6 个出口 | **10 个出口**（补 `switchTrack/toastIn/listStagger/skeletonShimmer`）+ 归属规则 | AR-61 / B4 |
| Δ6 | PR 门禁第 ④ 条是注释；单测跑 `testReleaseUnitTest` | **`wisdomGate` 复合任务 + `testDebugUnitTest` + G6 真断言** | AR-20 / B5+B6 |
| Δ7 | `WDTheme(layout = WDLayout(...))` 每次新建、`ColorScheme` 每次 new | **`WDLayout` 值等 + companion 单例 + `remember(colors, darkTheme)`** | AR-45 / B7 |
| Δ8 | 7 个 `CompositionLocal`，`WDTheme` 只 provide 6 个 | **`WDTheme` 7 个注入参数（+`icons`/`motionScale`）**；**不引 `LocalWDAppearance`** | AR-44 / AR-63 |
| Δ9 | `WDSheet` / `WDSheetState` / `rememberWDSheetState` / `WDBottomSheetDetent` | **`WDBottomSheet` / `WDBottomSheetState` / `rememberWDBottomSheetState` / `WDBottomSheetDetent`**（U1 违例修正） | AR-86 |
| Δ10 | `WDButton` 无 `role`/`onClickLabel`，但自证表承诺 `assertIsButton()` | 签名补两参；自证表加"前置条件"列 | AR-29 / AR-81 |
| Δ11 | 手写 `semantics { if (!enabled) disabled() }`；`stateDescription = loadingLabel` | `disabled` 收口到 `clickable`；`loadingLabel?.let { stateDescription = it }` | AR-49 |
| Δ12 | `Modifier.scale(scaleX = -1f)` 做 RTL 镜像 | **以 `ImageVector.autoMirror` 为准**，库内只兜底 | AR-76 |
| Δ13 | `const val → val` 的理由 = "apiDump 看不出变化" | 理由改为**避免内联**（api 文件对 const/非 const 都不打印值） | AR-60 |
| Δ14 | 行盒断言在令牌层 | **令牌层 + 布局层双层**（新增 `WDLineBoxTest`） | AR-62 |
| Δ15 | Kover "public API 行覆盖 ≥80%" | Kover 按包过滤的行覆盖（`generated`/`internal` 排除） | AR-23 |
| Δ16 | `Modifier.wdGlass(level)` 允许读 local | **只接参数**，解析在组件体内 | AR-43 |
| Δ17 | `WDPressIndication` 有 `equals/hashCode` 覆盖 | 删掉（`object` 天然身份相等） | AR-48 |
| Δ18 | `WDSemantics` 有 `stateValue`（恒等函数） | **删除**；保留 `join` / `positional` | AR-84 / D5 |
| Δ19 | 规则编号 R1–R8 / R1–R7 / R13 三名混用 | **G / L / C 三套编号** | AR-06 |
| Δ20 | 依赖检查脚本有 3 个 bug（白名单串用、不扫 `src/debug`、伪函数 `checkNoPackageCycle`） | 换成可编译骨架（`DepRule` + 各自 `allowlist` + 双扫描根 + 真实环检测） | AR-04 |

### 0.4 第 2 轮修复清单（`21-android-leader-review.md` 的 LR-01 – LR-28 → 本稿落点）

> 口径：本轮只改这 28 条的落点，**不动已达标部分**（评审 §0/§6 已确认 `WDButton`/`WDListRow`/`WDBottomSheet` 的契约化成果、`wisdomGate` 复合形态、M0 六步序列、`D1–D7` 站队与 §9 登记方式均为"可开工"）。

| LR | 严重度 | 一句话 | 本稿落点 |
| --- | --- | --- | --- |
| LR-01 | blocker | `WDIcon(tint = LocalContentColor.current)` 引 m3（`LocalContentColor` 只在 material3） | §2.3 + §2.5.3（新增库内 `LocalWDContentColor`，注入点 7→8）+ §2.9.1 + G1-③ 的单测 |
| LR-02 | blocker | `SemanticsProperties.TextLayoutResult` **不存在** | §2.8.1（改用 `SemanticsActions.GetTextLayoutResult` 动作 + 可编译骨架） |
| LR-03 | blocker | `LocalAccessibilityManager.isHighContrastTextEnabled` 不存在；框架方法 API 34+ | §3.2.3（`getSystemService(AccessibilityManager::class.java)` + `SDK_INT >= 34` 守卫 + 订阅决策）+ §3.5.3（U10 含 `contrast`） |
| LR-28 | high | U5 断言形式比 iOS 弱（放大档恒真、默认档无容差） | §2.8.1（两句定稿 + `tol` 分端 + fixture 矩阵缺失即 fail）+ §8.1-D2 |
| LR-04 | high | M0 出口⑨ 要 E2 数字但 `:demo` 在 M1（撞 U2） | §7.1（⑨ 改口径：脚本 + 空骨架 + 口径成文；数字在 M1 出口给） |
| LR-05 | high | E1'「每组件 ≤6 KB」不可测 | §1.5.6（改为「层包/批次 dex 增量 ≤ 6 KB × 该批组件数」+ M1 脚本名） |
| LR-06 | high | `WDTextField` 缺变体与 46 高 | §2.2②（`variant: WDTextFieldVariant = Inset`）+ §3.1.2（`size.field-height = 46` 进 M0-1） |
| LR-07 | high | `label` 未必填（违硬约束 5） | §2.2②（改 `label: String` 必填 + 理由） |
| LR-08 | high | F21–F35 两端编号互撞；11 行缺失；无 L-B 标点/词序机器检查 | §4.1（**统一 F21–F50 注册表**，按 `20 §3` + §3-附记 的唯一分配；两端形态两列 + 11 行补齐）+ **G13** + §7.1 M0-6 |
| LR-09 | high | `WDAppearance` 字段集 / `WDGlassSpec` 未定义；缺 `contrast` | §3.2.3（完整字段表）+ §3.5.3（`WDGlassSpec` + `resolve` 输入含 `contrast`）+ §2.9.1 |
| LR-10 | high | detent 家族类型名/case 名与 iOS 定稿不一致；`size.sheet.*` 未进 M0-1 | §2.1 改名表（`WDBottomSheetDetent(Half/Large)` + `WDBottomSheetDetents(All/Fixed)`）+ §3.1.2 + §9.2-A-14 关闭 |
| LR-11 | medium | M0-1 清单两行无可执行内容；未吸收 iOS CR-10 的 12 行 | §3.1.2（**唯一一张三仓 M0-1 清单**，四列：旧键→新键→值→决定人） |
| LR-12 | low | 「10 键/12 键」口径混用 | §3.1.3（统一为「既有 10 + 新增 2 = 12 个 `WDComponent` 出口」） |
| LR-13 | medium | G2 未排除 `foundation/generated/**` | §1.5.3 G2（扫描根 = 手写文件；`foundation/tokens/**` 仍符号级白名单，含 `WDElevation.*`） |
| LR-14 | medium | m3 计数期望串带 `wisdom-ui/` 前缀 ⇒ 恒红 | §1.1.3（期望值改正 + 反例单测） |
| LR-15 | medium | `G3` 一号两名（预览检查 vs m3 计数） | §0.3-Δ2 / §1.1.3 / §1.5.3 / §1.6 判据命令（统一为「**G1 的第三条子断言**」G1-③） |
| LR-16 | medium | 角色表含不存在的 `Role.Slider` | §2.6.3（删 Slider；滑动条走 `progressBarRangeInfo` + `setProgress`；9 值写死 + 反射单测） |
| LR-17 | medium | M2–M6 无「批前签名冻结」门 | §7.2（每批前置一步签名冻结） |
| LR-18 | medium | E1/E2 与 `08` U3 对不上 | §1.5.6 + §5（明写映射：08 的 E1 ≡ 本稿 E2） |
| LR-19 | medium | 新增导出类型不在稳定性清单；值等规则未写 | §2.9.1 + §3.1.3（`WDElevationSpec`/`WDElevationLayer`/`WDGlassSpec` 进 G5 + 逐层值等） |
| LR-20 | medium | shell 检查无 Gradle 包装；G8 正例无生产者 | §1.5.2（`tasks.register<Exec>` + `G8.dependsOn("assembleDebug")`）+ §1.2.3 命令块 |
| LR-21 | low | `knownUnstableArguments` 基线无生产者 | §7.1 M0-6（首次生成并入 + 谁写谁更新） |
| LR-22 | low | `dragBy` 非 suspend 却调 `snapTo` | §2.2④ + §3.4.1（同步写 `mutableFloatStateOf`；落档交给 `Animatable`） |
| LR-23 | low | 内部链未写 `clickable` 位置 ⇒ 热区可能等于视觉尺寸 | §2.5.1（完整链）+ §7.3（"热区 ≥48"的前置条件） |
| LR-24 | medium | 37 行默认值/case 表不可生成；2 处默认值不一致 | §2.10（新建：37 行 Android 默认值 + case，逐行判 U/F；`showsSeparator` 与行高公式已对齐 iOS） |
| LR-25 | medium | U3 槽位词表两端不同步 | §2.4（**21 名**共享槽位词表 + 37 行「组件 → 槽位名 ｜ 无」表 + 5 个补入名的形态/归属副表）；**R2A-01 已在 t18 完成同步** |
| LR-26 | low | 触觉/播报「未注入 ⇒ 平台实现」未写；缺两条单测 | §3.6.5（KDoc/README + 两条单测） |
| LR-27 | low | 阴影映射是需二次推导的公式；无评审标记 | §3.1.3（显式 5 行映射表 + 标"待 M2 视觉评审"） |

**两处"采纳但不回退"已由 Leader 裁定**（`21` §2.2）：AR-75 的可空默认值（+ LR-26 两条随附要求）、AR-74 的阴影近似映射（+ LR-27 两条随附要求）——本稿按裁定落齐。

**F 编号已按 `20-ios-leader-review.md` §3 重编号（存疑项 2 条，见 §4.1.1）**；同时按该文件 **§2.4 的两处显式改口**落了两处改名：① detent 家族前缀统一为 **`WDBottomSheet*`**（`WDBottomSheetDetent` / `WDBottomSheetDetents`；case 名 `Half`/`Large` ↔ 规范名 `half`/`large`，两端都不得出现 `WDSheet*`）；② U10 输出类型统一为 **`WDGlassResolution`**（旧名 `WDGlassEffective` 作废），U10 的**必选输入**改为 **`WDTextLevel`**（文字级别），Android 原有的 `WDGlassLevel` 六档降为**额外输入**（编号待定，见 §4.1 存疑项）。

---

## 1. 工程基建与协作规范


### 1.1 模块划分与依赖方向

#### 1.1.1 模块与逻辑层（沿用，补一条可测判据）

| 模块 | 插件 | 发布 | 参与 `apiDump` | 状态 |
| --- | --- | --- | --- | --- |
| `:wisdom-ui` | `com.android.library` | ✅ Maven Central | ✅ 唯一 ABI 基线 | 现状 `android/settings.gradle.kts:25` [读] |
| `:demo` | `com.android.application` | ❌ | ❌ | M1（U2） |
| `:benchmark` | `com.android.test` | ❌ | ❌ | M1 之后（U2） |

**不拆** `:wisdom-tokens` / `:wisdom-ui-api` / `:wisdom-ui-impl`（生成物直接引用 `androidx.compose.ui.graphics.Color` / `ui.unit.Dp` / `animation.core.SpringSpec` / `runtime.Immutable`：`android/.../foundation/Generated/WDTokens.kt:7-15` [读]）。

**新增可测判据（AR-01）**：**全库 `androidx.compose.material3` 的 import 计数必须 == 1**，且该处必须是 `foundation/theme/WDMaterialScheme.kt`。这是比"四条拆分判据"更早的报警器：一旦计数 ≥2，说明又长出一条 m3 依赖，U4 的"拆 artifact 时能删干净"立刻不成立。

```bash
# 判据命令（G1-③ 的本地形态）
grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin | tee /tmp/m3.txt | wc -l   # 必须 == 1
cat /tmp/m3.txt                                                                             # 必须只含 WDMaterialScheme.kt
```

#### 1.1.2 层包（沿用）

`io.github.wlunc.wisdom.foundation.generated`（只允许生成器写入）/ `…foundation.{tokens,typography,theme,material,motion,accessibility,icons}` / `…components.{primitives,composites}` / `…internal`。运行期内部实现进 `…internal`；构建期工具进 `android/tools/*`（M0 不建 convention plugin，抽出判据见旧稿）。

#### 1.1.3 依赖方向检查（**换掉旧稿的伪代码**，采纳 AR-04）

```kotlin
// android/wisdom-ui/build.gradle.kts（M0 落地；G1 = wisdomCheckPackageDeps）
private data class DepRule(
    val fromPath: Regex,
    val forbiddenImport: Regex,
    val allowlist: Set<String>,          // 按规则各自持有（AR-04 ①）
)

private val WISDOM_SRC_ROOTS = listOf("src/main/kotlin", "src/debug/kotlin")   // AR-04 ②：debug 也要扫

val wisdomCheckPackageDeps by tasks.registering {
    group = "verification"
    val srcRoot = layout.projectDirectory
    inputs.files(WISDOM_SRC_ROOTS.map { srcRoot.dir(it) })
    doLast {
        val rules = listOf(
            // 方向：foundation / internal 不得反向依赖 components
            DepRule(Regex("(foundation|internal)/"), Regex("""^import\s+io\.github\.wlunc\.wisdom\.components\."""), emptySet()),
            // primitives 不得依赖 composites
            DepRule(Regex("components/primitives/"), Regex("""^import\s+io\.github\.wlunc\.wisdom\.components\.composites\."""), emptySet()),
            // m3 只允许在 WDMaterialScheme.kt（AR-03：全库计数 == 1）
            DepRule(Regex(".*"), Regex("""^import\s+androidx\.compose\.material3\."""), setOf("foundation/theme/WDMaterialScheme.kt")),
        )
        val violations = mutableListOf<String>()
        val m3Hits = mutableListOf<String>()
        WISDOM_SRC_ROOTS.forEach { root ->
            val base = srcRoot.dir(root).asFile
            if (!base.exists()) return@forEach
            base.walkTopDown().filter { it.extension == "kt" }.forEach { f ->
                val rel = f.relativeTo(srcRoot.asFile).invariantSeparatorsPath
                f.readLines().withIndex()
                    .map { (i, line) -> i + 1 to line.substringBefore("//").trim() }   // AR-04 ③：先剥 `//`
                    .filter { (_, line) -> line.startsWith("import ") }
                    .forEach { (lineNo, line) ->
                        rules.forEach { rule ->
                            if (rule.fromPath.containsMatchIn(rel) &&
                                rule.forbiddenImport.containsMatchIn(line) &&
                                rule.allowlist.none { rel.endsWith(it) }
                            ) violations += "G1 $rel:$lineNo  $line"
                        }
                        if (line.contains("androidx.compose.material3")) m3Hits += rel
                    }
            }
        }
        check(noPackageCycle(srcRoot)) { "G1 包级循环依赖：${packageCycleReport(srcRoot)}" }   // 真实函数，非伪函数
        // G1 的第三条子断言（LR-15：不得再叫 G3；G3 = wisdomCheckMainNoPreview）
        // 期望串 = rel 的真实形态（rel = f.relativeTo(srcRoot)，srcRoot = 模块目录 ⇒ **不带** "wisdom-ui/" 前缀，LR-14）
        check(m3Hits.distinct() == listOf("src/main/kotlin/io/github/wlunc/wisdom/foundation/theme/WDMaterialScheme.kt")) {
            "G1-③ 全库 m3 触点必须恰好 1 个（WDMaterialScheme.kt），实测：${m3Hits.distinct()}"
        }
        check(violations.isEmpty()) { "依赖方向违例：\n${violations.joinToString("\n")}" }
    }
}
```

- `noPackageCycle(base): Boolean` / `packageCycleReport(base): String` 由 `tools/checks/package-graph.kt` 的 `internal fun` 提供（建"包 → 被 import 包"有向图 + DFS 找环），**配 1 个反例单测**（旧稿 S2 已登记：自研脚本必须能被自己测红）。
- **`import` 提取先剥 `//` 注释**；`"""` 块内的 import 字样不构成 import（块注释场景由 detekt 的 `ForbiddenImport` 兜第二层）。
- 第二条网：detekt `ForbiddenImport`（`androidx.compose.material3.**`，`excludes: ['**/foundation/theme/WDMaterialScheme.kt']`）。

### 1.2 构建与包管理

#### 1.2.1 版本目录（`android/gradle/libs.versions.toml`）

**AR-08 修正**：文档里不得留 `<M0 锁定>` 这类占位符——**落地顺序固定为"先在 M0 首次接入时锁定版本号并写回 catalog，再提交"**。`gradlePluginPortal()` 已在 `android/settings.gradle.kts:11` [读]（ktlint/detekt 的插件标记可解析）。

| 段 | 键 | 动作 |
| --- | --- | --- |
| `[versions]` | `agp 8.13.2` / `kotlin 2.4.20` / `composeBom 2026.06.01` / `vanniktechPublish 0.37.0` / `binaryCompatibility 0.18.2` / `junit 4.13.2` | 保持（`libs.versions.toml:2-9` [读]） |
| `[versions]` | `wisdom` | ➕ M0-7：`-Pwisdom.version` > `WISDOM_VERSION` 环境变量 > 本键（旧稿已给代码） |
| `[versions]` | `ktlint` / `detekt` / `robolectric` / `androidxTestCore` / `screenshot` | ➕ **M0 首次接入当日锁定并写回**（AR-08：不留占位） |
| `[libraries]` | `compose-ui-test-junit4` / `compose-ui-test-manifest` / `robolectric` / `androidx-test-core` | ➕ M1（AR-20 / §3.5 的测试依赖集） |
| `[plugins]` | `android-application`（M1）/ `android-test`（M1 后）/ `ktlint` / `detekt` / `screenshot` | ➕ |

**版本参数化验收（AR-09，两条都要贴输出）**：

```bash
./gradlew :wisdom-ui:generatePomFileForMavenPublication                       # POM 版本 == catalog 的 wisdom 键
./gradlew :wisdom-ui:generatePomFileForMavenPublication -Pwisdom.version=1.0.0 # POM 版本 == 1.0.0
```

#### 1.2.2 多入口（**源集与变体按 AR-10 / AR-20 修正**）

| 入口 | 物理位置 | 进 AAR？ | 变体 | 说明 |
| --- | --- | --- | --- | --- |
| 库公开 API | `src/main/kotlin` | ✅ | release/debug | 37 组件 + foundation |
| 调试断言 | `src/debug/kotlin/…/internal/WDDebugAssertions.kt`（真实现）+ `src/release/kotlin/…`（同签名 no-op） | debug ✅ / release 只含 no-op | 两变体 | **AR-30**：库无 `BuildConfig`（造它会污染 ABI），不得打开 `buildFeatures.buildConfig` |
| 预览 | `src/debug/kotlin/…/components/**/*Preview.kt` | ❌ | debug | **唯一物理位置**（AR-10 ②：不许两处各写一份） |
| 截图测试 | **物理同预览目录**，通过 `sourceSets` 挂进 screenshot 源集 | ❌ | debug | AR-10 |
| 单测 | `src/test/kotlin` | ❌ | **debug（PR 只跑 `testDebugUnitTest`）** | AR-20 / B6 |
| demo | `:demo` | ❌ | — | M1 |
| benchmark | `:benchmark` | ❌ | — | M1 后 |

```properties
# android/gradle.properties（AR-10 ①）
android.experimental.enableScreenshotTest=true
```

```kotlin
// android/wisdom-ui/build.gradle.kts（AR-10 ②：预览只写一份，挂给 screenshot 源集）
android {
    sourceSets {
        getByName("screenshotTest") { kotlin.srcDir("src/debug/kotlin") }
    }
}
```

**【待实测 V14】**：截图任务名与源集名以 M1 首日 `./gradlew :wisdom-ui:tasks --all | grep -i screenshot` 的实际输出为准，并回写本节。

#### 1.2.3 依赖块（U4 收缩，逐行对照现状）

```kotlin
dependencies {
    val composeBom = platform(libs.compose.bom)
    api(composeBom)                                   // build.gradle.kts:39 保留（07 §2.1 P-5：BOM 留 api）
    api(libs.compose.foundation)                      // :40
    implementation(libs.compose.material3)            // :41 api → implementation（U4 收缩）
    api(libs.compose.ui)                              // :42
    api(libs.compose.ui.graphics)                     // :43

    debugImplementation(libs.compose.ui.tooling)      // :45
    debugImplementation(libs.compose.ui.tooling.preview)  // :46 implementation → debugImplementation

    testImplementation(libs.junit)                    // :48（现状）
    testImplementation(libs.compose.ui.test.junit4)   // ➕ M1
    testImplementation(libs.robolectric)              // ➕ M1
    testImplementation(libs.androidx.test.core)       // ➕ M1
    debugImplementation(libs.compose.ui.test.manifest) // ➕ M1（AR-20/§3.5-2：必须进 debug 变体）
}
```

**四条必须写进 `android/README.md` 的说明**（U4 的收益边界，AR-12/AR-13/§3.6）：

1. **公开签名不得出现 m3 类型** —— 执行手段从"黑名单 grep"升级为 **G9 正向白名单**（AR-07）。
2. **BOM 留在 `api`**：POM 里是 `dependencyManagement` + `scope=import`，不向下传递（Maven 语义），Gradle 消费者经 module metadata 获得版本对齐。
3. **收缩的收益 = 消费方 compile classpath 干净 + 不被绑 m3 版本；体积收益 = 0，直到拆 artifact**（AR-12 原话"这是 draft 最诚实的一段"）。所有"U4 已省体积"的表述在 §7 的 M0 出口拿到数字（E2 + m3 dex 占比）之前一律标 **未量化**。
4. **拆 artifact 的可测触发判据（AR-13）**：① `apkanalyzer dex packages` 中 `androidx.compose.material3` 的 dex 增量占比 > 50% **且** E2 > 400KB；② 出现一个**真实**消费方 issue。任一成立 → M+1 里程碑拆 `:wisdom-ui-material-bridge`（唯一引用点是 `WDMaterialScheme.kt`，一行可断）。判据①的"消费方明确要求"这一条**删除**（不可测）。

**R8 的三条独立动作（AR-14，替代旧稿那条名不副实的"R8 断言"）**：

```bash
# ① 变体断言（含正负例）：release AAR 无预览符号；debug AAR 必须有（证明检查会失败）
./gradlew :wisdom-ui:assembleRelease :wisdom-ui:assembleDebug        # ← LR-20：两个产物都必须先装配
AAR_REL=wisdom-ui/build/outputs/aar/wisdom-ui-release.aar
AAR_DBG=wisdom-ui/build/outputs/aar/wisdom-ui-debug.aar
unzip -p "$AAR_REL" classes.jar > /tmp/rel.jar
unzip -p "$AAR_DBG" classes.jar > /tmp/dbg.jar                       # ← /tmp/dbg.jar 的生产者
jar tf /tmp/rel.jar | grep -c 'Preview' | grep -q '^0$'              # 必须 0
jar tf /tmp/dbg.jar | grep -c 'Preview' | grep -qv '^0$'             # 必须 > 0（正例）
# ② R8 冒烟（唯一能证明不误删的手段）：:demo release 开 minify + shrinkResources
./gradlew :demo:assembleRelease
ls -l demo/build/outputs/mapping/release/seeds.txt              # CI 产物存档（-printseeds）
# ③ consumer-rules.pro 保持空（仅注释）是**可接受**的；README 写明"若将来引反射式 API 必须重新评估"
```

#### 1.2.4 可复现构建（AR-15）

```bash
# 依赖校验元数据的生成命令（入库）
./gradlew --write-verification-metadata sha256 help
```

- **Robolectric 的 `android-all` 不走 Gradle 依赖解析** → 预热走 `robolectric.dependency.dir`（`src/test/resources/robolectric.properties` 旁放 `robolectric.dependency.dir` 系统属性或 `-Drobolectric.dependency.dir=…`），CI 缓存该目录；
- **`-g ./.gradle-home` 只在本地排查用**，CI 不隔离（隔离会同时踩"依赖缓存"与"Robolectric 缓存"两个坑）；
- 镜像只作可选 `android/tools/init-mirror.gradle`（不写进 `settings.gradle.kts`）。

### 1.3 代码规范

#### 1.3.1 三工具分工与重叠收口（AR-16）

| 工具 | 唯一所有者范围 | 明确不做 |
| --- | --- | --- |
| ktlint | 格式、命名、导入顺序、行宽 | 不做 Modifier 位置检查（**归 Compose lint**） |
| Compose lint（随 `ui` AAR） | `ModifierParameter`、`UnnecessaryComposedModifier`、`ComposableLambdaParameterPosition`、`UnrememberedMutableState`、`RememberReturnType` | 格式 |
| detekt | 结构复杂度 + `ForbiddenImport`（m3 黑名单，第二层网） | 与 ktlint 重叠的格式规则一律 `active: false` |

**规则：同一条规则只允许一个工具告警**。M0 接入时若 ktlint 的 Compose 规则集与 Compose lint 重叠 → **关掉 ktlint 那一侧**，并把这句写进 README。

#### 1.3.2 ktlint 配置（**按 AR-17 精简为 3 行 + 命名例外**）

```ini
# android/.editorconfig
root = true

[*.{kt,kts}]
ktlint_code_style = ktlint_official
max_line_length = 120
ktlint_function_naming_ignore_when_annotated_with = Composable,Test
```

- **删掉 `ij_kotlin_imports_layout`**（AR-17 ①：`*` 与"ktlint 默认布局"不等价，会改 `java./javax./kotlin.` 的分组语义）。导入顺序 = ktlint 默认布局，**不自定义**。
- **PascalCase 工厂一律改名**（AR-17 ② / AR-36 ①）：`WDSheetState.Saver(...)` → `WDBottomSheetState.saver(...)`；`@Test fun \`中文名\`()` 靠 `Test` 例外放行。
- **【待实测 V3】**：ktlint/detekt 的规则名与默认值以 M0 首次接入的实际输出校准（`08-decisions.md:47` 第 4 项未闭合）。

#### 1.3.3 命名与文件组织（AR-18 加限定词）

| 规则 | 范围 |
| --- | --- |
| 一个文件一个主类型 | **仅手写文件**（`components/**`、`foundation/**` 非 generated 部分） |
| 单文件 ≤ 400 行 | 同上 |
| `foundation/generated/**` **豁免**上述两条 | 生成物单文件多类型是生成器的正确形态（现状 `WDTokens.kt` 271 行含 11 个类型 [读]） |

命名：`WD` + UpperCamelCase（`04-architecture.md:57` [读]）；修饰符 `.wd` 前缀；参数 lowerCamelCase 且布尔不加 `is`（`07-summary.md:58` F1）；内部类照旧 `WD` 前缀 + `internal`。

### 1.4 提交、分支与 Review（AR-19 加一条模板要求）

- Conventional Commits + `变更集: tokens: vX.Y.Z` + `ABI: 无变化|只增|签名变化`；
- **trunk-based + rebase merge（禁止 squash）** —— M0-3 要求"纯移动/语义"两个提交可审计；
- **PR 模板第 3 段必须贴 `apiDump` 的类型级摘要**（新增/删除/改签名各几行）；写"ABI: 无变化"必须由 `git diff --exit-code -- wisdom-ui/api/wisdom-ui.api` 的输出背书（AR-19）；
- Review 人数矩阵沿用旧稿（碰 `api/wisdom-ui.api`/`contracts`/CI → 2 人，其中 1 名是 android-lead 或 tech-lead）。

### 1.5 CI 门禁

#### 1.5.1 三层强度（沿用 `07-summary.md:354-358`）

| 层 | Android | 属性 |
| --- | --- | --- |
| PR 必过（≤10 min） | `./gradlew wisdomGate` + 基线漂移检查（见下） | 阻塞合并 |
| nightly | 六态截图（screenshot 插件）+ `WDLineBoxTest` + 自算对比度 + 大字号矩阵 + `:demo:assembleRelease`（R8 冒烟）+ Benchmark（B1/B2/A2/A3/E2） | M1–M3 只报；M4 起 B1/B2/E1 转门槛（U3） |
| 发布前 | 集成 APK 差 + `apkanalyzer dex packages` + POM 三查 + tag 校验 + AAR 体积 | 阻塞发布 |

#### 1.5.2 `wisdomGate`：**一个复合任务 + 一条 git 检查**（AR-20，替代旧稿"六条命令"）

```kotlin
// android/wisdom-ui/build.gradle.kts
val wisdomGate by tasks.registering {
    group = "verification"
    description = "Android PR 必过门禁（评审 AR-20：六条命令合并为一个任务）"
    dependsOn(
        "apiCheck",                  // ① ABI 基线
        "assembleRelease",           // ② release 编译
        "testDebugUnitTest",         // ③ 单测（debug 变体：Compose 本地测试 + Robolectric + screenshot）
        "wisdomCheckPublicTypes",    // G9  公开类型正向白名单（U4 的真正机器化）
        "wisdomCheckComposeMetrics", // G6  稳定性指标真断言（B5）
        "wisdomCheckPackageDeps",    // G1  依赖方向 + 包图无环 + 第三子断言（m3 计数 == 1）
        "wisdomCheckTokenLiterals",  // G2  令牌字面量（符号级白名单）
        "wisdomCheckMainNoPreview",  // G3  src/main 不得出现 @Preview / ui-tooling
        "wisdomCheckStabilityFields",// G5  $stable 存在性
        "wisdomCheckGeneratedSelfProof", // G7 banner hash 自证
        "wisdomCheckReleaseSymbols", // G8  release AAR 无预览符号（含正例）
        "wisdomCheckDeprecatedBaseline", // G4 deprecated 成员数不增长
        "wisdomCheckInsetsWhitelist",    // G10 只允许 6 个组件碰 WindowInsets
        "wisdomCheckThemeDefaults",      // G11 theme 默认参数不得是构造调用
        "wisdomCheckSingleCallSite",     // G12 wdSystemGestureExclusion 调用点 == 1
        "wisdomCheckLiteralLanguage",    // G13 L-B 标点/词序字面量（= iOS R15 判据；M0–M2 warning）
        "assembleDebug",                 // G8 正例的生产者（LR-20：/tmp/dbg.jar 由它产出）
        "ktlintCheck", "detekt", "lintRelease",
    )
}
```

**shell 检查 → Gradle task 的包装（LR-20）**：每个 shell 检查一个 `Exec`（统一形态 + 声明输入，便于缓存与失败定位）：

```kotlin
// android/wisdom-ui/build.gradle.kts
fun Project.wdExecTask(name: String, script: String, vararg inputs: String) =
    tasks.register<Exec>(name) {
        group = "verification"
        commandLine("bash", layout.projectDirectory.file("tools/$script").asFile.absolutePath)
        inputs.files(inputs.map { layout.projectDirectory.file(it) })
    }

val wisdomCheckPublicTypes     = wdExecTask("wisdomCheckPublicTypes", "check-public-types.sh", "api/wisdom-ui.api")
val wisdomCheckReleaseSymbols  = wdExecTask("wisdomCheckReleaseSymbols", "check-release-symbols.sh", "build/outputs/aar/wisdom-ui-release.aar")
val wisdomCheckDeprecated      = wdExecTask("wisdomCheckDeprecatedBaseline", "check-deprecated.sh", "tools/deprecated-baseline.txt")
val wisdomCheckLiteralLanguage = wdExecTask("wisdomCheckLiteralLanguage", "check-literal-language.sh", "src/main/kotlin", "src/debug/kotlin")

// G8 的正例需要 debug AAR（LR-20：交付 /tmp/dbg.jar 的生产者）
wisdomCheckReleaseSymbols.configure { dependsOn("assembleRelease", "assembleDebug") }
```

CI 的两条命令（**基线漂移检查必须是 shell**：`apiDump` 之后要读 `git diff`，Gradle 任务里没有 git 语义）：

```bash
./gradlew wisdomGate
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
```

> **口径说明（对 AR-20 的落地解释）**：AR-20 要求"CI 只跑 `wisdomGate`，其余降为本地排查"。我把 **`apiDump` + `git diff --exit-code`** 保留为同一 CI 步骤的第二行——它是 M0-4 的出口判据（`08-decisions.md:35`），且**不能**作为 Gradle 依赖表达。除这两行外，CI 不再跑任何其他 Gradle 命令。

#### 1.5.3 门禁规则 G1–G13（定义表）

| # | task / 断言 | 判据（可执行） | 落点 |
| --- | --- | --- | --- |
| **G1** | `wisdomCheckPackageDeps` | 依赖方向三条 + 包图无环；**第三条子断言（G1-③）= 全库 m3 import 计数 == 1，且必须是 `src/main/kotlin/io/github/wlunc/wisdom/foundation/theme/WDMaterialScheme.kt`**（LR-14/LR-15：期望串**不带** `wisdom-ui/` 前缀；反例单测见 §1.5.4-H） | `wisdom-ui/build.gradle.kts` + `tools/checks/package-graph.kt` |
| **G2** | `wisdomCheckTokenLiterals` | 扫描根 = **手写文件**（`components/**`、`foundation/**` 但**排除 `foundation/generated/**`**，LR-13）；`Color(0x…)` / `#[0-9A-Fa-f]{6}` / 裸 `\d+(\.\d+)?\.(dp\|sp\|em)` **零容忍**；白名单 = **符号级**：只允许引用 `WDSize.*` / `WDRadius.*` / `WDSpacing.*` / `WDComponent.*` / `WDMotion.*` / `WDType.*` / `WDElevation.*`（`foundation/tokens/**` 的手写适配同样只走符号级白名单） | 同上 |
| **G3** | `wisdomCheckMainNoPreview` | `src/main/**` 不得出现 `@Preview`、`androidx.compose.ui.tooling`（AR-11）。**不含 m3 计数**（那是 G1-③） | 同上 |
| **G4** | `wisdomCheckDeprecatedBaseline` | `grep -rc '@Deprecated' src/main/kotlin` 与 `tools/deprecated-baseline.txt` 比对（apiDump 不记注解，AR-22-B） | `tools/check-deprecated.sh` |
| **G5** | `wisdomCheckStabilityFields` | 对 §2.9.1 的全量清单，在 `api/wisdom-ui.api` 里断言每个类名后 3 行内出现 `$stable`（实测 `:2,162` 等 13 处 [11 §0.2]） | JVM 单测 `WDStabilityContractTest` |
| **G6** | `wisdomCheckComposeMetrics` | 读 `build/compose-metrics/*-module.json`：`inferredUnstableClasses == 0` 且 `knownUnstableArguments` ≤ 基线（**文件缺失即 fail**，不允许 skip） | `tools/check-compose-metrics.sh` + `build/compose-metrics/baseline.json` |
| **G7** | `wisdomCheckGeneratedSelfProof` | banner 正则 `tokens v([\d.]+) · sha256:([0-9a-f]{12})`；`WDTokensVersion.sha256` == banner hash；解析失败 fail | JVM 单测 `WDTokensVersionTest` |
| **G8** | `wisdomCheckReleaseSymbols` | release AAR `classes.jar` 内 `Preview` 计数 == 0 **且** debug AAR > 0（正负例） | `tools/check-release-symbols.sh` |
| **G9** | `wisdomCheckPublicTypes` | 解析 `api/wisdom-ui.api` 全部 `L…;` 类型引用，**只允许前缀白名单**：`io/github/wlunc/wisdom/`、`androidx/compose/{ui,foundation,runtime,animation}/`、`kotlin/`、`java/`；出现 `material3`/`internal`/`activity`/`window`/`recyclerview` 一律 fail（AR-07：这条同时替代旧的 `grep material3`） | `tools/check-public-types.sh` |
| **G10** | `wisdomCheckInsetsWhitelist` | `WindowInsets` 只允许出现在 `WDNavigationBar`/`WDTabBar`/`WDToast`/`WDBottomSheet`/`WDActionSheet`/`WDAlert`（AR-68） | 扩 G1 的规则列表 |
| **G11** | `wisdomCheckThemeDefaults` | `foundation/theme/**` 的默认参数表达式不得是构造调用（正则 `= [A-Z]\w*\(` + 白名单 `WDLayout.Comfortable`/`WDEffectsBudget.Default`/`WDIconSet.Empty`/`WDMotionScale.Normal`） | 扩 G1 的规则列表 |
| **G12** | `wisdomCheckSingleCallSite` | `wdSystemGestureExclusion` 的调用点计数 == 1（只允许 drag handle，AR-72） | 扩 G1 的规则列表 |
| **G13**（本稿新增，LR-08③） | `wisdomCheckLiteralLanguage`（= iOS **R15** 同判据的 Android 版） | 扫描根 = 手写文件（排除 `foundation/generated/**`）；命中文档级标点 `，。、；：！？,.;:!?` 或词序模板 `第` / `共` / `关闭` / ` of ` **即 violation**；白名单 = `src/debug/**`（预览）、`src/test/**`、`internal/**` 的日志字符串；行内豁免语法 `// wd-literal-check:disable <RULE> — <理由>`（同行给理由）。**M0–M2 为 warning，M3 起转 error**（与 iOS R15 的升级路径一致） | `tools/check-literal-language.sh` → `Exec` 任务，进 `wisdomGate`（M0-6） |

#### 1.5.4 ABI 与产物校验（AR-07 / AR-22 / AR-58）

| # | 断言 | 手段 |
| --- | --- | --- |
| A | 公开类型白名单（U4 的真正机器化） | **G9** |
| B | deprecated 成员数不增长 | **G4** |
| C | 生成物自证（banner hash） | **G7** |
| D | release AAR 无预览符号（正负例） | **G8** |
| E | POM 三查：无 runtime 预览依赖、BOM 在 `dependencyManagement/import`、版本来自参数化 | `tools/size-report.sh`（T9） |
| **F** | **`$default` 行数变化 = 二进制不兼容**（AR-58：给既有 public 函数加带默认值的参数会让 `$default` 合成方法签名变化 → 老消费方 `NoSuchMethodError`） | CI 对 `api/wisdom-ui.api` 的 diff 做 `grep -c '\$default'` 前后比对；行数变化 → 必须走 major 或改新增重载 |
| G | `internal constructor` 变化是**基线可见**的（`api:3` 的 `DefaultConstructorMarker` 就是痕迹 [11 §3.1]） | `apiDump` 人审 + CHANGELOG |
| **H**（LR-14） | **G1-③ 的反例单测**：断言必须能失败 —— 在临时目录造第二个含 `import androidx.compose.material3.…` 的 `.kt` 文件，跑判定逻辑，断言"计数 == 2 ⇒ 检查失败"，且"删掉临时文件 ⇒ 恢复绿" | JVM 单测 `WDM3ImportCountTest`（正例 + 反例两条） |
| **I**（LR-13） | **G2 的反例单测**：在临时文件里写 `Color(0xFF000000)` / `16.dp` ⇒ 判失败；把同一内容放进 `foundation/generated/` 路径 ⇒ 判通过（证明扫描根排除生效） | JVM 单测 `WDLiteralScanTest`（两条） |
| **J**（LR-16） | **`Role` 集合反射单测**：`Role.Companion` 的取值集合 == 契约表集合（**9 个**，且不含 `Slider`） | JVM 单测 `WDRoleContractTest` |

#### 1.5.5 覆盖率（AR-23 修正口径）

```kotlin
// android/build.gradle.kts（Kover）
kover {
    reports {
        filters {
            excludes += "io.github.wlunc.wisdom.foundation.generated.*"
            excludes += "io.github.wlunc.wisdom.internal.*"
        }
    }
}
```

门槛 = `foundation`（手写部分）+ `components` 包的**行覆盖 ≥ 80%**（Kover 没有"只看 public 成员"的过滤器，AR-23 的修正成立）。M0–M3 只采集；M4 起进 nightly（**不进发布门槛**，P-4 三类指标）。

#### 1.5.6 包体积阈值（AR-24 补测量口径）

| 指标 | 口径 | M1–M3 | M4 起 |
| --- | --- | --- | --- |
| E2（主指标） | 集成 APK 差（R8 + 资源收缩） | ≤ 400 KB，只报 | 门槛 |
| E1 | AAR 字节（现状 31,326 B） | ≤ 150 KB，端内趋势 | 端内趋势 |
| E1' | **层包/批次 dex 增量 ≤ 6 KB × 该批组件数**（LR-05：`apkanalyzer dex packages` 只有包级增量，20 个组件同包无法归因 ⇒ 用「层包/批次」口径，**不**做 per-component demo 变体） | M1 出脚本 `tools/size-report.sh --dex` | 门槛 |
| F1 | R8 后 dex 增量（`apkanalyzer dex packages`），含 m3 占比（U4 量化） | 只报 | 门槛 |
| — | **禁止跨端比数值** | — | — |

**与 `08-decisions.md` U3 的口径映射（LR-18，必须明写，防"改名换门槛"）**：

> `08 U3` 的 **E1**（原名"主指标"）= 本稿的 **E2**（集成 APK 差）——P-4 把 Android 主指标从 AAR 字节改成了集成 APK 差，只是换了名字；本稿的 **E1**（AAR 字节）**仅作端内趋势**、不作门槛。即：**`08 U3` 说"M4 起 B1/B2/E1 转门槛"，落到本稿就是"B1/B2/E2 转门槛"**——两者指同一条指标，不存在"少一条门槛"。若上位要求按 `08` 字面把 AAR 字节也转门槛，本稿加回即可（一行改动）。

### 1.6 开发环境与预览矩阵

#### 1.6.1 本地/真机调试（AR-25 补"改哪条 → 断言什么"）

| 命令 | 预期断言 |
| --- | --- |
| `adb shell settings put system font_scale 2.0` | U7 上界：不截断/不重叠/行数变化；`WDLineBoxTest` 的 2.0 档 |
| `adb shell settings put system font_scale 1.3` | 中间档：横排→竖排降级生效（阈值来自 `WDComponent.layoutBreakFontScale`） |
| `adb shell settings put global animator_duration_scale 0` | 骨架/高光/进度环静止；`rememberWDInfiniteSpec()` 返回 `null` |
| `adb shell wm density 320` | 会**重建 Activity** → 验证 `Saver`（`WDBottomSheetState`/`LazyListState` 保持）的正例机会 |
| `adb shell am start -n io.github.wlunc.wisdom.demo/.DemoActivity` | 一层可切换：浅/深、fontScale、LTR/RTL、密度、图标集 |

**Live Edit**：库模块（`:wisdom-ui`）的行为 **【待实测 V6】**（旧稿已声明）；组件迭代主回路 = 预览矩阵 + `:demo` 示例页。

#### 1.6.2 Mock 数据与预览矩阵（AR-27 补双向检查）

- fixtures：`android/demo/src/main/kotlin/…/demo/fixtures/WDFixtures.kt`（正常/空/超长/极端尺寸）+ 读 `contracts/l10n-fixtures.json`（用 Android 内置 `org.json`，demo 不新增依赖）；`:demo` **禁止使用 `internal` API**。
- **预览用例名双向检查（G 系列补充单测）**：

```kotlin
// android/wisdom-ui/src/test/kotlin/…/contract/WDPreviewCaseCoverageTest.kt（AR-27）
// 读 src/debug/kotlin/**/*Preview.kt 的 @Preview(name = "…")，与 contracts/preview-cases.yaml 的
// `- name:` 行做**双向包含**断言：差集非空即 fail（手写行解析，不引 YAML 依赖）。
@Test fun `预览用例名与契约双向一致`() { /* 差集为空断言 */ }
```

```kotlin
// 预览矩阵（6 个 @Preview，name 与契约逐字对齐）
@Preview(name = "D-1.0", fontScale = 1f, showBackground = true)
@Preview(name = "D-1.3", fontScale = 1.3f, showBackground = true)
@Preview(name = "D-2.0-320", fontScale = 2f, widthDp = 320, showBackground = true)
@Preview(name = "N-2.0", fontScale = 2f, uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "RTL-1.0", locale = "ar", fontScale = 1f, showBackground = true)
@Preview(name = "S-2.0", fontScale = 2f, widthDp = 200, heightDp = 400, showBackground = true)
```

---

## 2. 组件 API 与实现细节

### 2.1 组件清单与批次（AR-28 沿用 + AR-86 改名）

| 层 | 数量 | 组件 |
| --- | --- | --- |
| `components/primitives` | 20 | Button / IconButton / TextField / SearchField / Switch / Checkbox / Radio / Slider / Stepper / Chip / Badge / Avatar / AvatarStack / Divider / ProgressBar / ProgressRing / Card / ListRow / ListSection / Icon |
| `components/composites` | 17 | SegmentedControl / Picker / DatePicker / FormRow / Alert / **BottomSheet** / ActionSheet / Toast / Banner / EmptyState / Skeleton / PullToRefresh / NavigationBar / TabBar / Toolbar / FAB / AssigneePicker |

**改名清单（AR-86 + LR-10，U1/U2 违例修正，**M0/M2 前**完成）**：

| 旧稿（Android） | 定稿（= iOS 定稿逐字一致） | 说明 |
| --- | --- | --- |
| `WDSheet` | **`WDBottomSheet`** | 组件名（U1） |
| `WDSheetState` | **`WDBottomSheetState`** | Android 专有状态类（iOS 无对应 ⇒ 形态差异登记 **F38**；其 `Saver` 恢复能力差异属 **F5**） |
| `rememberWDSheetState` | **`rememberWDBottomSheetState`** | 同上 |
| `WDSheetDetent.PartiallyExpanded/Expanded` | **`WDBottomSheetDetent.Half/Large`**（0.5 / 0.92；规范名 `half`/`large`） | 与 iOS `WDBottomSheetDetent.half/large` 同 case 名（U2）；**`20 §2.4-1` 收紧**：家族前缀统一 `WDBottomSheet*`，两端都不得出现 `WDSheet*` |
| —（旧稿用 `List<WDBottomSheetDetent>`） | **`WDBottomSheetDetents.All` / `WDBottomSheetDetents.Fixed(detent)`** | 与 iOS `WDBottomSheetDetents.all/.fixed(_:)` 同形（LR-10） |
| 目录 `WDBottomSheet/` | `WDBottomSheet.kt` / `WDBottomSheetDetent.kt` / `WDBottomSheetState.kt` / `WDBottomSheetNestedScroll.kt` | 文件粒度 |

`WDActionSheet` 独立存在（27 号组件）。**M0 期无消费方，改名成本最低**（`00-ios-draft.md:29` 已登记 iOS 侧同名类型；`02-ios-spec.md:615-617` 为 detent 家族定稿来源）。

### 2.2 四个完整签名（终稿）

#### ① `WDButton`（AR-29 / AR-31 / AR-49 / AR-81 已并入）

```kotlin
// 文件：android/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/primitives/WDButton/WDButton.kt
// 包：io.github.wlunc.wisdom.components.primitives（层包）
@Composable
public fun WDButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: WDButtonVariant = WDButtonVariant.Filled,
    size: WDButtonSize = WDButtonSize.Md,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingLabel: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClickLabel: String? = null,          // ➕ AR-29
    role: Role? = Role.Button,             // ➕ AR-29（androidx.compose.ui.semantics.Role，属 ui，不违反 U4）
    interactionSource: MutableInteractionSource? = null,
)
```

**内部调用点（终稿）**：

```kotlin
val source = interactionSource ?: remember { MutableInteractionSource() }
val haptics = rememberWDHaptics()                       // AR-75：组合期取，onClick 里调
val pressed by source.collectIsPressedAsState()

Modifier
    .clickable(
        interactionSource = source,
        indication = WDPressIndication,
        enabled = enabled,                               // AR-49：disabled 语义**收口在这里**，不手写 semantics{}
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            if (!loading) { haptics.press(); onClick() }  // AR-75：可调用（非 @Composable）
        },
    )
    .semantics { if (loading) loadingLabel?.let { stateDescription = it } }   // AR-49：空安全
```

**语义契约（可断言）**

| 断言 | 前置条件（AR-81 新增） |
| --- | --- |
| `onNodeWithText("保存").assertIsButton()` | 需 `role = Role.Button`（E3：`role == null` 不写 `Role`） |
| `onNodeWithText("保存").assertIsNotEnabled()` | 需 `enabled = false`（由 `clickable` 自带，E4） |
| `loading = true` 时 `onNodeWithText("保存").assertExists()` | 需文本用 `alpha = 0f` 占位而非移除（规格 `:66` 保留原标签供读屏） |
| `loading = true` 时 `onNodeWithText("保存").assert(hasStateDescription("处理中"))` | 需 `loadingLabel = "处理中"`（`07-summary.md:146` API-8） |
| 两态宽度差 ≤ 0.5dp | 需 `composeTestRule.runOnIdle { }` 内读 `onSizeChanged` 值（AR-81） |
| loading 期间 `action` 计数 == 0 | 注入 `MutableInteractionSource`，连续 press/release（U16） |

**debug 断言机制（AR-30）**：`loadingLabel == null && loading` 时的断言走

```kotlin
// src/debug/kotlin/io/github/wlunc/wisdom/internal/WDDebugAssertions.kt
internal fun wdDebugAssert(condition: Boolean, message: () -> String) {
    if (!condition) error("WD 断言失败：${message()}")
}
// src/release/kotlin/io/github/wlunc/wisdom/internal/WDDebugAssertions.kt
internal fun wdDebugAssert(condition: Boolean, message: () -> String) { /* no-op */ }
```

**不得**为此打开 `buildFeatures.buildConfig`（会在 ABI 里造出 `BuildConfig` 类型）。

#### ② `WDTextField`（AR-32 / AR-33）

```kotlin
// 文件：…/components/primitives/WDTextField/WDTextField.kt
@Composable
public fun WDTextField(
    text: String,                                    // C-15 #03：`value` → **`text`**（契约名 = text；与 iOS `text` 名词一致）
    onValueChange: (String) -> Unit,                 // 回调名不在 C-15 范围：M0-5 按 C-15 §5 的配对建议锁 `text ↔ onTextChange`
    modifier: Modifier = Modifier,
    variant: WDTextFieldVariant = WDTextFieldVariant.Inset,   // ➕ LR-06：case 与 iOS 定稿逐字一致（Inset/Outline/Glass）
    label: String,                                            // LR-07：**必填**（硬约束 5：标签必填 = 编译期约束）
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    /** 契约里写作 `default: "MAX_VALUE unless singleLine=true"`（AR-32 ①：默认值依赖另一参数，无法静态表达）。 */
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    characterLimit: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
)
```

```kotlin
// 文件：…/components/primitives/WDTextField/WDTextFieldVariant.kt（LR-06：case 与 iOS 定稿逐字一致）
public enum class WDTextFieldVariant { Inset, Outline, Glass }     // ↔ iOS WDTextFieldVariant(.inset/.outline/.glass)
```

- **变体（LR-06）**：`variant: WDTextFieldVariant = WDTextFieldVariant.Inset`（设计真源 `specs/01-basic.md:250-253` 的"inset 默认 / outline"）；`Glass` 与 `WDGlassLevel` 正交（玻璃外观由 `WDGlass.resolve` 决定，不进 `variant` 的语义）。
- **字段盒高（LR-06 + iOS CR-1 的同一条验收句）**：`WDSize.fieldHeight = 46.dp`（**进 M0-1**，键 `size.field-height: 46`）；加在**输入行**上而不是整个 `VStack` 等价物上；验收句（照抄进契约）：
  > `WDTextField` 的**字段盒**（不含标签与辅助文案）在默认档下：**所有变体、所有状态高度 = 46 ± 1dp**，**状态不得改变高度**；放大档下字段盒高度 = 行盒高（≥46），文字不裁切。标签与辅助行的存在不改变 46 的定义。
- **`label` 必填（LR-07）**：`label: String`（无默认值）。理由：`07:170` 的 L-B 落地要求"**标签必填成为编译期约束**"，iOS 侧定稿已是 `label: Text`（必填）；两端口径一致才能共用同一条验收（`specs/01-basic.md:318`"标签常驻"）。代价：调用方必须给标签——与 L-B 的整体代价同源（README 给默认文案片段）。
- **实现路线（不变，风险最高项）**：`androidx.compose.foundation.text.BasicTextField` + 同目录 `internal WDTextFieldDecorationBox`；**不用 M3 TextField**（组件层碰 m3 会直接违反 G1-③ 的 m3 计数 == 1）。
- **`characterLimit` 用 `InputTransformation.maxLength`（AR-32 ②）**，拒绝"在 `onValueChange` 里手写截断"（IME 组合态会炸）。
- **无障碍（AR-33 沿用）**：不设 `contentDescription`（A-2 例外）；错误态 `semantics { error(errorText) }`；标签进 `label` 槽或同一合并节点。
- 页面级 IME 归调用方（C8）；弹层组件例外（§3.3.5）。

#### ③ `WDListRow`（AR-34 / AR-67）

```kotlin
// 文件：…/components/primitives/WDListRow/WDListRow.kt
@Composable
public fun WDListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,                   // 调用方给**已格式化**的字符串（库不做日期/数字格式化）
    onClick: (() -> Unit)? = null,                  // null = 非交互行
    enabled: Boolean = true,
    selected: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    /** LR-24：与 iOS `showsSeparator: Bool = true` 同名同默认（U2；旧稿 `divider = false` 默认行为相反，已改）。 */
    showsSeparator: Boolean = true,
    accessibilityLabel: String? = null,
    /** 契约：`null` = `LocalWDLayout.current.density`（AR-34 ①）。 */
    density: WDLayoutDensity? = null,
)
```

**`onClick == null` 的三条断言（AR-34 ③）**：① 不注册 `clickable`；② 不写 `Role`；③ 不进行为动作集（`assertHasNoClickAction()`）。

**行高公式（LR-24：与 iOS `max(密度档下限, 槽位派生)` 对齐，替换旧稿的"只有密度档"）**：

```
rowHeight = max(density.rowMinHeight, slots.rowHeight)
  density.rowMinHeight = comfortable 60.dp / compact 44.dp（**可见内容**，两端同值；Android 布局盒另有 48 = 44 + 上下各 2dp 透明内边距，见下方 AR-67 补充解释）
  slots.rowHeight = 76.dp 当且仅当「有副标题 且（有尾部文本 或 有 trailing 槽）」，否则 0.dp（不抬下限）
```

4 格矩阵（{comfortable, compact} × {1 槽/单行, 2 槽/双行}）**逐格固化为单测**：`(comfortable,1)=60`、`(compact,1)=48`、`(comfortable,2)=76`、`(compact,2)=76`。**矩阵里的"行高" = Android 布局盒**（故 `(compact,1)=48` 成立）；**可见内容**口径另立一条断言（见下）。两端数值：**可见内容 44（两端同值）**；**布局盒/热区 = Android 48 / iOS 44**。

**行高即热区（AR-67 / D1）**：**禁止**用"扩热区"手段覆盖相邻行。断言：`compact` 单行下 `onSizeChanged` 高度 == 48dp ± 0.5；`(compact,2)` 下 == 76dp ± 0.5。

> **AR-67 补充解释（案 B，用户 2026-10-05 裁决；原文与上面两条断言值原样保留，本段是补充而非取代）**：**行高 = 布局盒（可见内容 44 + 上下各 2dp 透明内边距）；热区即该 48 布局盒，仍不与相邻行重叠**。
> - 上面两条断言 **照旧成立**：`(compact,1) == 48dp ± 0.5`、`(compact,2) == 76dp ± 0.5`（其中"行高" = **布局盒 48**）。
> - **新增一条断言（P7 ②）**：紧凑单行下 **可见内容高度 == 44dp ± 0.5**（测内容区、**不含**内边距）——防"2dp 内边距把设计稿 44 吃掉"。
> - 实现：`Modifier.wdTouchTarget()`（自研 `layout`，AR-66）把 44 的内容盒扩成 48 的命中盒并居中，**不新增令牌键**（`size.row-height.compact` 仍是单值 44）。

#### ④ `WDBottomSheet` + `WDBottomSheetDetent(s)`（AR-35 / AR-36 / AR-37 / LR-10，**已改名并对齐 iOS 定稿**）

**detent 家族命名（LR-10：U1/U2 要求逐字一致，**现在就统一，不等 M2**）**：

```kotlin
// 文件：…/components/composites/WDBottomSheet/WDBottomSheetDetent.kt
public enum class WDBottomSheetDetent(public val fraction: Float) {   // ↔ iOS WDBottomSheetDetent: case half, large（0.5 / 0.92）
    Half(WDSize.sheetDetentHalf),      // 0.5f，来自令牌 size.sheet.detent.half（M0-1）
    Large(WDSize.sheetDetentLarge),    // 0.92f，来自令牌 size.sheet.detent.large（M0-1）
}

/** ↔ iOS WDBottomSheetDetents（.all / .fixed(_)）。 */
public sealed interface WDBottomSheetDetents {
    public data object All : WDBottomSheetDetents                       // = {Half, Large}（两端默认档集合一致）
    public data class Fixed(public val detent: WDBottomSheetDetent) : WDBottomSheetDetents
}
```

> 命名说明（U1）：组件名 = `WDBottomSheet`（AR-86）；detent 家族 = **`WDBottomSheetDetent`（单档）/ `WDBottomSheetDetents`（`All`/`Fixed` 联合类型）**——即 **`20 §2.4-1` 收紧后的唯一命名**（iOS 侧由 IOS-03 同批改）；Android 专有的状态类 = `WDBottomSheetState`（iOS 无对应类型 ⇒ 形态差异登记 **F38**；`Saver` 恢复能力差异属 **F5**）。

```kotlin
// 文件：…/components/composites/WDBottomSheet/WDBottomSheet.kt
@Composable
public fun WDBottomSheet(
    presented: Boolean,                           // C-15 #26：`visible` → **`presented`**（契约名；可见性唯一真源，受控）
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    detents: WDBottomSheetDetents = WDBottomSheetDetents.All, // LR-10：默认档集合与 iOS .all 一致
    onDetentChange: ((WDBottomSheetDetent) -> Unit)? = null,
    glass: WDGlassLevel = WDGlassLevel.Regular,   // **额外**的玻璃档位参数（Android 侧）；U10 的必选输入是 WDTextLevel，见 §3.5.3
    dragHandle: Boolean = true,
    state: WDBottomSheetState = rememberWDBottomSheetState(),
)
```

**AR-35 的裁决定案**：**删掉**组件参数上的 `initialDetent` 与 `skipPartiallyExpanded`（它们只属于 `rememberWDBottomSheetState`）；仅在 `state` 上保留 detent 真源；并在 debug 期断言 `state.currentDetent` ∈ 解析后的 detent 集合（`require(...)` 走 `wdDebugAssert`）。

```kotlin
// 文件：…/components/composites/WDBottomSheet/WDBottomSheetState.kt
@Stable
public class WDBottomSheetState internal constructor(
    initialDetent: WDBottomSheetDetent,
    private val skipPartiallyExpanded: Boolean,
) {
    /** 落档后的档位（受控真源在调用方的 `presented`；这里只承载 detent）。 */
    public var currentDetent: WDBottomSheetDetent by mutableStateOf(initialDetent)
        internal set

    /** LR-22：拖拽期用 mutableFloatStateOf **同步写**（0..1 进度 + 像素偏移），只把「落档」交给 Animatable。 */
    private var dragProgressState = mutableFloatStateOf(initialDetent.fraction)
    public val dragProgress: Float get() = dragProgressState.floatValue

    /** AR-70 / LR-22：**非 suspend**（拖拽回调里直接调）；只写 snapshot state，不启协程。 */
    internal fun dragBy(deltaY: Float, sheetHeightPx: Float) {
        dragProgressState.floatValue = (dragProgressState.floatValue - deltaY / sheetHeightPx).coerceIn(0f, 1f)
    }
    /** 落档：suspend（Animatable.animateTo），由 `rememberCoroutineScope()` 在 `onPostFling` 里调用。 */
    internal suspend fun snapToDetent(detent: WDBottomSheetDetent) {
        dragProgressState.floatValue = detent.fraction          // 起手对齐
        // Animatable(initialValue = dragProgressState.floatValue).animateTo(detent.fraction, spring(...))
    }

    public companion object {
        /** AR-36 ②③：未知版本 / 未知 detent → 返回 null；不抛异常（listSaver 允许 null，E5）。 */
        public fun saver(skipPartiallyExpanded: Boolean): Saver<WDBottomSheetState, Any> = listSaver(
            save = { listOf(SAVER_VERSION, it.currentDetent.name, skipPartiallyExpanded) },
            restore = { saved ->
                if (saved.firstOrNull() != SAVER_VERSION) return@listSaver null
                val detent = WDBottomSheetDetent.entries.firstOrNull { it.name == saved.getOrNull(1) }
                    ?: return@listSaver null
                WDBottomSheetState(detent, saved.getOrNull(2) as? Boolean ?: skipPartiallyExpanded)
            },
        )
        private const val SAVER_VERSION = "v1"   // 私有，不进 ABI
    }
}

@Composable
public fun rememberWDBottomSheetState(
    initialDetent: WDBottomSheetDetent = WDBottomSheetDetent.Half,      // 与 iOS initialDetent = .half 一致
    skipPartiallyExpanded: Boolean = false,
): WDBottomSheetState = rememberSaveable(saver = WDBottomSheetState.saver(skipPartiallyExpanded)) {
    WDBottomSheetState(initialDetent, skipPartiallyExpanded)
}
```

**`Saver` 三条断言**（`WDBottomSheetStateSaverTest`，AR-36）：未知版本 → `null` 且不抛；未知 detent → `null` 且不抛；缺第 3 项 → 用传入的 `skipPartiallyExpanded`。**另加一条（LR-22）**：`dragBy` 在拖拽序列中不改 `currentDetent`（只有 `snapToDetent` / `onPostFling` 改），且 `dragProgress` 单调有界。

**IME 处理（AR-37：`union` 去重，替代相加）**：

```kotlin
// 文件：…/internal/WDDialogInsets.kt
@Composable
internal fun Modifier.wdDialogImePadding(): Modifier =
    this.windowInsetsPadding(
        WindowInsets.ime.union(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
    )
```

`WDBottomSheet.footer` 只用这一个修饰符（**不再叠加任何 `navigationBarsPadding`**）。验收：M4 真机 + 三键导航 + 键盘弹起 → footer 贴键盘且不悬空。

### 2.3 其余 33 个组件：关键参数补全（AR-38 / AR-39 / AR-41）

| 组件 | 本稿补全的签名级结论 |
| --- | --- |
| `WDIcon` | ```kotlin
@Composable public fun WDIcon(
    imageVector: ImageVector,
    contentDescription: String?,                       // null = 装饰性（clearAndSetSemantics）
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,                    // LR-01：默认 = 跟随 LocalWDContentColor，**在组件体内解析**
    size: Dp = WDSize.iconMd,
) {
    val resolved = if (tint == Color.Unspecified) LocalWDContentColor.current else tint
    // …（用 Color.Unspecified 作默认值的原因与 interactionSource 同理：默认参数表达式不在 Composable 上下文求值）
}
``` |
| `LocalWDContentColor`（LR-01，**本稿新增的库内 local**） | ```kotlin
// 文件：…/foundation/theme/WDContentColor.kt
/** 库内内容色（Compose 的 LocalContentColor 的自有版，**不引 m3**）。 */
public val LocalWDContentColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf { Color.Unspecified }
``` 默认 `Color.Unspecified`；由 `WDTheme` 以 `colors.textPrimary` provide；需要在子树上改色的组件（如 `filled` 按钮的文字）用 `CompositionLocalProvider(LocalWDContentColor provides …)` 局部覆盖 |
| `WDIcon(name = WDIconName.X, …)` | 从 `LocalWDIcons` 取；**缺图时保留占位尺寸**（同尺寸空 `Box`）+ debug 断言（**AR-41：不得零尺寸**，否则文字左移） |
| `WDProgressBar` / `WDProgressRing` | 必须写 `progressBarRangeInfo`（A-8 的 `aria-valuenow` 落点），**`value: Float?`**（C-15 #15/#16：`progress` → `value`；null = 不确定态） |
| `WDDatePicker` | **裁决（AR-38 / **F41**；评审稿旧号 F32）**：包装 `android.app.DatePickerDialog`（零文案 + 系统 a11y + minSdk 24 可用）。签名：`public fun WDDatePicker(date: Long?, onValueChange: (Long) -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, title: String? = null)`（C-15 #23：`value` → **`date`**；`Long?` = epoch millis，载体差异属 F41）；**日期字符串一律由调用方格式化** |
| `WDToast` | 库内计时 + `durationMillis` + 悬停暂停=**继续剩余时间**（INT-4）；`onDismiss` 暴露 |
| `WDXxxDefaults` | 例外只允许"颜色组合"；名字与范围写进 README（例：`WDIconButtonDefaults` 只能放颜色，**不得含尺寸/字阶**） |
| 其余 | 沿用旧稿 §2.1.3 四列表（数据参数/槽位/受控/默认值），完整签名按 M2–M6 批次出 |

### 2.4 组合方式（AR-40 沿用）

槽位四规则（单槽 `content`；多槽 `header`/`footer`/`leadingIcon`/`trailingIcon`/`leading`/`trailing`；**无槽位显式写"无"**；receiver 只在需要 `Modifier.weight` 时用且会进 ABI）；子组件不走槽位；不提供参数收集 Builder DSL；图标一律走槽位或注入（U8）。

**三种组合形态的准确写法（对应"slot 槽位 / 子组件 / Builder-content lambda"三个子项）**：

| 形态 | 写法 | 何时用 |
| --- | --- | --- |
| **槽位（slot）** | `content: @Composable () -> Unit`（无 receiver）；需要 `weight` 时 `content: @Composable RowScope.() -> Unit` | 内容可由调用方替换 |
| **子组件（固定结构）** | 组件内部直接摆自己的子组件（如 `WDListSection` 的标题区、`WDAlert` 的按钮行） | 规格没写"可替换"的段 |
| **Builder / content lambda（尾 lambda 容器）** | `WDTheme { }`、`WDGlass { }`、`WDListSection { }` —— `content: @Composable () -> Unit` 作为**最后一个参数** | 容器型组件；**不提供**"参数收集式 Builder DSL"（`WDXxx { 配置项 }`） |

**U3 共享槽位词表（**21 名，与 iOS `02-ios-spec.md:835-845`（§2.10）的词表逐字一致**，LR-25 / R2A-01）**：

```
槽位词表（M0 冻结，21 名）：content / header / footer / leadingIcon / trailingIcon / leading / trailing /
                          title / message / actions / items / label / icon / prefix / accessory / helper /
                          subtitle / valueText / options / placeholder / control
```

- **只有这 21 个名字算"槽位"**（进 U3，两端逐字相同）；其余一律是"参数"，不计入 U3；
- **IOS-02 的 5 个补入名**（R2A-01 的核心）：`subtitle`（#18）、`valueText`（#15/#16）、`options`（#07/#22）、`placeholder`（#04）、`control`（#24）—— 原 16 名版本漏了矩阵实际用到的这 5 个名字；**以 21 名为准**（`02-ios-spec.md:835-845`）；
- **两端共用同一份词表**（iOS §2.10 / Android 本节，**不得各自扩名**）；冻结后新增名字 = **改 U3**（需 android-lead + 架构师）；
- 无槽位的组件**必须显式写"无"**（下表 `无`）；
- **M0-5 产出**：`contracts/README.md` 的"槽位词表（21 名）"表 + **`{组件 → 槽位名 ∈ 21 名词表 ｜ 无}` 的 37 行表（必须由同一份词表生成）** + "组件类型名清单"（37 条 + `WDBottomSheetDetent`/`WDBottomSheetDetents` 家族，**不得出现 `WDSheet*`**）。下表即其 Android 侧内容（本轮不改仓库）。

| # | 组件 | 槽位名（∈ 21 名词表 ｜ `无`） |
| --- | --- | --- |
| 01 | `WDButton` | `label` / `leadingIcon` / `trailingIcon` |
| 02 | `WDIconButton` | `icon` |
| 03 | `WDTextField` | `label` / `prefix` / `accessory` / `helper`（Android 另有 `leadingIcon` / `trailingIcon`，均在词表内） |
| 04 | `WDSearchField` | `placeholder` / `label`（取消） |
| 05 | `WDSwitch` | `label` |
| 06 | `WDCheckbox` | `label` |
| 07 | `WDRadio` | `label`（组）/ `options` |
| 08 | `WDSlider` | `label` |
| 09 | `WDStepper` | `label` |
| 10 | `WDChip` | `label` / `leadingIcon` |
| 11 | `WDBadge` | `label` |
| 12 | `WDAvatar` | `icon` / `label` |
| 13 | `WDAvatarStack` | `items` |
| 14 | `WDDivider` | **无**（装饰，不进树） |
| 15 | `WDProgressBar` | `label` / `valueText` |
| 16 | `WDProgressRing` | `label` / `valueText` |
| 17 | `WDCard` | `content` |
| 18 | `WDListRow` | `title` / `subtitle` / `leading` / `trailing` |
| 19 | `WDListSection` | `header` / `footer` / `items` |
| 20 | `WDIcon` | `icon` |
| 21 | `WDSegmentedControl` | `items` |
| 22 | `WDPicker` | `label` / `options` |
| 23 | `WDDatePicker` | `label` |
| 24 | `WDFormRow` | `label` / `control` |
| 25 | `WDAlert` | `title` / `message` / `actions` |
| 26 | `WDBottomSheet` | `title` / `content` / `footer` |
| 27 | `WDActionSheet` | `title` / `message` / `items` |
| 28 | `WDToast` | `message` / `actions` |
| 29 | `WDBanner` | `message` / `actions` |
| 30 | `WDEmptyState` | `title` / `message` / `actions` |
| 31 | `WDSkeleton` | **无** |
| 32 | `WDPullToRefresh` | `label` |
| 33 | `WDNavigationBar` | `title` / `leading` / `trailing` |
| 34 | `WDTabBar` | `items` |
| 35 | `WDToolbar` | `items`（Android 另有 `leading` / `trailing`，均在词表内） |
| 36 | `WDFAB` | `icon` / `label`（必填） |
| 37 | `WDAssigneePicker` | `items` / `label` |

**五个补入名的 Android 形态与归属判定（R2A-01 ②；分类维度 = **F48**，t19/S-3 已裁定）**：

| 名字 | 组件 | Android 实现形态 | iOS 形态 | 归属判定 | 判据 |
| --- | --- | --- | --- | --- | --- |
| `subtitle` | `WDListRow` | **参数** `subtitle: String?`（可空文本参数，**不是** `@Composable` 槽位） | 参数 `subtitle: Text?`（`02-ios-spec.md:625`） | **U3 = 名字必须统一**（已统一为词表名）；**API 形态属 F 系列**（可空文本参数 各端自由） | 名字进消费方代码 → 必须统一；两端都用"文本参数"而非泛型 slot → 形态本就同构 |
| `valueText` | `WDProgressBar` / `WDProgressRing` | **参数** `valueText: String?`（旧稿写的 `value`/`helper` 已作废） | 矩阵记 `valueText`（`02-ios-spec.md:865-866`） | **U3 = 名字必须统一**；API 形态属 F 系列 | 同上；进度文案的**格式化归调用方**（库内零文案，与 Q-I18N-3 一致） |
| `options` | `WDPicker` / `WDRadio` | **数据参数** `options: List<…>`（旧稿写的 `items` 已作废） | 矩阵记 `options`（`:857` / `:872`） | **U3 = 名字必须统一**；API 形态属 F 系列（数据列表 vs `@ViewBuilder`） | 选项集是数据不是视图；语义名统一即可 |
| `placeholder` | `WDSearchField` | **参数** `placeholder: String?`（旧稿写成"=`label` 语义"已作废） | 矩阵记 `placeholder`（`:854`） | **U3 = 名字必须统一**；API 形态属 F 系列 | 占位符与 `label` 是**两个不同的槽位名**（同一行里 `label` 表"取消"按钮） |
| `control` | `WDFormRow` | **`@Composable` 槽位**（旧稿写的 `content` 已作废） | 矩阵记 `control`（`:874`） | **U3 = 名字必须统一**（这一条**同时**是 `@Composable` 槽位，两端形态一致） | 控件由调用方注入 → 两端都是"内容槽" |

> **清理记录（R2A-01 ③，替换旧稿的"词表外的两条清理"）**：① `WDProgressBar/Ring` 旧稿的 `value`→`helper` 映射**作废**，统一为 `valueText`（进 21 名词表）；② `WDListRow.subtitle` 旧稿"是参数不是槽位"的判定**作废**——它是词表内的槽位名（形态仍是文本参数）；③ `WDSearchField.placeholder` 旧稿"=`label` 语义"**作废**，它是独立槽位名；④ `WDPicker`/`WDRadio` 的 `items` → **`options`**；⑤ `WDFormRow` 的 `content` → **`control`**。
> **词表量纲（S-3 已裁定，分类维度 = **F48**）**：U3 统一的是「**名字**」与「**同一名字承载的语义**（同一内容 + 同一读屏结果）」；**API 形态**（`@Composable` 槽位 / 带类型参数 / 值数组）**各端自由**，登记在 **F48**（见 §4.1）。因此本节的落地（词表 = 语义名清单；`subtitle`/`valueText`/`placeholder`/`options` 保持参数、`control` 保持 `@Composable` 槽位）**成立**；t18 曾提的"若升格则有 4 处签名变更"**作废**（`25` §3 的 S-3 裁定）。

### 2.5 样式覆盖入口（AR-42 / AR-43 / AR-45）

**Modifier 顺序语义（终稿）**

```kotlin
// 组件内部链（固定，由上到下，**LR-23：clickable 必须落在 padding 之外**）：
//   [调用方 modifier] . wdTouchTarget() . clickable(interactionSource, indication, enabled, role, onClickLabel, onClick)
//                     . clip(shape) . background/glass . padding(内距) . [内容]
// 语义：
//   ① 调用方 modifier 作用于**最外层**；
//   ② wdTouchTarget 紧跟其后 → 扩热区不放大视觉、不越界到相邻行；
//   ③ clickable 在 clip/padding **之前** → 否则热区会被 padding 缩回视觉尺寸（L7 与"Sm 档视觉 32 / 热区 48"的断言必红）；
//   ④ clip/background 在 padding 之前 → 背景铺满热区，内距只影响内容。
```

| # | 规则 | 断言 / 检查 |
| --- | --- | --- |
| M1 | `modifier` 是必选参数后的第一个可选参数 | Compose lint `ModifierParameter` |
| M2 | 组件内部只应用一次，且在最外层 | lint + review |
| M3 | 组件不得把内部 `padding`/`background` 追加到调用方 modifier 之后 | review |
| M4 | `Modifier.wdXxx(...)` 只用于纯绘制/语义；带内容布局的容器必须是 `WDXxx { }` 组件 | lint `UnnecessaryComposedModifier` |
| M5 | 不用 `@JvmOverloads` / `@JvmName` | `apiDump` diff |
| **M6（AR-42 新增）** | 组件**不得**把内部修饰符插到调用方 `modifier` 之前（顺序由组件决定，不由调用方猜） | G1 的正则（可查：`.wdTouchTarget()` 必须出现在 `modifier` 之后一行内）+ review |
| **M2 断言（AR-42）** | `WDButton(modifier = Modifier.fillMaxWidth())` → 节点宽 == 父宽；`modifier = Modifier.padding(16.dp)` → 组件外缘 +16dp | Robolectric `assertWidthIsEqualTo` |

**`Modifier.wd*` 允许清单（6 条，AR-43 修正）**：

| 修饰符 | 是否读 local | 说明 |
| --- | --- | --- |
| `wdTouchTarget(minSize: Dp = WDSize.touchTargetMin)` | ❌ | 自研 `layout`，零 m3（AR-66） |
| `wdGlass(level: WDGlassLevel)` | ❌ **只接参数** | **解析在组件体内**（`WDGlass.resolve(...)` 算好后作为参数传入）；内部读 `LocalWDEffectsBudget` 会退化成 `Modifier.composed`（`07-summary.md:82` 反模式 2） |
| `wdMergedRow()` | ❌ | 纯语义 |
| `wdLabelledBy(label: String)` | ❌ | 纯语义（只写不读） |
| `wdLiveRegion(stateDescription: String)` | ❌ | 顶层函数，参数传入（AR-75） |
| `wdDialogImePadding()`（internal） | ✅ | 只允许弹层组件调用；走 `union` 去重 |
| `wdSystemGestureExclusion()` | ❌ | **只允许 drag handle 调用**（G12 断言调用点 == 1） |

**主题扩展点（AR-44 终稿 + LR-01：8 个参数，provide 8 个 local）**

```kotlin
// 文件：…/foundation/theme/WDTheme.kt
@Composable
public fun WDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: WDColors = if (darkTheme) wdDarkColors else wdLightColors,
    gradients: WDGradients = if (darkTheme) wdDarkGradients else wdLightGradients,
    typography: WDTypography = wdTypography,
    layout: WDLayout = WDLayout.Comfortable,                 // AR-45：companion 单例，不是构造调用
    budget: WDEffectsBudget = WDEffectsBudget.Default,        // AR-45②：companion 单例
    icons: WDIconSet = WDIconSet.Empty,                       // AR-44：补上第 6 个注入点
    motionScale: WDMotionScale = WDMotionScale.Normal,        // AR-44：补上第 7 个注入点
    content: @Composable () -> Unit,
) {
    val m3ColorScheme = remember(colors, darkTheme) { colors.toMaterialColorScheme(darkTheme) }  // AR-45①
    val m3Typography = remember(typography) { typography.toMaterialTypography() }               // AR-65
    val m3Shapes = remember { wdShapes() }                                                      // AR-65
    CompositionLocalProvider(
        LocalWDColors provides colors,
        LocalWDGradients provides gradients,
        LocalWDTypography provides typography,
        LocalWDLayout provides layout,
        LocalWDEffectsBudget provides budget,
        LocalWDIcons provides icons,
        LocalWDMotionScale provides motionScale,
        LocalWDContentColor provides colors.textPrimary,     // LR-01：第 8 个 local（由 colors 派生，**不需要新参数**）
    ) {
        MaterialTheme(colorScheme = m3ColorScheme, typography = m3Typography, shapes = m3Shapes, content = content)
    }
}
```

**不引 `LocalWDAppearance`（AR-44 ②/AR-63 裁决）**：`WDAppearance` 只作**值**传给 `WDGlass.resolve(level, appearance, capabilities, budget)`；`WDTheme` 内部由 `darkTheme` + 高对比度探测得到该值。`rememberWDHighContrast()` 保留为**只读工具函数**。

**`WDLayout` 值等 + 单例（AR-45 / B7 的根修法）**

```kotlin
// 文件：…/foundation/theme/WDLayout.kt
@Immutable
public class WDLayout internal constructor(public val density: WDLayoutDensity) {
    public val rowHeight: Dp get() = when (density) { /* 见 §3.3.1 */ }
    public val cardPadding: Dp get() = when (density) { /* … */ }

    override fun equals(other: Any?): Boolean = other is WDLayout && other.density == density
    override fun hashCode(): Int = density.hashCode()

    public companion object {
        public val Comfortable: WDLayout = WDLayout(WDLayoutDensity.Comfortable)
        public val Compact: WDLayout = WDLayout(WDLayoutDensity.Compact)
    }
}
```

配套四条（AR-45）：① `ColorScheme` 必须 `remember(colors, darkTheme)`（已并入 `WDTheme`）；② `WDAppearance`/`WDEffectsBudget`/`WDIconSet` 补值等或同款单例；③ **G11** 断言"`foundation/theme/**` 的默认参数表达式不得是构造调用"；④ 验收：`WDTheme` 因自身（`darkTheme` 翻转）重组时，读 `LocalWDLayout` 的节点**不得**重组。

### 2.6 状态、indication 与语义

#### 2.6.1 状态优先级（U6，沿用）

`disabled > loading > pressed > focused > hover > default`，三条派生：disabled 最高且吞输入 / loading 忽略 `action` 但留在树 / focused-hover 是叠加维度。

#### 2.6.2 indication 与按下反馈（AR-47 / AR-48）

```kotlin
// 文件：…/internal/WDPressIndication.kt
internal object WDPressIndication : IndicationNodeFactory {          // object 天然身份相等
    override fun create(interactionSource: InteractionSource): DelegatableNode = WDPressIndicationNode(interactionSource)
    // AR-48：不覆盖 equals/hashCode（旧稿的 hashCode() = -1 会误导后人以为要做值等）
}

// 按下"亮度 96%"的落地（AR-47）：固定为叠加一层带 alpha 的 fillPressed
Modifier.drawWithContent {
    drawContent()
    drawRect(WDTheme.colors.fillPressed.copy(alpha = WDComponent.pressOverlayAlpha))
}
```

- **新增令牌（AR-47）**：`motion.component.press-overlay-alpha`（缺省建议 0.06，深浅同值；若设计给深浅两套叠加色则改用两键）→ **D 系列需设计确认**（§8.1 新增 D7）。
- **三条禁则（写进 G 系列文字扫描）**：禁止 `ColorMatrix`、禁止 `renderEffect`/离屏层、禁止用 `graphicsLayer.alpha` 表达"亮度"。
- **设计侧的规格冲突**：`specs/01-basic.md:117`（保留 Ripple）与 `:118`（`indication = null`）二者留一；**本稿按 `117` 执行（保留水波 + `WDPressIndication`）**，请设计侧删除 `:118`。

#### 2.6.3 语义映射（AR-49 终稿）

| 语义维度 | 落点 | 本稿修正 |
| --- | --- | --- |
| 名称 | `contentDescription` / `Modifier.wdLabelledBy(label)` | `WDTextField` 例外（不设） |
| 值 | `stateDescription = loadingLabel` **经空安全写入**：`loadingLabel?.let { stateDescription = it }` | AR-49 ①（旧稿这行编不过） |
| 禁用 | **由 `clickable(enabled = false)` 自带**（E4：`AbstractClickableNode` 常量池含 `disabled`），**删除**每个组件里手写的 `semantics { disabled() }` | AR-49 ②：一处收口；若实测未带语义，补在 `WDSemantics` 的 `Modifier.wdDisabled()`（单点） |
| 角色 | **恰好 9 个取值，写死**：`Role.Button` / `Role.Checkbox` / `Role.Switch` / `Role.RadioButton` / `Role.Tab` / `Role.Image` / `Role.DropdownList` / `Role.ValuePicker` / `Role.Carousel`（LR-16：**删掉旧稿误列的 `Role.Slider`**——`Role.Companion` 里没有它） | AR-49 ③ + LR-16：产出 `contracts/roles-android.yaml`（与 iOS traits 对照）；**反射单测 `WDRoleContractTest`** 断言取值集合 == 契约表集合（见 §1.5.4-J） |
| 容器 | `isTraversalGroup + collectionInfo` / `paneTitle + dialog()` | A-1 |
| 播报 | `wdLiveRegion(stateDescription)` + `WDAnnouncer`（AR-75） | 见 §3.6.5 |
| 进度 | `progressBarRangeInfo` + `setProgress`（无障碍**动作**） | A-8；**LR-16：`WDSlider` 走这条，不写 `Role`**（`Role` 无 Slider；`SemanticsActions.SetProgress` 属动作不属角色） |
| 焦点 | `focusRequester` / `focusRestorer`（实验 API 只在 `foundation/accessibility/` 收口一次 opt-in） | A-3 |
| 装饰 | `clearAndSetSemantics {}` | `06-accessibility.md:84` [读] |

### 2.7 边界场景与去抖责任（AR-50 / AR-51 / AR-52）

- 超长文本：标签类允许单行截断（`maxLines = 1` + `Ellipsis`）；正文**禁止 `maxLines = 1`**；截断场景断言 `onNodeWithContentDescription(完整文案)` 存在且唯一（AR-50）。
- 空数据：组件不渲染占位；`WDBadge(count = 0)`/`WDAvatarStack(count = 0)` 隐藏（`clearAndSetSemantics {}`）。
- 极端尺寸：预览矩阵 S-2.0 档；不假设"能完整显示"。
- **快速点击/重复提交：库不去抖**（只保证 loading 期间 `action` 计数 == 0、disabled 不产生按下态、一次手势一次 `onClick`）；**调用方责任 = 状态机**。

```kotlin
// demo 推荐写法（AR-52：删掉 `if (loading) return@WDButton` —— 那会暗示库有两套机制）
@Composable
fun AsyncButtonSample(submit: suspend () -> Unit) {
    var loading by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    WDButton("提交", loading = loading, loadingLabel = "处理中", onClick = {
        loading = true
        scope.launch { try { submit() } finally { loading = false } }
    })
}
```

### 2.8 内部实现

#### 2.8.1 布局与测量规则 L1–L8（AR-53 加约束）

| # | 规则 | 反例 |
| --- | --- | --- |
| L1 | 容器高度只设下界 `Modifier.heightIn(min = …)` | `Modifier.height(WDSize.controlMd)` |
| L2 | 文字容器 `wrapContentHeight`，不与固定高度并排 | `Row(Modifier.height(44.dp)) { Text(...) }` |
| L3 | `maxLines` / `overflow` 必须显式写 | 依赖默认值 |
| **L4** | 横排→竖排的阈值来自令牌 **`WDComponent.layoutBreakFontScale`**（**不得**写死 `1.3f`）；读取一律 `LocalDensity.current.fontScale`（**禁止** `Configuration.fontScale`） | `if (fontScale >= 1.3f)` |
| L5 | 图标/头像/复选框/进度环锁 `dp`（`06:122-131`） | 图标用 `sp` |
| L6 | 一行多元素时文字 `Modifier.weight(1f, fill = false)` | Row 内不设权重 |
| L7 | 热区与视觉解耦（`wdTouchTarget()`；行高类组件**行高即热区** —— **案 B：行高 = 布局盒 48 = 可见内容 44 + 上下各 2dp 内边距**，见 §2.2③ / §3.3.1） | 用 `size` 同时表达两者 |
| L8 | 自绘文字必须 `with(LocalDensity.current) { sp.toPx() }` | 直接 `toPx()` |

**行盒断言分两层（AR-62 / **LR-02** / **LR-28**）**

| 层 | 文件 | 断言 |
| --- | --- | --- |
| 令牌层 | `WDTokensTest` | `letterSpacing` 只有 `overline` 非 0；弹簧公式 `abs(stiffness − μ·(2π/R)²) ≤ 0.5`；行盒比 `1.15 ≤ lineHeight/size ≤ 1.40`（12 条，含 `overline`） |
| **布局层（LR-02：改用 `SemanticsActions.GetTextLayoutResult` **动作**，`SemanticsProperties` 里**没有** layout 属性）** | `WDLineBoxTest`（Robolectric） | 见下方"可编译骨架"与两句定稿断言 |
| fixtures | `android/wisdom-ui/src/test/resources/linebox-fixtures.json` | **12 字阶 × {zh-Hans, en} × {默认, 放大档}** 的 `natural` 实测值；**缺失即 fail（不得 skip）**（LR-28；M1 首日填，见 V12） |

**行高/热区的布局层断言（案 B / P7）**：① **紧凑单行可见内容高度 == 44dp ± 0.5**（测内容区、**不含**内边距；P7 新增）；② **紧凑单行布局盒（= 热区）== 48dp ± 0.5**（AR-67 原断言，保留）；③ `(compact, 2)` 布局盒 == 76dp ± 0.5（原断言，保留）；④ `(comfortable, 1)` == 60dp ± 0.5（原断言，保留）。

**布局层断言的可编译骨架（LR-02）**：

```kotlin
// android/wisdom-ui/src/test/kotlin/…/typography/WDLineBoxTest.kt
@RunWith(RobolectricTestRunner::class)
class WDLineBoxTest {
    @get:Rule val rule = createComposeRule()

    /** 路线 A（推荐）：用 GetTextLayoutResult 动作取真实渲染结果。 */
    private fun renderedLineBox(text: String, style: WDTextStyle): Int {
        var height = -1
        rule.setContent {
            WDTheme { Text(text = text, style = style.toTextStyle(), modifier = Modifier.testTag("probe")) }
        }
        rule.onNodeWithTag("probe").fetchSemanticsNode().let { node ->
            val action = node.config[SemanticsActions.GetTextLayoutResult].action!!
            action(listOf())                       // 触发一次并填充
            height = node.config[SemanticsActions.GetTextLayoutResult].action!!(listOf())
                .first().size.height               // ← TextLayoutResult.size.height（Int，px）
        }
        return height
    }

    /** 路线 B（等价、更直观）：onTextLayout 捕获 + runOnIdle 读值。 */
    private fun renderedLineBoxB(text: String, style: WDTextStyle): Int {
        var captured: TextLayoutResult? = null
        rule.setContent { WDTheme { Text(text, style = style.toTextStyle(), onTextLayout = { captured = it }) } }
        rule.waitForIdle()
        return requireNotNull(rule.runOnIdle { captured }).size.height
    }

    @Test fun `默认档 = max(设计行盒×缩放, natural) ± 1px`() {
        val fx = LineBoxFixtures.of(script = "zh-Hans", tier = Tier.Default)      // 或 "en"
        WDType.entries.forEach { style ->
            val design = style.lineHeight.value                                    // sp
            val scale  = /* LocalDensity 注入档位；默认档 = 1f */
            val expected = max(design * scale * densityFontScale, fx.naturalPx(style))
            val actual = renderedLineBox(text = fx.sampleText, style = style)
            assertTrue("|Δ| = ${abs(actual - expected)} > tol(1px)", abs(actual - expected) <= 1.0)
        }
    }

    @Test fun `放大档 不裁切：box ≥ natural × 行数`() {
        val fx = LineBoxFixtures.of(script = "zh-Hans", tier = Tier.Zoom)         // fontScale = 2.0
        WDType.entries.forEach { style ->
            val lines = 3
            val actual = renderedLineBox(text = fx.sampleText.repeat(lines), style = style)
            assertTrue(actual >= fx.naturalPx(style) * lines)
        }
    }
}
```

**U5 的两句定稿（LR-28 = D2 = iOS CR-8，两端同一份契约文本）**：

1. **默认档**：`|renderedLineBox − max(设计行盒 × 缩放, natural(script))| ≤ tol`，其中 **iOS `tol = 0.5pt` / Android `tol = 1px`**（理由 = Robolectric 的 JVM 字体度量；该差异登记 **F42**）；
2. **放大档（不裁切）**：`renderedLineBox ≥ natural(script) × 行数`；
3. **fixture 矩阵**：**12 字阶 × {zh-Hans, en} × {默认, 放大档}**，**缺失即 fail**；两端各记一份（Android 侧 = V12）；
4. **禁止**："两端行高一致（22pt）"之类的表述与跨端绝对值比较；**"默认档 = 设计值"只在 `natural ≤ 设计行盒` 时才成立**（`07-summary.md:43` 的"±0.5pt 精确等于"据此**澄清**为公式形式——`08-decisions.md:29` 的硬约束 2 本来就写的是 `max(设计值×缩放, 自然行高)`）。

> **对 `07-summary.md:43` 的措辞请求（D2，已由 `21` §2.1 裁定"采纳，按统一形式"）**：请把 U5 的"默认档精确等于设计值（±0.5pt 容差）"改为上面第 1 句的**带容差 `max` 形式**；`07:190` 的"+2 余量"口径勘误由 tech-lead 在下一版处理（`21` §8-L6）。

#### 2.8.2 组件行为规则 C1–C15（"组件不该管" 12 条 + 3 条禁则）

C1 不做网络/磁盘 IO；C2 不做导航/路由；C3 不持有业务真源；C4 不做文案本地化（L-B）；C5 不做主题定义（不得出现静态令牌入口）；C6 不覆写平台/第三方 `CompositionLocal`；C7 不做权限/系统弹窗/分享/通知；C8 不管键盘/IME（**弹层组件显式例外**，见 §3.3.5）；C9 不管安全区（只暴露 `contentWindowInsets` 入口）；C10 不做全屏/系统栏/打点/日志；C11 不做分页/自动加载；C12 不做字体族决策与截断兜底；**C13 禁止 `pointerInput` 自造按下态**；**C14 禁止第二个 `interactionSource`/第二个按下源**；**C15 禁止覆写 `LocalIndication`**（库自己的 indication 只在调用点传）。

#### 2.8.3 复用、虚拟化与缓存（AR-54 / AR-55）

- **虚拟化**：库不提供列表容器；`LazyColumn` 由调用方使用。**`Modifier.animateItem()` 是 `LazyItemScope` 的扩展，`WDListRow` 内部无法调用**（AR-54）——文档只给可执行写法：

```kotlin
LazyColumn { items(tasks, key = { it.id }) { task ->
    WDListRow(title = task.title, modifier = Modifier.animateItem())   // 位置动画由调用方的 key 决定
} }
```

- **三处允许的缓存**：行盒 memo（key = `(style, fontScale, fontFamily, locale)`，**AR-55：不是 `Density` 对象、不是 `Configuration`**）；`WDButton` 两态共用的文本测量（结构复用）；生成常量（`WDType.*` 等 `object` 上的 `val`）。
- **渐变画刷（U3 复核）**：`WDLinearGradientBrush` 必须加 `equals/hashCode`，**同时比较 `angleDegrees` 与 `stops` 浮点列表**（AR §3.3-3），不能只比 `spec` 引用。
- **禁止**：`remember` 缓存派生值（`derivedStateOf` 除外）、`remember` 的 key 写每次都变的表达式、给列表项加模糊（P-1）。

#### 2.8.4 动画驱动（AR-56 降级未闭合项）

| 类型 | 驱动 | 是否自动尊重系统缩放 |
| --- | --- | --- |
| 属性动画 | `animate*AsState` / `Animatable` / `updateTransition` | ✅ 自动（组件**不得**重复判断） |
| 手势驱动 | `Animatable.snapTo/animateTo` + `Modifier.draggable` | ✅ |
| 常驻/自绘 | **必须**经 `WDMotion`：`rememberWDMotionScale()`；`scale == 0f` → 静态 | ❌ 需自己判断 |
| 禁止 | 组件直接 `rememberInfiniteTransition` | — |

**V5 降级（AR-56）**：`rememberWDMotionScale()` 的读取机制**已由字节码确认**（【11 §0.1 E7】：`MotionDurationScaleImpl` 持 `MutableFloatState` 并注册 `ContentObserver`，读它可观察、会触发重组）；**只剩真机数值与首帧时序待复验**（→ §9.1 V5，low，不阻塞 M0）。

### 2.9 类型、稳定性与 ABI

#### 2.9.1 `@Immutable` / `@Stable` 全量清单 + 机器检查（AR-57）

| 类 | 标注 |
| --- | --- |
| `WDColors` / `WDGradients` / `WDGradientSpec` / `WDTextStyle` / `WDTypography` | `@Immutable` |
| `WDLayout` / `WDEffectsBudget` / `WDIconSet` / `WDAppearance` / `WDGlassCapabilities` / `WDGlassSpec` / **`WDElevationSpec` / `WDElevationLayer`**（LR-19） | `@Immutable` |
| `WDBottomSheetState` | `@Stable`（可变） |
| 生成 `object`（`WDSize` 等） | 无标注 |

**值等规则（LR-19：含 `List` 字段的类型必须**逐层比较**，参照 `WDLinearGradientBrush` 的 `stops` 规则）**：

| 类型 | `equals`/`hashCode` 必须比较 |
| --- | --- |
| `WDElevationSpec` | `layers` **列表内容**（不是列表引用） |
| `WDElevationLayer` | `y` / `blur` / `color`（+ `x`，若保留）逐字段 |
| `WDGlassSpec` | `level` / `fillAlphaLight` / `fillAlphaDark` / `hasTopHighlight` / `hasHairline` |
| `WDLinearGradientBrush`（internal） | `angleDegrees` + `stops` 浮点列表（AR §3.3-3） |

**检查**：`G5` 断言 `api/wisdom-ui.api` 里上述每个类名后 3 行内出现 `$stable`（实测该字段确实进基线 [11 §0.2]）；`G6` 断言 `inferredUnstableClasses == 0`。`WDTextStyle` 是现状**唯一漏标**的生成类型（`WDTokens.kt:243` vs `:161,168` [读]）→ M0-3 第二个提交补。

#### 2.9.2 类型级约束：用类型系统挡错误用法（value class / sealed interface / enum）

| 手段 | 用途 | Android 侧落点 |
| --- | --- | --- |
| `@JvmInline value class` | 单位与量纲安全 | `WDMotionScale(value: Float)`（§3.5.2）；**value class 上不标 `@Immutable`**（AR-73） |
| `sealed interface` | 互斥状态/结果，让 `when` 必须穷举 | `WDBottomSheetDetentSource`（若 M6 支持受控 detent）；`WDActionResult`（若 A-4 决定提供） |
| `enum class` | 跨端契约要锁名字的有限变体 | `WDButtonVariant` / `WDButtonSize` / `WDBottomSheetDetent` / `WDGlassLevel` / `WDLayoutDensity` / `WDEffectKind` |
| `@Immutable` / `@Stable` | 给 Compose"允许跳过"的许可 | §2.9.1 全量清单 |
| `@RequiresOptIn`（`WDInternalApi`） | public 但不承诺兼容的 API 档位 | 仅 `…internal` 包；由 G9 兜住 |
| 非空 vs 可空 | 规格"必有/可选"的类型化 | `WDIconButton(contentDescription: String)`（必填）vs `leadingIcon: (@Composable () -> Unit)?` |
| **禁止** | — | 不用 `@JvmOverloads` / `@JvmName`；不用 `DeprecationLevel.HIDDEN`；生成类不出 `copy/componentN` |

#### 2.9.3 ABI 与废弃策略（AR-58 加一行）

| 项 | 规则 |
| --- | --- |
| 新增带默认值的参数 | **二进制不兼容**（`$default` 合成方法签名变化 → 老消费方 `NoSuchMethodError`）：必须走 major 或改新增重载；CI 用 `grep -c '\$default'` 前后比对（§1.5.4-F） |
| 改默认值 | 语义变更，进 CHANGELOG；**默认值只能靠契约锁**（Swift 默认值不进 ABI，**F32**） |
| 枚举新增 | 源级 breaking（消费方穷举）；库内 `when` 必须穷举 |
| `@Deprecated` | `WARNING` + `ReplaceWith`，保留 ≥1 minor；**禁 `DeprecationLevel.HIDDEN`**（BCV 按删除处理）；成员数由 **G4** 守 |
| 生成类 | `WDColors`/`WDGradients`/`WDTextStyle`/`WDTypography` 用 `internal constructor`；**`WDGradientSpec` 保留公开构造器**（AR §4.2-4；**并入 F33**） |
| 禁止 | `@JvmOverloads` / `@JvmName` / 生成类 `copy`/`componentN`（保留 `equals/hashCode/toString`） |
| 手写类 | `WDLayout` / `WDBottomSheetState` 同样 `internal constructor`（AR-77 对 **F33** 的加约束） |

### 2.10 37 组件默认值与枚举 case 对照表（LR-24 / **C-15 副本**；Android 侧，与 iOS §2.10 同形）

> 用途：**M0-5 的两端默认值对照表的 Android 列**（iOS 侧已备同形表）。判据：`U` = 名字/取值必须两端一致；`F` = 平台惯例（登记进 §4.1）；`(契约)` = 值由 `contracts/<component>.yaml` 的 `params[].default` 锁。
> **受控值名的真源 = `40-contract-names.md`（C-15 唯一表）**；本表是它的**副本**，只读——改名先改 C-15 再同步本表（与 F 注册表的副本纪律同源）。本表的 **"契约名"列即 `contracts/<component>.yaml: params[].name` 的取值**（M0-5 落库）。
> 枚举 case 名以**本表为准**（旧稿"从未枚举 case"的缺口，LR-24）；`决定人`：`D`=设计、`AL`=android-lead、`A`=架构师。
> **R3A-01 的改名结果（Android 14 行，逐行与 C-15 §2.1 一致）**：`WDTextField`/`WDSearchField` `value→text`、`WDSwitch` `checked→on`、`WDRadio`/`WDListSection` `selected→selection`、`WDProgressBar`/`WDProgressRing` `progress→value`、`WDDatePicker` `value→date`、`WDAlert`/`WDBottomSheet`/`WDActionSheet`/`WDToast` `visible→presented`、`WDTabBar` `selectedIndex→selection`、`WDAssigneePicker` `selectedIds→selection`；**`WDCheckbox` 保持 `checked`**（iOS 侧改 `isOn→isChecked`，C-15 §2.2）；**`WDBanner` 保持 `visible`**（行内可见，不进 `presented` 组，C-15 §1-#29）。

| # | 组件 | 受控值（Android 名） | **契约名（C-15 §1）** | 关键默认（Android） | 枚举 case（Android） | U/F（**改名后如实**） | 决定人 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 01 | `WDButton` | `loading` | **`loading`** | `variant = Filled`、`size = Md`、`enabled = true`、`loading = false`、`loadingLabel = null` | `WDButtonVariant{Filled, Tonal, Glass, Outline, Plain, Destructive}`、`WDButtonSize{Sm, Md, Lg}` | U（名词一致；`is` 前缀 = **F1**） | D |
| 02 | `WDIconButton` | `loading` | **`loading`** | `variant = Plain`、`enabled = true` | 复用 `WDButtonVariant`（子集：`Plain/Filled/Glass`） | U（同上） | D |
| 03 | `WDTextField` | **`text`**（← `value`） | **`text`** | `variant = Inset`、`label` **必填**、`readOnly = false`、`singleLine = false`、`maxLines = "MAX_VALUE unless singleLine"`、字段盒 `46.dp` | `WDTextFieldVariant{Inset, Outline, Glass}` | U（名词一致；**能力集差异 = F23**） | D |
| 04 | `WDSearchField` | **`text`**（← `value`） | **`text`** | `enabled = true` | — | U（名词一致） | D |
| 05 | `WDSwitch` | **`on`**（← `checked`） | **`on`** | `enabled = true` | — | U（`is` 前缀 = **F1**；设计口径"布尔开关"） | D |
| 06 | `WDCheckbox` | `checked` | **`checked`** | `enabled = true` | — | U（`is` 前缀 = **F1**；**iOS 侧改 `isOn→isChecked`**） | D |
| 07 | `WDRadio` | **`selection`**（← `selected`） | **`selection`** | `enabled = true` | — | U（名词一致） | D |
| 08 | `WDSlider` | `value` | **`value`** | `steps = 0`、`enabled = true` | —（**不进 `Role`**；`progressBarRangeInfo` + `setProgress`，LR-16） | U | D |
| 09 | `WDStepper` | `value` | **`value`** | `enabled = true` | — | U | D |
| 10 | `WDChip` | `selected` | **`selected`** | `enabled = true` | — | U（`is` 前缀 = **F1**） | D |
| 11 | `WDBadge` | — | — | `max = 99`；`count = 0` 隐藏 | — | U（无受控值） | D |
| 12 | `WDAvatar` | — | — | 直径锁死（`WDSize.avatarMd`） | — | U（无受控值） | D |
| 13 | `WDAvatarStack` | — | — | `max = 4`、`count = 0` 隐藏 | — | U（无受控值） | D |
| 14 | `WDDivider` | — | — | 装饰：`clearAndSetSemantics {}`（不进树） | — | U（无受控值） | D |
| 15 | `WDProgressBar` | **`value`**（← `progress`） | **`value`** | `value = null`（`null` = 不确定态） | — | U（名词一致；`null` 语义两端一致） | D |
| 16 | `WDProgressRing` | **`value`**（← `progress`） | **`value`** | `value = null`、环径锁死 | — | U（同上） | D |
| 17 | `WDCard` | — | — | `style = Elevated` | `WDCardStyle{Elevated, Outlined, Glass}` | U（无受控值） | D |
| 18 | `WDListRow` | `selected` | **`selected`** | `showsSeparator = true`（**LR-24 已从 `divider = false` 改齐**）、`enabled = true`、`density = null`（= 跟随 `LocalWDLayout`）、行高 `max(密度档下限, 槽位派生)` | — | U（`is` 前缀 = **F1**；行高公式 U；`density` 注入 = **F44**） | D + AL |
| 19 | `WDListSection` | **`selection`**（← `selected`） | **`selection`** | 末行无分隔 | — | U（名词一致） | D |
| 20 | `WDIcon` | — | — | `tint = Color.Unspecified`（→ `LocalWDContentColor`）、`size = WDSize.iconMd`；`contentDescription = null` = 装饰 | — | U（无受控值；注入机制 = **F14**） | AL |
| 21 | `WDSegmentedControl` | `selection` | **`selection`** | `enabled = true` | — | U | D |
| 22 | `WDPicker` | `selection` | **`selection`** | 空数组断言（debug） | — | U（形态 = **F48**） | D |
| 23 | `WDDatePicker` | **`date`**（← `value`） | **`date`** | `enabled = true`、`title = null` | — | U（名词一致；**载体 = F41**） | D + AL |
| 24 | `WDFormRow` | — | — | 标签关联（默认上提点击目标） | — | U（无受控值） | D |
| 25 | `WDAlert` | **`presented`**（← `visible`） | **`presented`** | `destructive = false` | — | U（`is` 前缀 = **F1**；形态 = **F21**） | D |
| 26 | `WDBottomSheet` | **`presented`**（← `visible`） | **`presented`** | `detents = WDBottomSheetDetents.All`、`initialDetent = WDBottomSheetDetent.Half`（在 `rememberWDBottomSheetState`）、`dragHandle = true`、`glass = Regular` | `WDBottomSheetDetent{Half, Large}`；`WDBottomSheetDetents{All, Fixed}` | U（`is` 前缀 = **F1**；形态 = **F21/F38**） | D |
| 27 | `WDActionSheet` | **`presented`**（← `visible`） | **`presented`** | 空 `items` 断言（debug） | — | U（`is` 前缀 = **F1**；形态 = **F21**） | D |
| 28 | `WDToast` | **`presented`**（← `visible`） | **`presented`** | `durationMillis = 3000`（**支持 ≥5000**，契约 `acceptance.yaml`；真源键 `durationMilliseconds`） | `WDToastVariant{Neutral, Success, Warning, Danger}` | U（`is` 前缀 = **F1**；计时实现 = F） | D |
| 29 | `WDBanner` | `visible`（**不改**） | **`visible`** | 软底（非玻璃） | `WDBannerVariant{Info, Warning, Danger}` | U（`is` 前缀 = **F1**；**行内可见，不进 `presented` 组**） | D |
| 30 | `WDEmptyState` | — | — | — | — | U（无受控值） | D |
| 31 | `WDSkeleton` | — | — | `lines = 3`；减弱动效 → 静态灰块 | — | U（无受控值） | D |
| 32 | `WDPullToRefresh` | `refreshing` | **`refreshing`** | 自实现下拉指示器（iOS 用系统 `.refreshable`） | — | U（`is` 前缀 = **F1**；**视觉/动作载体 = F50**） | D + AL |
| 33 | `WDNavigationBar` | — | — | 玻璃档由文字级别决定（`WDGlass.resolve`）；`<API 31` → 不透明 | — | U（无受控值；U10） | D + AL |
| 34 | `WDTabBar` | **`selection`**（← `selectedIndex`） | **`selection`** | 深色 → 不透明表面（`12-b22-glass.md:20` 裁定） | — | U（名词一致；**类型 = Int 索引**写进契约；P-1/O-13） | D |
| 35 | `WDToolbar` | — | — | 溢出收菜单 | — | U（无受控值；形态 = **F49**） | D |
| 36 | `WDFAB` | — | — | 热区 `WDSize.touchTargetMin`（Android 48 / iOS 44） | — | U（无受控值；U8） | D |
| 37 | `WDAssigneePicker` | **`selection`**（← `selectedIds`） | **`selection`** | 空集合法 | — | U（名词一致；**类型 = ID 集合**写进契约） | D |

**逐行判定口径（R3A-01 之后，如实）**：① **受控值列 = 契约名（C-15）逐行一致**，其中 **14 行 Android 改名**（上表加粗）后达到"名词逐字相同"；**7 行布尔类的差异只剩 `is` 前缀（F1 允许）**（#01/#02/#05/#06/#10/#18/#29，另 #25–#28/#32 同法）；② **不存在"两端名词不同却标 U"的行**（这正是 R3A-01 修掉的错）；③ 非名字类差异仍如实标 F：`F23`（#03 能力集）、`F41`（#23 载体）、`F44`（#18 密度注入）、`F48`（#22 等形态分类）、`F49`（#35 Toolbar）、`F50`（#32 载体）、`F21/F38`（#26 等弹层形态）、`F14`（#20 注入机制）——**全部已在 §4.1 的 F21–F50 有对应行**；④ **尚未出完整签名的组件**（#05/#07/#19/#25–#28/#34/#37 等）在 **M2–M6 批次出签名时直接按本表命名**（无需额外返工）；⑤ **回调名**不在 C-15 范围：按 C-15 §5 的配对建议在 M0-5 一并锁（`text ↔ onTextChange`、`presented ↔ onDismissRequest`、`selection ↔ onSelectionChange`、`refreshing ↔ onRefresh`），本轮**不改**现有 `onValueChange`/`onDismissRequest` 形参名。

---

## 3. 主题、样式与交互

### 3.1 Token：双轨制与生成物终稿

#### 3.1.1 双轨（AR-59 沿用）

| 轨道 | 内容 | 形态 |
| --- | --- | --- |
| 编译期常量轨 | 间距、圆角、尺寸、字阶、动效时长、弹簧、玻璃档位参数、密度数值、**阴影值** | `object` 上的 `public val`（**非 `const`**） |
| 运行时变量轨 | **32** 语义色（含 `text.disabled`；P8）+ 5 条渐变 | `@Immutable` 对象 + `CompositionLocal` 注入 |

**`const val → val` 的理由改写（AR-60）**：真问题是**内联**（已编译的消费方不重编就永远拿旧值）；"`apiDump` 看不出变化"不是 `const` 的锅——实测 `api/wisdom-ui.api:68-72` 对 `const` 与非 `const` **都不打印值**（【11 §0.2】）。因此：

- 改非 `const` 的收益 = **避免内联** + ABI 形状从 `field` 变 `getter`（可被 `apiCheck` 观测）；
- **值的可见性只由 banner hash + `WDTokensVersion.sha256` 提供**（G7），与 `const` 与否无关。

#### 3.1.2 唯一一张三仓 M0-1 冻结清单（LR-11：每行「旧键 → 新键 → 值 → 决定人」）

> **规则（照抄 iOS `02-ios-spec.md:284`）**：每个值**必须二选一** —— (a) 进令牌（M0-1 变更集内落完）；(b) 进 `contracts/README.md` 的 **F 系列/契约表**（两端各写自己的常量，但取值有契约真源）。**不允许悬空**；**无值不进清单**（LR-11：旧稿第 6/10 行没值、没旧键、没决定人，本轮已剔除）。
> 本表 = **iOS CR-10 的 12 行 + Android 的 5 项（行高（可见内容单值键 44 + Android 布局盒 48）/ 触控双键 / `press-overlay-alpha` / `layout-break-font-scale` / `WDElevation` 出口）+ D6 键名**，合并去重后的唯一清单（两端共用同一份；`(a)/(b)` 列标明落点）。

| # | 旧键 / 旧形态 | 新键（(a) 令牌） | 值 | 决定人 | 归属 |
| --- | --- | --- | --- | --- | --- |
| 1 | `motion.spring.*.{response, dampingFraction, stiffness}` | `motion.spring.*.{response, dampingRatio}`（删 `stiffness`，**`dampingFraction` → `dampingRatio`**，D6） | `0.40/0.85`、`0.28/0.82`、`0.42/0.68` | 设计 + 架构师 | **(a)** M0-1 |
| 2 | （无） | `type.*.letterSpacing` | 仅 `overline = 0.6`，其余 `0` | 设计 | **(a)** M0-1 |
| 3 | `size.touch-target-min: 44` | `size.touch-target-min-ios` / `size.touch-target-min-android` | `44` / `48` | U8 已裁 | **(a)** M0-1 |
| 4 | （无） | `size.row-height.comfortable`（**单值键**，两端同值） | `60` | 设计（D1） | **(a)** M0-1 |
| 5 | （无） | `size.row-height.compact`（**单值键**，两端同值 = **可见内容**） | **`44`**；Android 的**布局盒 48** = 44 + 上下各 2dp 透明内边距（由 `Modifier.wdTouchTarget()` 实现，**不新增令牌键**） | **用户裁决（D1 / 案 B）** | **(a)** M0-1 |
| 6 | （无） | `size.card-padding.comfortable` / `.compact` | `16` / `12` | 设计（D-8） | **(a)** M0-1 |
| 7 | （无） | `size.field-height`（iOS CR-10-1；**LR-06**） | `46` | 设计（`specs/01-basic.md:239`） | **(a)** M0-1 |
| 8 | （无） | `size.sheet.detent.half` / `.large`（iOS CR-10-3；**LR-10**） | `0.5` / `0.92` | 设计（`specs/02-advanced.md:569`） | **(a)** M0-1 |
| 9 | （无） | `size.sheet.max-width`（iOS CR-10-4） | `480` | 设计（`specs/02-advanced.md:591`） | **(a)** M0-1 |
| 10 | （无） | `size.sheet.corner-radius`（iOS CR-10-3 的圆角） | `32` | 设计 | **(a)** M0-1 |
| 11 | （无） | `size.sheet.handle.width` / `.height` / `size.sheet.handle-top-offset`（iOS CR-10-7 的把手几何；Android 可定制 ⇒ 进令牌） | `36` / `5` / `8` | 设计 | **(a)** M0-1（iOS 侧同值记 F27） |
| 12 | （无） | `motion.duration.reduced`（iOS CR-10-5） | `150` | 设计 | **(a)** M0-1 |
| 13 | （无） | `motion.component.press-overlay-alpha`（**D7① / AR-47**） | `0.06`（深浅同值；若设计给两套则改两键） | 设计（D7） | **(a)** M0-1 |
| 14 | （无） | `motion.component.layout-break-font-scale`（**D7② / AR-53**） | `1.3` | 设计 + android-lead | **(a)** M0-1 |
| 15 | （无） | `motion.component.*` 补全出口：`switch-track 260` / `toast-in 320` / `list-stagger 20` / `skeleton-shimmer 2200`（**AR-61 / B4**） | 见左（键已有值） | 架构师（生成器出口） | **(a)** M0-1（**无新值，仅补出口**） |
| 16 | `elevation.*` 有值无出口 | `WDElevation` 生成出口（`level0/1/2/3/brand`）（**AR-74 / LR-19 / LR-27**） | 见 `wisdom.tokens.json:252-271` | 架构师（生成器出口） | **(a)** M0-1（**无新值，仅补出口**） |
| 17 | （无） | `state.hover.brightness` / `state.pressed.brightness` / `state.focus.ring-width` / `state.focus.ring-alpha` / `state.disabled.alpha`（iOS CR-10-6） | `98` / `96` / `3` / `32` / `40` | 设计 | **(a)** M0-1；**设计若拒绝则改 (b)** 并在 `contracts/README.md` 登记 |
| 18 | iOS CR-10-10：`letterSpacing` 单位 = pt/sp 等价（不随字号缩放） | —（不建键） | 契约注释 | 架构师 | **(b)** `contracts/README.md` |
| 19 | iOS CR-10-11：`WDToast` 停留时长上限 | —（不建键） | 支持 ≥ `5000ms` | 设计 + 两端 | **(b)** `acceptance.yaml` |
| 20 | iOS CR-10-12：弹层系统转场时长（340/240） | —（不建键） | iOS 不适用 | 两端 | **(b)** F21/F27 + U11 断言排除清单 |
| 21 | iOS CR-10-8：关闭阈值 40% / 500pt/s | —（不建键） | iOS 不适用 | 两端 | **(b)** F27 |

**未给值 ⇒ 不进清单（LR-11 的剔除项，等设计给值再并入下一次变更集）**：旧稿第 6 行（`size.navbar-large`、tabbar `56 → 58`）与第 10 行（`status-text.*`、深色 `text.on-fill`、`gradient.mid`、`scrim`、`wash` 净 alpha）。

> 逐项确认（[实测] `21 §1 V7`）：令牌里**没有** `scrim`、**没有** `navbar-large`，`tabbar-height` 现为 **56** —— 这些正是"有名字没值"的行；在得到设计给出的旧键→新值之前，**它们不算 M0-1 冻结项**（避免把"待设计"伪装成"已冻结"）。`07-summary.md:331` 的清单相应按本表执行。

> **键形定稿（案 B，用户 2026-10-05 裁决；P7 回写）**：**行高 = 单值键** —— `size.row-height.{comfortable,compact}` = `60 / 44`，**两端同值（"可见内容"）**；**Android 的 48 是布局盒**（44 + 上下各 2dp 透明内边距，由 `Modifier.wdTouchTarget()` 实现），**不新增令牌键**。触控热区键**仍是双键**（`size.touch-target-min-{ios,android}` = `44 / 48`，U8）。**统一 / 自由两条登记**：**U（必须统一）= 两端"可见内容高度" 44 ± 0.5**；**F（各端自由，平台惯例）= 行距（Android 48 / iOS 44）**。口径来源：`30-dev-plan.md` §5.5 / §5.9（P7；AR-67 为**补充解释**，其原两条断言值保留）。

#### 3.1.2a `schemes` 维度落地条目（A-13 **层一** / P9；口径 = `30-dev-plan.md` §5.8）

> **本条目不进上面那张 21 行表**：`30 §2.2-②` 对它的判据是"**21 行全在**（17 行 (a) + 4 行 (b)，`grep -c == 21`）"；`schemes` 是 M0-1 **同批冻结的第三个维度**（`30 §5.1`），单列在此以免改变该判据口径。

| 项 | 结论 | 来源 |
| --- | --- | --- |
| 令牌 schema | 新增 **`schemes: {light, dark, …}`** 维度（每套一组语义色值；与 `WDColorSlot` **32** 槽位同批冻结，含 `text.disabled`） | 用户决策 #3/#8 + `30 §5.1` |
| 生成器 | 按 **`--schemes`** 一次产出多套；banner 与 `WDTokensVersion` 自证照旧（**G7**） | `30 §5.8` / `30 §8.3-M0-2` |
| 运行时选择（Android） | **`WDTheme(colors = …)`**（在**已生成**的 scheme 之间切换） | `30 §5.8` |
| 切换语义 | 换皮肤 = 换**已生成**的一套 scheme ⇒ 主题值变更触发受影响子树的**重组/重算**，**不重启进程**（无进程级生命周期动作） | `30 §5.8` |
| 性能纪律 | `staticCompositionLocalOf` 值变化 = **整树重组** ⇒ **切换动作不得放进高频路径**（滚动 / 动画 / 输入回调里禁止切换主题）；与 `12 §2.5`（AR-45 / B7）同源 | `30 §5.8` |
| **明确不支持** | 运行时加载任意 `token.json`、服务端下发皮肤、逐槽位任意覆盖；`Q-A2` **不重开**，语义色扩容（31 → 32）仍**非 breaking** | `30 §5.8` |
| 代价（写进 `README.md` 与 CHANGELOG） | 换品牌必须先回设计仓库改 scheme → 重新生成 → 发版；**调用方不能自助加品牌** | `30 §5.8` |
| 出口判据 | M0-1 生成物含 **≥2 套 scheme** 且 `build.js --check` 绿、槽位计数 == **32**；**M1** demo 有切换入口且切换**不需重启进程**（`30 §2.3-M1-4`） | `30 §5.8` + 本稿 §7.2-M1 |

#### 3.1.3 生成物签名（终稿）

```kotlin
// 文件：android/wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/foundation/generated/WDTokens.kt
@Immutable
public class WDTextStyle internal constructor(
    public val size: TextUnit,
    public val lineHeight: TextUnit,          // 总行盒高（绝对值，令牌真源；TYP-1/D-11）
    public val weight: FontWeight,
    public val letterSpacing: TextUnit,
) {
    public val lineHeightRatio: Float get() = lineHeight.value / size.value   // 派生只读（TYP-1）
    override fun equals(other: Any?): Boolean = /* 4 字段 */
    override fun hashCode(): Int = /* 4 字段 */
    override fun toString(): String = /* … */
}

public object WDType { public val overline: WDTextStyle = WDTextStyle(12.sp, 16.sp, FontWeight.Medium, 0.6.sp) /* …12 条 */ }

public object WDSize {
    public val touchTargetMin: Dp = 48.dp            // U8：只生成本端值
    public val rowHeightComfortable: Dp = 60.dp      // **可见内容** 60（两端同值；60 > 48，布局盒即 60）
    public val rowHeightCompact: Dp = 44.dp          // 案 B（P7）：**可见内容** 44（两端同值）；Android 布局盒 48 = 44 + 上下各 2dp 透明内边距（Modifier.wdTouchTarget() 实现，不另立令牌键）
    public val cardPaddingComfortable: Dp = 16.dp
    public val cardPaddingCompact: Dp = 12.dp
}

/** AR-61 + LR-12：**既有 10 个 `motion.component.*` + 新增 2（`press-overlay-alpha` / `layout-break-font-scale`）= 12 个 `WDComponent` 出口**；归属规则见下。 */
public object WDComponent {
    public val pressScale: Float = 0.97f              // :298
    public val pressOverlayAlpha: Float = 0.06f       // ➕ AR-47（新令牌）
    public val checkboxPressScale: Float = 0.90f
    public val checkboxDrawMillis: Int = 260
    public val checkboxGlowMillis: Int = 400
    public val switchKnobMillis: Int = 300
    public val switchTrackMillis: Int = 260           // ➕ 缺
    public val progressEaseOutMillis: Int = 3200
    public val toastInMillis: Int = 320               // ➕ 缺
    public val listStaggerMillis: Int = 20            // ➕ 缺
    public val skeletonShimmerMillis: Int = 2200      // ➕ 缺
    public val layoutBreakFontScale: Float = 1.3f     // ➕ AR-53（L4 的阈值来源）
}

/** AR-74：阴影出口（值来自 elevation.* group） */
public object WDElevation {
    public val level0: WDElevationSpec = WDElevationSpec(emptyList())
    public val level1: WDElevationSpec = WDElevationSpec(listOf(WDElevationLayer(0.dp, 1.dp, 2.dp, Color(0x0A0A3C64)), /* … */))
    // level2 / level3 / brand 同
}

public object WDMotion {
    public val durationInstant: Int = 100            // 非 const（AR-60）
    // …
    public val springGentle: SpringSpec<Float> = spring<Float>(dampingRatio = 0.85f, stiffness = 246.74f)  // μ=1.0
    public val springSnappy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.82f, stiffness = 503.65f)
    public val springBouncy: SpringSpec<Float> = spring<Float>(dampingRatio = 0.68f, stiffness = 223.72f)
}
```

**P8 同步（生成值区 / 槽位计数）**：U12 = **32 槽位**（含 `text.disabled`）⇒ 生成物 `WDColors` 有 **32** 个 `public val`、`WDColorSlot` 槽位名清单 **32** 条；`api/wisdom-ui.api` 的合成构造器参数数随之 +1（属 **`apiDump` 同提交**的可见变化，见 §7.1-M0-4）。计数断言 = `WDTokensTest` 的"**槽位计数 == 32**"。

**时长归属规则（AR-61 ②，写进 README）**：**交互动效时长 → `WDMotion`；某组件私有的描画/节奏参数 → `WDComponent`**。两者都是"时长"但不是同一类，禁止互相搬运。

**阴影映射表（LR-27：**显式 5 行**，不再留"第一层 blur ÷ 2"这种需二次推导的公式；值来自 `elevation.*` 的 layer，标"待 M2 视觉评审"）**：

| 令牌 | 令牌 layers（`wisdom.tokens.json:252-271`） | Android `Modifier.shadow(elevation = …)` | `ambientColor` / `spotColor` | 备注 |
| --- | --- | --- | --- | --- |
| `elevation.0` | `[]` | `0.dp`（不施加） | — | 无阴影 |
| `elevation.1` | `0/1/2` + `0/6/16`，`#0A3C640A` / `#0A3C640F` | **`4.dp`** | `#0A3C640F` / `#0A3C640F`（取较外层） | 列表项 e1 |
| `elevation.2` | `0/2/6` + `0/14/32`，`#0A3C640F` / `#0A3C6417` | **`9.dp`** | `#0A3C6417` / `#0A3C6417` | 卡片浮起 |
| `elevation.3` | `0/8/20` + `0/28/56`，`#0A3C6417` / `#0A3C6421` | **`17.dp`** | `#0A3C6421` / `#0A3C6421` | 每屏 ≤1 |
| `elevation.brand` | `0/6/16`，`#1677B32E` | **`8.dp`** | `#1677B32E` / `#1677B32E` | 品牌色阴影 |

- **映射规则（写死）**：`elevation.dp = ceil(较外层 blur ÷ 3.5)`（上表已是结果，实现直接查表，**不再现算**）；`ambientColor == spotColor ==` 较外层 layer 的 color；`shape` = 组件 `shape` 参数；
- **禁止**为复现多层阴影引入离屏层（`renderEffect` / `Modifier.blur`）——与 P-1 的"不引入离屏层"冲突；
- 登记为 **F43**（Android 阴影为近似映射，iOS 双层 shadow 为精确映射）；上表**待 M2 视觉评审**（与 `press-overlay-alpha` 同批）。

**令牌单测（终稿，替换现状弱断言）**：

| 断言 | 说明 |
| --- | --- |
| `letterSpacing` 只有 `overline` 非 0 | TYP-3 |
| 弹簧公式 `abs(stiffness − μ·(2π/R)²) ≤ 0.5` | μ 来自配置（默认 1.0）；U10 若逐令牌锁值只改配置 |
| 行盒比 `1.15 ≤ lineHeight/size ≤ 1.40`（12 条含 `overline`） | 令牌层 |
| `WDComponent` **12 个键全部存在**（含 `layoutBreakFontScale`/`pressOverlayAlpha`） | AR-61 / AR-53 |
| 行高生成值（**可见内容**）：`rowHeightCompact == 44.dp`、`rowHeightComfortable == 60.dp` | 案 B / AR-67 **补充解释**（P7；两端同值） |
| **`WDColorSlot` 槽位计数 == 32**（含 `text.disabled`；浅深成对） | P8 / 用户决策 #3 |
| 生成物 hash 自证 | G7 |

（现状 `WDTokensTest.kt:37` 的弱断言、"`:46` 自比自"、漏 `overline` 三处全部替换 [读]。）

### 3.2 亮/暗、品牌换肤、高对比度

#### 3.2.1 亮/暗

`WDTheme(darkTheme = isSystemInDarkTheme()) { }`；组件**不得**读 `isSystemInDarkTheme()`，只读 `WDTheme.colors`；深浅各一套 `WDColors`/`WDGradients`。

#### 3.2.2 品牌换肤（AR-64）

**已决（用户决策 #8）= 层一**：只支持 **方案 A** ⇒ 设计侧新增 scheme → 生成器产出 → `WDTheme(colors = …)`（**多套生成 scheme + 运行时选择**，落地条目见 §3.1.2a）；**`Q-A2` 不重开**，**不支持**运行时任意 `token.json` / 服务端下发 / 逐槽位覆盖。下述 ABI 代价仅作**反向说明**（若将来改回「运行时任意主题」：**32 参**公开构造器进 ABI（**P8**：U12 = 32 槽位，含 `text.disabled`），此后**每加一个语义色都是 breaking**）。口径来源 = `30-dev-plan.md` **§5.8 / §6.3.4**。

> **层一（多套生成 scheme + 运行时选择）的落地条目见 §3.1.2a（P9）**：`schemes: {light, dark, …}` + 生成器 `--schemes`；切换 = 换**已生成**的 scheme、**不重启进程**、**不进高频路径**；**不支持**运行时任意 `token.json` / 服务端下发。口径来源 = `30-dev-plan.md` §5.8（用户决策 #8）。

#### 3.2.3 高对比度与 M3 桥接（AR-63 / AR-65）

**(1) 高对比度三件事**（不新增 `LocalWDAppearance`）：

```kotlin
// 文件：…/foundation/theme/WDHighContrast.kt（只读工具，不引入新 local）
/**
 * 读系统「高对比度文字」开关（LR-03）。
 * 事实（21 §1 V3）：Compose 的 AccessibilityManager 接口只有 calculateRecommendedTimeoutMillis；
 * 框架方法名是 isHighContrastTextEnabled()（android-33 无 / android-36 有 ⇒ **API 34+**）。
 */
@Composable
public fun rememberWDHighContrast(): Boolean {
    val context = LocalContext.current
    val am = remember(context) { context.getSystemService(AccessibilityManager::class.java) }
    return remember(am) {
        Build.VERSION.SDK_INT >= WDApi.HIGH_CONTRAST_MIN && am?.isHighContrastTextEnabled() == true   // <34 恒 false
    }
    // 决策（LR-03）：**暂不订阅** addHighContrastTextStateChangeListener ——
    // 它需要 API 34+ 且订阅/退订要配对到生命周期；M0–M2 按「组合期读一次、**非响应式**」实现，
    // 该限制写进 KDoc/README，并登记为未闭合项（§9.1-V8）。M4 真机若验证到「开关变了但不重组」再改为可观察实现。
}
```

① 玻璃 → 不透明（走 `WDEffectsBudget.allowBlur = false` 路径）；② `border.hairline` → `border.hairline-strong`；③ 禁用态提高不透明度并补形状/字重差异。该值**作为参数**进 `WDGlass.resolve`（AR-63）。

**(1b) `WDAppearance` 完整字段表（LR-09：与 iOS **逐字同名同集合**，含 `contrast`；O-5 出口前冻结）**：

```kotlin
// 文件：…/foundation/theme/WDAppearance.kt（**值对象**，不是 local；不新增 LocalWDAppearance）
public enum class WDColorScheme { Light, Dark }             // ↔ iOS ColorScheme(.light/.dark)
public enum class WDContrast { Standard, Increased }        // ↔ iOS ColorSchemeContrast（CR-6：两端同列）

@Immutable
public class WDAppearance internal constructor(
    public val colorScheme: WDColorScheme,                  // 字段名与 iOS 逐字一致
    public val contrast: WDContrast,                        // = rememberWDHighContrast() 的强类型化
    public val reduceTransparency: Boolean,                 // 见下方关系说明
    public val differentiateWithoutColor: Boolean,          // Android：M0–M6 恒 false（无系统等价物，F28）
    public val reduceMotion: Boolean,                       // = rememberWDMotionScale() == 0f
) {
    override fun equals(other: Any?): Boolean = /* 5 字段 */
    override fun hashCode(): Int = /* 5 字段 */
    public companion object { public val Light: WDAppearance; public val Dark: WDAppearance }
}
```

| 字段 | iOS 来源 | Android 来源 | 关系 |
| --- | --- | --- | --- |
| `colorScheme` | `colorScheme` | `WDTheme(darkTheme)` | 同一事实，两个入口之一 |
| `contrast` | `colorSchemeContrast` | `rememberWDHighContrast()`（API 34+，<34 恒 `Standard`） | **CR-6**：两端同列，进 U10 输入 |
| `reduceTransparency` | `accessibilityReduceTransparency` | **`!WDEffectsBudget.allowBlur`** | **F16 的唯一开关**：Android 不新增 `LocalWDReduceTransparency`；`WDAppearance.reduceTransparency` 只是 `WDEffectsBudget.allowBlur` 的**只读镜像**（单一真源仍是 budget） |
| `differentiateWithoutColor` | `accessibilityDifferentiateWithoutColor` | M0–M6 恒 `false`（无系统等价物；登记 F28） | 语义槽位仍按 U9"不只靠颜色"执行 |
| `reduceMotion` | `accessibilityReduceMotion` | `rememberWDMotionScale() == 0f` | 与 **F30** 的"行为统一"一致 |

**(2) M3 桥接补齐 Typography + Shapes（AR-65，旧稿只桥 ColorScheme）**：

```kotlin
// 文件：…/foundation/theme/WDMaterialScheme.kt（**全库唯一允许 import m3 的文件**，G1 断言）
internal fun WDColors.toMaterialColorScheme(dark: Boolean): ColorScheme = /* 10 槽（现状 :60-84 的两函数搬入）；48 槽见 O-A2 */
internal fun WDTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = largeTitle, displayMedium = title1, displaySmall = title2,
    headlineLarge = title2, headlineMedium = title3, headlineSmall = headline,
    titleLarge = headline, titleMedium = subheadline, titleSmall = callout,
    bodyLarge = body, bodyMedium = callout, bodySmall = caption1,
    labelLarge = footnote, labelMedium = caption2, labelSmall = overline,
)
internal fun wdShapes(): Shapes = Shapes(
    extraSmall = RoundedCornerShape(WDRadius.xs), small = RoundedCornerShape(WDRadius.sm),
    medium = RoundedCornerShape(WDRadius.md), large = RoundedCornerShape(WDRadius.xl),
    extraLarge = RoundedCornerShape(WDRadius.xxl),
)
```

- 全部 `internal`（F19/U4：桥接**不得**出现在公开签名）；
- **G9 补充断言（LR-01）**：库内每个 `CompositionLocal` 的类型必须落在 G9 的类型白名单内（`LocalWDContentColor` 的类型 `Color` 属 `androidx/compose/ui/graphics` ✓；`LocalWDIcons` 的类型 `WDIconSet` 属 `io/github/wlunc/wisdom/` ✓）——把「禁止再引入 m3 类型」从文件级细化到 **local 类型级**；
- `WDTheme` 里三个值都 `remember(...)`（AR-45①）；
- **消费方形态测试（AR-65 ③）**：`WDTheme { Text("x") }` 的 `TextLayoutResult` 字号/行盒 == `WDType.body`（Robolectric 读 `onTextLayout`）。
- 38 个派生槽位"需要设计确认"→ 先做 10 槽，扩 48 槽列入 **O-A2**（M0/M1）。

### 3.3 密度、安全区、折叠屏、横竖屏、软键盘

#### 3.3.1 密度（AR-67 落地：可见内容单值键 44 + Android 布局盒 48）

```kotlin
public enum class WDLayoutDensity { Comfortable, Compact }

// WDLayout.rowHeight / cardPadding
public val rowHeight: Dp get() = when (density) {
    WDLayoutDensity.Comfortable -> WDSize.rowHeightComfortable     // **可见内容** 60（两端同值）
    WDLayoutDensity.Compact -> WDSize.rowHeightCompact             // **可见内容** 44（两端同值）；Android 布局盒 48 = 44 + 上下各 2dp 透明内边距（见下方 AR-67 补充解释）
}
public val cardPadding: Dp get() = when (density) {
    WDLayoutDensity.Comfortable -> WDSize.cardPaddingComfortable   // 16
    WDLayoutDensity.Compact -> WDSize.cardPaddingCompact           // 12
}
```

**契约新增句（AR-67）**："**Android 的列表行热区 = 行高；行高即热区，禁止用扩热区手段覆盖相邻行**"。这是 U8 双键方法论的自然延伸；**iOS 数值一字不改**。

> **AR-67 补充解释（案 B / P7；上面这句原文与它引用的两条断言值原样保留，本段是补充而非取代）**：**行高 = 布局盒（可见内容 44 + 上下各 2dp 透明内边距）；热区即该 48 布局盒，仍不与相邻行重叠**。
> - `WDLayout.rowHeight` 的语义 = **可见内容/视觉高度**（`compact` 44、`comfortable` 60，两端同值）；Android 的**布局盒**由 `Modifier.wdTouchTarget()`（AR-66）把内容盒扩到 **48** 并居中，**不新增令牌键**。
> - 上面引用的 `(compact,1) == 48dp ± 0.5`、`(compact,2) == 76dp ± 0.5` **原样成立**（"行高" = 布局盒）；**新增**"紧凑单行可见内容高度 == 44dp ± 0.5"（测内容区、不含内边距）。
> - **U/F 两条登记**：**U（必须统一）= 两端可见内容高度 44 ± 0.5**（进 `contracts/<component>.yaml` + 两端断言）；**F（各端自由，平台惯例）= 行距（Android 48 / iOS 44）** —— Android 纵向节奏比 iOS 疏 4dp，设计已接受。

#### 3.3.2 安全区（AR-68 insets 白名单）

| 角色 | 责任 |
| --- | --- |
| 安全区顶部 | 导航栏组件自身承担，内容不重复留白（`09-layout.md:140`） |
| 安全区底部 | Tab 栏 / 操作条 / Toast **各自承担，只留一次**（`:141`） |
| 页面底部留白 | `24 + 安全区`（`:142`） |
| **碰 insets 的组件白名单（AR-68，G10 断言）** | `WDNavigationBar` / `WDTabBar` / `WDToast` / `WDBottomSheet` / `WDActionSheet` / `WDAlert` |
| 其余 31 个组件 | **一律不得出现 `WindowInsets`**；容器型组件只暴露 `contentWindowInsets` 参数入口 |

#### 3.3.3 折叠屏 / 平板 / 横竖屏

库内**不引** `androidx.window` / `material3-window-size-class`（依赖白名单）；两列切换、铰链避让、`maxWidth` 由调用方负责（C9/C10 精神）；库负责的是 **`Saver`**（配置变化后 `WDBottomSheetState`/滚动位置保持，`09-layout.md:125`）。

#### 3.3.4 软键盘（AR-37 / AR-68）

| 场景 | 责任人 | 实现 |
| --- | --- | --- |
| 页面内容（含 `WDTextField`） | 调用方（`Modifier.imePadding()`） | C8 |
| **弹层组件** | **库**（显式例外） | `wdDialogImePadding()` = `WindowInsets.ime.union(navigationBars.only(Bottom))`，**用 `union` 去重，不得相加** |
| 底部操作条 | 随键盘上移、不叠加安全区 | `WDBottomSheet.footer` 只用 `wdDialogImePadding()` |

### 3.4 手势冲突

#### 3.4.1 嵌套滚动（AR-70）

```kotlin
// 文件：…/components/composites/WDBottomSheet/WDBottomSheetNestedScroll.kt（internal）
internal fun Modifier.wdBottomSheetNestedScroll(
    state: WDBottomSheetState, detents: List<WDBottomSheetDetent>, onDismissRequest: () -> Unit,
): Modifier = this.nestedScroll(object : NestedScrollConnection {
    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (source != NestedScrollSource.UserInput) return Offset.Zero
        return if (available.y > 0f && state.currentDetent != detents.first()) {
            state.dragBy(available.y)                     // internal（AR-70）
            Offset(0f, available.y)
        } else Offset.Zero
    }
    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity { /* 落档 or dismiss */ }
})
```

- `dragBy` / `snapToDetent` 必须 `internal`（不进 ABI，AR-70）；
- **`onPostFling` 的阈值边界必须有单测**（落档 vs dismiss 的分界，AR-70）；
- drag handle 用 `Modifier.draggable`（`rememberDraggableState`），**禁止 `pointerInput` 自造手势**（C13）。

#### 3.4.2 预测式返回（AR-71 沿用）

**不引 `activity-compose`**；`WDBottomSheet` 的 back 由 `Dialog` 窗口默认行为覆盖（`dismissOnBackPress = true`）→ `onDismissRequest`；断言"back 不重复触发"。**【待实测】**：M4 真机确认"展开态 back 是否先收档"（→ §9.1 V15）。

#### 3.4.3 系统手势避让（AR-72）

`Modifier.wdSystemGestureExclusion()`（= Compose 的 `Modifier.systemGestureExclusion()` 的库内入口）只施加在 drag handle 横条上，**G12 断言调用点 == 1**；横向组件在屏幕左右 16dp 边缘收紧命中区；禁止屏蔽 back 手势。

### 3.5 动画、动效与效果配额

#### 3.5.1 动画分类与降级（AR-56 沿用）

| 类型 | M0–M3 | M4–M6 |
| --- | --- | --- |
| 属性动画 | `animate*AsState` / `Animatable`；时长取 `WDMotion.duration*` | 同 |
| 转场 | `AnimatedVisibility` / `AnimatedContent` / `updateTransition` | 同 |
| 手势驱动 | `Animatable` + `snapTo/animateTo` | 同 |
| **共享元素** | **不用** | M6 后评估（实验 API + 跨端不可对齐）→ O-A9 |

**弹簧口径**（`08-decisions.md:30`，不改）：canonical = `response` + `dampingRatio`；`stiffness = μ·(2π/response)²`，μ 默认 1.0（246.74 / 503.65 / 223.72）；禁用 `massFactor`。

**减弱动态效果**（`06-accessibility.md:181-191` 七行逐条）：属性动画自动降级；自绘常驻动画经 `WDMotion`；`scale == 0f` 退化静态。

#### 3.5.2 `WDMotionScale` 唯一入口（AR-56 / AR-73）

```kotlin
// 文件：…/foundation/motion/WDMotionScale.kt
@JvmInline public value class WDMotionScale(public val value: Float) {      // AR-73：value class 上不标 @Immutable
    public companion object { public val Normal = WDMotionScale(1f); public val Off = WDMotionScale(0f) }
}
public val LocalWDMotionScale: ProvidableCompositionLocal<WDMotionScale> = staticCompositionLocalOf { WDMotionScale.Normal }

/** 唯一动画缩放入口：local × 系统（E7：MotionDurationScale 是 CoroutineContext.Element，可观察并触发重组）。 */
@Composable
public fun rememberWDMotionScale(): Float {
    val system = rememberCoroutineScope().coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    return LocalWDMotionScale.current.value * system
}

/** null = 必须画静态替代（禁止 `?: return`，AR-73 KDoc）。 */
@Composable
public fun rememberWDInfiniteSpec(durationMillis: Int): InfiniteRepeatableSpec<Float>? =
    if (rememberWDMotionScale() == 0f) null else infiniteRepeatable(tween(durationMillis), RepeatMode.Restart)
```

**规则**：属性动画路径**不得**读 `rememberWDMotionScale()`（会双重降级）；`LocalWDMotionScale` **只读不覆写系统**。

#### 3.5.3 效果配额：从文字变成 API（AR-74）

```kotlin
// 文件：…/foundation/theme/WDEffectsBudget.kt
@Immutable
public class WDEffectsBudget internal constructor(
    public val allowBlur: Boolean,
    public val blurBudget: Int,
    public val allowAmbientAnimation: Boolean,
) {
    override fun equals(other: Any?): Boolean = /* 3 字段 */
    override fun hashCode(): Int = /* 3 字段 */
    public companion object { public val Default: WDEffectsBudget = WDEffectsBudget(true, 1, true) }
}

// 文件：…/internal/WDEffectCounter.kt（内部计数，配额的可测形态）
public enum class WDEffectKind { BlurSurface, AmbientShimmer, AnimatedRing }
/** 超预算返回 false，调用方画静态替代。 */
@Composable internal fun rememberWDEffectSlot(kind: WDEffectKind): Boolean

// 文件：…/foundation/material/WDGlassCapabilities.kt（AR-66：能力位不能只看 SDK_INT）
@Immutable
public class WDGlassCapabilities internal constructor(
    public val supportsWindowBlur: Boolean,      // SDK_INT >= 31 && windowManager.isCrossWindowBlurEnabled
    public val blurAllowed: Boolean,             // WDEffectsBudget.allowBlur
    public val blurBudgetRemaining: Int,
) { override fun equals(other: Any?): Boolean = /* 3 字段 */; override fun hashCode(): Int = /* 3 字段 */ }

@Composable internal fun rememberWDGlassCapabilities(): WDGlassCapabilities   // LocalView + LocalWDEffectsBudget
```

**可测配额（替代旧稿的"≤6 / ≤1"文字）**：

| 效果 | 配额 | 单测 |
| --- | --- | --- |
| 增强玻璃面 | `blurBudget`（默认 1） | 第 2 个 → `WDGlassResolution.Opaque` |
| 骨架微光 | 同屏 6 | "同屏 7 个 `WDSkeleton` → 第 7 个走静态灰块" |
| 动画环 | 每屏 1 | 第 2 个 → 静态环 |
| 色晕 `wash` | 每屏 1、禁动画 | 代码扫描（一次绘制画 3 个 radial brush） |
| 阴影 | 列表项 `e0`/`e1`；`e3` 每屏 ≤1 | 令牌已出口（§3.1.3）+ 组件断言 |

**`WDGlassSpec`（LR-09：档位的静态描述，数据来自 `material.*` 令牌）与 `WDGlass.resolve`（U10 的输入输出）**：

```kotlin
// 文件：…/foundation/material/WDGlassSpec.kt
@Immutable
public class WDGlassSpec internal constructor(
    public val level: WDGlassLevel,
    public val fillAlphaLight: Float,        // ← material.{level}.fillLight 的 alpha
    public val fillAlphaDark: Float,         // ← material.{level}.fillDark 的 alpha
    public val hasTopHighlight: Boolean,     // 恒 true（"玻璃 = 模糊 + 半透明填充 + 顶部高光 + 外圈细线"，四条缺一不可）
    public val hasHairline: Boolean,         // 恒 true
) { override fun equals(other: Any?): Boolean = /* 5 字段 */; override fun hashCode(): Int = /* 5 字段 */ }

// 文件：…/foundation/material/WDGlass.kt
public enum class WDGlassLevel { UltraThin, Thin, Regular, Thick, Tinted, Sheen }
public enum class WDGlassResolution { Opaque, Glass, GlassStrong }   // ↔ iOS WDGlassResolution
public object WDGlass {
    /** 档位 → 静态规格（生成物 material.* 的只读视图）。 */
    public fun spec(level: WDGlassLevel): WDGlassSpec

    /**
     * U10 的输入 = {文字级别(必选), 外观(含 contrast), 系统能力, reduceTransparency(折进 budget), 效果预算} → 三选一；
     * 组件不得自己选档。
     * `level: WDGlassLevel`（六档）是 **Android 侧的额外输入**（`20 §2.4-2`：U10 的必选输入是文字级别；
     * 该"额外输入"的 F 编号待分配，见 §4.1.1-S2）。
     */
    @Composable public fun resolve(
        textLevel: WDTextLevel,             // ↔ iOS WDTextLevel(.primary/.secondary/.tertiary)；**U10 的必选输入**
        appearance: WDAppearance,           // 含 contrast（CR-6 / LR-03 / LR-09）
        capabilities: WDGlassCapabilities,
        budget: WDEffectsBudget,
        level: WDGlassLevel = WDGlassLevel.Regular,   // Android 额外输入（不写进契约的 U10 Inputs 句）
    ): WDGlassResolution
}

/** ↔ iOS WDTextLevel；U10 的必选输入（放 §3.5.3 与 §3.2.3 两处都可见）。 */
public enum class WDTextLevel { Primary, Secondary, Tertiary }
```

**`resolve` 的判定顺序（唯一实现点，可单测）**：① `budget.allowBlur == false` 或 `appearance.reduceTransparency` → `Opaque`；② `appearance.contrast == Increased` → `Opaque`（与 iOS 的高对比度第 1 条补偿一致）；③ `capabilities.supportsWindowBlur == false` → `Opaque`（minSdk 24 的多数设备走这条）；④ 否则 `GlassStrong`（`Thick`/`Tinted`/`Sheen` 档）或 `Glass`（其余档）。

### 3.6 无障碍

#### 3.6.1 语义槽位与角色（AR-49 沿用 §2.6.3）

产出 **`contracts/roles-android.yaml`**：Android `Role` 全部取值 ↔ iOS traits 对照，作为 A-1 的落地（`Role` 恰好 9 个取值 + 补全 `ValuePicker`/`Carousel` 的可用性）。

#### 3.6.2 焦点顺序（沿用）

视觉阅读顺序 = 焦点顺序；列表整行一个焦点；弹层焦点进入并限制在内、关闭后归还触发元素（库内 `wdFocusRestorer()` 收口一次 `@OptIn(ExperimentalComposeUiApi::class)`）；FAB/Toast 不抢焦点（`focusProperties { canFocus = false }`）但保留在语义树。

#### 3.6.3 热区：**自研 `layout` 修饰符（AR-66，零 m3 依赖）**

```kotlin
// 文件：…/foundation/accessibility/WDTouchTarget.kt
public fun Modifier.wdTouchTarget(minSize: Dp = WDSize.touchTargetMin): Modifier =
    this.layout { measurable, constraints ->
        val min = minSize.roundToPx()
        val p = measurable.measure(constraints)
        val w = maxOf(p.width, min).coerceAtMost(constraints.maxWidth)
        val h = maxOf(p.height, min).coerceAtMost(constraints.maxHeight)
        layout(w, h) { p.place((w - p.width) / 2, (h - p.height) / 2) }
    }
```

- **内层固定尺寸（视觉 32/44）也达标**（E2：`defaultMinSize` 做不到——它只改传给子节点的约束）；
- 不产生离屏层、不依赖 m3 → **使"全库 m3 import 计数 == 1"成为事实**（G1-③）；
- 位置：组件内部链的**最外层**（紧跟调用方 `modifier`）；
- 令牌双键：Android `WDSize.touchTargetMin = 48.dp`；**禁止**用它做视觉尺寸。

#### 3.6.4 `fontScale 2.0`（U7 沿用 + AR-62 双层断言）

`sp` 自动缩放 + `TextStyle.lineHeight`（总盒高）+ `LineHeightStyle.Mode.Minimum`；行盒语义 `max(设计值×缩放, 自然行高)`；**禁止** `Mode.Fixed` / `lineHeightMultiple` / `@ScaledMetric` / 固定高度包文字 / 正文 `maxLines = 1` / 组件内 provide 固定 `fontScale` 的 `LocalDensity`。断言分两层（§2.8.1 表）。

#### 3.6.5 触觉与播报：**可注入网关（AR-75 / B2）**

```kotlin
// 文件：…/foundation/motion/WDHaptics.kt
public interface WDHaptics {
    public fun press()
    public fun toggle()
    // threshold() / warning() 随 M4 批次加入（AR-75 ③：不过早固化 API）
}

public val LocalWDHaptics: ProvidableCompositionLocal<WDHaptics?> = staticCompositionLocalOf { null }

@Composable
public fun rememberWDHaptics(): WDHaptics {
    val override = LocalWDHaptics.current
    val view = LocalView.current
    return override ?: remember(view) { PlatformWDHaptics(view) }
}
```

> **对 AR-75 的一处默认值微调**：评审给的片段是 `staticCompositionLocalOf { error("provide 或使用 rememberWDHaptics()") }`。本稿把默认值改为 **`null`**，理由：`error(...)` 会让"未 provide 的预览/demo"在组合期直接崩溃，而"是否被注入"用可空类型表达更准确（`rememberWDHaptics()` 内部 `override ?: 平台实现`）。**API 形状（`interface` + `remember*()` + 可注入 local）完全按 AR-75**。

**LR-26 的两条随附要求（Leader 裁定，必须落）**：
1. **KDoc/README 写明语义**："`LocalWDHaptics`/`LocalWDAnnouncer` **未注入 ⇒ 平台实现（不是 no-op、不是无触觉）**；注入只用于测试捕获器或自定义实现"（同一句话两处：`WDHaptics.kt` 的 local KDoc + `android/README.md` 的"无障碍注入点"段）；
2. **两条单测**（`WDHapticsInjectionTest` / `WDAnnouncerInjectionTest`）：
   - 无 provider：`rememberWDHaptics()` 返回**平台实现**（非 null、非 no-op）且调用 `press()` 不崩；`rememberWDAnnouncer()` 同理；
   - 有 capturer：`CompositionLocalProvider(LocalWDHaptics provides capturer)` ⇒ 组件 `onClick` 后 capturer 的计数 == 1（`WDAnnouncer` 同）。

```kotlin
// 文件：…/foundation/accessibility/WDAnnouncer.kt
public interface WDAnnouncer {
    public fun announce(message: String, priority: WDAnnouncePriority = WDAnnouncePriority.Polite)
}
public val LocalWDAnnouncer: ProvidableCompositionLocal<WDAnnouncer?> = staticCompositionLocalOf { null }   // 同上的微调

@Composable public fun rememberWDAnnouncer(): WDAnnouncer { /* LocalView + AccessibilityManager，可被 override */ }

/** 顶层函数，不读 local（AR-75） */
public fun Modifier.wdLiveRegion(stateDescription: String): Modifier =
    this.semantics { liveRegion = LiveRegionMode.Polite; this.stateDescription = stateDescription }
```

**触觉常量映射表（按 API 分级 + 兜底，`pinned` 标"待真机核准"，AR-75 / `08-decisions.md:47`）**：

| 语义 | 常量（API 分级） | 兜底（低版本） |
| --- | --- | --- |
| `press()` | `HapticFeedbackConstants.TextHandleMove`（API 27+） | `LongPress`（API 1+） |
| `toggle()` | `TextHandleMove`（27+） | `LongPress` |
| `threshold()`（M4） | `LongPress` | `LongPress` |
| `warning()`（M4） | `Confirm`（API 30+）/ `Reject`（30+） | `LongPress` |

**规则**：① 组件在**组合期**取 `val haptics = rememberWDHaptics()`，在 `onClick` 里调 `haptics.press()`；② 测试用 `LocalWDHaptics`/`LocalWDAnnouncer` provide**捕获器**（U13 fixtures 断言）；③ M2 只公开 `press`/`toggle`；④ **iOS 侧需同构网关形态**（`00-ios-draft.md:819,1008-1011` 已是可注入 sink/协议）——F17 的"何时播报"fixtures 才能两端共享；⑤ 播报节流 = `WDAnnouncementThrottle`（纯逻辑，跨端共享 `announcement-cases.json`）。

**Android 侧的诚实标注**：`assertive` 播报在 Android 没有干净的公开 API（`announceForAccessibility` 走 `TYPE_ANNOUNCEMENT` = polite）→ `Assertive` 实现用 `AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED` 近似，**【待实测 V2】**（与触觉映射同批真机核准）。

#### 3.6.6 减少动画（无障碍视角）

"减弱动态效果"是**无障碍行为**（U9 的"行为必须统一"）：常驻动画停止、位移/缩放退化为淡入淡出、弹簧改线性（`06:181-191`）。实现入口 = §3.5.1/§3.5.2；**组件不得自行判断系统设置**（`06:193`），也**不得覆写**（INT-2）。

### 3.7 国际化

#### 3.7.1 L-B（库内零资源零文案，沿用）

`main/res/` 从 M0–M6 目标树**删除**（`07-summary.md:142` DIR-3 唯一口径）；文案与图标都来自调用方；`WDIconButton(contentDescription: String)`、`WDButton(text: String)` 由签名保证必填。

#### 3.7.2 `WDSemantics` 终稿（D5 / AR-84）

```kotlin
// 文件：…/foundation/accessibility/WDSemantics.kt
public object WDSemantics {
    /** 分隔符与词序由调用方传（库不知道任何语言习惯）。 */
    public fun join(separator: String, vararg parts: String): String =
        parts.filter { it.isNotEmpty() }.joinToString(separator)

    public fun positional(label: String, positionText: String, separator: String): String =
        join(separator, label, positionText)
    // AR-84/D5：删除 stateValue（恒等函数，无价值）
}

public fun Modifier.wdLabelledBy(label: String): Modifier = /* 只写不读 */
public fun Modifier.wdMergedRow(): Modifier = this.semantics(mergeDescendants = true) {}
```

**两端同批（D5）**：iOS 侧 `label(_:_:separator:)` 同形态；**iOS 初稿 `00-ios-draft.md:1000-1002` 当前内置全角逗号，属同一问题，必须同批改**；两端都**不得**有默认分隔符。返回类型差异（`String` vs `Text`）、参数顺序与 iOS 的 `[String]` 纯逻辑重载**一并登记在 F24 一行内**（`20 §3` 把旧 F34 并入 F24）。

#### 3.7.3 复数、性别、日期/数字、RTL

| 项 | 规则 |
| --- | --- |
| 复数/性别 | 库不处理（L-B 的推论）；调用方用 App 侧 `plurals`/格式化后传入；M0 语言清单 `zh-Hans` + `en`（U9） |
| 日期/数字 | 库内**禁止** `String.format`/`SimpleDateFormat`/`NumberFormat`/`DateTimeFormatter`；一律由调用方格式化后传入（`WDListRow.trailingText`、`WDDatePicker` 的显示串）；12/24 制与补零以平台 locale 为准（Q-I18N-3） |
| RTL 布局 | `start/end` 而非 `left/right`；禁 `absolute*`；`TextAlign.Start/End` |
| **RTL 图标镜像（AR-76 终稿）** | **以 `ImageVector.autoMirror` 为准**；`mirrorsInRTL` 是**语义标记，不是实现指令**。`WDIcon(name = …)` 只在 `LocalLayoutDirection == Rtl && name.mirrorsInRTL && !imageVector.autoMirror` 时兜底画镜像层（`graphicsLayer { scaleX = -1f }`）；`WDIcon(imageVector = …)` 由调用方负责注入 `autoMirror = true` 的向量。**禁止**无条件 `Modifier.scale(-1f)`（会与 `autoMirror` 双重镜像 = 看起来没镜像） |
| RTL 渐变 | **不镜像**（CSS 角度口径，**F40**）；契约里写成**显式断言**（AR-77） |
| 验证 | 预览 `locale = "ar"`（U9：`ar` 只作 RTL 验证语言） |

---

## 4. 与 iOS 的差异登记与"必须统一"清单

### 4.1 F21–F50（**唯一分配表的副本**，真源 = `20-ios-leader-review.md` §3 + §3-附记；LR-08 / R2A-02 / R3A-02）

**F1–F20 不动**（`07-summary.md:56-77` 的 F 系列是唯一真源）；**F21–F45 的编号与条目一字不动**；F46–F50 见 `20-ios-leader-review.md` **§3-附记**（两批补记，**只增不改**）。

> **治理规则（写进 `contracts/README.md` 表头，M0-5）**：⓪ **F21 起唯一登记处 = `20-ios-leader-review.md` §3（+附记）；本节表格是它的副本**（M0-5 起真源迁至 `contracts/README.md`）——副本只读、不得在此新增或改写编号，登记先改真源再同步副本；① **F21 起只有一处分配权**（研发 Leader / 架构师），两端修复任务按本表引用，**不得自行取名/取号**；② 新增差异 → 先在该表登记"下一个空号"再实现，一行必须是"两端形态两列"；③ 已分配号**只增不改**，条目内容变更走 CHANGELOG；④ 机械映射：**iOS 稿 F21–F29 号不变、条目不变**（仅把 F23 的密度附属项拆出为 **F44**）；Android 稿 F21→F30、F22→F31、F23→F32、F24+F31→F33、F25→F34、F26→F35、F27→F37、F28→F38、F29→F39、F30→F40、F32→F41、F33→F42、F34→（并入 F24）、F35→F43（Android 侧引用同步 = t11）。
> **Android 副本的同步状态（R2A-02 / R3A-02）**：F46/F47 两行已按 `20` §3-附记第 1 批追加（**已核对（t17）**）；**F48/F49/F50 三行**已按第 2 批补记追加；**S-1/S-2/S-3 已分别裁定为 F46/F47/F48**（见 §4.1.1）；本副本与唯一登记处**逐行一致**。本节即 `contracts/README.md` 的"F 系列两端形态"章节内容（**由架构师在 M0-5 落库；本轮不改仓库**）。

| # | 项 | iOS 形态（`02-ios-spec.md`） | Android 形态（本稿） | 必须统一的部分 / 判据 |
| --- | --- | --- | --- | --- |
| F21 | 弹层/presenter 形态 | `.wdSheet(...)` / `.wdActionSheet(...)` 修饰符挂在锚点视图（系统 `.sheet`/`.confirmationDialog`） | `WDBottomSheet` / `WDActionSheet` 可组合项**自呈现**（`Dialog` 窗口） | 可见性真源在调用方；焦点陷阱与归还；遮罩语义；`requiredWhen` 文案；**detents 默认档集合一致 = `{Half, Large}`** |
| F22 | 错误态读屏实现 | 无 error trait ⇒ `.accessibilityValue` + `.accessibilityHint` 组合 | `semantics { error(text) }` | 读屏**结果**一致："标签，值，错误" |
| F23 | 文本字段能力集 | `WDTextFieldAppearance`（variant/placeholder/prefix/accessory/helper/isSecure/submitLabel）+ `isEditable` | `variant` / `placeholder` / `supportingText` / `errorText` / `characterLimit` / `keyboardOptions` / `visualTransformation` / `readOnly` | 三件事两端都能表达：**计数 / 清除 / 显隐**；**标签必填两端一致**（LR-07 已改）。（**密度注入已拆出为 F44**） |
| F24 | 语义帮助函数形态（**含返回类型、参数顺序、iOS 的 `[String]` 纯逻辑重载**） | `join(_ parts: [Text], separator: Text) -> Text`；`positional(label:positionText:separator:)`；另有 `join([String], separator:)` **纯逻辑重载**（供同源 fixtures） | `WDSemantics.join(separator: String, vararg parts: String): String`；`positional(label: String, positionText: String, separator: String): String` | 语义等价 + 分隔符/位置措辞/语序**全部由调用方传** + 库内零标点（**G13**）。**登记的三处差异**：① 返回类型 `String` vs `Text`（旧 F34 **并入本行**）；② 参数顺序（Kotlin separator 在前 / Swift parts 在前）；③ 仅 iOS 有 `[String]` 重载 |
| F25 | 契约文件读取与跨仓自证机制 | 读 `contracts/dist/*.json`（`JSONDecoder`，零依赖） | 读同一份 JSON（`org.json` 或手写行解析，零依赖） | 同一源 + **解析失败必须 fail**（不得 skip） |
| F26 | 快照基线与金标设备（细化 F13） | 自研像素/感知哈希；玻璃类 `UIHostingController`+`drawHierarchy`、普通类 `ImageRenderer`；基线含 `{xcode,sdk,deviceType,runtime}` | 官方 screenshot 插件（debug 变体）+ Robolectric 语义树 | 六态矩阵的**态定义**一致；**禁止两端并排比截图**（D-4） |
| F27 | 系统控制的手势与几何 | `.swipeActions` 无阈值 API；系统把手 36×5、圆角 32 **不可定制**；关闭阈值（40% / 500pt/s）**不可断言** | `SwipeToDismissBox` 自实现吸附与阈值；把手几何**可定制**（令牌 `size.sheet.handle.{width,height}` + `size.sheet.handle-top-offset`）、顶部圆角 `size.sheet.corner-radius = 32` | **行为**一致："未达阈值不删除 + 提供自定义无障碍操作"；方向契约见 §3.4；iOS 的几何/阈值验收标"不适用（系统控制）" |
| F28 | 系统级无障碍开关可得性（细化 F16） | `reduceTransparency` / `reduceMotion` / `differentiateWithoutColor` / `colorSchemeContrast` **齐全** | **无 `reduceTransparency` 等价物**（→ 折进 `WDEffectsBudget.allowBlur`）；**有** `AccessibilityManager.isHighContrastTextEnabled()`（**API 34+，<34 恒 false**，LR-03） | U10 的**输入集合**与输出语义一致（`WDAppearance` 五字段，见 §3.2.3）；Android 用常量 `false` 兜底 |
| F29 | "关闭意图"回调可达性 | 只能交付"点了关闭按钮"（`onCloseButtonTap`）与"系统已完成关闭"（`onDismiss`）；`onDismissAttempt` 已删 | `onDismissRequest` **可交付意图**（遮罩点击 / 系统返回 / 下拖越阈值） | "可见性唯一真源在调用方" + "关闭后焦点归还触发元素" |
| F30 | 动效降级入口 | `accessibilityReduceMotion` | `MotionDurationScale`（`CoroutineContext.Element`）+ `Settings.Global.ANIMATOR_DURATION_SCALE`；**不存在 `LocalMotionDurationScale`**（`11` §0.1 E7） | **行为**一致：常驻动画停止、位移/缩放退化淡入淡出、弹簧改线性 |
| F31 | 时长/弹簧数值类型 | `Double` 秒 | `Int` 毫秒（换算在生成器） | 令牌名与 canonical 参数名一致（U11） |
| F32 | 默认参数与 ABI 兼容判定 | 默认值不进 ABI | Kotlin `$default` **进 ABI**；**给既有 public 函数加带默认值的参数也是二进制不兼容**（AR-58） | 默认值只能靠契约锁（U2） |
| F33 | 类型构造能力与可见性 | `struct` 属性可见性控制，无 `internal init` 等价物 | 生成类 + `WDLayout`/`WDBottomSheetState` = `internal constructor`（AR-77）；**`WDGradientSpec` 例外：保留公开构造器**（AR §4.2-4 / D4，2 参数值类型） | 共同目标：D-7"不承诺跨令牌版本的构造器兼容"；`apiCheck` 能看见构造器形状 |
| F34 | 容器语义近似 | `.accessibilityElement(children:)` | `Role` 恰好 9 值、**无容器角色** ⇒ `isTraversalGroup + collectionInfo` / `paneTitle + dialog()` 近似（A-1） | 读屏结果一致（行为统一、API 自由） |
| F35 | 软键盘避让责任 | 键盘 safe area 自动；库不做键盘避让（N8），`WDBottomSheet` 例外 | `WindowInsets.ime.union(navigationBars.only(Bottom))`（弹层内部负责，§3.3.4）；页面级归调用方 | 行为一致：聚焦字段可见、footer 贴键盘、**不叠加安全区** |
| **F36** | **安全区责任划分**（iOS：组件不消费、调用方 `.safeAreaInset` 安装；Android：6 组件白名单、组件自身承担） | 组件**不消费**安全区；四类浮层例外 + `WDNavigationBar`/`WDTabBar` 由调用方 `.safeAreaInset` **安装** | **6 组件白名单**（`WDNavigationBar`/`WDTabBar`/`WDToast`/`WDBottomSheet`/`WDActionSheet`/`WDAlert`）**各自承担**（G10 断言），其余 31 个组件零 `WindowInsets` | **契约句（两端同款，IOS-12）**："安全区只留一次；由谁施加（系统 `safeAreaInset` vs 组件自身）属平台惯例，**两端都不得重复留白**"；与 `09-layout.md:140-141` 的字面不冲突（设计说"各自承担"= 不得重复） |
| F37 | 状态文案类型 | `loadingAccessibilityText: Text?` | `loadingLabel: String?`（null → debug 断言，API-8） | `requiredWhen: loading` 进契约 |
| F38 | sheet/detent 表达与默认档（**含 `initialDetent` 位置差异**） | `.presentationDetents` + `initialDetent = .half`（**修饰符形参**）；`WDBottomSheetDetents` 是 iOS 的 `{.all, .fixed}` 联合类型 | `detents: WDBottomSheetDetents = All` + `rememberWDBottomSheetState(initialDetent = Half)`（**组件只留 `state` 一份真源**，AR-35）；**`WDBottomSheetDetents` 在 Android 同样存在**（`sealed interface`：`All`/`Fixed`） | detent 类型名/case 名/默认档集合一致（U1/U2，**`20 §2.4-1` 前缀统一 `WDBottomSheet*`**）；数值 0.5/0.92 同源令牌；**`WDBottomSheetState` + `Saver` 是 Android 净增益**（恢复能力差异属 **F5**，验收不得要求 iOS 也能恢复） |
| F39 | 触觉常量与执行 | `UIImpactFeedbackGenerator(.light/.rigid/.medium/.warning)` | `HapticFeedbackConstants` 按 API 分级 + 兜底（§3.6.5） | 语义 → 函数名一致；映射表标"待真机核准" |
| F40 | 渐变几何与 RTL 不镜像 | `WDTokenTypes.swift:85-99`（CSS 角度口径） | `WDGradient.kt:27`：`angleDegrees − 90°` | **RTL 不镜像**写成**显式断言**（AR-77；两端同款） |
| F41 | 日期/时间选择载体 | `DatePicker` + `.datePickerStyle` | `android.app.DatePickerDialog` 包装（零文案唯一可行，minSdk 24 可用） | 名字统一（`WDDatePicker`）；行为统一只有"选中值类型与时区语义"（epoch millis + **调用方格式化**） |
| F42 | 行盒/尺寸断言的测量手段与容差单位 | `UIHostingController` + `sizeThatFits`（点值断言，**tol 0.5pt**） | Robolectric JVM 字体度量 + `GetTextLayoutResult` 动作（**行为断言，tol 1px**） | 断言**语义**一致（`max(设计×缩放, natural)`、不裁切）；**禁止跨端比数值** |
| F43 | 阴影精度 | 双层 shadow 精确映射 | **近似映射**（`Modifier.shadow` 单 elevation，5 行映射表见 §3.1.3） | 令牌数据完整（`WDElevationSpec.layers` 保留原值）；**禁止**为复刻多层阴影引离屏层 |
| **F44** | **密度注入与行高下限机制**（行高公式本身属 **U** 系列） | `@Environment(\.wdDensity)` 注入；`行高 = max(密度档最小行高, 槽位派生行高)` | `density: WDLayoutDensity? = null`（null = 跟随 `LocalWDLayout`）；行高公式**同一份**（§2.2③ 的 4 格矩阵） | **行高公式是 U 系列**（两端同值同公式）；**注入形态是 F44**（环境值 vs 形参）；`WDListRow`/`WDListSection`/`WDCard` 是消费方 |
| **F45** | **状态视觉值实现机制** | `state.*` 令牌（hover 98% / pressed 96% / focus 环 3pt+32% / disabled 40%）直接作为视觉值与令牌 | `press-overlay-alpha`（`WDComponent`）+ 自绘 `drawWithContent` 叠加；focus 环自绘；disabled 走不透明度+形状 | **必须统一的是"状态优先级与可辨识性"**（U6 + `06-accessibility.md:28` 的"不只靠颜色"）；**实现机制**各端自由；两端**禁用态对比度**的 M0-1 决策见 §3.1.2 第 17 行 |
| **F46** | **交互角色/动作标签形参的能力不对称**（来源：S-1；**已裁定** = `20 §3-附记`；**已核对（t17）**） | 真 `Button` 自带角色与动作标签：**无** `role`/`onClickLabel` 形参（需要时由调用方用 `.accessibilityAddTraits` / `.accessibilityAction(named:)` 施加） | 暴露 `role: Role? = Role.Button` 与 `onClickLabel: String? = null` 两个形参 | 读屏**结果**一致（角色语义 + 动作标签文案由调用方给）；**形参面自由 —— 不得为对称给 iOS 加这两个形参**（`07 §1.4` 反模式 1 的同源推理） |
| **F47** | **玻璃档位作为"额外输入"**（来源：S-2；**已裁定** = `20 §3-附记`；**已核对（t17）**） | `WDGlass.resolve(textLevel:appearance:capabilities:budget:)` **只接文字级别** `WDTextLevel`，无档位形参 | `WDGlassLevel` 六档作为**额外输入**（不影响 U10 的必选输入集合） | U10 的 canonical 输入集合（`textLevel` + `appearance` + `capabilities` + `effectsBudget`）与输出 `{opaque, glass, glassStrong}` **不变**；额外档位**不得改变输出语义**（同一输入元组两端同输出） |
| **F48**（第 2 批补记 · t17 裁定） | **槽位 / 内容参数的实现分类**（来源：t15 §8.1 的 D09/D18 + android-dev 的 S-3；与 D02/D04/D05/D06 同批裁定） | 同一内容名可为 `@ViewBuilder` 泛型槽、值参数（`subtitle: Text?`、`valueText: Text?`、`placeholder: Text?`）或值数组（`options: [WDOption]`、`items: [WDSegment<ID>]`） | 同一内容名可为 `@Composable` content lambda 槽位或平铺参数 | **U3 统一的是「名字 + 同一组件的语义（同一名字 = 同一内容 + 同一读屏结果）」；API 形态（槽位/参数/值数组）各端自由**。判据：`contracts/README.md` 的 U3 段只锁 21 名清单 + `{组件 → 名称\|无}` 37 行表（名字逐字相同）；分类差异登记本行 |
| **F49**（第 2 批补记 · t17 裁定） | **`WDToolbar` 的 leading/trailing 表达**（来源：t15 §8.1 D14） | 走系统 `ToolbarItem` 语义表达 leading/trailing（无显式形参） | 显式 `leading`/`trailing` 槽位 | 名称集合按 U3 对齐（`items` + `leading` + `trailing`）；形态自由 |
| **F50**（第 2 批补记 · t17 裁定） | **`WDPullToRefresh` 的刷新动作与视觉载体**（来源：t15 §8.1 D17；已由 O-7 裁决覆盖） | `.refreshable` + `onRefresh: () async -> Void`（系统视觉） | 自实现下拉指示器（无 async 动作） | 行为一致："到阈值触发一次 + 进行中不可重复触发 + 到阈值一次触觉"；视觉载体自由（O-7） |
#### 4.1.1 编号表存疑项（S-1/S-2/S-3 **均已裁定**）

| # | 存疑项 | 现状 | 裁定 / 处置 |
| --- | --- | --- | --- |
| **S-1** | **交互角色/动作标签形参的能力不对称**（Android `WDButton(role: Role? = Role.Button, onClickLabel: String? = null)` vs iOS 无对应形参——真 `Button` 自带角色） | 第 1 轮 §3 表**漏了这一行**（t7 §5.1 写了"须登记 F 系列"但没分配号） | **已裁定：F46**（`20 §3-附记`）——本条已升格为 §4.1 的 **F46 行**；判据 = 读屏结果一致，**不得为对称给 iOS 加这两个形参** |
| **S-2** | **玻璃档位的"额外输入"参数**（Android `WDGlassLevel` 六档 vs U10 的必选输入 `WDTextLevel`） | `20 §2.4-2` 要求"Android 把'玻璃档位'降为额外输入 + **登记 F 行** + 改名" | **已裁定：F47**（`20 §3-附记`）——本条已升格为 §4.1 的 **F47 行**；U10 的必选输入集合与输出语义不变，六档不得改变输出语义（同一输入元组两端同输出） |
| **S-3**（t18 新增） | **U3 词表里"槽位"的量纲**：进 21 名是否等于"必须是 `@Composable` 槽位"？ | R2A-01 已裁定这 5 个名字"**改记槽位**、与 iOS 同形"；但它们在两端的**既有签名**里都是**带类型参数** | **已裁定：F48**（`25` §3 的 S-3 裁定 = `20` §3-附记第 2 批补记）：**U3 统一「名字 + 语义」，API 形态归 F48、各端自由**；本稿默认执行（`subtitle`/`valueText`/`placeholder`/`options` 保持参数、`control` 保持 `@Composable` 槽位）**成立**，"4 处签名变更"**作废** |

> **两条两端同步项（已定，非存疑）**：
> ① **U3 槽位词表 = 21 名，两端同源**：iOS 已在 `02-ios-spec.md:835-845`（§2.10）定稿 21 名（IOS-02 采纳），Android 的 §2.4 词表与 37 行表**已按同一份 21 名词表**落定（R2A-01）；**不存在"两种选择"**——若任一端再回退到 16 名，U3 即分叉，须走"改 U3"流程。
> ② **detent 改名**：iOS 侧由 **IOS-03** 改前缀为 `WDBottomSheet*`（本稿 Android 侧已按 `20 §2.4-1` 完成）。

### 4.2 必须统一（U 系列：U1–U14 沿用 + U15–U17 进契约）

| # | 项 | Android 落点 | 机器检查 |
| --- | --- | --- | --- |
| U1 | 组件/类型/层目录/组件目录名 | **`WDBottomSheet` 家族**（AR-86 改名）；`WDButton` 等 | 目录扫描 + 契约 |
| U2 | 枚举名 + case 名 + 默认值 | `WDBottomSheetDetent` 等；默认值进 `contracts/<component>.yaml` 的 `params[].default` | JVM 单测 + 契约 |
| U3 | 槽位语义名 | 21 名共享槽位词表（与 iOS §2.10 逐字一致）；无槽位显式写"无"；{组件 → 槽位名\|无} 37 行见表 §2.4 | 契约 + 人审 + `WdSlotVocabulary` 断言 |
| U4 | 令牌名与取值 | 生成物六类 object | `build.js --check` |
| U5 | 行盒语义 | `Mode.Minimum` + 绝对值 `lineHeight`；**布局层断言**（AR-62） | `WDLineBoxTest` |
| U6 | 交互状态优先级 | §2.6.1 三条派生 | 同一串输入 → 同一可见状态 |
| U7 | 动态字体降级顺序 | `06:133-141` 五条；阈值来自 `WDComponent.layoutBreakFontScale` | 截图 + 单测 |
| U8 | 触控目标与"热区≠视觉尺寸" | `48dp` 单常量（**触控双键**）+ **行高：可见内容 44 单值键 / Android 布局盒 48**（AR-67 **补充解释**，P7） | 令牌 `--check` + 热区断言（可见内容 44 ± 0.5 / 布局盒 48 ± 0.5） |
| U9 | 无障碍**行为** | §2.6.3 + §3.6 | 语义树断言 + fixtures |
| U10 | 玻璃档位决策输入输出 | `WDGlass.resolve(textLevel, appearance, capabilities, budget) → WDGlassResolution`（**必选输入含 `WDTextLevel`**；`WDGlassLevel` 六档是 Android 额外输入） | 契约 + 单测（`reduceTransparency`/`contrast=Increased` → `Opaque`） |
| U11 | 动效令牌 + 弹簧 canonical | `WDMotion`（非 const）+ μ 公式 | `build.js` + `WDMotionTest` |
| U12 | 语义色 **32** 槽位（含 **`text.disabled`**）+ 浅深成对 | `WDColors`（**32** 个 `public val`）；`WDColorSlot` 槽位名清单 = **32** | 生成物 + **G9**（公开类型白名单）+ **槽位计数断言（`WDColorSlot` 计数 == 32）** |
| U13 | 契约文件里的每个断言项 | 见 §1.5.4 | 三档强度（`07-summary.md:89-93`） |
| U14 | 生成物溯源格式 | banner + `WDTokensVersion.kt` | **G7**（解析失败 fail） |
| **U15** | "加载态容器宽度不变" | `onSizeChanged` 两态宽差 ≤ 0.5dp，**fontScale 2.0 也跑一遍**（AR-78） | Robolectric 断言 |
| **U16** | "`loading == true` 期间 action 计数 == 0" | 注入 `MutableInteractionSource` 连按 | Robolectric 断言 |
| **U17** | "`components/**` 不得引用静态令牌入口" | **G1 的规则列表 + G2 的符号白名单**（两端各自命令，AR-78） | 两侧各一条检查命令 |

> **AR-77 的两条登记要求已落**：**F33** 的"手写类也要 `internal constructor`"进 §2.9.3；**F40** 的"渐变 RTL 不镜像"进 §3.7.3 的**显式断言**（不再只写在两端形态列里）。

---

## 5. 与 `08-decisions.md` 8 条硬约束的逐条落地

| # | 硬约束 | 本稿落点 | 状态 |
| --- | --- | --- | --- |
| 1 | 令牌 schema 冻结窗口只有一次 | §3.1.2 的 **21 行清单一次改完**（含 **D6 键名 `dampingFraction→dampingRatio`**、`elevation.*` 出口、`WDComponent` 12 键、**行高：可见内容 44 单值键 + Android 布局盒 48**、`press-overlay-alpha`、`layoutBreakFontScale`）+ **§3.1.2a 的 `schemes` 维度（第三个必含维度，P9）** | ✅ 采纳 AR §4.1-1 的"执行不完整"指正；P7/P8/P9 已回写 |
| 2 | 行高：绝对值真源 + `letterSpacing` 槽位 + 派生只读 ratio + 行盒 `max(设计×缩放, 自然)` | §3.1.3（`WDTextStyle` 4 参 + 派生 getter）、§2.8.1（**令牌层 + 布局层双层断言**） | ✅ 采纳 AR-62；D2 请求改契约措辞 |
| 3 | 弹簧 `response` + `dampingRatio`，`stiffness = μ·(2π/R)²`，μ=1.0，禁 `massFactor` | §3.1.3 + `WDMotionTest` 公式断言（246.74 / 503.65 / 223.72） | ✅ 与 AR §4.1-3 的复算一致 |
| 4 | 图标：只统一语义名 + `mirrorsInRTL`；调用方注入 `ImageVector`；库内零资源 | §2.3（`WDIcon` 槽位/注入 + **缺图保留占位尺寸**）、§3.7.3（**`autoMirror` 为准**） | ✅ 采纳 AR-41 / AR-76 |
| 5 | 文案 L-B；读屏标签调用方给；库只提供拼接帮助函数 | §3.7.1–3.7.2（`WDSemantics.join/positional`，默认分隔符不存在，`stateValue` 已删） | ✅ 采纳 D5/AR-84 |
| 6 | Android 玻璃：降级为默认、增强可选；`Modifier.blur` **不是**背景模糊 | §3.5.3（`WDGlassCapabilities`：`SDK_INT >= 31 && isCrossWindowBlurEnabled`）、§3.5.5 禁则；全文 `Modifier.blur` 只出现在禁则 | ✅ 采纳 AR-66/AR-85；`android/README.md:22` 改写进 M0（§7） |
| 7 | iOS 门禁命令 | 不适用（Android 门禁 = §1.5）；**不含** `swift build/test` | ✅ |
| 8 | `apiDump` 与生成物同提交，`apiCheck` 由红转绿 | §7 的 M0 六步（含 `WDGradientKt`/`WDThemeKt` 门面类改名 + `toTextStyle` 迁 `WDTextMetrics.kt` 的 ABI 所有者变化） | ✅ 采纳 AR §3.1/AR-21 |

**另附 AR §4.2 的 5 条不一致清单的处置**：① U8 行高口径未定 → **已按案 B 定案**（AR-67 **补充解释**，P7：**可见内容 44 单值键（两端同值）+ Android 布局盒 48（含上下各 2dp 透明内边距）**，原两条断言保留 + 新增"可见内容 44 ± 0.5"）；② U8/F14 图标镜像机制 → **已改**（AR-76）；③ U4 门禁仅黑名单 + R3 白名单矛盾 → **已改**（G1-③/G3/G9 + 自研触控）；④ U12 漏登记 `WDGradientSpec` 构造能力不对称 → **已并入 F33（类型构造能力与可见性），并保留公开构造器**；⑤ U1 违例 `WDSheet` → **已改名**（AR-86）。

---

## 6. 逐条回应 `11-android-review.md`（AR-01 – AR-86）

### 6.1 总览与台账

| 处置 | 条数 | 说明 |
| --- | --- | --- |
| **全部采纳**（照评审的替代方案/加约束落地） | 84 | 含 16 条"反驳"的签名级替代方案、32 条"加约束"的可检查项、38 条"接受"（其中 AR-12/AR-19/AR-21/AR-24/AR-25/AR-31/AR-55/AR-78 等接受了附加条款） |
| **采纳 + 一处显式微调** | 2 | **AR-75**（`LocalWDHaptics`/`LocalWDAnnouncer` 的默认值由 `error(...)` 改为 `null`，API 形状不变）与 **AR-74**（配额 API 保留，阴影落地给"近似映射 + **F43** 登记"的诚实结论） |
| 未采纳 | 0 | 无 |

**与旧稿的关系**：旧稿 `10-android-draft.md` 中被改写的地方见 §0.3 的 Δ1–Δ20；旧稿的 `P1–P8` 由评审 §6.1 裁决，本稿按裁决执行（见 §8.1）。

### 6.2 28 条 blocker/high 的"改后签名 / 命令 / 断言"（评审的通过口径）

| AR | 严重度 | 改后形态（本稿落点） |
| --- | --- | --- |
| **AR-03** | blocker | §1.1.3 G1：m3 import 计数断言 `m3Hits.distinct() == listOf("…/WDMaterialScheme.kt")`；三条规则各自持有 `allowlist` |
| **AR-04** | high | §1.1.3 `DepRule(fromPath, forbiddenImport, allowlist)` 骨架 + `WISDOM_SRC_ROOTS = listOf("src/main/kotlin","src/debug/kotlin")` + 剥 `//` 注释 + `noPackageCycle()` 真实函数 + 1 个反例单测 |
| **AR-07** | high | §1.5.3 G9 `wisdomCheckPublicTypes`：`api/wisdom-ui.api` 类型引用白名单（`io/github/wlunc/wisdom/`、`androidx/compose/{ui,foundation,runtime,animation}/`、`kotlin/`、`java/`） |
| **AR-10** | high | §1.2.2：`android.experimental.enableScreenshotTest=true` + `sourceSets["screenshotTest"].kotlin.srcDir("src/debug/kotlin")` + V14 记任务名 |
| **AR-14** | high | §1.2.3：变体断言（release == 0 且 debug > 0）+ `:demo:assembleRelease` + `-printseeds` 存档 + consumer-rules 说明 |
| **AR-15** | high | §1.2.4：`./gradlew --write-verification-metadata sha256 help` + `robolectric.dependency.dir` 预热 + `-g` 仅本地 |
| **AR-17** | high | §1.3.2：`.editorconfig` 3 行（无 `ij_kotlin_imports_layout`）+ PascalCase 工厂改名 |
| **AR-20** | blocker | §1.5.2：`wisdomGate`（14 个依赖 + ktlint/detekt/lintRelease）+ CI 两行（`wisdomGate` + `apiDump && git diff --exit-code`）；单测统一 `testDebugUnitTest` |
| **AR-22** | high | §1.5.3 G4（`tools/check-deprecated.sh` + `tools/deprecated-baseline.txt`）+ G9 + G8 拆两义 |
| **AR-29** | high | §2.2① `WDButton(…, onClickLabel: String? = null, role: Role? = Role.Button, …)` + `clickable(…, onClickLabel, role, …)` |
| **AR-35** | blocker | §2.2④ `WDBottomSheet(…, state: WDBottomSheetState = rememberWDBottomSheetState(detents.first()))`，**删**组件上的 `initialDetent`/`skipPartiallyExpanded` |
| **AR-36** | high | §2.2④ `WDBottomSheetState.saver(...)`：`restore` 未知版本/未知 detent → `null`（不抛）+ 三条断言 |
| **AR-37** | high | §2.2④/§3.3.4 `WindowInsets.ime.union(navigationBars.only(Bottom))` |
| **AR-38** | high | §2.3 `WDIcon(imageVector, contentDescription, modifier, tint, size)` + `WDDatePicker` 走平台 `DatePickerDialog` + `progressBarRangeInfo`（**受控值名 `date` / `value` 见 §2.2/§2.3 的 C-15 标注**） |
| **AR-44** | high | §2.5 `WDTheme(8 参)`：补 `icons`/`motionScale`；不引 `LocalWDAppearance` |
| **AR-45** | blocker | §2.5 `WDLayout` 值等 + `Comfortable/Compact` 单例 + `ColorScheme`/`Typography`/`Shapes` 全部 `remember` + G11 + 重组验收 |
| **AR-49** | high | §2.6.3 `loadingLabel?.let { stateDescription = it }`；**删手写 `disabled()`**（E4 收口）；Role 表补全 + `contracts/roles-android.yaml` |
| **AR-57** | high | §2.9.1 全量 `@Immutable`/`@Stable` 清单 + G5（`$stable` 存在性）+ G6（metrics 真断言） |
| **AR-58** | high | §2.9.2 + §1.5.4-F：`$default` 行数变化 = 二进制不兼容 |
| **AR-61** | blocker | §3.1.3 `WDComponent` **12 键** + 归属规则（交互时长 → `WDMotion`；组件私有节奏 → `WDComponent`） |
| **AR-62** | high | §2.8.1 双层断言：令牌层 + `WDLineBoxTest`（布局层，读 `TextLayoutResult`）+ fixtures 记 natural；D2 请求改 U5 措辞 |
| **AR-65** | high | §3.2.3 桥 `Typography` + `Shapes`（全 `internal`）+ `remember(...)` + 消费方形态测试 |
| **AR-66** | blocker | §3.6.3 自研 `layout` 触控修饰符 + §3.5.3 `WDGlassCapabilities`（`isCrossWindowBlurEnabled`） |
| **AR-67** | blocker | §3.1.2 行高（**可见内容 44 单值键，两端同值**）+ §3.3.1 契约句"行高即热区" + §2.2③ 4 格矩阵 —— **补充说明（非取代；P7 / 案 B）**：行高 = 布局盒（可见内容 44 + 上下各 2dp 透明内边距）；原两条断言 `(compact,1)==48±0.5`、`(compact,2)==76±0.5` **保留**，新增"可见内容 44±0.5" |
| **AR-75** | blocker | §3.6.5 `WDHaptics`/`WDAnnouncer` 接口 + `rememberWDHaptics()`/`rememberWDAnnouncer()` + 可注入 local（默认值微调为 `null`）+ 常量分级表 |
| **AR-76** | high | §3.7.3 以 `ImageVector.autoMirror` 为准，库内只兜底；`mirrorsInRTL` = 语义标记 |
| **AR-81** | high | §2.2① 的"语义契约（可断言）"表（每条带**前置条件**）+ §7 自证表加前置条件列 + `runOnIdle`/`waitForIdle` 写法 |
| **AR-86** | high | §2.1 改名清单（`WDBottomSheet` 家族），M2 前完成 |

### 6.3 全量逐条回应表（AR-01 – AR-86）

> "处置"列 = 本稿的落地方式；"落点"列 = 本稿的对应章节。

| AR | 裁决 | 处置 | 落点 |
| --- | --- | --- | --- |
| AR-01 | 接受 | 追加可测判据"全库 m3 import 计数 == 1"（已升格为 G1 的一部分，比四条拆分判据更早报警） | §1.1.1 / §1.5.3 |
| AR-02 | 接受（无动作） | 层包 + 四逻辑层 + `internal` 层沿用 | §1.1.2 |
| AR-03 | 加约束（blocker） | R3 改为"全库 m3 计数 == 1 且必须在 `WDMaterialScheme.kt`"；触控改自研；三条规则各自 `allowlist` | §1.1.3 |
| AR-04 | 反驳 | 换可编译骨架：`DepRule` + 双扫描根 + 剥注释 + 真实环检测 + 反例单测 | §1.1.3 |
| AR-05 | 加约束 | R4 白名单改**符号级**（只许 `WDSize/WDRadius/WDSpacing/WDComponent/WDMotion/WDType` 成员） | §1.5.3 G2 |
| AR-06 | 加约束 | 全局改名 **G1–G13 / L1–L8 / C1–C15**（消除 R 系列三名混用；G13 见 LR-08） | §0.2 / §1.5.3 / §2.8 |
| AR-07 | 反驳 | 新增 **G9** 正向白名单（`ignoredPackages + nonPublicMarkers` 的漏记风险由它兜住） | §1.5.3 |
| AR-08 | 加约束 | 文档不留 `<M0 锁定>`：首次接入当日锁定并写回 catalog；注明 `gradlePluginPortal()` 已在 `settings.gradle.kts:11` | §1.2.1 |
| AR-09 | 接受 | 版本参数化两条验收（带 `-P` 与不带 `-P` 各贴输出） | §1.2.1 |
| AR-10 | 反驳 | `enableScreenshotTest=true` + `sourceSets` 挂载（预览只写一份）+ M1 首日取任务名（V14） | §1.2.2 |
| AR-11 | 接受 | 新增 G3：`src/main` 不得出现 `@Preview`/`ui-tooling`；screenshot 只跑 debug | §1.5.3 / §1.2.2 |
| AR-12 | 接受 | 升格为 M0 出口**数字要求**：`:demo` 建起后必须给 E2 与 m3 dex 占比；此前 U4 收益一律标"未量化" | §1.2.3 / §7 |
| AR-13 | 加约束 | 拆分判据可测化：① dex 占比 > 50% 且 E2 > 400KB；② 真实消费方 issue；删掉不可测的"消费方明确要求" | §1.2.3 |
| AR-14 | 加约束 | G8 拆成"变体断言（含正例）"+"`:demo` R8 冒烟（`-printseeds` 存档）"；`consumer-rules.pro` 保持空 + README 说明 | §1.2.3 |
| AR-15 | 加约束 | `--write-verification-metadata sha256 help` + Robolectric `android-all` 预热（`robolectric.dependency.dir`）+ `-g` 只作本地排查 | §1.2.4 |
| AR-16 | 接受 | 重叠规则指定唯一所有者（`ModifierParameter` 归 Compose lint；ktlint compose 规则重叠则关掉） | §1.3.1 |
| AR-17 | 反驳 | `.editorconfig` 精简为 3 行（**删 `ij_kotlin_imports_layout`**）+ PascalCase 工厂改名（`saver(...)`） | §1.3.2 |
| AR-18 | 加约束 | "一文件一主类型 / ≤400 行"**仅适用手写文件**；`foundation/generated/**` 豁免 | §1.3.3 |
| AR-19 | 接受 | PR 模板第 3 段必须贴 `apiDump` 的**类型级摘要**；"ABI 无变化"须由 `git diff --exit-code` 背书 | §1.4 |
| AR-20 | 反驳（blocker） | 六条命令合并为 **`wisdomGate`**；单测统一 `testDebugUnitTest`；第 ④ 条注释换成 **G6 真断言**（文件缺失即 fail） | §1.5.2 / §1.5.3 |
| AR-21 | 接受 | 步 3 出口判据补"包名重写对拍"；第 7 步补 `versions.md`/CHANGELOG 记录两个提交 + 一次 schema 冻结 | §7 |
| AR-22 | 加约束 | B 条落成 `tools/check-deprecated.sh` + 基线文件（G4）；A 条升 G9；D 条按 AR-14 拆两条 | §1.5.3 |
| AR-23 | 反驳 | Kover 按包过滤（排除 `generated`/`internal`），门槛 = 手写 `foundation` + `components` 行覆盖 ≥80% | §1.5.5 |
| AR-24 | 接受 | E1' 口径 = `apkanalyzer dex packages` 的**包级 dex 增量**；M1 出脚本 | §1.5.6 |
| AR-25 | 接受 | adb 命令 → 预期断言表（含 `wm density` 触发重建、验证 `Saver`） | §1.6.1 |
| AR-26 | 接受（不改） | 库模块 Live Edit 未验证保留为 V6（low，不阻塞） | §9.1 |
| AR-27 | 加约束 | 新增 `WDPreviewCaseCoverageTest`：`@Preview(name=…)` 与 `preview-cases.yaml` **双向包含**断言（手写行解析） | §1.6.2 |
| AR-28 | 接受（无动作） | 37 = 20 + 17、删 `patterns/`、批次 M2–M6 | §2.1 |
| AR-29 | 加约束（high） | `WDButton` 补 `onClickLabel` + `role: Role? = Role.Button`，并在 `clickable` 里传 | §2.2① / §6.2 |
| AR-30 | 接受 | `WDDebugAssertions`（`src/debug` 真实现 + `src/release` no-op）；**不开 `buildFeatures.buildConfig`** | §2.2① |
| AR-31 | 接受 | 成对断言：`loading=true` 时文本仍存在（`alpha=0f` 占位）+ 两态宽度差 ≤0.5dp | §2.2① 表 |
| AR-32 | 加约束 | `maxLines` 契约写作 `"MAX_VALUE unless singleLine=true"`；`characterLimit` 用 `InputTransformation.maxLength` | §2.2② |
| AR-33 | 接受（无动作） | `WDTextField` 不设 `contentDescription`、错误态 `semantics { error(...) }` | §2.2② |
| AR-34 | 加约束 | `density = null` 的契约语义；行高按 AR-67；`onClick == null` 三条断言 | §2.2③ |
| AR-35 | 反驳（blocker） | 组件只留 `state` 一份真源，删 `initialDetent`/`skipPartiallyExpanded` + `require(detent in detents)` 断言 | §2.2④ |
| AR-36 | 反驳（high） | `saver(skipPartiallyExpanded)`（改名）+ `restore` 返回 `null` 不抛 + 版本前缀校验 + 三条断言 | §2.2④ |
| AR-37 | 反驳（high） | `WindowInsets.ime.union(navigationBars.only(Bottom))`，footer 不再叠加任何 insets | §2.2④ / §3.3.4 |
| AR-38 | 加约束（high） | `WDIcon` 完整签名（**`tint` 默认 `Color.Unspecified` → `LocalWDContentColor`**，LR-01）；`WDDatePicker` 走平台 `DatePickerDialog`（**F41**）；`WDProgressBar/Ring` 补 `progressBarRangeInfo`；**三者受控值名按 C-15 落**（#23 `value`→`date`、#15/#16 `progress`→`value`，t22） | §2.3 |
| AR-39 | 接受 | `WDXxxDefaults` 例外只限颜色组合，范围写进 README | §2.3 |
| AR-40 | 接受（无动作） | 槽位四规则 + receiver 进 ABI 提醒 | §2.4 |
| AR-41 | 加约束 | 缺图**保留占位尺寸** + debug 断言；README 说明"默认空集是有意设计（U8）" | §2.3 |
| AR-42 | 接受 | "调用方 modifier 在最外层"变断言；补 M6（内部修饰符不得插到调用方 modifier 之前） | §2.5 |
| AR-43 | 加约束 | `wdGlass(level)` **只接参数**，解析在组件体内；`wd*` 一律不读 local | §2.5 |
| AR-44 | 反驳（high） | `WDTheme` 补 `icons`/`motionScale`（8 参 / 7 local）；**不引 `LocalWDAppearance`** | §2.5 / §3.2.3 |
| AR-45 | 反驳（blocker） | `WDLayout` 值等 + companion 单例；`ColorScheme`/`Typography`/`Shapes` 全 `remember`；G11 + 重组验收 | §2.5 |
| AR-46 | 接受（无动作） | 状态优先级 + 三条派生 + error 属"值维度" | §2.6.1 |
| AR-47 | 加约束 | 按下叠加 = `fillPressed × WDComponent.pressOverlayAlpha`；新增令牌 `press-overlay-alpha`；**禁 `ColorMatrix`/`renderEffect`/`graphicsLayer.alpha`** | §2.6.2 / §3.1.3 |
| AR-48 | 接受 + 加约束 | 删 `WDPressIndication` 的 `equals/hashCode` 覆盖；请设计删 `specs:118` 的 `indication = null` | §2.6.2 |
| AR-49 | 加约束（high） | `stateDescription` 空安全；删除组件级手写 `disabled()`（E4 一处收口）；`Role` 补 `ValuePicker`/`Carousel` + `contracts/roles-android.yaml` | §2.6.3 |
| AR-50 | 接受 | 截断场景断言完整读屏文案存在且唯一 | §2.7 |
| AR-51 | 接受（无动作） | 组件不渲染空态占位；`count = 0` 隐藏 | §2.7 |
| AR-52 | 接受 | demo 示例删 `if (loading) return@WDButton` | §2.7 |
| AR-53 | 加约束 | 阈值进令牌 `WDComponent.layoutBreakFontScale = 1.3f`（L4），并断言"阈值来自令牌" | §2.8.1 / §3.1.3 |
| AR-54 | 加约束 | 文档改可执行两行：`items(list, key = { it.id }) { WDListRow(modifier = Modifier.animateItem()) }` | §2.8.3 |
| AR-55 | 接受 | 行盒 memo key = `(style, fontScale, fontFamily, locale)`（不是 `Density`/`Configuration`） | §2.8.3 |
| AR-56 | 接受 + 降级 | V5 降级为"真机复验"（E7 机制已闭合）；补"只读不得覆写系统" | §2.8.4 / §3.5.2 / §9.1 |
| AR-57 | 加约束（high） | 全量 `@Immutable`/`@Stable` 清单 + G5（`$stable` 存在性）+ G6（metrics） | §2.9.1 |
| AR-58 | 加约束（high） | "加带默认值的参数 = 二进制不兼容"进 ABI 规则 + `$default` 行数断言 | §2.9.2 / §1.5.4 |
| AR-59 | 接受（无动作） | Token 双轨制 | §3.1.1 |
| AR-60 | 接受但换理由 | 理由改为**避免内联**；值的可见性只由 banner hash 提供 | §3.1.1 |
| AR-61 | 加约束（blocker） | `WDComponent` **12 键**（10 个 `component.*` + `pressOverlayAlpha` + `layoutBreakFontScale`）+ 归属规则 + 行高（可见内容单值键 + 布局盒 48） | §3.1.3 |
| AR-62 | 反驳（high） | 断言分两层（令牌层 + `WDLineBoxTest` 布局层）；fixtures 记 natural；D2 请求改 U5 措辞 | §2.8.1 / §8.1 |
| AR-63 | 加约束 | 不引 `LocalWDAppearance`；`WDAppearance` 作**值**传入 `WDGlass.resolve`；`rememberWDHighContrast()` 保留只读 | §3.2.3 |
| AR-64 | 接受 | "是否要 App 内运行时换肤"列为 **M0 产品决策**（A-13），默认方案 A 并写明代价（**结果：已决 = 层一**，见 §3.2.2 / §3.1.2a） | §3.2.2 / §9.2 |
| AR-65 | 加约束（high） | M1 桥 `Typography` + `Shapes`（`internal`）+ `remember` + 消费方形态测试（`WDTheme { Text("x") }` == `WDType.body`） | §3.2.3 |
| AR-66 | 反驳（blocker）+ 加约束 | 触控自研 `layout` 修饰符；玻璃能力位 `WDGlassCapabilities`（**不看单一 `SDK_INT`**） | §3.6.3 / §3.5.3 |
| AR-67 | 加约束（blocker） | 行高：**可见内容 44（单值键，两端同值）+ Android 布局盒 48（= 44 + 上下各 2dp 透明内边距）** + 契约句"Android 列表行热区即行高" —— **补充说明（非取代；P7 / 案 B）**：原两条断言保留、新增"可见内容 44 ± 0.5" | §3.1.2 / §3.3.1 / §2.2③ |
| AR-68 | 接受 | insets 组件白名单（6 个）→ G10；其余组件零 `WindowInsets` | §3.3.2 |
| AR-69 | 接受（无动作） | 不引 `androidx.window`；折叠屏/横竖屏由宿主负责；库负责 `Saver` | §3.3.3 |
| AR-70 | 接受 | `dragBy`/`snapToDetent` 必须 `internal`；`onPostFling` 阈值边界单测 | §3.4.1 |
| AR-71 | 接受（无动作） | 不引 `activity-compose`；back → `onDismissRequest` 由 Dialog 默认行为覆盖 | §3.4.2 |
| AR-72 | 接受 | `wdSystemGestureExclusion()` 调用点 == 1（G12） | §3.4.3 |
| AR-73 | 接受 + 小修 | 去掉 value class 上的 `@Immutable`；`rememberWDInfiniteSpec` 的 null 契约写进 KDoc | §3.5.2 |
| AR-74 | 加约束 | 新增 `rememberWDEffectSlot(kind)` 可测配额 + 阴影令牌进 M0-1（`elevation.*` 已存在，补 Kotlin 出口）；阴影落地给"近似映射 + **F43** 登记" | §3.5.3 / §3.1.3 / §4.1 |
| AR-75 | 反驳（blocker） | `WDHaptics`/`WDAnnouncer` 接口 + `remember*()` + 可注入 local + 常量分级表；**默认值微调为 `null`**（理由见 §3.6.5） | §3.6.5 |
| AR-76 | 反驳（high） | 镜像以 `ImageVector.autoMirror` 为准；库内只兜底；`mirrorsInRTL` = 语义标记 | §3.7.3 |
| AR-77 | 接受 + 2 加约束 | **F33**（手写类也 `internal constructor`）与 **F40**（RTL 不镜像写成显式断言）已落 | §4.1 / §3.7.3 |
| AR-78 | 接受 | U17 写成"两端各自的检查命令"；U15 补 `fontScale 2.0` 也跑一遍 | §4.2 |
| AR-79 | 接受（结论） | 两处缺口已补：硬约束 1 的 M0-1 清单加 D6 + 硬约束 2 加**布局级**行盒断言 | §3.1.2 / §2.8.1 |
| AR-80 | 加约束 | M0 补 4 项（截图插件开关、`robolectric.properties`、metrics 断言 task、`verification-metadata` 生成命令）；M2 补 2 项（`role`/`onClickLabel`、热区断言） | §7 |
| AR-81 | 反驳（一条） | 自证表每条加"前置条件"列；`assertIsButton` 需 `role = Role.Button`；尺寸断言在 `runOnIdle {}`/`waitForIdle()` 内 | §2.2① / §7 |
| AR-82 | 加约束 | 风险表追加 S9–S12（m3 拆不干净 / Robolectric 离线 / CJK 行盒 / local 值不稳） | §8.2 |
| AR-83 | 逐条裁决 | P1–P8 按评审 §6.1 执行（见 §8.1） | §8.1 |
| AR-84 | 加约束 | V5 降级；新增 V12–V14；**O-A1（indication）与 O-A5（`LocalWDAppearance`）关闭**；O-A11 由 D5 拍板 | §9 |
| AR-85 | 加约束 | 证据索引补 §0.1 的 E1–E8；M0 清单加 `android/README.md:22` 改写 + `:68-70` 门禁命令对齐 | §10 / §7 |
| AR-86 | 加约束（high） | `WDSheet` → `WDBottomSheet` 家族改名（M2 前完成） | §2.1 |

---

## 7. 排期与出口判据（M0–M6）

### 7.1 M0（规范冻结，2.5 周）——**含 AR-80 补的 4 项**

| # | 动作 | 落点 | 出口判据 |
| --- | --- | --- | --- |
| **M0-1** | 令牌 D1 清单**一次改完**（§3.1.2 的 **21 行**，含 D6 键名、**行高：可见内容 44 单值键 + Android 布局盒 48（= 44 + 上下各 2dp 内边距，不另立令牌键）**、`WDComponent` 12 键、`elevation` 出口、`press-overlay-alpha`、`layoutBreakFontScale`；**并含 §3.1.2a 的 `schemes` 维度 + 32 槽位**） | `wisdomdesign/tokens/wisdom.tokens.json`（**属设计仓，需设计确认 D1/D6/D7**） | `build.js --check` 绿 + 对比度断言绿 + 新增"`motion.spring.*` 键集合恰为 `{response, dampingRatio}`"断言 |
| **M0-2** | 生成器改造：`generated/` 输出 + 包名、banner `version+sha256`、`WDTokensVersion.kt`、派生 `lineHeightRatio`、缺字段 emit 0、**`build.js:549` 静默 `mkdirSync` → `exit 1`** | `wisdomdesign/tools/token-build/build.js` | 错误路径用例（目录缺失）报错退出 |
| **M0-3** | **提交 1（纯移动）**：`Generated/`→`generated/` + 包名迁移 + 手写文件分层搬家 + 生成物重生成 + `apiDump` 同提交 | `android/…/foundation/**` | `apiCheck` 绿；**步 3 出口判据**：`apiDump` 的 diff 只含包名重写——用 `sort` + `sed 's/generated/Generated/'` 对拍证明（AR-21 ①）；同时覆盖基线里**全部** `foundation.generated` 相关门面类改名（含 `WDGradientKt`/`WDThemeKt`） |
| **M0-4** | **提交 2（语义）**：`WDTextStyle` 4 参 + `@Immutable` + `class` + `internal constructor` + 保留 `equals/hashCode`；生成类 `internal constructor`；`toTextStyle()` 迁 `foundation/typography/WDTextMetrics.kt`（**ABI 所有者从 `WDThemeKt` 变 `WDTextMetricsKt`，必须同批 + 记 CHANGELOG**）；触控 44→48；弹簧 μ=1.0；**行高取值：可见内容 44（单值键）+ Android 布局盒 48（由 `Modifier.wdTouchTarget()` 的 2dp 内边距实现）**；**U12 = 32 槽位**（P8） | 生成物 + `api/wisdom-ui.api` | `apiCheck` 绿；人审 diff 只出现预期增删；`:wisdom-ui:apiCheck` **由红转绿**（`08-decisions.md:35`） |
| **M0-5** | **提交 3（依赖面）**：`material3 → implementation`、`tooling-preview → debugImplementation` | `android/wisdom-ui/build.gradle.kts:41,46` | `api` 文件**零 diff** + G9/G1 绿 |
| **M0-6** | 门禁落地：`wisdomGate` + G1–G13 + CI 两行；**增量（LR-21）**：首次运行生成 `knownUnstableArguments` 基线并入库 | `android/wisdom-ui/build.gradle.kts`、`.github/workflows/ci.yml`、`build/compose-metrics/baseline.json`、`tools/update-metrics-baseline.sh` | 首次全绿；**G6（metrics 真断言）与 G13、截图插件开关（`android.experimental.enableScreenshotTest=true`）必须有真实 task**（AR-80 的 2 项 + LR-08/21） |
| **M0-6a**（LR-21） | **基线生产者与更新责任**：① **写入者** = `tools/update-metrics-baseline.sh`（内部执行 `./gradlew -I tools/compose-metrics.init.gradle :wisdom-ui:compileReleaseKotlin` 后把 `build/compose-metrics/*-module.json` 的 `knownUnstableArguments` 抽成 `baseline.json`）；② **谁更新** = **PR 作者**（仅当 PR 因"已知不稳定参数增长"被 G6 拦下时，作者须在 PR 描述里给出理由并提交基线更新，由 reviewer 复核）；③ **禁止** CI 自动改写基线（避免"基线追着代码跑"） | `tools/update-metrics-baseline.sh`、`build/compose-metrics/baseline.json` | 基线文件存在且被 G6 读取（**文件缺失即 fail**） |
| **M0-7** | **测试基建（AR-80 补）**：`src/test/resources/robolectric.properties`（固定 `sdk`）+ `android-all` 预热路径 + `verification-metadata.xml` 生成命令入库 | `android/wisdom-ui/src/test/resources/`、`android/gradle/verification-metadata.xml`、`android/README.md` | 干净 `GRADLE_USER_HOME` 有网可解析；Robolectric 首个单测可跑 |
| **M0-8** | **`android/README.md` 改写（AR-85）**：`:22` 改为"默认降级（半透明填充 + 高光 + hairline）；增强为可选的窗口模糊（API 31+ 且 `isCrossWindowBlurEnabled()`，仅浮层）"、`:68-70` 的门禁命令对齐 `wisdomGate`、追加"`WD*` 命名的已知代价"、"U4 体积收益未量化"、"默认图标集为空是有意设计" | `android/README.md` | 与 P-1/U4/U8 口径一致 |
| **M0-9** | 版本治理 + CHANGELOG 模板 + **第 7 步**：记录"两个提交 + 一次 schema 冻结" | `versions.md`、各仓 `CHANGELOG.md` | AR-21 ② |
| **M0-10** | **U4 数字要求（AR-12）**：`:demo` 建起后必须给一次 E2（集成 APK 差）与 `apkanalyzer dex packages` 的 m3 占比 | `android/demo/`、`tools/size-report.sh` | 两个数字进 CHANGELOG；此前 U4 收益标"未量化"（V10） |

**M0 出口判定（9 条，已按 LR-04 修正 ⑨）**：① `build.js --check` 绿 + 生成物带 version/hash；② 令牌 schema 冻结（含 D1/D4/D6/D7 + LR-06/LR-10 的新键）；③ `apiCheck` 绿且 `apiDump` 与生成物同提交；④ **`wisdomGate` 一次跑绿**（含 G1–G13）；⑤ `android/README.md` 四处改写完成（`:22` 玻璃口径、`:68-70` 门禁命令、U4 体积收益未量化、无障碍注入点语义）；⑥ 契约六文件存在且 PR 必过三类可断言；⑦ 目录树与文件粒度按 `07-summary.md:299-316` 落地（含 `patterns/` 已删、`WDBottomSheet`/`WDBottomSheetDetent(s)` 命名）；⑧ `versions.md` + CHANGELOG 模板就位；⑨ **`tools/size-report.sh` + 空 `:demo` 骨架 + 测量口径成文就绪**（**LR-04**：两个数字（E2、m3 dex 占比）在 **M1 出口**给出，因为 `:demo` 是 U2 的 M1 交付物；若坚持 M0 出数字，则必须把 `:demo` 提前为 M0 交付并**显式改 U2**——需上位批准）。

### 7.2 M1–M6

> **批前签名冻结（LR-17）**：M2 起**每一批**的第一道门都不是写代码，而是"该批组件的完整签名（含枚举 case 与默认值）+ 契约 `params/slots/default` 入库"；未冻结 = 该批不可开工。**受控值参数名一律按 C-15（`40-contract-names.md`）落名**（R3A-01：定名前不得冻结签名），本稿 §2.10 的"契约名"列即其 Android 侧输入。

| 里程碑 | 本稿条目 | 出口判据 |
| --- | --- | --- |
| **M1** | `:demo`（U2）+ fixtures + 预览矩阵双向检查（AR-27）+ `WDLineBoxTest`（AR-62）+ `WDTheme` 8 参（AR-44）+ `WDLayout` 单例（AR-45）+ M3 桥 `Typography`/`Shapes`（AR-65）+ 高对比度三件事（AR-63）+ `WDMotionScale`（AR-56）+ `WDHaptics`/`WDAnnouncer` 网关（AR-75）+ 截图插件接线（AR-10） | 真机 fontScale 2.0 观感；弹簧 μ 并排评审（U10）；`WDTheme { Text("x") }` 字阶 == `WDType.body`；六态截图入库 |
| **M2** | **⓪ 批前签名冻结（LR-17）**：**11 件**基础组件（**V20 对齐**：按 `30-dev-plan.md` §3.1 与 §8.2-P2 的既有裁决统一为 11 件 = 8 件 + `WDBadge`/`WDAvatar`/`WDDivider`；**时点维持 M1 出口前**）的**完整签名（含枚举 case 与默认值）+ `contracts/<component>.yaml` 的 params/slots/default 入库** ⇒ 才允许开工；① `WDButton` 的 `role`/`onClickLabel`（AR-29）+ 热区断言（AR-67：可见内容 44 ± 0.5 / 布局盒 48 ± 0.5）+ `WDTextField` 自研路线验证（V7）+ `WDBottomSheet` 家族（AR-35/36/37，含 `WDBottomSheetDetent(s)`）+ indication 定案落地（AR-48）+ 去抖边界（AR-52） | **11 件**基础组件的语义树 + 热区 + 两态宽度 + `saver` 三条断言全绿 |
| **M3** | **⓪ 批前签名冻结**（本批组件同 M2 口径）+ 基础封板 + 公开 API 冻结（`apiDump`/符号快照入库）+ `$default` 规则生效（AR-58）+ **三处改名必须已完成**（AR-86 / LR-10） | `wisdomGate` 全绿 |
| **M4** | **⓪ 批前签名冻结**（本批同口径）+ 玻璃三路真机验证（`WDGlassCapabilities`）+ 效果配额 API 单测（AR-74）+ 阴影近似映射与 `press-overlay-alpha` 的**视觉评审**（LR-27）+ 预测式返回决策（O-A8）+ E2 转门槛（U3，映射见 §1.5.6）+ 发布物冒烟（`publishToMavenLocal` + 独立小工程） | 玻璃档位单测 + E2 ≤400KB（= `08` U3 的 E1）+ 发布物能编能渲 |
| **M5** | **⓪ 批前签名冻结** + 导航/表单批的降级顺序（`06:133-141`）与 RTL + `WDDatePicker`（F41） | 2.0 档 + RTL 截图 |
| **M6** | **⓪ 批前签名冻结** + 场景组件 + 无障碍回归 + 截图基线封板 + `WDActionState` 决策（A-4） | 全量门禁 + 双端同 tag `v1.0.0` |

### 7.3 自证表（**AR-81：每条加"前置条件"**）

| 主张 | 前置条件 | 验证方式 |
| --- | --- | --- |
| 分层无反向依赖 + m3 计数 == 1 | — | `./gradlew wisdomCheckPackageDeps`（G1） |
| 公开类型白名单 | — | G9 |
| `apiCheck` 转绿 | M0-3/M0-4 完成 | `./gradlew :wisdom-ui:apiCheck` |
| 行盒比与字距（令牌层） | — | `WDTokensTest` |
| **行盒语义（布局层）** | fixtures 有 zh/en natural 值（V12） | `WDLineBoxTest`（读 `TextLayoutResult`） |
| `assertIsButton()` | **`role = Role.Button`**（AR-29/AR-81） | Robolectric 语义树 |
| `assertIsNotEnabled()` | `enabled = false` | 同上 |
| 加载态宽度不变 | 两态各测一次；断言在 `composeTestRule.runOnIdle {}` 内读尺寸 | `onSizeChanged` 断言（U15） |
| loading 期间 action == 0 | 注入 `MutableInteractionSource` | 回调计数（U16） |
| 热区 ≥48 | `Sm` 档视觉 32 **且内部链顺序为 `[调用方 modifier] . wdTouchTarget() . clickable(...) . clip(shape) . background/glass . padding(内距)`**（**LR-23**：`clickable` 必须在 `clip`/`padding` 之前，否则热区被缩回视觉尺寸） | `onSizeChanged` 断言（`Sm` 档断言 ≥48dp ± 0.5） |
| `compact` 行高：**布局盒 == 48 ± 0.5**（AR-67 原断言）**且可见内容 == 44 ± 0.5**（P7 新增） | 行高单值键（44）生效 + `wdTouchTarget()` 的上下各 2dp 内边距 | `WDListRow` 高度断言（量**内容区**与量**布局盒**各一次）（AR-67 补充解释） |
| `saver` 不崩 | 三条断言（未知版本/未知 detent/缺项） | `WDBottomSheetStateSaverTest` |
| 触觉/播报可断言 | 用 `LocalWDHaptics`/`LocalWDAnnouncer` provide 捕获器 | fixtures 断言（U13） |
| 重组稳定性 | metrics 文件存在 | `wisdomCheckComposeMetrics`（G6） |
| 渐变画刷不破坏 skip | `equals` 同时比 `angleDegrees` 与 `stops` | 单测 + metrics |
| 弹簧公式 | μ 配置可读 | `WDMotionTest` |
| 生成物自证 | — | `WDTokensVersionTest`（G7） |
| 体积 | `:demo` 存在 | `tools/size-report.sh` + `apkanalyzer dex packages` |
| 玻璃降级 | `WDGlassCapabilities` 由测试注入 | 单测：`allowBlur=false` 或 `supportsWindowBlur=false` → `Opaque` |
| 效果配额 | — | "同屏 7 个 `WDSkeleton` → 第 7 个静态"单测 |

---

## 8. 需改判项 / 需上位批准项

### 8.1 D1–D7 的裁决状态（**已由 `21-android-leader-review.md` §2 裁定；本节不再“待批”**）

> 口径变化：`21` §2.1/§2.2 已对 D1–D7 与两处微调**逐条裁定**。下表的“归属/截止/阻塞”三列保留作**执行台账**；**仍需给值的人**单列（这些是“设计给值”，不是“再讨论一轮”）。

| # | 事项 | Leader 裁决（`21` §2） | 本稿落地 | 仍需给值的人 | 是否阻塞 M0-1 |
| --- | --- | --- | --- | --- | --- |
| **D1** | `compact` 行高 | **已决（用户决策 #2 / 案 B）**：**行高 = 可见内容 44（单值键、两端同值）**；**Android 的 48 = 布局盒 / 热区（= 可见内容 44 + 上下各 2dp 透明内边距，由 `Modifier.wdTouchTarget()` 实现；热区 ≠ 视觉尺寸，不新增令牌键）** —— 旧 D1 的行高口径（把 Android 的行高写成布局盒尺寸）已作废（P7 / t42 修正） | §3.1.2 第 4/5 行 + §3.1.3（生成值 = `44.dp`）+ §2.2③ 行高公式 + §3.3.1 | 设计（**已由用户拍板，无待给值**） | **是**（对 M0-1 令牌冻结仍成立） |
| **D2** | U5 断言形式 | **采纳，但按统一形式**（= iOS CR-8 的两句；**是澄清不是改判**） | §2.8.1 的两句定稿 + §3.1.2（不进令牌） | 研发 Leader（M0-5 契约措辞） | 否（走 M0-5） |
| **D3** | `WDDatePicker` 载体 | **采纳**（平台 `DatePickerDialog`；登记 F41） | §2.3 + §4.1-F41 | — | 否（M2/M3） |
| **D4** | `WDGradientSpec` 公开构造器 | **采纳**（**并入 F33**，不再单列） | §2.9.3 + §4.1-F33 | 架构师（生成器出口） | **是**（`apiDump` 基线） |
| **D5** | `WDSemantics` 签名 | **采纳主体，纠正一处对 iOS 的引用**（`label/contextual` 已被 iOS 定稿删除） | §3.7.2：iOS = `join(_ parts: [Text], separator: Text)` + `positional(label:positionText:separator:)` + `[String]` 重载；参数顺序差异登记 **F24** | 两端（M0-5 契约） | 否 |
| **D6** | `dampingFraction → dampingRatio` | **采纳** | §3.1.2 第 1 行 + §3.1.3 | 架构师 + 设计 | **是** |
| **D7** | `press-overlay-alpha` + `layout-break-font-scale` + **F43**（阴影近似映射） | **采纳全部三项**；**名址口径**：两个新键都放 `motion.component.*`（既有 10 键同组，生成器映射不动）；`layout-break-font-scale` 语义上不是动效 ⇒ **README 记一行命名债**（“`motion.component.*` = 组件私有参数的聚合组，不全是动效”），M+1 再议迁组 | §3.1.2 第 13/14 行 + §3.1.3 + §4.1-**F43**（阴影）/ **F45**（状态视觉）+ README 命名债 | 设计（值） | **是** |
| **D8**（本稿新增，承接 LR-08③） | Android 的 L-B 标点/词序机器检查（= iOS R15） | 采纳：新增 **G13**，M0–M2 warning / M3 起 error | §1.5.3-G13 + §1.5.2 gate + §7.1 M0-6 | 架构师（两端同一套判据） | 否（M0-6） |
| **D9**（本稿新增，承接 LR-24/LR-25） | 37 行默认值表 + 37 行槽位表 + F 注册表落库 | 采纳：本稿出内容，M0-5 由架构师落 `contracts/README.md` | §2.4（21 名 + 37 行）、§2.10（37 行默认值/case + C-15 契约名列）、§4.1（F21–F50 + 存疑项 3 条） | 架构师（落库）+ 设计（值） | 否（M0-5） |

### 8.2 与既定裁决的冲突登记（**本稿无与 `07`/`08` 的直接冲突**，以下是"改了设计文档数值/措辞"的项）

| # | 与谁冲突 | 内容 | 处置 |
| --- | --- | --- | --- |
| C1 | `wisdomdesign/docs/09-layout.md:96`（compact 行高 44） | **案 B 修正（P7 / t42）：行高 = 可见内容 44 单值键（两端同值）；Android 的 48 = 布局盒 / 热区（含上下各 2dp 内边距），不再作为行高值** | **已决（用户决策 #2）**；设计侧只剩 **P10** 的措辞同步（把「行高 44 时热区就是整行」改为「整行 = 48 布局盒」）；**iOS 数值一字不改**（评审 §5.3-4 已核 iOS 44/44 自洽） |
| C2 | `07-summary.md:43`（U5 的"±0.5pt 精确等于"） | D2 改成公式断言 | 需研发 Leader 批准；两端同改（AR-62 说明两端都会红） |
| C3 | `02-android.md:744` 的 Q-A2（生成类一律 `internal constructor`） | D4 让 `WDGradientSpec` 例外保留公开构造器 | 需研发 Leader + 架构师批准；**并入 F33** |
| C4 | `02-android.md:796`（旧键保留一个版本 + `@Deprecated`） | U8 一次性改名、不留过渡键（`08-decisions.md:20`） | **以 `08` 为准**（评审已确认）；此处仅留档，不需新裁决 |
| C5 | `android/README.md:22`（"Android 12+ 走背景模糊"） | 改为 P-1 口径（默认降级 + 可选窗口模糊 API 31+） | 属 README 改写，M0-8 执行（AR-85） |

### 8.3 风险表（AR-82 追加 S9–S12）

| # | 风险 | 何时爆发 | 缓解（已落条） |
| --- | --- | --- | --- |
| S1–S8 | 旧稿风险（迁移漏批 / 自研脚本假绿 / `WDTextField` 自研 / 双键改名 / 双重降级 / 桥接版本 / 高对比度无系统等价物 / 2.0 阈值不准） | — | 旧稿 §8 + 本稿对应条 |
| **S9** | **m3 触点拆不干净**（又长出一处 m3 依赖 → U4 的"拆 artifact 时能删干净"破产） | M1 起任何新组件 | G1-③（计数 == 1）+ AR-66 的自研触控 |
| **S10** | **Robolectric `android-all` 离线不可用** → M1 整棵语义树跑不起来 | M1 首日 | `robolectric.dependency.dir` 预热 + CI 缓存（AR-15） |
| **S11** | **CJK 自然行高顶开设计行高** → 行盒契约"默认档 == 设计值"失败 | M1 | AR-62 双层断言 + fixtures 记 natural + D2 改措辞 |
| **S12** | **`staticCompositionLocalOf` 值不稳导致整树重组**（不报错、只掉帧） | M1 | AR-45 值等 + 单例 + `remember` + G11 + 重组验收 |
| **S13**（本稿新增） | `press-overlay-alpha` 的观感在深色下不达设计预期 | M2 首次视觉评审 | D7 的"单键 alpha → 两键叠加色"退路已在设计裁决里预留 |

---

## 9. 未决项与未闭合验证项（**归属角色 + 是否阻塞 M0**）

### 9.1 未闭合验证项（**不得当成已闭合结论**）

| # | 项 | 归属 | 是否阻塞 M0 | 与 `08-decisions.md:41-48` 的关系 |
| --- | --- | --- | --- | --- |
| V1 | 真机 `fontScale 2.0` 观感（不截断/不重叠/层级保持） | 两端 | 否 | = `08 §3-5` |
| V2 | 触觉常量映射 + **`assertive` 播报的近似实现**真机核准 | android-dev | 否（M4 前） | = `08 §3-4` + 本稿新增播报项 |
| V3 | ktlint/detekt/Compose lint 的规则集与告警内容（含 `.editorconfig` 3 行的准确键名） | android-dev | **是**（M0-6 要接入） | = `08 §3-4` |
| V4 | `publishToMavenLocal` + `javaDocReleaseGeneration`（Dokka 版本） | android-dev | 否（阻塞发布前） | = `08 §3-3` |
| V5 | `rememberWDMotionScale()` 在真机 `animator_duration_scale=0` 的**数值与首帧时序** | android-dev | 否 | 新增；**机制已由 E7 闭合，仅剩真机复验**（AR-56 降级） |
| V6 | 库模块（`:wisdom-ui`）的 Live Edit 行为 | android-dev | 否 | 新增 |
| V7 | `WDTextField` 自研路线（`BasicTextField` + DecorationBox）的 IME/选区/无障碍达标度 | android-dev | 否（阻塞 M2） | 新增 |
| V8 | `isHighContrastTextEnabled()` 在 Android 14/15 与厂商 ROM 的真实行为，以及**“组合期读一次、非响应式”是否可接受**（LR-03 的订阅决策：不订阅则系统开关变化不触发重组） | android-dev | 否（M4 前） | 新增（LR-03 明文写了该限制） |
| **V16** | **阴影近似映射（§3.1.3 的 5 行表）与 `press-overlay-alpha` 的观感**是否达标 | 设计 + android-dev | 否（**M2/M4 视觉评审关**；LR-27 + D7） | 新增 |
| **V17** | M0 出口⑨ 的两个数字（E2 / m3 dex 占比）能否在 M1 出口按期给出（依赖 `:demo`） | android-dev | 否（**LR-04** 已把口径移到 M1） | 新增 |
| **V18** | **C-15 的 14 个受控值名尚未落 `contracts/<component>.yaml: params[].name`**（M0-5 落库）；落库前 **M2 的批前签名冻结不得启动** | android-dev + 架构师 | 否（**阻塞 M2 批前签名冻结**，不阻塞 M0） | R3A-01 ③ |
| **V19** | 两端 §2.10 的"受控值列 = 契约名 + 括注本端形态"改造是否一致（iOS 侧由 iOS 修复任务落） | ios-lead + android-dev | 否 | R3A-01；C-15 §2.2 |
| V9 | 自研 `wdTouchTarget` 在 `LazyColumn` 相邻行下的实测（重叠/误触） | android-dev | 否 | 替代旧稿 V9（`minimumInteractiveComponentSize` 已不再使用，AR-66） |
| V10 | **E2（集成 APK 差）与 m3 dex 占比**：当前仓库无 app 模块，一条都没数 | android-dev | **是**（M0-10 出口数字） | 与 `08 §3-3` 相邻 + U4 量化（AR-12） |
| V11 | `Modifier.animateItem()` 在 `WDListRow` 场景的实际效果与 key 要求 | android-dev | 否 | 新增（AR-54 的落地验证） |
| V12 | **CJK 行盒 vs 设计行高**：zh/en 的 natural 实测值未测 | android-dev + 设计 | 否（**阻塞 M1 行盒断言**） | 新增（AR-62/S11） |
| V13 | "全库 m3 import 计数真能收敛到 1" | android-dev | 否（G1 会在 M0 直接给出答案） | 新增（AR-01/AR-66） |
| V14 | screenshot 插件的源集名与任务名 | android-dev | 否（**阻塞 M1 nightly**） | 新增（AR-10） |
| V15 | `WDBottomSheet` 展开态下 back 的语义（先收档还是直接关闭） | android-dev | 否（M4 前） | 新增（AR-71 的落地验证） |

### 9.2 未决项（开放问题；**不允许"待定"**，每条给归属 + 默认动作 + 截止 + 是否阻塞 M0）

| # | 问题 | 归属 | 默认动作（不裁决就按这个做） | 截止 | 阻塞 M0 |
| --- | --- | --- | --- | --- | --- |
| **A-1** | indication 定案（`specs:117` vs `:118`） | 设计 | **关闭**（AR-84 拍板）：保留水波 + `WDPressIndication`；请设计删除 `:118` | 已关闭（M2 前设计可否决） | 否 |
| **A-2** | M3 桥接范围：10 槽位是否扩 48；Typography/Shapes 保持 `internal` | android-lead + 设计 | 先做 10 槽位 + Typography/Shapes 桥（AR-65），48 槽位留待设计确认 | M1 | 否 |
| **A-3** | 高对比度是否需要设计侧新增令牌 | 设计 | 先做三件"不破坏可读性"的降级（§3.2.3） | M1 | 否 |
| **A-4** | 是否在 M6 提供 `WDActionState`（loading + 一次性触发 + 错误） | android-lead + 研发 Leader | 不提供；去抖责任留在调用方（§2.7） | M5 | 否 |
| **A-5** | `LocalWDAppearance` 是否引入 | android-lead | **关闭**（AR-44/AR-63）：不引，`WDAppearance` 作值传参 | 已关闭 | 否 |
| **A-6** | M3 桥接在消费方 m3 版本更低时的跳色边界 | android-dev | 10 槽位桥接 + README 写明"版本更低时缺字段走默认" | M1 | 否 |
| **A-7** | 铰链/折叠屏避让是否由库提供（需 Jetpack WindowManager） | 设计 + android-lead | 不提供，归宿主（§3.3.3） | M5 | 否 |
| **A-8** | 预测式返回是否需要（需 `activity-compose`） | android-dev | 不引；Dialog 默认 back（§3.4.2） | M4 | 否 |
| **A-9** | 共享元素转场是否做 | 两端 + 设计 | 不做（实验 API + 跨端不可对齐） | M6 后 | 否 |
| **A-10** | 密度档是否需运行时切换 UI | 设计 | 库只提供 `LocalWDLayout`，切档 UI 归宿主 | M5 | 否 |
| **A-11** | `WDSemantics` 最终签名（`O-A11`） | 两端 + 研发 Leader | **D5 拍板**：`join(separator, vararg parts)`；两端同批 | 已给答案（M0 契约窗口内确认 iOS 同形） | 否 |
| **A-12** | `WDIconSet` 的键类型与缺图降级 | 两端 + 架构师 | 键 = 生成的 `WDIconName` 枚举；`androidHint` 仅建议；缺图**保留占位尺寸**（AR-41） | M1（与 ios-lead 对齐） | 否 |
| **A-13**（**已决**） | 是否要 App 内运行时品牌换肤 | **用户决策 #8** | **已决 = 层一**（多套生成 scheme + 运行时选择；**不支持**运行时任意 token / 服务端下发；`Q-A2` 不重开）；落地条目 = §3.1.2a；口径 = `30` **§5.8 / §6.3.4** | 已给答案（随 **M0-1** 冻结 scheme 维度） | 否 |
| **A-15**（t22 新增） | **回调名配对**（C-15 §5：`text ↔ onTextChange`、`presented ↔ onDismissRequest`、`selection ↔ onSelectionChange`、`refreshing ↔ onRefresh`）——**非 C-15 范围**，本轮不改现有形参名 | 架构师 + 两端 | 按 C-15 §5 在 **M0-5** 随 `params[].name` 一并锁；本轮保持 `onValueChange`/`onDismissRequest` | M0-5 | 否 |
| **A-14**（**已关闭**） | detent 家族类型名/case 名与 iOS 对齐 | ios-lead + android-dev | **按 `02-ios-spec.md:615-617` 定稿执行**：`WDBottomSheetDetent{Half, Large}` + `WDBottomSheetDetents{All, Fixed}`（0.5 / 0.92）；不再“待确认”（LR-10） | 已定稿（M0-1 进令牌、M2 前完成改名） | 否 |

---

## 10. 证据索引与验收

### 10.1 外部证据（AAR 字节码，来自评审 §0.1，本稿据此改设计）

| # | 结论 | 本稿用途 |
| --- | --- | --- |
| E1 | `minimumInteractiveComponentSize()` 非 `@Composable`，读 m3 的 local | AR-66：改为自研（避免第二处 m3 触点） |
| E2 | `defaultMinSize` 不能把子节点固定尺寸抬到 48dp | AR-66：`wdTouchTarget` 用 `layout{}` |
| E3 | `clickable` 有 `onClickLabel`/`role`；`role = null` 不写 `Role` | AR-29/AR-81 |
| E4 | `clickable(enabled=false)` 自带 `disabled()` 语义 | AR-49：删组件级手写 `disabled()` |
| E5 | `listSaver` 的 `restore` 可返回 null | AR-36：恢复期不崩 |
| E6 | `ImageVector` 有 `equals/hashCode` 且**有 `getAutoMirror()`** | §2.8.3（`WDIconSet` 值等可行）+ AR-76（镜像机制） |
| E7 | `MotionDurationScale` 是 `CoroutineContext.Element`；`MotionDurationScaleImpl` 持 `MutableFloatState` + `ContentObserver` | AR-56：V5 机制闭合，只留真机复验 |
| **V1**（`21` §1） | `LocalContentColor` **只存在于 material3**（ui/foundation/ui-text 零命中） | **LR-01**：新增库内 `LocalWDContentColor`（§2.3 / §2.5） |
| **V2**（`21` §1） | `SemanticsProperties` **没有任何 layout 属性**；文字布局只能经 `SemanticsActions.GetTextLayoutResult` | **LR-02**：布局层断言改用该**动作**（§2.8.1） |
| **V3**（`21` §1） | 框架方法名 `isHighContrastTextEnabled()`（android-36 有 / android-33 无 ⇒ **API 34+**）；Compose 的 `AccessibilityManager` 只有 `calculateRecommendedTimeoutMillis` | **LR-03**：改读系统服务 + 版本守卫（§3.2.3） |
| **V4**（`21` §1） | `Role.Companion` 恰好 **9** 个取值、**没有 `Slider`** | **LR-16**：删 `Role.Slider`；9 值写死 + 反射单测（§2.6.3 / §1.5.4-J） |
| **V5'**（`21` §1） | 规格点名的其余 API 全部存在（`WindowInsets.union`、`InputTransformation.maxLength`、`NestedScrollSource.UserInput`、`HapticFeedbackConstants.*`、`LazyItemScope.animateItem`、`AccessibilityManager` 类） | 排除误报：AR-37/AR-32/AR-70/AR-75 的 API 前提成立 |
| **V6–V10**（`21` §1） | `apiCheck` 今天确实红（缺 4 getter）；令牌 10 键 / 弹簧三键 / 触控单键 / 无 `scrim` / 无 `navbar-large`；`explicitApi()` 生效；`README.md:22` 待改写；Kover 未接入 | LR-11（无值不进清单）、§1.5.5（Kover 首次接入）、§7.1 M0-8（README 改写） |
| E8 | `LineHeightStyle.Mode` 有 `Fixed/Minimum/Tight`；`Role` 恰好 9 个取值 | U5 用 `Mode.Minimum` 成立；AR-49 的 Role 表 |

### 10.2 仓库内证据（本稿亲手读 **[读]**）

| 事实 | 路径:行 |
| --- | --- |
| `material3` 仍是 `api`；`tooling-preview` 是 `implementation` | `android/wisdom-ui/build.gradle.kts:41,46` |
| `explicitApi()` 已生效 | `android/wisdom-ui/build.gradle.kts:31` |
| `apiValidation` 落点 + BCV 插件在根工程 | `android/build.gradle.kts:6,11-13` |
| `include(":wisdom-ui")`；`gradlePluginPortal()` 可用 | `android/settings.gradle.kts:25,11` |
| 版本目录现状 26 行 | `android/gradle/libs.versions.toml:1-26` |
| `api` 文件 207 行、零 `material3`、`WDTextStyle` 带 `component1..3`/`copy$default` | `android/wisdom-ui/api/wisdom-ui.api:1-31,163-172` |
| `touchTargetMin = 44.dp`、`data class WDTextStyle`、五个 `const val` 时长、弹簧 380/900/300 | `android/.../foundation/Generated/WDTokens.kt:225,243,262-266,268-270` |
| M3 桥接只有 ColorScheme 10 槽位 | `android/.../foundation/WDTheme.kt:60-84` |
| 渐变的 CSS 角度实现 | `android/.../foundation/WDGradient.kt:27` |
| 单测三处弱断言 | `android/wisdom-ui/src/test/.../WDTokensTest.kt:28-47` |
| 令牌：`touch-target-min: 44`、`motion.spring.*` 三键含 `stiffness`、`letterSpacing` 只在 `overline`、`component.*` **10 键**、`elevation.{0,1,2,3,brand}` 存在、无 `row-height`/`card-padding` | `wisdomdesign/tokens/wisdom.tokens.json:231,292-295,194,297-308,252-271` |
| 生成器输出路径 + 静默 `mkdirSync` | `wisdomdesign/tools/token-build/build.js:20-24,549` |
| 令牌纪律（组件禁字面量） | `wisdomdesign/README.md:94` |
| 命名真源 / Modifier 位置 / 版本策略 | `wisdomdesign/docs/04-architecture.md:57,75,83-86` |
| `Modifier.blur` 不是背景模糊 | `wisdomdesign/docs/03-platform-mapping.md:206` |
| 触控 44/48、字阶断点、降级五条、减弱动效七行、触觉表 | `wisdomdesign/docs/06-accessibility.md:26,31,114-118,133-141,181-198,204-213` |
| 密度 60/44 + "行高 44 时热区就是整行" + 安全区只留一次 | `wisdomdesign/docs/09-layout.md:95-96,101,133-144` |
| 按下 0.97 + 亮度 96% | `wisdomdesign/docs/01-foundation.md:387` |
| `WDButton` 规格（宽度不变 `:29,:66`、Ripple 矛盾 `:117-118`） | `wisdomdesign/docs/specs/01-basic.md:9-129` |
| 37 组件清单 | `wisdomdesign/docs/specs/README.md:1-100` |
| 深色 Tab 不透明裁定 | `wisdomdesign/docs/12-b22-glass.md:20` |
| iOS 初稿的定点事实（`WDSheet` 不存在、可注入触觉/播报、行高 44 自洽、`WDSemantics` 内置全角逗号） | `00-ios-draft.md:29,819,936,1000-1011,1023,1048`（评审 §5.3 定点核对） |
| 8 条硬约束 / U/F 边界 / 门禁三层 | `08-decisions.md:28-35`；`07-summary.md:31-96,325-358` |

### 10.3 与 `10-android-draft.md` / `11-android-review.md` 的差异索引

- 与旧稿的 delta：本稿 §0.3（Δ1–Δ20）；
- 与评审的逐条对应：本稿 §6.3（AR-01–AR-86）；
- **本稿相对评审的唯一两处显式微调**：AR-75 的 local 默认值（`error()` → `null`）与 AR-74 的阴影落地（近似映射 + **F43** 登记）——均在 §6.1/§3.6.5/§3.1.3 写明理由；
- **F 编号按 `20-ios-leader-review.md` §3 重编号，存疑项 2 条**（§4.1.1-S1 角色/动作标签形参、S2 玻璃六档额外输入的编号）；
- **按 `20` §2.4 的两处改名已落**：detent 家族 → `WDBottomSheetDetent` / `WDBottomSheetDetents`（case `Half`/`Large`）；U10 输出 → `WDGlassResolution`，U10 必选输入 → `WDTextLevel`。

### 10.4 本轮的验收命令（只读，不改三仓）

```bash
test -s 12-android-spec.md
test "$(grep -c '^## ' 12-android-spec.md)" -ge 8
grep -q 'F48' 12-android-spec.md          # R3A-02：F48–F50 已进副本
test -z "$(git -C android status --porcelain)"
test -z "$(git -C iOS status --porcelain)"
test -z "$(git -C wisdomdesign status --porcelain)"
```

**本轮（t22，第 4 轮修复）的写入声明**：唯一写入 = `12-android-spec.md`（R3A-01 定名 + §2.10 契约名列 + F48–F50 副本同步 + F46/F47 状态统一）；**`android/`、`iOS/`、`wisdomdesign/`、**跨端工作区文档集（已退役）**、`02-ios-spec.md`、`40-contract-names.md` 全部只读、零改动**（上述三条 `git … --porcelain` 均为空）。

### 10.5 P7–P9 回写（`t42`）的落点、验收与写入声明

**回写来源**：`30-dev-plan.md` **§5.9 的 P7 / P8 / P9（Android 侧）** + §5.5 / §5.8 的键形定稿 + 用户 2026-10-05 的**案 B 裁决**（行高：可见内容 44 两端同值；Android 布局盒 48 = 44 + 上下各 2dp 透明内边距）。

| 项 | 落点（本稿行号） | 改成了什么 |
| --- | --- | --- |
| **P7** ①（六处完整口径） | `:41`（Δ4）、`:674`（行高公式）、`:678`（4 格矩阵补注）、`:1291-1292`（M0-1 清单第 4/5 行：**双键 → 单值键**）、`:1353`（`rowHeightCompact` 生成值 + 注释）、`:1416`（令牌单测行） | 一律写成"**可见内容 44（两端同值）+ Android 布局盒 48（含上下各 2dp 透明内边距，由 `Modifier.wdTouchTarget()` 实现）**"的完整口径，**不简化**为 44 或 48 |
| **P7** ②（AR-67 **补充解释**，非取代） | `:680` 后（§2.2③，实际块 `:682-685`）、`:1532` 后（§3.3.1，实际块 `:1534-1537`）；台账 `:1981`、`:2059` | **原文与两条断言值 `(compact,1)==48±0.5`、`(compact,2)==76±0.5` 原样保留**；追加"行高 = 布局盒（可见内容 44 + 上下各 2dp 内边距）；热区即该 48 布局盒，仍不与相邻行重叠"；台账行加"**补充说明（非取代）**" |
| **P7** ③（新增断言） | `:684`（§2.2③ 块内）、`:1075`（§2.8.1）、`:2129`（§7.3 自证表） | **紧凑单行可见内容高度 == 44dp ± 0.5**（测内容区、不含内边距） |
| **P7** ④（两条登记） | `:1314`（键形定稿）、`:1537`（§3.3.1 块内） | **U（必须统一）= 两端可见内容高度 44 ± 0.5**；**F（各端自由，平台惯例）= 行距（Android 48 / iOS 44）** |
| **P7** ⑤（键形定稿） | `:1314`、`:1291-1292`、21 行清单的表头注（「本表 = iOS CR-10 的 12 行 + …」那句）、`:2088`（M0-1 行） | 行高 = **单值键**（`size.row-height.{comfortable,compact}` = 60 / 44，两端同值）；**48 是布局盒**、**不新增令牌键**；触控热区仍双键 |
| **P8**（U12 31 → 32） | `:1274`（双轨表「运行时变量轨」）、`:1417`（槽位计数断言）、`:1430`（构造器参数计数由旧值 → **32 参**公开构造器）、`:1914`（U12 行 + 机器检查）、`:1390`（P8 同步说明：生成物 32 个 `public val` / `WDColorSlot` 32 条 / `api` 合成构造器参数 +1） | **32 槽位（含 `text.disabled`）**，浅深成对；计数断言 == 32 |
| **P9**（schemes 维度） | `:1316-1329`（新增 **§3.1.2a**：标题 + 注 + 8 行表 + 出口判据）、`:1432`（§3.2.2 后的指引句） | `schemes: {light, dark, …}` + 生成器 `--schemes`；切换 = 换已生成 scheme、**不重启进程**、**不进高频路径**；**不支持**运行时任意 `token.json` / 服务端下发；口径 = `30 §5.8` |
| **V20 对齐**（船长授权扩展项） | `:2109`（§7.2-M2） | `12` 原写"M2 **8 件**" → 统一为 **11 件**（= 8 件 + `WDBadge`/`WDAvatar`/`WDDivider`），并标注"**V20 对齐（时点维持 M1 出口前）**" |

**本轮回写没有改变的事实**：① 21 行清单仍是 **21 行**（17 行 (a) + 4 行 (b)，编号连续 1..21；`schemes` 单列为 §3.1.2a，**不进该表**，以免改变 `30 §2.2-②` 的判据口径）；② AR-67 的两条原断言**一字未动**；③ AR-67 一律按"**补充解释**"表述（"**非取代**" / "**补充而非取代**"），**没有任何“取代”式的结论**。

**本轮的验收命令（可直接复制）**：

```bash
test -s 12-android-spec.md
test "$(grep -cE '已[取]代' 12-android-spec.md)" -eq 0   # 期望 0（用字符类写该词，避免本行自命中）
grep -q 'text.disabled' 12-android-spec.md               # P8
grep -q '布局盒' 12-android-spec.md                       # P7
grep -q 'schemes' 12-android-spec.md                     # P9
# 说明：android 仓当前有 t35 的**未提交产物**（android/AGENTS.md、android/docs/ARCHITECTURE.md，均为 `??`），
# 故 `test -z "$(git -C android status --porcelain)"` 在本工作区**不可能为空**；t42 未触碰 android/——
# 等价的定点检查见下（本条与 t35 的验收口径一致）：
test -z "$(git -C android status --porcelain -- wisdom-ui gradle build.gradle.kts settings.gradle.kts README.md)"
```

> **下游复核用完整清单（t44/t45）**：见本任务报告（`t52`）与 `AGENTS.md §8` 中的可复制命令组；本文件内**不内嵌** `grep -c` 形式的计数断言——因为命令本身的文本会被 `grep` 自命中而失真（`已[取]代` 一例即为为此写成字符类）。

**本轮（`t42`，P7–P9 回写）的写入声明**：**唯一写入 = `12-android-spec.md`**（本文件）；`android/`、`iOS/`、`wisdomdesign/`、**跨端工作区文档集（已退役）**、`02-ios-spec.md`、`30-dev-plan.md`、`40-contract-names.md` 与其余 `**` **全部只读、零改动**；`android/` 下的两个新文件是 `t35` 的产物，与本轮无关。

---

← **迁移前追溯输入（已退役，无仓内副本）**：Android 实现初稿、Android 组长质询、iOS 实现初稿、跨端架构终稿、用户决策集 —— 其结论已内联本文件与 `ARCHITECTURE.md` §16；如需原件请向跨端工作区索取（本仓不再提供路径）。


