# M2 批前签名冻结（Android · 11 件）
> 目的：为 Android M2 基础批建立**签名真源**，满足 android/AGENTS.md §3.1 I-2 与 android/docs/SPEC.md §3.3 的批前门禁要求。  
> 冻结时点：2026-11-21（Asia/Shanghai）。本文件只登记签名与契约核对结论，**不授权开工写代码**；开工仍需 I-1…I-5 全绿。  
> 效力链：用户决策 ＞ 跨端契约 U/F ＞ 本端规格副本 ＞ C-15 名表 ＞ 计划/冻结值口径 ＞ 本端 AGENTS/ARCH。  
> 真源裁决：**wisdomdesign/contracts/<Component>.yaml 效力高于 android/docs/SPEC.md §2.10 副本、android/AGENTS.md 次级枚举行与历史冻结文档**；本文件遇到冲突一律“以契约为准”。  
> 写入范围：仅本文件；未改契约、SPEC 正文、AGENTS 正文、生成物或源码。

## 1. 冻结口径与读法

1. **参数并集**：契约 `params` 是重载并集，不等于单一 Kotlin 函数的形参列表。下面每件都给“函数签名（Android 主入口）+ 契约 params 并集表 + default + slots + enums 全量 case”，避免把 iOS 重载语义误当成 Android 可省略参数。
2. **参数名**：只认 `params[].name` = C-15 canonical；当 canonical 本身带 is（如 `isOnline`/`isDisabled`）则照抄，当 canonical 不带 is 而 `params[].ios` 带 is（`isLoading`/`isSelected`/`isChecked`/`isOn`）时才去前缀。**`params[].ios` 永远不是 Android 名来源。**
3. **默认值**：以契约 `params[].default` / `default` 为准；点号枚举默认（.filled/.md/...）写成 Kotlin `Enum.Case`。契约缺省默认的参数在签名中无默认值。
4. **枚举 case**：契约用 lowerCamel，Android 用 UpperCamel（PascalCase）；比较/自检时统一 `lowercase()`，大小写差异按 DIR-1 已登记，不算不一致。带关联值的 case（如 `delete(title:onDelete:)`）保留契约写法，Android 需用等价 sealed/重载表达，case 名仍归一化比较。
5. **槽位**：契约 `slots` 是语义槽位名，不必逐字等于参数名；Android 槽位形态遵守 SPEC §2.4 与 C-15/契约，不新增 21 名词表之外的名字。
6. **Android 类型映射**：`Text`→`String`；`Binding<T>`→`T` + 回调（回调名不在 C-15 范围，本轮沿用 SPEC 现名并在备注说明）；`() -> Label`→`String`（label 槽）；`(@Composable () -> Unit)?` 用于图标/内容槽；枚举 lowercase→PascalCase。

---

## 2. 逐件签名冻结（11 件）

