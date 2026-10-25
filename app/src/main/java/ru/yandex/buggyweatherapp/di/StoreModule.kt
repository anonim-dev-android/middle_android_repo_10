package ru.yandex.buggyweatherapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.yandex.buggyweatherapp.data.database.FavDao
import ru.yandex.buggyweatherapp.data.database.FavDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    @Singleton
    fun provideFavDatabase(@ApplicationContext context: Context): FavDatabase =
        Room.databaseBuilder(
            context,
            FavDatabase::class.java,
            "app_fav_database"
        ).build()

    @Provides
    @Singleton
    fun provideFavDao(database: FavDatabase): FavDao = database.favDao()
}