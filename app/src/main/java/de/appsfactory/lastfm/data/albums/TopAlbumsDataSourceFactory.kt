package de.appsfactory.lastfm.data.albums

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.DataSource.Factory
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.webservice.LastFmService

class TopAlbumsDataSourceFactory(val query: String, private val lastFmService: LastFmService) : Factory<Int, Album>() {

    private val mutableLiveData: MutableLiveData<TopAlbumsDataSource> = MutableLiveData()
    val topAlbumsDataSourceLiveData: LiveData<TopAlbumsDataSource>
        get() = mutableLiveData

    override fun create(): DataSource<Int, Album> {
        val topAlbumsDataSource = TopAlbumsDataSource(query, lastFmService)
        mutableLiveData.postValue(topAlbumsDataSource)
        return topAlbumsDataSource
    }
}
