package com.example.eventmanager.data

import androidx.lifecycle.LiveData
import com.example.eventmanager.domain.EventListRepository
import javax.inject.Inject
import androidx.lifecycle.map
import com.example.eventmanager.domain.EventItem

class EventListRepositoryImpl @Inject constructor(
    private val eventListDao: EventListDao,
    private val mapper: EventListMapper
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
}
