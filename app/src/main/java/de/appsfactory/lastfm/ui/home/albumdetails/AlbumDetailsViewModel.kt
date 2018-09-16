package de.appsfactory.lastfm.ui.home.albumdetails

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import de.appsfactory.lastfm.data.albums.AlbumSource
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.AlbumInfo
import de.appsfactory.lastfm.utils.defaultErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class AlbumDetailsViewModel @Inject constructor(private val albumSource: AlbumSource) : ViewModel(), LifecycleObserver {

    // Disposable
    private val disposable: CompositeDisposable = CompositeDisposable()

    val albumInfo = MutableLiveData<AlbumInfo>()
    val album = ObservableField<Album>()
    val isFav = ObservableBoolean(false)

    fun prepareData(albumParam: Album) {
        album.set(albumParam)
        getAlbumInfo(albumParam.name, albumParam.artist?.name)
        isAlbumFavourite(album.get()!!.name)
    }

    private fun getAlbumInfo(albumName: String, artistName: String?) {
        disposable.add(albumSource.getAlbumInfo(albumName, artistName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(defaultErrorHandler())
                .subscribe({
                    albumInfo.postValue(it)
                }, {
                    Log.e("Error:", it.message)
                }, {
                })
        )
    }

    private fun isAlbumFavourite(albumName: String) {
        disposable.add(albumSource.isAlbumFavourite(album.get()!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(defaultErrorHandler())
                .subscribe({
                    isFav.set(it)
                }, {
                    Log.e("Error:", it.message)
                }, {
                })
        )
    }

    fun favClicked() {
        if (isFav.get()) {
            disposable.add(albumSource.removeFavouriteAlbum(album.get()!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(defaultErrorHandler())
                    .subscribe({
                        isFav.set(it)
                    }, {
                        Log.e("Error:", it.message)
                    }, {
                        Log.e("favClicked:", "completed")
                    })
            )
        } else {
            disposable.add(albumSource.addFavouriteAlbum(album.get()!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(defaultErrorHandler())
                    .subscribe({
                        isFav.set(it)
                    }, {
                        Log.e("Error:", it.message)
                    }, {
                        Log.e("favClicked:", "completed")
                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}