package com.knotworking.animations

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.knotworking.animations.navigation.ColorRoute
import com.knotworking.animations.navigation.FlourishRoute
import com.knotworking.animations.navigation.PositionRoute
import com.knotworking.animations.navigation.VisibilityRoute
import com.knotworking.animations.navigation.allRoutes
import com.knotworking.animations.navigation.icon
import com.knotworking.animations.navigation.label
import com.knotworking.animations.screens.ColorAnimationScreen
import com.knotworking.animations.screens.FlourishAnimationScreen
import com.knotworking.animations.screens.PositionAnimationScreen
import com.knotworking.animations.screens.VisibilityAnimationScreen

@Composable
fun AnimationApp() {
    val backStack = rememberNavBackStack(VisibilityRoute)
    val current = backStack.lastOrNull()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            allRoutes.forEach { route ->
                item(
                    icon = { Icon(route.icon, contentDescription = route.label) },
                    label = { Text(route.label) },
                    selected = current == route,
                    onClick = {
                        if (current != route) {
                            backStack.clear()
                            backStack.add(route)
                        }
                    }
                )
            }
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry(VisibilityRoute) { VisibilityAnimationScreen() }
                entry(PositionRoute) { PositionAnimationScreen() }
                entry(ColorRoute) { ColorAnimationScreen() }
                entry(FlourishRoute) { FlourishAnimationScreen() }
            }
        )
    }
}
