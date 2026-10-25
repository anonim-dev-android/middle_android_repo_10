package ru.yandex.buggyweatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.yandex.buggyweatherapp.data.repository.FavRepositoryImpl
import ru.yandex.buggyweatherapp.data.repository.LocationRepositoryImpl
import ru.yandex.buggyweatherapp.data.repository.WeatherRepositoryImpl
import ru.yandex.buggyweatherapp.domain.repository.FavRepository
import ru.yandex.buggyweatherapp.domain.repository.LocationRepository
import ru.yandex.buggyweatherapp.domain.repository.WeatherRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLocationRepo(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindNetworkRepo(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun bindFavRepo(impl: FavRepositoryImpl): FavRepository

}