package ru.handh.clientsparse.service.impl

import java.io.FileNotFoundException
import java.io.IOException
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import ru.handh.clientsparse.dto.ClientAddressDTO
import ru.handh.clientsparse.entity.ClientsEntity
import ru.handh.clientsparse.model.Clients
import ru.handh.clientsparse.repository.ClientRepository
import ru.handh.clientsparse.service.ClientService
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val resourceLoader: ResourceLoader
): ClientService {

    override fun getClientsFromXml(): List<Clients> {
        val factory = XMLInputFactory.newInstance()
        var parser: XMLStreamReader? = null
        val clients = mutableListOf<Clients>()

        try {
            val resource = resourceLoader.getResource("classpath:parser/clients.xml")
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
                if (event == XMLStreamConstants.START_ELEMENT && parser.localName == "client") {
                    clients.add(
                        Clients(
                            parser.getAttributeValue(0).toInt(),
                            parser.getAttributeValue(1),
                            parser.getAttributeValue(2),
                            parser.getAttributeValue(3).toInt()
                        )
                    )
                }
            }
        } catch (e: XMLStreamException) {
            println(e.message)
        }

        return clients
    }

    override fun setClients(clients: List<ClientsEntity>) {
        clientRepository.saveAll(clients)
    }

    override fun getClientsByStreet(street: String): List<ClientsEntity> {
        return clientRepository.findByAddressStreet(street)
    }

    override fun getClientsWithFlatNumberGreaterThan(flatNumber: Int): List<ClientAddressDTO> {
        return clientRepository.findClientsWithFlatNumberGreaterThan(flatNumber)
    }
}