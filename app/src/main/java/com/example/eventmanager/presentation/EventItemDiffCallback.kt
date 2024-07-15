package com.example.eventmanager.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.eventmanager.domain.EventItem

class EventItemDiffCallback: DiffUtil.ItemCallback<EventItem>() {

    override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
        return oldItem == newItem
    }
}
