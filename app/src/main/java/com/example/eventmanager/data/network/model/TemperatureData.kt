package com.example.eventmanager.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TemperatureData(
    @SerializedName("temp")
    @Expose
    val temp: Double? = null
)