### 2.1 WDAvatar

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDAvatar(
    value: WDAvatarValue,
    modifier: Modifier = Modifier,
    size: WDAvatarSize = WDAvatarSize.Md,
    isOnline: Boolean = false,
    isDisabled: Boolean = false,
    tone: WDMemberTone? = null,
)
```
> `WDAvatarValue` 为值形态（对应契约 slots `icon` / `label` 与 A-11 `image:` 第三构造）；`isOnline` / `isDisabled` 按契约 params[].name 逐字落签名（B2 非受控增量，F1 的“去 is”只约束受控值；C-15 #12 无受控值）。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| value | WDAvatarValue | —（必填） | value: WDAvatarValue | — | 主值入口 |
| size | WDAvatarSize | .md | size: WDAvatarSize | WDAvatarSize.Md | 非受控值 |
| isOnline | Bool | false | isOnline: Boolean | false | canonical 名；Android 保留 is（B2 增量，非 C-15 受控值；F1 仅覆盖受控值） |
| isDisabled | Bool | false | isDisabled: Boolean | false | canonical 名；Android 保留 is（B2 增量，非 C-15 受控值；F1 仅覆盖受控值） |
| tone | WDMemberTone? | null | tone: WDMemberTone? | null | A-11 增量，默认 surface.tint |

- slots（契约）：`icon`, `label`（由 `WDAvatarValue` 承载；A-11 `image:` 为第三构造，不改变 slots）。
- default（契约）：`size=.md; isOnline=false; isDisabled=false; tone=null`。
- enums 全量：`WDAvatarSize{Xs, Sm, Md, Lg, Xl}`；`WDMemberTone{Tone1, Tone2, Tone3, Tone4}`（生成物类型，本批登记 case，不在本仓定义）。
- 受控值：无（C-15 #12 = —）。

### 2.2 WDBadge

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDBadge(
    label: String,
    modifier: Modifier = Modifier,
    variant: WDBadgeVariant? = null,
    value: WDBadgeValue? = null,
)
```
> 契约 `variant` 的 `params[].default = .shared` 是“既有 init(label:) 的语义默认”，组件级 `default: {}` 未列 variant；按契约 README §8.1 以 `params[].default` 为准。Android 采用 `variant: WDBadgeVariant? = null`，null = 既有 `label` 重载等价 Shared；显式传值时取全量 case。若实现选择非空默认值，则必须写 `variant: WDBadgeVariant = WDBadgeVariant.Shared` 且不得另造默认口径；**本冻结表以可空默认锁定“既有重载无 variant”语义**。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| label | Text | —（必填） | label: String | — | 槽位 |
| value | WDBadgeValue | null | value: WDBadgeValue? | null | M5 增量并集 |
| variant | WDBadgeVariant | .shared | variant: WDBadgeVariant? | null（等价 Shared） | B1 增量；冲突以契约 default 为准 |

- slots（契约）：`label`。
- default（契约）：`{}`；params 默认 `value=null; variant=.shared`。
- enums 全量：`WDBadgeVariant{Shared, Schedule, Pending, Success, Warning, Danger}`。
- 受控值：无（C-15 #11 = —）。

### 2.3 WDButton

**Android 主函数签名（冻结；沿用 SPEC §2.2①，参数名逐行同 C-15/契约）**
```kotlin
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
    onClickLabel: String? = null,
    role: Role? = Role.Button,
    interactionSource: MutableInteractionSource? = null,
)
```

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| label | () -> Label | — | text: String | — | C-15 canonical 名是 slot `label`；Android SPEC 以 `text` 承载可见文案（冻结值 #15 仅 TextField/SearchField 的 text 属 C-15；Button 的受控值是 loading，label 为槽位） |
| variant | WDButtonVariant | .filled | variant: WDButtonVariant | WDButtonVariant.Filled | 非受控值 |
| size | WDButtonSize | .md | size: WDButtonSize | WDButtonSize.Md | 非受控值 |
| loading | Bool | false | loading: Boolean | false | C-15 #01 canonical |
| loadingAccessibilityText | Text? | null | loadingLabel: String? | null | 回调/文案名不在 C-15；沿用 SPEC §2.2 |
| leadingIcon | WDIconName? | null | leadingIcon: (@Composable () -> Unit)? | null | 槽位（F14 注入） |
| trailingIcon | WDIconName? | null | trailingIcon: (@Composable () -> Unit)? | null | 槽位（F14 注入） |
| action | () -> Void | — | onClick: () -> Unit | — | 回调名不在 C-15；沿用 SPEC §2.2 |

- slots（契约）：`label`, `leadingIcon`, `trailingIcon`。
- default（契约）：`variant=.filled; size=.md; isLoading=false; loadingAccessibilityText=null; leadingIcon=null; trailingIcon=null`。
- enums 全量：`WDButtonVariant{Filled, Tonal, Glass, Outline, Plain, Destructive}`；`WDButtonSize{Sm, Md, Lg}`。
- 受控值：`loading`（C-15 #01；iOS isLoading 仅作 params[].ios）。

