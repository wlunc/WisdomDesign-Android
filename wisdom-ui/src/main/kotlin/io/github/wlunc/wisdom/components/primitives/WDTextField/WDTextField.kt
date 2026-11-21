package io.github.wlunc.wisdom.components.primitives.WDTextField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.flow.collect
import io.github.wlunc.wisdom.foundation.generated.WDColors
import io.github.wlunc.wisdom.foundation.generated.WDRadius
import io.github.wlunc.wisdom.foundation.generated.WDSize
import io.github.wlunc.wisdom.foundation.generated.WDSpacing
import io.github.wlunc.wisdom.foundation.generated.WDState
import io.github.wlunc.wisdom.foundation.generated.WDType
import io.github.wlunc.wisdom.foundation.theme.WDTheme
import io.github.wlunc.wisdom.foundation.typography.toTextStyle

/** U6 可见状态：disabled > error > focused > read-only > default（focus/hover 为叠加维度）。 */
internal enum class WDTextFieldVisualState {
    Default,
    Focused,
    Error,
    Disabled,
    ReadOnly,
}

/** 供 JVM 单测直接断言状态优先级；无 Compose 运行依赖。 */
internal fun wdTextFieldVisualState(
    enabled: Boolean,
    isFocused: Boolean,
    hasError: Boolean,
    readOnly: Boolean,
): WDTextFieldVisualState = when {
    !enabled -> WDTextFieldVisualState.Disabled
    hasError -> WDTextFieldVisualState.Error
    isFocused -> WDTextFieldVisualState.Focused
    readOnly -> WDTextFieldVisualState.ReadOnly
    else -> WDTextFieldVisualState.Default
}

internal data class WDTextFieldFieldColors(
    val background: Color,
    val border: Color,
    val content: Color,
    val alpha: Float,
)

/** 令牌化解析；不读静态 scheme、不判断系统深浅。 */
internal fun wdTextFieldFieldColors(
    variant: WDTextFieldVariant,
    state: WDTextFieldVisualState,
    colors: WDColors,
): WDTextFieldFieldColors = WDTextFieldFieldColors(
    background = when {
        state == WDTextFieldVisualState.ReadOnly -> colors.surfaceCardSunken
        variant == WDTextFieldVariant.Inset -> colors.fillField
        variant == WDTextFieldVariant.Glass -> colors.surfaceGlassStrong
        else -> colors.surfaceCardSolid
    },
    border = when (state) {
        WDTextFieldVisualState.Error -> colors.statusDanger
        WDTextFieldVisualState.Focused -> colors.textBrand
        else -> colors.borderHairlineStrong
    },
    content = when (state) {
        WDTextFieldVisualState.Disabled -> colors.textDisabled
        WDTextFieldVisualState.ReadOnly -> colors.textSecondary
        else -> colors.textPrimary
    },
    alpha = if (state == WDTextFieldVisualState.Disabled) WDState.disabledAlpha / 100f else 1f,
)

/**
 * 自研 DecorationBox：标签常驻；字段盒高度恒为 WDSize.fieldHeight（46dp）；
 * supporting/error 文案在盒外，不存在固定高度包文字的正文。
 */
@Composable
internal fun WDTextFieldDecorationBox(
    text: String,
    label: String,
    placeholder: String?,
    supportingText: String?,
    errorText: String?,
    variant: WDTextFieldVariant,
    state: WDTextFieldVisualState,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = WDTheme.colors
    val field = wdTextFieldFieldColors(variant, state, colors)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(WDSpacing.s2)) {
        BasicText(
            text = label,
            style = WDType.subheadline.toTextStyle().copy(
                color = if (state == WDTextFieldVisualState.Disabled) colors.textDisabled else colors.textSecondary,
            ),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(WDSize.fieldHeight)
                .graphicsLayer { alpha = field.alpha }
                .background(field.background, RoundedCornerShape(WDRadius.md))
                .then(
                    when {
                        state == WDTextFieldVisualState.Focused || state == WDTextFieldVisualState.Error ->
                            Modifier.border(WDState.focusRingWidth, field.border, RoundedCornerShape(WDRadius.md))
                        variant != WDTextFieldVariant.Inset ->
                            Modifier.border(WDSpacing.s1, field.border, RoundedCornerShape(WDRadius.md))
                        else -> Modifier
                    },
                )
                .padding(horizontal = WDSpacing.s4),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WDSpacing.s3),
            ) {
                if (leadingIcon != null) Box { leadingIcon() }
                Box(modifier = Modifier.weight(1f)) {
                    if (text.isEmpty() && placeholder != null) {
                        BasicText(
                            text = placeholder,
                            style = WDType.body.toTextStyle().copy(color = colors.textTertiary),
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) Box { trailingIcon() }
            }
        }
        val helper = errorText ?: supportingText
        if (helper != null) {
            BasicText(
                text = helper,
                style = WDType.footnote.toTextStyle().copy(
                    color = if (errorText != null) colors.statusDanger else colors.textSecondary,
                ),
            )
        }
    }
}

/** B6 计数 accessory：调用方传入文案；≥90% 时由令牌 statusWarning 提示。 */
@Composable
public fun WDTextFieldCount(current: Int, limit: Int) {
    val colors = WDTheme.colors
    val nearLimit = limit > 0 && current >= limit * 9 / 10
    BasicText(
        text = "$current/$limit",
        style = WDType.footnote.toTextStyle().copy(
            color = if (nearLimit) colors.statusWarning else colors.textTertiary,
        ),
    )
}

/** 契约主签名（t2 冻结 §2.11）：受控值名 text；label 必填；B6 能力按参数落地。 */
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
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val colors = WDTheme.colors
    val state = wdTextFieldVisualState(enabled, focused, errorText != null, readOnly)
    val fieldState = remember { TextFieldState(text) }
    LaunchedEffect(fieldState) {
        snapshotFlow { fieldState.text.toString() }.collect { value -> onValueChange(value) }
    }
    LaunchedEffect(text) {
        if (fieldState.text.toString() != text) {
            fieldState.edit { replace(0, length, text) }
        }
    }
    val inputTransformation = remember(characterLimit) {
        if (characterLimit != null) InputTransformation.maxLength(characterLimit) else InputTransformation.Companion
    }

    BasicTextField(
        state = fieldState,
        inputTransformation = inputTransformation,
        modifier = modifier.semantics { if (errorText != null) error(errorText) },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = WDType.body.toTextStyle().copy(color = colors.textPrimary),
        keyboardOptions = keyboardOptions,
        keyboardActions = if (onSubmit != null) keyboardActions.copy(onDone = { onSubmit() }) else keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        interactionSource = source,
        cursorBrush = SolidColor(colors.textBrand),
        decorationBox = { inner ->
            WDTextFieldDecorationBox(
                text = text,
                label = label,
                placeholder = placeholder,
                supportingText = supportingText,
                errorText = errorText,
                variant = variant,
                state = state,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                innerTextField = inner,
            )
        },
    )
}