package ru.yandex.buggyweatherapp.data.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.yandex.buggyweatherapp.BuildConfig

interface WeatherApiService {


    companion object {
        val BASE_URL = BuildConfig.WEATHER_BASE_URL
    }

    
    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Call<JsonObject>
    
    @GET("weather")
    fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Call<JsonObject>
    
    @GET("forecast")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): Call<JsonObject>
}