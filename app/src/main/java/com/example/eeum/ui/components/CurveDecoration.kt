package com.example.eeum.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eeum.R

@Composable
fun CurveDecoration(
    showRight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.curve_left),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(80.dp)
        )

        if (showRight) {
            Image(
                painter = painterResource(R.drawable.curve_right),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(80.dp)
            )
        }
    }
}
