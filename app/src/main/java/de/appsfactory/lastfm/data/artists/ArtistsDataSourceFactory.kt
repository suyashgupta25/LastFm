package de.appsfactory.lastfm.data.artists

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.DataSource.Factory
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.webservice.LastFmService

class ArtistsDataSourceFactory(val query: String, private val lastFmService: LastFmService) : Factory<Int, Artist>() {

    private val mutableLiveData: MutableLiveData<ArtistsDataSource> = MutableLiveData()
    val artistsDataSourceLiveData: LiveData<ArtistsDataSource>
        get() = mutableLiveData

    override fun create(): DataSource<Int, Artist> {
        val artistsDataSource = ArtistsDataSource(query, lastFmService)
        mutableLiveData.postValue(artistsDataSource)
        return artistsDataSource
    }
}
