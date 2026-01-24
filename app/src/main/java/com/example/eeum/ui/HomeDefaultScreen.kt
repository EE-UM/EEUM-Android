package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.ui.components.BottomNav
import com.example.eeum.ui.components.CurveDecoration

@Composable
fun HomeDefaultScreen(onShake: () -> Unit) {
    val bg = Color(0xFFF7F6F2) // 원본 느낌의 아이보리

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .clickable { onShake() } // 임시로 클릭 시 다음 화면으로 넘어가게 설정
    ) {

        // ✅ 상단 타이틀 (원본처럼 살짝 내려서)
        Text(
            text = "eeum",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )

        // ✅ 곡선: "Top에 붙이지 말고" 내려서, 길게/얇게
        CurveDecoration(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)          // 🔥 얇게
                .align(Alignment.TopStart)
                .padding(top = 72.dp)   // 🔥 원본처럼 타이틀 아래에 위치
                .offset(x = (-75).dp)   // 🔥 왼쪽이 화면 밖에서 시작하는 느낌
        )

        // ✅ Shake 텍스트 블록: 원본 위치로 내려서 고정
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, end = 24.dp, top = 260.dp) // 🔥 여기서 대부분 결정됨
        ) {
            Text(
                text = "Shake",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "to receive someone’s letter\nanswer with music",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF9A9A9A)
            )
        }

        // ✅ 하단 네비: 아이콘+텍스트 세트로 중앙 정렬 느낌
        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .fillMaxWidth()
                .padding(horizontal = 56.dp)
        )
    }
}
