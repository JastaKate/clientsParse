package ru.handh.clientsparse.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.handh.clientsparse.entity.AddressEntity
import ru.handh.clientsparse.model.Address
import ru.handh.clientsparse.service.AddressService

@RestController
@RequestMapping("/api/v1/addresses")
class AddressesController(
    private val addressService: AddressService
) {

    @GetMapping("/parsed")
    fun getAddresses(): List<Address> {
        return addressService.getAddressesFromXml()
    }

    @GetMapping("/sorted")
    fun getSortedAddresses(): List<AddressEntity> {
        return addressService.getSortedAddresses()
    }
}