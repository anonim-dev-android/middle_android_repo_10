package ru.yandex.buggyweatherapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yandex.buggyweatherapp.data.database.FavDao
import ru.yandex.buggyweatherapp.data.model.FavModel
import kotlin.random.Random

class FavRepository(
    private val favDao: FavDao
) {

    /**
     * @return `true` if city is favorite, otherwise - `false`
     */
    suspend fun isCityFavorite(cityName: String) = withContext(Dispatchers.IO) {
        favDao.isCityFavorite(cityName) ?: false
    }

    /**
     * Add or update record from database.
     */
    suspend fun setCityFavState(cityName: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        when(favDao.isCityFavorite(cityName)) {
            null -> favDao.addFavoriteCity(FavModel(cityName = cityName, isFavorite = isFavorite))
            else -> favDao.setCityFavorite(cityName, isFavorite)
        }
    }

}