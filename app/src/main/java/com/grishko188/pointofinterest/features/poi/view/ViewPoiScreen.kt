package com.grishko188.pointofinterest.features.poi.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ViewPoiScreen(navController: NavHostController) {
    Surface(
        Modifier.padding(16.dp)
    ) {
        Text(text = "This is View article screen", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}