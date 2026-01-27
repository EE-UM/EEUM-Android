package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.eeum.data.CompletionType
import com.example.eeum.data.CreatePostRequest
import com.example.eeum.data.MusicTrack
import com.example.eeum.data.ShareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ShareDefaultScreen(
    onBack: () -> Unit,
    onOpenMusicSearch: () -> Unit,
    onShareComplete: (Long) -> Unit,
    selectedTrack: MusicTrack?,
    repository: ShareRepository = ShareRepository()
) {
    val bg = Color(0xFFF7F6F2)
    var title by remember { mutableStateOf("") }
    var story by remember { mutableStateOf("") }
    var isSettingsVisible by remember { mutableStateOf(false) }
    var selectedCompletion by remember { mutableStateOf(ShareCompletionOption.AUTO) }
    var selectedMaxComments by remember { mutableStateOf(20) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var shareStatus by remember { mutableStateOf<ShareStatus?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val titleMaxLength = 50
    val storyMaxLength = 200
    val maxCommentOptions = listOf(5, 10, 15, 20, 25, 30)

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
                Icon(Icons.Filled.Home, contentDescription = "back to home")
            }
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "share"
            )
        }

        Spacer(Modifier.height(20.dp))

        if (selectedTrack == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFFE6E2DC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onOpenMusicSearch) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "search music"
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "사연의 제목을 작성해 주세요",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D1D1D)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .clickable { onOpenMusicSearch() }
                    .padding(16.dp)
            ) {
                val artworkUrl = selectedTrack.artworkUrl
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
                        contentDescription = "${selectedTrack.title} 앨범 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE6E2DC))
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedTrack.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE24A3B),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = selectedTrack.artist,
                        fontSize = 13.sp,
                        color = Color(0xFF1D1D1D)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "OO자에서 OO자 이내로 자유롭게 공유하고싶은 사연을 작성해 주세요",
            fontSize = 12.sp,
            color = Color(0xFF777777)
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = title,
            onValueChange = { title = it.take(titleMaxLength) },
            placeholder = { Text("사연의 제목") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = story,
            onValueChange = { story = it.take(storyMaxLength) },
            placeholder = { Text("사연의 내용") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = false
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${story.length}/$storyMaxLength",
            fontSize = 12.sp,
            color = Color(0xFF999999)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { isSettingsVisible = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1D1D1D),
                contentColor = Color.White
            )
        ) {
            Text("share", fontSize = 14.sp)
        }
        shareStatus?.let { status ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = status.message,
                fontSize = 12.sp,
                color = status.color
            )
        }
        Spacer(Modifier.height(12.dp))
    }

    if (isSettingsVisible) {
        Dialog(
            onDismissRequest = { isSettingsVisible = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .background(Color(0xFFF9F7F2), RoundedCornerShape(20.dp))
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "설정",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        RadioButton(
                            selected = selectedCompletion == ShareCompletionOption.AUTO,
                            onClick = { selectedCompletion = ShareCompletionOption.AUTO }
                        )
                        Column(modifier = Modifier.padding(start = 4.dp)) {
                            Text(
                                text = "플레이리스트 자동 완료",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "추가된 댓글 및 음악 수를 설정하면\n자동으로 플레이리스트가 완성처리됩니다.",
                                fontSize = 12.sp,
                                color = Color(0xFF7A7A7A)
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFE0DED8), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .clickable {
                                        if (selectedCompletion == ShareCompletionOption.AUTO) {
                                            isDropdownExpanded = true
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "최대 댓글 갯수",
                                    fontSize = 12.sp,
                                    color = Color(0xFF7A7A7A)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${selectedMaxComments}개",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(text = "▼", fontSize = 10.sp)
                                }
                            }
                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false }
                            ) {
                                maxCommentOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text("${option}개") },
                                        onClick = {
                                            selectedMaxComments = option
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        RadioButton(
                            selected = selectedCompletion == ShareCompletionOption.MANUAL,
                            onClick = { selectedCompletion = ShareCompletionOption.MANUAL }
                        )
                        Column(modifier = Modifier.padding(start = 4.dp)) {
                            Text(
                                text = "수동 완료 처리",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "inbox 내 작업한 사연에서 직접\n플레이리스트를 완료할 수 있습니다.",
                                fontSize = 12.sp,
                                color = Color(0xFF7A7A7A)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "취소",
                            fontSize = 14.sp,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier.clickable { isSettingsVisible = false }
                        )
                        Text(
                            text = "공유",
                            fontSize = 14.sp,
                            color = Color(0xFF1D1D1D),
                            modifier = Modifier.clickable(enabled = !isSubmitting) {
                                if (isSubmitting) return@clickable
                                when {
                                    selectedTrack == null -> {
                                        shareStatus = ShareStatus(
                                            message = "곡을 선택해 주세요.",
                                            color = Color(0xFFB00020)
                                        )
                                    }
                                    title.isBlank() -> {
                                        shareStatus = ShareStatus(
                                            message = "사연의 제목을 입력해 주세요.",
                                            color = Color(0xFFB00020)
                                        )
                                    }
                                    story.isBlank() -> {
                                        shareStatus = ShareStatus(
                                            message = "사연의 내용을 입력해 주세요.",
                                            color = Color(0xFFB00020)
                                        )
                                    }
                                    else -> {
                                        isSubmitting = true
                                        shareStatus = ShareStatus(
                                            message = "공유 중...",
                                            color = Color(0xFF777777)
                                        )
                                        val completionType = if (selectedCompletion == ShareCompletionOption.AUTO) {
                                            CompletionType.AUTO
                                        } else {
                                            CompletionType.MANUAL
                                        }
                                        coroutineScope.launch {
                                            val request = CreatePostRequest(
                                                title = title.trim(),
                                                content = story.trim(),
                                                albumName = selectedTrack.album,
                                                songName = selectedTrack.title,
                                                artistName = selectedTrack.artist,
                                                artworkUrl = selectedTrack.artworkUrl,
                                                appleMusicUrl = selectedTrack.previewUrl,
                                                completionType = completionType.value,
                                                commentCountLimit = selectedMaxComments
                                            )
                                            val result = withContext(Dispatchers.IO) {
                                                repository.sharePlaylist(request)
                                            }
                                            result
                                                .onSuccess {
                                                    shareStatus = ShareStatus(
                                                        message = "플레이리스트가 공유되었습니다.",
                                                        color = Color(0xFF1D1D1D)
                                                    )
                                                    isSettingsVisible = false
                                                    onShareComplete(it.postId)
                                                }
                                                .onFailure {
                                                    shareStatus = ShareStatus(
                                                        message = "공유에 실패했어요. 다시 시도해 주세요.",
                                                        color = Color(0xFFB00020)
                                                    )
                                                }
                                            isSubmitting = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

private enum class ShareCompletionOption {
    AUTO,
    MANUAL
}

private data class ShareStatus(
    val message: String,
    val color: Color
)
