package com.example.counter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountDao {
    @Query("SELECT * FROM entities WHERE name = :name")
    suspend fun getCounts(name: String): List<Count>

    @Query("SELECT * FROM entities WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<Count>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCount(count: Count)
}
