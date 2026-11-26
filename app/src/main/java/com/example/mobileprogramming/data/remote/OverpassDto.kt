package com.example.mobileprogramming.data.remote

import com.example.mobileprogramming.data.model.QueueService
import com.example.mobileprogramming.data.model.ServiceCategory
import java.util.Locale
import kotlinx.serialization.Serializable

@Serializable
data class OverpassResponse(
    val elements: List<OverpassElement> = emptyList()
)

@Serializable
data class OverpassElement(
    val type: String = "",
    val id: Long,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val tags: Map<String, String> = emptyMap()
)

fun OverpassElement.toQueueService(): QueueService? {
    val name = tags["name:en"] ?: tags["name"] ?: return null
    val category = categoryFromTags(tags) ?: return null

    val street = listOfNotNull(tags["addr:street"], tags["addr:housenumber"]).joinToString(" ")
    val location = listOf(street, tags["addr:city"].orEmpty())
        .filter { it.isNotBlank() }
        .joinToString(", ")
        .ifBlank { "%.4f, %.4f".format(Locale.US, lat, lon) }

    val description = tags["description"]
        ?: tags["opening_hours"]?.let { "Opening hours: $it" }
        ?: "Place from OpenStreetMap"

    val imageUrl = tags["image"]?.takeIf { it.startsWith("https://") }
        ?: "https://picsum.photos/seed/osm$id/600/300"

    return QueueService(
        id = "osm-$id",
        name = name,
        description = description,
        location = location,
        category = category,
        peopleInQueue = (id % 10).toInt(),
        averageServiceMinutes = averageMinutesFor(category),
        isOpen = tags["opening_hours"] != "off",
        imageUrl = imageUrl
    )
}

fun categoryFromTags(tags: Map<String, String>): ServiceCategory? {
    val amenity = tags["amenity"]
    val shop = tags["shop"]
    return when {
        amenity in listOf("clinic", "doctors", "dentist") -> ServiceCategory.HEALTH
        amenity == "bank" -> ServiceCategory.BANKING
        amenity == "townhall" || tags["office"] == "government" -> ServiceCategory.PUBLIC_SERVICES
        amenity in listOf("cafe", "fast_food") -> ServiceCategory.FOOD
        shop in listOf("hairdresser", "beauty") -> ServiceCategory.BEAUTY
        amenity == "post_office" -> ServiceCategory.POST
        else -> null
    }
}

fun averageMinutesFor(category: ServiceCategory): Int = when (category) {
    ServiceCategory.HEALTH -> 15
    ServiceCategory.BANKING -> 8
    ServiceCategory.PUBLIC_SERVICES -> 10
    ServiceCategory.FOOD -> 4
    ServiceCategory.BEAUTY -> 30
    ServiceCategory.POST -> 5
}