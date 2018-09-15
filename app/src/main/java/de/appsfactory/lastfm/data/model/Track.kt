package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Track (
        val name: String?,
        val url: String?,
        val duration: Int,
        @Json(name = "@attr")
        val attrs: Attrs?,
        val streamable: Streamable?,
        val artist: Artist?
)