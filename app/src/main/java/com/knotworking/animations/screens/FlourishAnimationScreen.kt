package com.knotworking.animations.screens

import androidx.compose.animation.Animatable as ColorAnimatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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

// ── Colour palette ────────────────────────────────────────────────────────

private val FlourishColors = listOf(
    Color(0xFFE91E63),  // Hot Pink
    Color(0xFF00BCD4),  // Cyan
    Color(0xFFFFEB3B),  // Amber Yellow
    Color(0xFF4CAF50),  // Vivid Green
    Color(0xFFFF5722),  // Deep Orange
    Color(0xFF7C4DFF),  // Vivid Violet
)

// ── Overlap timing (ms from button press) ─────────────────────────────────
/** Phase 1 (rise) always starts at t = 0. */
/** Spin begins this many ms after the rise, overlapping its bouncy tail. */
private const val SpinStartDelayMs = 150L

/** Grow begins this many ms after the rise, overlapping the spin's last 150ms. */
private const val GrowStartDelayMs = 400L

/** Descent begins this many ms after the rise, ~150ms before the settle-back finishes. */
private const val DescendStartDelayMs = 1000L

// ── Rise ──────────────────────────────────────────────────────────────────
/** How far upward the shape travels, in px. Negative = up the screen. */
private const val RiseTranslationY = -80f
private const val RiseStiffness = 600f   // snappy — gets there in ~250ms
private const val RiseDamping = 0.55f  // one light jiggle at the top

// ── Spin + colour ─────────────────────────────────────────────────────────
/** Degrees rotated in one flourish. 360 = one full clockwise turn. */
private const val SpinDegrees = 360f

/** Duration of the spin and simultaneous colour blend, in ms. */
private const val SpinDurationMs = 400

// ── Grow ──────────────────────────────────────────────────────────────────
/** Peak scale factor during the bloom. 1.4 = 40% larger than resting size. */
private const val GrowScale = 1.4f
private const val GrowStiffness = 500f   // peaks in ~150ms
private const val GrowDamping = 0.4f  // visibly overshoots before settling

// ── Settle-back (bloom → normal size) ────────────────────────────────────
private const val SettleStiffness = 400f
private const val SettleDamping = 0.6f  // single small under-bounce at 1f

// ── Descent ───────────────────────────────────────────────────────────────
private const val DescendStiffness = 300f  // slightly softer than the rise
private const val DescendDamping = 0.7f  // lands with a gentle thud

@Composable
fun FlourishAnimationScreen(modifier: Modifier = Modifier) {
    var colorIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }

    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val color = remember { ColorAnimatable(FlourishColors[0]) }

    // ── Named animation phases ────────────────────────────────────────────
    // Local suspend funs capture the Animatables above via closure, keeping
    // the choreography block below easy to read and tweak.

    suspend fun rise() {
        offsetY.animateTo(
            targetValue = RiseTranslationY,
            animationSpec = spring(stiffness = RiseStiffness, dampingRatio = RiseDamping),
        )
    }

    suspend fun spinAndRecolour(targetColor: Color, startRotation: Float) {
        coroutineScope {
            launch {
                rotation.animateTo(
                    targetValue = startRotation + SpinDegrees,
                    animationSpec = tween(
                        durationMillis = SpinDurationMs,
                        easing = FastOutSlowInEasing
                    ),
                )
            }
            launch {
                color.animateTo(
                    targetValue = targetColor,
                    animationSpec = tween(
                        durationMillis = SpinDurationMs,
                        easing = FastOutSlowInEasing
                    ),
                )
            }
        }
    }

    suspend fun growAndSettle() {
        scale.animateTo(
            targetValue = GrowScale,
            animationSpec = spring(stiffness = GrowStiffness, dampingRatio = GrowDamping),
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(stiffness = SettleStiffness, dampingRatio = SettleDamping),
        )
    }

    suspend fun descend() {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = spring(stiffness = DescendStiffness, dampingRatio = DescendDamping),
        )
    }

    // ─────────────────────────────────────────────────────────────────────

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
                        val spinStartRotation = rotation.value  // snapshot before animation begins

                        // Each phase launches in parallel at a staggered delay so the
                        // start of the next overlaps the tail of the previous.
                        coroutineScope {
                            launch { rise() }
                            launch {
                                delay(SpinStartDelayMs); spinAndRecolour(
                                FlourishColors[nextIndex],
                                spinStartRotation
                            )
                            }
                            launch {
                                delay(GrowStartDelayMs)
                                growAndSettle()
                            }
                            launch {
                                delay(DescendStartDelayMs)
                                descend()
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
