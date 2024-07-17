package com.example.eventmanager.domain

sealed class TemperatureCategory(val imageUrl: String) {
    data object Cold : TemperatureCategory("https://openweathermap.org/img/wn/13d@2x.png")
    data object Moderate : TemperatureCategory("https://openweathermap.org/img/wn/02d@2x.png")
    data object Hot : TemperatureCategory("https://openweathermap.org/img/wn/01d@2x.png")
    data object Undefined : TemperatureCategory("https://openweathermap.org/img/wn/50d@2x.png")
}