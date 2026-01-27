package com.example.eeum.data

data class PostDetail(
    val postId: Long,
    val title: String,
    val content: String,
    val songName: String? = null,
    val artistName: String? = null,
    val artworkUrl: String? = null,
    val appleMusicUrl: String? = null,
    val createdAt: String? = null,
    val isLiked: Boolean = false,
    val comments: List<PostComment> = emptyList()
)

data class PostComment(
    val commentId: Long,
    val content: String,
    val postId: Long,
    val userId: Long,
    val username: String? = null,
    val albumName: String? = null,
    val songName: String? = null,
    val artistName: String? = null,
    val artworkUrl: String? = null,
    val appleMusicUrl: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: String? = null,
    val modifiedAt: String? = null
)
