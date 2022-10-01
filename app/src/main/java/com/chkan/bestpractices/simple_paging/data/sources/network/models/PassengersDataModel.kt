package com.chkan.bestpractices.simple_paging.data.sources.network.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PassengersDataModel(
    @SerialName("data")
    val list: List<Data>,
    @SerialName("limit")
    val limit: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("total")
    val total: Int
)

@Serializable
data class Data(
    @SerialName("firstName")
    val firstName: String,
    @SerialName("id")
    val id: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("picture")
    val picture: String
)
