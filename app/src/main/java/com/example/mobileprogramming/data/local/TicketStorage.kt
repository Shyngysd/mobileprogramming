package com.example.mobileprogramming.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.mobileprogramming.data.model.Ticket
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TicketStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveTickets(tickets: List<Ticket>) {
        val array = JSONArray()
        tickets.forEach { ticket ->
            array.put(
                JSONObject()
                    .put("id", ticket.id)
                    .put("serviceId", ticket.serviceId)
                    .put("serviceName", ticket.serviceName)
                    .put("number", ticket.number)
                    .put("peopleAhead", ticket.peopleAhead)
                    .put("createdAt", ticket.createdAt)
            )
        }
        prefs.edit { putString(KEY_TICKETS, array.toString()) }
    }

    fun loadTickets(): List<Ticket> {
        val json = prefs.getString(KEY_TICKETS, null) ?: return emptyList()
        return try {
            val array = JSONArray(json)
            List(array.length()) { index ->
                val item = array.getJSONObject(index)
                Ticket(
                    id = item.getString("id"),
                    serviceId = item.getString("serviceId"),
                    serviceName = item.getString("serviceName"),
                    number = item.getInt("number"),
                    peopleAhead = item.getInt("peopleAhead"),
                    createdAt = item.getLong("createdAt")
                )
            }
        } catch (e: JSONException) {
            emptyList()
        }
    }

    private companion object {
        const val PREFS_NAME = "queue_prefs"
        const val KEY_TICKETS = "tickets"
    }
}