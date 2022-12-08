package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import com.grishko188.pointofinterest.features.main.PoiAppState
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.navigation.getMainScreens

@Composable
fun BottomBar(
    appState: PoiAppState,
    items: List<Screen> = getMainScreens()
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.background,
        cutoutShape = CircleShape,
        elevation = 8.dp
    ) {
        items.forEach { screen ->
            val isSelected = appState.currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = requireNotNull(screen.icon)),
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(screen.name),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.name),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                selected = isSelected,
                onClick = { appState.navigateToRoot(screen) }
            )
        }
    }
}
