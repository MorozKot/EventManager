package com.example.eventmanager.data.mapper

import com.example.eventmanager.data.database.EventItemDbModel
import com.example.eventmanager.data.network.model.TemperatureData
import com.example.eventmanager.data.network.model.WeatherDto
import com.example.eventmanager.domain.EventItem
import javax.inject.Inject

class EventListMapper @Inject constructor() {

    fun mapEntityToDbModel(eventItem: EventItem) = EventItemDbModel(
        id = eventItem.id,
        name = eventItem.name,
        description = eventItem.description,
        date = eventItem.date,
        city = eventItem.city,
        visited = eventItem.visited,
        missed = eventItem.missed,
        temperature = eventItem.temperature,
        image = eventItem.image
    )

    fun mapDbModelToEntity(eventItemDbModel: EventItemDbModel) = EventItem(
        id = eventItemDbModel.id,
        name = eventItemDbModel.name,
        description = eventItemDbModel.description,
        date = eventItemDbModel.date,
        city = eventItemDbModel.city,
        visited = eventItemDbModel.visited,
        missed = eventItemDbModel.missed,
        temperature = eventItemDbModel.temperature,
        image = eventItemDbModel.image
    )

    fun mapListDbModelToListEntity(list: List<EventItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

    fun mapWeatherDtoToTemperatureData(weatherDto: WeatherDto): List<TemperatureData> {
        return weatherDto.data?.map {
            TemperatureData(temp = it.temp)
        } ?: emptyList()
    }
}
