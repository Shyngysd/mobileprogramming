package com.example.mobileprogramming.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PlacesApiService {
    @Headers("User-Agent: QueueApp/1.0 (Android student project)")
    @GET("api/interpreter")
    suspend fun getPlaces(@Query("data") query: String): OverpassResponse
}