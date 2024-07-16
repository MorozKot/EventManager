package com.example.eventmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmanager.domain.DeleteEventItemUseCase
import com.example.eventmanager.domain.EditEventItemUseCase
import com.example.eventmanager.domain.EventItem
import com.example.eventmanager.domain.GetEventListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getEventListUseCase: GetEventListUseCase,
    private val deleteEventItemUseCase: DeleteEventItemUseCase,
    private val editEventItemUseCase: EditEventItemUseCase,
) : ViewModel() {

    val eventList = getEventListUseCase.getEventList()

    fun deleteEventItem(eventItem: EventItem) {
        viewModelScope.launch {
            deleteEventItemUseCase.deleteEventItem(eventItem)
        }
    }

    fun changeVisitedState(eventItem: EventItem) {
        viewModelScope.launch {
            val newItem = eventItem.copy(visited = !eventItem.visited, missed = false)
            editEventItemUseCase.editEventItem(newItem)
        }
    }

    fun changeMissedState(eventItem: EventItem) {
        viewModelScope.launch {
            val newItem = eventItem.copy(missed = !eventItem.missed, visited = true)
            editEventItemUseCase.editEventItem(newItem)
        }
    }
}
