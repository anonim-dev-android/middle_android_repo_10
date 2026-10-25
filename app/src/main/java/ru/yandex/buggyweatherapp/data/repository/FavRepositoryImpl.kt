package ru.yandex.buggyweatherapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yandex.buggyweatherapp.data.database.FavDao
import ru.yandex.buggyweatherapp.data.model.FavModel
import ru.yandex.buggyweatherapp.domain.repository.FavRepository
import javax.inject.Inject

class FavRepositoryImpl @Inject constructor(
    private val favDao: FavDao
): FavRepository {


    override suspend fun isCityFavorite(cityName: String, country: String?) = withContext(Dispatchers.IO) {
        favDao.isCityFavorite(cityName, country) ?: false
    }


    override suspend fun setCityFavState(cityName: String, country: String?, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        when(favDao.isCityFavorite(cityName, country)) {
            null -> favDao.addFavoriteCity(FavModel(cityName = cityName, countryCode = country, isFavorite = isFavorite))
            else -> favDao.setCityFavorite(cityName, isFavorite)
        }
    }

}