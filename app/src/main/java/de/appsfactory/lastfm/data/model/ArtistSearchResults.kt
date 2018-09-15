package de.appsfactory.lastfm.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ArtistSearchResults(
        val results: ArtistMatchedResults?
)
