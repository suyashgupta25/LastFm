package de.appsfactory.lastfm.ui.home.topalbums

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import de.appsfactory.lastfm.data.albums.TopAlbumsDataSource
import de.appsfactory.lastfm.data.albums.TopAlbumsDataSourceFactory
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.webservice.LastFmService
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.utils.AppConstants
import de.appsfactory.lastfm.utils.AppConstants.Companion.PAGE_SIZE
import de.appsfactory.lastfm.utils.AppConstants.Companion.PPREFETCH_SIZE
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class TopAlbumsViewModel @Inject constructor(private val webService: LastFmService) : ViewModel() {

    private val executor: ExecutorService = Executors.newFixedThreadPool(5)

    val topAlbumsList: LiveData<PagedList<Album>>
    val queryLiveData: MutableLiveData<String> = MutableLiveData()
    val networkState: LiveData<NetworkState>
    private val dataSourceFactoryLiveData: LiveData<TopAlbumsDataSourceFactory>
    private val dataSourceLiveData: LiveData<TopAlbumsDataSource>

    init {
        queryLiveData.value = AppConstants.EMPTY

        dataSourceFactoryLiveData = Transformations.switchMap(queryLiveData) {
            val dataSourceFactoryLiveData = MutableLiveData<TopAlbumsDataSourceFactory>()
            dataSourceFactoryLiveData.setValue(TopAlbumsDataSourceFactory(it, webService))
            dataSourceFactoryLiveData
        }

        dataSourceLiveData = Transformations.switchMap(dataSourceFactoryLiveData)
        { it.topAlbumsDataSourceLiveData }

        networkState = Transformations.switchMap(dataSourceLiveData) { it.networkState }

        topAlbumsList = Transformations.switchMap(dataSourceFactoryLiveData) { dataSourceFactory ->
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
}