package com.knotworking.animations.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knotworking.animations.ui.theme.AnimationExamplesTheme

@Composable
fun VisibilityAnimationScreen(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Box with weight(1f) always reserves layout space so the Button below
        // never jumps when AnimatedVisibility collapses its content.
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
                Card(
                    modifier = Modifier.size(200.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Hello! 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VisibilityAnimationScreenPreview() {
    AnimationExamplesTheme {
        VisibilityAnimationScreen()
    }
}