### 2.4 WDCard

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDCard(
    style: WDCardStyle = WDCardStyle.Elevated,
    modifier: Modifier = Modifier,
    media: WDCardMedia? = null,
    action: (() -> Unit)? = null,
    content: @Composable () -> Unit,
)
```
> A-13 `media` 增量：**本批纳入**（契约 params 已落库且 default=null；仅“media 重载”必填的语义不照搬到 Kotlin 可选默认）。`WDCardStyle.media` case 与 `media == null` 的组合 = 空图占位；A-13 属源级 breaking，需 CHANGELOG Breaking 段与迁移片段，本冻结只登记签名。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| style | WDCardStyle | .elevated | style: WDCardStyle | WDCardStyle.Elevated | 非受控值 |
| content | () -> Content | — | content: @Composable () -> Unit | — | 尾 lambda 槽位 |
| action | () -> Void | null | action: (() -> Unit)? | null | B7 可点卡增量 |
| media | WDCardMedia? | null | media: WDCardMedia? | null | A-13 增量，本批纳入 |

- slots（契约）：`content`。
- default（契约）：`style=.elevated; action=null; media=null`。
- enums 全量：`WDCardStyle{Elevated, Outlined, Glass, Sunken, Media}`（五个 case；Media 为 A-13 增量，纳入本批）。
- 受控值：无（C-15 #17 = —）。

### 2.5 WDCheckbox

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    indeterminate: Boolean = false,
    size: WDCheckboxSize = WDCheckboxSize.Standard,
    accessibilityLabel: String? = null,
)
```
> 契约 `checked` 是 C-15 canonical（iOS 形参 isChecked 不进 Android 签名）。`indeterminate` 为新增带默认参数：Android 若加到**既有** public 函数将触发 `$default` ABI 变更（AGENTS §9.4）；本冻结把它放在**新增重载**中，既有 `(checked,onCheckedChange,label)` 重载保持 3 参不变。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| checked | Binding<Bool> | — | checked: Boolean | — | C-15 exception：保持 checked |
| label | Text | — | label: String? | null（无可见 label 时为 null） | 槽位；B8 无标签重载用 accessibilityLabel |
| indeterminate | Bool | false | indeterminate: Boolean | false | 半选显示态；新增重载 |
| size | WDCheckboxSize | .standard | size: WDCheckboxSize | WDCheckboxSize.Standard | B7 增量 |
| accessibilityLabel | Text | null | accessibilityLabel: String? | null | B8 增量；无标签时必填（由调用方保证） |

- slots（契约）：`label`。
- default（契约）：`indeterminate=false; size=.standard; accessibilityLabel=null`。
- enums 全量：`WDCheckboxSize{Standard, Compact}`。
- 受控值：`checked`（C-15 #06 例外，Android 不改名）。

### 2.6 WDDivider

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDDivider(
    style: WDDividerStyle = WDDividerStyle.Full,
    modifier: Modifier = Modifier,
)
```
> 装饰语义（`clearAndSetSemantics {}`，不进无障碍树）属实现细节，不改变签名。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| style | WDDividerStyle | .full | style: WDDividerStyle | WDDividerStyle.Full | B3 增量；非受控值 |

- slots（契约）：`无`（归一为空集合，不计入 21 名词表）。
- default（契约）：`style=.full`。
- enums 全量：`WDDividerStyle{Full, Inset}`（名字是 **Inset**，不是 Indent）。
- 受控值：无（C-15 #14 = —）。

### 2.7 WDIcon

**Android 主函数签名（冻结；SPEC §2.3 注入机制）**
```kotlin
@Composable
public fun WDIcon(
    icon: WDIconName,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = WDSize.iconMd,
)
```
> F14：Android 调用方注入 `ImageVector`（LocalWDIcons）；契约 `icon: WDIconName` 作为语义键，缺图保留占位尺寸。SPEC §2.3 另有 `imageVector` 直传写法，二者可通过重载共存；本冻结以契约 params 为准冻结语义键入口。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| icon | WDIconName | — | icon: WDIconName | — | 槽位；语义名 44 条 |
| size | WDIconSize | .md | size: Dp | WDSize.iconMd | Android 采用尺寸令牌；枚举 case 仍需全量登记用于注入映射 |

- slots（契约）：`icon`。
- default（契约）：`size=.md`。
- enums 全量：`WDIconSize{Sm, Md, Lg, Xl}`。
- 受控值：无（C-15 #20 = —）。

### 2.8 WDIconButton

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDIconButton(
    icon: WDIconName,
    accessibilityLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: WDButtonVariant = WDButtonVariant.Plain,
    size: WDButtonSize = WDButtonSize.Md,
    loading: Boolean = false,
    shape: WDIconButtonShape = WDIconButtonShape.Circle,
)
```
> B4：Android **不新建 `WDIconButtonVariant` 类型**，复用 `WDButtonVariant` 子集 `{Plain, Filled, Tonal, Glass, Outline}`（Destructive 不适用）；契约同名枚举仅作跨端 case 比较。`accessibilityLabel` 无默认值（F-08 / SPEC:869）。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| icon | WDIconName | — | icon: WDIconName | — | 槽位 |
| accessibilityLabel | Text | — | accessibilityLabel: String | — | 必填读屏标签（L-B） |
| variant | WDIconButtonVariant | .plain | variant: WDButtonVariant | WDButtonVariant.Plain | 复用类型；子集见上 |
| size | WDButtonSize | .md | size: WDButtonSize | WDButtonSize.Md | 控件档 |
| loading | Bool | false | loading: Boolean | false | C-15 #02 canonical |
| action | () -> Void | — | onClick: () -> Unit | — | 回调名不在 C-15 |
| shape | WDIconButtonShape | .circle | shape: WDIconButtonShape | WDIconButtonShape.Circle | B8 增量 |

