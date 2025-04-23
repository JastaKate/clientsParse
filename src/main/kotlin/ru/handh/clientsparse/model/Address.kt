package ru.handh.clientsparse.model

data class Address(
    val id: Int,
    val city: String,
    val street: String,
    val house: String,
    val floor: Int,
    val flatNumber: Int
)