package com.example.eeum.data

class FeedRepository(
    private val api: MusicApi = MusicApiProvider.api
) {
    private suspend fun getAuthHeader(): String {
        return AuthHeaderStore.getOrFetchAuthHeader(api)
    }

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

    suspend fun fetchMyPosts(): Result<MyPostsData> {
        return runCatching {
            val authHeader = getAuthHeader()
            val response = api.fetchMyPosts(authHeader)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "내 게시글을 불러오지 못했어요.")
        }
    }

    suspend fun fetchCommentedPosts(): Result<CommentedPostsData> {
        return runCatching {
            val authHeader = getAuthHeader()
            val response = api.fetchCommentedPosts(authHeader)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "댓글 단 게시글을 불러오지 못했어요.")
        }
    }

    suspend fun fetchLikedPosts(): Result<LikedPostsData> {
        return runCatching {
            val authHeader = getAuthHeader()
            val response = api.fetchLikedPosts(authHeader)
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "좋아요 게시글을 불러오지 못했어요.")
        }
    }

    suspend fun reportComment(
        commentId: Long,
        reportedUserId: Long,
        reportReason: String
    ): Result<ReportCommentResponse> {
        return runCatching {
            val authHeader = getAuthHeader()
            val response = api.reportComment(
                authHeader,
                ReportCommentRequest(
                    commentId = commentId,
                    reportedUserId = reportedUserId,
                    reportReason = reportReason
                )
            )
            response.data
                ?: throw IllegalStateException(response.error?.message ?: "신고를 완료하지 못했어요.")
        }
    }
}
