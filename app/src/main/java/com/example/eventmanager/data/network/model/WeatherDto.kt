package com.example.eventmanager.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("data")
    @Expose
    val data: List<TemperatureData>? = null
)