package de.appsfactory.lastfm.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class TopAlbumResults(
        val topalbums: AlbumMatchesResults?
)
