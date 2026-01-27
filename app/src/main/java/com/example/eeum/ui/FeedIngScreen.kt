package com.example.eeum.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.IngPost
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedIngScreen(
    onBack: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    val coroutineScope = rememberCoroutineScope()

    var posts by remember { mutableStateOf<List<IngPost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasMore by remember { mutableStateOf(true) }

    val pageSize = 10
    val pagerState = rememberPagerState(pageCount = { posts.size })

    fun loadMore() {
        if (isLoading || !hasMore) return
        coroutineScope.launch {
            isLoading = true
            errorMessage = null

            val lastPostId = posts.lastOrNull()?.postId
            val result = repository.fetchIngPosts(pageSize, lastPostId)

            result.onSuccess { newItems ->
                posts = posts + newItems
                hasMore = newItems.isNotEmpty()
            }.onFailure { error ->
                errorMessage = error.message ?: "피드를 불러오지 못했어요."
            }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadMore()
    }

    LaunchedEffect(pagerState, posts.size, hasMore, isLoading) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { currentPage ->
                if (posts.isEmpty()) return@collectLatest
                val shouldLoadMore = currentPage >= posts.lastIndex - 2
                if (shouldLoadMore) loadMore()
            }
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
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Home, contentDescription = "home")
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFECEBE7))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Inbox",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.MailOutline,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ing",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1D1D)
            )
            Text(
                text = "Done",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFB0B0B0)
            )
        }

        Spacer(Modifier.height(12.dp))

        when {
            posts.isNotEmpty() -> {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 48.dp),
                    pageSpacing = 20.dp,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    // ✅ Conflict resolved: use a version-safe offset formula
                    val pageOffset =
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                    val clampedOffset = pageOffset.coerceIn(-1f, 1f)
                    val absOffset = clampedOffset.absoluteValue
                    val scale = lerp(start = 0.9f, stop = 1f, fraction = 1f - absOffset)
                    val alpha = lerp(start = 0.65f, stop = 1f, fraction = 1f - absOffset)
                    val rotation = 6f * clampedOffset

                    FeedIngCard(
                        post = posts[page],
                        onOpenDetail = onOpenDetail,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            rotationZ = rotation
                            translationX = clampedOffset * 26f
                        }
                    )
                }
            }

            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
                        text = "표시할 피드가 없어요.",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                }
            }
        }

        if (isLoading && posts.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1D1D1D))
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage.orEmpty(),
                color = Color(0xFFE24A3B),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FeedIngCard(
    post: IngPost,
    onOpenDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
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
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6E2DC))
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = post.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = post.content,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF555555)
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onOpenDetail(post.postId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D1D1D),
                    contentColor = Color.White
                )
            ) {
                Text(text = "view", fontSize = 14.sp)
            }
        }
    }
}
