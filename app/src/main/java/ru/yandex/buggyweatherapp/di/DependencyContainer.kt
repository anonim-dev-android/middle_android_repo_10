package ru.yandex.buggyweatherapp.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.yandex.buggyweatherapp.data.database.FavDatabase
import ru.yandex.buggyweatherapp.data.repository.FavRepository
import ru.yandex.buggyweatherapp.data.repository.LocationRepository
import ru.yandex.buggyweatherapp.data.repository.WeatherRepository
import ru.yandex.buggyweatherapp.ui.screens.WeatherViewModel

class DependencyContainer(context: Context) {

    private val favDatabase = Room.databaseBuilder(
        context,
        FavDatabase::class.java,
        "app_fav_database"
    ).build()
    private val favDao = favDatabase.favDao()
    val favRepository = FavRepository(favDao)

    private val locationContainer = LocationContainer(context)

    val weatherRepository = WeatherRepository(
        CoroutineScope(Dispatchers.IO + SupervisorJob()),
        RetrofitContainer.weatherApi,
        favRepository
        )
    val locationRepository = LocationRepository(
        locationContainer.fusedLocationClient,
        locationContainer.geocoder,
        locationContainer.tracker
    )

    val viewModel = WeatherViewModel(
        weatherRepository,
        locationRepository,
        favRepository
    )
}