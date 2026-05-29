@file:OptIn(ExperimentalAnimationGraphicsApi::class)

package com.knotworking.animations.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knotworking.animations.R
import com.knotworking.animations.ui.theme.AnimationExamplesTheme

@Composable
fun IconAnimationScreen(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var isClosed by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        item { PlayPauseCard(isPlaying = isPlaying, onClick = { isPlaying = !isPlaying }) }
        item { AddCloseCard(isClosed = isClosed, onClick = { isClosed = !isClosed }) }
        item { FavoriteCard(isFavorite = isFavorite, onClick = { isFavorite = !isFavorite }) }
        item { ExpandCollapseCard(isExpanded = isExpanded, onClick = { isExpanded = !isExpanded }) }
    }
}

@Composable
private fun IconExampleCard(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
    }
}

@Composable
private fun PlayPauseCard(isPlaying: Boolean, onClick: () -> Unit) {
    val avd = AnimatedImageVector.animatedVectorResource(R.drawable.avd_play_pause)
    val painter = rememberAnimatedVectorPainter(avd, atEnd = isPlaying)

    IconExampleCard(label = "Play / Pause", onClick = onClick) {
        Icon(
            painter = painter,
            contentDescription = if (isPlaying) "Pause" else "Play",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun AddCloseCard(isClosed: Boolean, onClick: () -> Unit) {
    val avd = AnimatedImageVector.animatedVectorResource(R.drawable.avd_add_close)
    val painter = rememberAnimatedVectorPainter(avd, atEnd = isClosed)

    IconExampleCard(label = "Add / Close", onClick = onClick) {
        Icon(
            painter = painter,
            contentDescription = if (isClosed) "Close" else "Add",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun FavoriteCard(isFavorite: Boolean, onClick: () -> Unit) {
    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "heart color",
    )
    val heartScale = remember { Animatable(1f) }
    var isInitialComposition by remember { mutableStateOf(true) }
    LaunchedEffect(isFavorite) {
        if (isInitialComposition) {
            isInitialComposition = false
            return@LaunchedEffect
        }
        if (isFavorite) {
            heartScale.animateTo(1.3f, spring(dampingRatio = 0.3f, stiffness = 900f))
            heartScale.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 400f))
        } else {
            heartScale.animateTo(0.85f, tween(80))
            heartScale.animateTo(1f, tween(100))
        }
    }

    IconExampleCard(label = "Favourite", onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavorite) "Unfavourite" else "Favourite",
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = heartScale.value
                    scaleY = heartScale.value
                },
            tint = heartColor,
        )
    }
}

@Composable
private fun ExpandCollapseCard(isExpanded: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "arrow rotation",
    )

    IconExampleCard(label = "Expand / Collapse", onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer { rotationZ = rotation },
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun IconAnimationScreenPreview() {
    AnimationExamplesTheme {
        IconAnimationScreen()
    }
}
