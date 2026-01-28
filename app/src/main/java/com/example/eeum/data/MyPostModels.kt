package com.example.eeum.data

data class MyPostsData(
    val postCount: Int = 0,
    val getMyPostResponses: List<MyPost> = emptyList()
)

data class MyPost(
    val postId: Long,
    val title: String,
    val artworkUrl: String? = null,
    val isCompleted: Boolean = false,
    val currentPlaylistCount: Int = 0,
    val targetPlaylistCount: Int = 0
)
