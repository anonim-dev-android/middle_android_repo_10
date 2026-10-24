package ru.yandex.buggyweatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.yandex.buggyweatherapp.data.model.FavModel


@Database(entities = [FavModel::class], version = 1, exportSchema = false)
abstract class FavDatabase: RoomDatabase() {

    abstract fun favDao(): FavDao

}