package de.appsfactory.lastfm.data.albums.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.utils.AppConstants.Companion.DATABASE_NAME
import de.appsfactory.lastfm.utils.ImageDataConverter

/**
 * The Room database for this app
 */
@Database(entities = [Album::class], version = 1, exportSchema = false)
@TypeConverters(ImageDataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Gets the singleton instance of Database.
         *
         * @param context The context.
         * @return The singleton instance of Database.
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
