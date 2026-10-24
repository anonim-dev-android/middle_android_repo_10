package ru.yandex.buggyweatherapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yandex.buggyweatherapp.data.model.FavModel

@Dao
interface FavDao {

    /**
     * @return `true` if the record exists and is favorite, `false` if the record exists and not is favorite, otherwise - `null`
     */
    @Query("SELECT isFavorite FROM favourites_cities WHERE cityName = :cityName LIMIT 1")
    fun isCityFavorite(cityName: String): Boolean?

    @Query("UPDATE favourites_cities SET isFavorite = :isFavorite WHERE cityName = :cityName")
    fun setCityFavorite(cityName: String, isFavorite: Boolean): Int

    /**
     * Insert new city to table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteCity(favRecord: FavModel)

}