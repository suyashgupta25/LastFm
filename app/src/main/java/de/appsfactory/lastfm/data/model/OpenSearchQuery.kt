package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class OpenSearchQuery(
        @Json(name = "#text")
        val text: String?,
        val role: String?,
        val searchTerms: String?,
        val startPage: Long
)
