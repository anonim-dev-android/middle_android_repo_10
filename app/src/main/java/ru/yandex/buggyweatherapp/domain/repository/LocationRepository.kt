package ru.yandex.buggyweatherapp.domain.repository

import ru.yandex.buggyweatherapp.domain.model.Location

interface LocationRepository {

    /**
     * Get current real (= where we now) location from location client.
     * @param callback function to handle results (either success or error)
     */
    fun getCurrentLocation(callback: (Location?) -> Unit)

    /**
     * Get city name for given location.
     * @param location
     */
    fun getCityNameFromLocation(location: Location): String?

    /**
     * Start global location tracking. It "lives" during application lives.
     */
    fun startLocationTracking()
    /**
     * Subscribe to location changes.
     */
    fun subscribeToTracking(listener: (Location) -> Unit)
    /**
     * Removing subscription to location changes.
     */
    fun unsubscribeToTracking(listener: (Location) -> Unit)
    /**
     * Stop global (= living during application lives) location tracking.
     */
    fun stopLocationTracking()

}