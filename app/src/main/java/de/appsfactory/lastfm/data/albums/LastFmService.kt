package de.appsfactory.lastfm.data.albums

import de.appsfactory.lastfm.data.model.AlbumDetailsResult
import de.appsfactory.lastfm.data.model.TopAlbumResults
import de.appsfactory.lastfm.data.model.ArtistSearchResults
import de.appsfactory.lastfm.data.model.TopArtistsSearchResults
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {

    @GET("/2.0?method=artist.gettopalbums&format=json")
    fun getTopAlbumsByArtist(
            @Query("artist") artist: String,
            @Query("page") page: Int,
            @Query("limit") perPage: Int,
            @Query("api_key") api_key: String
    ): Call<TopAlbumResults>

    @GET("/2.0?method=artist.search&format=json")
    fun searchArtist(
            @Query("artist") artist: String,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
            @Query("api_key") api_key: String
    ): Call<ArtistSearchResults>

    @GET("/2.0?method=chart.gettopartists&format=json")
    fun chartTopArtists(
            @Query("page") page: Int,
            @Query("limit") limit: Int,
            @Query("api_key") api_key: String
    ): Call<TopArtistsSearchResults>

    @GET("/2.0?method=album.getinfo&format=json")
    fun getAlbumInfo(
            @Query("album") album: String,
            @Query("artist") artist: String?,
            @Query("api_key") api_key: String
    ): Observable<AlbumDetailsResult>

    companion object {
        val API_KEY = ""
    }

}
