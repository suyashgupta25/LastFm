package de.appsfactory.lastfm.data.albums.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import de.appsfactory.lastfm.data.model.Album
import io.reactivex.Observable


/**
 * The Data Access Object for the [Album] class.
 */
@Dao
interface AlbumDao {

    @Query("SELECT * FROM myalbums")
    fun getAlbums(): LiveData<List<Album>>

    @Query("SELECT * FROM myalbums WHERE name = :name")
    fun getAlbumByName(name: String): LiveData<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: Album)

    @Delete
    fun delete(album: Album)

}