package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Attrs (
    @Json(name = "for")
    val forAttr: String?,
    val rank: Int?,
    val artist: String?,
    val page: Int?,
    val perPage:Int?,
    val totalPages:Int?,
    val total:Int?
)