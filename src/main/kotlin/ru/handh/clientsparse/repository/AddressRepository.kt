package ru.handh.clientsparse.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.handh.clientsparse.entity.AddressEntity

@Repository
interface AddressRepository: JpaRepository<AddressEntity, Int> {
}