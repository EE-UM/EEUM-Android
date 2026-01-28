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

data class CommentedPostsData(
    val commentedPostsCount: Int = 0,
    val getCommentedPostsResponses: List<CommentedPost> = emptyList()
)

data class CommentedPost(
    val postId: Long,
    val title: String,
    val artworkUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class LikedPostsData(
    val likedPostsSize: Int = 0,
    val getLikedPostsResponses: List<LikedPost> = emptyList()
)

data class LikedPost(
    val postId: Long,
    val title: String? = null,
    val artworkUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
