package com.example.eeum.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopSection(showRightCurve: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = "eeum",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(12.dp))
        CurveDecoration(showRight = showRightCurve)
    }
}
