package com.chkan.bestpractices.ui.models

import com.chkan.bestpractices.domain.models.PassengersDomainModel

/**
 * @author Dmytro Chkan on 14.06.2022.
 */
/**
 * Convert Network results to UI objects
 */
fun List<PassengersDomainModel>?.mapToPassengersUI(): List<PassengersUIModel> {
    return this?.map {
        PassengersUIModel(
            id = it.id,
            firstName = it.firstName,
            lastName = it.lastName,
            picture = it.picture
        )
    } ?: listOf()
}