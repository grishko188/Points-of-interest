package com.grishko188.pointofinterest.ui.composables.uistates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grishko188.pointofinterest.ui.composables.uikit.PulsingProgressBar

@Composable
fun ProgressView(
    background: Color = MaterialTheme.colorScheme.background,
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        PulsingProgressBar(
            modifier = Modifier
                .width(96.dp)
                .height(96.dp),
            color = progressColor,
            diameter = 96.dp
        )
    }
}

@Preview
@Composable
fun ProgressPreview() {
    ProgressView()
}