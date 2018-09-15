package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Streamable (
        @Json(name = "#text")
        val text: Long,
        val fulltrack: Int
)