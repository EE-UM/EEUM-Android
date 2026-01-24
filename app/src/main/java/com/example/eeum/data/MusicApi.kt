package com.example.eeum.data

import retrofit2.http.GET
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
}
