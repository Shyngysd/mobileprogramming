package com.example.mobileprogramming.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class Ticket(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceId: String,
    val serviceName: String,
    val number: Int,
    val peopleAhead: Int,
    val isServed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)