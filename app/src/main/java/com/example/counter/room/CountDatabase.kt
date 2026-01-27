package com.example.counter.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Count::class], version = 1)
abstract class CountDatabase : RoomDatabase() {
    abstract fun countDao(): CountDao
}
