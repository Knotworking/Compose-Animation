package com.knotworking.animations.screens

import androidx.compose.animation.Animatable as ColorAnimatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knotworking.animations.ui.theme.AnimationExamplesTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val FlourishColors = listOf(
    Color(0xFFE91E63),  // Hot Pink
    Color(0xFF00BCD4),  // Cyan
    Color(0xFFFFEB3B),  // Amber Yellow
    Color(0xFF4CAF50),  // Vivid Green
    Color(0xFFFF5722),  // Deep Orange
    Color(0xFF7C4DFF),  // Vivid Violet
)

@Composable
fun FlourishAnimationScreen(modifier: Modifier = Modifier) {
    var colorIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }

    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val color = remember { ColorAnimatable(FlourishColors[0]) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.height(32.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = offsetY.value
                        rotationZ = rotation.value
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .size(200.dp)
                    .clip(RoundedCornerShape(24))
                    .background(color.value),
            )
        }
        Button(
            onClick = {
                if (!isAnimating) {
                    val nextIndex = (colorIndex + 1) % FlourishColors.size
                    scope.launch {
                        isAnimating = true
                        // Snapshot rotation before any animation begins — used inside keyframes
                        val spinStartRotation = rotation.value

                        coroutineScope {
                            // Phase 1 — Rise: starts immediately
                            launch {
                                offsetY.animateTo(
                                    targetValue = -80f,
                                    animationSpec = spring(stiffness = 600f, dampingRatio = 0.55f),
                                )
                            }

                            // Phase 2 — Spin + colour: starts 150ms in, overlapping Phase 1's tail
                            launch {
                                delay(150)
                                coroutineScope {
                                    launch {
                                        // 3 full rotations; keyframe at 300ms makes first half
                                        // arrive at 1/3 of duration — slow wind-up into fast burst
                                        rotation.animateTo(
                                            targetValue = spinStartRotation + 1080f,
                                            animationSpec = keyframes {
                                                durationMillis = 900
                                                spinStartRotation + 540f at 300 using EaseIn
                                            },
                                        )
                                    }
                                    launch {
                                        color.animateTo(
                                            targetValue = FlourishColors[nextIndex],
                                            animationSpec = tween(
                                                durationMillis = 900,
                                                easing = FastOutSlowInEasing,
                                            ),
                                        )
                                    }
                                }
                            }

                            // Phase 3 — Grow + settle: starts at 900ms, overlapping Phase 2's last 150ms
                            launch {
                                delay(900)
                                // Bloom with clear overshoot; stiffness 500 peaks in ~150ms
                                scale.animateTo(
                                    targetValue = 1.4f,
                                    animationSpec = spring(stiffness = 500f, dampingRatio = 0.4f),
                                )
                                // Settle back with a single small under-bounce
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(stiffness = 400f, dampingRatio = 0.6f),
                                )
                            }

                            // Phase 4 — Descent: starts at 1300ms, during Phase 3's settle-back
                            launch {
                                delay(1300)
                                offsetY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(stiffness = 300f, dampingRatio = 0.7f),
                                )
                            }
                        }

                        colorIndex = nextIndex
                        isAnimating = false
                    }
                }
            },
            enabled = !isAnimating,
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            Text("Flourish")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlourishAnimationScreenPreview() {
    AnimationExamplesTheme {
        FlourishAnimationScreen()
    }
}
