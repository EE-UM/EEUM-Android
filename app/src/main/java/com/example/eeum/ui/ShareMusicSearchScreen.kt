package com.example.eeum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.example.eeum.data.MusicRepository
import com.example.eeum.data.MusicTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ShareMusicSearchScreen(
    onBack: () -> Unit,
    repository: MusicRepository = MusicRepository()
) {
    val bg = Color(0xFFF7F6F2)
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<MusicTrack>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchTypes = "songs"
    val searchLimit = 10

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "음악 검색",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(16.dp))

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("노래 제목 또는 가수") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (query.isBlank()) return@IconButton
                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null
                            val keyword = query
                            val response = runCatching {
                                withContext(Dispatchers.IO) {
                                    repository.search(
                                        term = keyword,
                                        types = searchTypes,
                                        limit = searchLimit
                                    )
                                }
                            }
                            response
                                .onSuccess { results = it }
                                .onFailure { errorMessage = "음악을 불러오지 못했어요." }
                            isLoading = false
                        }
                    }
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "search")
                }
            },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        when {
            isLoading -> {
                Text(
                    text = "검색 중...",
                    fontSize = 14.sp,
                    color = Color(0xFF777777)
                )
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    fontSize = 14.sp,
                    color = Color(0xFFB00020)
                )
            }

            results.isEmpty() -> {
                Text(
                    text = "검색 결과가 없습니다.",
                    fontSize = 14.sp,
                    color = Color(0xFF777777)
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(results) { track ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFFE6E2DC), RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = track.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = track.artist,
                                    fontSize = 12.sp,
                                    color = Color(0xFF777777)
                                )
                                track.album?.takeIf { it.isNotBlank() }?.let { album ->
                                    Text(
                                        text = album,
                                        fontSize = 11.sp,
                                        color = Color(0xFF9A9A9A)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
