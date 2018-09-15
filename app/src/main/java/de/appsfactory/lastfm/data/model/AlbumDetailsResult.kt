package de.appsfactory.lastfm.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class AlbumDetailsResult(
        val album: AlbumInfo?
)
