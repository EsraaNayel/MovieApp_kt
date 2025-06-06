package com.esraa.nayel.movieapp.feature.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.esraa.nayel.movieapp.Constants
import com.esraa.nayel.movieapp.feature.data.cache.MovieCacheDataSource
import com.esraa.nayel.movieapp.feature.data.models.MovieData

@Database(entities = [MovieData::class], exportSchema = false, version = Constants.DATABASE_VERSION)
@TypeConverters(Converter::class)
abstract class MoviesDatabase : RoomDatabase(){
    abstract fun dataSource(): MovieCacheDataSource

    companion object {
        private const val DATABASE_NAME = Constants.DATABASE_NAME

        @Volatile
        private var INSTANCE: MoviesDatabase? = null
        fun getInstance(context: Context): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: provideDatabase(context).also { INSTANCE = it }
            }
        }

        private fun provideDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(
                context,
                MoviesDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}