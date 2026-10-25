package ru.yandex.buggyweatherapp.domain.repository

interface FavRepository {

    /**
     * @return `true` if city is favorite, otherwise - `false`
     */
    suspend fun isCityFavorite(cityName: String): Boolean

    /**
     * Add or update record from database.
     */
    suspend fun setCityFavState(cityName: String, isFavorite: Boolean): Any

}