package de.appsfactory.lastfm.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.util.DiffUtil
import com.squareup.moshi.Json
import de.appsfactory.lastfm.utils.ImageDataConverter
import se.ansman.kotshi.JsonSerializable
import se.ansman.kotshi.KotshiConstructor

@JsonSerializable
@TypeConverters(ImageDataConverter::class)
data class Artist @KotshiConstructor constructor(
        @ColumnInfo(name = "artist_mbid")
        val mbid: String?,
        @ColumnInfo(name = "artist_name")
        val name: String,
        @ColumnInfo(name = "artist_url")
        val url: String?,
        @Json(name = "image")
        @ColumnInfo(name = "artist_images")
        val images: List<ImageURL>?,
        @ColumnInfo(name = "artist_listeners")
        val listeners: Long?,
        @ColumnInfo(name = "artist_streamable")
        val streamable: Int?
):Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(ImageURL),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    fun getImageURL(size: ImageURL.Size): ImageURL {
        for (imageURL in images!!) {
            if (size == imageURL.size) {
                return imageURL
            }
        }
        throw RuntimeException("Size " + size.name + " not available for artist " + name)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mbid)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeTypedList(images)
        parcel.writeValue(listeners)
        parcel.writeValue(streamable)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }

        var DIFF_CALLBACK: DiffUtil.ItemCallback<Artist> = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return if (oldItem.mbid == null || oldItem.mbid == "")
                    oldItem.name === newItem.name
                else
                    oldItem.mbid === newItem.mbid
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}