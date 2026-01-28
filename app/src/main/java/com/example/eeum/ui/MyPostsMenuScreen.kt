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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.R
import com.example.eeum.data.CommentedPost
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.LikedPost
import kotlinx.coroutines.launch

@Composable
fun MyPostsMenuScreen(
    onClose: () -> Unit,
    onOpenPosts: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenLikes: () -> Unit
) {
    val bg = Color(0xFFF7F6F2)
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
            Text(
                text = "Menu",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D)
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "close")
            }
        }

        Spacer(Modifier.height(16.dp))

        MenuItem(text = "Posts", onClick = onOpenPosts)
        MenuItem(text = "Comments", onClick = onOpenComments)
        MenuItem(text = "Likes", onClick = onOpenLikes)
    }
}

@Composable
fun MyCommentsScreen(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    val coroutineScope = rememberCoroutineScope()

    var comments by remember { mutableStateOf<List<CommentedPost>>(emptyList()) }
    var commentCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadComments() {
        if (isLoading) return
        coroutineScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.fetchCommentedPosts()
            result.onSuccess { data ->
                comments = data.getCommentedPostsResponses
                commentCount = data.commentedPostsCount
            }.onFailure { error ->
                errorMessage = error.message ?: "댓글 단 게시글을 불러오지 못했어요."
            }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadComments()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TopNavigationRow(onHome = onHome, onOpenMenu = onOpenMenu)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Comments",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "${commentCount}개\n참여한 사연과 플레이리스트입니다.",
            fontSize = 12.sp,
            color = Color(0xFF7A7A7A)
        )

        Spacer(Modifier.height(16.dp))

        when {
            comments.isNotEmpty() -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(comments) { comment ->
                        CommentedPostRow(comment = comment)
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
                        text = "표시할 댓글 게시글이 없어요.",
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
fun MyLikesScreen(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    val coroutineScope = rememberCoroutineScope()

    var likes by remember { mutableStateOf<List<LikedPost>>(emptyList()) }
    var likesCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadLikes() {
        if (isLoading) return
        coroutineScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.fetchLikedPosts()
            result.onSuccess { data ->
                likes = data.getLikedPostsResponses
                likesCount = data.likedPostsSize
            }.onFailure { error ->
                errorMessage = error.message ?: "좋아요 게시글을 불러오지 못했어요."
            }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadLikes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 24.dp)
            .padding(top = 12.dp)
    ) {
        TopNavigationRow(onHome = onHome, onOpenMenu = onOpenMenu)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Likes",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "${likesCount}개\n좋아요 한 플레이리스트입니다.",
            fontSize = 12.sp,
            color = Color(0xFF7A7A7A)
        )

        Spacer(Modifier.height(16.dp))

        when {
            likes.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(likes) { likedPost ->
                        LikedPostCard(likedPost = likedPost)
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
                        text = "표시할 좋아요 게시글이 없어요.",
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
private fun CommentedPostRow(comment: CommentedPost) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE6E2DC))
        ) {
            val artworkUrl = comment.artworkUrl
            if (!artworkUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artworkUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = comment.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp)
        ) {
            Text(
                text = comment.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1D1D),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = comment.createdAt ?: comment.updatedAt ?: "사연을 확인해 보세요.",
                fontSize = 10.sp,
                color = Color(0xFF7A7A7A),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LikedPostCard(likedPost: LikedPost) {
    val title = likedPost.title ?: "사연 제목"

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE6E2DC))
        ) {
            val artworkUrl = likedPost.artworkUrl
            if (!artworkUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artworkUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color(0xFFD4574B),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(16.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = likedPost.createdAt ?: likedPost.updatedAt ?: "좋아요 한 게시글입니다.",
                    fontSize = 10.sp,
                    color = Color(0xFF7A7A7A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(6.dp))
        }
    }
}

@Composable
private fun TopNavigationRow(
    onHome: () -> Unit,
    onOpenMenu: () -> Unit
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
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1D1D1D),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    )
}
