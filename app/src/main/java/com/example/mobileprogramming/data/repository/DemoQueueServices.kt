package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory

object DemoQueueServices {
    // Locations and queue details are fictional and used for the project demo.
    val services: List<QueueService> = listOf(
        QueueService(
            id = "student-service-center",
            name = "Student Service Center",
            description = "Help with student documents and administrative requests.",
            location = "Demo campus, Student Services area",
            category = ServiceCategory.STUDENT_SERVICES,
            peopleInQueue = 6,
            averageServiceMinutes = 10,
            isOpen = true
        ),
        QueueService(
            id = "it-help-desk",
            name = "IT Help Desk",
            description = "Support with campus accounts and technical issues.",
            location = "Demo campus, IT Support area",
            category = ServiceCategory.IT_SUPPORT,
            peopleInQueue = 3,
            averageServiceMinutes = 15,
            isOpen = true
        ),
        QueueService(
            id = "library",
            name = "Library",
            description = "Borrow and return books or get help finding resources.",
            location = "Demo campus, Library area",
            category = ServiceCategory.LIBRARY,
            peopleInQueue = 2,
            averageServiceMinutes = 5,
            isOpen = true
        ),
        QueueService(
            id = "canteen",
            name = "Canteen",
            description = "Pick up meals and snacks.",
            location = "Demo campus, Dining area",
            category = ServiceCategory.DINING,
            peopleInQueue = 0,
            averageServiceMinutes = 8,
            isOpen = false
        )
    )
}
