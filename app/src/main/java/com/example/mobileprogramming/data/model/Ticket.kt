package com.example.mobileprogramming.data.model
data class Ticket(
    val id: String,          //
    val serviceId: String,   //
    val serviceName: String, //
    val number: Int,         //
    val peopleAhead: Int,    //
    val createdAt: Long = System.currentTimeMillis() //
)