package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.changedToCanceled
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.forEachGesture
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.data.FeedRepository
import com.example.eeum.data.MusicTrack
import com.example.eeum.data.PostComment
import com.example.eeum.data.PostDetail
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun PostDetailScreen(
    postId: Long,
    onBack: () -> Unit,
    onOpenMusicSearch: () -> Unit,
    selectedTrack: MusicTrack?,
    onClearSelectedTrack: () -> Unit,
    repository: FeedRepository = FeedRepository()
) {
    val bg = Color(0xFFF7F6F2)
    var detail by remember { mutableStateOf<PostDetail?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isGridView by remember { mutableStateOf(true) }
    var commentInput by remember { mutableStateOf("") }
    var localComments by remember { mutableStateOf<List<PostComment>>(emptyList()) }
    var reportTarget by remember { mutableStateOf<PostComment?>(null) }
    var showReportAction by remember { mutableStateOf(false) }
    var showReportReasons by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(postId) {
        isLoading = true
        repository.fetchPostDetail(postId)
            .onSuccess { detail = it }
            .onFailure { errorMessage = it.message ?: "게시글을 불러오지 못했어요." }
        isLoading = false
    }

    LaunchedEffect(detail?.postId) {
        localComments = detail?.comments ?: emptyList()
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

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (artworkUrl.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.62f)
                                    .aspectRatio(1f)
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
                                    .fillMaxWidth(0.62f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE6E2DC))
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = post.songName ?: "곡 제목",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D1D1D),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = post.artistName ?: "아티스트",
                            fontSize = 12.sp,
                            color = Color(0xFF777777),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                    .heightIn(max = 500.dp)
                    .background(
                        Color(0xFFF7F6F2),
                        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
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

                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                Text(
                    text = post.content,
                    fontSize = 13.sp,
                    color = Color(0xFF3A3A3A),
                    lineHeight = 19.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "like",
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(18.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Message,
                        contentDescription = "comment",
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                Text(
                    text = "댓글",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                    Text(
                        text = if (isGridView) "글 보기" else "그리드 보기",
                        fontSize = 12.sp,
                        color = Color(0xFF777777),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .clickable { isGridView = !isGridView }
                    )
                }

                if (localComments.isEmpty()) {
                    Text(
                        text = "아직 댓글이 없어요.",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (isGridView) {
                            localComments.chunked(2).forEach { rowComments ->
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
                        } else {
                            localComments.forEach { comment ->
                                CommentListItem(
                                    comment = comment,
                                    modifier = Modifier.fillMaxWidth(),
                                    onLongPress = {
                                        reportTarget = comment
                                        showReportAction = true
                                    }
                                )
                            }
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    if (selectedTrack != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEDEBE5), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = selectedTrack.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1D1D1D),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = selectedTrack.artist,
                                    fontSize = 11.sp,
                                    color = Color(0xFF777777),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(onClick = onClearSelectedTrack) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "선택한 음악 삭제",
                                    tint = Color(0xFF777777)
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF1D1D1D))
                                .clickable { onOpenMusicSearch() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "댓글에 음악 추가",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        TextField(
                            value = commentInput,
                            onValueChange = { commentInput = it },
                            placeholder = { Text("사연과 관련된 노래를 골라주세요.") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(18.dp),
                            singleLine = false
                        )

                        IconButton(
                            onClick = {
                                if (commentInput.isBlank() && selectedTrack == null) return@IconButton
                                val newComment = PostComment(
                                    commentId = System.currentTimeMillis(),
                                    content = commentInput.trim(),
                                    postId = post.postId,
                                    userId = 0L,
                                    username = "나",
                                    albumName = selectedTrack?.album,
                                    songName = selectedTrack?.title,
                                    artistName = selectedTrack?.artist,
                                    artworkUrl = selectedTrack?.artworkUrl
                                )
                                localComments = localComments + newComment
                                commentInput = ""
                                onClearSelectedTrack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "댓글 등록",
                                tint = Color(0xFF1D1D1D)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showReportAction) {
        ReportActionDialog(
            comment = reportTarget,
            onDismiss = { showReportAction = false },
            onReportClick = {
                showReportAction = false
                showReportReasons = true
            }
        )
    }

    if (showReportReasons) {
        val target = reportTarget
        ReportReasonDialog(
            onDismiss = {
                showReportReasons = false
                reportTarget = null
            },
            onReasonSelected = { reason ->
                val trimmedReason = reason.trim()
                if (target == null || trimmedReason.isBlank()) {
                    showReportReasons = false
                    reportTarget = null
                    return@ReportReasonDialog
                }
                if (target.userId == 0L) {
                    android.widget.Toast
                        .makeText(context, "신고할 수 없는 댓글이에요.", android.widget.Toast.LENGTH_SHORT)
                        .show()
                    showReportReasons = false
                    reportTarget = null
                    return@ReportReasonDialog
                }
                coroutineScope.launch {
                    repository.reportComment(
                        commentId = target.commentId,
                        reportedUserId = target.userId,
                        reportReason = trimmedReason
                    ).onSuccess {
                        android.widget.Toast
                            .makeText(context, "신고가 완료되었어요.", android.widget.Toast.LENGTH_SHORT)
                            .show()
                    }.onFailure {
                        android.widget.Toast
                            .makeText(context, it.message ?: "신고에 실패했어요.", android.widget.Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                showReportReasons = false
                reportTarget = null
            }
        )
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
                    .aspectRatio(1f)
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
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE6E2DC))
            )
        }

        Text(
            text = comment.songName ?: "곡 제목",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1D1D),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = comment.artistName ?: "아티스트",
            fontSize = 11.sp,
            color = Color(0xFF777777),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CommentListItem(
    comment: PostComment,
    modifier: Modifier = Modifier,
    onLongPress: (() -> Unit)? = null
) {
    val interactiveModifier = if (onLongPress != null) {
        modifier.pointerInput(Unit) {
            forEachGesture {
                awaitPointerEventScope {
                    val down = awaitPointerEvent()
                        .changes
                        .firstOrNull { it.changedToDown() }
                        ?: return@awaitPointerEventScope
                    val pointerId = down.id
                    val longPressJob = launch {
                        delay(1000L)
                        onLongPress()
                    }
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId } ?: continue
                        if (change.changedToUp() || change.changedToCanceled()) {
                            longPressJob.cancel()
                            break
                        }
                    }
                }
            }
        }
    } else {
        modifier
    }

    Column(
        modifier = interactiveModifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "♪",
                fontSize = 12.sp,
                color = Color(0xFF777777)
            )

            Text(
                text = listOfNotNull(comment.songName, comment.artistName).joinToString(" • "),
                fontSize = 12.sp,
                color = Color(0xFF1D1D1D),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!comment.content.isNullOrBlank()) {
            Text(
                text = comment.content.orEmpty(),
                fontSize = 12.sp,
                color = Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ReportActionDialog(
    comment: PostComment?,
    onDismiss: () -> Unit,
    onReportClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66000000))
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF7F6F2)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "♪",
                            fontSize = 12.sp,
                            color = Color(0xFF777777)
                        )
                        Text(
                            text = listOfNotNull(comment?.songName, comment?.artistName).joinToString(" • ")
                                .ifBlank { "댓글" },
                            fontSize = 12.sp,
                            color = Color(0xFF1D1D1D),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (!comment?.content.isNullOrBlank()) {
                        Text(
                            text = comment?.content.orEmpty(),
                            fontSize = 12.sp,
                            color = Color(0xFF555555),
                            lineHeight = 18.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    TextButton(
                        onClick = onReportClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            text = "신고하기",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D1D1D)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportReasonDialog(
    onDismiss: () -> Unit,
    onReasonSelected: (String) -> Unit
) {
    val reasons = listOf(
        "폭력 및 혐오 표현",
        "성적 불쾌감을 일으키는 표현",
        "스팸 및 사기",
        "기타: 입력해주세요"
    )
    var showCustomInput by remember { mutableStateOf(false) }
    var customReason by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF7F6F2)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "닫기")
                    }
                    Text(
                        text = "신고하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(48.dp))
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "이 댓글을 신고하는 이유가 무엇인가요?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )

                Spacer(Modifier.height(8.dp))

                reasons.forEachIndexed { index, reason ->
                    Text(
                        text = reason,
                        fontSize = 13.sp,
                        color = Color(0xFF1D1D1D),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (index == reasons.lastIndex) {
                                    showCustomInput = true
                                } else {
                                    onReasonSelected(reason)
                                }
                            }
                            .padding(vertical = 12.dp)
                    )
                    if (index != reasons.lastIndex) {
                        Divider(color = Color(0xFFE0DED7))
                    }
                }

                if (showCustomInput) {
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = customReason,
                        onValueChange = { customReason = it },
                        placeholder = { Text("신고 사유를 입력해주세요.") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        onClick = { onReasonSelected(customReason.trim()) },
                        enabled = customReason.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0DED7), RoundedCornerShape(18.dp))
                            .background(Color.White, RoundedCornerShape(18.dp))
                    ) {
                        Text(
                            text = "신고하기",
                            fontSize = 14.sp,
                            color = Color(0xFF1D1D1D)
                        )
                    }
                }
            }
        }
    }
}
