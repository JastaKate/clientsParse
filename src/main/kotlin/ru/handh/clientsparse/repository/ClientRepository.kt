package ru.handh.clientsparse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.handh.clientsparse.dto.ClientAddressDTO
import ru.handh.clientsparse.entity.ClientsEntity

@Repository
interface ClientRepository: JpaRepository<ClientsEntity, Int> {

    @Query("SELECT c FROM ClientsEntity c JOIN c.address a WHERE a.street = :street")
    fun findByAddressStreet(street: String): List<ClientsEntity>

    @Query(
        "SELECT new ru.handh.clientsparse.dto.ClientAddressDTO(a.street, a.house, c.name, a.flatNumber) " +
                "FROM ClientsEntity c JOIN c.address a " +
                "WHERE c.address.flatNumber > :flatNumber"
    )
    fun findClientsWithFlatNumberGreaterThan(flatNumber: Int): List<ClientAddressDTO>
}