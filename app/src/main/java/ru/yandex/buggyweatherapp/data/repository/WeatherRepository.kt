package ru.yandex.buggyweatherapp.data.repository

import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.yandex.buggyweatherapp.data.api.WeatherApiService
import ru.yandex.buggyweatherapp.data.model.LocationMapper
import ru.yandex.buggyweatherapp.data.model.WeatherMapper
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.model.WeatherData

class WeatherRepository(
    private val weatherApi: WeatherApiService
) {
    private var cachedWeatherData: WeatherData? = null
    
    
    fun getWeatherData(location: Location, callback: (WeatherData?, Exception?) -> Unit) {
        
        val call = weatherApi.getCurrentWeather(location.latitude, location.longitude)
        
        
        try {
            
            val response = call.execute()
            
            if (response.isSuccessful) {
                val weatherData = WeatherMapper.parseJson(response.body()!!, location)
                cachedWeatherData = weatherData
                callback(weatherData, null)
            } else {
                
                callback(null, Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            
            Log.e("WeatherRepository", "Error fetching weather", e)
            callback(null, e)
        }
    }
    
    fun getWeatherByCity(cityName: String, callback: (WeatherData?, Exception?) -> Unit) {
        weatherApi.getWeatherByCity(cityName).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val json = response.body()!!
                        val location = LocationMapper.parseWeatherApiJson(json)
                        val weatherData = WeatherMapper.parseJson(json, location)
                        callback(weatherData, null)
                    } catch (e: Exception) {

                        callback(null, e)
                    }
                } else {
                    callback(null, Exception("Error fetching weather data"))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null, Exception(t))
            }
        })
    }

}