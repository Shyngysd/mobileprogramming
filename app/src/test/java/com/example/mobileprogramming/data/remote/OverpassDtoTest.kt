package com.example.mobileprogramming.data.remote

import com.example.mobileprogramming.data.model.ServiceCategory
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OverpassDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun overpassJsonIsParsedAndMappedToQueueService() {
        val text = """
            {
              "version": 0.6,
              "elements": [
                {
                  "type": "node",
                  "id": 123456,
                  "lat": 43.2383,
                  "lon": 76.9453,
                  "tags": {
                    "amenity": "bank",
                    "name": "Halyk Bank",
                    "addr:street": "Abay Avenue",
                    "addr:housenumber": "10",
                    "addr:city": "Almaty",
                    "opening_hours": "Mo-Fr 09:00-18:00"
                  }
                }
              ]
            }
        """.trimIndent()

        val response = json.decodeFromString<OverpassResponse>(text)
        val service = response.elements.first().toQueueService()!!

        assertEquals("osm-123456", service.id)
        assertEquals("Halyk Bank", service.name)
        assertEquals(ServiceCategory.BANKING, service.category)
        assertEquals("Abay Avenue 10, Almaty", service.location)
        assertEquals("Opening hours: Mo-Fr 09:00-18:00", service.description)
        assertEquals(6, service.peopleInQueue)
        assertTrue(service.isOpen)
        assertTrue(service.imageUrl.startsWith("https://"))
    }

    @Test
    fun placeWithoutNameIsSkipped() {
        val element = OverpassElement(id = 1, tags = mapOf("amenity" to "cafe"))

        assertNull(element.toQueueService())
    }

    @Test
    fun tagsAreMappedToCategories() {
        assertEquals(ServiceCategory.HEALTH, categoryFromTags(mapOf("amenity" to "dentist")))
        assertEquals(ServiceCategory.FOOD, categoryFromTags(mapOf("amenity" to "fast_food")))
        assertEquals(ServiceCategory.BEAUTY, categoryFromTags(mapOf("shop" to "hairdresser")))
        assertEquals(ServiceCategory.POST, categoryFromTags(mapOf("amenity" to "post_office")))
        assertEquals(ServiceCategory.PUBLIC_SERVICES, categoryFromTags(mapOf("office" to "government")))
        assertNull(categoryFromTags(mapOf("amenity" to "parking")))
    }
}