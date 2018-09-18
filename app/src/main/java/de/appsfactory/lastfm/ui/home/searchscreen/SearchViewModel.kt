package de.appsfactory.lastfm.ui.home.searchscreen

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import de.appsfactory.lastfm.data.artists.ArtistsDataSource
import de.appsfactory.lastfm.data.artists.ArtistsDataSourceFactory
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.webservice.LastFmService
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY
import de.appsfactory.lastfm.utils.AppConstants.Companion.PAGE_SIZE
import de.appsfactory.lastfm.utils.AppConstants.Companion.PPREFETCH_SIZE
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val webService: LastFmService) : ViewModel() {

    private val executor: ExecutorService = Executors.newFixedThreadPool(5)

    val artistsList: LiveData<PagedList<Artist>>
    val queryLiveData: MutableLiveData<String> = MutableLiveData()
    private val artistsDataSourceFactoryLiveData: LiveData<ArtistsDataSourceFactory>
    private val artistsDataSourceLiveData: LiveData<ArtistsDataSource>
    val networkState: LiveData<NetworkState>

    init {
        queryLiveData.value = EMPTY

        artistsDataSourceFactoryLiveData = Transformations.switchMap(queryLiveData) {
            val artistsDataSourceFactoryLiveData = MutableLiveData<ArtistsDataSourceFactory>()
            artistsDataSourceFactoryLiveData.setValue(ArtistsDataSourceFactory(it, webService))
            artistsDataSourceFactoryLiveData
        }

        artistsDataSourceLiveData = Transformations.switchMap(artistsDataSourceFactoryLiveData) { it.artistsDataSourceLiveData }

        networkState = Transformations.switchMap(artistsDataSourceLiveData) { it.networkState }

        artistsList = Transformations.switchMap(artistsDataSourceFactoryLiveData) { dataSourceFactory ->
            val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(PAGE_SIZE)
                    .setPageSize(PAGE_SIZE)
                    .setPrefetchDistance(PPREFETCH_SIZE)
                    .build()

            LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                    .setFetchExecutor(executor)
                    .build()
        }
    }

    fun onSearchClick(text: String) {
        queryLiveData.value = text
    }

    fun getArtistName(position:Int) : String {
        return artistsList.value!![position]!!.name;
    }

}