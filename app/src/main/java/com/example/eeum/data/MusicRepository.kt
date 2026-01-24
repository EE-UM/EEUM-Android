package com.example.eeum.data

data class MusicTrack(
    val title: String,
    val artist: String,
    val album: String? = null,
    val artworkUrl: String? = null,
    val previewUrl: String? = null
)

class MusicRepository(
    private val api: MusicApi = MusicApiProvider.api
) {
    suspend fun search(
        term: String,
        types: String,
        limit: Int
    ): List<MusicTrack> {
        if (term.isBlank()) return emptyList()
        val response = api.searchMusic(term.trim(), types, limit)
        return response.data.map { item ->
            MusicTrack(
                title = item.songName ?: "제목 미상",
                artist = item.artistName ?: "아티스트 미상",
                album = item.albumName,
                artworkUrl = item.artworkUrl,
                previewUrl = item.previewMusicUrl
            )
        }
    }
}
