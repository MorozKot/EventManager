package com.example.eventmanager.domain

import com.example.eventmanager.data.network.model.TemperatureData
import javax.inject.Inject

class GetTemperatureUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    suspend fun getTemperatureData(city: String, startDate: String, endDate: String): List<TemperatureData> {
        return eventListRepository.getWeather(city, startDate, endDate)
    }
}
