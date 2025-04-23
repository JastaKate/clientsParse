package ru.handh.clientsparse.service

import ru.handh.clientsparse.dto.ClientAddressDTO
import ru.handh.clientsparse.entity.ClientsEntity
import ru.handh.clientsparse.model.Clients

interface ClientService {
    fun getClientsFromXml(): List<Clients>
    fun setClients(clients: List<ClientsEntity>)
    fun getClientsByStreet(street: String): List<ClientsEntity>
    fun getClientsWithFlatNumberGreaterThan(flatNumber: Int): List<ClientAddressDTO>
}