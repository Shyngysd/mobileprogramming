package com.example.mobileprogramming.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private const val BASE_URL = "https://overpass-api.de/"

private val json = Json { ignoreUnknownKeys = true }

private val client = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

object PlacesApi {
    val retrofitService: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }
}