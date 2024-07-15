package com.example.eventmanager.data

import com.example.eventmanager.domain.EventItem
import javax.inject.Inject

class EventListMapper @Inject constructor() {

    fun mapEntityToDbModel(eventItem: EventItem) = EventItemDbModel(
        id = eventItem.id,
        name = eventItem.name,
        description = eventItem.description,
    date = eventItem.date,
    address = eventItem.address,
    weather = eventItem.weather
    )

    fun mapDbModelToEntity(eventItemDbModel: EventItemDbModel) = EventItem(
        id = eventItemDbModel.id,
        name = eventItemDbModel.name,
        description = eventItemDbModel.description,
        date = eventItemDbModel.date,
        address = eventItemDbModel.address,
        weather = eventItemDbModel.weather
    )

    fun mapListDbModelToListEntity(list: List<EventItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