- slots（契约）：`icon`（a11y 标签为必填参数，非语义槽位词表成员，按 F-08 单列）。
- default（契约）：`variant=.plain; size=.md; isLoading=false; shape=.circle`。
- enums 全量：`WDIconButtonVariant{Plain, Filled, Tonal, Glass, Outline}`（Android 映射到 `WDButtonVariant` 子集，不另建类型）；`WDIconButtonShape{Circle, RoundedSquare}`；`WDButtonSize{Sm, Md, Lg}`。
- 受控值：`loading`（C-15 #02；iOS isLoading 仅作 params[].ios）。

### 2.9 WDListRow

**Android 主函数签名（冻结；SPEC §2.2③，C-15 canonical 名逐行一致）**
```kotlin
@Composable
public fun WDListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    showsSeparator: Boolean = true,
    accessibilityLabel: String? = null,
    density: WDLayoutDensity? = null,
)
```

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| title | Text | — | title: String | — | 槽位 |
| subtitle | Text? | null | subtitle: String? | null | 槽位 |
| leading | WDListRowLeading | .none | leading: (@Composable () -> Unit)? | null | 槽位；Android 以组合 lambda 表达 |
| trailing | WDListRowTrailing | .none | trailing: (@Composable () -> Unit)? | null | 槽位；Android 以组合 lambda 表达 |
| selected | Bool | false | selected: Boolean | false | C-15 #18 canonical；iOS isSelected 仅作 params[].ios |
| swipeAction | WDListRowSwipeAction | .none | —（本批 Android 签名不暴露） | — | 契约枚举仍全量冻结；Android 形态待实现阶段按 U/F 登记 |
| showsSeparator | Bool | true | showsSeparator: Boolean | true | 与 iOS 同名同默认（LR-24） |
| action | (() -> Void)? | null | onClick: (() -> Unit)? | null | 回调名不在 C-15；null = 非交互行 |

- slots（契约）：`title`, `subtitle`, `leading`, `trailing`。
- default（契约）：`subtitle=null; leading=.none; trailing=.none; isSelected=false; swipeAction=.none; showsSeparator=true; action=null`（默认表用 iOS 名 isSelected；params[].name 用 canonical selected）。
- enums 全量：`WDListRowLeading{None, Icon(WDIconName), Avatar(WDAvatarValue), Checkbox, SelectionIndicator}`；`WDListRowTrailing{None, Value(String), Badge(WDBadgeValue), Avatar(WDAvatarValue), Disclosure}`；`WDListRowSwipeAction{None, Delete(title:onDelete:)}`。
- 受控值：`selected`（C-15 #18；iOS isSelected 例外）。

### 2.10 WDSwitch

