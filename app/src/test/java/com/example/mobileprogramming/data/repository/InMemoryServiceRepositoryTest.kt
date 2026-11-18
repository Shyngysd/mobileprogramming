package com.example.mobileprogramming.data.repository

import com.example.mobileprogramming.data.model.QueueService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class InMemoryServiceRepositoryTest {
    @Test
    fun startsWithDemoServicesAndLooksUpById() {
        val repository = InMemoryServiceRepository()
        val demoService = DemoQueueServices.services.first()

        assertEquals(DemoQueueServices.services, repository.services.value)
        assertEquals(demoService, repository.getServiceById(demoService.id))
        assertNull(repository.getServiceById("missing-service"))
    }

    @Test
    fun refreshLoadsNewDataFromTheSource() = runBlocking {
        val originalService = DemoQueueServices.services.first()
        val source = mutableListOf(originalService)
        val repository = InMemoryServiceRepository { source }
        val updatedService = originalService.copy(peopleInQueue = originalService.peopleInQueue + 1)

        source[0] = updatedService
        assertEquals(listOf(originalService), repository.services.value)

        repository.refresh()

        assertEquals(listOf(updatedService), repository.services.value)
        assertEquals(updatedService, repository.getServiceById(updatedService.id))
    }

    @Test
    fun publishedListCannotBeModified() {
        val repository = InMemoryServiceRepository()

        assertThrows(UnsupportedOperationException::class.java) {
            (repository.services.value as MutableList<QueueService>).clear()
        }
        assertEquals(DemoQueueServices.services, repository.services.value)
    }
}
