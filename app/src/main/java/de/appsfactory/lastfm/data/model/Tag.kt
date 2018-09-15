package de.appsfactory.lastfm.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Tag(
        val name: String?,
        val url: String?
)