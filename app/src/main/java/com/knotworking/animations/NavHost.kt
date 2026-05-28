package com.knotworking.animations

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.knotworking.animations.navigation.ColorRoute
import com.knotworking.animations.navigation.FlourishRoute
import com.knotworking.animations.navigation.TabRoute
import com.knotworking.animations.navigation.PositionRoute
import com.knotworking.animations.navigation.VisibilityDetailRoute
import com.knotworking.animations.navigation.VisibilityRoute
import com.knotworking.animations.navigation.allTabRoutes
import com.knotworking.animations.navigation.icon
import com.knotworking.animations.navigation.label
import com.knotworking.animations.screens.ColorAnimationScreen
import com.knotworking.animations.screens.FlourishAnimationScreen
import com.knotworking.animations.screens.PositionAnimationScreen
import com.knotworking.animations.screens.VisibilityAnimationScreen
import com.knotworking.animations.screens.VisibilityDetailScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavHost() {
    val backStack = rememberNavBackStack(VisibilityRoute)

    val selectedRoute: TabRoute? = when (val current = backStack.lastOrNull()) {
        is TabRoute -> current
        is VisibilityDetailRoute -> VisibilityRoute
        else -> null
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            allTabRoutes.forEach { route ->
                item(
                    icon = { Icon(route.icon, contentDescription = route.label) },
                    label = { Text(route.label) },
                    selected = route == selectedRoute,
                    onClick = {
                        if (selectedRoute != route) {
                            backStack.clear()
                            backStack.add(route)
                        }
                    }
                )
            }
        }
    ) {
        SharedTransitionLayout {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<VisibilityRoute> {
                        VisibilityAnimationScreen(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            onImageClick = { url -> backStack.add(VisibilityDetailRoute(url)) },
                        )
                    }
                    entry(PositionRoute) { PositionAnimationScreen() }
                    entry(ColorRoute) { ColorAnimationScreen() }
                    entry(FlourishRoute) { FlourishAnimationScreen() }
                    entry<VisibilityDetailRoute> { args ->
                        VisibilityDetailScreen(
                            imageUrl = args.imageUrl,
                            sharedTransitionScope = this@SharedTransitionLayout,
                        )
                    }
                }
            )
        }
    }
}
