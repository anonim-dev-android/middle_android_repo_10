package ru.yandex.buggyweatherapp

import android.app.Application
import android.content.Context
import ru.yandex.buggyweatherapp.di.DependencyContainer

class WeatherApplication : Application() {

    val depContainer = DependencyContainer(this)

}

fun Context.depContainer() = (this.applicationContext as WeatherApplication).depContainer
