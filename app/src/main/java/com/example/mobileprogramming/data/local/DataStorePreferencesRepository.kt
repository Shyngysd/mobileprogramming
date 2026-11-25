package com.example.mobileprogramming.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mobileprogramming.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorePreferencesRepository(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    private companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val SORT_MODE = stringPreferencesKey("sort_mode")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    private val preferences: Flow<Preferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences()) else throw it
        }

    override val isDarkTheme: Flow<Boolean> = preferences.map { it[IS_DARK_THEME] ?: false }
    override val sortMode: Flow<String> = preferences.map { it[SORT_MODE] ?: "QUEUE_SIZE" }
    override val userName: Flow<String> = preferences.map { it[USER_NAME] ?: "Guest" }

    override suspend fun saveDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { it[IS_DARK_THEME] = isDarkTheme }
    }

    override suspend fun saveSortMode(sortMode: String) {
        dataStore.edit { it[SORT_MODE] = sortMode }
    }

    override suspend fun saveUserName(name: String) {
        dataStore.edit { it[USER_NAME] = name }
    }
}
