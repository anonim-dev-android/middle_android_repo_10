package ru.yandex.buggyweatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites_cities")
data class FavModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val isFavorite: Boolean
)
