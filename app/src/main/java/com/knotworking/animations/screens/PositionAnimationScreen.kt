package com.knotworking.animations.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knotworking.animations.ui.theme.AnimationExamplesTheme

@Composable
fun PositionAnimationScreen(modifier: Modifier = Modifier) {
    var movedRight by remember { mutableStateOf(false) }

    val offsetX by animateDpAsState(
        targetValue = if (movedRight) 120.dp else (-120).dp,
        animationSpec = spring(stiffness = 200f, dampingRatio = 0.5f),
        label = "position",
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
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }

        Button(
            onClick = { movedRight = !movedRight },
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            Text("Move")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PositionAnimationScreenPreview() {
    AnimationExamplesTheme {
        PositionAnimationScreen()
    }
}
