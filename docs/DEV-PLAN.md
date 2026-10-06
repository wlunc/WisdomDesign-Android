# DEV-PLAN · WisdomDesign-Android 详细开发计划（**自包含**）

> **这份文件的角色**：Android 端的**执行计划**——只回答"**按什么顺序做、每批能不能开工、做完算不算过、门禁多强、依赖谁**"。冻结值的完整清单、命令逐条释义、反模式与禁止事项在仓内手册里（见 §11 的分工表）。
> **自包含声明**：本文件**不引用仓外设计文档集的任何路径或链接**（那批文件将从根目录移除）。凡本端需要的口径一律**内联**；可链接的对象只有**仓内**文件：本手册 [`../AGENTS.md`](../AGENTS.md)、架构文档 [`ARCHITECTURE.md`](ARCHITECTURE.md)、`settings.gradle.kts`、`gradle/libs.versions.toml`。
> **口径来源（追溯用，不是链接）**：跨端总计划 `30`、迁移前的本端实现规格 `12`（今 **`docs/SPEC.md`**）、C-15 名表 `40`、跨端契约终稿 `07`、用户决策 `08`、设计复核 `63`/`66` —— 均为**迁移前**的设计文档集；本文件已把 Android 端所需内容**内联或改写**。**冲突裁决链**：用户决策 `08` ＞ 跨端契约 `07` ＞ **`docs/SPEC.md`（原 `12`）** ＞ 名字表 `40` ＞ 计划口径 `30` ＞ 本仓 `AGENTS`/`ARCHITECTURE`/本文件。
> **简写纪律**：本文里 `` `07`/`08`/`12`/`30`/`40`/`63`/`66` `` **仅用于历史追溯与回写登记，不是施工依据**。凡施工动作所依赖的口径，一律**内联在本文件（§5 / §7）**或**指向仓内**（[`../AGENTS.md`](../AGENTS.md)、[`ARCHITECTURE.md`](ARCHITECTURE.md)）——**不得把外部章节号当作动作级引用**（自检命令见 §11.1）。**同源优先级**：本文件 §4.2 / §5 / §7 的表与 [`../AGENTS.md`](../AGENTS.md) **§5.2 / §6** 同源；**冲突以手册为准**。
> **预算说明**：本文件是 `docs/` 下的普通文档，**不自动加载、不受 64KB 预算约束**，可以写详细；`AGENTS.md` 仍受该预算约束（≤60,000 B），因此**判据写手册、细节写这里**。
> **人日 / 周数**：本文件凡出现一律标 **【参考信息】**，**不作承诺、不作验收判据**。判定只看"入口判据能否开工"与"出口判据能否结批"。
> 落库：`android-lead`，2026-10-05。

---

## 0. 目录（Contents）

> **用法**：点标题跳转；锚点按 GitHub 规则生成（去标点、空格→`-`），若本地渲染器不同，**以链接文字定位同名标题**即可。
> **建议阅读顺序**：第一天接手 → §15 开发者指南 → §13 单组件作业流程 SOP → §12 逐组件索引；排期与结批 → §2 / §3 / §14；口径冲突 → §5 / §7 + [`../AGENTS.md`](../AGENTS.md) §6；找东西 → §16 术语表。

