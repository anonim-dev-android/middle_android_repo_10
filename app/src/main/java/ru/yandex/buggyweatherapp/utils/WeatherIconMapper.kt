package ru.yandex.buggyweatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherIconMapper {

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun getPngLink(iconCode: String): String = "https://openweathermap.org/img/wn/$iconCode@2x.png"
    
    fun getWeatherDescription(description: String, temperature: Double) =
        "${description.replaceFirstChar { it.uppercase() }}, ${temperature.toInt()}°C"
    
    fun getWeatherIconResource(iconCode: String): Int = 0
    fun getBackgroundColor(weatherId: Int, temperature: Double): Int = 0
}