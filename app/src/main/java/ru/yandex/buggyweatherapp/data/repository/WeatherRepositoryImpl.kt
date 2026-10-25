package ru.yandex.buggyweatherapp.data.repository

import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.yandex.buggyweatherapp.data.api.WeatherApiService
import ru.yandex.buggyweatherapp.data.model.WeatherMapper
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.data.model.WeatherData
import ru.yandex.buggyweatherapp.domain.model.Weather
import ru.yandex.buggyweatherapp.domain.repository.FavRepository
import ru.yandex.buggyweatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApiService,
    private val favRepository: FavRepository
): WeatherRepository {
    private val callbackScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var cachedWeatherData: WeatherData? = null
    
    
    override suspend fun getWeatherData(location: Location, callback: (Weather?, Exception?) -> Unit) = withContext(Dispatchers.IO) {
        val call = weatherApi.getCurrentWeather(location.latitude, location.longitude)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val weatherData = WeatherMapper.parseJson(response.body()!!)
                cachedWeatherData = weatherData
                val isFavouriteCity = favRepository.isCityFavorite(weatherData.cityName)
                callback(WeatherMapper.parseToUserWeather(weatherData, isFavouriteCity), null)
            } else {
                Log.e("WeatherRepository", "API Error: ${response.code()}")
                sendCacheOrError(location.name, Exception("API Error: ${response.code()}"), callback)
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather", e)
            sendCacheOrError(location.name, e, callback)
        }
    }

    override suspend fun getWeatherByCity(cityName: String, callback: (Weather?, Exception?) -> Unit) = withContext(Dispatchers.IO) {
        weatherApi.getWeatherByCity(cityName).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                callbackScope.launch {
                    if (response.isSuccessful && response.body() != null) {
                        try {
                            val weatherData = WeatherMapper.parseJson(response.body()!!)
                            cachedWeatherData = weatherData
                            val isFavouriteCity = favRepository.isCityFavorite(weatherData.cityName)
                            callback(WeatherMapper.parseToUserWeather(weatherData, isFavouriteCity), null)
                        } catch (e: Exception) {
                            Log.e("WeatherRepository", "Error fetching weather", e)
                            sendCacheOrError(cityName, e, callback)
                        }
                    } else {
                        Log.e("WeatherRepository", "Error fetching weather. API Error: ${response.code()}")
                        sendCacheOrError(cityName, Exception("Error fetching weather data. API Error: ${response.code()}"), callback)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null, Exception(t))
            }
        })
    }

    /**
     * If cached weather is from given city - return it.
     * Otherwise - return exception
     */
    private suspend fun sendCacheOrError(cityName: String?, e: Exception, callback: (Weather?, Exception?) -> Unit) {
        when {
            cityName == null -> callback(null, e)
            cachedWeatherData?.cityName == cityName -> {
                // cachedWeatherData is not null here because cityName is not nullable
                val isFavouriteCity = favRepository.isCityFavorite(cachedWeatherData!!.cityName)
                callback(WeatherMapper.parseToUserWeather(cachedWeatherData!!, isFavouriteCity), null)
            }
            else -> callback(null, e)
        }
    }

}