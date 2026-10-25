package ru.yandex.buggyweatherapp.domain.repository

import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.model.Weather

interface WeatherRepository {

    /**
     * Request for current weather in given [location].
     *
     * @param location domain location model
     * @param callback function to handle results (either success or error)
     */
    suspend fun getWeatherData(location: Location, callback: (Weather?, Exception?) -> Unit)

    /**
     * Request for current weather in given city.
     *
     * @param cityName given city name
     * @param callback function to handle results (either success or error)
     */
    suspend fun getWeatherByCity(cityName: String, callback: (Weather?, Exception?) -> Unit)

}