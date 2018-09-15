package de.appsfactory.lastfm.ui.home.topalbums

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.databinding.ObservableBoolean
import de.appsfactory.lastfm.data.NetworkState
import de.appsfactory.lastfm.data.albums.LastFmService
import de.appsfactory.lastfm.data.albums.TopAlbumsDataSource
import de.appsfactory.lastfm.data.albums.TopAlbumsDataSourceFactory
import de.appsfactory.lastfm.data.albums.local.AlbumDao
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.utils.AppConstants
import de.appsfactory.lastfm.utils.runOnIoThread
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class TopAlbumsViewModel @Inject constructor(private val webService: LastFmService) : ViewModel() , LifecycleObserver {

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
                    .setInitialLoadSizeHint(TopAlbumsViewModel.PAGE_SIZE)
                    .setPageSize(TopAlbumsViewModel.PAGE_SIZE)
                    .setPrefetchDistance(16)
                    .build()

            LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                    .setFetchExecutor(executor)
                    .build()
        }
    }

    fun getArtistName(position:Int) : String {
        return topAlbumsList.value!![position]!!.name;
    }

    fun insertAlbums() {
        runOnIoThread {
            //albumDao.insertAlbums(topAlbumsList.value!!.toList())
        }
    }

    companion object {
        private val PAGE_SIZE = 16
    }
}