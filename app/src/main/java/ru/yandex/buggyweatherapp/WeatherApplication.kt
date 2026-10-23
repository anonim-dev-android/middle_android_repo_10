package ru.yandex.buggyweatherapp

import android.app.Application
import ru.yandex.buggyweatherapp.utils.LocationTracker

class WeatherApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()

        LocationTracker.getInstance(this)
    }
}