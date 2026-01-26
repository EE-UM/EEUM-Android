package com.example.eeum.data

data class IngPost(
    val postId: Long,
    val title: String,
    val content: String,
    val songName: String? = null,
    val artistName: String? = null,
    val artworkUrl: String? = null,
    val appleMusicUrl: String? = null,
    val createdAt: String? = null,
    val isCompleted: Boolean = false
)
