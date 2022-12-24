package com.grishko188.pointofinterest.core.validator

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberUrlValidator(
    context: Context = LocalContext.current
) = remember(context) { UrlValidator(context) }