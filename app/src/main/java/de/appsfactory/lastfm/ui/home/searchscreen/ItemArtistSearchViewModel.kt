package de.appsfactory.lastfm.ui.home.searchscreen

import android.databinding.ObservableField
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY

class ItemArtistSearchViewModel(artist: Artist?) {

    val name = ObservableField<String>(EMPTY)
    val listenersNumber = ObservableField<String>(EMPTY)
    val url = ObservableField<String>(EMPTY)

    init {
        name.set(artist?.name)
        listenersNumber.set(artist?.listeners.toString())
        url.set(artist?.getImageURL(ImageURL.Size.MEDIUM)?.url)
    }
}