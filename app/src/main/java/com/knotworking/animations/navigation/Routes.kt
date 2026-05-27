package com.knotworking.animations.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey

@Serializable
data object VisibilityRoute : NavRoute
@Serializable
data object PositionRoute : NavRoute
@Serializable
data object ColorRoute : NavRoute
@Serializable
data object FlourishRoute : NavRoute

val allRoutes: List<NavRoute> = listOf(VisibilityRoute, PositionRoute, ColorRoute, FlourishRoute)

val NavRoute.label: String
    get() = when (this) {
        VisibilityRoute -> "Visibility"
        PositionRoute -> "Position"
        ColorRoute -> "Color"
        FlourishRoute -> "Flourish"
    }

val NavRoute.icon: ImageVector
    get() = when (this) {
        VisibilityRoute -> Icons.Default.Visibility
        PositionRoute -> Icons.Default.OpenWith
        ColorRoute -> Icons.Default.Palette
        FlourishRoute -> Icons.Default.AutoAwesome
    }
