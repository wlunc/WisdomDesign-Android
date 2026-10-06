# AGENTS.md · WisdomDesign-Android 仓内施工手册

> **读者**：会在这个仓库里写代码的 agent（vibe coding 角色）与人类 reviewer。
> **本文件的效力**：本仓的**操作入口**（"按什么顺序做、做到什么算过、什么值已冻结、什么不能碰"）。
> **效力顺序（冲突裁决链，跨端统一：与 `iOS/AGENTS.md`、两端 `ARCHITECTURE.md` 同一串）**：**`08`（用户决策）＞ `07`（跨端契约 U/F）＞ 本端规格 `12` ＞ `40`（只管名字，C-15 唯一表）＞ `30`（进程与冻结值口径）＞ 本端 `AGENTS`/`ARCH`**。
> **分工说明**：**名字冲突看 C-15 名表**（[`docs/SPEC.md`](docs/SPEC.md) §2.10 只是只读副本）；**进程 / 冻结值口径冲突看 [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) §5 / §6 / §7**；其余实现细节仍以 [`docs/SPEC.md`](docs/SPEC.md) 为准。
> **预算纪律（`t62`）**：**本文件受 64KB（65536 B）自动加载预算约束**；**新增细节请写 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)**，本文件只保留**入口、判据与指针**（超限会被宿主**静默截断尾部**）。核对命令：`wc -c < AGENTS.md`。
> **来源（迁移后）**：本端规格已迁仓为 [`docs/SPEC.md`](docs/SPEC.md)；计划与进程口径在 [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md)；架构细节在 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)。原始输入来自**跨端工作区文档集（已退役）**（用户决策 / 跨端契约 U/F / C-15 名表 / 可开工边界评审）；结论已内联本仓（追溯见 `docs/ARCHITECTURE.md` §16）。落库：`android-lead`，2026-10-05。
> **写法**：不复述规格全文，只给"结构 + 决策 + 引用"。**记号解析（迁移后生效）**：`12 §x.y` = [`docs/SPEC.md`](docs/SPEC.md)（本端规格，已迁仓、**节号与迁移前一致**）；`30 §x.y` = [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md)（§2 里程碑 / §3 批次 / §5 冻结值 / §6 回写 / §7 待给值 / §8 风险）；`40` = `contracts/<component>.yaml` + 本手册 §6 第 15 行；`07`/`08`/`20`/`21`/`27`/`61`/`63`/`66` = **跨端工作区文档集（已退役）**，**仅作历史追溯**（结论已内联，或见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) §16）。**设计真源**位于**外部设计仓 `wisdomdesign/docs/`**（未退役；含 `01-foundation.md`、`03-perf-release.md`、`06-accessibility.md`、`12-b22-glass.md` 等）——后文以**文件简称**引用。**本仓是三个仓中的一个**（`android/`、`iOS/`、`wisdomdesign/`），只在 `android/` 内施工。
> **本文件不替代任何门禁**：凡命令写在这里，就必须真的跑；凡标【未验证】的，**不得写成"已通过"**（§7）。

---

## 1. 本仓定位与可写 / 不可写边界

本仓 = **单模块 Compose 组件库**（`:wisdom-ui`）+ 两个非发布入口（`:demo`、`:benchmark`，M1 起）。发布物是 Maven Central 上的 `io.github.wlunc.wisdom:wisdom-ui`（AAR，二进制兼容基线 = `api/wisdom-ui.api`）。`12 §1.1.1`

| 区域 | 路径 | 允许写？ | 规则 |
| --- | --- | --- | --- |
| 库公开 API | `wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/{components,foundation}/**`（**除 generated**） | ✅ | 层包；见 §10 的反模式清单 |
| **生成物** | `wisdom-ui/src/main/kotlin/…/foundation/generated/**` | ⛔ **唯一写入者 = `wisdomdesign/tools/token-build/build.js`** | 手改会被下一次生成覆盖；改令牌只能回 `wisdomdesign/`（设计仓）。**这是 M0-3（提交 1）之后的目标态**：当前实际是 **`foundation/Generated/`（大写）**，且手写的 `WDGradient.kt` / `WDTheme.kt` 仍在 `foundation/` 根下（`12 §1.1.2`） |
| **ABI 基线** | `wisdom-ui/api/wisdom-ui.api` | ⛔ 只能由 `./gradlew :wisdom-ui:apiDump` 生成 | **必须与生成物同提交**；M0-4 的出口判据（§9.3） |
| 调试断言 / 预览 | `src/debug/kotlin/**`、`src/release/kotlin/**`（no-op 断言）、`src/test/kotlin/**` + `src/test/resources/**` | ✅ | 预览**只写一份**，物理位置 = `src/debug/kotlin/…/components/**`，由 `sourceSets` 挂给 screenshot 源集（`12 §1.2.2`） |
| 构建脚本 | `wisdom-ui/build.gradle.kts`（检查任务段） | ✅（**M0-6 的任务清单**） | 依赖块按 `12 §1.2.3`；**不得**加 `buildFeatures.buildConfig` |
| 版本目录 | `gradle/libs.versions.toml` | ⚠️ 仅"锁定版本号"这类显式任务 | **不得留 `<M0 锁定>` 占位**：先锁值再提交（`12 §1.2.1` AR-08） |
| 依赖校验 | `gradle/verification-metadata.xml` | ⛔ 由 `./gradlew --write-verification-metadata sha256 help` 生成 | `12 §1.2.4`。**该文件今天不存在**（M0-7 生成后入库） |
| 检查脚本 | `tools/**`（`check-*.sh`、`compose-metrics.init.gradle`、`size-report.sh`、`update-metrics-baseline.sh`） | ✅ | 自研检查**必须配反例单测**（G1-③/H、G2/I，`12 §1.5.4`）。**`tools/` 目录今天不存在**（M0-6 起建立） |
| CI | `.github/workflows/**` | ✅ | PR 只跑两行（`wisdomGate` + `apiDump && git diff --exit-code`），`12 §1.5.2`。**该目录今天不存在**（M0-6/M0-9 建） |
| demo / benchmark | `demo/**`（M1）、`benchmark/**`（M1 后） | ✅ | `:demo` **禁止使用 `internal` API**（它是消费者视角的验证器）。**两个模块今天都不存在**（`settings.gradle.kts` 只 `include(":wisdom-ui")`） |
| 仓库文档 | `README.md`、`CHANGELOG.md`、`docs/**` | ⚠️ 由 lead / tech-lead 执行 | `README.md` 的四处改写是 M0-8 的任务（`12 §7.1`） |
| **本仓之外** | `wisdomdesign/tokens/wisdom.tokens.json`、`tools/token-build/build.js`、`contracts/**`、`iOS/**` | ⛔ **完全不属于本仓** | 改令牌/改契约要走设计仓或架构师；本仓只消费生成物与契约 |

**当前仓库里实际存在的**（2026-10-05 实测）：`settings.gradle.kts`（只 `include(":wisdom-ui")`）、`build.gradle.kts`、`gradle/libs.versions.toml`、`wisdom-ui/{api/wisdom-ui.api, build.gradle.kts, consumer-rules.pro, src/main/**, src/test/**}`、`AGENTS.md`、`docs/ARCHITECTURE.md`。**上表中标"今天不存在 / M0-6 起 / M1 起"的路径都还没建立**——按它们去读或建文件会失败、或建错目录。

**一句话**：本仓可以自由写的是"组件与 foundation 的手写实现 + 检查脚本 + 测试"；**一切"自动生成的、被别人拥有的、跨仓共享的"文件都不在这个仓里手改**。

---

## 2. 唯一真源与阅读顺序

**阅读顺序（先读哪份；「冲突裁决链」见文首，两者不是一回事）**：

| # | 文件 | 它管什么 | 什么时候必须读 |
| --- | --- | --- | --- |
| 1 | [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) **§9.2** | 跨端契约的 U/F 清单（U = 必须统一 / F = 各端自由）+ 跨端依赖与并行度 | 任何"两端要不要一致"的问题 |
| 2 | [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) **§5 + §8.2** | 用户决策后的冻结值 + 未闭合验证项（V1–V18） | 任何"值能不能改"的问题 |
| 3 | [`docs/SPEC.md`](docs/SPEC.md) | **本端实现规格**（工程基建 / 37 组件签名 / 主题交互 / G1–G13 / M0–M6 / 未决项）—— 终审 pass（历史记号 `27`）；**节号与迁移前一致** | 动手前必读对应章节；**写代码 = 实现它** |
| 4 | `contracts/<component>.yaml`（`params`/`slots`/`default`；**M0-5 才落库**） | **C-15 受控值名**：37 行（canonical + 两端形态 + 是否允许 `is` 前缀）；本手册 §6 第 15 行的 14 行改名 + 2 例外 | 出任何组件签名时（先查表再落名） |
| 5 | [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) | 开发计划：§2 里程碑与入口/出口判据、§3 批次分配、§4 命令与门禁、§5 冻结值、§6 回写清单 P7–P12、**§7 设计待给值 D-1…D-16**、§8 风险与未验证、§10 变更与发布 | 排期、结批、**进程与冻结值口径**（**仅这几个维度**上它的效力高于本端规格，见文首分工说明；其余实现细节仍以 `12` 为准） |
| 6 | 本文件 | 操作手册（边界 / 命令 / 冻结值 / 禁止事项） | 每次开工前扫一遍 §6/§7/§10 |

