package com.example.eventmanager.domain

data class EventItem(
    val name: String,
    val description: String,
    val date: String,
    val address: String,
    val weather: String,
    val visited: Boolean,
    var id: Int = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
