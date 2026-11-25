package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.local.TicketDao
import com.example.mobileprogramming.data.model.Ticket
import kotlinx.coroutines.flow.Flow

class OfflineTicketRepository(private val ticketDao: TicketDao) : TicketRepository {

    override fun getAllTicketsStream(): Flow<List<Ticket>> = ticketDao.getAllTickets()

    override suspend fun insertTicket(ticket: Ticket): Int = ticketDao.insert(ticket).toInt()

    override suspend fun updateTicket(ticket: Ticket) = ticketDao.update(ticket)

    override suspend fun deleteTicket(ticket: Ticket) = ticketDao.delete(ticket)

    override suspend fun getNextNumber(): Int = (ticketDao.getLastNumber() ?: 0) + 1
}