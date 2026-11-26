package com.example.mobileprogramming.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val BASE_URL = "https://raw.githubusercontent.com/Shyngysd/mobileprogramming/main/"

private val json = Json { ignoreUnknownKeys = true }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

object PlacesApi {
    val retrofitService: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }
}