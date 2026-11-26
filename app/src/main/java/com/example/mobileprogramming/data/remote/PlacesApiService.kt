package com.example.mobileprogramming.data.remote

import retrofit2.http.GET

interface PlacesApiService {
    @GET("api/places.json")
    suspend fun getPlaces(): List<PlaceDto>
}