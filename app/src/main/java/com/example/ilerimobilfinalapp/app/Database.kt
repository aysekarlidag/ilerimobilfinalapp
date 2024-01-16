package com.example.ilerimobilfinalapp.app

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Dao
interface AppDao {
    @Query("SELECT * FROM MovieEntity")
    fun getAll(): List<MovieEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<MovieEntity>)
}

@Database(entities = [MovieEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao(): AppDao
}