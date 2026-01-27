package com.example.eeum

import androidx.compose.runtime.*
import com.example.eeum.data.MusicTrack
import com.example.eeum.ui.*

@Composable
fun AppRoot() {

    var screen by remember { mutableStateOf(Screen.SPLASH) }
    var selectedTrack by remember { mutableStateOf<MusicTrack?>(null) }
    var selectedPostId by remember { mutableStateOf<Long?>(null) }

    when (screen) {
        Screen.SPLASH ->
            SplashScreen { screen = Screen.HOME_DEFAULT }

        Screen.HOME_DEFAULT ->
            HomeDefaultScreen(
                onShake = { screen = Screen.HOME_SHAKEN },
                onFeed = { screen = Screen.FEED_ING },
                onShare = { screen = Screen.SHARE_DEFAULT }
            )

        Screen.HOME_SHAKEN ->
            HomeShakenScreen(
                onReply = { /* view 이후 */ },
                onFeed = { screen = Screen.FEED_ING },
                onShare = { screen = Screen.SHARE_DEFAULT }
            )

        Screen.FEED_ING ->
            FeedIngScreen(
                onBack = { screen = Screen.HOME_DEFAULT },
                onOpenDetail = { postId ->
                    selectedPostId = postId
                    screen = Screen.POST_DETAIL
                }
            )

        Screen.SHARE_DEFAULT ->
            ShareDefaultScreen(
                onBack = { screen = Screen.HOME_DEFAULT },
                onOpenMusicSearch = { screen = Screen.SHARE_MUSIC },
                onShareComplete = { postId ->
                    selectedPostId = postId
                    screen = Screen.POST_DETAIL
                },
                selectedTrack = selectedTrack
            )

        Screen.SHARE_MUSIC ->
            ShareMusicSearchScreen(
                onBack = { screen = Screen.SHARE_DEFAULT },
                onSelectTrack = { track ->
                    selectedTrack = track
                    screen = Screen.SHARE_DEFAULT
                }
            )

        Screen.POST_DETAIL -> {
            val postId = selectedPostId
            if (postId == null) {
                screen = Screen.HOME_DEFAULT
            } else {
                PostDetailScreen(
                    postId = postId,
                    onBack = { screen = Screen.HOME_DEFAULT }
                )
            }
        }
    }
}
