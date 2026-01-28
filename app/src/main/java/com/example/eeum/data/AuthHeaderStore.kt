package com.example.eeum.data

object AuthHeaderStore {
    @Volatile
    private var cachedAuthHeader: String? = null

    fun setAuthToken(accessToken: String, tokenType: String = "Bearer") {
        val safeTokenType = tokenType.ifBlank { "Bearer" }.trim()
        cachedAuthHeader = "${safeTokenType} ${accessToken.trim()}".trim()
    }

    fun clearAuthToken() {
        cachedAuthHeader = null
    }

    suspend fun getOrFetchAuthHeader(api: MusicApi): String {
        return cachedAuthHeader ?: run {
            val loginResponse = api.loginForTest(
                TestLoginRequest(
                    idToken = "test",
                    provider = "test"
                )
            )
            val loginData = loginResponse.data
                ?: throw IllegalStateException(loginResponse.error?.message ?: "로그인 실패")
            val header = "${loginData.tokenType.ifBlank { "Bearer" }.trim()} ${loginData.accessToken}".trim()
            cachedAuthHeader = header
            header
        }
    }
}
