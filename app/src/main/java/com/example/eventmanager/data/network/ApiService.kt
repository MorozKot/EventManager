package com.example.eventmanager.data.network

import com.example.eventmanager.data.network.model.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("daily")
    suspend fun getWeather(
        @Query(QUERY_PARAM_CITY) city: String = "",
        @Query(QUERY_PARAM_START_DATE) startDate: String = "",
        @Query(QUERY_PARAM_END_DATE) endDate: String = ""
        ): WeatherDto

    companion object {
        private const val QUERY_PARAM_CITY = "city"
        private const val QUERY_PARAM_START_DATE = "start_date"
        private const val QUERY_PARAM_END_DATE = "end_date"
    }
}