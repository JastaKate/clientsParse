package ru.handh.clientsparse.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "clients")
data class ClientsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String = "",

    @Column(name = "personal_number", nullable = false, unique = true)
    val personalNumber: String = "",

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    @JsonBackReference
    val address: AddressEntity? = null,
)
