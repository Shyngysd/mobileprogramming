package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun getAllTicketsStream(): Flow<List<Ticket>>

    suspend fun insertTicket(ticket: Ticket): Int

    suspend fun updateTicket(ticket: Ticket)

    suspend fun deleteTicket(ticket: Ticket)

    suspend fun getNextNumber(): Int
}