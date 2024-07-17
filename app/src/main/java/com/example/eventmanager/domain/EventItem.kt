package com.example.eventmanager.domain

data class EventItem(
    val name: String,
    val description: String,
    val date: String,
    val city: String,
    val visited: Boolean,
    val missed: Boolean,
    val temperature: String,
    val image: String,
    var id: Int = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
