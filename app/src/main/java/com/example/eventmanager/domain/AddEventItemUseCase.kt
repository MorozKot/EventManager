package com.example.eventmanager.domain

import javax.inject.Inject

class AddEventItemUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    suspend fun addEventItem(eventItem: EventItem) {
        eventListRepository.addEventItem(eventItem)
    }
}