**Android 主函数签名（冻结）**
```kotlin
@Composable
public fun WDSwitch(
    on: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    description: String? = null,
    size: WDSwitchSize = WDSwitchSize.Standard,
)
```
> C-15 #05 canonical = `on`；iOS isOn 不进 Android 签名。`enabled` 为 Android 既有重载参数（SPEC §2.10 #05 已给默认 true），可保留为附加重载；本主签名按契约 params 并集冻结。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| on | Binding<Bool> | — | on: Boolean | — | C-15 #05 canonical |
| label | Text | — | label: String? | null（由标签槽保证） | 槽位 |
| description | Text? | null | description: String? | null | B5 增量 |
| size | WDSwitchSize | .standard | size: WDSwitchSize | WDSwitchSize.Standard | B5 增量 |

- slots（契约）：`label`。
- default（契约）：`description=null; size=.standard`。
- enums 全量：`WDSwitchSize{Standard, Compact}`。
- 受控值：`on`（C-15 #05；iOS isOn 仅作 params[].ios）。

### 2.11 WDTextField

**Android 主函数签名（冻结；SPEC §2.2② 终稿，C-15 #03 canonical = text）**
```kotlin
@Composable
public fun WDTextField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    variant: WDTextFieldVariant = WDTextFieldVariant.Inset,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    characterLimit: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    onSubmit: (() -> Unit)? = null,
    onEditingChanged: ((Boolean) -> Unit)? = null,
)
```
> `label` 必填（F-08 / L-B）；自研 `BasicTextField` 路线（V7）；字段盒 46dp；`readOnly=false`；`singleLine=false`；`maxLines=MAX_VALUE unless singleLine` 与契约 default 对齐。

| 契约 params[].name | 契约 type | 契约 default | Android 参数名/类型 | Android 默认值 | C-15/备注 |
| --- | --- | --- | --- | --- | --- |
| text | Binding<String> | — | text: String | — | C-15 #03 canonical |
| label | Text | — | label: String | — | 必填槽位 |
| appearance | WDTextFieldAppearance | .init() | WDTextFieldAppearance（解构为 variant/placeholder/supportingText/errorText/leadingIcon/trailingIcon/characterLimit/keyboardOptions/keyboardActions/visualTransformation 等） | WDTextFieldAppearance.Default（variant=Inset、placeholder=null、prefix=None、accessory=None、helper=None、isSecure=false、submitLabel=Done） | 契约对象形态；Android SPEC §2.2② 将其展开为具体参数 |
| isEditable | Bool | true | readOnly: Boolean | false | 语义等价（isEditable=true ↔ readOnly=false）；非 C-15 受控值 |
| onSubmit | (() -> Void)? | null | onSubmit: (() -> Unit)? | null | 回调名逐字对齐；SPEC §2.2② 主签名未单列，作为附加重载参数登记 |
| onEditingChanged | ((Bool) -> Void)? | null | onEditingChanged: ((Boolean) -> Unit)? | null | 回调名逐字对齐；SPEC §2.2② 主签名未单列，作为附加重载参数登记 |

- slots（契约）：`label`, `prefix`, `accessory`, `helper`（Android 以 leadingIcon/trailingIcon/placeholder/supportingText/errorText/characterLimit 等具体参数承载，**语义槽位名仍按契约登记**）。
- default（契约，含点路径）：`appearance=.init(); isEditable=true; onSubmit=null; onEditingChanged=null; appearance.variant=.inset; appearance.placeholder=null; appearance.prefix=.none; appearance.accessory=.none; appearance.helper=.none; appearance.isSecure=false; appearance.submitLabel=.done`。
- enums 全量：`WDTextFieldVariant{Inset, Outline, Glass}`；`WDTextFieldPrefix{None, Icon(WDIconName), Text(String)}`；`WDTextFieldAccessory{None, Unit(String), Clear(accessibilityLabel:), Reveal(hiddenAccessibilityLabel:shownAccessibilityLabel:), Count(current:limit:)}`；`WDTextFieldHelper{None, Hint(String), Error(String)}`。
- 受控值：`text`（C-15 #03；iOS/Android 同名）。

---

## 3. 三枚举冻结（跨批）

