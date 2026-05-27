package com.knotworking.animations.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knotworking.animations.ui.theme.AnimationExamplesTheme

private val ColorA = Color(0xFF6650A4) // Purple
private val ColorB = Color(0xFF03DAC6) // Teal

@Composable
fun ColorAnimationScreen(modifier: Modifier = Modifier) {
    var toggled by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (toggled) ColorB else ColorA,
        animationSpec = tween(durationMillis = 800),
        label = "bg color",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(200.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .background(bgColor),
        )

        Button(
            onClick = { toggled = !toggled },
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            Text("Toggle Color")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ColorAnimationScreenPreview() {
    AnimationExamplesTheme {
        ColorAnimationScreen()
    }
}
