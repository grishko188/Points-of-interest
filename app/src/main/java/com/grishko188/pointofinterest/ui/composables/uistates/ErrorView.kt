package com.grishko188.pointofinterest.ui.composables.uistates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grishko188.pointofinterest.R
import com.grishko188.pointofinterest.ui.composables.uikit.PrimaryButton
import com.grishko188.pointofinterest.ui.theme.DarkMainColor
import com.grishko188.pointofinterest.ui.theme.WarmGray400

@Composable
fun ErrorView(
    background: Color = MaterialTheme.colorScheme.background,
    title: String = stringResource(id = R.string.title_ui_state_error),
    message: String? = null,
    icon: Int = R.drawable.ic_error,
    color: Color = DarkMainColor,
    textColor: Color = DarkMainColor,
    onRetryClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier.size(112.dp),
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "Error icon",
            tint = color
        )

        Spacer(Modifier.height(64.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )

        if (message != null) {

            Spacer(Modifier.height(4.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

        if (onRetryClick != null) {

            Spacer(Modifier.height(48.dp))

            PrimaryButton(
                text = stringResource(id = R.string.button_title_try_again),
                onClick = onRetryClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview() {
    ErrorView(
        message = "Network connection issue", onRetryClick = {}
    )
}