| 枚举 | 全量 case（契约 canonical → Android） | 默认 | 批次 | 本批处置 |
| --- | --- | --- | --- | --- |
| `WDCardStyle` | `elevated/outlined/glass/sunken/media` → `{Elevated, Outlined, Glass, Sunken, Media}` | `Elevated` | M2（WDCard） | **纳入本批**：前 4 case 与 android/AGENTS.md §6 #22 / SPEC §2.10 #17 一致；`Media` 为 A-13 增量（2026-11-21 落库），契约效力更高 ⇒ 本批以 5 case 冻结，`media` 参数 default=null 一并在 §2.4 冻结 |
| `WDToastVariant` | `neutral/success/warning/danger` → `{Neutral, Success, Warning, Danger}` | `Neutral` | **M4**（WDToast） | 仅列出登记，不作为 M2 实现范围；契约 WDToast.yaml 已落库 |
| `WDBannerVariant` | `info/warning/danger` → `{Info, Warning, Danger}` | `Info` | **M4**（WDBanner） | 仅列出登记，不作为 M2 实现范围；契约 WDBanner.yaml 已落库 |

> 归一化规则：Kotlin `name.lowercase()` 与契约 lowerCamel 比较；`Sunken`/`sunken`、`RoundedSquare`/`roundedSquare`、`Tone1`/`tone1` 等大小写差异属 DIR-1，不计不一致。

---

## 4. 契约 ↔ SPEC 副本（含次级冻结行）不一致清单与裁决

> 下面每条的裁决均为“**以契约为准（wisdomdesign/contracts/<Component>.yaml 效力高于 android/docs/SPEC.md §2.10 副本、android/AGENTS.md 次级行、历史 M2 冻结文档）**”。本文件不改副本源码，只登记冲突与落法。

