package com.example.eeum.data

class ShareRepository(
    private val api: MusicApi = MusicApiProvider.api
) {
    suspend fun sharePlaylist(request: CreatePostRequest): Result<CreatePostResponse> {
        return runCatching {
            val authHeader = AuthHeaderStore.getOrFetchAuthHeader(api)

            val response = api.createPost(authHeader, request)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "공유 실패")
        }
    }
}
