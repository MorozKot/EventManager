package com.example.eventmanager.domain

import javax.inject.Inject

class EditEventItemUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    suspend fun editEventItem(eventItem: EventItem) {
        eventListRepository.editEventItem(eventItem)
    }
}
