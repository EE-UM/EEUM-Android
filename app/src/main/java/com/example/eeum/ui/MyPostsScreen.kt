package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.R
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.MyPost
import kotlinx.coroutines.launch

@Composable
fun MyPostsScreen(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    val coroutineScope = rememberCoroutineScope()

    var posts by remember { mutableStateOf<List<MyPost>>(emptyList()) }
    var postCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val gridState = rememberLazyGridState()

    fun loadPosts() {
        if (isLoading) return
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.fetchMyPosts()
            result.onSuccess { data ->
                posts = data.getMyPostResponses
                postCount = data.postCount
            }.onFailure { error ->
                errorMessage = error.message ?: "내 사연을 불러오지 못했어요."
            }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHome) {
                Icon(Icons.Filled.Home, contentDescription = "home")
            }
            IconButton(onClick = onOpenMenu) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_multi),
                    contentDescription = "menu"
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Posts",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "${postCount}개\n직접 공유한 사연과 플레이리스트입니다.",
            fontSize = 12.sp,
            color = Color(0xFF7A7A7A)
        )

        Spacer(Modifier.height(16.dp))

        when {
            posts.isNotEmpty() -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(posts, key = { _, item -> item.postId }) { index, post ->
                        MyPostCard(
                            index = index,
                            post = post,
                            onOpenDetail = onOpenDetail
                        )
                    }
                }
            }

            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1D1D1D))
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "표시할 사연이 없어요.",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                }
            }
        }

        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage.orEmpty(),
                color = Color(0xFFE24A3B),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun MyPostCard(
    index: Int,
    post: MyPost,
    onOpenDetail: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(post.postId) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE6E2DC))
        ) {
            val artworkUrl = post.artworkUrl
            if (!artworkUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artworkUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1D1D1D))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                val currentCount = post.currentPlaylistCount
                val targetCount = post.targetPlaylistCount
                Text(
                    text = "${currentCount}/${targetCount}",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = post.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1D1D)
        )

        val subtitle = if (post.isCompleted) "완료됨" else "진행 중"
        Text(
            text = subtitle,
            fontSize = 10.sp,
            color = Color(0xFF7A7A7A),
            maxLines = 1
        )
    }
}
