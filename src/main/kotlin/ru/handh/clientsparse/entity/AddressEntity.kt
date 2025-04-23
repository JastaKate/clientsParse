package ru.handh.clientsparse.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "clients_address")
data class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "city", nullable = false)
    val city: String = "",

    @Column(name = "street", nullable = false)
    val street: String = "",

    @Column(name = "house", nullable = false)
    val house: String = "",

    @Column(name = "floor", nullable = false)
    val floor: Int = 0,

    @Column(name = "flat_number", nullable = false)
    val flatNumber: Int = 0,

    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val clients: List<ClientsEntity> = emptyList()
)