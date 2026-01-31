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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable { onShake() }
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_setting),
            contentDescription = "settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 18.dp)
                .size(18.dp)
                .clickable { onSettings() }
        )

        CurveDecoration(
            modifier = Modifier
                .width(220.dp)
                .height(60.dp)
                .align(Alignment.TopEnd)
                .padding(top = 42.dp, end = 6.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 22.dp, end = 22.dp)
                .offset(y = (-22).dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Shake",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.2).sp
                )

                Spacer(Modifier.width(4.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_eeum),
                    contentDescription = "eeum icon",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(y = 5.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "to receive someone’s letter\nanswer with music",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF8E8E8E)
            )
        }

        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 22.dp)
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            onFeed = onFeed,
            onShare = onShare
        )
    }
}
