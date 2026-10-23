package ru.yandex.buggyweatherapp.ui.screens

import ru.yandex.buggyweatherapp.domain.model.WeatherData

data class WeatherUiState (
    val weatherData: WeatherData? = null,
    val cityName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)