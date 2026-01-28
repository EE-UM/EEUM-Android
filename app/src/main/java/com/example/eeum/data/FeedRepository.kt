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

    suspend fun fetchDonePosts(
        pageSize: Int,
        lastPostId: Long?
    ): Result<List<IngPost>> {
        return runCatching {
            val response = api.fetchDonePosts(pageSize = pageSize, lastPostId = lastPostId)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "완료된 피드를 불러오지 못했어요.")
        }
    }

    suspend fun fetchPostDetail(postId: Long): Result<PostDetail> {
        return runCatching {
            val response = api.fetchPostDetail(postId)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "게시글을 불러오지 못했어요.")
        }
    }

    suspend fun fetchRandomPost(): Result<RandomPost> {
        return runCatching {
            val response = api.fetchRandomPost()
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "랜덤 게시글을 불러오지 못했어요.")
        }
    }
}
