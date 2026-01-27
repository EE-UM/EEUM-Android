package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.RandomPost
import com.example.eeum.ui.components.BottomNav
import com.example.eeum.ui.components.CurveDecoration

@Composable
fun HomeShakenScreen(
    onView: (Long) -> Unit,
    onFeed: () -> Unit,
    onShare: () -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    var randomPost by remember { mutableStateOf<RandomPost?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        repository.fetchRandomPost()
            .onSuccess { randomPost = it }
            .onFailure { errorMessage = it.message ?: "랜덤 게시글을 불러오지 못했어요." }
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "eeum",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            CurveDecoration(
                showRight = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .offset(x = (-64).dp)
            )

            Spacer(Modifier.height(28.dp))

            when {
                isLoading -> {
                    Text(
                        text = "랜덤 스토리를 불러오는 중...",
                        fontSize = 14.sp,
                        color = Color(0xFF777777)
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage.orEmpty(),
                        fontSize = 14.sp,
                        color = Color(0xFFB00020)
                    )
                }
                randomPost != null -> {
                    Text(
                        text = randomPost!!.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1D1D)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = randomPost!!.content,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = Color(0xFF555555)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { randomPost?.let { onView(it.postId) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                enabled = !isLoading && randomPost != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D1D1D),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Reply,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("view", fontSize = 14.sp)
            }

            Spacer(Modifier.height(12.dp))
            BottomNav(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp, vertical = 6.dp),
                onFeed = onFeed,
                onShare = onShare
            )
        }
    }
}
