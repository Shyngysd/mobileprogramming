package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory

object DemoQueueServices {
    val services: List<QueueService> = listOf(
        QueueService(
            id = "city-clinic",
            name = "City Clinic",
            description = "Doctor appointments, tests and medical certificates.",
            location = "Central Street 12, Demo City",
            category = ServiceCategory.HEALTH,
            peopleInQueue = 6,
            averageServiceMinutes = 10,
            isOpen = true
        ),
        QueueService(
            id = "smile-dental",
            name = "Smile Dental Care",
            description = "Check-ups, cleaning and dental treatment.",
            location = "Green Avenue 5, Demo City",
            category = ServiceCategory.HEALTH,
            peopleInQueue = 2,
            averageServiceMinutes = 20,
            isOpen = true
        ),
        QueueService(
            id = "bank-branch",
            name = "Bank Branch",
            description = "Cards, deposits, loans and money transfers.",
            location = "Market Square 3, Demo City",
            category = ServiceCategory.BANKING,
            peopleInQueue = 4,
            averageServiceMinutes = 8,
            isOpen = true
        ),
        QueueService(
            id = "public-service-center",
            name = "Public Service Center",
            description = "ID cards, certificates and other official documents.",
            location = "Freedom Street 40, Demo City",
            category = ServiceCategory.PUBLIC_SERVICES,
            peopleInQueue = 9,
            averageServiceMinutes = 7,
            isOpen = true
        ),
        QueueService(
            id = "coffee-point",
            name = "Coffee Point",
            description = "Coffee, tea and fresh pastries to go.",
            location = "Park Lane 8, Demo City",
            category = ServiceCategory.FOOD,
            peopleInQueue = 3,
            averageServiceMinutes = 3,
            isOpen = true
        ),
        QueueService(
            id = "burger-house",
            name = "Burger House",
            description = "Burgers, fries and drinks.",
            location = "Food court, Riverside Mall, Demo City",
            category = ServiceCategory.FOOD,
            peopleInQueue = 0,
            averageServiceMinutes = 5,
            isOpen = false
        ),
        QueueService(
            id = "barber-studio",
            name = "Barber Studio",
            description = "Haircuts, beard trims and styling.",
            location = "Lake Road 21, Demo City",
            category = ServiceCategory.BEAUTY,
            peopleInQueue = 1,
            averageServiceMinutes = 30,
            isOpen = true
        ),
        QueueService(
            id = "post-office",
            name = "Post Office",
            description = "Send and pick up letters and parcels.",
            location = "Station Street 2, Demo City",
            category = ServiceCategory.POST,
            peopleInQueue = 5,
            averageServiceMinutes = 4,
            isOpen = false
        )
    )
}