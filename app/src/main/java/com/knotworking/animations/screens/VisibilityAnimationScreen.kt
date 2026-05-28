package com.knotworking.animations.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage

private const val IMAGE_URL = "https://picsum.photos/seed/photo/800/600"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun VisibilityAnimationScreen(
    sharedTransitionScope: SharedTransitionScope,
    onImageClick: (imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            this@Column.AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + scaleIn(initialScale = 0.7f),
                exit = fadeOut() + scaleOut(targetScale = 0.7f),
            ) {
                val animatedContentScope = LocalNavAnimatedContentScope.current
                with(sharedTransitionScope) {
                    AsyncImage(
                        model = IMAGE_URL,
                        contentDescription = "Tap to view detail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .sharedElement(
                                rememberSharedContentState(key = IMAGE_URL),
                                animatedVisibilityScope = animatedContentScope,
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onImageClick(IMAGE_URL) },
                    )
                }
            }
        }

        Button(
            onClick = { visible = !visible },
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            Text(if (visible) "Hide" else "Show")
        }
    }
}

