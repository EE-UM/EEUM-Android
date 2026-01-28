package com.example.eeum

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import com.example.eeum.data.MusicTrack
import com.example.eeum.ui.*

@Composable
fun AppRoot() {

    var screen by remember { mutableStateOf(Screen.SPLASH) }
    val backStack = remember { mutableStateListOf<Screen>() }
    var selectedTrack by remember { mutableStateOf<MusicTrack?>(null) }
    var selectedCommentTrack by remember { mutableStateOf<MusicTrack?>(null) }
    var selectedPostId by remember { mutableStateOf<Long?>(null) }

    val navigateTo: (Screen, Boolean) -> Unit = { target, addToBackStack ->
        if (addToBackStack) {
            backStack.add(screen)
        }
        screen = target
    }

    val popBack: () -> Unit = {
        if (backStack.isNotEmpty()) {
            screen = backStack.removeLast()
        }
    }

    val navigateHome: () -> Unit = {
        backStack.clear()
        screen = Screen.HOME_DEFAULT
    }

    BackHandler(enabled = backStack.isNotEmpty()) {
        popBack()
    }

    when (screen) {
        Screen.SPLASH ->
            SplashScreen { navigateTo(Screen.HOME_DEFAULT, false) }

        Screen.HOME_DEFAULT ->
            HomeDefaultScreen(
                onShake = { navigateTo(Screen.HOME_SHAKEN, true) },
                onFeed = { navigateTo(Screen.FEED_ING, true) },
                onShare = { navigateTo(Screen.SHARE_DEFAULT, true) },
                onSettings = { navigateTo(Screen.SETTINGS, true) }
            )

        Screen.HOME_SHAKEN ->
            HomeShakenScreen(
                onView = { postId ->
                    selectedPostId = postId
                    navigateTo(Screen.POST_DETAIL, true)
                },
                onFeed = { navigateTo(Screen.FEED_ING, true) },
                onShare = { navigateTo(Screen.SHARE_DEFAULT, true) }
            )

        Screen.FEED_ING ->
            FeedIngScreen(
                onBack = { popBack() },
                onOpenInbox = { navigateTo(Screen.MY_POSTS, true) },
                onOpenDetail = { postId ->
                    selectedPostId = postId
                    navigateTo(Screen.POST_DETAIL, true)
                }
            )

        Screen.SHARE_DEFAULT ->
            ShareDefaultScreen(
                onBack = { popBack() },
                onOpenMusicSearch = { navigateTo(Screen.SHARE_MUSIC, true) },
                onShareComplete = { postId ->
                    selectedPostId = postId
                    navigateTo(Screen.POST_DETAIL, true)
                },
                selectedTrack = selectedTrack
            )

        Screen.SHARE_MUSIC ->
            ShareMusicSearchScreen(
                onBack = { popBack() },
                onSelectTrack = { track ->
                    selectedTrack = track
                    popBack()
                }
            )
        Screen.SETTINGS ->
            SettingsScreen(
                onBack = { popBack() }
            )

        Screen.POST_DETAIL -> {
            val postId = selectedPostId
            if (postId == null) {
                navigateTo(Screen.HOME_DEFAULT, false)
            } else {
                PostDetailScreen(
                    postId = postId,
                    onBack = {
                        selectedCommentTrack = null
                        popBack()
                    },
                    onOpenMusicSearch = { navigateTo(Screen.POST_COMMENT_MUSIC, true) },
                    selectedTrack = selectedCommentTrack,
                    onClearSelectedTrack = { selectedCommentTrack = null }
                )
            }
        }

        Screen.POST_COMMENT_MUSIC ->
            ShareMusicSearchScreen(
                onBack = { popBack() },
                onSelectTrack = { track ->
                    selectedCommentTrack = track
                    popBack()
                }
            )

        Screen.MY_POSTS ->
            MyPostsScreen(
                onHome = { navigateHome() },
                onOpenMenu = { navigateTo(Screen.MY_POSTS_MENU, true) },
                onOpenDetail = { postId ->
                    selectedPostId = postId
                    navigateTo(Screen.POST_DETAIL, true)
                }
            )

        Screen.MY_POSTS_MENU ->
            MyPostsMenuScreen(
                onClose = { popBack() },
                onOpenPosts = { navigateTo(Screen.MY_POSTS, true) },
                onOpenComments = { navigateTo(Screen.MY_POSTS_COMMENTS, true) },
                onOpenLikes = { navigateTo(Screen.MY_POSTS_LIKES, true) }
            )

        Screen.MY_POSTS_COMMENTS ->
            MyCommentsScreen(
                onHome = { navigateHome() },
                onOpenMenu = { navigateTo(Screen.MY_POSTS_MENU, true) }
            )

        Screen.MY_POSTS_LIKES ->
            MyLikesScreen(
                onHome = { navigateHome() },
                onOpenMenu = { navigateTo(Screen.MY_POSTS_MENU, true) }
            )
    }
}
