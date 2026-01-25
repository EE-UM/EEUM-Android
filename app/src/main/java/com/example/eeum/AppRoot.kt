package com.example.eeum

import androidx.compose.runtime.*
import com.example.eeum.data.MusicTrack
import com.example.eeum.ui.*

@Composable
fun AppRoot() {

    var screen by remember { mutableStateOf(Screen.SPLASH) }
    var selectedTrack by remember { mutableStateOf<MusicTrack?>(null) }

    when (screen) {
        Screen.SPLASH ->
            SplashScreen { screen = Screen.HOME_DEFAULT }

        Screen.HOME_DEFAULT ->
            HomeDefaultScreen(
                onShake = { screen = Screen.HOME_SHAKEN },
                onShare = { screen = Screen.SHARE_DEFAULT }
            )

        Screen.HOME_SHAKEN ->
            HomeShakenScreen(
                onReply = { /* view 이후 */ },
                onShare = { screen = Screen.SHARE_DEFAULT }
            )

        Screen.SHARE_DEFAULT ->
            ShareDefaultScreen(
                onBack = { screen = Screen.HOME_DEFAULT },
                onOpenMusicSearch = { screen = Screen.SHARE_MUSIC },
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
    }
}
