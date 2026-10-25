package ru.yandex.buggyweatherapp.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder =
        Geocoder(context, Locale.getDefault())

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

}