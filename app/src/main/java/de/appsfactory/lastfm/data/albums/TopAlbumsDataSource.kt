package de.appsfactory.lastfm.data.albums

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import de.appsfactory.lastfm.data.NetworkState
import de.appsfactory.lastfm.data.Status
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.TopAlbumResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private typealias TopAlbumsLoadedCallback = (List<Album>) -> Unit

class TopAlbumsDataSource internal constructor(private val queryParam: String, private val lastFMService: LastFmService) : PageKeyedDataSource<Int, Album>() {
    private val mNetworkState: MutableLiveData<NetworkState> = MutableLiveData()
    val networkState: LiveData<NetworkState>
        get() = mNetworkState

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, Album>) {
        loadArtists(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }

    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Album>) {
        //Implementation not needed. There is no data before initial page loaded.
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Album>) {
        val page = params.key
        loadArtists(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    private fun loadArtists(page: Int, pageSize: Int, callback: TopAlbumsLoadedCallback) {
        Log.d(TAG, "Loading Page $page page size $pageSize")
        mNetworkState.postValue(NetworkState.LOADING)
        if (!queryParam.isEmpty()) {
            getTopAlbums(page, pageSize, callback)
        } else {
            postNetworkError("Invalid Artist name")
        }
    }

    private fun getTopAlbums(page: Int, pageSize: Int, consumer: TopAlbumsLoadedCallback) {
        lastFMService.getTopAlbumsByArtist(
                queryParam,
                page,
                pageSize,
                LastFmService.API_KEY).enqueue(object : Callback<TopAlbumResults> {
            override fun onResponse(call: Call<TopAlbumResults>, response: Response<TopAlbumResults>) {
                if (response.isSuccessful && response.code() == 200) {
                    val topalbumsResponse = response.body()?.topalbums?.albums
                    val topalbums = ArrayList<Album>()
                    if (topalbumsResponse != null) topalbums.addAll(topalbumsResponse)
                    postLoadedData(topalbums, consumer)
                } else {
                    postNetworkError(response.message())
                }
            }

            override fun onFailure(call: Call<TopAlbumResults>, t: Throwable) {
                postNetworkError(t.message)
            }
        })
    }

    private fun postLoadedData(artists: List<Album>, callback: TopAlbumsLoadedCallback) {
        callback.invoke(artists)
        mNetworkState.postValue(NetworkState.LOADED)
    }

    private fun postNetworkError(errorMessage: String?) {
        Log.e(TAG + ": API CALL", errorMessage)
        mNetworkState.postValue(NetworkState(Status.FAILED, errorMessage ?: "Unknown error"))
    }

    companion object {
        private val TAG = TopAlbumsDataSource::class.java.getSimpleName()
    }

}
