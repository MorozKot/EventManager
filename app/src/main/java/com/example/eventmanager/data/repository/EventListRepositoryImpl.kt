package com.example.eventmanager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.eventmanager.data.database.EventListDao
import com.example.eventmanager.data.mapper.EventListMapper
import com.example.eventmanager.data.network.ApiService
import com.example.eventmanager.data.network.model.TemperatureData
import com.example.eventmanager.domain.EventItem
import com.example.eventmanager.domain.EventListRepository
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(
    private val eventListDao: EventListDao,
    private val mapper: EventListMapper,
    private val apiService: ApiService
) : EventListRepository {

    override suspend fun addEventItem(eventItem: EventItem) {
        eventListDao.addEventItem(mapper.mapEntityToDbModel(eventItem))
    }

    override suspend fun deleteEventItem(eventItem: EventItem) {
        eventListDao.deleteEventItem(eventItem.id)
    }

    override suspend fun editEventItem(eventItem: EventItem) {
        eventListDao.addEventItem(mapper.mapEntityToDbModel(eventItem))
    }

    override suspend fun getEventItem(eventItemId: Int): EventItem {
        val dbModel = eventListDao.getEventItem(eventItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getEventList(): LiveData<List<EventItem>> {
        return eventListDao.getEventList().map { dbModelList ->
            mapper.mapListDbModelToListEntity(dbModelList)
        }
    }

    override suspend fun getWeather(
        city: String,
        startDate: String,
        endDate: String
    ): List<TemperatureData> {
        return try {
            val response = apiService.getWeather(city, startDate, endDate)
            Log.d("WeatherRepository", "API Response: $response")
            response.data?.map { TemperatureData(temp = it.temp) } ?: emptyList()
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather data", e)
            emptyList()
        }
    }
}
