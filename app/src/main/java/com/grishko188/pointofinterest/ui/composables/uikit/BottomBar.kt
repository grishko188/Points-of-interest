package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grishko188.pointofinterest.navigation.Screen
import com.grishko188.pointofinterest.ui.theme.OrangeMain
import com.grishko188.pointofinterest.ui.theme.UnselectedColor

@Composable
fun BottomBar(navController: NavHostController, items: List<Screen>) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        elevation = 4.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = requireNotNull(screen.icon)),
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(screen.name),
                        tint = if (isSelected) OrangeMain else UnselectedColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.name),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) OrangeMain else UnselectedColor
                    )
                },
                selectedContentColor = OrangeMain,
                unselectedContentColor = UnselectedColor,
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
