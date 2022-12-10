package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    paddingsVertical: Dp = 16.dp,
    paddingsHorizontal: Dp = 24.dp
) {
    val textColor = if (enabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    Button(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = paddingsHorizontal, vertical = paddingsVertical),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(4.dp),
        enabled = enabled,
        onClick = onClick
    ) {

        Text(text = text.uppercase(), color = textColor, style = MaterialTheme.typography.titleSmall, fontSize = 14.sp)
    }
}