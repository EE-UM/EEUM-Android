package com.example.eeum.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.R
import com.example.eeum.ui.components.BottomNav
import com.example.eeum.ui.components.CurveDecoration

@Composable
fun HomeDefaultScreen(
    onShake: () -> Unit,
    onFeed: () -> Unit,
    onShare: () -> Unit,
    onSettings: () -> Unit
) {
    val bg = Color(0xFFF7F6F2) // 원본 느낌의 아이보리

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable { onShake() }
    ) {
        val settingsTop = maxHeight * 0.038f
        val settingsEnd = maxWidth * 0.07f
        val curveTop = maxHeight * 0.065f
        val curveWidth = maxWidth * 0.62f
        val curveHeight = maxHeight * 0.078f
        val headlineTop = maxHeight * 0.36f
        val headlineStart = maxWidth * 0.09f
        val bottomNavPadding = maxHeight * 0.05f

        Image(
            painter = painterResource(id = R.drawable.ic_setting),
            contentDescription = "settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = settingsTop, end = settingsEnd)
                .size(18.dp)
                .clickable { onSettings() }
        )

        CurveDecoration(
            modifier = Modifier
                .width(curveWidth)
                .height(curveHeight)
                .align(Alignment.TopEnd)
                .padding(top = curveTop)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = headlineStart)
                .padding(top = headlineTop)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Shake",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.4).sp
                )

                Spacer(Modifier.width(4.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_eeum),
                    contentDescription = "eeum icon",
                    modifier = Modifier
                        .size(28.dp)
                        .offset(y = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "to receive someone’s letter\nanswer with music",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = Color(0xFF8E8E8E)
            )
        }

        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomNavPadding)
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            onFeed = onFeed,
            onShare = onShare
        )
    }
}