- [0. 目录（Contents）](#0-目录contents)
- [1. 本端定位与范围](#1-本端定位与范围)
  - [1.1 本端负责什么（37 件组件的**全部 Android 实现**）](#11-本端负责什么37-件组件的全部-android-实现)
  - [1.2 不属本端（跨仓只读）](#12-不属本端跨仓只读)
  - [1.3 本端今天（尚未开工）的实测状态](#13-本端今天尚未开工的实测状态)
- [2. 里程碑 M0–M6 的本端视图](#2-里程碑-m0m6-的本端视图)
  - [2.0 每批统一的入口判据（I-1…I-5，**全部满足才可开工**）](#20-每批统一的入口判据i-1i-5全部满足才可开工)
  - [2.1 M0 — 规范冻结（本仓部分：M0-3…M0-10）](#21-m0--规范冻结本仓部分m0-3m0-10)
  - [2.2 M1 — 基建与可验证性（本端任务）](#22-m1--基建与可验证性本端任务)
  - [2.3 M2 — 基础批（11 件）](#23-m2--基础批11-件)
  - [2.4 M3 — 封板批（1 件 + 封板动作）](#24-m3--封板批1-件--封板动作)
  - [2.5 M4 — 反馈与弹层（9 件）](#25-m4--反馈与弹层9-件)
  - [2.6 M5 — 导航与表单（15 件）](#26-m5--导航与表单15-件)
  - [2.7 M6 — 场景 + 回归发布（1 件）](#27-m6--场景--回归发布1-件)
- [3. 批次分配（本端逐组件）](#3-批次分配本端逐组件)
  - [3.1 计数自证](#31-计数自证)
  - [3.2 逐批清单与本批第 0 步](#32-逐批清单与本批第-0-步)
  - [3.3 批前签名冻结（每批第 0 步，**不得跳过**）](#33-批前签名冻结每批第-0-步不得跳过)
- [4. 命令与门禁（全部在 `android/` 下执行）](#4-命令与门禁全部在-android-下执行)
  - [4.0 命令现状表（**先读这张表**）](#40-命令现状表先读这张表)
  - [4.1 PR 必过（阻塞合并）](#41-pr-必过阻塞合并)
  - [4.2 `wisdomGate` 里有什么（G1–G13；**前 4 条现成**，其余 `M0-6 起`）](#42-wisdomgate-里有什么g1g13前-4-条现成其余-m0-6-起)
  - [4.3 nightly（**M1 起**；M1–M3 只报不拦，M4 起 B1/B2/E2 转门槛）](#43-nightlym1-起m1m3-只报不拦m4-起-b1b2e2-转门槛)
  - [4.4 发布前（阻塞发布；POM 两条现成，其余 M1/M4 起）](#44-发布前阻塞发布pom-两条现成其余-m1m4-起)
  - [4.5 本地与真机](#45-本地与真机)
  - [4.6 文档类改动也要过 verify（**本仓纪律**）](#46-文档类改动也要过-verify本仓纪律)
- [5. 冻结值与本端契约](#5-冻结值与本端契约)
- [6. 规格回写项（P7–P12 中属本端的）](#6-规格回写项p7p12-中属本端的)
- [7. 设计待给值 / 待签发（本端相关 + **owner** + 默认动作 + 是否阻塞 M0-1）](#7-设计待给值--待签发本端相关--owner--默认动作--是否阻塞-m0-1)
  - [7.1 M0-1 冻结清单现值（**内联副本**；D-3 / D-7 自足可执行）](#71-m0-1-冻结清单现值内联副本d-3--d-7-自足可执行)
- [8. 风险与未验证清单](#8-风险与未验证清单)
  - [8.1 风险登记（结构性问题，缓解已落条）](#81-风险登记结构性问题缓解已落条)
  - [8.2 未验证项（**实测回填前，一律记"未验证"**）](#82-未验证项实测回填前一律记未验证)
- [9. 关键路径与跨端依赖](#9-关键路径与跨端依赖)
  - [9.1 本端串行关键路径（任一步延迟 ⇒ 全线顺延）](#91-本端串行关键路径任一步延迟--全线顺延)
  - [9.2 跨端依赖与并行度](#92-跨端依赖与并行度)
  - [9.3 时长估算【参考信息】](#93-时长估算参考信息)
- [10. 变更与发布](#10-变更与发布)
  - [10.1 三层版本（互不混用）](#101-三层版本互不混用)
  - [10.2 提交与 PR 纪律](#102-提交与-pr-纪律)
  - [10.3 发布顺序（M6 checklist，逐条可判）](#103-发布顺序m6-checklist逐条可判)
  - [10.4 CHANGELOG 与冻结值变更](#104-changelog-与冻结值变更)
- [11. 与仓内另两份文档的分工](#11-与仓内另两份文档的分工)
  - [11.1 本文件的校验命令（落库自证）](#111-本文件的校验命令落库自证)
- [12. 逐组件索引（37 件：批次 / 关键路径件 / 落点 / C-15 / 验收 / 依赖）](#12-逐组件索引37-件批次--关键路径件--落点--c-15--验收--依赖)
  - [12.0 怎么用这张表（列定义与验收代码）](#120-怎么用这张表列定义与验收代码)
  - [12.1 施工总表（37 行，可作为 checklist）](#121-施工总表37-行可作为-checklist)
  - [12.2 计数与一致性自证（可复制）](#122-计数与一致性自证可复制)
- [13. 单组件作业流程（SOP：从 0 到合并）](#13-单组件作业流程sop从-0-到合并)
  - [13.1 全景（8 步）](#131-全景8-步)
  - [13.2 步骤 ①：选件与读规格（**批前签名冻结之后**才开工）](#132-步骤-①选件与读规格批前签名冻结之后才开工)
  - [13.3 步骤 ②：落点与文件清单](#133-步骤-②落点与文件清单)
  - [13.4 步骤 ③：骨架与关键实现要点](#134-步骤-③骨架与关键实现要点)
  - [13.5 步骤 ④：测试怎么写（**六类断言，缺一不可**）](#135-步骤-④测试怎么写六类断言缺一不可)
  - [13.6 步骤 ⑤：本地跑什么（命令 + 期望）](#136-步骤-⑤本地跑什么命令--期望)
  - [13.7 步骤 ⑥：PR 前跑什么（阻塞合并两条 + 分层）](#137-步骤-⑥pr-前跑什么阻塞合并两条--分层)
  - [13.8 步骤 ⑦：PR 与评审要求](#138-步骤-⑦pr-与评审要求)
  - [13.9 步骤 ⑧：批前冻结与批出口](#139-步骤-⑧批前冻结与批出口)
  - [13.10 SOP 自检（可复制）](#1310-sop-自检可复制)
- [14. 验收手册（逐批：出口判据 + 可运行命令 + 不通过处置 + 检查清单）](#14-验收手册逐批出口判据--可运行命令--不通过处置--检查清单)
  - [14.0 每批都先跑的三条（通用）](#140-每批都先跑的三条通用)
  - [14.1 M0 — 规范冻结（本仓部分 M0-3…M0-10；出口判据 = §2.1 的 5 条）](#141-m0--规范冻结本仓部分-m0-3m0-10出口判据--21-的-5-条)
  - [14.2 M1 — 基建与可验证性（本端任务见 §2.2）](#142-m1--基建与可验证性本端任务见-22)
  - [14.3 M2 — 基础批（11 件；出口 = §2.3 的 6 条）](#143-m2--基础批11-件出口--23-的-6-条)
  - [14.4 M3 — 封板批（1 件 + 封板动作；出口 = §2.4 的 6 条）](#144-m3--封板批1-件--封板动作出口--24-的-6-条)
  - [14.5 M4 — 反馈与弹层（9 件；出口 = §2.5 的 7 条）](#145-m4--反馈与弹层9-件出口--25-的-7-条)
  - [14.6 M5 — 导航与表单（15 件；出口 = §2.6 的 7 条）](#146-m5--导航与表单15-件出口--26-的-7-条)
  - [14.7 M6 — 场景 + 回归发布（1 件；出口 = §2.7 的 7 条）](#147-m6--场景--回归发布1-件出口--27-的-7-条)
- [15. 开发者指南（环境 / 规范 / 提交 / 排障 / 升级）](#15-开发者指南环境--规范--提交--排障--升级)
  - [15.1 环境与工具链](#151-环境与工具链)
  - [15.2 代码规范](#152-代码规范)
  - [15.3 提交、分支与 PR](#153-提交分支与-pr)
  - [15.4 常见错误与排查（≥6 条）](#154-常见错误与排查6-条)
  - [15.5 遇到阻塞找谁（升级路径）](#155-遇到阻塞找谁升级路径)
- [16. 术语表与已退役代号解析](#16-术语表与已退役代号解析)
  - [16.1 本仓常用术语](#161-本仓常用术语)
  - [16.2 两端差异术语（F 系列节选，本端相关）](#162-两端差异术语f-系列节选本端相关)
  - [16.3 已退役代号解析（**只做历史追溯，不是引用**）](#163-已退役代号解析只做历史追溯不是引用)
- [17. 关键决策摘要（决策 + 来源）](#17-关键决策摘要决策--来源)
- [19. 设计文档与设计图查阅指南](#19-设计文档与设计图查阅指南)
  - [19.1 设计仓结构与只读纪律](#191-设计仓结构与只读纪律)
  - [19.2 每份设计文档回答什么问题](#192-每份设计文档回答什么问题)
  - [19.3 按任务查场景到章节](#193-按任务查场景到章节)
  - [19.4 检索命令复制即用](#194-检索命令复制即用)
  - [19.5 设计文档与仓内文档的关系](#195-设计文档与仓内文档的关系)
- [20. 逐组件设计溯源表](#20-逐组件设计溯源表)
  - [20.1 怎么用三步](#201-怎么用三步)
  - [20.2 溯源表 37 件](#202-溯源表-37-件)
  - [20.3 关于实测命中数与锚点](#203-关于实测命中数与锚点)
- [21. 从设计规格到实现与验收](#21-从设计规格到实现与验收)
  - [21.1 设计规格 12 节到本端产出](#211-设计规格-12-节到本端产出)
  - [21.2 画廊核验六步](#212-画廊核验六步)
  - [21.3 画廊能核什么不能核什么](#213-画廊能核什么不能核什么)
  - [21.4 截图存档批出口硬要求](#214-截图存档批出口硬要求)
- [18. 文档变更历史](#18-文档变更历史)
  - [18.1 本轮（`t86`）自检（可复制）](#181-本轮t86自检可复制)

**目录自检（可复制；条数差必须 = 0）**：

```bash
cd android
# ① 标题集合 == 目录链接文字集合（含本节自身）
diff <(grep -oE '^#{2,3} .*' docs/DEV-PLAN.md | sed 's/^#* //' | sort) \
     <(grep -oE '^ *- \[[^]]+\]\(#' docs/DEV-PLAN.md | sed 's/^ *- \[//; s/\](.*$//' | sort) && echo "OK: 目录 = 标题"
# ② 条数相等 + 每条都有锚点
test "$(grep -cE '^#{2,3} ' docs/DEV-PLAN.md)" -eq "$(grep -cE '^ *- \[[^]]+\]\(#[^)]+\)$' docs/DEV-PLAN.md)" && echo "OK: 条数差 = 0"
```

---

## 1. 本端定位与范围

**本端 = 单模块 Compose 组件库 `:wisdom-ui`**（`settings.gradle.kts` 今天只 `include(":wisdom-ui")`）。发布物 = Maven Central 上的 `io.github.wlunc.wisdom:wisdom-ui`（AAR）；二进制兼容基线 = `wisdom-ui/api/wisdom-ui.api`（只由 `apiDump` 生成）。

### 1.1 本端负责什么（37 件组件的**全部 Android 实现**）

| 面 | 内容 | 计数量 |
| --- | --- | --- |
| 组件实现 | **20 primitives + 17 composites = 37 件**（逐件清单与批次见 §3） | 37 |
| foundation 手写层 | `theme`（含 `WDMaterialScheme`，全库 m3 唯一触点）/ `typography`（`WDTextMetrics`）/ `motion` / `accessibility`（`WDHaptics`/`WDAnnouncer`）/ `icons`（语义名 + 调用方注入） | 5 个包 |
| 生成物（**只读**） | `foundation/generated/**` —— 唯一写入者 = 设计仓的令牌生成器 | — |
| 检查脚本与测试 | `tools/**`（M0-6 起）、`src/debug`/`src/release`/`src/test`、`src/test/resources`（M0-7/M1 起） | — |
| 非发布入口 | `:demo`（M1 起，U2 的可视验证器）、`:benchmark`（M1 之后） | 2 个模块 |
| CI | `.github/workflows/**`（M0-6/M0-9 起）；PR 只跑两行（§4.1） | — |

### 1.2 不属本端（跨仓只读）

| 对象 | 拥有者 | 本仓形态 |
| --- | --- | --- |
| 令牌值与键名 | 设计仓的 `tokens` 文件 + 生成器 | 只消费 `foundation/generated/**` |
| 令牌生成逻辑 | 设计仓的生成器脚本 | 本仓**不写**生成器 |
| 受控值参数名 / 枚举默认值 | C-15 名表 → 契约 `params[].name` | 本仓按名落码；**名字的落库形态 = 契约 `contracts/<component>.yaml`**（本文件不复述名表） |
| 图标资源 / 文案 | 调用方（宿主 App） | **库内零资源零文案**（L-B）：无 `res/`、无字体、无 `strings.xml` |
| iOS 侧一切 | ios-lead | 只按 U 系列对齐 + 按 F 系列登记差异 |

> 边界表（可写 / 不可写、逐目录）与"今天仓库里实际存在什么"见 [`../AGENTS.md`](../AGENTS.md) §1；本节只给范围。

### 1.3 本端今天（尚未开工）的实测状态

- **存在**：`settings.gradle.kts`、`build.gradle.kts`、`gradle/libs.versions.toml`、`wisdom-ui/{api/wisdom-ui.api, build.gradle.kts, consumer-rules.pro, src/main/**, src/test/**}`、`AGENTS.md`、`docs/`（ARCHITECTURE.md / DEV-PLAN.md）。
- **不存在**（按里程碑建立）：`tools/**`（M0-6）、`.github/**`（M0-6/M0-9）、`gradle/verification-metadata.xml`（M0-7）、`src/test/resources/robolectric.properties`（M0-7/M1）、`src/debug/**`（M1）、`:demo`/`:benchmark`（M1 / M1 之后）。
- **当前门禁状态：`apiCheck` 是红的** —— `api/wisdom-ui.api` 里 `WDColors` 只有 27 个 getter，源码有 31 个 `public val`（缺 `surfaceTint`、`textOnLightPrimary/Secondary/Tertiary`）。**M0 出口必须由红转绿**（转绿 6 步见 §2.1）。
- **本端特有风险面（点名）**：① `apiCheck` 恒红会让后续每个 PR 恒红；② **G9 公开签名不得出现 m3 类型** + 全库 m3 import 计数 == 1；③ 玻璃三路降级（多数设备无窗口模糊能力 ⇒ 走默认降级）；④ 效果配额（模糊面同屏 ≤1、列表项 0）。

---

## 2. 里程碑 M0–M6 的本端视图

### 2.0 每批统一的入口判据（I-1…I-5，**全部满足才可开工**）

| # | 入口判据 | 本端怎么查 |
| --- | --- | --- |
| **I-1** | 上一里程碑出口已判定通过 | M0 看 §2.1 的**本仓相关 5 条**；M1+ 看上一批的出口判据（§3） |
| **I-2** | **批前签名冻结会通过**：该批每件组件的完整签名（含枚举 case + 默认值）+ 契约 `params`/`slots`/`default` 入库 | 文件存在 + 与 C-15 逐行 **0 不一致**；**C-15 未落库前 M2 的冻结不得启动** |
| **I-3** | 该批依赖的令牌与契约已冻结（M0-1 清单 21 行、U3 槽位词表、C-15、F 注册表、`schemes` 维度） | 21 行清单**行行有值或书面延后**（延后必须留痕：deferral + CHANGELOG） |
| **I-4** | 门禁在本仓可跑且为绿 | **`wisdomGate` 落地（M0-6）之后**：`./gradlew wisdomGate` exit 0；**在此之前**以 §4.0 的现成任务 + `apiDump` 漂移检查为判据。⚠️ **M0 期间不得以"`wisdomGate` exit 0"作为开工判据**（命令不存在 ≠ 门禁失败） |
| **I-5** | 该批未闭合前置项都有"默认执行项"（无悬空） | §7 设计待给值表逐行有 owner/时点/默认动作；§8 未验证项逐行有归属 |

**冻结 ≠ 开工**：可以先把后一批的签名冻结做掉，但**不能开始写代码**。

### 2.1 M0 — 规范冻结（本仓部分：M0-3…M0-10）

**本端任务**

| # | 任务 | 出口判据（本端） |
| --- | --- | --- |
| M0-1 / M0-2 | **不属本端但决定本端**：设计侧令牌 D1 清单一次改完（21 行）+ 生成器改造（`generated/` 输出、banner `version+sha256`、`WDTokensVersion`、派生 `lineHeightRatio`、缺字段 emit 0、静默 `mkdir` → exit 1、`--schemes`） | 生成器 `--check` exit 0 + 对比度断言 + 弹簧键集合 == `{response, dampingRatio}` |
| **M0-3** | **提交 1（纯移动）**：`Generated/`→`generated/` + 包名迁移 + 手写文件分层搬家 + 重跑生成器 + **`apiDump` 同提交** | 该提交 `apiCheck` 绿；diff 只含包名重写（`sort` + `sed 's/generated/Generated/'` 对拍） |
| **M0-4** | **提交 2（语义）= A1 + A3 同提交**（明细见下方 6 步的第 4 步） | **`apiCheck` 由红转绿**；人审 diff 只出现预期增删 |
| **M0-5** | **提交 3（依赖面）**：`material3 → implementation`、`tooling-preview → debugImplementation` | `api/wisdom-ui.api` **零 diff** + G9/G1 绿 |
| **M0-6** | 门禁落地：`wisdomGate`（G1–G13）+ CI 两行；首次生成 `knownUnstableArguments` 基线并入库 | 首次全绿；G6/G13/截图开关必须有**真实 task**（不是注释） |
| M0-6a | 基线生产者与更新责任：写入者 = `tools/update-metrics-baseline.sh`；更新者 = **PR 作者**（仅当被 G6 拦下） | `build/compose-metrics/baseline.json` 存在且被 G6 读取（**缺失即 fail**）；**禁止 CI 自动改写基线** |
| **M0-7** | 测试基建：`src/test/resources/robolectric.properties`（固定 `sdk`）+ `android-all` 预热路径 + 生成 `verification-metadata.xml` 入库 | 干净 `GRADLE_USER_HOME` 有网可解析；Robolectric 首个单测可跑 |
| **M0-8** | `README.md` 四处改写（玻璃口径 / 门禁命令 / U4 体积收益未量化 / 无障碍注入点语义） | 与 P-1、U4、U8 口径一致 |
| **M0-9** | 版本治理：`versions.md` + `CHANGELOG.md` 模板 + 记录"两个提交 + 一次 schema 冻结" | 两文件就位；版本链路 `-Pwisdom.version` > `WISDOM_VERSION` > catalog 可判 |
| **M0-10** | `tools/size-report.sh` + 空 `:demo` 骨架 + 测量口径成文 | 脚本可跑；**E2 + m3 dex 占比两个数字在 M1 出口给**（此前一律标"未量化"） |

**入口判据**：I-1（M0 是第一个里程碑，无前置出口）+ I-3 的 M0 版本（21 行清单 / C-15 / schemes 维度）+ I-4 的 M0 形态（现成任务）。

**出口判据（本仓相关 5 条）**：③ `apiCheck` 绿且 `apiDump` 与生成物同提交；④ **`wisdomGate` 一次跑绿**（含 G1–G13）；⑤ `README.md` 四处改写完成；⑦ 目录树与命名（`patterns/` 已删、`WDBottomSheet`/`WDBottomSheetDetent(s)`）；⑨ `tools/size-report.sh` + 空 `:demo` 骨架 + 测量口径成文（两个数字在 M1 出口）。

**门禁强度**：PR 必过 + 首次全绿；**M0 出口报告须给"已给值 N 项 / 按默认动作冻结 M 项（逐行）"两类计数**（与 §7 的 deferral 表同一份记录）。

#### 2.1.1 `apiCheck` 由红转绿：**A1 + A3 同提交**的 6 步序列（M0 出口项）

| 步 | 动作 | 落点 | 出口判据 |
| --- | --- | --- | --- |
| **1** | 令牌 D1 清单**一次改完**（21 行）：含键名 `dampingFraction → dampingRatio`、行高**单值 44**（案 B）、触控双键 44/48、`WDComponent` 12 键、`elevation` 出口、`press-overlay-alpha`、`layout-break-font-scale`、**`schemes` 维度**、**U12 = 32 槽位** | 设计仓（本仓只读） | `--check` exit 0 + 对比度断言 + 弹簧键集合断言 |
| **2** | 生成器改造（同上 M0-2） | 设计仓 | 错误路径用例（目录缺失）必须 exit 1 |
| **3** | **提交 1（纯移动）**：目录改名 + 包名迁移 + 硬件分层搬家 + 重跑生成器 + **`apiDump` 同提交** | 本仓 `foundation/**` + `api/wisdom-ui.api` | 该提交 `apiCheck` 绿 |
| **4** | **提交 2（语义）= A1 + A3 同提交**：**A1** 用 `apiDump` 补齐基线（31 getter + 新构造器形状）；**A3** `WDTextStyle` 4 参 + `@Immutable` + `class` + `internal constructor`（保留 `equals`/`hashCode`）；生成类 `internal constructor`；`toTextStyle()` 迁到 `foundation/typography/WDTextMetrics.kt`（**ABI 所有者 `WDThemeKt` → `WDTextMetricsKt`**，同批 + 记 CHANGELOG）；触控 44→48；弹簧 μ=1.0；行高案 B | 生成物 + `api/wisdom-ui.api` | **`:wisdom-ui:apiCheck` 由红转绿**；人审 diff 只出现预期增删 |
| **5** | **提交 3（依赖面）**：`material3 → implementation`、`tooling-preview → debugImplementation` | `wisdom-ui/build.gradle.kts` | `api/wisdom-ui.api` **零 diff**（有 diff ⇒ 有 m3 类型泄露 ⇒ 回退）+ G9/G1 绿 |
| **6** | **门禁落地**：`wisdomGate`（G1–G13）+ CI 两行 + 首次生成 `build/compose-metrics/baseline.json` 入库 | `build.gradle.kts`、`.github/workflows/ci.yml`、`tools/update-metrics-baseline.sh` | 首次全绿；G6/G13/截图开关有**真实 task** |

> **为什么 A1 与 A3 必须同提交**：两者都改 `api/wisdom-ui.api`；拆开会得到"一次提交里基线既缺 getter 又混着形态变更"的不可审计 diff。

### 2.2 M1 — 基建与可验证性（本端任务）

| 类别 | 本端任务 |
| --- | --- |
| 入口 | `:demo` 模块（U2 可视验证器；**禁止使用 `internal` API**）+ fixtures（zh/en `natural`） |
| 主题 | `WDTheme` 8 参 + `WDLayout` 单例 + m3 桥（`Typography`/`Shapes`）+ 高对比度三件事 + `WDMotionScale` + `WDHaptics`/`WDAnnouncer` 网关 |
| 断言 | `WDLineBoxTest`（行盒 = `max(设计盒高 × 缩放, natural)`）+ 预览矩阵双向检查 + 截图插件接线 |
| 工具 | `tools/size-report.sh` 出 **E2 + m3 dex 占比**两个数字（M0-10 收口） |

**出口判据**：真机 `fontScale 2.0` 观感（不截断 / 不重叠 / 层级保持）；弹簧 μ 并排评审；`WDTheme { Text("x") }` 字阶 == `WDType.body`；六态截图入库；**E2 + m3 dex 占比两个数字进 CHANGELOG**。
**门禁强度**：PR 必过 + nightly（M1–M3 nightly **只报不拦**）。

### 2.3 M2 — 基础批（11 件）

**本端任务**：⓪ 批前签名冻结（11 件）→ 实现 `WDButton`（含 `role`/`onClickLabel`）、`WDIconButton`、`WDTextField`（自研路线，见 §8-V7）、`WDSwitch`、`WDCheckbox`、`WDBadge`、`WDAvatar`、`WDDivider`、`WDCard`、`WDListRow`、`WDIcon`。
**入口判据**：I-1（M1 出口）+ **I-2（批前签名冻结）** + I-3（C-15 / U3 / F 注册表 / 默认值表已落库）+ I-4（PR 门禁绿）。
**出口判据（6 条）**：① 批前冻结（`params[].name` 与 C-15 逐行一致、枚举 `allCases` 归一化绿）；② `wisdomGate` 绿；③ nightly：六态截图 + 无障碍断言（`assertIsButton` / `assertIsNotEnabled` / 热区 / 两态宽度 / `saver` 三条）；④ B1/B2/A3 数字进报告（只报）；⑤ 契约用例名 + 图标语义名对齐；⑥ 冒烟退役。
**门禁强度**：PR 必过 + nightly **连续 3 日绿**（第 3 日观察通过后才可举行批出口评审）。

### 2.4 M3 — 封板批（1 件 + 封板动作）

**本端任务**：⓪ 批前签名冻结 + `WDListSection` + **封板动作**（`apiDump`/ABI 基线入库、`$default` 规则生效、Kover 首次接入 + 覆盖率门槛、体积门槛值定）。
**出口判据（6 条）**：① 公开 API 冻结（`apiDump` 入库）；② `$default` 规则（CI 计数比对）；③ **三处改名已完成**（`WDBottomSheet*` 家族、detent 家族、`Generated/`→`generated/`）；④ Kover 门槛；⑤ 体积门槛值定；⑥ **基础批 11+1 件全完成 = 12/20 primitives**（余 8 件：M4 2 件 + M5 6 件）。
**门禁强度**：同 M2 + **API/ABI 基线入库**。

### 2.5 M4 — 反馈与弹层（9 件）

**本端任务**：⓪ 批前签名冻结（9 件）→ `WDProgressBar`、`WDProgressRing`（2 件 primitives）+ `WDAlert`、`WDBottomSheet`、`WDActionSheet`、`WDToast`、`WDBanner`、`WDEmptyState`、`WDSkeleton`（7 件 composites）；**玻璃三路真机验证**（有窗口模糊 / 无能力 / 高对比度或减弱 → 见 [`ARCHITECTURE.md`](ARCHITECTURE.md) §8）+ **效果配额单测** + 阴影 / 按下叠加**视觉评审**。
**出口判据（7 条）**：① 批前冻结；② 玻璃档位单测 + `WDGlassCapabilities` 真机三路记录；③ 效果配额单测（"同屏 7 个 `WDSkeleton` → 第 7 个静态"）；④ 阴影与 `press-overlay-alpha` 视觉评审；⑤ **E2 ≤400 KB 转门槛**；⑥ `publishToMavenLocal` + 独立小工程能编能渲；⑦ release AAR 预览符号 == 0 且 debug > 0（G8）。
**门禁强度**：PR + nightly；**M4 起 B1/B2/E2 转门槛**。

### 2.6 M5 — 导航与表单（15 件）

**本端任务**：⓪ 批前签名冻结（15 件）→ `WDSearchField`、`WDRadio`、`WDSlider`、`WDStepper`、`WDChip`、`WDAvatarStack`（6 件 primitives）+ `WDSegmentedControl`、`WDPicker`、`WDDatePicker`、`WDFormRow`、`WDPullToRefresh`、`WDNavigationBar`、`WDTabBar`、`WDToolbar`、`WDFAB`（9 件 composites）；动态字体降级顺序（U7）+ RTL。
**出口判据**：① 批前冻结；② 2.0 档 + LTR/RTL 截图入库；③ 性能指标进报告；④ 无障碍断言（`WDSlider` 松手只播报一次、`WDStepper` 高频交互不给触觉、`WDTabBar` 允许 2 行）；⑤ 契约用例名 + 图标语义名仍绿；⑥ sheet 键盘避让项已闭合；⑦ **20/20 primitives 完成于本批出口**（11+1+2+6）。
**门禁强度**：PR + nightly。

### 2.7 M6 — 场景 + 回归发布（1 件）

**本端任务**：⓪ 批前签名冻结（1 件）+ `WDAssigneePicker` + 无障碍回归（TalkBack 闭眼走查 + demo 审计）+ 截图基线封板（37 件 × 浅/深 × 默认/2.0 档 × LTR/RTL）+ 发布前层 + tag。
**出口判据（7 条）**：① 批前冻结；② 无障碍回归；③ 截图封板；④ 全量门禁；⑤ 发布前层（§4.4）；⑥ **双端同 tag `v1.0.0`**；⑦ 发布 checklist 完成。
**门禁强度**：PR + nightly + 发布前层 + **双端同 tag**。

---

## 3. 批次分配（本端逐组件）

### 3.1 计数自证

- **组件**：M2 11 + M3 1 + M4 9 + M5 15 + M6 1 = **37**。（**历史留档**：原计划另有"档位 6 A + 19 B + 12 C = 37"——A/B/C 属**工作量口径**，已按用户决策 #1 停用，**不得作为现行判据**；见 §18）
- **primitives**：**20 = M2 11 + M3 1 + M4 2 + M5 6**；**20/20 完成于 M5 出口**——**M3 封板批只到 12/20**。
- **composites**：17 = M4 7 + M5 9 + M6 1。

### 3.2 逐批清单与本批第 0 步

| 批 | 件数 | 组件（本端实现对象） | 第 0 步（**批前签名冻结**） | 本批出口要点 |
| --- | --- | --- | --- | --- |
| **M2** | 11 | `WDButton`、`WDIconButton`、`WDTextField`、`WDSwitch`、`WDCheckbox`、`WDBadge`、`WDAvatar`、`WDDivider`、`WDCard`、`WDListRow`、`WDIcon` | 11 件完整签名 + 契约 `params`/`slots`/`default` 入库；与 C-15 逐行 0 不一致 | §2.3 的 6 条；关键路径 `WDTextField → WDListRow` |
| **M3** | 1 + 封板 | `WDListSection` | 同上（1 件）+ 封板动作清单 | §2.4 的 6 条；**12/20 primitives** |
| **M4** | 9 | `WDProgressBar`、`WDProgressRing`（P）+ `WDAlert`、`WDBottomSheet`、`WDActionSheet`、`WDToast`、`WDBanner`、`WDEmptyState`、`WDSkeleton`（C） | 9 件；注意 `WDBottomSheetDetent{Half, Large}` / `WDBottomSheetDetents{All, Fixed}` 命名 | §2.5 的 7 条；**E2 ≤400 KB 转门槛** |
| **M5** | 15 | `WDSearchField`、`WDRadio`、`WDSlider`、`WDStepper`、`WDChip`、`WDAvatarStack`（P）+ `WDSegmentedControl`、`WDPicker`、`WDDatePicker`、`WDFormRow`、`WDPullToRefresh`、`WDNavigationBar`、`WDTabBar`、`WDToolbar`、`WDFAB`（C） | 15 件 | §2.6 的 7 条；**20/20 primitives** |
| **M6** | 1 | `WDAssigneePicker` | 1 件 | §2.7 的 7 条；**双端同 tag `v1.0.0`** |

> **primitives 编号注记**：`WDListSection` 在 **20 件 primitives 清单**里是**第 19 件**（第 20 件是 `WDIcon`），但按**批次口径**它是**第 12 件完成的**——两个数字都对，别混用（清单本体见 §3.1 的计数自证）。

### 3.3 批前签名冻结（每批第 0 步，**不得跳过**）

- **判据**：该批每件组件的**完整签名**（含枚举 case + 默认值）+ 契约文件的 `params` / `slots` / `default` 入库；与 C-15 名表**逐行 0 不一致**；枚举 `allCases` 归一化对比为绿。
- **纪律**：**冻结 ≠ 开工**——可以提前把后一批的签名冻结做掉，但**不能开始写代码**；**C-15 未落库前 M2 的冻结不得启动**。
- **为什么单列**：默认值进 ABI（只能靠契约锁，编译器不拦）；批前冻结是"签名先冻、代码后写"的唯一闸门。

---

## 4. 命令与门禁（全部在 `android/` 下执行）

### 4.0 命令现状表（**先读这张表**）

| 命令 / 交付物 | 交付时点 | 今天该用什么 |
| --- | --- | --- |
| `:wisdom-ui:apiCheck` / `assembleRelease` / `assembleDebug` / `testDebugUnitTest` / `compileDebugKotlin` / `build` / `apiDump` / `generatePomFileForMavenPublication` | **现成** | 直接跑 |
| `./gradlew wisdomGate` + `wisdomCheck*` + `tools/**` | **M0-6 起** | 用 §4.2 前 4 条现成任务 + `apiDump && git diff --exit-code` 代替；**`wisdomGate` 不存在 ≠ 门禁失败** |
| `.github/workflows/**`（CI） | **M0-6 / M0-9 起** | 本地手跑 §4.2 的现成命令 |
| `gradle/verification-metadata.xml` | **M0-7 起** | 先生成再入库（§4.5） |
| `src/debug/**`（预览）、`src/test/resources/robolectric.properties` | **M0-7 / M1 起** | 今天没有 |
| `:demo`（`assembleRelease` / `installDebug` / 启动） | **M1 起** | 今天会报 `Project ':demo' not found` |
| `:benchmark`（首个 JSON） | **M1 之后** | 无——**不得**写"已有基准数据" |
| 六态截图任务 | **M1 起**（任务名待实测，见 §8-V14） | 无 |

### 4.1 PR 必过（阻塞合并）

> **与 [`../AGENTS.md`](../AGENTS.md) §5 同源；冲突以手册为准**。

```bash
# ① 复合门禁（M0-6 起；今天不存在）：G1–G13 + apiCheck + assembleRelease/Debug + testDebugUnitTest + ktlint/detekt/lintRelease
./gradlew wisdomGate

# ② 基线不漂：apiDump 后必须零 diff（这一刻意用 shell，Gradle 里没有 git 语义）
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
```

### 4.2 `wisdomGate` 里有什么（G1–G13；**前 4 条现成**，其余 `M0-6 起`）

> **与 [`../AGENTS.md`](../AGENTS.md) §5.2 同源；冲突以手册为准**。

```bash
./gradlew :wisdom-ui:apiCheck                       # ① ABI 基线（今天必红，见 §2.1.1）
./gradlew :wisdom-ui:assembleRelease                # ② release 编译（+ G8 负例生产者）
./gradlew :wisdom-ui:assembleDebug                  # ③ debug 编译（+ G8 正例生产者）
./gradlew :wisdom-ui:testDebugUnitTest              # ④ 单测（只跑 debug 变体）
./gradlew :wisdom-ui:wisdomCheckPackageDeps         # G1 依赖方向 + 包图无环 + m3 import 计数 == 1
./gradlew :wisdom-ui:wisdomCheckTokenLiterals       # G2 令牌字面量（符号级白名单）
./gradlew :wisdom-ui:wisdomCheckMainNoPreview       # G3 src/main 不得出现 @Preview / ui-tooling
./gradlew :wisdom-ui:wisdomCheckDeprecatedBaseline  # G4 deprecated 成员数不增长
./gradlew :wisdom-ui:wisdomCheckStabilityFields     # G5 $stable 存在性（api 文件）
./gradlew :wisdom-ui:wisdomCheckComposeMetrics      # G6 inferredUnstableClasses == 0（文件缺失即 fail）
./gradlew :wisdom-ui:wisdomCheckGeneratedSelfProof  # G7 banner hash 自证
./gradlew :wisdom-ui:wisdomCheckReleaseSymbols      # G8 release AAR 预览符号 == 0 且 debug > 0
./gradlew :wisdom-ui:wisdomCheckPublicTypes         # G9 公开类型正向白名单（U4 的真正机器化）
./gradlew :wisdom-ui:wisdomCheckInsetsWhitelist     # G10 只允许 6 个组件碰 WindowInsets
./gradlew :wisdom-ui:wisdomCheckThemeDefaults       # G11 theme 默认参数不得是构造调用
./gradlew :wisdom-ui:wisdomCheckSingleCallSite      # G12 手势排除调用点 == 1
./gradlew :wisdom-ui:wisdomCheckLiteralLanguage     # G13 L-B 标点/词序（M0–M2 warning，M3 起 error）

# 手工判据（G1-③ 的本地形态；不属 gate 但 review 要贴）
grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin | tee /tmp/m3.txt | wc -l   # 必须 == 1
cat /tmp/m3.txt   # 必须只含 foundation/theme/WDMaterialScheme.kt
```

### 4.3 nightly（**M1 起**；M1–M3 只报不拦，M4 起 B1/B2/E2 转门槛）

```bash
./gradlew :wisdom-ui:validateDebugScreenshotTest    # 六态截图（任务名以 M1 首日实测为准）
./gradlew :wisdom-ui:testDebugUnitTest --tests '*WDLineBoxTest*'
./gradlew :demo:assembleRelease                     # R8 冒烟（唯一能证明不误删的手段）
ls -l demo/build/outputs/mapping/release/seeds.txt  # CI 产物存档（-printseeds）
tools/size-report.sh --dex                          # E2 + m3 dex 占比（M1 出口两个数字）
# 口径：E2 / E1' / F1 在**集成 APK**（:demo release）上测；AAR 只喂 E1 字节趋势
```

### 4.4 发布前（阻塞发布；POM 两条现成，其余 M1/M4 起）

```bash
./gradlew :wisdom-ui:publishToMavenLocal            # 发布物冒烟（M4）
./gradlew :wisdom-ui:generatePomFileForMavenPublication                 # POM 版本 == catalog 的 wisdom 键
./gradlew :wisdom-ui:generatePomFileForMavenPublication -Pwisdom.version=1.0.0
apkanalyzer dex packages demo/build/outputs/apk/release/demo-release.apk   # 【待实测】APK 路径 M1 建 :demo 时定稿
# 口径：AAR 只用于 E1（字节趋势）；E2 / E1' / F1 一律在集成 APK 上测
```

### 4.5 本地与真机

```bash
./gradlew :wisdom-ui:compileDebugKotlin              # 只编库
./gradlew :wisdom-ui:build                           # 编译 + 单测 + check（含 apiCheck）
./gradlew --write-verification-metadata sha256 help  # M0-7：生成依赖校验元数据（入库）
./gradlew :demo:installDebug                         # M1 起：组件迭代主回路

# 真机验证（改哪条 → 断言什么）
adb shell settings put system font_scale 2.0         # U7 上界：不截断/不重叠/行数变化
adb shell settings put system font_scale 1.3         # 中间档：横排→竖排降级（阈值来自令牌）
adb shell settings put global animator_duration_scale 0   # 常驻动画静止
adb shell wm density 320                             # 重建 Activity → 验 Saver
```

### 4.6 文档类改动也要过 verify（**本仓纪律**）

> 任何**文档类产出**（本计划 / 施工手册 / 架构 / 台账）在提交前跑**六个固定项**；另加两条纪律（**禁止自命中**、**双跑**）。本节是跨端文档纪律在**本仓的常驻副本**——根计划目录移除后本节仍可独立查阅；判据与断字写法与 `iOS/docs/DEV-PLAN.md` §4.6 的同名条目**等价**（差异仅路径）。

**① 六个固定项（下面整段可直接复制执行；期望值见 ②表）**

```bash
cd android
# ① 结构自检：防"标题粘连" ⇒ 必须 == 0
grep -cE '^#{2,3} .*#{2,3} ' docs/DEV-PLAN.md
# ② 标题计数：与改动前一致（本次基线 ^## = 19 / ^### = 62）；有增减必须写说明
grep -c '^## '  docs/DEV-PLAN.md ; grep -c '^### ' docs/DEV-PLAN.md
# ③ 图表围栏：本计划无 Mermaid；架构文档 Mermaid 张数已知，且围栏成对
grep -cE '^[`]{3}' docs/DEV-PLAN.md ; grep -cE '^[`]{3}mermaid' docs/ARCHITECTURE.md
# ④ 字节上限：AGENTS ≤ 60000 B（自动加载预算）
wc -c < AGENTS.md | tr -d ' '
# ⑤ 外部引用加固（a）软指代词必须 == 0（字符类写法，避免自命中）
grep -cE '本[端]规格|[外]部规格|见[规]格' docs/DEV-PLAN.md
# ⑤ 外部引用加固（b）"数字代号 + 节号"的引用必须带文件名或落在回写/台账语境 ⇒ a == b
a=$(grep -cE '\b(0[1-9]|[1-3][0-9]|40) §' docs/DEV-PLAN.md)
b=$(grep -nE '\b(0[1-9]|[1-3][0-9]|40) §' docs/DEV-PLAN.md | grep -cE '\.md|回写|台账|外部')
test "$a" = "$b" && echo "⑤b OK (a=b=$a)"
# ⑤ 外部引用加固（c）跨文档节号核验：ARCH 里每个 `DEV-PLAN.md` §X 必须在本文件真实存在 ⇒ 悬空 == 0
for ref in $(grep -oE 'DEV-PLAN\.md`? *\*{0,2}§[0-9]+(\.[0-9]+)?' docs/ARCHITECTURE.md | grep -oE '§[0-9]+(\.[0-9]+)?' | sort -u); do n=${ref#§}; grep -qE "^#{2,3} ${n}([. ]|$)" docs/DEV-PLAN.md || echo "悬空: $ref"; done | wc -l
# ⑥ 表格结构自检：连续 `|` 行成块 ⇒ 每块第 2 行必须是分隔行 ⇒ 必须 == 0
awk '/^\|/{if(!b){k++;n=0;f=NR} b=1;n++; if(n==2 && $0 !~ /^\|[-: |]*-[-: |]*\|[[:space:]]*$/) print f; next} {b=0} END{}' docs/DEV-PLAN.md | wc -l
```

**② 六项的判据、期望值与不通过处置**

| # | 项 | 判据（期望值） | 不通过怎么办 |
| --- | --- | --- | --- |
| ① | **结构自检** | 首条命令 == **0** | 标题被拼到同一行了：拆回两行，并把该项加进该次 verify |
| ② | **标题计数** | `^## ` = **19**、`^### ` = **62**（与改动前一致，**或**在提交说明里写清增减与原因） | 缺节/多节先定位再改文本 |
| ③ | **图表围栏** | 本文件 Mermaid = **0**、围栏 = **36**（偶）；`docs/ARCHITECTURE.md` Mermaid = **9**、围栏 = **30**（偶） | 补齐围栏；成对性用取值是否为偶数复核 |
| ④ | **字节上限** | `AGENTS.md` = **59,563 B** ≤ **60,000** | 超出就把细节**下沉到本文件或 `docs/ARCHITECTURE.md`**，手册只留指针 |
| ⑤ | **外部引用加固** | 软指代 == **0**；`a == b`（当前 **0 = 0**）；跨文档悬空 == **0** | 补文件名限定，或把该句移进 §6 回写台账语境；**不要**用"加白名单"绕过 |
| ⑥ | **表格结构自检** | awk 判据 == **0**（当前 **40 块 / 0 异常**）；块内 <2 行的变体由 §18.1 的 python 版补查 | 说明有元素被插进了表头与数据行之间（**GFM 会把该表降级**：表头成空表、数据行变段落）⇒ 把说明/图例**移到表外**（表头之前或表尾之后），或补分隔行 |

**③ 为什么必要 + 真实实例（一律用任务号指代，不复述被查串）**

> - **⑥ 的两次现场证明**：① **t74 · D2-01** —— 图例被插进**表头与数据行之间**，GFM 下该表降级（表头空、数据行变段落），而当时的**路径 / 字节 / 标题三项检查全绿**；② **iOS 侧 t82 期** —— 给术语表加行时误落**表格外**（空行后独立一行），被 ⑥ 当场报出（块内行数 <2），随即并回表尾、复检归 0。**③ 本端实例（t86 期）**：块级变体在 §5 冻结值表尾抓到同类退化（`| 19 |` 前有孤立空行 ⇒ 19–21 行成无表头块），修复后由 1 异常降为 **0**。
> - **⑤ 的实例**：**A-1** —— 只写"§x.y"、不写文件名的**裸节号**能**绕过路径 `grep`** 给出**假绿**；被测文档被移除后即成为指向不存在章节的死引用。本轮（**t90**）按 ⑤ 的软指代判据清掉 3 处不合格写法（补文件名或显式化）。
> - **① 的根因**：**t58** 的拼接让两个标题落在同一行，当时的 verify 只查"存在性/计数"因而**漏检**；**t41 / t59** 各自发现，**t64** 修复 ⇒ 由此把"结构自检"升级为文档类 verify 的**固定项**。
> - **自命中共 6 次**：**t42 / t48 / t58 / t62 / t64 / t68** —— 均为"新增文本里复述了 verify 正在匹配的字面串"；修复方式**一律改文本**（描述式或字符类），**不改判据**。

**④ 禁止自命中**

新增/修改的文本**不得携带被同一条 verify 用于 `grep` 的字面串**（否则判据会被自己触发）。两种做法：**描述式引用**（不复述被查串，改用指代说法）；**字符类写法**（需要展示模式时，把其中某个字符写成字符类，使文本里不出现完整字面串——本节 ⑤⑥ 的命令即此法）。

**⑤ 双跑纪律**

机器判据通过后，**用同一条命令再跑一次**并贴输出；计数与首次一致才算过。若发生变化，**先改文本、不改判据**。

**⑥ 判据与文本只能改一个**

判据由验收方给出；**冲突时先改文本**。

---

## 5. 冻结值与本端契约

> **与 [`../AGENTS.md`](../AGENTS.md) §6 同源；冲突以手册为准**。**本表是内联副本**，真源 = 本仓 [`../AGENTS.md`](../AGENTS.md) §6（完整 22 行）与 [`ARCHITECTURE.md`](ARCHITECTURE.md) §4/§8/§9；副本随真源更新，**不得反向以副本修真源**。设计规则条文（玻璃矩阵 / 效果配额 / 对比度门槛）见手册 **§6.1**。本节只给**施工与验收必须记住的值**。

| # | 项 | **必须记住的值** |
| --- | --- | --- |
| 1 | **行高** | **`size.row-height.compact = 44`（单值键，两端同值 = 可见内容）**；`comfortable = 60`（同为单值键） |
| 2 | **Android 布局盒 / 热区** | **48 = 44 可见内容 + 上下各 2dp 透明内边距**（`Modifier.wdTouchTarget()`）；**热区 = 布局盒、不覆盖相邻行**；**48 不是行高值、不新增令牌键** |
| 3 | 统一 / 自由的划分 | **U** = 两端**可见内容 44 ± 0.5**；**F = `F51`** 行距差异（Android 48 / iOS 44） |
| 4 | **U12 色槽** | **32 槽位**（含新增 `text.disabled`），浅深成对；生成物 32 个 `public val`；槽位计数断言 == 32 |
| 5 | **换肤（层一）** | `schemes: {light, dark, …}` + 生成器 **`--schemes`**；入口 = `WDTheme(colors = …)`；**不支持**运行时任意令牌 / 服务端下发 / **逐槽位任意覆盖**；切换 = 换已生成 scheme、**不重启进程**、**不进高频路径**（值变化 = 整树重组） |
| 6 | **弹簧 canonical** | 真源字段 = `response` + `dampingRatio`；`stiffness = μ·(2π/response)²`，**μ = 1.0** → gentle `246.74` / snappy `503.65` / bouncy `223.72`；**禁用 `massFactor`** |
| 7 | **图标** | 契约只统一 **44 条语义名 + `mirrorsInRTL`**；Android **调用方注入 `ImageVector`**（默认空集）；**库内零图标资源**；镜像以 `autoMirror` 为准（**不要**再 `scale(-1f)` 二次镜像） |
| 8 | **触控双键** | `touch-target-min-ios = 44` / `touch-target-min-android = 48`；每端只生成本端常量；**一次性改名、不留过渡键**；不得用作视觉尺寸 |
| 9 | **minSdk** | **24**（保持）；玻璃在多数设备走"默认降级"路径 |
| 10 | **m3 收缩** | `material3 → implementation`（BOM 留在 `api`）、`tooling-preview → debugImplementation`；**G9：公开签名不得出现 m3 类型**；**全库 m3 import 计数 == 1**（只能 `foundation/theme/WDMaterialScheme.kt`） |
| 11 | 字段盒高 | `size.field-height = 46`（所有变体/状态 46 ± 1dp；状态不得改变高度） |
| 12 | 弹层几何 | detent `Half = 0.5` / `Large = 0.92`；`max-width = 480`；`corner-radius = 32`；handle `36 × 5` + offset `8` |
| 13 | 动效杂项 | `motion.duration.reduced = 150`；`press-overlay-alpha = 0.06`；`layout-break-font-scale = 1.3`；`WDToast.durationMillis = 3000`（支持 ≥5000） |
| 14 | **受控值参数名（C-15）** | 14 行 Android 改名：`text`（TextField/SearchField）、`on`（Switch）、`selection`（Radio/ListSection/TabBar/AssigneePicker）、`value`（ProgressBar/Ring）、`date`（DatePicker）、`presented`（Alert/BottomSheet/ActionSheet/Toast）；**两个例外**：`WDCheckbox = checked`（iOS 侧改）、`WDBanner = visible` |
| 15 | 无障碍网关 | `WDHaptics`/`WDAnnouncer` = `interface` + `remember*()` + 可注入 local（`null` 默认 = **平台实现，不是 no-op**） |
| 16 | 文案 / 资源 | **L-B：库内零资源零文案**（无 `res/`、无字体、无 `strings.xml`）；文案全部调用方传入；`WDSemantics.join(separator, vararg parts)` **无默认分隔符** |
| 17 | 必须统一的三串（逐字照抄） | ① **行盒公式** `renderedLineBox = max(设计盒高 × 缩放, natural(script))`（默认档 `abs(rendered − max(设计, natural)) ≤ 1px`）；② **状态优先级全序串** `disabled > loading > pressed > focused > hover > default`；③ **玻璃输入/输出集合**：输出 `WDGlassResolution{Opaque, Glass, GlassStrong}`，必选输入 `WDTextLevel` + `WDAppearance` + `WDGlassCapabilities` + `WDEffectsBudget`（Android 额外输入 `WDGlassLevel`，登记为 `F47`） |
| 18 | 本端登记的差异（F 系列节选） | `F41` `WDDatePicker` 用平台对话框；`F42` 行盒容差 1px（iOS 0.5pt）；`F43` 阴影近似映射；`F45` 禁用态 40% 不透明度；`F47` `WDGlassLevel` 额外输入；**`F51` 行距差异** |
| 19 | **阴影出口** | `WDElevation`（`level0/1/2/3/brand`）→ `Modifier.shadow` 映射 **5 行**（**近似映射**，登记 **`F43`**）；**禁止**为多层阴影引入离屏层 |
| 20 | **组件命名** | `WDBottomSheet` / `WDBottomSheetState`（+ `saver(...)`）/ `rememberWDBottomSheetState`；**`WDSheet` 这个名字不存在** |
| 21 | **三枚举（M2/M4 首批要用）** | `WDCardStyle{Elevated, Outlined, Glass}`（M2 `WDCard`，默认 `Elevated`）；`WDToastVariant{Neutral, Success, Warning, Danger}`（M4）；`WDBannerVariant{Info, Warning, Danger}`（M4）；**契约比较用归一化后的规范名**（Kotlin `UpperCamel` ↔ Swift `lowerCamel` = 已登记 DIR-1） |

**契约纪律**：名字冲突看 C-15 名表；**进程 / 冻结值口径看本文件 §2 / §3** 与 [`../AGENTS.md`](../AGENTS.md) §3/§4；**实现细节以本文件 §5 + [`../AGENTS.md`](../AGENTS.md) §6 + 契约 `contracts/<component>.yaml`（`params` / `slots` / `default`）为准**。**时点**：`contracts/<component>.yaml` 属 **M0-5 才落库**（今天不存在）；M0-5 之前，名字与默认值以本文件 §5 + [`../AGENTS.md`](../AGENTS.md) §6 为准。

---

## 6. 规格回写项（P7–P12 中属本端的）

> **回写纪律**：任何规格回写**必须先在计划侧登记为 P 项**（文件 + 章节 + owner + 时点），再按登记范围执行；**执行者不得顺手改登记外的口径**。发现规格缺陷（口径不一致 / 键形冲突 / 编号缺失）⇒ 登记新 P 项，不自行改写。
> **登记信息（非动作引用）**：下表"本端要改哪里"列给出的**文件 + 章节**是**回写对象的登记**（迁移前的文档位置），**不是施工依据**——本端的施工动作一律以本文件 §5 / §7 + [`../AGENTS.md`](../AGENTS.md) §6 + 契约 `contracts/<component>.yaml` 为准；被登记的文件迁出/改名后，本表需按新位置重锚（自检与允许清单见 §11.1）。

| # | 事项 | 本端要改哪里 | 状态 |
| --- | --- | --- | --- |
| **P7** | 行高与热区（案 B） | 迁移前的本端实现规格 `12`：§0.3-Δ4（行高键）、§2.2③（行高公式 / 4 格矩阵 / AR-67 段）、§3.1.2（M0-1 清单第 4/5 行）、§3.1.3（生成物 `WDSize`）、§3.3.1（契约句）、§7.1-M0-4、§7.3（自证表） | **已完整回写**（单值键 44 + 布局盒 48 + 可见内容 44 ± 0.5 三处齐；`AR-67` 为**补充解释**：原两条断言 `(compact,1)==48dp±0.5`、`(compact,2)==76dp±0.5` 原样成立，另**新增**"可见内容 44 ± 0.5"） |
| **P8** | U12 色槽数 | `12`：§3.1.1 / §3.1.3（生成值区 + 槽位计数）/ §3.2.2（构造器参数数）/ §4.2-U12 / §7.1-M0-4 | **已回写**：31 → **32**（含 `text.disabled`），生成值区与 G9 计数断言同步 |
| **P9** | `schemes` 维度 | `12`：新增 §3.1.2a（scheme 维度落地条目）+ §3.2.2 指引句 | **已回写**（含生成器 `--schemes`、不支持运行时任意令牌的边界） |
| **P10** | 设计文档同步 | **不在本仓**（设计侧）：把"行高 44 时热区就是整行"改为"**整行 = 48 布局盒**（可见 44 + 上下各 2dp）；热区 = 该布局盒、不覆盖相邻行" | 登记在案；owner = 设计 + 架构师，**时点 = M0-1**；未回写前以 §5 口径为准 |
| **P11** | M0-1 清单的行高**键形**（两端规格） | **不在本仓**（`02` 侧）：双键 → **单值键**（`size.row-height.compact = 44`，`comfortable = 60`） | 由 `t53` 登记；**时点 = M0-1 冻结前**；本端已按单值键施工 |
| **P12** | 计划文档自身的 pathspec 笔误 | **不属本仓**（计划侧，已就地修正） | 已修正（`t53` 变更集） |

---

## 7. 设计待给值 / 待签发（本端相关 + **owner** + 默认动作 + 是否阻塞 M0-1）

> **纪律**：**清单不缩表**——16 项逐项必须有 **owner 与时点**；**设计未按时给值的行保留行位**，按"默认动作"列**先冻结**（不是删除），逐行记入 `versions.md` 的 deferral 表并在 CHANGELOG 标注；**每个 M0/M1 出口报告须给"已给值 N 项 / 按默认动作冻结 M 项（逐行）"两类计数**。
> **本表是内联副本**，真源 = 本仓 [`../AGENTS.md`](../AGENTS.md) §6 + 设计仓的令牌 / 契约落库；副本随真源更新，**冲突以手册为准**。**按编号 D-1…D-16 排序**（"阻塞 M0-1"列可单独筛）。

| # | 条目（本端影响） | owner | 时点 | **阻塞 M0-1** | **默认动作（设计未按时生效，全部要留痕）** |
| --- | --- | --- | --- | --- | --- |
| **D-1** | 大标题导航高 `size.navbar-large` = **96** | 设计 + 架构师 | M0-1 | **是** | 按候选值 **96** 落盘并标"**待签发**" + 记 deferral；设计否决 → CHANGELOG + 研发 Leader 变更审批 |
| **D-2** | 悬浮 Tab 栏高 56 → 58（距左右 16 / 距底安全区 8） | 设计 + 架构师 | M0-1 | **是** | 维持 **56**（现值）先冻结 + 记 deferral；给值 58 后走变更 |
| **D-3** | 语义色文字色 4 条 `status-text.*` | 设计 + 架构师 | M0-1 | **是** | **见 §7.1 尾部的 4 条设计候选值与两条留痕路径**（候选 = 语义色本体四值；文字色变体未给值 ⇒ 未进 21 行清单） |
| **D-4** | 深色 `text.on-fill` = `#FFFFFF` | 设计 + 架构师 | M0-1 | **是** | 按候选值 **`#FFFFFF`** 落盘并标"待签发" + 记 deferral |
| **D-5** | `gradient.mid` 起点 `#3898B4` | 设计 + 架构师 | M0-1 | **是** | 按候选值 **`#3898B4`** 落盘并标"待签发" + 记 deferral |
| **D-6** | `scrim` 值（令牌**无此键**） | 设计 + 架构师 | M0-1 | **是** | **不新增该键**（M0-1 不引入未签发令牌）+ 标 deferral；组件侧沿用系统默认遮罩 |
| **D-7** | M0-1 冻结清单中**由设计点头的 13 行**（#2 / #4 / #5 / #6 / #7 / #8 / #9 / #10 / #11 / #12 / #13 / #14 / #17；含 #5 行高、#17 `state.*` 5 键） | 设计 + 架构师 | M0-1 | **是** | **全量按 §7.1 的现值先冻结**（行高 = 可见 **44** + Android 布局盒 **48**）+ 记 deferral；#5 标"**需点头**"、#17 标"**设计若拒绝则改 (b)**" |
| **D-8** | `letterSpacing` 单位 = **pt/sp 等价、不随字号缩放** | 设计 + 架构师 | M0-1 | **是** | 按"pt/sp 等价"先冻结 + 记 deferral（该行在清单里落 **(b) 契约注释**，不建键） |
| **D-9** | 高对比度第 2/3 条（`hairline-strong` + 1.5pt、`textSecondary → textPrimary`）的设计签发 | 设计 + 本端 lead | M1（Android）/ M4 | 否 | M0–M3 只做第 1 条；第 2/3 条 M4 起 |
| **D-10** | `press-overlay-alpha` + 阴影近似映射的**视觉评审关** | 设计 + 本端 lead | M2 / M4 | 否 | 按现值（`0.06` / `F43` 近似映射）执行 + M2/M4 **视觉评审关** |
| **D-11** | m3 桥接范围 **10 → 48 槽位** | 设计 + 本端 lead | M1 | 否 | 先做 **10 槽位** + Typography/Shapes 桥；48 槽位 M1 出结论 |
| **D-12** | indication 水波去留 | 设计 + 本端 lead | M2 前 | 否 | **保留 indication**（不删），M2 前设计可否决 |
| **D-13** | 折叠屏避让归属 + 切档 UI 归属 | 设计 + 本端 lead | M5 | 否 | **不做库内避让**；切档 UI 归宿主 |
| **D-14** | 六个设计侧未决项的默认执行项确认 | 设计 + 本端 lead | M2 / M4 / M5 | 否 | 按默认执行项先执行 + 记 deferral |
| **D-15** | CJK 行盒 fixture：zh/en 的 `natural` 实测（= §8 的 `V12`） | 设计 + 本端 lead | M1 出口前 | 否（**阻塞行盒断言**） | fixture 先按测量值并标**「未验证」**，M1 出口前给 zh/en natural |
| **D-16** | 设计稿触控句的 **P10 回写**（整行 = 48 布局盒；可见内容两端一致 44） | 设计 + 架构师 | M0-1 | **是** | 按 §6-P10 回写；未回写前**以本文件 §5 口径为准** |

**owner 口径**：`D-1…D-8` 与 `D-16` = **设计 + 架构师**（架构师为落库方）；`D-9…D-15` = **设计 + 本端 lead**（真源细分：`D-10` / `D-15` 另含"两端"，`D-15` 另含设计侧 fixture 测量）。同一映射与 §7.1 的"决定人"列一致。
**16 项之外（随 M0-1 令牌落库）**：**三张阶梯**（圆角 / 间距 / 字号）、同心圆角与平台补偿、**图标尺寸·线宽阶梯** —— owner = **tech-lead**，时点 = **M0-1**；本端只按"随令牌走"处理，不自行取值。
**两项设计裁决（M0 D1）**：`DF-10`（iOS 玻璃降级形态，不影响本端）、`DF-14`（reduced 时长 150ms vs 160ms 备选）—— 默认按 **150ms** 冻结（本端已按 150 施工）。

### 7.1 M0-1 冻结清单现值（**内联副本**；D-3 / D-7 自足可执行）

> 共 **21 行 = 17 行 (a) 令牌 + 4 行 (b) 契约/验收**（判据：本清单 **21 行**，`grep` 可数）。**✅ = "决定人含设计"的 13 行点头**（#2 / #4 / #5 / #6 / #7 / #8 / #9 / #10 / #11 / #12 / #13 / #14 / #17）。
> **本表是内联副本**，真源 = 本仓 [`../AGENTS.md`](../AGENTS.md) §6 与设计仓令牌 / 契约落库；**冲突以手册为准**。

| # | 键 / 形态（旧 → 新） | 值 | 决定人 | 落点 |
| --- | --- | --- | --- | --- |
| 1 | `motion.spring.*.{response, dampingFraction, stiffness}` → `{response, dampingRatio}`（删 `stiffness`） | `0.40/0.85`、`0.28/0.82`、`0.42/0.68` | 设计 + 架构师 | (a) |
| 2 | （新增）`type.*.letterSpacing` | 仅 `overline = 0.6`，其余 `0` | ✅ 设计 | (a) |
| 3 | `size.touch-target-min` → `size.touch-target-min-{ios,android}` | `44` / `48` | U8 已裁 | (a) |
| 4 | （新增）`size.row-height.comfortable`（**单值键**，两端同值） | `60` | ✅ 设计（D1） | (a) |
| 5 | （新增）`size.row-height.compact`（**单值键**，两端同值 = **可见内容**） | **`44`**；Android 布局盒 **48** = 44 + 上下各 2dp（`Modifier.wdTouchTarget()`；**不新增令牌键**） | ✅ **用户裁决（D1 / 案 B）** | (a) |
| 6 | （新增）`size.card-padding.{comfortable,compact}` | `16` / `12` | ✅ 设计 | (a) |
| 7 | （新增）`size.field-height` | `46` | ✅ 设计 | (a) |
| 8 | （新增）`size.sheet.detent.{half,large}` | `0.5` / `0.92` | ✅ 设计 | (a) |
| 9 | （新增）`size.sheet.max-width` | `480` | ✅ 设计 | (a) |
| 10 | （新增）`size.sheet.corner-radius` | `32` | ✅ 设计 | (a) |
| 11 | （新增）`size.sheet.handle.{width,height}` + `size.sheet.handle-top-offset` | `36` / `5` / `8` | ✅ 设计 | (a) |
| 12 | （新增）`motion.duration.reduced` | `150` | ✅ 设计 | (a) |
| 13 | （新增）`motion.component.press-overlay-alpha` | `0.06`（深浅同值；若设计给两套则改两键） | ✅ 设计 | (a) |
| 14 | （新增）`motion.component.layout-break-font-scale` | `1.3` | ✅ 设计 + android-lead | (a) |
| 15 | `motion.component.*` 补出口：`switch-track 260` / `toast-in 320` / `list-stagger 20` / `skeleton-shimmer 2200` | 键已有值 | 架构师（生成器出口） | (a)（**无新值，仅补出口**） |
| 16 | `elevation.*` 有值无出口 → `WDElevation` 生成出口（`level0/1/2/3/brand`） | 键已有值 | 架构师（生成器出口） | (a)（**无新值，仅补出口**） |
| 17 | （新增）`state.{hover.brightness, pressed.brightness, focus.ring-width, focus.ring-alpha, disabled.alpha}` | `98` / `96` / `3` / `32` / `40` | ✅ 设计（**若拒绝则改 (b)** 并在 `contracts/README.md` 登记） | (a) |
| 18 | `letterSpacing` 单位 = pt/sp 等价（不随字号缩放） | —（**不建键**） | 架构师 | **(b)** `contracts/README.md` |
| 19 | `WDToast` 停留时长上限 | 支持 ≥ `5000ms` | 设计 + 两端 | **(b)** 验收文件 |
| 20 | 弹层系统转场时长（`340` / `240`） | iOS 不适用 | 两端 | **(b)** F21/F27 + U11 断言排除清单 |
| 21 | 关闭阈值 `40%` / `500pt/s` | iOS 不适用 | 两端 | **(b)** F27 |

**`status-text.*`（D-3：两条留痕路径，设计在 M0-1 前二选一）**：设计真源给出的是**语义色本体**四值 —— 成功 `#2A7F5C`（白字 4.9:1）、信息 `#1677B3`（4.9:1）、警示 `#97651F`（5.0:1）、危险 `#B8564D`（4.7:1）；**文字色变体未单独给值**，故这 4 条**未进上面的 21 行清单**（"无值不进清单"）。**默认动作**：① 以上述 4 个设计值作为 `status-text.*` 的**候选值**先落盘并标"待签发"；② 维持"未给值不进清单"，组件侧先用 `text.*` 通用槽位。**两条都要**记 `versions.md` deferral 表 + CHANGELOG。
**同批"有名字没值"项（一并留痕）**：`size.navbar-large`（D-1）、tabbar `56 → 58`（D-2）、深色 `text.on-fill`（D-4）、`gradient.mid`（D-5）、`scrim`（D-6）—— 在得到设计给值之前**不算 M0-1 冻结项**（避免把"待设计"伪装成"已冻结"）。

---

## 8. 风险与未验证清单

### 8.1 风险登记（结构性问题，缓解已落条）

| # | 风险 | 影响 | 本端缓解 |
| --- | --- | --- | --- |
| **R-01** | 令牌 schema 二次 breaking | 生成物 + 签名 + 契约连锁返工 | 21 行清单**一次落完** + 清单不缩表 + 生成器 `--check` 守门 |
| **R-02** | `apiCheck` 未在 M0 转绿 | 之后所有 PR 恒红、门禁失效 | M0-3/M0-4 两个提交 + `apiDump` 同提交（§2.1.1） |
| **R-04** | CJK 自然行高顶开设计行高 | U5"默认档 == 设计值"失败 | 双层断言 + fixtures 记 `natural` + 带容差措辞（`F42`：1px） |
| **R-05** | m3 触点拆不干净 | "拆 artifact 能删干净"破产 | G1-③ + 反例单测 + 自研 `wdTouchTarget` |
| **R-06** | Robolectric `android-all` 离线不可用 | M1 整棵语义树跑不起来 | `robolectric.dependency.dir` 预热 + CI 缓存（M0-7） |
| **R-07** | `staticCompositionLocalOf` 值不稳 → 整树重组 | 掉帧但不报错 | G11 + 单例/`remember` 纪律；**层一切换不得进高频路径** |
| **R-08** | 阴影 / 按下叠加的观感不可机器判 | 视觉返工 | **视觉评审关**（M2/M4，随 §7-D-10） |

> **编号说明**：**`R-03` = 未使用 / 已合并**（沿用 [`ARCHITECTURE.md`](ARCHITECTURE.md) §14.2 的编号，不另立定义）。完整风险表（含依赖风险：设计仓 / 翻译 / 图标资源 / 跨仓）见同处 §14.2。

### 8.2 未验证项（**实测回填前，一律记"未验证"**）

| # | 未验证项 | 归属 | 阻塞点 |
| --- | --- | --- | --- |
| V1 | 真机 `fontScale 2.0` 观感（不截断 / 不重叠 / 层级保持） | 两端 | M1 |
| V2 | 触觉常量映射 + `assertive` 播报的近似实现 | android-dev | M4 前 |
| V3 | ktlint / detekt / Compose lint 的规则集与告警内容（含 `.editorconfig` 键名） | android-dev | **阻塞 M0-6** |
| V4 | `publishToMavenLocal` + `javaDocReleaseGeneration`（Dokka 版本） | android-dev | 阻塞发布前（M4） |
| V5 | `rememberWDMotionScale()` 在真机 `animator_duration_scale = 0` 的数值与首帧时序 | android-dev | 不阻塞（机制已由字节码闭合） |
| V6 | 库模块（`:wisdom-ui`）的 Live Edit 行为 | android-dev | 不阻塞 |
| V7 | `WDTextField` 自研路线（`BasicTextField` + DecorationBox）的 IME / 选区 / 无障碍达标度 | android-dev | **阻塞 M2** |
| V8 | `isHighContrastTextEnabled()` 在 Android 14/15 与厂商 ROM 的真实行为；"组合期读一次、非响应式"是否可接受 | android-dev | M4 前 |
| V9 | 自研 `wdTouchTarget` 在 `LazyColumn` 相邻行下的实测（重叠 / 误触） | android-dev | 不阻塞 |
| V10 | **E2（集成 APK 差）与 m3 dex 占比**：当前无 app 模块，一条都没数 | android-dev | M0-10 出脚本；**数字在 M1 出口** |
| V11 | `Modifier.animateItem()` 在 `WDListRow` 场景的效果与 key 要求 | android-dev | 不阻塞 |
| V12 | **CJK 行盒 vs 设计行高**：zh/en 的 `natural` 实测值未测 | android-dev + 设计 | **阻塞 M1 行盒断言** |
| V13 | "全库 m3 import 计数真能收敛到 1" | android-dev | M0 会给答案（G1-③） |
| V14 | screenshot 插件的**源集名与任务名** | android-dev | **阻塞 M1 nightly** |
| V15 | `WDBottomSheet` 展开态下 back 的语义（先收档还是直接关闭） | android-dev | M4 前 |
| V16 | 阴影近似映射 + `press-overlay-alpha` 的**观感** | 设计 + android-dev | M2 / M4 视觉评审关 |
| V17 | M0 出口⑨ 的两个数字能否在 M1 出口按期给出（依赖 `:demo`） | android-dev | 不阻塞 |
| V18 | **C-15 的 14 个受控值名尚未落 `contracts/<component>.yaml: params[].name`** | android-dev + 架构师 | **阻塞 M2 批前签名冻结**（M0-5 落库） |

**三条明令**：① 上表任一在**实测回填前**，出口报告 / `README` / `CHANGELOG` / PR 描述**一律记"未验证"**并给归属与时点；**不得**写"已达标 / 已通过 / 已验证"。② **`:benchmark` 的首个结果 JSON 尚不存在**——不得写"已有基准数据"。③ 门禁**耗时**（PR ≤10 min / ≤5 min warm）是**目标值不是实测值**。

---

## 9. 关键路径与跨端依赖

### 9.1 本端串行关键路径（任一步延迟 ⇒ 全线顺延）

```text
M0-1 令牌冻结 ─→ M0-2 生成器 ─→ M0-3/M0-4 生成物 + apiDump（apiCheck 转绿）
   └→ M0-5 契约六文件 + C-15 / U3 / F 注册表 / 默认值表落库
        └→ [批前签名冻结] ─→ M2（WDTextField / WDListRow）─→ M2 出口
             └→ M3 公开 API + ABI 基线冻结 ─→ M4 玻璃 + 效果配额 + E2 转门槛
                  └→ M5（DatePicker / PullToRefresh / NavigationBar / TabBar / SegmentedControl）
                       └→ M6 WDAssigneePicker + 无障碍回归 + 截图封板 + 发布前层 + 双端 tag v1.0.0
```

### 9.2 跨端依赖与并行度

- **唯一串行点**：令牌冻结与契约落库（M0-1 / M0-2 / M0-5）——两端都等它；之后 **iOS ↔ Android 完全并行**。
- **批 ↔ 批不可并行**；**同一角色不得并行两个未完成任务**。
- **跨端必须一致（U 系列，换端后消费方可见）**：U5 行盒公式、U6 状态优先级全序串、U8 图标语义名 + 触控双键、U10 玻璃输入/输出集合、U12 32 槽位、U3 槽位词表、C-15 受控值名。
- **各端自由但必须登记（F 系列，本端节选）**：`F41` 日期选择载体、`F42` 行盒容差（1px vs 0.5pt）、`F43` 阴影近似映射、`F45` 禁用态 40%、`F47` 玻璃档位额外输入、**`F51` 行距差异（Android 48 / iOS 44）**。
- **本端对上游的依赖**：设计仓令牌与生成器（M0-1/M0-2）、契约六文件 + C-15 名表（M0-5）、§7 的 16 项设计给值（9 项阻塞 M0-1）。

### 9.3 时长估算【参考信息】

**仅作排期参考，不作承诺、不作验收判据**。批与批之间按"出口判据 + nightly 连续 3 日绿"判定，真实节奏以门禁结果为准；nightly 的"连续 3 日绿"**已计入日历**，不是免费等待。

---

## 10. 变更与发布

### 10.1 三层版本（互不混用）

| 层 | 载体 | 本端形态 |
| --- | --- | --- |
| 令牌数据 | 设计仓 tag + `versions.md` | 生成物 banner 自证（`tokens v… · sha256:…`） |
| 组件契约 | 设计仓 `contracts/**` + manifest | 两端读 manifest 断言 hash 一致（缺失 = fail） |
| 库版本 | `gradle/libs.versions.toml` 的 `wisdom` 键 | `-Pwisdom.version` > `WISDOM_VERSION` > catalog；`apiDump` + CHANGELOG |

### 10.2 提交与 PR 纪律

- **Conventional Commits**：`<type>(<scope>): <摘要>`；提交正文两行必填：`变更集: tokens: vX.Y.Z`（碰令牌/生成物）+ `ABI: 无变化 | 只增 | 签名变化`（碰公开签名）。
- **trunk-based**；**必须 rebase merge（禁止 squash）**：M0-3/M0-4 要求"纯移动"与"语义"两个提交**可独立审计**。
- **`apiDump` 与生成物同提交**；`api/wisdom-ui.api` 不得手改；`apiDump` 后必须零 diff。
- **`$default` 规则**：给**既有** public 函数加带默认值的参数 = **二进制不兼容** ⇒ 走 major 或改为新增重载；CI 用 `$default` 计数前后比对。
- **PR 模板六段**：变更类型 / 变更集标识 / 影响面（贴 `apiDump` 类型级摘要）/ 门禁输出 / 证据（六态截图、语义树断言、预览矩阵）/ 迁移片段（仅 Breaking）。
- **Review 人数**：碰 `api/wisdom-ui.api`、契约、CI 或发布配置 ⇒ **2 人**，其中 1 名必须是 tech-lead 或 android-lead。

### 10.3 发布顺序（M6 checklist，逐条可判）

1. 设计仓：令牌 + 契约冻结并打 tag，产出 `dist/*` + manifest。
2. 生成：跑设计仓生成器（两端生成物 + manifest）。
3. 两端提交：Android `apiDump` + `api/wisdom-ui.api` **与生成物同一提交**。
4. 两端跑绿：`./gradlew wisdomGate` + `:demo:assembleRelease` + benchmark。
5. 发布前层：集成 APK 差 + `apkanalyzer dex` + POM 三查 + tag 校验。
6. 两端打 tag：**同 tag `v1.0.0`**；tag 前确认三仓 HEAD 与 `versions.md` 一致。

**tag 只增不改**：禁止 move / delete / re-tag；打错只能发新版本并在 CHANGELOG 标注废弃版本号。

### 10.4 CHANGELOG 与冻结值变更

- M0-9 建 `CHANGELOG.md` 模板；记录"**两个提交 + 一次 schema 冻结**"；M1 出口补 U4 的两个数字。
- **延后项留痕**：设计未给值而按默认动作冻结的项，逐行记 `versions.md` deferral 表 + CHANGELOG。
- **改冻结值 = 走变更流程**：先改真源（令牌 / 契约）→ 再同步两端副本 → 记 CHANGELOG；**禁止**在本仓代码里先改、事后再补文档。

---

## 11. 与仓内另两份文档的分工

| 文档 | 它答什么 | 什么情况下去读 |
| --- | --- | --- |
| **本文件 `docs/DEV-PLAN.md`** | **计划**：顺序、入口/出口判据、门禁强度、依赖、风险与未验证、发布节奏 | 排期、开工前确认判据、结批 |
| [`../AGENTS.md`](../AGENTS.md) | **操作手册**：可写/不可写边界、命令现状表、**冻结值全表（§6）**、设计规则条文（§6.1）、未验证清单（§7）、回写台账（§8）、反模式 18 条（§10）。**受 64KB 自动加载预算约束（≤60,000 B）**——新增细节写本文件或架构文档，手册只加判据与指针 | 每次动手前扫一遍；出签名 / 跑命令 / 查冻结值 |
| [`ARCHITECTURE.md`](ARCHITECTURE.md) | **架构细节**：系统全景、层包与依赖、令牌流水线、组件模型与 `Modifier` 顺序、渲染与重组、玻璃降级决策树、无障碍、门禁拓扑、发布时序、ADR、风险表、台账（§16） | 查"为什么这样做"、改结构、查图表 |

**唯一真源纪律**：同一事实**只在一处改**——令牌值在生成物、名字在 C-15、冻结值口径在本文件与手册 §6（冲突以手册为准）、架构细节在 `ARCHITECTURE.md`。三份文件互相**只给章节号指针**，不复述大段内容。

### 11.1 本文件的校验命令（落库自证）

> **文档类改动的六个固定项见 §4.6**（结构自检 / 标题计数 / 图表围栏 / 字节上限 / 外部引用加固 / 表格结构自检）——本节是**本文件的落库自证**，与 §4.6 的通用纪律**合跑**：§4.6 判"文档有没有坏"，本节判"本文件是否仍自包含、仍不越界"。

```bash
test -s docs/DEV-PLAN.md                                                     # 非空
test "$(grep -c '^## ' docs/DEV-PLAN.md)" -ge 11                             # ≥ 11 个一级小节
test -z "$(grep -nE '\.\./\.\./|/impl-plan|structure-discussion' docs/DEV-PLAN.md)"   # 自包含：不得出现仓外文档集路径（命中即 fail）
# ② 仓外**节号 / 文档名**（人工复核；路径模式查不到的假绿就靠这一条）
#    允许清单 = §6 规格回写台账的 P7 / P8 / P9 三行（登记"回写对象 + 章节"，属登记信息、非动作引用）；
#    其余必须 0。实测：2 行（P7 / P8），P9 属同一允许组。
grep -nE '本[端]规格|12[^0-9]{0,3}[§]|(30|40|02|07|08|20|63|66)[^0-9]{0,3}[§]|01[-]foundation|12[-]b22[-]glass|03[-]perf[-]release|06[-]accessibility|09[-]layout' docs/DEV-PLAN.md
#    允许清单（当前实测 3 行）：文首"口径来源（追溯用）"段 + §6 回写台账的 P7 / P8 两行（P9 属同一允许组，因措辞不同未被此模式捕获）
#    模式里的 `[§]` / `[-]` 是**断字**写法（等价于 `§` / `-`），仅为避免本行自命中；照抄执行即可。
test "$(wc -c < AGENTS.md | tr -d ' ')" -le 60000                            # 手册仍在预算内
test -z "$(git status --porcelain -- wisdom-ui gradle build.gradle.kts settings.gradle.kts README.md)"   # 施工面未被触碰
```

---

## 12. 逐组件索引（37 件：批次 / 关键路径件 / 落点 / C-15 / 验收 / 依赖）

> **用途**：这是**施工总表**——每件组件在开工前，先在本表定位「批次 / 落点 / 规格锚点 / 参数名 / 验收集 / 依赖」，再走 §13 的 SOP。**判据一律以 [`../AGENTS.md`](../AGENTS.md) §6 与 `docs/SPEC.md` 对应节为准**，本表只做索引与最低验收集。

### 12.0 怎么用这张表（列定义与验收代码）

| 列 | 含义 | 取值来源 |
| --- | --- | --- |
| **组件** | 公开类型名（`WD` 前缀，Kotlin UpperCamel） | `docs/SPEC.md` §2.1 / §2.10 |
| **批次** | 实现批次 M2–M6（**批 ↔ 批不得并行**） | 本文件 §3.2 |
| **关键路径件** | `是` / `否` —— 该件是否在**本批串行关键路径**上（`是` ⇒ 其延迟会顺延本批出口与后续批次） | 判定依据 = §3.2 各批「本批出口要点」列（M2 = `关键路径 WDTextField → WDListRow`）+ §9.1 串行链（M2 / M5 / M6 逐件点名）+ [`../AGENTS.md`](../AGENTS.md) §3.2「关键路径（本仓）」列（M4 = `WDBottomSheet` → 弹层族）；**判定式 = 两端可取证点名件的并集（跨端同答案）**：本端三处未点名、但对端点名的件也记 `是`；**未被任一端点名的记 `否`**。原 `档位（A/B/C）` 列已按用户决策 #1 移除——理由见 §18 |
| **落点** | 包内目录（**主文件 = `<组件名>.kt`**） | 根前缀 = `wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/`；完整形态 `…/components/{primitives,composites}/<组件>/…`（层与件数见 `docs/SPEC.md` §2.1：primitives 20 / composites 17） |
| **`docs/SPEC.md` 锚点** | 该件的规格位置（清单 / 签名或补全 / C-15 行号；组合件另加 §2.4） | `docs/SPEC.md` §2.1 / §2.2（仅 4 件完整签名）/ §2.3（其余 33 件）/ §2.4 / §2.10 |
| **Android 形态 / C-15 名** | 受控值参数名（Android 名 → 契约名）；`无` = 该件没有受控值参数 | `docs/SPEC.md` §2.10（C-15 副本） |
| **验收** | **最低验收集**（代码见下）；具体断言以 `docs/SPEC.md` §2.6 / §2.7 与该件节 + 本文件 §14 为准 | 本表 §12.0 代码表 |
| **依赖** | 先决件与复用关系 | `docs/SPEC.md` §2.4 / §2.10 |
| **状态** | checklist：`☐` 未开工 / `◐` 进行中 / `☑` 已过本件验收 | 施工时由 PR 作者更新（**不得**在批出口前批量预勾） |

**验收代码表（最低验收集；每件都要跑「本行代码 + 该件节里的专项断言」）**：

| 代码 | 含义 | 判据 / 命令 |
| --- | --- | --- |
| **A** | **ABI / `apiCheck`** | 公开签名有任何增删改 ⇒ **`apiDump` 与实现同提交**，`./gradlew :wisdom-ui:apiCheck` 绿（`现成`）；M3 起 = BCV 基线冻结（新增 public 成员视为 breaking 需评审） |
| **S** | **稳定性 / 重组** | 公开 `@Composable` 的参数类型必须 `@Immutable`/`@Stable` 或本身稳定；G5 `$stable` 存在性 + G6 `inferredUnstableClasses == 0`（`M0-6 起`）；禁止 §15.2 的稳定性反模式 |
| **C** | **契约一致性** | `params[].name` 与 C-15 逐行 **0 不一致**；枚举 `allCases` 归一化比较绿（M0-5 落库后为硬判据；此前以 `docs/SPEC.md` §2.10 为准） |
| **E** | **交互 / 语义** | 语义树断言（`assertIsButton`/`assertIsNotEnabled` 等）、状态优先级全序串（`docs/SPEC.md` §2.6）、热区 = 布局盒、`interactionSource` 唯一 |
| **V** | **视觉 / 效果** | 六态截图（`M1 起`）+ 玻璃/配额/阴影相关的视觉评审项（`docs/SPEC.md` §3.5 / [`../AGENTS.md`](../AGENTS.md) §6.1） |
| **L** | **布局 / 字体** | `fontScale` 2.0 与 1.3 档、行盒公式（`docs/SPEC.md` §2.8.1）、LTR/RTL 不越界；行高件另跑「可见 44 ± 0.5 / 布局盒 48」 |

### 12.1 施工总表（37 行，可作为 checklist）

| 组件 | 批次 | 关键路径件 | 落点（相对 `wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/`） | `docs/SPEC.md` 锚点 | Android 形态 / C-15 名 | 验收（代码见 §12.0） | 依赖 | 状态 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `WDButton` | M2 | 否 | `…/components/primitives/WDButton/` | §2.1 / §2.2 / §2.10-01 | 受控值 `loading` → 契约名 `loading` | A S C E V L；状态机全序串 + `role`/`onClickLabel` | 基础（`WDTheme` + 令牌） | ☐ |
| `WDIconButton` | M2 | 否 | `…/components/primitives/WDIconButton/` | §2.1 / §2.3 / §2.10-02 | 受控值 `loading` → 契约名 `loading` | A S C E V；热区断言 | 复用 `WDButtonVariant`（子集 Plain/Filled/Glass） | ☐ |
| `WDTextField` | M2 | 是 | `…/components/primitives/WDTextField/` | §2.1 / §2.2 / §2.10-03 | 受控值 `text`（← `value`） → 契约名 `text` | A S C E L；V7 自研路线：IME/选区/无障碍 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDSearchField` | M5 | 否 | `…/components/primitives/WDSearchField/` | §2.1 / §2.3 / §2.10-04 | 受控值 `text`（← `value`） → 契约名 `text` | A S C E L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDSwitch` | M2 | 否 | `…/components/primitives/WDSwitch/` | §2.1 / §2.3 / §2.10-05 | 受控值 `on`（← `checked`） → 契约名 `on` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDCheckbox` | M2 | 否 | `…/components/primitives/WDCheckbox/` | §2.1 / §2.3 / §2.10-06 | 受控值 `checked` → 契约名 `checked` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDRadio` | M5 | 否 | `…/components/primitives/WDRadio/` | §2.1 / §2.3 / §2.10-07 | 受控值 `selection`（← `selected`） → 契约名 `selection` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDSlider` | M5 | 否 | `…/components/primitives/WDSlider/` | §2.1 / §2.3 / §2.10-08 | 受控值 `value` → 契约名 `value` | A S C E；松手只播报一次 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDStepper` | M5 | 否 | `…/components/primitives/WDStepper/` | §2.1 / §2.3 / §2.10-09 | 受控值 `value` → 契约名 `value` | A S C E；高频交互不给触觉 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDChip` | M5 | 否 | `…/components/primitives/WDChip/` | §2.1 / §2.3 / §2.10-10 | 受控值 `selected` → 契约名 `selected` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDBadge` | M2 | 否 | `…/components/primitives/WDBadge/` | §2.1 / §2.3 / §2.10-11 | 受控值：**无**（纯视觉/容器） | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDAvatar` | M2 | 否 | `…/components/primitives/WDAvatar/` | §2.1 / §2.3 / §2.10-12 | 受控值：**无**（纯视觉/容器） | A S C L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDAvatarStack` | M5 | 否 | `…/components/primitives/WDAvatarStack/` | §2.1 / §2.3 / §2.10-13 | 受控值：**无**（纯视觉/容器） | A S C L | 组合（见 §2.4） | ☐ |
| `WDDivider` | M2 | 否 | `…/components/primitives/WDDivider/` | §2.1 / §2.3 / §2.10-14 | 受控值：**无**（纯视觉/容器） | A S C V | 基础（`WDTheme` + 令牌） | ☐ |
| `WDProgressBar` | M4 | 否 | `…/components/primitives/WDProgressBar/` | §2.1 / §2.3 / §2.10-15 | 受控值 `value`（← `progress`） → 契约名 `value` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDProgressRing` | M4 | 否 | `…/components/primitives/WDProgressRing/` | §2.1 / §2.3 / §2.10-16 | 受控值 `value`（← `progress`） → 契约名 `value` | A S C E V | 基础（`WDTheme` + 令牌） | ☐ |
| `WDCard` | M2 | 否 | `…/components/primitives/WDCard/` | §2.1 / §2.3 / §2.10-17 | 受控值：**无**（纯视觉/容器） | A S C E V | 基础（`WDTheme` + 令牌） | ☐ |
| `WDListRow` | M2 | 是 | `…/components/primitives/WDListRow/` | §2.1 / §2.2 / §2.10-18 | 受控值 `selected` → 契约名 `selected` | A S C E L；行高断言（可见 44 ± 0.5 / 布局盒 48）+ 热区 V9 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDListSection` | M3 | 否 | `…/components/primitives/WDListSection/` | §2.1 / §2.3 / §2.10-19 | 受控值 `selection`（← `selected`） → 契约名 `selection` | A S C E L；同上（U = 可见内容 44 ± 0.5） | 组合（见 §2.4） | ☐ |
| `WDIcon` | M2 | 否 | `…/components/primitives/WDIcon/` | §2.1 / §2.3 / §2.10-20 | 受控值：**无**（纯视觉/容器） | A S C L；44 条语义名 + `mirrorsInRTL`（G13） | 基础（`WDTheme` + 令牌） | ☐ |
| `WDSegmentedControl` | M5 | 是 | `…/components/composites/WDSegmentedControl/` | §2.1 / §2.3 / §2.10-21 / §2.4 | 受控值 `selection` → 契约名 `selection` | A S C E | 基础（`WDTheme` + 令牌） | ☐ |
| `WDPicker` | M5 | 否 | `…/components/composites/WDPicker/` | §2.1 / §2.3 / §2.10-22 / §2.4 | 受控值 `selection` → 契约名 `selection` | A S C E V L；弹层/焦点回归 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDDatePicker` | M5 | 是 | `…/components/composites/WDDatePicker/` | §2.1 / §2.3 / §2.10-23 / §2.4 | 受控值 `date`（← `value`） → 契约名 `date` | A S C E V L；F41 日期载体（Android） | 基础（`WDTheme` + 令牌） | ☐ |
| `WDFormRow` | M5 | 否 | `…/components/composites/WDFormRow/` | §2.1 / §2.3 / §2.10-24 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C E L | 槽位 `label`/`control`（§2.4） | ☐ |
| `WDAlert` | M4 | 是 | `…/components/composites/WDAlert/` | §2.1 / §2.3 / §2.10-25 / §2.4 | 受控值 `presented`（← `visible`） → 契约名 `presented` | A S C E V L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDBottomSheet` | M4 | 是 | `…/components/composites/WDBottomSheet/` | §2.1 / §2.2 / §2.10-26 / §2.4 | 受控值 `presented`（← `visible`） → 契约名 `presented` | A S C E V L；Detent/Saver 断言（V15；F5/F38） | 同目录 `WDBottomSheetState`/`WDBottomSheetDetent(s)`/`NestedScroll` | ☐ |
| `WDActionSheet` | M4 | 是 | `…/components/composites/WDActionSheet/` | §2.1 / §2.3 / §2.10-27 / §2.4 | 受控值 `presented`（← `visible`） → 契约名 `presented` | A S C E V L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDToast` | M4 | 是 | `…/components/composites/WDToast/` | §2.1 / §2.3 / §2.10-28 / §2.4 | 受控值 `presented`（← `visible`） → 契约名 `presented` | A S C E L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDBanner` | M4 | 否 | `…/components/composites/WDBanner/` | §2.1 / §2.3 / §2.10-29 / §2.4 | 受控值 `visible`（不改） → 契约名 `visible` | A S C E L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDEmptyState` | M4 | 否 | `…/components/composites/WDEmptyState/` | §2.1 / §2.3 / §2.10-30 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDSkeleton` | M4 | 否 | `…/components/composites/WDSkeleton/` | §2.1 / §2.3 / §2.10-31 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C V；效果配额（同屏 ≤6；同屏 7 个 → 第 7 个静态） | 基础（`WDTheme` + 令牌） | ☐ |
| `WDPullToRefresh` | M5 | 是 | `…/components/composites/WDPullToRefresh/` | §2.1 / §2.3 / §2.10-32 / §2.4 | 受控值 `refreshing` → 契约名 `refreshing` | A S C E V | 基础（`WDTheme` + 令牌） | ☐ |
| `WDNavigationBar` | M5 | 是 | `…/components/composites/WDNavigationBar/` | §2.1 / §2.3 / §2.10-33 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C E L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDTabBar` | M5 | 是 | `…/components/composites/WDTabBar/` | §2.1 / §2.3 / §2.10-34 / §2.4 | 受控值 `selection`（← `selectedIndex`） → 契约名 `selection` | A S C E V L；允许 2 行 | 基础（`WDTheme` + 令牌） | ☐ |
| `WDToolbar` | M5 | 否 | `…/components/composites/WDToolbar/` | §2.1 / §2.3 / §2.10-35 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C L | 基础（`WDTheme` + 令牌） | ☐ |
| `WDFAB` | M5 | 否 | `…/components/composites/WDFAB/` | §2.1 / §2.3 / §2.10-36 / §2.4 | 受控值：**无**（纯视觉/容器） | A S C E V | 基础（`WDTheme` + 令牌） | ☐ |
| `WDAssigneePicker` | M6 | 是 | `…/components/composites/WDAssigneePicker/` | §2.1 / §2.3 / §2.10-37 / §2.4 | 受控值 `selection`（← `selectedIds`） → 契约名 `selection` | A S C E V L | 基础（`WDTheme` + 令牌） | ☐ |

> **「关键路径件」列的判定依据（可复核）**：① §3.2 本批出口要点列（M2 = `关键路径 WDTextField → WDListRow`）；② §9.1 串行链（M2 `WDTextField/WDListRow`、M5 `DatePicker/PullToRefresh/NavigationBar/TabBar/SegmentedControl`、M6 `WDAssigneePicker`）；③ [`../AGENTS.md`](../AGENTS.md) §3.2「关键路径（本仓）」列（M4 = `WDBottomSheet` → 弹层族）。**判定式：关键路径件 = 批内串行链上被点名件的并集（跨端同答案）；M4 弹层族三件（`WDAlert`/`WDActionSheet`/`WDToast`）依 iOS 侧原 `(C)` 标记并入。**两端任一处点名的件记 `是`，均未点名的记 `否`（保守；不等于不重要，只是不在串行链上）。**`是` = 12 件 / `否` = 25 件**。

### 12.2 计数与一致性自证（可复制）

```bash
# ① 本表行数 = 37（且组件名唯一）
grep -cE '^\| `WD[A-Za-z]+` \| M[2-6] \|' docs/DEV-PLAN.md          # 期望 37
# ② 批次分布 = 11/1/9/15/1（与 §3.1 计数自证一致）
for b in M2 M3 M4 M5 M6; do printf "%s=%s " $b "$(grep -cE "^\| \`WD[A-Za-z]+\` \| $b \|" docs/DEV-PLAN.md)"; done; echo   # 期望 M2=11 M3=1 M4=9 M5=15 M6=1
# ③ 层分布 = primitives 20 / composites 17（**只数本表行**，不数路径模式说明行）
grep -cE '^\| `WD[A-Za-z]+` \| M[2-6] \|.*/primitives/' docs/DEV-PLAN.md    # 期望 20
grep -cE '^\| `WD[A-Za-z]+` \| M[2-6] \|.*/composites/' docs/DEV-PLAN.md    # 期望 17
# ④ 组件名集合 = `docs/SPEC.md` §2.10 的 37 行（对齐真源，不靠人工目检）
diff <(grep -oE '^\| `WD[A-Za-z]+` \| M[2-6] ' docs/DEV-PLAN.md | grep -oE 'WD[A-Za-z]+' | sort) \
     <(sed -n '/^### 2.10 /,/^## 3/p' docs/SPEC.md | grep -oE '^\| 0?[0-9]+ \| `WD[A-Za-z]+`' | grep -oE 'WD[A-Za-z]+' | sort) && echo "OK: 索引 = C-15 真源"
```

> **已移除的列与残留缺口（如实登记）**：① **`档位（A/B/C）` 列已按用户决策 #1 删除**——A/B/C 本质是"工作量（人日）"口径的载体（A = 1.0 / B = 1.75 / C = 3.0 pd），既与用户已明确的"**所有开发都是 vibe coding，不需要计算人力**；计划重要的是开发进程"冲突，其逐件分配来源（迁移前的跨端计划）又**已退役、本仓无副本**；② 替代轴 = **`关键路径件`（是/否，事实性、可取证）**，判定依据与计数见上；③ **逐件人日/工作量不再作为本计划的任何判据**（不作排期承诺、不进出口判据）；若日后确需工作量口径，**从工单数据重建**，**不得**从旧档位反推；④ 若日后需要引入"关键路径"以外的新轴（如风险等级、外部依赖），按 §10.4 走变更流程并在 §18 记一行。
> **⑤ 跨端同形不同义的记号（务必区分，不得互读）**：本文件 **§3.2 的 `（P）` / `（C）` = 层标记**（primitives / composites，与 §3.1 的 20/17 计数同源）；**iOS 侧的 `(C)` = 复杂度 C 档 / 关键路径件**（其手册图例另有定义，且**其侧已标注为"历史标记写法"、仅作证据本体**——引用时同样不得当现行口径）。两端同形、语义不同，引用时**必须带端别**。另：§3.1 聚合行的 "6 A + 19 B + 12 C" 里的 **12** 与 §12.1 的 **12 件关键路径件**数值相同（M2 2 + M4 4 + M5 5 + M6 1）——iOS 侧图例把 C 档解释为"串行在前的关键路径件"，故该数值**可作旁证**；但 **A/B/C 已废止**，本表判定仍以 §12.1 的三处点名 + **跨端并集**为准，**不得**用 A/B/C 反推。

---

## 13. 单组件作业流程（SOP：从 0 到合并）

> **适用**：M2 起的每一件组件（M0/M1 的基建任务走 §14.1 / §14.2 的清单，不走本 SOP）。
> **总原则**：**先冻结签名，再写实现**；**先写断言，再调视觉**；**`apiDump` 与实现同提交**（`docs/SPEC.md` §1.4）。

### 13.1 全景（8 步）

```text
①选件读规格 → ②定落点与文件清单 → ③骨架与关键实现 → ④写测试（六类断言）
      → ⑤本地跑（编译/单测/截图） → ⑥PR 前跑（apiCheck + wisdomGate + 漂移检查）
            → ⑦PR 与评审（rebase 禁 squash / 2 人 review / apiDump 同提交） → ⑧批前冻结与批出口
```

### 13.2 步骤 ①：选件与读规格（**批前签名冻结之后**才开工）

1. 在 §12.1 找到该件的一行 → 记下 **批次 / 落点 / SPEC 锚点 / C-15 名 / 验收代码 / 依赖**。
2. 按顺序读：`docs/SPEC.md` **§2.1**（清单与批次）→ **§2.2 或 §2.3**（签名/关键参数）→ **§2.10**（默认值与枚举 case）→ **§2.6**（状态与语义）→ **§2.7**（边界场景）→ **§2.8**（内部实现与行盒）→ **§2.9**（类型、稳定性与 ABI）。
3. 交叉核对：[`../AGENTS.md`](../AGENTS.md) **§6 冻结值**（该件若碰冻结值，逐字照抄）、**§6.1**（设计规则 DF-02/03/04）、**§10**（反模式）。
4. 产物：**一句话实现说明 + 该件的 C-15 名与默认值清单**（贴进 PR 描述"影响面"段）。

```bash
# 读规格（示例：WDButton）
grep -n 'WDButton' docs/SPEC.md | head -20            # 定位所有出现
sed -n '/^### 2.10 /,/^## 3/p' docs/SPEC.md | grep -n 'WDButton'   # C-15 行
```

### 13.3 步骤 ②：落点与文件清单

| 文件 | 何时需要 | 路径规则 |
| --- | --- | --- |
| 主实现 | 每件 | `…/components/{primitives,composites}/<组件>/<组件>.kt` |
| 枚举与参数类型 | 有枚举/受控值类型时 | 同目录 `<组件><Variant\|Size\|Style>.kt`（或并入主文件，按 §2.10 粒度） |
| 状态类 | 有 `remember*State` 时 | 同目录 `<组件>State.kt`（+ `saver(...)`；见 `WDBottomSheetState`） |
| 预览 | 每件（**一份**） | `src/debug/kotlin/…/components/<组件>/<组件>Preview.kt`（由 `sourceSets` 挂给 screenshot 源集） |
| 单测 | 每件 | `src/test/kotlin/…/components/<组件>/<组件>Test.kt` |
| 语义/截图测试 | 交互件、视觉件 | 同上目录 `<组件>SemanticsTest.kt` / `<组件>ScreenshotTest.kt` |

> **`src/main` 不得出现 `@Preview` / ui-tooling**（G3，`M0-6 起`）；预览只写 `src/debug` 一份。

### 13.4 步骤 ③：骨架与关键实现要点

按 `docs/SPEC.md` §2.8（内部实现）逐条落地，**顺序固定**：

1. **签名**：`explicitApi()` 已开 ⇒ 所有公开声明必须显式 `public` + 返回类型；参数顺序照 §2.2/§2.3。
2. **槽位**：只用 §2.4 的 **21 名共享词表**（`content`/`header`/`leadingIcon`/`label`/`control`…）；**无槽位显式写"无"**；需要 `weight` 时才用 receiver。
3. **状态机**：把 §2.6 的优先级串落成**单一**派生（`disabled > loading > pressed > focused > hover > default`）；`loading` **不用** `enabled=false` 表达。
4. **`interactionSource`**：唯一按下源（`clickable`/`toggleable` 自带），**禁止**自造 `pointerInput` 按下态或第二个 source。
5. **`Modifier` 顺序**：先外部 `modifier`，再 `wdTouchTarget()`（热区 = 布局盒，不覆盖相邻行），再视觉（`clip` → 背景/玻璃 → `border` → `shadow`），最后语义；`Modifier` 工厂**不读** local/theme（禁 `Modifier.composed`）。
6. **令牌**：任何尺寸/颜色/时长走 `WDSize/WDColorScheme/WDMotion/…`；**裸值 = G2 fail**（白名单见 [`../AGENTS.md`](../AGENTS.md) §10 第 1 行 → 白名单详情在 [`ARCHITECTURE.md`](ARCHITECTURE.md) §11）。
7. **m3 边界**：组件**不得**引 `androidx.compose.material3`（全库 import 计数 **== 1**，只允许 `foundation/theme/WDMaterialScheme.kt`）。
8. **稳定性**：公开参数类型加 `@Immutable`/`@Stable`（或使用稳定类型）；列表项不要 `remember` 缓存派生值。

### 13.5 步骤 ④：测试怎么写（**六类断言，缺一不可**）

| # | 类别 | 写在哪 | 必含断言（示例形态） |
| --- | --- | --- | --- |
| 1 | **API 形状 / ABI** | 由 `apiDump` 记录，不手写 | 公开签名与 `docs/SPEC.md` §2.2/§2.3 一致；`api/wisdom-ui.api` diff 只出现预期增删 |
| 2 | **状态优先级 / 交互** | 单测（Robolectric 单测或 Compose 测试） | 同一串输入 → 同一可见状态（§2.6）；`loading` 留在无障碍树；`disabled` 吞输入 |
| 3 | **边界场景** | 单测 | §2.7 逐条（空数据不渲染占位、`count = 0` 隐藏、超长文本、`fontScale` 极值、RTL） |
| 4 | **语义树** | Compose 测试 | `assertIsButton`/`assertIsNotEnabled`/`assertContentDescriptionEquals`；热区断言（布局盒 48）；松手/高频交互的触觉与播报次数 |
| 5 | **六态截图** | screenshot 测试（`M1 起`） | 浅/深 × 默认/2.0 档 × LTR/RTL；基线入库后再改视觉 = 必须解释 diff |
| 6 | **`fontScale` / 行盒** | 单测 + 真机 | 2.0 档：不截断/不重叠/行数变化；默认档 `abs(rendered − max(设计, natural)) ≤ 1px`；行高件另测「可见内容 44 ± 0.5 / 布局盒 48」 |

> **`apiDump` 也当断言用**：任何"我以为没改签名"的改动，最终由 `api/wisdom-ui.api` 的 diff 说话（§13.7）。

### 13.6 步骤 ⑤：本地跑什么（命令 + 期望）

| 命令 | 现状 | 期望 |
| --- | --- | --- |
| `./gradlew :wisdom-ui:compileDebugKotlin` | `现成` | 编译通过；无 warning 新增（`-Werror` 未开，但稳定性告警要清零） |
| `./gradlew :wisdom-ui:testDebugUnitTest --tests '*<组件>*'` | `现成` | 全绿；报告在 `wisdom-ui/build/reports/tests/testDebugUnitTest/index.html` |
| `./gradlew :wisdom-ui:validateDebugScreenshotTest` | `M1 起`（任务名待实测 V14） | 截图通过；差异图在 `build/reports/…`（首次落地即基线） |
| `./gradlew :demo:installDebug` | `M1 起` | 真机/模拟器可交互；`fontScale 1.3/2.0` 手验（`adb shell settings put system font_scale 2.0`） |
| `./gradlew :wisdom-ui:apiDump` | `现成` | 生成/更新 `wisdom-ui/api/wisdom-ui.api`；**与实现同提交** |

### 13.7 步骤 ⑥：PR 前跑什么（阻塞合并两条 + 分层）

```bash
cd android
# ① 基线不漂（最容易被忽略、也最容易被 CI 抓）
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
# ② 复合门禁（M0-6 起；今天不存在 —— 用 §4.0 的现成任务替代，见下）
./gradlew wisdomGate
# ③ M0-6 之前的等价四连（现成）
./gradlew :wisdom-ui:apiCheck :wisdom-ui:assembleRelease :wisdom-ui:assembleDebug :wisdom-ui:testDebugUnitTest
# ④ m3 边界（手工判据，review 要贴）
grep -rl "androidx.compose.material3" wisdom-ui/src/main/kotlin | wc -l   # 必须 == 1
```

**`apiCheck` 当前是红的（27 getter vs 源码 31）**：M0-4 的 **A1 + A3 同提交**六步序列（§2.1.1）跑完之前，`apiCheck` 的红色**不是本件的回归**——本件只要求"自己引入的 diff 是预期的"，并**在批出口前**随 M0 收口。

### 13.8 步骤 ⑦：PR 与评审要求

| 要求 | 细则 |
| --- | --- |
| 分支与合并 | 短分支 `feat/<组件>`；**必须 rebase merge，禁 squash**（M0-3 纯移动 / M0-4 语义 两个提交要可独立审计） |
| 提交信息 | Conventional Commits + 两行必填：`变更集: tokens: vX.Y.Z`（碰令牌时）、`ABI: 无变化 \| 只增 \| 签名变化`（碰公开签名时） |
| `apiDump` | **与实现同提交**；PR 描述贴类型级摘要（新增/删除/改签名各几行） |
| review 人数 | 碰 `api/wisdom-ui.api`、`contracts/**`、CI/发布配置 ⇒ **2 人**，其中 1 名 = `android-lead` 或 `tech-lead` |
| PR 六段 | 变更类型 / 变更集标识 / 影响面（含 apiDump 摘要）/ 门禁输出 / 证据（截图 + 语义树）/ 迁移片段（仅 Breaking） |

### 13.9 步骤 ⑧：批前冻结与批出口

1. **批前签名冻结（每批第 0 步，不得跳过）**：本批每件签名 + `contracts/<component>.yaml` 的 `params`/`slots`/`default` 入库，与 C-15 逐行 **0 不一致**（`docs/SPEC.md` §9.1-V18：**C-15 未落库前 M2 冻结不得启动**）。
2. **批内**：每件按本 SOP 走完 ①–⑦，PR 逐件合并；`wisdomGate` 批内每日至少一次。
3. **批出口**：跑 §14 对应批的**出口清单 + 验收命令**，出**出口报告**（含命令原文与输出）。
4. **批出口后**：更新 §12.1 的状态列（`☑`）；把未闭合项写进 §8.2 未验证清单（**不得写"已通过"**）。

### 13.10 SOP 自检（可复制）

```bash
# ① 本 SOP 八步齐全
test "$(grep -cE '^### 13\.[0-9]+ 步骤|^### 13\.1 ' docs/DEV-PLAN.md)" -ge 8 && echo "OK: 8 步齐"
# ② 六类断言表存在且 ≥6 行
grep -cE '^\| [1-6] \| \*\*' docs/DEV-PLAN.md
# ③ 每条命令标了现状（现成 / M0-6 起 / M1 起 / 【待实测】）
grep -c '现成' docs/DEV-PLAN.md
```

---

## 14. 验收手册（逐批：出口判据 + 可运行命令 + 不通过处置 + 检查清单）

> **三层门禁与命令现状**见 §4；本节是**按批次的"结批怎么验"**。所有命令都在 `android/` 下跑。
> **现状图例**：`现成` = 今天可跑；`M0-6 起` = 门禁落地后；`M1 起` = `:demo`/screenshot 落地后；`【待实测】` = 任务名/路径以首次实测为准。
> **执行约定（cwd / 跨仓 / 占位符）**：本节命令**一律在 `android/` 下执行**（先 `cd android` 再逐条复制）；**跨仓路径必须显式写 `../wisdomdesign/…`**（同级的 `iOS/` 同理写 `../iOS/…`），不要用 `../../…` 之类的相对猜测；**占位符**（如 `<组件>`）必须**加引号**或**替换为实际路径**——未加引号会被 shell 当成输入重定向（报 `bash: 组件: No such file or directory`）。
> **契约时点与守卫**：`../wisdomdesign/contracts/` **当前不存在**（设计仓今天只有 `docs/`、`tokens/`、`tools/`），契约文件属 **M0-5 产出**。因此涉及契约的命令一律**前置 `test -d ../wisdomdesign/contracts` 守卫**：未落库时按「**M0-5 待办**」记账并跳过，**不得**以「文件不存在」判失败；M0-5 落库后必须逐条复跑。


### 14.0 每批都先跑的三条（通用）

```bash
# ① 文档与本文件校验（本文件 §11.1 + §18，改动文档时必跑）
bash -c 'test -s docs/DEV-PLAN.md && grep -qE "^#{2,3} .*目录" docs/DEV-PLAN.md'
# ② 基线不漂（现成）
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
# ③ 现成四连（M0-6 之前等同 PR 门禁）
./gradlew :wisdom-ui:apiCheck :wisdom-ui:assembleRelease :wisdom-ui:testDebugUnitTest
```

**不通过怎么办（通用）**：① diff 非空 ⇒ 先判断是不是**预期改动**（预期 ⇒ 同提交带 `apiDump` 一起进 PR；非预期 ⇒ 回退实现，禁止手改 `api/wisdom-ui.api`）；② 编译/单测失败 ⇒ 先看 `wisdom-ui/build/reports/**`，再按 §15.4 排查；③ 命令不存在（`wisdomGate`/`:demo`）⇒ **不算失败**，按 §4.0 用现成替代。

### 14.1 M0 — 规范冻结（本仓部分 M0-3…M0-10；出口判据 = §2.1 的 5 条）

| # | 出口判据（§2.1） | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ③ | `apiCheck` 绿且 `apiDump` 与生成物同提交 | `./gradlew :wisdom-ui:apiCheck` + `git status --porcelain -- wisdom-ui/api/wisdom-ui.api` | exit 0；`apiDump` 改动已在同一提交里 | `现成` |
| ④ | `wisdomGate` 一次跑绿（G1–G13） | `./gradlew wisdomGate` | exit 0；16 条 `wisdomCheck*` 全绿 | `M0-6 起` |
| ⑤ | `README.md` 四处改写完成 | `grep -nE '玻璃\|wisdomGate\|未量化\|无障碍' README.md` | 四处口径与 P-1/U4/U8 一致 | `现成`（人工核） |
| ⑦ | 目录树与命名（`patterns/` 已删、`WDBottomSheet`/`WDBottomSheetDetent(s)`） | `ls wisdom-ui/src/main/kotlin/…/components` + `grep -rn 'WDSheet' wisdom-ui/src/main/kotlin \| wc -l` | 无 `patterns/` 包；`WDSheet` 命中 **0** | `现成` |
| ⑨ | `tools/size-report.sh` + 空 `:demo` 骨架 + 测量口径成文（两个数字在 M1 出口） | `bash tools/size-report.sh --help`；`./gradlew :demo:assembleDebug` | 脚本可跑；`:demo` 能编（两个数字 M1 给） | `M1 起` |

**M0 检查清单**：`☐ apiCheck 由红转绿（A1+A3 同提交，六步序列 §2.1.1）` `☐ G1–G13 全绿` `☐ README 四处改写` `☐ 三处改名完成` `☐ size-report + demo 骨架` `☐ U4 两个数字登记为 M1 出口项（未量化）`

### 14.2 M1 — 基建与可验证性（本端任务见 §2.2）

| 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- |
| `:demo` 可编可跑 | `./gradlew :demo:assembleDebug` | exit 0 | `M1 起` |
| 真机 `fontScale 2.0` 观感 | `adb shell settings put system font_scale 2.0` 后手验 | 不截断/不重叠/层级保持 | `【待实测】` |
| 弹簧 μ 并排评审 | `adb shell settings put global animator_duration_scale 0/1` 对比 | 三档（gentle/snappy/bouncy）观感一致，μ = 1.0 | `M1 起` |
| 六态截图入库 | `./gradlew :wisdom-ui:validateDebugScreenshotTest` | 通过并生成基线 | `M1 起`（任务名 V14） |
| `WDLayout`/`WDTheme` 字阶 | `./gradlew :wisdom-ui:testDebugUnitTest --tests '*WDTheme*'` | `WDTheme { Text("x") }` 字阶 == `WDType.body` | `现成` |
| **E2 + m3 dex 占比两个数字** | `tools/size-report.sh --dex` + `apkanalyzer dex packages …` | 两个数字进 CHANGELOG | `M1 起` |
| `WDLineBoxTest`（zh/en natural） | `./gradlew :wisdom-ui:testDebugUnitTest --tests '*WDLineBoxTest*'` | 全绿（V12 未实测前不得写"已达标"） | `M1 起` |

**M1 检查清单**：`☐ demo 可跑` `☐ 2.0 档手验` `☐ 截图基线入库` `☐ 两个数字（E2/占比）` `☐ V1/V12/V14 状态回填` `☐ U4 收益口径"已量化 or 未量化"`

### 14.3 M2 — 基础批（11 件；出口 = §2.3 的 6 条）

| # | 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ① | 批前冻结：`params[].name` 与 C-15 逐行一致 + `allCases` 归一化绿 | `test -d ../wisdomdesign/contracts && ls ../wisdomdesign/contracts/*.yaml \| wc -l`；`grep -c 'WD' "../wisdomdesign/contracts/<组件>.yaml"` | 11 件契约齐；逐行 0 不一致；**未落库时脚本输出「M0-5 待办」= 不算失败** | `M0-5 起`（C-15 未落库前**冻结不得启动**） |
| ② | `wisdomGate` 绿 | `./gradlew wisdomGate` | exit 0 | `M0-6 起` |
| ③ | nightly：六态截图 + 无障碍断言（热区 / 两态宽度 / `saver` 三条） | `./gradlew :wisdom-ui:validateDebugScreenshotTest` + `--tests '*Semantics*'` | 全绿 | `M1 起` |
| ④ | B1/B2/A3 数字进报告（只报） | `./gradlew :benchmark:…`（任务名 V14/`【待实测】`） | 报告含数字 | `M1 后` |
| ⑤ | 契约用例名 + `icons.json` 对齐 | `test -d ../wisdomdesign/contracts && grep -c 'WD' ../wisdomdesign/contracts/icons.json` | 44 条语义名对齐 | `M0-5 起` |
| ⑥ | 冒烟退役 | `./gradlew :demo:assembleRelease` | R8 冒烟通过 | `M1 起` |

**M2 检查清单**：`☐ 11 件签名冻结` `☐ 逐件走完 §13 SOP` `☐ apiDump 与实现同提交（每件）` `☐ 六态截图（11 件）` `☐ 热区断言 = 布局盒 48` `☐ §12.1 状态列更新为 ☑`

### 14.4 M3 — 封板批（1 件 + 封板动作；出口 = §2.4 的 6 条）

| # | 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ① | 公开 API 冻结（`apiDump` 入库） | `./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api` | 零 diff（基线已入库） | `现成` |
| ② | `$default` 规则生效 | `grep -c '\$default' wisdom-ui/api/wisdom-ui.api` | 计数记入基线；新增带默认值参数 = 需评审 | `现成` |
| ③ | 三处改名已完成 | `grep -rn 'WDSheet\|Generated/' wisdom-ui/src/main/kotlin \| wc -l` | **0** | `现成` |
| ④ | Kover 首次接入 + 门槛 | `./gradlew :wisdom-ui:koverHtmlReport`（任务名 `【待实测】`） | 报告生成、门槛生效 | `M0-6 起` |
| ⑤ | 体积门槛值定 | `tools/size-report.sh --dex` | 门槛值写入 §8.1 风险登记 | `M1 起` |
| ⑥ | **基础批 11+1 件全完成 = 12/20 primitives** | `grep -c 'primitives' docs/DEV-PLAN.md` + 人工核 §12.1 状态列 | 12 件 `☑`；余 8 件登记在 §3.1 | `现成` |

**M3 检查清单**：`☐ 12/20 primitives 完成` `☐ ABI 基线冻结` `☐ $default 计数记档` `☐ 覆盖率门槛` `☐ 体积门槛值定` `☐ 未达标项进 §8.2`

### 14.5 M4 — 反馈与弹层（9 件；出口 = §2.5 的 7 条）

| # | 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ① | 批前冻结（9 件） | 同 14.3-① | 0 不一致 | `M0-5 起` |
| ② | 玻璃档位单测 + 真机三路记录 | `./gradlew :wisdom-ui:testDebugUnitTest --tests '*WDGlass*'` | 四步判定 + 真机三路截图/录像 | `M0-6 起` |
| ③ | 效果配额单测（同屏 7 个 `WDSkeleton` → 第 7 个静态） | `--tests '*EffectsBudget*'` | 断言绿 | `M0-6 起` |
| ④ | 阴影与 `press-overlay-alpha` 视觉评审 | 人工 + 截图 | 评审通过并留证 | `M1 起` |
| ⑤ | **E2 ≤400 KB 转门槛** | `tools/size-report.sh --dex` | 数字 ≤ 门槛（否则不得结批） | `M1 起` |
| ⑥ | `publishToMavenLocal` + 独立小工程能编能渲 | `./gradlew :wisdom-ui:publishToMavenLocal` | 消费方工程可编可渲（V4） | `M4`（V4 未闭合前记"未验证"） |
| ⑦ | release AAR 预览符号 == 0 且 debug > 0 | `./gradlew :wisdom-ui:wisdomCheckReleaseSymbols` | G8 绿 | `M0-6 起` |

**M4 检查清单**：`☐ 9 件冻结` `☐ 玻璃三路记录` `☐ 配额单测` `☐ 视觉评审留证` `☐ E2 ≤400 KB` `☐ publishToMavenLocal 冒烟`

### 14.6 M5 — 导航与表单（15 件；出口 = §2.6 的 7 条）

| # | 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ① | 批前冻结（15 件） | 同 14.3-① | 0 不一致 | `M0-5 起` |
| ② | 2.0 档 + LTR/RTL 截图入库 | `./gradlew :wisdom-ui:validateDebugScreenshotTest` | 通过 | `M1 起` |
| ③ | 性能指标进报告 | `./gradlew :benchmark:…` | 报告含数字 | `M1 后` |
| ④ | 无障碍断言（`WDSlider` 松手播报一次；`WDStepper` 高频不给触觉；`WDTabBar` 允许 2 行） | `--tests '*Semantics*'` | 三条断言绿 | `M1 起` |
| ⑤ | 契约用例名 + 图标语义名仍绿 | `--tests '*Contract*'` + `icons.json` | 全绿 | `M0-5 起` |
| ⑥ | `V3`（sheet 键盘避让）已闭合 | 真机手验 | 键盘不遮输入 | `【待实测】` |
| ⑦ | **20/20 primitives 完成于本批出口（11+1+2+6）** | 人工核 §12.1（状态列） | 20 件 `☑` | `现成` |

**M5 检查清单**：`☐ 15 件冻结` `☐ 2.0 + RTL 截图` `☐ 三条无障碍断言` `☐ V3 闭合` `☐ 20/20 primitives` `☐ §12.1 状态全更新`

### 14.7 M6 — 场景 + 回归发布（1 件；出口 = §2.7 的 7 条）

| # | 出口判据 | 验收命令 | 期望 | 现状 |
| --- | --- | --- | --- | --- |
| ① | 批前冻结（1 件） | 同 14.3-① | 0 不一致 | `M0-5 起` |
| ② | 无障碍回归（TalkBack 闭眼走查 + demo 审计） | 真机 + `./gradlew :demo:assembleRelease` | 走查记录留档 | `M1 起` |
| ③ | 截图封板（37 件 × 浅/深 × 默认/2.0 档 × LTR/RTL） | `./gradlew :wisdom-ui:validateDebugScreenshotTest` | 全绿且基线冻结 | `M1 起` |
| ④ | 全量门禁 | `./gradlew wisdomGate` + 漂移检查 | 全绿 | `M0-6 起` |
| ⑤ | 发布前层 | `./gradlew :wisdom-ui:publishToMavenLocal` + `generatePomFileForMavenPublication` | POM 版本 == catalog | `M4 起` |
| ⑥ | **双端同 tag `v1.0.0`** | `git tag -l`（两端比对） | 两端同 tag、只增不改 | `M6` |
| ⑦ | 发布 checklist 完成 | §10.3 逐条 | 全部可判通过 | `M6` |

**M6 检查清单**：`☐ 1 件冻结` `☐ 无障碍回归留档` `☐ 截图封板` `☐ 全量门禁` `☐ 发布前层（POM 三查）` `☐ 双端同 tag` `☐ §10.3 全勾`

---

## 15. 开发者指南（环境 / 规范 / 提交 / 排障 / 升级）

> 本节面向"第一天接手本仓"的开发者。**版本真源 = `gradle/libs.versions.toml` + `wisdom-ui/build.gradle.kts`**；本节数字若与其冲突，**以真源为准**。

### 15.1 环境与工具链

| 项 | 值（真源） | 备注 |
| --- | --- | --- |
| JDK | **17**（`sourceCompatibility/targetCompatibility = VERSION_17`、`jvmTarget = JVM_17`） | 用 17；更高 JDK 需 `jvmToolchain(17)` 兜住 |
| Gradle | **8.14.5**（wrapper） | 一律用 `./gradlew`，不要用系统 gradle |
| AGP | **8.13.2**（`libs.versions.toml`） | 上限 compileSdk 36（升 AGP 9 才能升 BOM） |
| Kotlin | **2.4.20** | 与 Compose 编译器插件同版本 |
| Compose BOM | **2026.06.01** | 见 toml 注释：更高 BOM 要求 compileSdk 37 ⇒ 暂锁此版 |
| compileSdk / minSdk | **36 / 24** | minSdk 24 是冻结值（[`../AGENTS.md`](../AGENTS.md) §6 第 9 行） |
| 发布 / BCV | vanniktech **0.37.0** / binary-compatibility-validator **0.18.2** | BCV = `apiCheck`/`apiDump` 的实现者 |
| 测试 | JUnit **4.13.2**（+ Robolectric `M0-7 起`） | Robolectric 需 `src/test/resources/robolectric.properties`（`M0-7 起`） |
| 显式 API | `explicitApi()` **已开** | 公开声明必须显式 `public` + 返回类型 |
| ktlint / detekt | **今天未接入**（V3；`M0-6 起`） | 接入当日校准规则集并与 `.editorconfig` 对齐 |

```bash
java -version                                   # 期望 17.x
./gradlew --version                             # 期望 Gradle 8.14.5
./gradlew :wisdom-ui:compileDebugKotlin         # 冒烟：能编
```

### 15.2 代码规范

| 面 | 规则 |
| --- | --- |
| 格式 | ktlint（`M0-6 起`；未接入前按 4 空格缩进 + 尾逗号 + 无通配 import 手工对齐） |
| 静态检查 | detekt（`M0-6 起`）；Compose lint（同批接入，V3） |
| 包结构 | `components/{primitives,composites}/<组件>/`；`foundation/{theme,typography,generated,…}`；**层内不得反向依赖**（G1） |
| 命名 | 组件 `WD<Name>`；枚举 `WD<Name><Axis>`（如 `WDButtonVariant`）；state `WD<Name>State` + `rememberWD<Name>State`；参数名照 C-15（`docs/SPEC.md` §2.10） |
| 可见性 | 公开 API 一律显式 `public`；内部实现 `internal`（`explicitApi()` 下"不写 public = internal 不暴露"） |
| 令牌 | **禁裸值**（`16.dp`/`Color(0x…)`/`100ms`）；白名单见 [`ARCHITECTURE.md`](ARCHITECTURE.md) §11（G2） |
| m3 | 组件**不得** import `androidx.compose.material3`；全库引用计数 **== 1**（`foundation/theme/WDMaterialScheme.kt`） |
| 稳定性 | 公开参数类型 `@Immutable`/`@Stable`，或用稳定类型；**禁** `remember` 缓存派生值、`Modifier.composed`、自造 `interactionSource` |
| 文案/资源 | 库内**零资源零文案**（L-B）：无 `res/`、无 `strings.xml`、无图标资源（图标由调用方注入 `ImageVector`） |

### 15.3 提交、分支与 PR

```bash
git switch -c feat/WDButton          # 短分支：feat/ | fix/ | chore/ | tokens/
# 提交前必跑（见 §13.7）
./gradlew :wisdom-ui:apiDump && git diff --exit-code -- wisdom-ui/api/wisdom-ui.api
git commit -m "feat(WDButton): 初版实现" # 正文两行：变更集 + ABI
```

- **必须 rebase merge，禁 squash**：M0-3（纯移动）/M0-4（语义）两个提交要可独立审计。
- **`apiDump` 与实现同提交**：`api/wisdom-ui.api` 是二进制兼容基线，单独提交会让 CI 恒红。
- **PR 六段**：变更类型 / 变更集标识 / 影响面（含 apiDump 类型级摘要）/ 门禁输出 / 证据 / 迁移片段。
- **review 人数**：碰 `api/wisdom-ui.api`、`contracts/**`、CI/发布配置 ⇒ **2 人**（其中 1 名 `android-lead` 或 `tech-lead`）。
- **`$default` 纪律**：给**既有** public 函数加带默认值参数 = 二进制不兼容 ⇒ 走 major 或新增重载（§10.2）。

### 15.4 常见错误与排查（≥6 条）

| # | 症状 | 根因 | 处置 |
| --- | --- | --- | --- |
| 1 | `apiCheck` 红：`WDColors` 缺 4 个 getter（27 vs 源码 31） | **M0 之前的已知状态**（基线未重生成） | 按 §2.1.1 的 **A1 + A3 同提交六步序列**收口；**不要**手改 `api/wisdom-ui.api` |
| 2 | `Task 'wisdomGate' not found` / `Project ':demo' not found` | 门禁与 `:demo` 尚未落地（`M0-6` / `M1`） | **不算失败**：用 §4.0 的现成四连替代；`:demo` 用库单测 + 预览替代 |
| 3 | R8/minify 后消费方崩溃（类/成员被删） | 缺 keep 或反射/`internal` 误用 | 跑 `./gradlew :demo:assembleRelease`（唯一能证明不误删的手段）+ 看 `mapping/release/seeds.txt`；必要时补 `consumer-rules.pro`（G8） |
| 4 | Compose 稳定性告警 / `inferredUnstableClasses != 0` | 公开参数类型不稳定（`List`/`Map` 裸用）或漏 `@Immutable` | 给类型加 `@Immutable`/用 `ImmutableList`；跑 G5/G6（`M0-6 起`）并清零 |
| 5 | 改一个开关/主题值导致**整树重组** | 把高频值放进 `staticCompositionLocalOf`，或 `WDTheme(colors=…)` 变化触发全树 | 高频值改用 `compositionLocalOf`；scheme 切换是"换已生成 scheme"，**不进高频路径**（[`../AGENTS.md`](../AGENTS.md) §6 第 5 行） |
| 6 | 编译期找不到 `:demo` / 模块未识别 | `settings.gradle.kts` 只 `include(":wisdom-ui")` | M1 建 `:demo` 时补 `include(":demo")`；今天不要假设它存在 |
| 7 | `Could not resolve …` / `Permission denied`（`.gradle` 缓存） | 缓存目录权限/离线 | 用 `-g ./.gradle-home` 隔离缓存（`M0-10 起`）；公司镜像见 §4.5；不要 `chmod -R 777` 全局缓存 |
| 8 | 令牌/生成物被手改后 CI 恒红 | 手改 `foundation/generated/**` | **回滚**并改设计仓令牌 + 重跑生成器（G7 自证）；生成物只读 |
| 9 | 截图测试首次运行全红 | 基线尚未建立 | 首次落地即基线（§13.5 类别 5）；后续 diff 必须解释 |

### 15.5 遇到阻塞找谁（升级路径）

| 情形 | 找谁 | 需要带什么 |
| --- | --- | --- |
| 规格缺值 / 设计待给值（`D-1…D-16`） | **设计 + 架构师**（本文件 §7 的 owner 列） | 缺哪一项 + 默认动作影响面 + 是否阻塞 M0-1 |
| 契约/命名冲突（C-15 与实现不一致） | **架构师**（C-15 真源）+ `android-lead` | 逐行对照表（哪一行、期望值、现值） |
| 冻结值想改（§5 / [`../AGENTS.md`](../AGENTS.md) §6） | **研发 Leader / 架构师** 走变更流程 | 变更理由 + 影响（ABI/视觉/两端一致性）+ 回滚方案 |
| 跨端一致性（U 项）或新增 F 项 | **ios-lead + 架构师** | 该 U/F 的现状 + 两端形态 + 是否需登记 |
| 批/排期/关键路径阻塞 | **研发 Leader**（船长） | 阻塞点 + 已尝试 + 所需决策 |
| 门禁/CI/发布（BCV、POM、tag） | **tech-lead** | 命令 + 完整输出 + 期望 |
| 仓库权限 / 跨仓改动（`wisdomdesign/`、`contracts/`） | **架构师**（本仓不可写） | 需要改的文件与理由 |

> **通用纪律**：任何阻塞先在本文件 §8.1 风险登记 / §8.2 未验证清单落一行，**再**升级——不要口头阻塞、不要静默绕过。

---

## 16. 术语表与已退役代号解析

### 16.1 本仓常用术语

| 术语 | 含义 |
| --- | --- |
| **U / F** | U = 两端**必须统一**（`docs/SPEC.md` §4.2）；F = **各端自由但必须登记**（§4.1） |
| **C-15** | 37 行受控值参数名唯一表；落库形态 = `contracts/<component>.yaml: params[].name`（M0-5） |
| **G1–G13** | 本仓 13 条机器门禁，挂在 `wisdomGate` 下（§4.2） |
| **I-1…I-5** | 每批的开工判据（§2.0） |
| **P7–P12** | 规格/设计回写清单（§6） |
| **V1–V18** | 未验证项（§8.2），**实测前一律记"未验证"** |
| **案 B** | 行高最终口径：可见内容 **44** 两端同值；Android 布局盒 **48** = 44 + 上下各 **2dp** 透明内边距 |
| **L-B** | 库内零资源零文案（文案由调用方传入） |
| **A1 / A3** | M0-4 同提交的两件事：A1 = 用 `apiDump` 补齐 ABI 基线；A3 = `WDTextStyle` 4 参 + `@Immutable` + `internal constructor`（§2.1.1） |
| **E1 / E2 / E1' / F1** | 体积指标：E2 = 集成 APK 差（主指标，M4 起门槛）；E1 = AAR 字节趋势；E1' = 层包/批次 dex 增量；F1 = R8 后 dex 增量（**F1 双义**：F 注册表 `F1` = `WDBanner.visible` 的 `is` 前缀） |
| **`$default`** | Kotlin 默认参数的合成方法；给既有 public 函数加默认值参数 ⇒ 二进制不兼容（§10.2） |
| **explicitApi / BCV** | 显式公开 API 模式；BCV = binary-compatibility-validator（`apiDump`/`apiCheck`） |

### 16.2 两端差异术语（F 系列节选，本端相关）

| 号 | 差异 | Android 侧形态 |
| --- | --- | --- |
| **F41** | 日期选择载体 | Android 自研 `WDDatePicker`（弹层） |
| **F42** | 行盒容差 | Android **1px**（iOS 0.5pt） |
| **F43** | 阴影近似映射 | `WDElevation` → `Modifier.shadow`（5 行近似） |
| **F45** | 禁用态 | 40% 不透明度（`state.*`） |
| **F47** | 玻璃档位作为额外输入 | `WDGlassLevel` 六档（**不写进** U10 的 Inputs 句） |
| **F48** | 槽位量纲 | API 形态各端自由（名字/语义统一） |
| **F51** | **行距差异** | Android **48**（布局盒）/ iOS **44** |
| **DIR-1** | 枚举命名大小写 | Kotlin UpperCamel ↔ Swift lowerCamel（比较用归一化名） |

### 16.3 已退役代号解析（**只做历史追溯，不是引用**）

> 迁移前的跨端工作区文档集**已不再随仓提供**；下表把仍可能出现在旧记录里的**数字代号**映射到本仓等价物。**本仓的施工依据只有：`docs/SPEC.md` / 本文件 / [`../AGENTS.md`](../AGENTS.md) / [`ARCHITECTURE.md`](ARCHITECTURE.md)**。

| 代号 | 迁移前是什么 | 本仓等价物 |
| --- | --- | --- |
| `07` | 跨端契约终稿（U/F 清单 + 门禁） | 本文件 §9.2（U/F 节选）；[`../AGENTS.md`](../AGENTS.md) §6 |
| `08` | 用户决策集（10 项 + 8 条硬约束） | 本文件 §5（冻结值）；[`../AGENTS.md`](../AGENTS.md) §6 |
| `12` | 迁移前的本端实现规格（收敛稿） | **`docs/SPEC.md`**（节号与迁移前一致） |
| `20` / `21` | 跨端评审 / 架构裁决记录 | [`ARCHITECTURE.md`](ARCHITECTURE.md) §16（台账） |
| `27` | 架构师终审（可开工边界） | 本文件 §2.0（入口判据）+ `docs/SPEC.md` 终审状态 |
| `30` | 跨端总计划（进程 / 批次 / 冻结值口径 / 回写清单 / 设计待给值） | **本文件**（对应等价物 = §5 冻结值 · §6 回写项 · §7 待给值；原节号仅历史对照，不再引用） |
| `40` | C-15 受控值名唯一表 | `contracts/<component>.yaml`（M0-5 落库）+ 本文件 §12.1 + `docs/SPEC.md` §2.10 |
| `61` / `62` / `63` / `66` | 执行者视角评审 / 跨端审计 / 设计口径复核 | `ARCHITECTURE.md` §16 台账（逐条状态） |

---

## 17. 关键决策摘要（决策 + 来源）

| # | 决策（最终值） | 来源 / 效力 | 本文件落点 |
| --- | --- | --- | --- |
| 1 | 行高：**可见内容 44**（两端同值，**单值键**）；Android **布局盒 48 = 44 + 上下各 2dp**（`Modifier.wdTouchTarget()`，热区不覆盖相邻行） | 用户决策 #2（案 B）+ [`../AGENTS.md`](../AGENTS.md) §6 #1–#3 | §5、§12.1 |
| 2 | 行距差异登记 **F51**（Android 48 / iOS 44）；AR-67 = **补充解释**（原两条断言原样成立 + 新增"可见内容 44 ± 0.5"） | [`../AGENTS.md`](../AGENTS.md) §6 #3、§8 | §5、§9.2 |
| 3 | 色槽 **U12 = 32**（含 `text.disabled`）；类型名 `WDColorSlot` | 用户决策 #3 + [`../AGENTS.md`](../AGENTS.md) §6 #4 | §5、§8.2 |
| 4 | 换肤**只做层一**：多套生成 scheme + 运行时选择（`WDTheme(colors = …)`）；**不支持**运行时任意 `token.json` / 服务端下发 / **逐槽位任意覆盖** | 用户决策 #8 + [`../AGENTS.md`](../AGENTS.md) §6 #5 | §5、§6（P9） |
| 5 | 触控双键 44/48；**每端只生成本端常量**；一次性改名不留过渡键 | 用户决策（U8）+ §5 | §5、§12.1 |
| 6 | m3 收缩：`material3 → implementation`；BOM 留 `api`；公开签名不得出现 m3 类型；**全库 import 计数 == 1** | 用户决策（U4）+ §4.2（G9/G1） | §5、§15.2 |
| 7 | `apiCheck` **当前为红**（27 vs 31）；转绿 = **A1 + A3 同提交六步序列**（M0 出口项 ③） | 实测 + §2.1.1 | §2.1.1、§13.7、§14.1 |
| 8 | 库内零资源零文案（L-B）；图标由调用方注入 `ImageVector`（44 条语义名 + `mirrorsInRTL`） | 用户决策（U8）+ §5 | §5、§15.2 |
| 9 | 进程：**批 ↔ 批不可并行**；每批第 0 步 = **批前签名冻结**（不得跳过）；批出口 = 命令 + 报告 | 迁移前跨端总计划（已退役）→ 本文件 §2/§3 | §2、§3、§13.9、§14 |
| 10 | 提交纪律：**rebase merge 禁 squash**；`apiDump` 与实现同提交；碰 ABI/契约/CI ⇒ 2 人 review | §10.2 + [`../AGENTS.md`](../AGENTS.md) §9 | §10.2、§13.8、§15.3 |
| 11 | **进程优先、不算人力**（用户决策 #1）：组织轴 = 顺序 / 入口出口判据 / 门禁强度 / 依赖与关键路径；**工作量（人日 / 档位）不作判据**，§12 的"档位"列据此改为 **`关键路径件`（是/否）** | 用户决策 #1（vibe coding，不计算人力） | §12.0、§12.2、§3.1、§18 |

---

---

## 19. 设计文档与设计图查阅指南

> **一句话**：本文件、`../AGENTS.md`、`docs/SPEC.md` 回答"**怎么实现、怎么验收**"；外部设计仓 `../wisdomdesign/` 回答"**长什么样、为什么这样**"。本节是设计仓的**地图**——什么时候去翻、翻哪一份、怎么翻。

### 19.1 设计仓结构与只读纪律

| 位置（相对本仓） | 是什么 | 能否改 |
| --- | --- | --- |
| `../wisdomdesign/docs/` | 规范文档 **13 份**（清单见 §19.2） | ❌ **只读** |
| `../wisdomdesign/docs/specs/` | **逐组件详细规格 4 份**（01–37 + 配方 R1–R5） | ❌ 只读 |
| `../wisdomdesign/design/gallery/` | 可视化画廊 **3 份**（自包含 HTML，浏览器直接打开） | ❌ 只读 |
| `../wisdomdesign/design/preview/` | 主题预览 **1 份** | ❌ 只读 |
| `../wisdomdesign/tokens/wisdom.tokens.json` | **令牌唯一真源**（色值只在这里出现一次） | ❌ 只读（经 M0-1 冻结流程） |

**只读纪律**：本仓任何角色**不得修改** `../wisdomdesign/**`。发现设计侧需要给值/改稿 ⇒ 走 §15 的升级路径登记，由设计与架构师裁决后按 P 项回写。

**命名口径提醒**：设计文档写"两端同名 `WDButton` / `WdButton`"，本端实际命名**以 `docs/SPEC.md` §2.10 与 [`../AGENTS.md`](../AGENTS.md) §6 为准**（本仓为 `WD*`）；设计侧写短名（`Button`）时按**设计编号**对照，不按字面大小写推断。

### 19.2 每份设计文档回答什么问题

| 文档 | 行数 | 回答什么问题 | 什么时候读 |
| --- | --- | --- | --- |
| `docs/01-foundation.md` | 409 | 设计原则、Liquid Glass 基调、色彩 / 字体 / 间距 / 形状 / 高度材质 / 图标 | **动手前必读**：§3 色彩、§4 字体、§5 间距、§6 圆角、§7 高度与材质 |
| `docs/02-components.md` | 112 | 组件总清单（A–F 分类）、优先级、**编号 → 画廊**对照 | 找"有哪些组件 / 先做哪个 / 画廊在哪" |
| `docs/03-platform-mapping.md` | 314 | 令牌 → SwiftUI / **Compose** 映射、命名对照、平台差异、**新增组件的工作流** | 查"这个令牌在本端用什么 API"、新增组件 |
| `docs/04-architecture.md` | 152 | 仓库划分、组件分层、命名规范、版本与兼容、分发、令牌流水线 | 结构性问题（分层 / 命名 / 分发） |
| `docs/05-preview-and-theme.md` | 88 | 预览形态、规格画廊、用例清单、主题 | 写 Preview、做主题换肤 |
| `docs/06-accessibility.md` | 285 | 无障碍四条底线、屏幕阅读器、动态字体、对比度、减弱动态效果、触觉 | **验收前必读**（每条都进断言） |
| `docs/07-content.md` | 239 | 文案：三条原则、句式、时间与日期、长度上限、禁忌词、中英混排 | 组件含文字时（**注意 L-B：库内零文案，文案由调用方给**） |
| `docs/08-icons.md` | 207 | 图标：来源、尺寸与线宽、颜色、**语义对照表**、新增流程、用法与禁止用法、验收清单 | 任何用图标的地方（44 条语义名 → 本端 `ImageVector` 注入） |
| `docs/09-layout.md` | 196 | 页面骨架、边距与栅格、纵向节奏、密度、多设备、键盘与安全区、滚动、列表长度 | 布局类组件（Card / ListRow / Sheet / NavigationBar / TabBar） |
| `docs/10-review-summary.md` | 368 | v1.0 评审总览与开工决策（历史留档） | 追溯"为什么这么定" |
| `docs/11-a2-dark-canvas.md` | 279 | 深色 canvas 目标色值（提案 + 深色字阶 × 底色对比度表） | 深色主题相关（`WDTheme` 深色表） |
| `docs/12-b22-glass.md` | 174 | **玻璃唯一真源与可用边界**、三处输入更正、降级口径（含 Android 三路降级） | **所有玻璃组件**（六档 × 文字可用矩阵） |
| `docs/13-tint.md` | — | 色调（tint）口径 | 语义色 / tint 相关 |
| `docs/specs/README.md` | 109 | **规格模板（12 节）**、编写规则、编号索引、配方 R1–R5 | **每接一个组件的第一份** |
| `docs/specs/01-basic.md` | 1941 | **01–20 详细规格**（每件按 12 节写，缺一节不算完成） | 做 01–20 任一件 |
| `docs/specs/02-advanced.md` | 1642 | **21–36 详细规格** | 做 21–36 任一件 |
| `docs/specs/03-patterns.md` | 492 | **37 AssigneePicker** + 配方 R1–R5（配方按"撞边界数据"验收） | 做 37、做场景配方 |
| `design/gallery/wisdom-components.html` | 159 KB | **01–20 可视化画廊**（自包含） | 看视觉基线 |
| `design/gallery/wisdom-advanced.html` | 139 KB | **21–36 可视化画廊** | 同上 |
| `design/gallery/wisdom-patterns.html` | 106 KB | **37 + R1–R5 场景画廊** | 同上 |
| `design/preview/wisdom-light.html` | 132 KB | 主题预览（浅色） | 主题 / 换肤观感 |

### 19.3 按任务查场景到章节

| 我要做的事 | 按顺序读 |
| --- | --- |
| **接一个新组件** | `specs/README.md`（12 节模板与编号）→ 本文件 **§20.2** 查它的设计编号 → `specs/0X-*.md` 该件的 12 节 → `01-foundation.md` §3–§7 → 该件涉及的无障碍 / 布局 / 图标 / 文案章节 → **画廊对应编号** |
| 改主题 / 换肤 | `05-preview-and-theme.md` §4 → `13-tint.md` → `01-foundation.md` §3 色彩 → `tokens/wisdom.tokens.json` → 本文件 §5 的 `schemes` 三条硬边界 |
| 玻璃相关 | `12-b22-glass.md` 全篇 → `06-accessibility.md` §5 对比度 → 本文件 §5 的玻璃硬规则（含三路降级） |
| 写无障碍断言 | `06-accessibility.md` §2–§8 → 该件规格第 **10 节** → 本文件 §14 的对应断言 |
| 布局 / 密度 | `09-layout.md` §2–§6 → 该件规格第 **3 / 6 节** → 本端 `fontScale` 2.0 档 |
| 图标 | `08-icons.md` §4 语义对照表 + §6 用法 / §7 禁止用法（本端由调用方注入 `ImageVector`） |
| 文案 | `07-content.md` §2 / §4 / §6（**库内零文案**，文案由调用方注入） |
| 查令牌取值 | `tokens/wisdom.tokens.json`（唯一真源）→ `03-platform-mapping.md` 查本端 Compose API |

### 19.4 检索命令复制即用

```bash
cd ../wisdomdesign

# 某个组件在哪些设计文档里出现
grep -rln 'BottomSheet' docs

# 某个令牌的定义与使用
grep -rn 'size.control.md' docs tokens/wisdom.tokens.json | head

# 某条无障碍底线在规范里的位置
grep -n '对比度\|动态字体\|焦点' docs/06-accessibility.md | head

# 取某件规格的某一节（例：26 BottomSheet 的验收清单）
awk '/^# 26 · BottomSheet/,/^# 27 · /' docs/specs/02-advanced.md | grep -n '验收清单'

# 组件编号 → 规格锚点（GitHub slug：连续连字符不折叠）
#   01 → docs/specs/01-basic.md#01--button
#   21 → docs/specs/02-advanced.md#21--segmentedcontrol
#   37 → docs/specs/03-patterns.md#37--assigneepicker
```

### 19.5 设计文档与仓内文档的关系

- **效力分工**：设计稿 = **视觉与规范的依据**；`docs/SPEC.md` = **实现与验收的判据**；[`../AGENTS.md`](../AGENTS.md) = **操作与禁止项**。三者冲突时 ⇒ **库行为以 SPEC（与手册）为准、视觉以设计稿为准**，并把冲突**登记为差异项（F 号）**，不自行取舍。
- **跨端必须一致项**（改动须走契约流程）：行高可见内容 **44 单值键**、布局盒 **48 = 44 + 上下各 2dp**、**32 槽位**、`schemes` **三条硬边界**、弹簧 canonical **μ = 1.0**、图标 **44 条语义名 + 调用方注入**、**L-B 零文案**。
- **只读原则的例外**：只有 **M0-1 令牌冻结窗口**（只开一次）与**经裁决的 P 项回写**可以触碰设计仓，且必须由登记 owner 执行。

---

## 20. 逐组件设计溯源表

### 20.1 怎么用三步

1. 在本文件 **§12.1** 找到该件（批次 / 关键路径件 / 落点 / `SPEC.md` 锚点 / C-15 名 / 验收代码 / 依赖）；
2. 在**本表**找到它的**设计编号**，按「设计规格」列的锚点打开该件的 **12 节规格**；
3. 按「画廊」列的**文件名 + 检索词**在浏览器里打开画廊并搜索，与实现并排比对（核验方法见 §21.2/§21.3）。

### 20.2 溯源表 37 件

| WD 组件 | 设计编号 | 设计规格（锚点） | 画廊文件 · 检索词（实测命中） | 设计优先级 | 本端批次 |
| --- | --- | --- | --- | --- | --- |
| `WDButton` | 01 | `specs/01-basic.md#01--button` | `wisdom-components.html` · `Button`（6） | P0 | M2 |
| `WDIconButton` | 02 | `specs/01-basic.md#02--iconbutton` | `wisdom-components.html` · `IconButton`（3） | P0 | M2 |
| `WDTextField` | 03 | `specs/01-basic.md#03--textfield` | `wisdom-components.html` · `TextField`（3） | P0 | M2 |
| `WDSearchField` | 04 | `specs/01-basic.md#04--searchfield` | `wisdom-components.html` · `SearchField`（3） | P0 | M5 |
| `WDSwitch` | 05 | `specs/01-basic.md#05--switch` | `wisdom-components.html` · `Switch`（3） | P0 | M2 |
| `WDCheckbox` | 06 | `specs/01-basic.md#06--checkbox` | `wisdom-components.html` · `Checkbox`（3） | P0 | M2 |
| `WDRadio` | 07 | `specs/01-basic.md#07--radio` | `wisdom-components.html` · `Radio`（3） | P1 | M5 |
| `WDSlider` | 08 | `specs/01-basic.md#08--slider` | `wisdom-components.html` · `Slider`（3） | P0 | M5 |
| `WDStepper` | 09 | `specs/01-basic.md#09--stepper` | `wisdom-components.html` · `Stepper`（3） | P1 | M5 |
| `WDChip` | 10 | `specs/01-basic.md#10--chip` | `wisdom-components.html` · `Chip`（3） | P0 | M5 |
| `WDBadge` | 11 | `specs/01-basic.md#11--badge` | `wisdom-components.html` · `Badge`（3） | P0 | M2 |
| `WDAvatar` | 12 | `specs/01-basic.md#12--avatar` | `wisdom-components.html` · `Avatar`（5） | P0 | M2 |
| `WDAvatarStack` | 13 | `specs/01-basic.md#13--avatarstack` | `wisdom-components.html` · `AvatarStack`（2） | P0 | M5 |
| `WDDivider` | 14 | `specs/01-basic.md#14--divider` | `wisdom-components.html` · `Divider`（3） | P1 | M2 |
| `WDProgressBar` | 15 | `specs/01-basic.md#15--progressbar` | `wisdom-components.html` · `ProgressBar`（2） | P0 | M4 |
| `WDProgressRing` | 16 | `specs/01-basic.md#16--progressring` | `wisdom-components.html` · `ProgressRing`（2） | P0 | M4 |
| `WDCard` | 17 | `specs/01-basic.md#17--card` | `wisdom-components.html` · `Card`（3） | P0 | M2 |
| `WDListRow` | 18 | `specs/01-basic.md#18--listrow` | `wisdom-components.html` · `ListRow`（3） | P0 | M2 |
| `WDListSection` | 19 | `specs/01-basic.md#19--listsection` | `wisdom-components.html` · `ListSection`（3） | P0 | M3 |
| `WDIcon` | 20 | `specs/01-basic.md#20--icon` | `wisdom-components.html` · `Icon`（6） | P0 | M2 |
| `WDSegmentedControl` | 21 | `specs/02-advanced.md#21--segmentedcontrol` | `wisdom-advanced.html` · `SegmentedControl`（4） | P0 | M5 |
| `WDPicker` | 22 | `specs/02-advanced.md#22--picker` | `wisdom-advanced.html` · `Picker`（10） | P1 | M5 |
| `WDDatePicker` | 23 | `specs/02-advanced.md#23--datepicker` | `wisdom-advanced.html` · `DatePicker`（3） | P1 | M5 |
| `WDFormRow` | 24 | `specs/02-advanced.md#24--formrow` | `wisdom-advanced.html` · `FormRow`（3） | P1 | M5 |
| `WDAlert` | 25 | `specs/02-advanced.md#25--alert` | `wisdom-advanced.html` · `Alert`（6） | P0 | M4 |
| `WDBottomSheet` | 26 | `specs/02-advanced.md#26--bottomsheet` | `wisdom-advanced.html` · `BottomSheet`（3） | P0 | M4 |
| `WDActionSheet` | 27 | `specs/02-advanced.md#27--actionsheet` | `wisdom-advanced.html` · `ActionSheet`（3） | P1 | M4 |
| `WDToast` | 28 | `specs/02-advanced.md#28--toast` | `wisdom-advanced.html` · `Toast`（4） | P0 | M4 |
| `WDBanner` | 29 | `specs/02-advanced.md#29--banner` | `wisdom-advanced.html` · `Banner`（3） | P0 | M4 |
| `WDEmptyState` | 30 | `specs/02-advanced.md#30--emptystate` | `wisdom-advanced.html` · `EmptyState`（3） | P1 | M4 |
| `WDSkeleton` | 31 | `specs/02-advanced.md#31--skeleton` | `wisdom-advanced.html` · `Skeleton`（3） | P1 | M4 |
| `WDPullToRefresh` | 32 | `specs/02-advanced.md#32--pulltorefresh` | `wisdom-advanced.html` · `PullToRefresh`（3） | P1 | M5 |
| `WDNavigationBar` | 33 | `specs/02-advanced.md#33--navigationbar` | `wisdom-advanced.html` · `NavigationBar`（3） | P0 | M5 |
| `WDTabBar` | 34 | `specs/02-advanced.md#34--tabbar` | `wisdom-advanced.html` · `TabBar`（4） | P0 | M5 |
| `WDToolbar` | 35 | `specs/02-advanced.md#35--toolbar` | `wisdom-advanced.html` · `Toolbar`（4） | P1 | M5 |
| `WDFAB` | 36 | `specs/02-advanced.md#36--fab` | `wisdom-advanced.html` · `FAB`（3） | P1 | M5 |
| `WDAssigneePicker` | 37 | `specs/03-patterns.md#37--assigneepicker` | `wisdom-patterns.html` · `AssigneePicker`（2） | P1 | M6 |

**合计 37 行**（与 §12.1 的 37 件一致）；设计编号 01–37 **无缺号**。

### 20.3 关于实测命中数与锚点

- 画廊是**自包含 HTML（无构建步骤）**，且**没有逐组件锚点** ⇒ 定位方式是**浏览器内搜索组件名**（Ctrl+F）。
- 「命中数」是**本次实测值**（对画廊文件做 `grep -c`），用途是**确认搜到的是不是同一处**：命中 **0** 说明该组件在这张画廊里没有独立展示（例如 `WDDivider` 多作为其他组件的分隔出现），此时**以规格文件为准**，必要时在批出口报告里登记"画廊未独立展示"。
- 组件在设计侧的**编号**是稳定标识（`specs/` 的锚点与之一一对应）；**画廊内的滚动位置不稳定**，不要引用"第 N 屏"。

---

## 21. 从设计规格到实现与验收

> 设计规格每件都按 **12 节**写（`specs/README.md` 的模板，缺一节不算完成）。本节把它**逐节映射**到本端要产出什么、对应哪类断言，避免"读完了规格但不知道该写什么"。

### 21.1 设计规格 12 节到本端产出

| 设计规格第 N 节 | 它规定什么 | 本端落到哪里 | 对应断言 / 验收 |
| --- | --- | --- | --- |
| 1 用途 | 何时用 / 不用、替代方案 | 组件 KDoc + 评审项 | 评审：是否用错组件 |
| 2 解剖 | 由哪几段组成、每段用什么令牌 | 组合函数与槽位结构（`wisdom-ui/src/main/kotlin/io/github/wlunc/wisdom/components/{primitives,composites}/<组件>/`） | API 形状 + `apiDump` 同提交 |
| 3 尺寸 | 各档高度 / 内距 / 字号 / 图标 / 圆角 | 常量**引令牌**（禁硬编码数值） | 行盒与尺寸断言 + 六态截图 + `fontScale` 2.0 布局 |
| 4 变体 | 每个变体的背景 / 文字 / 描边 / 阴影 → 令牌 | 枚举 + 样式分支 | 变体矩阵截图 |
| 5 状态 | 默认 / 按下 / 聚焦 / 禁用 / 加载 / 选中 / 错误 的视觉·动效·触觉 | 状态机 + **状态优先级** | 状态优先级断言 + 六态截图 |
| 6 布局 | 与相邻元素关系、是否全宽、最大宽度、换行 | 布局容器与 `Modifier` 链（顺序固定） | 布局截图（含最大宽度） |
| 7 内容 | 字数上限、截断方式、空值、数字与单位格式 | **文案由调用方注入**（库内零文案 L-B） | L-B 检查 + 截断 / 空值用例 |
| 8 交互 | 点击 / 长按 / 滑动 / 拖拽 / 键盘 | 手势与 `interactionSource`（**唯一**） | 交互用例 + 语义动作 |
| 9 动效 | 时长与曲线（引 `motion.*` 令牌） | 动效常量 + **reduced 降级** | reduced 断言 |
| 10 无障碍 | 朗读文本、角色、触控热区、动态字体、对比度 | `semantics` + 热区（`wdTouchTarget()`） | 语义树 / 朗读 / 热区 / 对比度断言 |
| 11 平台差异 | iOS 与 Android 各自怎么做 | 本端实现分支（如玻璃三路降级） | 差异登记（F 号，见 §16 术语表） |
| 12 验收清单 | 可勾选的检查项 | 转成本端断言 + §14 的验收命令 | 逐条勾选，进批出口报告 |

**本端要额外过的一道门**：任何**公开签名**变化必须在同一次提交里带 `apiDump` 结果，且 `apiCheck` 必须绿（当前为红，转绿 6 步见 §2.1.1）；公开面**不得出现 m3 类型**（G9）。

### 21.2 画廊核验六步

1. **打开**：浏览器打开对应画廊 HTML（文件自包含，无需构建、无需服务器）；
2. **定位**：按 §20.2 的检索词搜索该组件（命中数用于确认位置）；
3. **逐项比对**：形态 → 尺寸 → 变体 → 状态，与实现并排；
4. **六态截图**：默认 / 按下 / 聚焦 / 禁用 / 加载 / 选中，逐一比对并存档（见 §21.4）；
5. **差异分流**：**实现错** ⇒ 改实现；**设计未覆盖 / 与 SPEC 冲突** ⇒ 登记差异项（F 号），**不自行取舍**；
6. **留痕**：核验结论写进批出口报告（§14 的出口结构）。

### 21.3 画廊能核什么不能核什么

| ✅ 能核（视觉基线） | ❌ 不能核（归入 §8 的 V1–V18 未验证清单） |
| --- | --- |
| 形态、密度、层级、玻璃档位、圆角、间距、字阶 | 真实动效时长与曲线 |
| 变体与状态的**静态**表现 | 真机字体与行盒（模拟器字面量驱动） |
| 明暗两套主题的观感 | 朗读文本、焦点顺序、触觉反馈 |
| 图标用法与底色搭配 | 动态字体放大后的布局（`fontScale` 2.0 档） |

### 21.4 截图存档批出口硬要求

每个组件在批出口前提供 **六态 × {light, dark}** 截图（M1 起截图基线入库），命名 `<组件>-<态>-<主题>.png`；**与画廊的比对结论**一并写进批出口报告。截图是"视觉是否对齐设计稿"的唯一可复核证据——**没有截图的批不算出口**。

---

## 18. 文档变更历史

| 轮次 | 变更 | 说明 |
| --- | --- | --- |
| `t68` | 本文件首次落库（§1–§11） | 进程骨架 / 批次 / 命令门禁 / 冻结值 / 回写 / 待给值 / 风险 / 关键路径 / 发布 / 分工 |
| `t75` | §5 冻结值与 §7 待给值补**自有自足**列与 §7.1 内联 21 行清单 | 消除对本仓外文档的运行时依赖 |
| `t81` | §7 的出处列与 §12 表体下沉到 `ARCHITECTURE.md` §16.8/§16.9 的手法在手册侧落地（本文件同步去掉对根文档集的引用） | 与手册同一纪律 |
| `t84` | 修引用完整性：`schemes` 第三条硬边界（**逐槽位任意覆盖**）；`contracts/<component>.yaml` 加 **M0-5 才落库**时点；悬空章节号换算为 §5/§6/§7 | 与 `docs/SPEC.md` / 手册口径对齐 |
| **`t91`** | 收口三项：**F-1** §14.3 契约命令路径 `../wisdomdesign/contracts/…` + `test -d` 守卫 + **M0-5 时点**说明（§14.0 增「cwd / 跨仓 / 占位符」执行约定）；**F-2** §12.1 的 C-15 列双反引号 → 单反引号（**仅改标记、内容一字未动**），§4.6 ③ 围栏计数命令改字符类写法；**F-3** 占位符加引号 | 来源 = `t88` 评审（pass，3 条非阻塞 findings）+ 船长裁决；另见 `docs/SPEC.md` 的同批修复（F48–F50 归表） |
| **`t90`** | 文档固定项由**四项补齐为六项**：§4.6 重写为「六固定项 + 两条纪律」版（新增 **⑤ 外部引用加固**、**⑥ 表格结构自检**，与 iOS 侧同名条目判据/断字写法等价）；同批清掉 3 处不合格的**软指代**写法（补文件名或显式化）；§11.1 增与 §4.6 的分工说明 | **为什么必要**：⑤ 的实例 = **A-1**（裸节号绕过路径 grep 给假绿）；⑥ 的两次现场证明 = **t74 · D2-01** 与 **iOS 侧 t82 期**（均为表格降级而其它检查全绿），本端 **t86 期**亦有同类实例。**判据与文本只能改一个：先改文本** |
| 跨端经验采纳（`t88` 期） | 固定项 **⑥ 升级为块级**：§18.1 新增 **⑦ 表格块结构自检**（连续 `|` 行成块 / 块内第 2 行必须为分隔行 / 块 ≥ 2 行） | **来源**：iOS 侧同类检查当场报出其 §17 的"表格降级"（一行落在表外）；Android 采用同款后**本轮即在 §5 冻结值表尾抓到 1 处同类缺陷**（`| 19 |` 前有孤立空行，使 19–21 行成为**无表头/无分隔行**的块）并已修复（删该空行，行序与内容不变）。**证据**：块级检查实测 异常块 = **0**（修前 = 1） |
| 船长裁决（`t88` 期） | **§12「档位（A/B/C）」列 → 「关键路径件（是/否）」**：§12.0 列定义、§12.1 表头与 **37 行**、§12.2 依据与说明、§17 第 11 条、§3.1 停用注 同步改 | **为何去掉 A/B/C**：① 逐件分配来源 = 迁移前的跨端计划（**已退役，本仓无副本**，不可取证）；② A/B/C 是**工作量口径**的载体（A = 1.0 / B = 1.75 / C = 3.0 pd），与用户决策 #1"**不需要计算人力**、重要的是开发进程"冲突 ⇒ **不以任何形式当现行口径**；③ 替代轴 = **关键路径件**（事实性，依据 §3.2 / §9.1 / [`../AGENTS.md`](../AGENTS.md) §3.2，见 §12.2）；④ 日后若需工作量口径，**从工单数据重建**（不从旧档位反推）。**其余列（落点 / `docs/SPEC.md` 锚点 / C-15 参数名 / 验收代码 / 依赖 / 状态）保持现状** |
| 跨端对齐（`t88` 期） | **关键路径件统一为 12 件**：§12.0 判定规则改为"两端可取证点名件的**并集**"、§12.1 表下补判定式、count 改 **`是` = 12 / `否` = 25**、37 行中 M4 三件（`WDAlert`/`WDActionSheet`/`WDToast`）由 `否` → `是` | **差异来源（双向）**：Android 判 9（依据本仓 §3.2 / §9.1 / [`../AGENTS.md`](../AGENTS.md) §3.2 的点名）；iOS 判 11（依据其原 `(C)` 标记——iOS 多 M4 三件）；**并集 = 12 件**（Android 独有贡献 = `WDAssigneePicker`，依据 §9.1 的 M6 串行链点名）。**为何用并集**：避免任一端丢失信息；**跨端同答案**优先于单端"保守取否"。**与 iOS 核对（本日）：`是` 集合 12/12 逐件一致**（iOS 侧同批落 12/25、同款判定式；其对 M4 的依据 = 其 §3 批次表的 `(C)` 标记，本端对 M4 的依据 = [`../AGENTS.md`](../AGENTS.md) §3.2 点名 `WDBottomSheet` + 本次并入三件）。其余列（落点 / `docs/SPEC.md` 锚点 / C-15 名 / 验收代码 / 依赖 / 状态）保持现状 |
| **`t86`** | **新增 §0 目录**（可跳转锚点 + 条数差 = 0 自检）、**§12 逐组件索引（37 行施工总表）**、**§13 单组件作业流程 SOP（8 步）**、**§14 验收手册（逐批命令 + 处置 + 检查清单）**、**§15 开发者指南（环境/规范/提交/排障/升级）**、**§16 术语表与已退役代号解析**、**§17 关键决策摘要**、**§18 变更历史** | **是增补不是重写**：§1–§11 全部判据、冻结值、批次表、回写项、待给值、V1–V18 **一字未动**；本轮只追加，未改任何口径 |
| **设计向详补（船长 · 本轮）** | 新增 **§19 设计文档与设计图查阅指南**、**§20 逐组件设计溯源表**（37 件）、**§21 从设计规格到实现与验收**（12 节映射 / 画廊核验六步 / 六态截图） | 同 iOS 侧：设计编号 01–37 无缺号；画廊用**检索词 + 实测命中数**定位；本端额外强调 `apiDump` 同提交与 `apiCheck` 转绿、公开面不得出现 m3 类型（G9） |
| **目录锚点修正 + 重建（同批）** | ① 目录按**标题为准**重建：**96 条 = 96 个标题**（新增 15 条新章节条目），顺序一致；② **纠正 23 条锚点的"折叠连续连字符"缺陷**（原写法把两空格折成一个 `-`，GitHub 不折叠 ⇒ 这些锚点点不到标题），改为 GitHub 规则（如 `#21-m0--规范冻结本仓部分m0-3m0-10`） | 证据：iOS 侧同类缺陷由 `docs/DEV-PLAN-REVIEW.md` 的 R2-01 登记（14 条），本端同批自查出 **23 条**；修正后自检：缺失 = 0、多余 = 0、顺序一致 = True；表格块 46 / 异常 0 |

### 18.1 本轮（`t86`）自检（可复制）

```bash
# ① 目录 = 标题（条数差 = 0，见 §0 的自检命令）
diff <(grep -oE '^#{2,3} .*' docs/DEV-PLAN.md | sed 's/^#* //' | sort) \
     <(grep -oE '^ *- \[[^]]+\]\(#' docs/DEV-PLAN.md | sed 's/^ *- \[//; s/\](.*$//' | sort) && echo "OK: 目录 = 标题"
# ② 逐组件索引 = 37 行
test "$(grep -cE '^\| `WD[A-Za-z]+` \| M[2-6] \|' docs/DEV-PLAN.md)" -eq 37 && echo "OK: 37 行"
# ③ 固定项（六条）
test -s docs/DEV-PLAN.md
test "$(grep -cE '^#{2,3} .*#{2,3} ' docs/DEV-PLAN.md)" -eq 0
test "$(grep -cE 'd[o]cs/impl[-]plan|d[o]cs/structure[-]discussion' docs/DEV-PLAN.md)" -eq 0
test "$(grep -cE '^\|[^|]*[^|]$' docs/DEV-PLAN.md)" -eq 0
grep -qE '^#{2,3} .*目录' docs/DEV-PLAN.md
grep -q '逐槽位' docs/DEV-PLAN.md
# ⑦ 表格**块级**结构（比行级更严：连续 `|` 行成块；块内第 2 行必须是分隔行；块 >= 2 行）
python3 - <<'EOF'
import re, sys
L = open('docs/DEV-PLAN.md', encoding='utf-8').read().split('\n')
blocks, cur, start = [], [], 1
for i, l in enumerate(L, 1):
    if l.startswith('|'):
        if not cur: start = i
        cur.append(l)
    elif cur:
        blocks.append((start, cur)); cur = []
if cur: blocks.append((start, cur))
bad = [(s, len(b)) for s, b in blocks if len(b) < 2 or not re.match(r'^\|[\s:\-|]+\|$', b[1])]
print('table blocks =', len(blocks), '| bad =', len(bad), [s for s, _ in bad])
sys.exit(1 if bad else 0)
EOF
```
