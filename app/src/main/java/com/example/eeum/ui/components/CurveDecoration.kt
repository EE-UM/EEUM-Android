package com.example.eeum.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eeum.R

@Composable
fun CurveDecoration(
    showRight: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        if (showRight) {
            Image(
                painter = painterResource(R.drawable.curve_right),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer() {
                        scaleX = 1.25f   // 1.05~1.15에서 조절
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f) // 왼쪽 기준 확대
                    }
            )

        }
    }
}
