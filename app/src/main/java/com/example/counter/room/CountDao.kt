package com.example.counter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CountDao {
    @Query("SELECT * FROM entities WHERE name = :name")
    fun getCounts(name: String): Single<List<Count>>

    @Query("SELECT * FROM entities WHERE name LIKE '%' || :query || '%'")
    fun searchByName(query: String): Single<List<Count>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCount(count: Count): Completable
}
