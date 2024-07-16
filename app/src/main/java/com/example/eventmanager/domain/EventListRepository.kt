package com.example.eventmanager.domain

import androidx.lifecycle.LiveData
import com.example.eventmanager.data.network.model.TemperatureData

interface EventListRepository {

    suspend fun addEventItem(eventItem: EventItem)

    suspend fun deleteEventItem(eventItem: EventItem)

    suspend fun editEventItem(eventItem: EventItem)

    suspend fun getEventItem(eventItemId: Int): EventItem

    fun getEventList(): LiveData<List<EventItem>>

    suspend fun getWeather(city: String, startDate: String, endDate: String): List<TemperatureData>
}
