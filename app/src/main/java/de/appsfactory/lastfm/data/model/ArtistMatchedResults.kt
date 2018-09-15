package de.appsfactory.lastfm.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class ArtistMatchedResults (
        @Json(name = "opensearch:Query")
        val query: OpenSearchQuery?,

        @Json(name = "opensearch:totalResults")
        val totalResults: Long,

        @Json(name = "opensearch:startIndex")
        val startIndex: Long,

        @Json(name = "opensearch:itemsPerPage")
        val itemsPerPage: Long,

        @Json(name = "artistmatches")
        val artistMatches: ArtistMatches?,

        @Json(name = "@attr")
        val attrs: Attrs?
){

}