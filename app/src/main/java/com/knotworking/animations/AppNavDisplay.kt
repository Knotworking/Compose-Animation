package com.knotworking.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import com.knotworking.animations.navigation.ColorRoute
import com.knotworking.animations.navigation.FlourishRoute
import com.knotworking.animations.navigation.IconRoute
import com.knotworking.animations.navigation.TabRoute
import com.knotworking.animations.navigation.PositionRoute
import com.knotworking.animations.navigation.VisibilityDetailRoute
import com.knotworking.animations.navigation.VisibilityRoute
import com.knotworking.animations.navigation.allTabRoutes
import com.knotworking.animations.navigation.icon
import com.knotworking.animations.navigation.label
import com.knotworking.animations.screens.ColorAnimationScreen
import com.knotworking.animations.screens.FlourishAnimationScreen
import com.knotworking.animations.screens.IconAnimationScreen
import com.knotworking.animations.screens.PositionAnimationScreen
import com.knotworking.animations.screens.VisibilityAnimationScreen
import com.knotworking.animations.screens.VisibilityDetailScreen
import com.knotworking.animations.ui.theme.AnimationExamplesTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavDisplay() {
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
                transitionSpec = fadeTransition,
                popTransitionSpec = fadeTransition,
                entryProvider = entryProvider {
                    entry<VisibilityRoute> {
                        VisibilityAnimationScreen(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            onImageClick = { url -> backStack.add(VisibilityDetailRoute(url)) },
                        )
                    }
                    entry(PositionRoute) { PositionAnimationScreen() }
                    entry(ColorRoute) { ColorAnimationScreen() }
                    entry(IconRoute) { IconAnimationScreen() }
                    entry(FlourishRoute) { FlourishAnimationScreen() }
                    entry<VisibilityDetailRoute> { args ->
                        VisibilityDetailScreen(
                            imageUrl = args.imageUrl,
                            onBack = { backStack.removeLastOrNull() },
                            sharedTransitionScope = this@SharedTransitionLayout,
                        )
                    }
                }
            )
        }
    }
}

private val fadeTransition: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
    {
        fadeIn(tween(500)) togetherWith
                fadeOut(tween(500))
    }

@Preview
@Composable
private fun AppNavDisplayPreview() {
    AnimationExamplesTheme {
        AppNavDisplay()
    }
}
