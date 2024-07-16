package com.example.eventmanager.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Config {
    val API_KEY = "1c8781245eed48c2a781919a801556a5"
}

object ApiFactory {

    private const val BASE_URL = "https://api.weatherbit.io/v2.0/history/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("key", Config.API_KEY)
                .build()
            val request = original.newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        addInterceptor(logging)
    }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(httpClient)
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}