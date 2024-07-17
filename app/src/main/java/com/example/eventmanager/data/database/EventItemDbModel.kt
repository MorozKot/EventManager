package com.example.eventmanager.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_items")
data class EventItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val city: String,
    val visited: Boolean,
    val missed: Boolean,
    val temperature: String,
    val image: String
)