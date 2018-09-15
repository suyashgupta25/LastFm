package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.appsfactory.lastfm.data.albums.AlbumSource
import de.appsfactory.lastfm.data.model.Album
import javax.inject.Inject

class MyAlbumsViewModel @Inject constructor(private val albumSource: AlbumSource) : ViewModel(), LifecycleObserver {

    var albums: LiveData<List<Album>> = albumSource.getTopAlbums()

    var hasAlbums = MutableLiveData<Boolean>()

}