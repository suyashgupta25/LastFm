package de.appsfactory.lastfm.data.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.util.DiffUtil
import com.squareup.moshi.Json
import de.appsfactory.lastfm.utils.ImageDataConverter
import se.ansman.kotshi.JsonSerializable
import se.ansman.kotshi.KotshiConstructor

@JsonSerializable
@Entity(tableName = "myalbums")
@TypeConverters(ImageDataConverter::class)
data class Album @KotshiConstructor constructor(
        @Json(name = "mbid")
        var id: String?,
        @PrimaryKey
        var name: String,
        @Embedded
        var artist: Artist?,
        var url: String?,
        @Json(name = "image")
        val images: List<ImageURL>?,
        var listeners: Long?,
        var playcount: Long,
        var streamable: Int?
): Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readParcelable(Artist::class.java.classLoader),
                parcel.readString(),
                parcel.createTypedArrayList(ImageURL),
                parcel.readValue(Long::class.java.classLoader) as? Long,
                parcel.readLong(),
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
                parcel.writeString(id)
                parcel.writeString(name)
                parcel.writeParcelable(artist, flags)
                parcel.writeString(url)
                parcel.writeTypedList(images)
                parcel.writeValue(listeners)
                parcel.writeLong(playcount)
                parcel.writeValue(streamable)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Album> {
                override fun createFromParcel(parcel: Parcel): Album {
                        return Album(parcel)
                }

                override fun newArray(size: Int): Array<Album?> {
                        return arrayOfNulls(size)
                }

                var DIFF_CALLBACK: DiffUtil.ItemCallback<Album> = object : DiffUtil.ItemCallback<Album>() {
                        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                                return if (oldItem.id == null || oldItem.id == "" || newItem.id == null || newItem.id == "")
                                        oldItem.name === newItem.name
                                else
                                        oldItem.id === newItem.id
                        }

                        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                                return oldItem == newItem
                        }
                }
        }

}
