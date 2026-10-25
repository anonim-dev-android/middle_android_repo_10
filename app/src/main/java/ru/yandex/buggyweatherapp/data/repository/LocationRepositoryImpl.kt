package ru.yandex.buggyweatherapp.data.repository

import android.location.Geocoder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import ru.yandex.buggyweatherapp.data.model.LocationMapper
import ru.yandex.buggyweatherapp.domain.model.Location
import ru.yandex.buggyweatherapp.domain.repository.LocationRepository
import ru.yandex.buggyweatherapp.utils.LocationTracker
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val locationTracker: LocationTracker
): LocationRepository {

    override fun startLocationTracking() {
        locationTracker.startTracking()
    }

    override fun subscribeToTracking(listener: (Location) -> Unit) {
        locationTracker.addListener(listener)
    }

    override fun unsubscribeToTracking(listener: (Location) -> Unit) {
        locationTracker.removeListener(listener)
    }

    override fun stopLocationTracking() {
        locationTracker.stopTracking()
    }

    override fun getCurrentLocation(callback: (Location?) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val userLocation = LocationMapper.parseToUserLocation(location)
                        callback(userLocation)
                    } else {
                        
                        requestLocationUpdates(callback)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("LocationRepository", "Error getting location", e)
                    callback(null)
                }
        } catch (e: SecurityException) {
            Log.e("LocationRepository", "Location permission not granted", e)
            callback(null)
        }
    }
    
    
    private fun requestLocationUpdates(callback: (Location?) -> Unit) {
        try {
            val locationRequest = LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .build()
            
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        val userLocation = LocationMapper.parseToUserLocation(location)
                        fusedLocationClient.removeLocationUpdates(this)
                        callback(userLocation)
                    }
                }
            }
            
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationRepository", "Location permission not granted", e)
            callback(null)
        }
    }
    
    
    override fun getCityNameFromLocation(location: Location): String? {
        try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            
            return if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                when {
                    address.locality != null -> address.locality
                    address.subAdminArea != null -> address.subAdminArea
                    else -> address.adminArea
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Error getting city name", e)
            return null
        }
    }
    
}