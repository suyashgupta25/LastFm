package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ArtistMatches(
        @Json(name = "artist")
        val artists: List<Artist>?
) {}
