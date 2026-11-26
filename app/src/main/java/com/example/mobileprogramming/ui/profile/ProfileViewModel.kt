package com.example.mobileprogramming.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.UserProfile
import com.example.mobileprogramming.data.repository.TicketRepository
import com.example.mobileprogramming.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    ticketRepository: TicketRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _profile = MutableStateFlow(
        UserProfile(
            name = "Guest",
            phone = "+7 700 000 00 00",
            email = "guest@example.com"
        )
    )
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    val activeTicketsCount: StateFlow<Int> = ticketRepository.getAllTicketsStream()
        .map { tickets -> tickets.count { !it.isServed } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = 0
        )

    val isDarkTheme: StateFlow<Boolean> = preferencesRepository.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        viewModelScope.launch {
            val savedName = preferencesRepository.userName.first()
            _profile.update { current -> current.copy(name = savedName) }
        }
    }

    fun updateName(newName: String) {
        _profile.update { current -> current.copy(name = newName) }
        viewModelScope.launch {
            preferencesRepository.saveUserName(newName)
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.saveDarkTheme(enabled)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                ProfileViewModel(
                    AppContainer.ticketRepository,
                    AppContainer.userPreferencesRepository
                )
            }
        }
    }
}
