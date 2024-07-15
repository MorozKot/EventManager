package com.example.eventmanager.domain

data class EventItem(

    var id: Int = UNDEFINED_ID,
    val name: String,
    val description: String,
    val date: String,
    val address: String,
    val weather: String
) {

    companion object {

        const val UNDEFINED_ID = 0
    }
}
