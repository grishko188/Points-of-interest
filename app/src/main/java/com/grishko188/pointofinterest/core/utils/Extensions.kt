package com.grishko188.pointofinterest.core.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringFromResource(@StringRes res: Int?): String? = if (res != null) stringResource(id = res) else null