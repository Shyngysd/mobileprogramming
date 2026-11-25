package com.example.mobileprogramming.data.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Boolean>
    val sortMode: Flow<String>
    val userName: Flow<String>

    suspend fun saveDarkTheme(isDarkTheme: Boolean)
    suspend fun saveSortMode(sortMode: String)
    suspend fun saveUserName(name: String)
}
