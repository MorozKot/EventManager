package com.example.eventmanager.domain

import javax.inject.Inject

class GetEventItemUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    suspend fun getEventItem(eventItemId: Int): EventItem {
        return eventListRepository.getEventItem(eventItemId)
    }
}
