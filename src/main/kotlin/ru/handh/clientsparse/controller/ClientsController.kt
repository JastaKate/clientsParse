package ru.handh.clientsparse.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.handh.clientsparse.dto.ClientAddressDTO
import ru.handh.clientsparse.entity.ClientsEntity
import ru.handh.clientsparse.model.Clients
import ru.handh.clientsparse.service.ClientService

@RestController
@RequestMapping("/api/v1/clients")
class ClientsController(
    private val clientService: ClientService,
) {

    @GetMapping("/parsed")
    fun getClients(): List<Clients> {
        return clientService.getClientsFromXml()
    }

    @GetMapping
    fun getClientsByStreet(@RequestParam street: String): List<ClientsEntity> {
        return clientService.getClientsByStreet(street)
    }

    @GetMapping("/by-flat-number")
    fun getClientsByFlatNumber(@RequestParam flatNumber: Int): List<ClientAddressDTO> {
        return clientService.getClientsWithFlatNumberGreaterThan(flatNumber)
    }
}