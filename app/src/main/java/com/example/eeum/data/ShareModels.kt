package com.example.eeum.data

data class ApiError(
    val code: String? = null,
    val message: String? = null,
    val data: Map<String, Any>? = null
)

data class ApiResponse<T>(
    val result: String,
    val data: T? = null,
    val error: ApiError? = null
)

data class TestLoginRequest(
    val idToken: String,
    val provider: String
)

data class TestLoginResponse(
    val accessToken: String,
    val tokenType: String,
    val role: String? = null,
    val isRegistered: Boolean? = null
)

data class CreatePostRequest(
    val title: String,
    val content: String,
    val albumName: String? = null,
    val songName: String? = null,
    val artistName: String? = null,
    val artworkUrl: String? = null,
    val appleMusicUrl: String? = null,
    val completionType: String,
    val commentCountLimit: Int
)

data class CreatePostResponse(
    val postId: Long,
    val userId: Long
)

enum class CompletionType(val value: String) {
    AUTO("AUTO_COMPLETION"),
    MANUAL("MANUAL_COMPLETION")
}
