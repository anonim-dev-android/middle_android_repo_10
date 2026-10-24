package ru.yandex.buggyweatherapp.di

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.yandex.buggyweatherapp.data.repository.LocationRepository
import ru.yandex.buggyweatherapp.data.repository.WeatherRepository
import ru.yandex.buggyweatherapp.ui.screens.WeatherViewModel

class DependencyContainer(context: Context) {

    private val locationContainer = LocationContainer(context)

    val weatherRepository = WeatherRepository(RetrofitContainer.weatherApi)
    val locationRepository = LocationRepository(
        locationContainer.fusedLocationClient,
        locationContainer.geocoder,
        locationContainer.tracker
    )

    val viewModel = WeatherViewModel(
        weatherRepository,
        locationRepository
    )
}