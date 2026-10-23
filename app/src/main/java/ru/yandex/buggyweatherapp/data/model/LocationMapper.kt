package ru.yandex.buggyweatherapp.data.model

import com.google.gson.JsonObject
import ru.yandex.buggyweatherapp.domain.model.Location

object LocationMapper {

    fun parseWeatherApiJson(json: JsonObject): Location {
        val coord = json.getAsJsonObject("coord")
        val lat = coord.get("lat").asDouble
        val lon = coord.get("lon").asDouble
        val name = json.get("name").asString

        return Location(lat, lon, name)
    }

    fun parseFromLocation(location: android.location.Location) =
        Location(latitude = location.latitude, longitude = location.longitude)

}