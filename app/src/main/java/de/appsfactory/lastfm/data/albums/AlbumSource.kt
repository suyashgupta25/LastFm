package de.appsfactory.lastfm.data.albums

import android.arch.lifecycle.LiveData
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.AlbumInfo
import io.reactivex.Observable

interface AlbumSource {

    fun getTopAlbums(): LiveData<List<Album>>

    fun isAlbumFavourite(albumName: String): Observable<Boolean>

    fun addFavouriteAlbum(album: Album): Observable<Boolean>

    fun removeFavouriteAlbum(album: Album): Observable<Boolean>

    fun getAlbumInfo(albumName: String, artistName: String?): Observable<AlbumInfo>

}