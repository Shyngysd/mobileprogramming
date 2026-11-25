package com.example.mobileprogramming.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobileprogramming.data.model.Ticket

@Database(entities = [Ticket::class], version = 1, exportSchema = false)
abstract class QueueDatabase : RoomDatabase() {

    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var Instance: QueueDatabase? = null

        fun getDatabase(context: Context): QueueDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, QueueDatabase::class.java, "queue_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}