package ru.yandex.buggyweatherapp.di

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yandex.buggyweatherapp.data.api.WeatherApiService

object RetrofitContainer {
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    val weatherApi: WeatherApiService = retrofit.create(WeatherApiService::class.java)

}