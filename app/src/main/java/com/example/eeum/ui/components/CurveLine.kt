package com.example.eeum.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun CurveLine() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            quadraticBezierTo(
                size.width * 0.5f,
                size.height * 1.1f,
                size.width,
                size.height * 0.5f
            )
        }
        drawPath(path, Color.Black)
    }
}
