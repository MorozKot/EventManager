package com.example.eventmanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventListDao {

    @Query("SELECT * FROM event_items ORDER BY date ASC") //TODO подумать о сортировке по дате
    fun getEventList(): LiveData<List<EventItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEventItem(eventItemDbModel: EventItemDbModel)

    @Query("DELETE FROM event_items WHERE id=:eventItemId")
    suspend fun deleteEventItem(eventItemId: Int)

    @Query("SELECT * FROM event_items WHERE id=:eventItemId LIMIT 1")
    suspend fun getEventItem(eventItemId: Int): EventItemDbModel
}
