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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Message
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
import com.example.eeum.data.PostComment
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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

            Spacer(Modifier.height(16.dp))

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
                                .height(240.dp)
                                .background(Color(0xFFE6E2DC), RoundedCornerShape(16.dp)),
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
                                .height(240.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE6E2DC))
                        )
                    }
                }
            }
        }

        if (detail != null) {
            val post = detail!!
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .background(Color(0xFFF7F6F2), RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(44.dp)
                        .height(4.dp)
                        .background(Color(0xFFE0DED7), RoundedCornerShape(2.dp))
                )

                val artworkUrl = post.artworkUrl
                if (artworkUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(96.dp)
                            .background(Color(0xFFE6E2DC), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "앨범",
                            fontSize = 11.sp,
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
                            .align(Alignment.CenterHorizontally)
                            .size(96.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE6E2DC))
                    )
                }

                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                Text(
                    text = post.artistName ?: "가수이름",
                    fontSize = 12.sp,
                    color = Color(0xFF777777)
                )

                Text(
                    text = post.content,
                    fontSize = 13.sp,
                    color = Color(0xFF3A3A3A),
                    lineHeight = 19.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(18.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Message,
                        contentDescription = null,
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "댓글",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                if (post.comments.isEmpty()) {
                    Text(
                        text = "아직 댓글이 없어요.",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        post.comments.chunked(2).forEach { rowComments ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowComments.forEach { comment ->
                                    CommentCard(
                                        comment = comment,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowComments.size == 1) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentCard(
    comment: PostComment,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val artworkUrl = comment.artworkUrl
        if (artworkUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(Color(0xFFE6E2DC), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "앨범",
                    fontSize = 10.sp,
                    color = Color(0xFF9A9A9A)
                )
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artworkUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = comment.songName ?: "댓글 앨범 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE6E2DC))
            )
        }

        Text(
            text = comment.songName ?: "어머니께",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1D1D)
        )

        Text(
            text = comment.artistName ?: "가수이름",
            fontSize = 11.sp,
            color = Color(0xFF777777)
        )
    }
}
