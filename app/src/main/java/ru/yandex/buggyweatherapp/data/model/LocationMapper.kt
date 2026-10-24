package ru.yandex.buggyweatherapp.data.model

import com.google.gson.JsonObject
import ru.yandex.buggyweatherapp.domain.model.Location

object LocationMapper {

    /**
     * Converts from WeatherApi json-object to user location
     *
     * @see Location
     */
    fun parseToLocation(json: JsonObject): Location {
        val coord = json.getAsJsonObject("coord")
        val lat = coord.get("lat").asDouble
        val lon = coord.get("lon").asDouble
        val name = json.get("name").asString

        return Location(lat, lon, name)
    }

    /**
     * Converts from [android.location.Location] to user location
     *
     * @see [android.location.Location] android geographic location
     * @see [Location] our model of user location
     */
    fun parseToUserLocation(location: android.location.Location) =
        Location(latitude = location.latitude, longitude = location.longitude)

}