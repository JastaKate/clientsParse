package ru.handh.clientsparse.service.impl

import java.io.FileNotFoundException
import java.io.IOException
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.handh.clientsparse.entity.AddressEntity
import ru.handh.clientsparse.model.Address
import ru.handh.clientsparse.repository.AddressRepository
import ru.handh.clientsparse.service.AddressService
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

@Service
class AddressServiceImpl(
    private val addressRepository: AddressRepository,
    private val resourceLoader: ResourceLoader
): AddressService {

    override fun getAddressesFromXml(): List<Address> {
        val factory = XMLInputFactory.newInstance()
        var parser: XMLStreamReader? = null
        val addresses = mutableListOf<Address>()

        try {
            val resource = resourceLoader.getResource("classpath:parser/address.xml")
            val inputStream = resource.inputStream
            parser = factory.createXMLStreamReader(inputStream)
        } catch (e: FileNotFoundException) {
            println("Check file path")
        } catch (e: XMLStreamException) {
            println(e.message)
        } catch (e: IOException) {
            println(e.message)
        }

        try {
            while (parser?.hasNext() == true) {
                val event = parser.next()
                if (event == XMLStreamConstants.START_ELEMENT && parser.localName == "address") {
                    val id = parser.getAttributeValue(null, "id")?.toIntOrNull() ?: 0
                    val city = parser.getAttributeValue(null, "city") ?: ""
                    val street = parser.getAttributeValue(null, "street") ?: ""
                    val house = parser.getAttributeValue(null, "house") ?: ""
                    val floor = parser.getAttributeValue(null, "floor")?.toIntOrNull() ?: 0
                    val flatNumber = parser.getAttributeValue(null, "flatNumber")?.toIntOrNull() ?: 0

                    addresses.add(Address(id, city, street, house, floor, flatNumber))
                }
            }
        } catch (e: XMLStreamException) {
            println(e.message)
        }

        return addresses
    }

    override fun getSortedAddresses(): List<AddressEntity> {
        return addressRepository.findAll(Sort.by(Sort.Direction.DESC, "house"))
    }
}