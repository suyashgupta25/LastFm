package de.appsfactory.lastfm.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class AlbumWiki(

        var published: String?,
        var summary: String?,
        var content: String?
) {
}