package de.appsfactory.lastfm.data.albums

import android.arch.lifecycle.LiveData
import de.appsfactory.lastfm.data.albums.local.AlbumDao
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.AlbumDetailsResult
import de.appsfactory.lastfm.data.model.AlbumInfo
import de.appsfactory.lastfm.data.webservice.LastFmService
import io.reactivex.Observable
import javax.inject.Inject

class AlbumDataSource @Inject constructor(private val albumDao: AlbumDao, private val lastFmService: LastFmService) : AlbumSource {

    override fun getTopAlbums(): LiveData<List<Album>> = albumDao.getAlbums()

    override fun isAlbumFavourite(albumName: String): Observable<Boolean> {
        return Observable.fromCallable {
            val albumByName = albumDao.getAlbumByName(albumName)
            albumByName?.let {return@fromCallable true}
            return@fromCallable false
        }
    }

    override fun addFavouriteAlbum(album: Album): Observable<Boolean> {
        return Observable.fromCallable {
            albumDao.insertAlbum(album)
            return@fromCallable true
        }
    }

    override fun removeFavouriteAlbum(album: Album): Observable<Boolean> {
        return Observable.fromCallable {
            albumDao.delete(album)
            return@fromCallable false
        }
    }

    override fun getAlbumInfo(albumName: String, artistName: String?): Observable<AlbumInfo> {
        return lastFmService.getAlbumInfo(albumName, artistName, LastFmService.API_KEY).flatMap { item: AlbumDetailsResult ->
            Observable.just(item).map { item: AlbumDetailsResult ->
                item.album!!
            }
        }
    }
}