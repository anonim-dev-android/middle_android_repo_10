package ru.yandex.buggyweatherapp.domain.model

import ru.yandex.buggyweatherapp.data.model.WeatherData

data class Weather(
    val data: WeatherData,
    var isFavorite: Boolean = false,
    var isSelected: Boolean = false
)
