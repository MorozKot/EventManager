package com.example.eventmanager.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.eventmanager.R
import com.example.eventmanager.databinding.ItemEventUpcomingBinding
import com.example.eventmanager.databinding.ItemEventVisitedBinding
import com.example.eventmanager.domain.EventItem

class EventListAdapter : ListAdapter<EventItem, EventItemViewHolder>(EventItemDiffCallback()) {

    var onEventItemLongClickListener: ((EventItem) -> Unit)? = null
    var onEventItemClickListener: ((EventItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_UPCOMING -> R.layout.item_event_upcoming
            VIEW_TYPE_VISITED -> R.layout.item_event_visited
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return EventItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: EventItemViewHolder, position: Int) {
        val eventItem = getItem(position)
        val binding = viewHolder.binding
        binding.root.setOnLongClickListener {
            onEventItemLongClickListener?.invoke(eventItem)
            true
        }
        binding.root.setOnClickListener {
            onEventItemClickListener?.invoke(eventItem)
        }
        when (binding) {
            is ItemEventVisitedBinding -> {
                binding.eventItem = eventItem
            }
            is ItemEventUpcomingBinding -> {
                binding.eventItem = eventItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.visited) {
            VIEW_TYPE_VISITED
        } else {
            VIEW_TYPE_UPCOMING
        }
    }

    companion object {

        const val VIEW_TYPE_VISITED = 100
        const val VIEW_TYPE_UPCOMING = 101

        const val MAX_POOL_SIZE = 30
    }
}
