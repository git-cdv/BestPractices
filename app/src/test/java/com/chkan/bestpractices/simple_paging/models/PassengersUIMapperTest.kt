package com.chkan.bestpractices.simple_paging.models

import com.chkan.bestpractices.simple_paging.domain.models.PassengersDomainModel
import org.junit.Assert.*

import org.junit.Test

class PassengersUIMapperTest {

    @Test
    fun mapper() {
        val mapper = PassengersUIMapper()
        val passDomainList = listOf(PassengersDomainModel(
            id = "123",
            firstName = "firstName",
            lastName = "lastName",
            picture = "url",
            total = 10
        ))

        val uiList = mapper.map(passDomainList)

        val expectedPassengersList = listOf(PassengersUIModel(
            id = "123",
            firstName = "firstName",
            lastName = "lastName",
            picture = "url"
        ))

        assertEquals(expectedPassengersList.first(), uiList.first())
    }
}