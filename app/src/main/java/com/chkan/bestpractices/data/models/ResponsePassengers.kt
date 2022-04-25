package com.chkan.bestpractices.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePassengers(
    @SerialName("data")
    val list: List<Data>,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("totalPassengers")
    val totalPassengers: Int
)

@Serializable
data class Data(
    @SerialName("airline")
    val airline: List<Airline>,
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("trips")
    val trips: Int,
    @SerialName("__v")
    val v: Int
)

@Serializable
data class Airline(
    @SerialName("country")
    val country: String,
    @SerialName("established")
    val established: String,
    @SerialName("head_quaters")
    val headQuaters: String,
    @SerialName("id")
    val id: Int,
    @SerialName("logo")
    val logo: String,
    @SerialName("name")
    val name: String,
    @SerialName("slogan")
    val slogan: String,
    @SerialName("website")
    val website: String
)