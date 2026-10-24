package ru.yandex.buggyweatherapp.ui.screens

import ru.yandex.buggyweatherapp.domain.model.Weather

data class WeatherUiState (
    val data: Weather? = null,
    val cityName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)