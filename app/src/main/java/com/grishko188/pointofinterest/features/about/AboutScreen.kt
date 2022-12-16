package com.grishko188.pointofinterest.features.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grishko188.pointofinterest.R

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .padding(48.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(156.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.mipmap.ic_poi_splash_icon),
                contentDescription = "Application logo",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.size(32.dp))
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.about_screen_descriprion),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }

        Column(modifier = Modifier.height(128.dp)) {
            // TODO add info
        }
    }
}