**"唯一真源"清单（同一个事实只有一个地方能改）**：

| 事实 | 唯一真源 | 本仓的形态 |
| --- | --- | --- |
| 令牌值与键名 | `wisdomdesign/tokens/wisdom.tokens.json` | `foundation/generated/**`（生成物，只读） |
| 令牌生成逻辑 | `wisdomdesign/tools/token-build/build.js` | —（本仓不写生成器） |
| 受控值参数名 | `contracts/<component>.yaml`（C-15） | `12 §2.10` 是**副本**（只读）；`contracts/<component>.yaml: params[].name` 是落库形态 |
| 组件枚举 case / 默认值 | `contracts/<component>.yaml`（M0-5 起；此前 = `12 §2.10`） | Kotlin `enum class` + 默认参数（默认值进 ABI，**只能靠契约锁**，`12 §2.9.3`） |
| 公开 ABI | 编译器输出 | `api/wisdom-ui.api`（`apiDump` 生成） |
| F 注册表（各端自由项） | iOS 侧 leader review（已退役）§3 + §3-附记 | `12 §4.1` 是**副本**（只读） |
| 组件批次 | iOS 侧规格（已退役）§2.10 的"批次"列（跨端一致） | `30 §3.1` 是本仓的执行口径（**唯一可读版本**） |
| **M0-1 令牌冻结清单** | `docs/DEV-PLAN.md` **§5/§7.1 的合并视图** | **本端 M0-1 清单（`12` 侧 21 行 = 17 行 (a) 令牌 + 4 行 (b) 契约/验收）**；`02` 侧的 17 行是 **(a) 令牌视角的子集**，粒度不同、**不冲突**；判据以 **21 行**为准（`grep -c == 21`，`30 §2.2-②`）。**本文件与 `12` 都不自称"唯一一张三仓清单"**（XR-04） |

---

## 3. 进程骨架：每批怎么开工、怎么结批

> 依据 `30 §3.0`（排期的主读入口）+ `07 §6.4`（三层门禁表）。**人日与周数只作参考，不参与判定**；判定只看"入口判据能不能开工"与"出口判据能不能结批"。

### 3.1 统一的入口判据（I-1…I-5，全部满足才可开工）

| # | 入口判据 | 可验证形态（本仓怎么查） |
| --- | --- | --- |
| **I-1** | 上一里程碑出口已判定通过 | 出口报告落盘（**M0 看 §4.1 的「本仓相关 5 条」= `12` §7.1 的 9 条里的 ③④⑤⑦⑨**；M1+ 看 `30 §2.3`） |
| **I-2** | **批前签名冻结会通过**：该批每件组件的完整签名（含枚举 case 与默认值）+ `contracts/<component>.yaml` 的 `params`/`slots`/`default` 入库 | 文件存在 + 与 C-15 逐行 **0 不一致**（`40 §1`）；**C-15 未落库前 M2 的冻结不得启动**（`12 §9.1-V18`） |
| **I-3** | 该批依赖的令牌与契约已冻结（M0-1 清单、U3 槽位词表、C-15、F 注册表、scheme 维度） | `12 §3.1.2` 的 21 行清单行行有值或书面延后 |
| **I-4** | 门禁在本仓可跑且为绿 | **`wisdomGate` 落地（M0-6）之后**：`./gradlew wisdomGate` **exit 0**；**在此之前**以 §5.0 的现成任务 + 基线漂移检查为判据：`./gradlew :wisdom-ui:apiCheck :wisdom-ui:assembleRelease :wisdom-ui:testDebugUnitTest` **+** `./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api`。**⚠️ 本条判据"`wisdomGate` exit 0"在 M0-6 完成后才成立，M0 期间不得作为开工判据**（命令不存在 ⇒ 找不到 task 不等于门禁失败） |
| **I-5** | 该批未闭合前置项都有"默认执行项"（无悬空） | `30 §6.3` 台账逐行有归属/时点 |

**冻结 ≠ 开工**：可以先把后一批的签名冻结做掉，但**不能开始写代码**（`30 §3.2` 的 G-01…G-03 说明）。

### 3.2 每批的出口判据与门禁强度（`30 §3.0`）

| 批 | 件数 | 出口判据 | 门禁强度 | 关键路径（本仓） |
| --- | --- | --- | --- | --- |
| **M2** 基础 | 11 | 6 条（§4.3） | PR 必过 + nightly **连续 3 日绿** | `WDTextField` → `WDListRow` |
| **M3** 封板 | 1 + 封板动作 | 6 条（§4.3） | 同上 + **API/ABI 基线入库** | 封板（基础批 **12/20** primitives；余 8 件在 M4/M5 出口完成） |
| **M4** 反馈与弹层 | 9 | 7 条（§4.3） | 同上 + 发布前层冒烟（`publishToMavenLocal`） | `WDBottomSheet` → 弹层族 |
| **M5** 导航与表单 | 15 | 6 条（§4.3） | PR + nightly | `WDDatePicker`/`WDPullToRefresh`/`WDNavigationBar`/`WDTabBar`/`WDSegmentedControl` |
| **M6** 场景 + 回归发布 | 1 | 7 条（§4.3） | 同上 + 发布前层 + **双端同 tag** | `WDAssigneePicker` + 回归与发布 |

### 3.3 三层门禁（Android 落地形态）

| 层 | 命令 | 属性 |
| --- | --- | --- |
| **PR 必过**（目标 ≤10 min；**目标值不是实测值**） | `./gradlew wisdomGate` **+** `./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api` | 阻塞合并；批内每日至少一次，**批出口前必须全绿** |
| **nightly** | 六态截图（screenshot 插件）+ `WDLineBoxTest` + 自算对比度 + 大字号矩阵 + `:demo:assembleRelease`（R8 冒烟）+ Benchmark（B1/B2/A2/A3/E2） | **M1–M3 只报不拦**；M4 起 B1/B2/E1（= 本仓的 E2）转门槛（`08 U3` / `12 §1.5.6`） |
| **发布前** | 集成 APK 差（R8 + 资源收缩）+ `apkanalyzer dex packages` + POM 三查 + tag 校验 + AAR 体积 | 阻塞发布（M4 冒烟 / M6 正式） |

> **nightly 的"连续 3 日绿"不是免费等待**：它已计入每一批的日历；批出口评审只能在第 3 日观察通过后举行（`30 §3.4`）。

### 3.4 关键路径（单端串行，任一步延迟 ⇒ 全线顺延）

```text
M0-1 令牌冻结 ─→ M0-2 生成器 ─→ M0-3/M0-4 生成物 + apiDump（apiCheck 转绿）
   └→ M0-5 契约六文件 + C-15 / U3 / F 注册表 / 默认值表落库
        └→ [批前签名冻结] ─→ M2（TextField / ListRow）─→ M2 出口
             └→ M3 公开 API + ABI 基线冻结 ─→ M4 玻璃 + 效果配额 + E2 转门槛
                  └→ M5（DatePicker / PullToRefresh / NavigationBar / TabBar / SegmentedControl）
                       └→ M6 AssigneePicker + 无障碍回归 + 截图封板 + 发布前层 + 双端 tag v1.0.0
```

**并行度**：iOS ↔ Android 完全并行（唯一串行点 = 契约落库与批前冻结）；**批 ↔ 批不可并行**；**同一角色不得并行两个未完成任务**（`30 §3.5`）。

---

## 4. 本端 M0–M6 批次表与出口

> 组件级批次的**执行口径 = `30 §3.1`**（真源是 `iOS 侧规格（已退役） §2.10` 的"批次"列，跨端一致）。注意 `12 §7.2` 仍写"M2 8 件"——**那是待回写项 V20**，以本表为准（§8）。

### 4.1 M0 — 规范冻结（本仓部分：M0-3…M0-10）