| # | 位置 | SPEC/历史副本现文 | 契约真源 | 裁决（以契约为准） |
| --- | --- | --- | --- | --- |
| 1 | SPEC §2.10 #02 WDIconButton | `variant=Plain、enabled=true`；枚举子集 `Plain/Filled/Glass` | `variant=.plain`；`WDIconButtonVariant:[plain,filled,tonal,glass,outline]`；无 `enabled` 参数 | 子集更正为 `Plain/Filled/Tonal/Glass/Outline`；不新建 `WDIconButtonVariant` 类型（复用 `WDButtonVariant`）；`enabled` 非契约参数，不进冻结签名 |
| 2 | SPEC §2.10 #03 WDTextField | `variant=Inset`、`readOnly=false`、`singleLine=false`、`maxLines=MAX_VALUE unless singleLine`、盒 46dp | 契约用 `appearance` 对象（variant=.inset 等 7 项）+ `isEditable=true` + onSubmit/onEditingChanged 回调 | 默认值语义一致；Android 解构 appearance 为具体参数（§2.11），`isEditable=true ↔ readOnly=false`；回调名逐字对齐并在签名附加重载登记；C-15 `text` 不变 |
| 3 | SPEC §2.10 #05 WDSwitch | `enabled=true、description=null、size=Standard` | `on/label/description=null/size=.standard`，无 `enabled` 参数 | 保留 `on` canonical；`enabled` 作为 Android 既有重载可选参数，不进契约并集主签名 |
| 4 | SPEC §2.10 #06 WDCheckbox | 只写 `enabled=true、indeterminate=false` | 5 参并集：`checked/label/indeterminate=false/size=.standard/accessibilityLabel=null` | 增加 `size` 与 `accessibilityLabel` 全量 case/default；`checked` 保持（不删 is 前缀例外，Android 本就用 checked） |
| 5 | SPEC §2.10 #11 WDBadge | `max=99；count=0 隐藏；variant=Shared`；枚举 `WDBadgeVariant{...}` | params `label/value/variant`，`variant.default=.shared`，组件级 `default:{}`；无 `max` 参数 | 契约无 `max` 参数，`max=99` 为实现约束不进签名；`variant` 语义默认 Shared（注：组件级 default 表未列，按 params 默认裁定） |
| 6 | SPEC §2.10 #12 WDAvatar | `size=Md；isOnline=false；isDisabled=false`；枚举 5 case | 追加 `tone: WDMemberTone? = null` 与 `WDMemberTone{Tone1..Tone4}`；A-11 `image:` 第三构造 | 纳入 `tone` 参数与枚举；`isOnline`/`isDisabled` 按契约 params 名保留；`WDAvatarXxl(72)` 仍不在契约内 |
| 7 | SPEC §2.10 #14 WDDivider | 只写装饰语义，枚举列 `WDDividerStyle{Full, Inset}`（当前文本已含） | `style: WDDividerStyle = .full` 显式参数 + 枚举 2 case | 保留 `style` 参数与枚举；缩进数值不在契约内，不写进签名 |
| 8 | SPEC §2.10 #17 WDCard / AGENTS §6 #22 / iOS SPEC §2.11 历史行 | `WDCardStyle{Elevated,Outlined,Glass,Sunken}`（4 case） | `WDCardStyle:[elevated,outlined,glass,sunken,media]` + `media: WDCardMedia? = null`（A-13） | **以契约为准**：冻结 5 case；`Media` 本批纳入；源级 breaking 走 minor + CHANGELOG，不改契约 |
| 9 | SPEC §2.10 #18 WDListRow | `trailingText` 参数；无 `swipeAction` 枚举行 | 契约 params 含 `swipeAction: WDListRowSwipeAction = .none`，枚举 `{None, Delete(title:onDelete:)}`；无 `density` 参数 | `selected/showsSeparator` 与 C-15/契约一致；`swipeAction` 枚举全量冻结但 Android 签名暂不暴露（F 项待实现登记）；`density`=F44 注入，作为 Android 既有重载参数保留 |
| 10 | SPEC §2.10 #20 WDIcon | `tint=Color.Unspecified、size=WDSize.iconMd`；枚举列 `—` | `icon: WDIconName` + `size: WDIconSize = .md`；`WDIconSize{Sm,Md,Lg,Xl}` | 以契约 `icon` 语义键为参数名；尺寸用 `WDSize.iconMd` 默认值；枚举仍全量冻结，注入机制 = F14 |
| 11 | SPEC §2.10 #01 WDButton | `loadingLabel=null`、枚举 6+3 case | `loadingAccessibilityText=null`、`leadingIcon/trailingIcon: WDIconName?`；受控值 `loading` 一致 | 受控值/枚举/default 语义一致；回调与文案参数名不在 C-15，按 SPEC 沿用；槽位 `label` 与 Android `text` 属实现差异，不改变 C-15 |
| 12 | SPEC §2.10 #28 WDToast / #29 WDBanner | Toast `durationMillis=3000`（真源键 durationMilliseconds）；Banner `visible` 例外 | `WDToast.durationMilliseconds=3000`、`WDToastVariant` 4 case；`WDBanner.visible`、`WDBannerVariant` 3 case | M4 组件只冻结枚举与 canonical 名；`durationMilliseconds` 为真源键，Android 命名 `durationMillis` 属既有副本差异，M4 再校对 |
| 13 | 所有含 is 前缀的契约 params[].ios | SPEC 已列 Android 去前缀 / 例外 | WDButton/WDIconButton `isLoading`、WDCheckbox `isChecked`、WDListRow `isSelected`、WDSwitch `isOn`；WDAvatar `isOnline/isDisabled` 本身即 canonical | Android 公开参数只取 `params[].name`（loading/checked/selected/isOnline/isDisabled/on）；`params[].ios` 不是 Android 名来源 |

> 附带说明：`wisdomdesign/contracts/CROSS-END-SYNC.md` §1.2 曾判定 Android SPEC §2.10 的 6 行需回写；本冻结表不修改 SPEC 正文，而是在本文件把契约真源逐行落地。若后续 lead 回写 SPEC，必须先改契约再同步，且本冻结表裁决不变。

---

## 5. C-15 逐行自检（11 件 × params[].name）

