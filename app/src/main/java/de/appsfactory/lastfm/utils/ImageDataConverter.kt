package de.appsfactory.lastfm.utils

import android.arch.persistence.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY

class ImageDataConverter {

    @TypeConverter
    fun fromImageURLList(value: List<ImageURL>?): String {
        if (value == null) {
            return EMPTY
        }
        val moshi = Moshi.Builder().build()
        val listData = Types.newParameterizedType(List::class.java, ImageURL::class.java)
        val adapter = moshi.adapter<List<ImageURL>>(listData)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toImageURLList(value: String?): List<ImageURL> {
        if (value == null || value.equals(EMPTY)) {
            return emptyList()
        }
        val moshi = Moshi.Builder().build()
        val listData = Types.newParameterizedType(List::class.java, ImageURL::class.java)
        val adapter = moshi.adapter<List<ImageURL>>(listData)
        return adapter.fromJson(value)!!
    }
}