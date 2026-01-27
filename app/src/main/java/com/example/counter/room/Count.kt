package com.example.counter.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "entities")
data class Count(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "value") val value: Int
)
