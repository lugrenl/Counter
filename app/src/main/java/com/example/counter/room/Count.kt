package com.example.counter.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = "entities",
    indices = [Index(value = ["name"], unique = true)] // Индекс для быстрого поиска по имени
)
data class Count(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "value") val value: Int
)