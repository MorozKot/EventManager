package com.example.eventmanager.domain

import androidx.lifecycle.LiveData

interface EventListRepository {

    suspend fun addEventItem(eventItem: EventItem)

    suspend fun deleteEventItem(eventItem: EventItem)

    suspend fun editEventItem(eventItem: EventItem)

    suspend fun getEventItem(eventItemId: Int): EventItem

    fun getEventList(): LiveData<List<EventItem>>
}
