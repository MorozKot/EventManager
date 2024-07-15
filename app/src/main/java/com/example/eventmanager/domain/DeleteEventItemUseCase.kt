package com.example.eventmanager.domain

import javax.inject.Inject

class DeleteEventItemUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    suspend fun deleteEventItem(eventItem: EventItem) {
        eventListRepository.deleteEventItem(eventItem)
    }
}
