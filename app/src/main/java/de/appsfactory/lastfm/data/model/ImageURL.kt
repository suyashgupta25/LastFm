package de.appsfactory.lastfm.data.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable
import se.ansman.kotshi.KotshiConstructor

@JsonSerializable
data class ImageURL @KotshiConstructor constructor(
        @Json(name = "#text")
        var url: String,
        var size: Size?
): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), Size.valueOf(parcel.readString())) {
    }

    enum class Size  {
        @Json(name = "small")
        SMALL,
        @Json(name = "medium")
        MEDIUM,
        @Json(name = "large")
        LARGE,
        @Json(name = "extralarge")
        EXTRALARGE,
        @Json(name = "mega")
        MEGA,
        @Json(name = "")
        EMPTY
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        if (size == null)  parcel.writeString("") else parcel.writeString(size?.name)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageURL> {
        override fun createFromParcel(parcel: Parcel): ImageURL {
            return ImageURL(parcel)
        }

        override fun newArray(size: Int): Array<ImageURL?> {
            return arrayOfNulls(size)
        }
    }
}
