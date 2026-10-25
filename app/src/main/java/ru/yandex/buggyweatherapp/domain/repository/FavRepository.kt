package ru.yandex.buggyweatherapp.domain.repository

interface FavRepository {

    /**
     * @return `true` if city is favorite, otherwise - `false`
     */
    suspend fun isCityFavorite(cityName: String, country: String?): Boolean

    /**
     * Add or update record from database.
     */
    suspend fun setCityFavState(cityName: String, country: String?, isFavorite: Boolean): Any

}