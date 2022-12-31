package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.grishko188.pointofinterest.core.validator.Validator

@Composable
fun PoiOutlineTextField(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    enabled: Boolean = true,
    maxLines: Int = 1,
    onValidValue: ((TextFieldValue) -> Unit)? = null,
    @DrawableRes leadingIconRes: Int? = null,
    @StringRes labelTextRes: Int? = null,
    @StringRes placeholderTextRes: Int? = null,
    validator: Validator? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null
) {

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { value ->
            onValueChanged(value)
            validator?.let { if (validator.validate(value.text)) onValidValue?.invoke(value) }
        },
        isError = validator?.isValid?.not() ?: false,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = {
            if (leadingIconRes != null) {
                Icon(
                    ImageVector.vectorResource(id = leadingIconRes),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            if (textFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        val empty = TextFieldValue("")
                        onValueChanged(empty)
                        validator?.validate(empty.text)
                        onValidValue?.invoke(empty)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), shape = CircleShape),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        shape = RoundedCornerShape(8.dp),
        label = {
            if (labelTextRes != null)
                Text(text = stringResource(id = labelTextRes))
        },
        placeholder = {
            if (placeholderTextRes != null) {
                Text(
                    text = stringResource(id = placeholderTextRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            }
        },
        supportingText = {
            if (validator?.isValid?.not() == true) {
                Text(
                    text = validator.getErrorMessage(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default
    )
}

@Composable
fun PoiFilledTextField(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    enabled: Boolean = true,
    maxLines: Int = 1,
    onValidValue: ((TextFieldValue) -> Unit)? = null,
    @DrawableRes leadingIconRes: Int? = null,
    @StringRes labelTextRes: Int? = null,
    @StringRes placeholderTextRes: Int? = null,
    validator: Validator? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null
) {

    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ),
        value = textFieldValue,
        onValueChange = { value ->
            onValueChanged(value)
            validator?.let { if (validator.validate(value.text)) onValidValue?.invoke(value) }
        },
        isError = validator?.isValid?.not() ?: false,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = {
            if (leadingIconRes != null) {
                Icon(
                    ImageVector.vectorResource(id = leadingIconRes),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            if (textFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        val empty = TextFieldValue("")
                        onValueChanged(empty)
                        validator?.validate(empty.text)
                        onValidValue?.invoke(empty)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), shape = CircleShape),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        shape = RoundedCornerShape(64.dp),
        label = {
            if (labelTextRes != null)
                Text(text = stringResource(id = labelTextRes))
        },
        placeholder = {
            if (placeholderTextRes != null) {
                Text(
                    text = stringResource(id = placeholderTextRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            }
        },
        supportingText = {
            if (validator?.isValid?.not() == true) {
                Text(
                    text = validator.getErrorMessage(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default
    )
}