package de.appsfactory.lastfm.data.artists

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.model.ArtistSearchResults
import de.appsfactory.lastfm.data.model.TopArtistsSearchResults
import de.appsfactory.lastfm.data.webservice.LastFmService
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.data.webservice.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private typealias ArtistsLoadedCallback = (List<Artist>) -> Unit

class ArtistsDataSource internal constructor(private val query: String, private val lastFMService: LastFmService) : PageKeyedDataSource<Int, Artist>() {
    private val mNetworkState: MutableLiveData<NetworkState> = MutableLiveData()
    val networkState: LiveData<NetworkState>
        get() = mNetworkState

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, Artist>) {
        loadArtists(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }

    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Artist>) {
        //Implementation not needed. There is no data before initial page loaded.
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Artist>) {
        val page = params.key
        loadArtists(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    private fun loadArtists(page: Int, pageSize: Int, callback: ArtistsLoadedCallback) {
        Log.d(TAG, "Loading Page $page page size $pageSize")
        mNetworkState.postValue(NetworkState.LOADING)

        if (query.isEmpty()) {
            chartTopArtists(page, pageSize, callback)
        } else {
            searchArtistsByName(query, page, pageSize, callback)
        }
    }

    private fun searchArtistsByName(query: String, page: Int, pageSize: Int, callback: ArtistsLoadedCallback) {
        lastFMService.searchArtist(
                query,
                page,
                pageSize,
                LastFmService.API_KEY)
                .enqueue(object : Callback<ArtistSearchResults> {

            override fun onResponse(call: Call<ArtistSearchResults>, response: Response<ArtistSearchResults>) {
                if (response.isSuccessful && response.code() == 200) {
                    val artistsResponse = response.body()?.results?.artistMatches?.artists
                    val artists = ArrayList<Artist>()
                    if (artistsResponse != null) artists.addAll(artistsResponse)
                    postLoadedData(artists, callback)
                } else {
                    postNetworkError(response.message())
                }
            }

            override fun onFailure(call: Call<ArtistSearchResults>, t: Throwable) {
                postNetworkError(t.message)
            }
        })
    }

    private fun chartTopArtists(page: Int, pageSize: Int, consumer: ArtistsLoadedCallback) {
        lastFMService.chartTopArtists(
                page,
                pageSize,
                LastFmService.API_KEY).enqueue(object : Callback<TopArtistsSearchResults> {
            override fun onResponse(call: Call<TopArtistsSearchResults>, response: Response<TopArtistsSearchResults>) {
                if (response.isSuccessful && response.code() == 200) {
                    val artistsResponse = response.body()?.artists?.artists
                    val artists = ArrayList<Artist>()
                    if (artistsResponse != null) artists.addAll(artistsResponse)
                    postLoadedData(artists, consumer)
                } else {
                    postNetworkError(response.message())
                }
            }

            override fun onFailure(call: Call<TopArtistsSearchResults>, t: Throwable) {
                postNetworkError(t.message)
            }
        })
    }

    private fun postLoadedData(artists: List<Artist>, callback: ArtistsLoadedCallback) {
        callback.invoke(artists)
        mNetworkState.postValue(NetworkState.LOADED)
    }

    private fun postNetworkError(errorMessage: String?) {
        Log.e(TAG + ": API CALL", errorMessage)
        mNetworkState.postValue(NetworkState(Status.FAILED, errorMessage
                ?: "Unknown error"))
    }

    companion object {
        private val TAG = ArtistsDataSource::class.java.getSimpleName()
    }

}
