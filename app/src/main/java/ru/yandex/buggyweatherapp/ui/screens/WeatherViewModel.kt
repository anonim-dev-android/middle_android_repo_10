package ru.yandex.buggyweatherapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.model.Weather
import ru.yandex.buggyweatherapp.domain.repository.FavRepository
import ru.yandex.buggyweatherapp.domain.repository.LocationRepository
import ru.yandex.buggyweatherapp.domain.repository.WeatherRepository
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val favRepository: FavRepository
) : ViewModel() {


    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val weatherUiState = _weatherUiState.asStateFlow()

    private val _currentLocation = MutableStateFlow<Location?>(null)
    
    private var refreshTimer: Timer? = null
    private val refreshDelay = 60_000L

    private val trackingListener: (Location) -> Unit = { localLocation ->
        if (_currentLocation.value != localLocation) {
            handleLocation(localLocation)
        }
    }
    
    fun initialize() {
        Log.i("WeatherViewModel", "init")
        // subscribing to tracking
        locationRepository.subscribeToTracking(trackingListener)
        fetchCurrentLocationWeather()
        startWeatherAutoRefresh()
    }
    
    private fun fetchCurrentLocationWeather() {
        handleLoadingState()
        viewModelScope.launch {
            locationRepository.getCurrentLocation { location ->
                if (location != null) {
                    handleLocation(location)

                    val cityNameFromLocation = locationRepository.getCityNameFromLocation(location)
                    handleSuccessState(mapOf(ArgsKey.CITY to cityNameFromLocation))

                    getWeatherForLocation(location)
                } else {
                    handleErrorState("Unable to get current location")
                }
            }
        }
    }

    fun refreshWeatherForCurrentLocation() = _currentLocation.value?.let { getWeatherForLocation(it) }
    
    private fun getWeatherForLocation(location: Location) {
        handleLoadingState()
        viewModelScope.launch {
            weatherRepository.getWeatherData(location) { data, exception ->
                if (data != null) {
                    handleSuccessState(mapOf(ArgsKey.WEATHER to data))
                } else {
                    Log.e("WeatherViewModel", "getWeatherForLocation($location). Got exception: ${exception?.message} ${exception?.stackTraceToString()}")
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
        viewModelScope.launch {
            weatherRepository.getWeatherByCity(city) { data, exception ->
                if (data != null) {
                    handleSuccessState(
                        mapOf(
                            ArgsKey.CITY to data.data.cityName,
                            ArgsKey.WEATHER to data
                        )
                    )
                   handleLocation(Location(data.data.cityName))
                } else {
                    Log.e("WeatherViewModel", "searchWeatherByCity($city). Got exception: ${exception?.message} ${exception?.stackTraceToString()}")
                    handleErrorState(exception?.message ?: "Unknown error")
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _weatherUiState.value.data?.let { currData ->
                val newFavState = !currData.isFavorite
                favRepository.setCityFavState(currData.data.cityName, newFavState)
                handleSuccessState(mapOf(ArgsKey.WEATHER to currData.copy(isFavorite = newFavState)))
            }
        }
    }
    
    private fun startWeatherAutoRefresh() {
        if (refreshTimer == null) {
            refreshTimer = Timer()
        }

        refreshTimer?.schedule(object : TimerTask() {
            override fun run() {
                Log.d("WeatherViewModel", "Trying to refresh weather fot location ${_currentLocation.value}")
                _currentLocation.value?.let { location -> getWeatherForLocation(location) }
            }
        }, refreshDelay, refreshDelay)
    }

    /**
     * Starts app-global tracking geoposition with [LocationManager].
     *
     * @see ru.yandex.buggyweatherapp.utils.LocationTracker
     */
    fun startLocationTracking() = locationRepository.startLocationTracking()

    /**
     * Starts app-global tracking geoposition with [LocationManager].
     *
     * @see ru.yandex.buggyweatherapp.utils.LocationTracker
     */
    fun stopLocationTracking() = locationRepository.stopLocationTracking()

    override fun onCleared() {
        Log.i("WeatherViewModel", "onCleared")
        refreshTimer?.cancel()
        refreshTimer = null
        locationRepository.unsubscribeToTracking(trackingListener)

        super.onCleared()
    }

    //============================== UPDATING STATE BLOCK ==============================//

    private enum class ArgsKey { WEATHER, CITY }

    /**
     * Updated [_weatherUiState] with [WeatherUiState.data] or/and [WeatherUiState.cityName].
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
     * @see Weather
     */
    private fun handleSuccessState(args: Map<ArgsKey, Any?>) {
        Log.d("WeatherViewModel", "handleSuccessState with args: ${args.entries.map{ entry -> "${entry.key} - ${entry.value}" }.joinToString(", ")}")
        _weatherUiState.update { _weatherUiState.value.copy(
            // include stopping loading moves
            isLoading = false,

            // if we have in arguments value with the key -> save it to new copy, otherwise - get current value
            data = if (args.containsKey(ArgsKey.WEATHER)) args[ArgsKey.WEATHER] as Weather? else _weatherUiState.value.data,
            cityName = if (args.containsKey(ArgsKey.CITY)) args[ArgsKey.CITY] as String? else _weatherUiState.value.cityName,
        ) }
    }

    /**
     * Updated [_weatherUiState] with `isLoading = true` flag. Error message was reset.
     */
    private fun handleLoadingState() {
        Log.d("WeatherViewModel", "handleLoadingState")
        _weatherUiState.update { _weatherUiState.value.copy(isLoading = true, error = null) }
    }


    /**
     * Updated [_weatherUiState] with error message. The function includes `isLoading = false`!
     */
    private fun handleErrorState(message: String) {
        Log.d("WeatherViewModel", "handleErrorState message: $message")
        _weatherUiState.update { _weatherUiState.value.copy(isLoading = false, error = message) }
    }


    private fun handleLocation(location: Location?) {
        Log.d("WeatherViewModel", "handleLocation location: $location")
        _currentLocation.update { location }
    }

}
