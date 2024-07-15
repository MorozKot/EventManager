package com.example.eventmanager.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetEventListUseCase @Inject constructor(
    private val eventListRepository: EventListRepository
) {

    fun getEventList(): LiveData<List<EventItem>> {
        return eventListRepository.getEventList()
    }
}
