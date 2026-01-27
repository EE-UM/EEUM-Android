package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.PostDetail

@Composable
fun PostDetailScreen(
    postId: Long,
    onBack: () -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    var detail by remember { mutableStateOf<PostDetail?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(postId) {
        isLoading = true
        repository.fetchPostDetail(postId)
            .onSuccess { detail = it }
            .onFailure { errorMessage = it.message ?: "게시글을 불러오지 못했어요." }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back")
            }
            Text(
                text = "게시글",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(20.dp))

        when {
            isLoading -> {
                Text(
                    text = "불러오는 중...",
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
            detail != null -> {
                val post = detail!!
                val artworkUrl = post.artworkUrl
                if (artworkUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(Color(0xFFE6E2DC), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "앨범 이미지",
                            fontSize = 12.sp,
                            color = Color(0xFF9A9A9A)
                        )
                    }
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(artworkUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${post.title} 앨범 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE6E2DC))
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = post.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = post.content,
                    fontSize = 14.sp,
                    color = Color(0xFF3A3A3A),
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listOfNotNull(post.songName, post.artistName).joinToString(" • "),
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                    Text(
                        text = post.createdAt.orEmpty(),
                        fontSize = 11.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}
