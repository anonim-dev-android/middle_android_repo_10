package ru.yandex.buggyweatherapp.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.buggyweatherapp.data.repository.LocationRepository
import ru.yandex.buggyweatherapp.data.repository.WeatherRepository
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.model.WeatherData
import ru.yandex.buggyweatherapp.utils.ImageLoader
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper
import java.util.Timer
import java.util.TimerTask

class WeatherViewModel(
    private val coroutineScope: CoroutineScope,
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {


    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val weatherUiState = _weatherUiState.asStateFlow()

    private val currentLocation = MutableLiveData<Location>()
    
    private var refreshTimer: Timer? = null
    
    
    fun initialize() {
        fetchCurrentLocationWeather()
        startAutoRefresh()
    }

    fun startLocationTracking() = locationRepository.startLocationTracking()
    
    
    fun fetchCurrentLocationWeather() {
        handleLoadingState()
        locationRepository.getCurrentLocation { location ->
            if (location != null) {
                currentLocation.value = location

                val cityNameFromLocation = locationRepository.getCityNameFromLocation(location)
                handleSuccessState(mapOf(ArgsKey.CITY to cityNameFromLocation))
                getWeatherForLocation(location)
            } else {
                handleErrorState("Unable to get current location")
            }
        }
    }
    
    fun getWeatherForLocation(location: Location) {
        handleLoadingState()
        weatherRepository.getWeatherData(location) { data, exception ->
            Handler(Looper.getMainLooper()).post {
                if (data != null) {
                    handleSuccessState(mapOf(ArgsKey.WEATHER to data))
                } else {
                    handleErrorState(exception?.message ?: "Unknown error")
                }
            }
        }
    }
    
    fun searchWeatherByCity(city: String) {
        if (city.isBlank()) {
            handleErrorState("City name cannot be empty")
            return
        }

        handleLoadingState()
        weatherRepository.getWeatherByCity(city) { data, exception ->
            if (data != null) {
                handleSuccessState(
                    mapOf(
                        ArgsKey.CITY to data.cityName,
                        ArgsKey.WEATHER to data
                    )
                )
                currentLocation.value = Location(data.cityName)
            } else {
                handleErrorState("Unknown error")
            }
        }
    }
    
    
    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°C"
    }
    
    
    fun loadWeatherIcon(iconCode: String) {
        coroutineScope.launch {
            val iconUrl = WeatherIconMapper.getPngLink(iconCode)
            ImageLoader.loadImage(iconUrl)
        }
    }
    
    
    private fun startAutoRefresh() {
        refreshTimer = Timer()
        refreshTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentLocation.value?.let { location ->
                    getWeatherForLocation(location)
                }
            }
        }, 60000, 60000)
    }
    
    
    fun toggleFavorite() {
        _weatherUiState.value.weatherData?.let { currData ->
            handleSuccessState(
                mapOf(ArgsKey.WEATHER to currData.copy(isFavorite = !currData.isFavorite))
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

    }

    //============================== UPDATING STATE BLOCK ==============================//

    private enum class ArgsKey { WEATHER, CITY }

    /**
     * Updated [_weatherUiState] with [WeatherUiState.weatherData] or/and [WeatherUiState.cityName].
     * For successful updating we need to provide map with keys and values:
     * * `ArgsKey.DATA` key for weatherData
     * * `ArgsKey.CITY` key for cityName
     *
     * Example:
     * ```kotlin
     * val weatherData = /* getting data of type WeatherData? */
     * val cityName = /* getting cityName of type String */
     * handleSuccessState(
     *     mapOf(ArgsKey.DATA to weatherData, ArgsKey.CITY to cityName)
     * )
     * ```
     *
     * @see ArgsKey
     * @see WeatherData
     */
    private fun handleSuccessState(args: Map<ArgsKey, Any?>) {
        _weatherUiState.update { _weatherUiState.value.copy(
            // include stopping loading moves
            isLoading = false,

            // if we have in arguments value with the key -> save it to new copy, otherwise - get current value
            weatherData = if (args.containsKey(ArgsKey.WEATHER)) args[ArgsKey.WEATHER] as WeatherData? else _weatherUiState.value.weatherData,
            cityName = if (args.containsKey(ArgsKey.CITY)) args[ArgsKey.CITY] as String? else _weatherUiState.value.cityName,
        ) }
    }

    /**
     * Updated [_weatherUiState] with `isLoading = true` flag. Error message was reset.
     */
    private fun handleLoadingState() =
        _weatherUiState.update { _weatherUiState.value.copy(isLoading = true, error = null) }

    /**
     * Updated [_weatherUiState] with error message. The function includes `isLoading = false`!
     */
    private fun handleErrorState(message: String) =
        _weatherUiState.update { _weatherUiState.value.copy(isLoading = false, error = message) }

}
