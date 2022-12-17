package com.grishko188.pointofinterest.core.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.grishko188.pointofinterest.features.categories.ui.models.CategoryUiModel

@Composable
@ReadOnlyComposable
fun stringFromResource(@StringRes res: Int?): String? = if (res != null) stringResource(id = res) else null

fun List<CategoryUiModel>.containsId(id: String) = this.any { it.id == id }

@Composable
fun chromeTabsIntent(): CustomTabsIntent {
    val builder = CustomTabsIntent.Builder()
    val colorSchemeParams = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(MaterialTheme.colorScheme.background.toArgb())
        .setSecondaryToolbarColor(MaterialTheme.colorScheme.secondary.toArgb())
        .setNavigationBarColor(MaterialTheme.colorScheme.background.toArgb())
        .setNavigationBarDividerColor(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f).toArgb())
        .build()
    builder.setDefaultColorSchemeParams(colorSchemeParams)
    return builder.build()
}

/**
 * Shorter version of [CustomTabsIntent.launchUrl],
 * created to launch all any intent in the same code style.
 */
fun CustomTabsIntent.launch(context: Context, url: String) {
    launchUrl(context, Uri.parse(url))
}