| # | 动作 | 出口判据 |
| --- | --- | --- |
| **M0-3** | **提交 1（纯移动）**：`Generated/`→`generated/` + 包名迁移 + 手写文件分层搬家 + 重跑生成器 + **`apiDump` 同提交** | 该提交 `apiCheck` 绿；diff 只含包名重写（用 `sort` + `sed 's/generated/Generated/'` 对拍，`12 §7.1`） |
| **M0-4** | **提交 2（语义）**：`WDTextStyle` 4 参 + `@Immutable` + `class` + `internal constructor`（保留 `equals/hashCode`）；生成类 `internal constructor`；`toTextStyle()` 迁 `foundation/typography/WDTextMetrics.kt`（**ABI 所有者从 `WDThemeKt` → `WDTextMetricsKt`，同批 + 记 CHANGELOG**）；触控 44→48；弹簧 μ=1.0；行高按 §6 的案 B 取值；U12 = 32 槽位 | `apiCheck` 绿（**由红转绿**）；人审 diff 只出现预期增删 |
| **M0-5** | **提交 3（依赖面）**：`material3 → implementation`、`tooling-preview → debugImplementation` | `api/wisdom-ui.api` **零 diff** + G9/G1 绿 |
| **M0-6** | 门禁落地：`wisdomGate`（G1–G13）+ CI 两行；首次生成 `knownUnstableArguments` 基线并入库 | 首次全绿；G6/G13/截图开关必须有**真实 task** |
| **M0-6a** | 基线生产者与更新责任：写入者 = `tools/update-metrics-baseline.sh`；更新者 = **PR 作者**（仅当被 G6 拦下）；**禁止 CI 自动改写基线** | `build/compose-metrics/baseline.json` 存在且被 G6 读取（**缺失即 fail**） |
| **M0-7** | 测试基建：`src/test/resources/robolectric.properties`（固定 `sdk`）+ `android-all` 预热路径 + `verification-metadata.xml` 生成命令入库 | 干净 `GRADLE_USER_HOME` 有网可解析；Robolectric 首个单测可跑 |
| **M0-8** | `README.md` 四处改写（`:22` 玻璃口径、`:68-70` 门禁命令、U4 体积收益未量化、无障碍注入点语义） | 与 P-1/U4/U8 口径一致 |
| **M0-9** | 版本治理 + CHANGELOG 模板 + 记录"两个提交 + 一次 schema 冻结" | `versions.md` + `CHANGELOG.md` 就位 |
| **M0-10** | **U4 数字要求**：`:demo` 建起后给一次 E2 + `apkanalyzer dex packages` 的 m3 占比 | 两个数字进 CHANGELOG（**M1 出口给**，`30 §5.7` U2/U4 行）；此前 U4 收益一律标"未量化" |

**M0 出口判定（本仓相关 **5** 条 = `12` §7.1 的 9 条中的 ③④⑤⑦⑨；本表只给本仓要做的部分）**：③ `apiCheck` 绿且 `apiDump` 与生成物同提交；④ **`wisdomGate` 一次跑绿**（含 G1–G13）；⑤ `README.md` 四处改写完成；⑦ 目录树与命名（`patterns/` 已删、`WDBottomSheet`/`WDBottomSheetDetent(s)`）；⑨ `tools/size-report.sh` + 空 `:demo` 骨架 + 测量口径成文（**两个数字在 M1 出口**）。

### 4.2 M1–M6 本仓任务与出口

