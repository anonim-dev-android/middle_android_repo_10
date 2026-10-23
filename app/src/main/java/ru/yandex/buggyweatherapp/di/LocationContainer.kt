package ru.yandex.buggyweatherapp.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ru.yandex.buggyweatherapp.utils.LocationTracker
import java.util.Locale

class LocationContainer(context: Context) {

    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val geocoder = Geocoder(context, Locale.getDefault())
    val tracker = LocationTracker.getInstance(context)

}