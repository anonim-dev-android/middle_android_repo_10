package ru.yandex.buggyweatherapp.data.model

import android.util.Log
import com.google.gson.JsonObject
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.model.Weather

object WeatherMapper {

    /**
     * Converts from WeatherApi json-object to [WeatherData]
     */
    fun parseJson(json: JsonObject): WeatherData {
        Log.d("WeatherMapper", "parseJson $json")
        val main = json.getAsJsonObject("main")
        val wind = json.getAsJsonObject("wind")
        val sys = json.getAsJsonObject("sys")
        val weather = json.getAsJsonArray("weather").get(0).asJsonObject
        val clouds = json.getAsJsonObject("clouds")
        val coord = json.getAsJsonObject("coord")

        return WeatherData(
            cityName = json.get("name").asString,
            country = sys.get("country")?.asString,
            temperature = main.get("temp").asDouble,
            feelsLike = main.get("feels_like").asDouble,
            minTemp = main.get("temp_min").asDouble,
            maxTemp = main.get("temp_max").asDouble,
            humidity = main.get("humidity").asInt,
            pressure = main.get("pressure").asInt,
            windSpeed = wind.get("speed").asDouble,
            windDirection = if (wind.has("deg")) wind.get("deg").asInt else 0,
            description = weather.get("description").asString,
            icon = weather.get("icon").asString,
            cloudiness = clouds.get("all").asInt,
            sunriseTime = sys.get("sunrise").asLong,
            sunsetTime = sys.get("sunset").asLong,
            timezone = json.get("timezone").asInt,
            timestamp = json.get("dt").asLong,
            rawApiData = json.toString(),
            location = Location(
                longitude = coord.get("lon").asDouble,
                latitude = coord.get("lat").asDouble,
                name = json.get("name").asString
            ),
            rain = if (json.has("rain") && json.getAsJsonObject("rain").has("1h"))
                json.getAsJsonObject("rain").get("1h").asDouble else null,
            snow = if (json.has("snow") && json.getAsJsonObject("snow").has("1h"))
                json.getAsJsonObject("snow").get("1h").asDouble else null
        )
    }

    /**
     * Converts from [WeatherData] to ui [ru.yandex.buggyweatherapp.domain.model.Weather]
     */
    fun parseToUserWeather(weatherData: WeatherData, isFavouriteCity: Boolean = false) = Weather(weatherData, isFavouriteCity)

}