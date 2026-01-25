package com.example.eeum.data

class ShareRepository(
    private val api: MusicApi = MusicApiProvider.api
) {
    private var cachedAuthHeader: String? = null

    suspend fun sharePlaylist(request: CreatePostRequest): Result<CreatePostResponse> {
        return runCatching {
            val authHeader = cachedAuthHeader ?: run {
                val loginResponse = api.loginForTest(
                    TestLoginRequest(
                        idToken = "test",
                        provider = "test"
                    )
                )
                val loginData = loginResponse.data
                    ?: throw IllegalStateException(loginResponse.error?.message ?: "로그인 실패")
                val tokenType = loginData.tokenType.ifBlank { "Bearer" }
                val header = "${tokenType.trim()} ${loginData.accessToken}".trim()
                cachedAuthHeader = header
                header
            }

            val response = api.createPost(authHeader, request)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "공유 실패")
        }
    }
}
