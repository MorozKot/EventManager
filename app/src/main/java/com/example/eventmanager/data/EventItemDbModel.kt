package com.example.eventmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_items")
data class EventItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val address: String,
    val weather: String,
    val visited: Boolean
)