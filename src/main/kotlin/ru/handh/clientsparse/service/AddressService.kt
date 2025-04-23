package ru.handh.clientsparse.service

import ru.handh.clientsparse.entity.AddressEntity
import ru.handh.clientsparse.model.Address

interface AddressService {
    fun getAddressesFromXml(): List<Address>
    fun getSortedAddresses(): List<AddressEntity>
}