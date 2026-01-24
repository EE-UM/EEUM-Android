package com.example.eeum

import androidx.compose.runtime.*
import com.example.eeum.ui.*

@Composable
fun AppRoot() {
    var screen by remember { mutableStateOf(Screen.SPLASH) }

    when (screen) {
        Screen.SPLASH ->
            SplashScreen { screen = Screen.HOME_DEFAULT }

        Screen.HOME_DEFAULT ->
            HomeDefaultScreen { screen = Screen.HOME_SHAKEN }

        Screen.HOME_SHAKEN ->
            HomeShakenScreen { /* view 이후 */ }
    }
}