| 组件 | C-15 canonical（受控值） | 契约 params[].name | 冻结签名中的 Android 参数名 | 结果 |
| --- | --- | --- | --- | --- |
| WDAvatar | — | value, size, isOnline, isDisabled, tone | value, size, isOnline, isDisabled, tone | ✅ 无受控值；params 名逐字一致 |
| WDBadge | — | label, value, variant | label, value, variant | ✅ |
| WDButton | loading | label, variant, size, loading, loadingAccessibilityText, leadingIcon, trailingIcon, action | text, variant, size, loading, loadingLabel, leadingIcon, trailingIcon, onClick | ✅ loading 逐字一致（其余非受控名按 SPEC 沿用） |
| WDCard | — | style, content, action, media | style, content, action, media | ✅ |
| WDCheckbox | checked（例外） | checked, label, indeterminate, size, accessibilityLabel | checked, label, indeterminate, size, accessibilityLabel | ✅ |
| WDDivider | — | style | style | ✅ |
| WDIcon | — | icon, size | icon, size | ✅ |
| WDIconButton | loading | icon, accessibilityLabel, variant, size, loading, action, shape | icon, accessibilityLabel, variant, size, loading, onClick, shape | ✅ loading 逐字一致 |
| WDListRow | selected | title, subtitle, leading, trailing, selected, swipeAction, showsSeparator, action | title, subtitle, leading, trailing, selected, showsSeparator, onClick（swipeAction 暂不暴露） | ✅ selected 逐字一致 |
| WDSwitch | on | on, label, description, size | on, label, description, size | ✅ |
| WDTextField | text | text, label, appearance, isEditable, onSubmit, onEditingChanged | text, label, variant/…, readOnly, onSubmit, onEditingChanged | ✅ text 逐字一致 |

**表末自检结论**：
- 11 件 × `params[].name` 与 C-15 canonical 逐行核对：**0 处受控值参数名不一致**。唯一需说明的例外是 `WDCheckbox.checked`（C-15 明确 Android 保持 checked）与 `WDIconButton` 复用 `WDButtonVariant` 的类型名差异（case 集合已归一化对齐）。
- 所有非受控参数名按契约 `params[].name` 或 SPEC 既有 Android 名逐行登记；`WDAvatar.isOnline/isDisabled` 的 is 属于 canonical（保留），其余 is 前缀只出现在 `params[].ios`，不进 Android 公开签名。
- 枚举 `allCases` 归一化：本文件 §2 + §3 列出的每个枚举 case 均以 `name.lowercase()` 对契约 lowerCamel，**全部匹配（绿）**；`WDCardStyle.Media` 与 `WDIconButton` 子集映射已单列，不计失配。

---

## 6. 验证状态（本轮可跑 / 环境阻塞）

- `test -s android/docs/M2-SIGNATURE-FREEZE.md`：本文件落盘后为空检查应为 pass（本文件内不记录命令输出，见任务回执）。
- 静态自检：本文所有签名/默认值/枚举均逐行摘自 11 份契约 + SPEC §2.10/§2.2/§2.3，未运行生成器或编译。
- **【未验证 · 环境阻塞】Gradle 编译 / 单测 / wisdomGate 门禁**：本机默认 GRADLE_USER_HOME 下 `~/.gradle/wrapper/... .lck` 报 Operation not permitted；改用 workspace-local `.gradle-home` 则 gradle-8.14.5 下载 10s 超时。按 captain 环境告警，本轮不把任何 `./gradlew` 命令写成通过；编译/单测/门禁一律标【未验证】。本任务写入范围仅为文档，不依赖上述命令作为验收。
- **【未验证】CI 级枚举编译验证（U-07 / V18 后续）**：待 Android 实操阶段与 `wisdomGate` 一并验证；本冻结表只保证文档层签名与枚举归一化自洽。

---

## 7. 变更纪律

1. 本文件是 M2 批前签名真源；实现若需改签名/默认值/枚举 case，必须先改 `wisdomdesign/contracts/<Component>.yaml`，再更新本文件与 SPEC 副本，不得反向操作。
2. 新增带默认参数到**既有** public 函数（如 WDCheckbox.indeterminate/width）会改变 `$default` ABI；须走重载或 major，详见 AGENTS §9.4 / §1.5.4-F。
3. 新增枚举 case = 源级 breaking；A-13 `WDCardStyle.media` 与 B1–B5 增量必须进 CHANGELOG Breaking 段与迁移片段。
4. 冻结 ≠ 开工：本文件落盘只闭合 I-2 的“签名冻结”一项；开工仍看 I-1/I-3/I-4/I-5。
