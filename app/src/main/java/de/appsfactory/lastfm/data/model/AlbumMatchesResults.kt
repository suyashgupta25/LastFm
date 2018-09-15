package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumMatchesResults(

        @Json(name = "album")
        val albums: List<Album>?,

        @Json(name = "@attr")
        val attrs: Attrs?
)
