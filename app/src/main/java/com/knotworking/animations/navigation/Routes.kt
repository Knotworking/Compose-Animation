package com.knotworking.animations.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface TabRoute : NavKey

@Serializable
data object VisibilityRoute : TabRoute

@Serializable
data object PositionRoute : TabRoute

@Serializable
data object ColorRoute : TabRoute

@Serializable
data object IconRoute : TabRoute

@Serializable
data object FlourishRoute : TabRoute

@Serializable
data class VisibilityDetailRoute(val imageUrl: String) : NavKey

val allTabRoutes: List<TabRoute> = listOf(
    VisibilityRoute,
    PositionRoute,
    ColorRoute,
    IconRoute,
    FlourishRoute
)

val TabRoute.label: String
    get() = when (this) {
        VisibilityRoute -> "Visibility"
        PositionRoute -> "Position"
        ColorRoute -> "Color"
        FlourishRoute -> "Flourish"
        IconRoute -> "Icon"
    }

val TabRoute.icon: ImageVector
    get() = when (this) {
        VisibilityRoute -> Icons.Default.Visibility
        PositionRoute -> Icons.Default.OpenWith
        ColorRoute -> Icons.Default.Palette
        FlourishRoute -> Icons.Default.AutoAwesome
        IconRoute -> Icons.Default.PlayArrow
    }
