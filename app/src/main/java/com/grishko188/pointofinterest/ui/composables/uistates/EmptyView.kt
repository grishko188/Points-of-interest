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
import com.grishko188.pointofinterest.ui.theme.DarkMainColor
import com.grishko188.pointofinterest.ui.theme.WarmGray400

@Composable
fun EmptyView(
    background: Color = MaterialTheme.colorScheme.background,
    title: String = stringResource(id = R.string.title_ui_state_empty),
    message: String? = null,
    icon: Int = R.drawable.ic_empty_sad_face,
    color: Color = WarmGray400,
    textColor: Color = DarkMainColor
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
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "Empty",
            tint = color
        )

        Spacer(Modifier.height(48.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )

        if (message != null) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    EmptyView(
        message = stringResource(id = R.string.message_ui_state_empty_main_screen)
    )
}