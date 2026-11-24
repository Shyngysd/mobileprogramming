package com.example.mobileprogramming.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileprogramming.AppContainer
import com.example.mobileprogramming.data.model.UserProfile
import com.example.mobileprogramming.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    ticketRepository: TicketRepository
) : ViewModel() {

    private val _profile = MutableStateFlow(
        UserProfile(
            name = "Guest",
            phone = "+7 700 000 00 00",
            email = "guest@example.com"
        )
    )
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    val activeTicketsCount: StateFlow<Int> = ticketRepository.tickets
        .map { tickets -> tickets.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ticketRepository.tickets.value.size
        )

    fun updateName(newName: String) {
        _profile.update { current -> current.copy(name = newName) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ProfileViewModel(AppContainer.ticketRepository) }
        }
    }
}