package com.example.eeum.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class MusicSearchResponse(
    val result: String,
    val data: List<MusicSearchItem> = emptyList(),
    val error: MusicSearchError? = null
)

data class MusicSearchItem(
    val albumName: String? = null,
    val songName: String? = null,
    val artistName: String? = null,
    val artworkUrl: String? = null,
    val previewMusicUrl: String? = null
)

data class MusicSearchError(
    val code: String? = null,
    val message: String? = null
)

interface MusicApi {
    @GET("apple-music/search")
    suspend fun searchMusic(
        @Query("term") term: String,
        @Query("types") types: String,
        @Query("limit") limit: Int
    ): MusicSearchResponse

    @POST("user/test")
    suspend fun loginForTest(
        @Body request: TestLoginRequest
    ): ApiResponse<TestLoginResponse>

    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Body request: CreatePostRequest
    ): ApiResponse<CreatePostResponse>

    @GET("posts/ing/infinite-scroll")
    suspend fun fetchIngPosts(
        @Query("pageSize") pageSize: Int,
        @Query("lastPostId") lastPostId: Long?
    ): ApiResponse<List<IngPost>>

    @GET("posts/done/infinite-scroll")
    suspend fun fetchDonePosts(
        @Query("pageSize") pageSize: Int,
        @Query("lastPostId") lastPostId: Long?
    ): ApiResponse<List<IngPost>>

    @GET("posts/{postId}")
    suspend fun fetchPostDetail(
        @Path("postId") postId: Long
    ): ApiResponse<PostDetail>

    @GET("posts/random")
    suspend fun fetchRandomPost(): ApiResponse<RandomPost>

    @GET("posts/my")
    suspend fun fetchMyPosts(
        @Header("Authorization") authorization: String
    ): ApiResponse<MyPostsData>

    @POST("report/comment")
    suspend fun reportComment(
        @Header("Authorization") authorization: String,
        @Body request: ReportCommentRequest
    ): ApiResponse<ReportCommentResponse>
}