| 里程碑 | 本仓任务 | 出口判据（本仓侧） |
| --- | --- | --- |
| **M1** | `:demo`（U2）+ fixtures + 预览矩阵双向检查 + `WDLineBoxTest`（fixtures 含 zh/en natural）+ `WDTheme` 8 参 + `WDLayout` 单例 + m3 桥 `Typography`/`Shapes` + 高对比度三件事 + `WDMotionScale` + `WDHaptics`/`WDAnnouncer` 网关 + 截图插件接线 | 真机 `fontScale 2.0` 观感；弹簧 μ 并排评审（U10）；`WDTheme { Text("x") }` 字阶 == `WDType.body`；六态截图入库；**E2 + m3 dex 占比两个数字** |
| **M2** | **⓪ 批前签名冻结（11 件）** → `WDButton`（含 `role`/`onClickLabel`）+ `WDIconButton` + `WDTextField`（自研路线 V7）+ `WDSwitch` + `WDCheckbox` + `WDBadge` + `WDAvatar` + `WDDivider` + `WDCard` + `WDListRow` + `WDIcon` | 6 条：① 批前冻结（`params[].name` 与 C-15 逐行一致、`allCases` 归一化绿）② `wisdomGate` 绿 ③ nightly：六态截图 + 无障碍断言（`assertIsButton`/`assertIsNotEnabled`/热区/两态宽度/`saver` 三条）④ B1/B2/A3 数字进报告（只报）⑤ 契约用例名 + `icons.json` 对齐 ⑥ 冒烟退役 |
| **M3** | **⓪ 批前签名冻结** + `WDListSection`（`12 §2.1` 里它是**第 19 件** primitive —— **第 20 件是 `WDIcon`**；按批次表口径它是**第 12 件完成的**）+ **封板动作**：`apiDump`/ABI 基线入库、`$default` 规则生效、覆盖率门槛、体积门槛值 | 6 条：① 公开 API 冻结（`apiDump` 入库）② `$default` 规则 ③ **三处改名已完成**（`WDBottomSheet*` 家族、detent、`Generated/`→`generated/`）④ Kover 首次接入 + 门槛 ⑤ 体积门槛值定 ⑥ **基础批 11+1 件全完成 = 12/20 primitives**（余 8 件：M4 2 件 `WDProgressBar`/`WDProgressRing` + M5 6 件 `WDSearchField`/`WDRadio`/`WDSlider`/`WDStepper`/`WDChip`/`WDAvatarStack`） |
| **M4** | **⓪ 批前签名冻结（9 件）** + `WDProgressBar`/`WDProgressRing`/`WDAlert`/`WDBottomSheet`/`WDActionSheet`/`WDToast`/`WDBanner`/`WDEmptyState`/`WDSkeleton` + 玻璃三路真机验证 + 效果配额单测 + 阴影/按下叠加视觉评审 | 7 条：① 批前冻结 ② 玻璃档位单测 + `WDGlassCapabilities` 真机三路记录 ③ 效果配额单测（"同屏 7 个 `WDSkeleton` → 第 7 个静态"）④ 阴影与 `press-overlay-alpha` 视觉评审 ⑤ **E2 ≤400 KB 转门槛** ⑥ `publishToMavenLocal` + 独立小工程能编能渲 ⑦ release AAR 预览符号 == 0 且 debug > 0 |
| **M5** | **⓪ 批前签名冻结（15 件）** + `WDSearchField`/`WDRadio`/`WDSlider`/`WDStepper`/`WDChip`/`WDAvatarStack`/`WDSegmentedControl`/`WDPicker`/`WDDatePicker`/`WDFormRow`/`WDPullToRefresh`/`WDNavigationBar`/`WDTabBar`/`WDToolbar`/`WDFAB` + 动态字体降级顺序（U7）+ RTL | 6 条：① 批前冻结 ② 2.0 档 + LTR/RTL 截图入库 ③ 性能指标进报告 ④ 无障碍断言（`WDSlider` 松手播报一次、`WDStepper` 高频不给触觉、`WDTabBar` 2 行允许）⑤ 契约用例名 + 图标语义名仍绿 ⑥ `V3`（sheet 键盘避让）已闭合 ⑦ **20/20 primitives 完成于本批出口**（11+1+2+6） |
| **M6** | **⓪ 批前签名冻结（1 件）** + `WDAssigneePicker` + 无障碍回归 + 截图基线封板 + 发布前层 + tag | 7 条：① 批前冻结 ② 无障碍回归（TalkBack 闭眼走查 + demo 审计）③ 截图封板（37 件 × 浅/深 × 默认/**2.0 档（U7）** × LTR/RTL）④ 全量门禁 ⑤ 发布前层 ⑥ **双端同 tag `v1.0.0`** ⑦ 发布 checklist 完成 |

> **批次计数自证**：11 + 1 + 9 + 15 + 1 = **37**（组件）；档位 6 A + 19 B + 12 C = 37（`30 §3.1`）。
> **primitives 计数自证**：20 = M2 11 + M3 1 + M4 2 + M5 6。
> **二十件全部完成于 M5 出口**（11+1+2+6）——**封板批只到 12/20**。`30` 原文的"最后一个 primitive"表述曾在 `t48` 登记为质疑项 **N-1**；**现已由 `t50` 勘误闭合**（`30 §8.2`「**勘误（t50）**」），本表口径与 `30` **现文一致**（见 §12 的 N-1 行）。

---

## 5. 命令清单（含**现状标注**：`现成` / `M0-6 起` / `M1 起`）

> 全部在 `android/` 目录下跑。**PR 门禁只有两条命令**（`12 §1.5.2`）；其余命令是本地排查或分层门禁的一部分。
> **标 `M0-6 起` / `M1 起` 的命令在对应里程碑落地前不存在**（`tools/**`、`wisdomGate`、16 条 `wisdomCheck*`、`:demo`、`:benchmark`、`.github/`、`gradle/verification-metadata.xml`、`src/debug/` 今天都不在仓库里）——**不要把它们写进出口报告**，也不要因为"命令找不到"误判环境坏了。先看 §5.0。

### 5.0 命令现状表（**先读这张表**：哪些今天可跑、哪些还没有）

| 命令 / 交付物 | 交付时点 | 今天（2026-10-05）**该用什么** |
| --- | --- | --- |
| `:wisdom-ui:apiCheck` / `assembleRelease` / `assembleDebug` / `testDebugUnitTest` / `compileDebugKotlin` / `build` / `apiDump` / `generatePomFileForMavenPublication` | **现成**（`settings.gradle.kts:25` 有 `:wisdom-ui`） | 直接跑 |
| `./gradlew wisdomGate` + 16 条 `wisdomCheck*` + `tools/**` | **M0-6 起** | 用 §5.2 的 4 条现成任务 + `apiDump && git diff --exit-code` 代替；`wisdomGate` 不存在时**不算门禁失败** |
| `.github/workflows/**`（CI 三层） | **M0-6/M0-9 起** | 本地手跑 §5.2 的现成命令 |
| `gradle/verification-metadata.xml` | **M0-7 起** | 先跑 §5.5 的生成命令，再入库 |
| `src/debug/**`（预览）、`src/test/resources/robolectric.properties` | **M0-7 / M1 起** | 今天没有预览与 Robolectric 配置 |
| `:demo`（`assembleRelease` / `installDebug` / 启动命令） | **M1 起** | 今天会报 `Project ':demo' not found`；用库单测 + 预览替代 |
| `:benchmark`（Macrobenchmark 首个 JSON） | **M1 之后** | 无——**不得**写"已有基准数据" |
| 六态截图插件任务（`validateDebugScreenshotTest`） | **M1 起**（任务名待实测 V14） | 无 |
| `-g ./.gradle-home` + `tools/init-mirror.gradle` | **M0-10 起** | 用默认缓存；离线需预热 |

### 5.1 PR 必过（阻塞合并）

```bash
# ① 复合门禁（M0-6 起；今天不存在）：G1–G13 + apiCheck + assembleRelease/Debug + testDebugUnitTest + ktlint/detekt/lintRelease
./gradlew wisdomGate

# ② 基线不漂：apiDump 后必须零 diff（这一刻意用 shell，Gradle 里没有 git 语义）
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
```

### 5.2 `wisdomGate` 里有什么（G1–G13，逐条可单独跑；**前 4 条现成，其余 `M0-6 起`**）

```bash
./gradlew :wisdom-ui:apiCheck                    # ABI 基线
./gradlew :wisdom-ui:assembleRelease             # release 编译（+ G8 的负例生产者）
./gradlew :wisdom-ui:assembleDebug               # debug 编译（+ G8 的正例生产者）
./gradlew :wisdom-ui:testDebugUnitTest           # 单测（只跑 debug 变体，PR 口径）
./gradlew :wisdom-ui:wisdomCheckPackageDeps      # G1 依赖方向 + 包图无环 + **m3 import 计数 == 1**
./gradlew :wisdom-ui:wisdomCheckTokenLiterals    # G2 令牌字面量（符号级白名单）
./gradlew :wisdom-ui:wisdomCheckMainNoPreview    # G3 src/main 不得出现 @Preview / ui-tooling
./gradlew :wisdom-ui:wisdomCheckDeprecatedBaseline  # G4 deprecated 成员数不增长
./gradlew :wisdom-ui:wisdomCheckStabilityFields  # G5 $stable 存在性（api 文件）
./gradlew :wisdom-ui:wisdomCheckComposeMetrics   # G6 inferredUnstableClasses == 0（文件缺失即 fail）
./gradlew :wisdom-ui:wisdomCheckGeneratedSelfProof  # G7 banner hash 自证
./gradlew :wisdom-ui:wisdomCheckReleaseSymbols   # G8 release AAR 预览符号 == 0 且 debug > 0
./gradlew :wisdom-ui:wisdomCheckPublicTypes      # G9 公开类型正向白名单（U4 的真正机器化）
./gradlew :wisdom-ui:wisdomCheckInsetsWhitelist  # G10 只允许 6 个组件碰 WindowInsets
./gradlew :wisdom-ui:wisdomCheckThemeDefaults    # G11 theme 默认参数不得是构造调用
./gradlew :wisdom-ui:wisdomCheckSingleCallSite   # G12 wdSystemGestureExclusion 调用点 == 1
./gradlew :wisdom-ui:wisdomCheckLiteralLanguage  # G13 L-B 标点/词序（M0–M2 warning，M3 起 error）

# 手工判据（G1-③ 的本地形态；不属 gate 但 review 要贴）
grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin | tee /tmp/m3.txt | wc -l   # 必须 == 1
cat /tmp/m3.txt   # 必须只含 foundation/theme/WDMaterialScheme.kt
```

### 5.3 nightly（`M1 起`；只报不拦，M4 起 B1/B2/E2 转门槛）

```bash
./gradlew :wisdom-ui:validateDebugScreenshotTest    # 六态截图（任务名以 M1 首日实测为准：V14）
./gradlew :wisdom-ui:testDebugUnitTest --tests '*WDLineBoxTest*'
./gradlew :demo:assembleRelease                     # R8 冒烟（唯一能证明不误删的手段）
ls -l demo/build/outputs/mapping/release/seeds.txt  # CI 产物存档（-printseeds）
tools/size-report.sh --dex                          # E2 + m3 dex 占比（M1 出口的两个数字）
# 口径提醒：E2 / E1' / F1 在**集成 APK**上测（:demo release）；AAR 只喂 E1 字节趋势（12 §1.5.6）
```

### 5.4 发布前（阻塞发布；**POM 两条现成**，其余 `M1 起` / `M4 起`）

```bash
./gradlew :wisdom-ui:publishToMavenLocal            # 发布物冒烟（M4；V4 未闭合）
./gradlew :wisdom-ui:generatePomFileForMavenPublication                 # POM 版本 == catalog 的 wisdom 键
./gradlew :wisdom-ui:generatePomFileForMavenPublication -Pwisdom.version=1.0.0
# F1（R8 后 dex 增量）：对象 = **集成 APK**（:demo release，R8 + 资源收缩），不是库 AAR（AAR 未过 R8）
apkanalyzer dex packages demo/build/outputs/apk/release/demo-release.apk   # 【待实测】APK 路径在 M1 建 :demo 时定稿
# 口径：AAR 只用于 tools/size-report.sh 的 E1（字节趋势）；E2 / E1' / F1 一律在集成 APK 上测（12 §1.5.6 + 30 §3.4）
```

### 5.5 常用本地命令

```bash
./gradlew :wisdom-ui:compileDebugKotlin          # 只编库
./gradlew :demo:installDebug                     # 组件迭代主回路（M1 起）
./gradlew :wisdom-ui:build                       # 编译 + 单测 + check（含 apiCheck）
./gradlew --write-verification-metadata sha256 help   # M0-7：生成依赖校验元数据（入库）
./gradlew -g ./.gradle-home -I tools/init-mirror.gradle :wisdom-ui:assembleRelease   # 隔离缓存 + 可选镜像（仅本地排查）

# 真机验证（改哪条 → 断言什么，12 §1.6.1）
adb shell settings put system font_scale 2.0        # U7 上界：不截断/不重叠/行数变化
adb shell settings put system font_scale 1.3        # 中间档：横排→竖排降级（阈值来自令牌）
adb shell settings put global animator_duration_scale 0   # 常驻动画静止；rememberWDInfiniteSpec() == null
adb shell wm density 320                            # 重建 Activity → 验 Saver（WDBottomSheetState/LazyListState）
adb shell am start -n io.github.wlunc.wisdom.demo/.DemoActivity   # ⚠️ applicationId 与 Activity 名为**占位示例**，M1 建 :demo 时定稿（见 §7 同条）
```

---

## 6. 冻结值清单（用户 6 条决策后的**最终值**，不得自行改写）

> 这一节的值**已冻结**。任何"我觉得 48 更好/44 更对"的想法都要走变更流程（§9.4），不允许在代码里悄悄偏离。
> **逐行出处**：本表只保留**值**；每一行的来源（用户决策 / 跨端契约 / 计划口径 / 本端规格章节 / 设计仓）已整体迁到 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) **§17 冻结值来源对照表**（无预算约束）。

| # | 项 | **最终值** |
| --- | --- | --- |
| 1 | **行高（可见内容）** | **`size.row-height.comfortable = 60`**、**`size.row-height.compact = 44`**（**单值键**，两端同值） |
| 2 | **Android 行布局盒 / 热区** | **48 = 44 可见内容 + 上下各 2dp 透明内边距**，由 `Modifier.wdTouchTarget()` 实现；**热区 = 布局盒、不覆盖相邻行**；代价 = Android 纵向节奏比 iOS 疏 4dp（设计已接受） |
| 3 | 行高的统一/自由划分 | **U** = 两端**可见内容高度 44 ± 0.5**；**F** = 行距（Android 48 / iOS 44）—— **F 号已挂号：`F51` = 列表行距差异（Android 48 / iOS 44）**；AR-67 原断言 `(compact,1)==48±0.5`、`(compact,2)==76±0.5` **保留**（"行高"按布局盒解释），另**新增**"可见内容高度 == 44 ± 0.5"断言 |
| 4 | **U12 色槽数** | **32 槽位**（新增 `text.disabled`）；浅深成对；计数断言随生成物（**类型名 = `WDColorSlot`，32 条**，见 `ARCHITECTURE.md §4`） |
| 5 | **换肤（层一）** | 多套生成 scheme + **运行时选择**：`schemes: {light, dark, …}` + 生成器 `--schemes`；Android 侧入口 = `WDTheme(colors = …)`；**不支持**运行时任意 `token.json` / 服务端下发 / **逐槽位任意覆盖**；切换 = 换已生成的 scheme、**不重启进程**、**不进高频路径**（`staticCompositionLocalOf` 值变化 = 整树重组） |
| 6 | **弹簧 canonical** | 真源字段 = `response` + `dampingRatio`（令牌里由 `dampingFraction` **改名**为 `dampingRatio`）；`stiffness = μ·(2π/response)²`；**μ = 1.0** → gentle `246.74` / snappy `503.65` / bouncy `223.72`；**禁用 `massFactor`**（统一 `stiffnessMultiplier(μ)`） |
| 7 | **图标** | 契约只统一 **44 条语义名 + `mirrorsInRTL`**；Android **调用方注入 `ImageVector`**（`LocalWDIcons`，默认空集）；**库内零图标资源**（不引 `material-icons-extended`、不打包 drawable）；`mirrorsInRTL` 是**语义标记**，镜像以 `ImageVector.autoMirror` 为准；缺图**保留占位尺寸** |
| 8 | **触控双键** | `size.touch-target-min-ios = 44` / `size.touch-target-min-android = 48`；**每端只生成本端常量** `WDSize.touchTargetMin`；**一次性改名、不留过渡键**；不得用它做视觉尺寸 |
| 9 | **minSdk** | **24**（保持）；玻璃在多数设备走"默认降级"路径 |
| 10 | **material3 收缩** | `material3 → implementation`；**BOM 保留在 `api`**；`tooling-preview → debugImplementation`；**公开签名不得出现 m3 类型**（G9 正向白名单）；**全库 m3 import 计数 == 1**（只能 `foundation/theme/WDMaterialScheme.kt`）；**体积收益 = 0 直到拆 artifact**（拆的判据 = m3 dex 占比 >50% **且** E2 >400 KB，或真实消费方 issue） |
| 11 | 字段盒高 | `size.field-height = 46`；所有变体/状态高度 = 46 ± 1dp（状态不得改变高度） |
| 12 | 弹层几何 | detent `Half = 0.5` / `Large = 0.92`；`max-width = 480`；`corner-radius = 32`；handle `36 × 5`、offset `8`；类型名 `WDBottomSheetDetent{Half, Large}` + `WDBottomSheetDetents{All, Fixed}` |
| 13 | 动效杂项 | `motion.duration.reduced = 150`；`motion.component.press-overlay-alpha = 0.06`；`motion.component.layout-break-font-scale = 1.3`；`WDComponent` **12 键**（10 个 `component.*` + 上述两键）；`WDToast.durationMillis = 3000`（支持 ≥5000，真源键 `durationMilliseconds`） |
| 14 | 阴影出口 | `WDElevation`（`level0/1/2/3/brand`）→ `Modifier.shadow` 映射表 5 行（**近似映射**，登记 F43）；**禁止**为多层阴影引入离屏层 |
| 15 | **受控值参数名** | 14 行 Android 改名**已定**：`text`（TextField/SearchField）、`on`（Switch）、`selection`（Radio/ListSection/TabBar/AssigneePicker）、`value`（ProgressBar/Ring）、`date`（DatePicker）、`presented`（Alert/BottomSheet/ActionSheet/Toast）；**`WDCheckbox = checked`**（iOS 侧改）、**`WDBanner = visible`** 两个例外 |
| 16 | 组件命名 | `WDBottomSheet` / `WDBottomSheetState`（+ `saver(...)`）/ `rememberWDBottomSheetState`；`WDSheet` 这个名字**不存在** |
| 17 | 无障碍网关 | `WDHaptics`/`WDAnnouncer` = `interface` + `remember*()` + 可注入 local（`null` 默认 = 平台实现，**不是 no-op**）；M2 只公开 `press`/`toggle` |
| 18 | 库内零资源零文案（L-B） | 无 `res/`、无字体、无 `strings.xml`；文案全部调用方传入；`WDSemantics.join(separator, vararg parts)` **无默认分隔符**（与 iOS 同批） |
| 19 | **U5 行盒公式（必须统一）** | **`renderedLineBox = max(设计盒高 × 缩放, natural(script))`**；默认档断言 **`abs(rendered − max(设计, natural)) ≤ 1px`**（Android 容差；iOS = `0.5pt`，**差异登记 F42**）；放大档 **`≥ ⌈natural × 行数⌉`**（不裁切）；**禁止** `Mode.Fixed` / `lineHeightMultiple` / `@ScaledMetric` / 固定高度包文字 / 正文 `maxLines = 1` |
| 20 | **U6 状态优先级全序串（必须统一，逐字照抄）** | **`disabled > loading > pressed > focused > hover > default`**；三条派生：① disabled 最高且**吞输入**；② loading 忽略 `action` 但**留在无障碍树**（**不用** `enabled=false` 表达）；③ focused·hover 是**叠加维度**（不参与互斥）；验收 = **同一串输入 → 同一可见状态**（两端单测） |
| 21 | **U10 玻璃档位的输入/输出集合（必须统一）** | **输出类型 = `WDGlassResolution{Opaque, Glass, GlassStrong}`**（两端同列同名；历史名 `WDGlassEffective` **已作废**）；**必选输入** = `WDTextLevel{Primary, Secondary, Tertiary}` + `WDAppearance`（`colorScheme`/`contrast`/`reduceTransparency`/`differentiateWithoutColor`/`reduceMotion`）+ `WDGlassCapabilities` + `WDEffectsBudget`；**Android 额外输入 = `WDGlassLevel{UltraThin, Thin, Regular, Thick, Tinted, Sheen}`（F47，**不写进** U10 的 Inputs 句）**；签名 `WDGlass.resolve(textLevel, appearance, capabilities, budget) → WDGlassResolution`；四步判定见 `ARCHITECTURE.md §8` |
| 22 | **三枚举（M2/M4 首批要用）** | `WDCardStyle{Elevated, Outlined, Glass}`（M2 `WDCard`，默认 `Elevated`）；`WDToastVariant{Neutral, Success, Warning, Danger}`（M4）；`WDBannerVariant{Info, Warning, Danger}`（M4）；**契约比较用归一化后的规范名**（Kotlin UpperCamel ↔ Swift lowerCamel，大小写差异 = 已登记 DIR-1） |
---

### 6.1 设计规则条文（DF-02 / DF-03 / DF-04）

> 与 `12` 冲突时**以 `12` 为准**（本节只放判据）；**行距口径见 §6 #1–#3（`F51`）**：U = 两端可见内容 44 ± 0.5，Android 48 = 布局盒，**不得压回 44**。设计侧待给值 16 项**不复抄**（见 [`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) **§7 + §7.1**）；**设计待给值随令牌走（DF-17）**：三张阶梯（圆角 / 间距 / 字号）与图标尺寸·线宽阶梯随 M0-1 冻结；**密度档三规则、减弱动态四类点名、无"降低透明度"开关的含义**见 `docs/ARCHITECTURE.md` §8.3 / §7.1 / §5.1。**展开论证与长表已下沉**：玻璃 ✅/❌ 实算 = `docs/ARCHITECTURE.md` **§8.1**；配额"违反时怎么办"与 API = **§8.2**；对比度不达标组合与验收细目 = **§9.1a**；修订台账 = **§16**。

**6.1.1 玻璃档位 × 文字可用（DF-02）** —— 六档真源 = 设计仓基础文档 §7.2；推导与生成期断言见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) **§8.1**

- `{ultraThin, thin, regular}` → `surface.glass`（`WDGlassLevel.{UltraThin,Thin,Regular}` → `Glass`）：**浅色端只放 `primary`；深色端任何字阶都不上**。
- `thick` → `surface.glass-strong`（→ `GlassStrong`）：浅色端 `primary`/`secondary`/`tertiary` 全可用；**深色端只放 `primary`**。
- **`tinted` / `sheen` 不作文字载体**（强调容器 / 装饰），**不得并进 `GlassStrong`**；`WDGlassResolution` 只是**可读性结论**。
- **四条规则**（照抄设计仓无障碍 §5.2 的 1–4）：① 浅色 `glass` 只放 `primary`，其余换 `glass-strong`；② 深色端任何字阶不上 `glass`，`glass-strong` 只放 `primary`；③ **悬浮 Tab 栏必须 `glass-strong`**，**深色端改用不透明表面**（设计仓玻璃补充 §7 **方案 A**）；④ **`status.*` 不上玻璃**。
- **降级四步**（唯一实现点）：`allowBlur=false`/`reduceTransparency` → `contrast=Increased` → 无模糊能力 → 按档位；图见 `docs/ARCHITECTURE.md` **§8**。

**6.1.2 效果配额 7 条（DF-03）** —— 上限照抄设计仓性能与发布文档 §1.2；逐条处置与机器化 API 见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) **§8.2** + `12 §3.5.3`（AR-74）

① 玻璃模糊面 **同屏 ≤1、列表项内 0**｜② `wash` **每屏 1 处、禁动画**｜③ `sheen` **每屏 ≤1**（仅强调卡片）｜④ 骨架微光 **同屏 ≤6**（仅加载态）｜⑤ 阴影：列表项 `e0`/`e1`、**`e3` 每屏 ≤1**｜⑥ 进度 / 下拉环 **每屏 ≤1 个动画环**｜⑦ 触觉 **同一次操作 1 次**。**超出即降级**，配额 = 设计评审**可勾选项**。

**6.1.3 对比度门槛（DF-04）** —— 照抄设计仓无障碍 §5.1 + §5.2 规则 6

| 内容 | 下限 | 验收口径 |
| --- | --- | --- |
| 正文 | **4.5:1** | **取最不利位置的背景色**（不取平均）；玻璃取**最暗 / 最亮内容的合成色**；nightly 自算对比度（`12 §1.5.6`） |
| 大字号 / 图形·图标·控件边界 | **3:1** | 同上（大字号矩阵；轨道 / 描边 / 进度填充 / `border.hairline`） |
| 禁用态 | 不适用（**可辨识即可**） | 40% 不透明度（`state.*`，F45） |
| 焦点光环 | **3:1（对相邻色）** | `text.brand` 18–32% 叠加 |

**玻璃上的已知不达标组合**（浅色 `glass` 的 `secondary`/`tertiary`、**深色 `glass` 连 `primary`**、深色 `glass-strong` 的 `secondary`/`tertiary`）**逐值实算与推导见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) §9.1a**（设计仓无障碍 §5.2 表 / 玻璃补充 §4）。
**高对比度**：`rememberWDHighContrast()` **API 34+**（**`<34` 恒 `Standard`**，`12 §3.2.3`/`LR-03`）→ `contrast=Increased` 直接 `Opaque`。**验收**：① nightly 最不利取色；② 每套 scheme 账目进 `contracts/contrast.json`（`30 §5.8`）；③ `D-13` 未给值前记"未验证"（§7）。

## 7. 【未验证】清单（**不得写成"已通过"**）
> 规则（`30 §3.4` 的门禁数字口径 + `12 §9.1`）：下表任一项在**实测回填前**，出口报告、README、CHANGELOG、PR 描述里**一律记为"未验证"**，并给出归属与时点；**不得**写"已达标 / 已通过 / 已验证"。

| # | 未验证项 | 归属 | 阻塞点 | 备注 |
| --- | --- | --- | --- | --- |
| V3 | ktlint / detekt / Compose lint 的**规则集与告警内容**（含 `.editorconfig` 键名） | android-dev | **阻塞 M0-6** | M0 首次接入当日校准 |
| V4 | `publishToMavenLocal` + `javaDocReleaseGeneration`（Dokka 版本） | android-dev | 阻塞发布前（M4） | `08 §3-3` |
| V10 | **E2（集成 APK 差）+ m3 dex 占比**：当前仓库无 app 模块，一条都没数 | android-dev | M0 只出脚本+骨架；**数字在 M1 出口** | `08 §3-3` 相邻 + U4 量化 |
| V1 | 真机 `fontScale 2.0` 观感（不截断/不重叠/层级保持） | 两端 | M1 | = `08 §3-5` |
| V12 | **CJK 行盒 vs 设计行高**：zh/en 的 `natural` 实测值未测 | android-dev + 设计 | **阻塞 M1 行盒断言** | fixtures 缺失即 fail |
| V2 | 触觉常量映射 + `assertive` 播报的近似实现 | android-dev | M4 前 | = `08 §3-4` |
| V5 | `rememberWDMotionScale()` 在真机 `animator_duration_scale=0` 的**数值与首帧时序** | android-dev | 不阻塞（机制已由字节码闭合） | 只剩真机复验 |
| V6 | 库模块的 Live Edit 行为 | android-dev | 不阻塞 | 迭代主回路 = 预览 + `:demo` |
| V7 | `WDTextField` 自研路线（`BasicTextField` + DecorationBox）的 IME/选区/无障碍达标度 | android-dev | **阻塞 M2** | M2 首件 |
| V8 | `isHighContrastTextEnabled()` 的真实行为 + "组合期读一次、**非响应式**"是否可接受 | android-dev | M4 前 | 系统开关变化不触发重组 |
| V9 | 自研 `wdTouchTarget` 在 `LazyColumn` 相邻行下的实测（重叠/误触） | android-dev | 不阻塞 | 替代旧稿的 `minimumInteractiveComponentSize` 路线 |
| V11 | `Modifier.animateItem()` 在 `WDListRow` 场景的效果与 key 要求 | android-dev | 不阻塞 | — |
| V13 | "全库 m3 import 计数真能收敛到 1" | android-dev | M0 会给答案 | G1-③ |
| V14 | screenshot 插件的**源集名与任务名** | android-dev | **阻塞 M1 nightly** | M1 首日 `./gradlew :wisdom-ui:tasks --all \| grep -i screenshot` |
| V15 | `WDBottomSheet` 展开态下 back 的语义（先收档还是直接关闭） | android-dev | M4 前 | — |
| V16 | 阴影近似映射 + `press-overlay-alpha` 的**观感** | 设计 + android-dev | M2/M4 **视觉评审关** | — |
| V17 | M0 出口⑨ 的两个数字能否在 M1 出口按期给出（依赖 `:demo`） | android-dev | 不阻塞 | — |
| V18 | **C-15 的 14 个受控值名尚未落 `contracts/<component>.yaml: params[].name`** | android-dev + 架构师 | **阻塞 M2 批前签名冻结** | M0-5 落库 |
| — | **基准 JSON**：`:benchmark` 的**首个结果 JSON 尚不存在** | android-dev | M1 出口 | 不得写"已有基准数据" |
| — | **`:demo` 的 applicationId / `DemoActivity` 名**（§5.5 的启动命令是占位示例） | android-dev | M1（建 `:demo` 时） | 定稿后回写本文件与 [`docs/SPEC.md`](docs/SPEC.md) §1.6.1；A-06 |
| — | 门禁**耗时**（PR ≤10 min / ≤5 min warm）是**目标值不是实测值** | tech-lead | — | `30 §3.4` 明文 |

---

## 8. 与规格文本的差异（P7–**P12** / V20 回写台账）

> **口径来源（统一写法）**：以 **[`docs/DEV-PLAN.md`](docs/DEV-PLAN.md) §5 / §6 为准**；本端规格（[`docs/SPEC.md`](docs/SPEC.md)）的对应条目按 **§6 的 P7–P12 回写**（**P11/P12 由 `t53` 登记**；`30 §5.9` 的标题仍写 P7–P10，以 §5.9 表尾的「P7–P12」说明为准）。
> **读法**：下表的"**最终值**"列是**施工依据**；"规格现文（**回写前**快照）"列是**回写之前**的文本（时点 = 2026-10-05 22:25 之前），只用于核对"改了什么"——**用章节号锚点，不写会漂移的行号**；**不要**把该列当成 `12` 的现状（现状见本节下方"回写状态"表）。
> **引用纪律（A-02 ③）**：**引用他文件的行号前先 `grep -n` 现查**；本文件与 `ARCHITECTURE.md` 优先引用**章节号**；确需给行号时必须同时给可 `grep` 的锚点串。
> **AR-67 的写法（必须照抄口径）**：案 B 下 AR-67 是**补充解释**，不是取代——它原有的两条断言 `(compact,1) == 48dp ± 0.5`、`(compact,2) == 76dp ± 0.5` **原样成立**（其中"行高" = **布局盒 48**），**新增**一条"紧凑单行**可见内容**高度 == 44 ± 0.5"的断言。

| # | 事项 | 规格现文（**回写前**快照；**章节号锚点**，行号会漂移） | **最终值（施工依据）** | 执行者 / 时点 |
| --- | --- | --- | --- | --- |
| **P7** | 行高与热区（`compact`） | `12` **§0.3-Δ4**（行高双键）、**§2.2③**（行高公式 / 4 格矩阵 / AR-67 段）、**§3.1.2**（M0-1 清单第 4/5 行）、**§3.1.3**（生成物 `WDSize`）、**§3.3.1**（契约句）、**§7.1-M0-4**、**§7.3**（自证表） | **单值键** **`size.row-height.compact = 44`**（两端同值 = **可见内容**）；**Android 布局盒 = 48 = 44 + 上下各 2dp 透明内边距**（`Modifier.wdTouchTarget()` 实现，热区 = 布局盒、**不与相邻行重叠**、**不新增令牌键**）；`size.row-height.comfortable = 60`（两端同值）；**U = 可见内容 44 ± 0.5**、**F = 行距（Android 48 / iOS 44）**；AR-67 两条原断言保留 + 新增"可见内容 44 ± 0.5" | android-lead + 架构师 / **M0-1 冻结前；最迟 M2 批前签名冻结** |
| **P8** | U12 色槽数 | `12` **§3.1.1**（运行时变量轨）、**§3.1.3**（生成值区 / 槽位计数）、**§3.2.2**（公开构造器参数数）、**§4.2-U12**、**§7.1-M0-4** | **32 槽位**（新增 `text.disabled`）；浅深成对；**槽位计数断言 == 32**（`WDColorSlot`）；生成物 `WDColors` **32** 个 `public val`，`api/wisdom-ui.api` 合成构造器参数 +1 | android-lead + 架构师 / **M0-1** |
| **P9** | `schemes` 维度 | `12` 原**全文无** `schemes:` 条目（只在 §3.2.2 以散文提到"方案 A"）；回写后落 **§3.1.2a**（新增小节） | `schemes: {light, dark, …}` + 生成器 `--schemes`；**切换 = 换已生成的 scheme**、触发重组/重算、**不重启进程**、**不进高频路径**；**不支持**运行时任意 `token.json` / 服务端下发；口径 = `30 §5.8` | ios-lead + 架构师（本仓按此施工）/ **M0-1** |
| **P10** | 设计文档同步 | `wisdomdesign/docs/09-layout.md` §触控目标（"行高 44 时热区就是整行"） | 改为"**整行 = 48 布局盒（可见 44 + 上下各 2dp 透明内边距）**；热区 = 该布局盒、不覆盖相邻行；**可见内容两端一致 44**" | 设计 + 架构师 / **M0-1**（不在本仓） |
| **V20** | M2 批次件数 | `12` **§7.2**（"M2 8 件基础组件"） | **11 件**（`WDBadge`/`WDAvatar`/`WDDivider` 进 M2）；口径统一在 **M1 出口前**完成（`30 §8.2-P2`） | android-lead + ios-lead / M1 出口前 |

**回写状态（逐项实测，2026-10-05；证据可复制复现）**

| 项 | 状态 | 证据（`cd` 到工作区根后执行） |
| --- | --- | --- |
| **P7** | **已完整回写**（评审 `61` 的 A-02 快照写于 22:25，是"**P7 已部分回写**"的中途状态；t42 于 22:28 补齐生成值区 / AR-67 台账 / 自证表） | `grep -n 'rowHeightCompact\|行高双键\|布局盒\|可见内容' docs/SPEC.md` → 单值键 `44` + "布局盒 48" + "可见内容 44dp ± 0.5"三处齐；旧形态（`rowHeightCompact = 48.dp`、"行高双键生成值"）**零命中** |
| **P8** | **已回写（t42）** | `grep -c '32 槽位' docs/SPEC.md` > 0；`grep -c '31 槽位\|31 语义色\|31 参' …` = **0**；另有"槽位计数 == 32"断言行 |
| **P9** | **已回写（t42）** | `grep -n 'schemes' docs/SPEC.md` → §3.1.2a 标题 + 表体 + §3.2.2 指引句；`--schemes` 命中 |
| **P10 / V20** | P10 不在本仓（设计 + 架构师）；**V20 已随 t42 对齐** | `grep -c '8 件基础组件' docs/SPEC.md` = **0**；`11 件` 命中 |

> **⚠️ 本节引用的行号以 `docs/SPEC.md` 当前版本为准，锚点以章节号为准**；`12` 被回写或重排后，请用上表的 `grep` 命令现查，**不要照抄历史行号**。

---

## 9. 提交 / 分支 / PR / 版本治理

### 9.1 提交信息与分支

- **Conventional Commits**：`<type>(<scope>): <摘要>`，`type ∈ feat|fix|perf|refactor|chore|docs|test|build|ci`，`scope` = 模块或组件。
- 提交正文两行必填：`变更集: tokens: vX.Y.Z`（碰令牌/生成物时）+ `ABI: 无变化 | 只增 | 签名变化`（碰 public 签名时）。`12 §1.4`
- **trunk-based**：`main` 受保护；短期分支 `feat/<组件>`、`fix/<…>`、`chore/<…>`、`tokens/<版本>`。
- **必须 rebase merge（禁止 squash）**：M0-3/M0-4 明确要求"纯移动"与"语义"**两个提交**可独立审计，squash 会把两次 ABI 变化压成一次。`12 §1.4`

### 9.2 PR 模板（六段，逐段可勾）

1. **变更类型**（组件 / 令牌 / 基建 / 契约）；2. **变更集标识**；3. **影响面** —— 必须贴 **`apiDump` 的类型级摘要**（新增/删除/改签名各几行），写"ABI: 无变化"要由 `git diff --exit-code -- wisdom-ui/api/wisdom-ui.api` 的输出背书；4. **门禁输出**（`wisdomGate` + 基线漂移检查）；5. **证据**（六态截图 / 语义树断言 / 预览矩阵）；6. **迁移片段**（仅 Breaking）。

**Review 人数矩阵**：碰 `api/wisdom-ui.api`、`contracts/**`、CI/发布配置 → **2 人**，其中 1 名必须是 `android-lead` 或 `tech-lead`。`12 §1.4`

### 9.3 `apiCheck` 由红转绿：A1 + A3 同提交的 6 步序列（M0 出口项）

> **当前状态（必须写清）：`apiCheck` 是红的。** 实测形态：`api/wisdom-ui.api` 里 `WDColors` 只有 **27** 个 getter，而源码有 **31** 个 `public val` → 缺 `surfaceTint`、`textOnLightPrimary`、`textOnLightSecondary`、`textOnLightTertiary`（`11 §0.2`）。不转绿 ⇒ 之后**每个 PR 恒红**，M2 起无门禁可依（`30 §5.2`）。

| 步 | 动作 | 落点 | 出口判据 |
| --- | --- | --- | --- |
| **1** | **M0-1**：令牌 D1 清单**一次改完**（21 行，含 D6 键名 `dampingFraction→dampingRatio`、行高按 §6 的案 B、触控双键 44/48、`WDComponent` 12 键、`elevation` 出口、`press-overlay-alpha`、`layout-break-font-scale`、**`schemes` 维度 + 32 槽位**） | `wisdomdesign/`（设计仓，**本仓不可写**） | `node tools/token-build/build.js --check` exit 0 + 对比度断言 + `motion.spring.*` 键集合 == `{response, dampingRatio}` |
| **2** | **M0-2**：生成器改造（`generated/` 输出、banner `version+sha256`、`WDTokensVersion`、派生 `lineHeightRatio`、缺字段 emit 0、静默 `mkdirSync` → `exit 1`、`--schemes`） | `wisdomdesign/tools/token-build/build.js` | 错误路径用例（目录缺失）必须 exit 1 |
| **3** | **提交 1（纯移动）**：`Generated/`→`generated/` + 包名迁移 + 手写文件分层搬家 + 重跑生成器 + **`apiDump` 同提交** | `android/…/foundation/**`、`api/wisdom-ui.api` | 该提交 `apiCheck` 绿；diff 只含包名重写（`sort` + `sed 's/generated/Generated/'` 对拍） |
| **4** | **提交 2（语义）= A1 + A3 同提交**：**A1** 用 `apiDump` 补齐基线（31 个 getter + 新构造器形状）；**A3** `WDTextStyle` 4 参 + `@Immutable` + `class` + `internal constructor`（保留 `equals/hashCode`）；生成类 `internal constructor`；`toTextStyle()` 迁 `foundation/typography/WDTextMetrics.kt`（**ABI 所有者从 `WDThemeKt` → `WDTextMetricsKt`，必须同批 + 记 CHANGELOG**）；触控 44→48；弹簧 μ=1.0；行高按 §6 | 生成物 + `api/wisdom-ui.api` | **`:wisdom-ui:apiCheck` 由红转绿**（`08 §2-8`）；人审 diff 只出现预期增删 |
| **5** | **提交 3（依赖面）**：`material3 → implementation`、`tooling-preview → debugImplementation` | `wisdom-ui/build.gradle.kts` | `api/wisdom-ui.api` **零 diff**（有 diff = 有 m3 类型泄露 → 回退）+ G9/G1 绿 |
| **6** | **门禁落地**：`wisdomGate`（G1–G13）+ CI 两行 + 首次生成 `build/compose-metrics/baseline.json` 并入库 | `build.gradle.kts`、`.github/workflows/ci.yml`、`tools/update-metrics-baseline.sh` | 首次全绿；G6/G13/截图开关有**真实 task**（不是注释） |

> **A1 与 A3 必须同提交**的理由：两者都改 `api/wisdom-ui.api`，拆开会出现"一次提交里基线既缺 getter 又混着形态变更"的不可审计 diff（`08 §2-8` + `12 §7.1` M0-3/M0-4）。

### 9.4 版本治理与发布时序

- **三层版本**：令牌数据（`tokens` 变更集）→ 组件契约（`contracts/`）→ 库版本（`wisdom`）。三者不混用。`07 §0-8`
- **库版本参数化**：`-Pwisdom.version` > `WISDOM_VERSION` 环境变量 > `libs.versions.toml` 的 `wisdom` 键；验收 = 两条 POM 命令（§5.4）。`12 §1.2.1`
- **发布顺序（不可颠倒）**：令牌冻结并打设计仓 tag → 生成 → **两端提交（含 `apiDump`/符号快照）并全绿** → 两端打 tag。`07 §2.2-REL-5`
- **tag 只增不改**：禁止 move / delete / re-tag；打错版本只能发新版本并在 CHANGELOG 标注废弃版本号。`07 §2.2-REL-5`
- **改冻结值 = 走变更流程**：改 §6 的任何一个值都要"先改真源（令牌/契约）→ 再同步两端副本 → 记 CHANGELOG"；**禁止**在本仓代码里先改、事后再补文档。`12 §1.4`
- **`$default` 规则**：给**既有** public 函数**加带默认值的参数** = 二进制不兼容（`$default` 合成方法签名变化 → 老消费方 `NoSuchMethodError`）→ 必须走 major 或改为新增重载；CI 用 `grep -c '\$default'` 前后比对。`12 §2.9.3` + `12 §1.5.4-F`

---

## 10. 反模式与禁止事项（**逐条都有机器检查或 review 项**）

| # | 禁止 | 为什么 | 谁拦 |
| --- | --- | --- | --- |
| 1 | **裸值**：`Color(0x…)`、`#RRGGBB`、裸 `16.dp`/`12.sp`/`100ms` | 令牌是唯一真源（`wisdomdesign/README.md:94`） | **G2**（符号级白名单见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) §11） |
| 2 | **绕过 `WDGlass.resolve` 自己选档**；组件自己判断系统能力/高对比度 | U10：组件不得自己选档 | review + 契约断言 |
| 3 | **用扩热区手段覆盖相邻行**（把 48 硬塞进 44 的视觉盒并溢出） | 案 B：热区 = 布局盒 48，**不越界**；L7 | 热区断言 + review |
| 4 | **手改生成物**（`foundation/generated/**`）、**手改 `api/wisdom-ui.api`** | 会被下一次生成/`apiDump` 覆盖；基线失真 = 兼容性失真 | **G7** + `apiDump && git diff --exit-code` |
| 5 | 在 `Modifier` 工厂里读主题/local（`Modifier.composed`） | `07 §1.4` 反模式 2：每帧比较失效 | review（`wdGlass` 只接参数） |
| 6 | `pointerInput` 自造按下态 / 第二个 `interactionSource` / 覆写 `LocalIndication` | C13–C15：按下源唯一 | review + lint |
| 7 | 用 `clickable(enabled = false)` 表达 **loading**；手写 `semantics { disabled() }` | loading ≠ disabled（U6：loading 留在树）；`disabled` 语义由 `clickable` 自带 | 语义树断言（`assertIsNotEnabled`）+ review |
| 8 | 组件读 `isSystemInDarkTheme()`；组件读静态令牌入口（`WDColorSchemes`/`wdLightColors`/`WDPalette`/`WDDerived`） | F3：组件只读 `WDTheme.colors` | **G1/G2**（U17） |
| 9 | 把 `Modifier.blur` 当背景模糊；`renderEffect`/`ColorMatrix`/离屏层 | P-1：`Modifier.blur` 模糊自身内容；效果配额禁离屏层 | review + 配额单测 |
| 10 | `Mode.Fixed` / `lineHeightMultiple` / `@ScaledMetric` / 固定高度包文字 / 正文 `maxLines = 1` | U5/U7：行盒 = `max(设计×缩放, natural)`、不裁切 | `WDLineBoxTest` + review |
| 11 | `remember` 缓存派生值（`derivedStateOf` 除外）；`remember` 的 key 写每次都变的表达式；给列表项加模糊 | 会掩盖真实重组源或破坏配额 | metrics（G6）+ review |
| 12 | 组件层新增 public `CompositionLocal`；用 `compositionLocalOf` 承载高频值；在 `Modifier` 工厂读 local | 失效追踪成本 + 反模式 2 | review + G9（local 类型白名单） |
| 13 | `@JvmOverloads` / `@JvmName` / `DeprecationLevel.HIDDEN` / 生成类 `copy`·`componentN` | ABI 形状与 BCV 语义 | `apiDump` diff + G4 |
| 14 | 给**既有** public 函数加带默认值的参数（不走 major / 不加重载） | `$default` 签名变化 = 二进制不兼容 | CI 的 `$default` 计数比对（§9.4） |
| 15 | 无条件 `Modifier.scale(-1f)` 镜像图标 | 会与 `ImageVector.autoMirror` **双重镜像 = 看起来没镜像** | review + `icons.json` 断言 |
| 16 | 库内文案 / 资源 / 图标资源；`WDSemantics` 内置分隔符或词序 | L-B（库内零资源零文案）+ U8 | **G13**（M0–M2 warning，M3 起 error） |
| 17 | 引新依赖：`activity-compose`、`androidx.window`、`material3-window-size-class`、`material-icons-extended`、Espresso、`Haze` 等 | 依赖白名单 / 包体积 / U4 | review（依赖块变更需 2 人） |
| 18 | 改批次、加组件、改帧/批口径、跳过批前签名冻结 | 进程骨架是判定依据（§3），不是建议 | review + `30 §3.3` |

---

## 11. 术语速查与相关文件

| 术语 | 含义 |
| --- | --- |
| **U 系列** | 两端**必须统一**的项（`07 §1.2` + `12 §4.2`）；换端后消费方可见的名字/取值/行为 |
| **F 系列** | **各端自由**、但必须登记"两端形态"的项（`12 §4.1`，真源 = `20` 的 F 注册表副本） |
| **C-15** | 37 行受控值参数名的唯一表（`contracts/<component>.yaml`） |
| **G1–G13** | 本仓的 13 条机器门禁（`12 §1.5.3`），全部挂在 `wisdomGate` 下 |
| **I-1…I-5** | 每批的开工判据（`30 §3.0`） |
| **P7–P12** | 决策生效后的规格/设计文档回写清单（`30 §5.9`；**P11/P12 由 `t53` 登记**；见 §8） |
| **案 B** | 行高最终口径：可见内容 44 两端同值 + Android 布局盒 48（含上下各 2dp 内边距） |
| **L-B** | 库内零资源 + 零文案：文案由调用方传入 |
| **E1 / E2 / E1' / F1** | 体积指标：E2 = 集成 APK 差（主指标，M4 起门槛）；E1 = AAR 字节（趋势）；E1' = 层包/批次 dex 增量；F1 = R8 后 dex 增量（`12 §1.5.6`）。**`F1` 双义**：此处 = 体积指标；F 注册表 `F1` = `WDBanner.visible` 的 `is` 前缀 |
| **V1–V18** | 未闭合验证项（§7 与 `12 §9.1`） |

**相关文件（都在本仓内，随代码走）**：[`docs/SPEC.md`](docs/SPEC.md)（本端规格）、[`docs/DEV-PLAN.md`](docs/DEV-PLAN.md)（计划）、[`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)（架构与图表）、`contracts/<component>.yaml`（名字 / 槽位 / 默认值）。**跨端工作区文档集（已退役）不再随仓提供**：其结论已内联本仓，追溯见 `docs/ARCHITECTURE.md` §16 与 `docs/DEV-PLAN.md` §7 / §9.2。

### 11.1 本文件的校验命令（落库自证）

```bash
test -s AGENTS.md                                     # 非空
test "$(grep -c '^## ' AGENTS.md)" -ge 8              # ≥ 8 个一级小节
grep -q 'apiDump' AGENTS.md && grep -q 'wisdomGate' AGENTS.md   # 关键命令在文内
grep -q 'P7' AGENTS.md && grep -q '32 槽位' AGENTS.md # 回写清单与冻结值在文内
test -z "$(git status --porcelain -- wisdom-ui gradle build.gradle.kts settings.gradle.kts README.md)"
```

---

## 12. 文档修订台账（**索引行**；表体已下沉）

> 完整表格已整体迁入 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) **§16.1–§16.9**；本手册只留索引。

**条目号速查**：`A-01`…`A-11`（`t48`）、`XR-01`/`XR-04`/`XR-08`/`XR-10`/`XR-15`（`t54`）、`N-1`（**已闭合**）、`A-12`/`A-14`/`A-15`（**已闭环**；§8 列名标"回写前"；标签 = **P7–P12**）、`DF-02`/`DF-03`/`DF-04`、预算治理（`t62`）。
