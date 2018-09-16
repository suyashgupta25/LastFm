package de.appsfactory.lastfm.data.albums.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import de.appsfactory.lastfm.data.model.Album


/**
 * The Data Access Object for the [Album] class.
 */
@Dao
interface AlbumDao {

    @Query("SELECT * FROM myalbums")
    fun getAlbums(): LiveData<List<Album>>

    @Query("SELECT * FROM myalbums WHERE name LIKE :name LIMIT 1")
    fun getAlbumByName(name: String): Album?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: Album)

    @Delete
    fun delete(album: Album)

}