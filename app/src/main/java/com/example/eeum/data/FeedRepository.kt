package com.example.eeum.data

class FeedRepository(
    private val api: MusicApi = MusicApiProvider.api
) {
    suspend fun fetchIngPosts(
        pageSize: Int,
        lastPostId: Long?
    ): Result<List<IngPost>> {
        return runCatching {
            val response = api.fetchIngPosts(pageSize = pageSize, lastPostId = lastPostId)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "피드를 불러오지 못했어요.")
        }
    }
}
