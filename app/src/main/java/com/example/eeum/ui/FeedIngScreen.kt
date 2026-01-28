package com.example.eeum.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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

    var selectedTab by remember { mutableStateOf(FeedTab.ING) }

    var ingPosts by remember { mutableStateOf<List<IngPost>>(emptyList()) }
    var isLoadingIng by remember { mutableStateOf(false) }
    var errorIng by remember { mutableStateOf<String?>(null) }
    var hasMoreIng by remember { mutableStateOf(true) }

    var donePosts by remember { mutableStateOf<List<IngPost>>(emptyList()) }
    var isLoadingDone by remember { mutableStateOf(false) }
    var errorDone by remember { mutableStateOf<String?>(null) }
    var hasMoreDone by remember { mutableStateOf(true) }

    val pageSize = 10
    val pagerState = rememberPagerState(pageCount = { ingPosts.size })
    val doneGridState = rememberLazyGridState()

    fun loadMoreIng() {
        if (isLoadingIng || !hasMoreIng) return
        coroutineScope.launch {
            isLoadingIng = true
            errorIng = null

            val lastPostId = ingPosts.lastOrNull()?.postId
            val result = repository.fetchIngPosts(pageSize, lastPostId)

            result.onSuccess { newItems ->
                ingPosts = ingPosts + newItems
                hasMoreIng = newItems.isNotEmpty()
            }.onFailure { error ->
                errorIng = error.message ?: "피드를 불러오지 못했어요."
            }

            isLoadingIng = false
        }
    }

    fun loadMoreDone() {
        if (isLoadingDone || !hasMoreDone) return
        coroutineScope.launch {
            isLoadingDone = true
            errorDone = null

            val lastPostId = donePosts.lastOrNull()?.postId
            val result = repository.fetchDonePosts(pageSize, lastPostId)

            result.onSuccess { newItems ->
                donePosts = donePosts + newItems
                hasMoreDone = newItems.isNotEmpty()
            }.onFailure { error ->
                errorDone = error.message ?: "완료된 피드를 불러오지 못했어요."
            }

            isLoadingDone = false
        }
    }

    LaunchedEffect(Unit) {
        loadMoreIng()
    }

    LaunchedEffect(selectedTab) {
        if (selectedTab == FeedTab.DONE && donePosts.isEmpty()) {
            loadMoreDone()
        }
    }

    LaunchedEffect(pagerState, ingPosts.size, hasMoreIng, isLoadingIng) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { currentPage ->
                if (ingPosts.isEmpty()) return@collectLatest
                val shouldLoadMore = currentPage >= ingPosts.lastIndex - 2
                if (shouldLoadMore) loadMoreIng()
            }
    }

    LaunchedEffect(doneGridState, donePosts.size, hasMoreDone, isLoadingDone) {
        snapshotFlow { doneGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleIndex ->
                if (lastVisibleIndex == null || donePosts.isEmpty()) return@collectLatest
                val shouldLoadMore = lastVisibleIndex >= donePosts.lastIndex - 2
                if (shouldLoadMore) loadMoreDone()
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
            FeedTabItem(
                title = "Ing",
                isSelected = selectedTab == FeedTab.ING,
                onClick = { selectedTab = FeedTab.ING }
            )
            FeedTabItem(
                title = "Done",
                isSelected = selectedTab == FeedTab.DONE,
                onClick = { selectedTab = FeedTab.DONE }
            )
        }

        Spacer(Modifier.height(12.dp))

        when (selectedTab) {
            FeedTab.ING -> {
                when {
                    ingPosts.isNotEmpty() -> {
                        HorizontalPager(
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 36.dp),
                            pageSpacing = 16.dp,
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

                            FeedIngImageCard(
                                post = ingPosts[page],
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    this.alpha = alpha
                                    rotationZ = rotation
                                    translationX = clampedOffset * 26f
                                }
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        FeedIngDetailPanel(
                            post = ingPosts[pagerState.currentPage],
                            onOpenDetail = onOpenDetail
                        )
                    }

                    isLoadingIng -> {
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
            }

            FeedTab.DONE -> {
                if (donePosts.isNotEmpty()) {
                    LazyVerticalGrid(
                        state = doneGridState,
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        itemsIndexed(donePosts, key = { _, item -> item.postId }) { index, post ->
                            FeedDoneCard(
                                index = index,
                                post = post,
                                onOpenDetail = onOpenDetail
                            )
                        }

                        if (isLoadingDone) {
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color(0xFF1D1D1D))
                                }
                            }
                        }
                    }
                } else if (isLoadingDone) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1D1D1D))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "완료된 피드가 없어요.",
                            fontSize = 12.sp,
                            color = Color(0xFF777777)
                        )
                    }
                }

                if (errorDone != null) {
                    Text(
                        text = errorDone.orEmpty(),
                        color = Color(0xFFE24A3B),
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (selectedTab == FeedTab.ING) {
            if (isLoadingIng && ingPosts.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1D1D1D))
                }
            }

            if (errorIng != null) {
                Text(
                    text = errorIng.orEmpty(),
                    color = Color(0xFFE24A3B),
                    fontSize = 12.sp
                )
            }
        }
    }
}

private enum class FeedTab {
    ING,
    DONE
}

@Composable
private fun FeedTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(bottom = 2.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF1D1D1D) else Color(0xFFB0B0B0),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .padding(vertical = 2.dp)
                .clickable(onClick = onClick)
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(3.dp)
                .background(
                    if (isSelected) Color(0xFF1D1D1D) else Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
private fun FeedIngImageCard(
    post: IngPost,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        val artworkUrl = post.artworkUrl
        if (artworkUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color(0xFFE6E2DC)),
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
                    .height(240.dp)
                    .background(Color(0xFFE6E2DC))
            )
        }
    }
}

@Composable
private fun FeedIngDetailPanel(
    post: IngPost,
    onOpenDetail: (Long) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
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

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onOpenDetail(post.postId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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

@Composable
private fun FeedDoneCard(
    index: Int,
    post: IngPost,
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
                Text(
                    text = "${index + 1}/20",
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

        val subtitle = listOfNotNull(post.songName, post.artistName).joinToString(" ")
        Text(
            text = if (subtitle.isBlank()) " " else subtitle,
            fontSize = 10.sp,
            color = Color(0xFF7A7A7A),
            maxLines = 1
        )
    }
}
