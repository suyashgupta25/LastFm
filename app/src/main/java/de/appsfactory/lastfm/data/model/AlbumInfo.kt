package de.appsfactory.lastfm.data.model

import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class AlbumInfo(

        @Json(name = "mbid")
        var id: String?,
        @PrimaryKey
        val name: String,
        val artist: String?,
        val url: String?,
        @Json(name = "image")
        val images: List<ImageURL>?,
        val listeners: Long?,
        val playcount: Long,
        val streamable: Int?,
        val tracks: AlbumTracks?,
        val tags: AlbumTags?,
        val wiki: AlbumWiki?
) {
}