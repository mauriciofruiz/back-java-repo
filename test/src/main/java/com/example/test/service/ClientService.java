package com.example.test.service;

import com.example.test.dto.PersonClientDto;
import com.example.test.dto.PersonClientDtoResponse;
import com.example.test.model.Client;
import com.example.test.model.Person;
import com.example.test.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio para gestionar clientes.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PersonService personService;

    /**
     * Crea un nuevo cliente y guarda la información de la persona asociada.
     *
     * @param personClientDto Datos del cliente y la persona.
     * @return Mono<Void> indicando la finalización de la operación.
     */
    public Mono<Void> create(PersonClientDto personClientDto) {
        return personService.savePerson(convertToPerson(personClientDto))
                .flatMap(savedPerson -> saveClient(convertToClient(savedPerson, personClientDto)))
                .then();
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id Identificador del cliente.
     * @param personClientDto Datos actualizados del cliente y la persona.
     * @return Mono<Void> indicando la finalización de la operación.
     */
    public Mono<Void> update(Integer id, PersonClientDto personClientDto) {
        return personService.getPersonById(id)
                .flatMap(person -> updatePerson(person, personClientDto))
                .flatMap(person -> clientRepository.findClientByPersonClientId(id)
                        .flatMap(client -> updateClient(client, personClientDto)))
                .then();
    }

    /**
     * Guarda un cliente en el repositorio.
     *
     * @param client Cliente a guardar.
     * @return Mono<Client> con el cliente guardado.
     */
    public Mono<Client> saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id Identificador del cliente.
     * @return Mono<PersonClientDtoResponse> con los datos del cliente y la persona.
     */
    public Mono<PersonClientDtoResponse> getClientById(Integer id) {
        return clientRepository.findById(id)
                .flatMap(client -> personService.getPersonById(client.getPersonClientId())
                        .map(person -> convertToDtoResponse(client, person)));
    }

    /**
     * Elimina un cliente por su identificador.
     *
     * @param id Identificador del cliente.
     * @return Mono<Void> indicando la finalización de la operación.
     */
    public Mono<Void> deleteClientById(Integer id) {
        return clientRepository.findById(id)
                .flatMap(client -> personService.getPersonById(client.getPersonClientId())
                        .flatMap(person -> clientRepository.deleteById(id)
                                .then(personService.deletePersonById(person.getPersonId()))));
    }

    /**
     * Obtiene todos los clientes.
     *
     * @return Flux<PersonClientDtoResponse> con la lista de clientes y sus datos asociados.
     */
    public Flux<PersonClientDtoResponse> getAllClients() {
        return clientRepository.findAll()
                .flatMap(client -> personService.getPersonById(client.getPersonClientId())
                        .map(person -> convertToDtoResponse(client, person)));
    }

    /**
     * Actualiza la información de una persona con los datos proporcionados.
     *
     * @param person La persona a actualizar.
     * @param personClientDto Los datos actualizados de la persona.
     * @return Mono<Person> con la persona actualizada.
     */
    private Mono<Person> updatePerson(Person person, PersonClientDto personClientDto) {
        person.setPersonName(personClientDto.getPersonName());
        person.setPersonGender(personClientDto.getPersonGender());
        person.setPersonAge(personClientDto.getPersonAge());
        person.setPersonIdentification(personClientDto.getPersonIdentification());
        person.setPersonAddress(personClientDto.getPersonAddress());
        person.setPersonPhone(personClientDto.getPersonPhone());
        return personService.savePerson(person);
    }

    /**
     * Actualiza la información de un cliente con los datos proporcionados.
     *
     * @param client El cliente a actualizar.
     * @param personClientDto Los datos actualizados del cliente.
     * @return Mono<Client> con el cliente actualizado.
     */
    private Mono<Client> updateClient(Client client, PersonClientDto personClientDto) {
        client.setClientPassword(personClientDto.getClientPassword());
        client.setClientStatus(personClientDto.getClientStatus());
        return saveClient(client);
    }

    /**
     * Convierte un PersonClientDto a un objeto Person.
     *
     * @param personClientDto Los datos del cliente y la persona.
     * @return Person con los datos convertidos.
     */
    private Person convertToPerson(PersonClientDto personClientDto) {
        return new Person(
                null,
                personClientDto.getPersonName(),
                personClientDto.getPersonGender(),
                personClientDto.getPersonAge(),
                personClientDto.getPersonIdentification(),
                personClientDto.getPersonAddress(),
                personClientDto.getPersonPhone()
        );
    }

    /**
     * Convierte un Person y PersonClientDto a un objeto Client.
     *
     * @param savedPerson La persona guardada.
     * @param personClientDto Los datos del cliente y la persona.
     * @return Client con los datos convertidos.
     */
    private Client convertToClient(Person savedPerson, PersonClientDto personClientDto) {
        return new Client(
                null,
                savedPerson.getPersonId(),
                personClientDto.getClientPassword(),
                Boolean.TRUE
        );
    }

    /**
     * Convierte un Client y Person a un objeto PersonClientDtoResponse.
     *
     * @param client El cliente.
     * @param person La persona.
     * @return PersonClientDtoResponse con los datos del cliente y la persona.
     */
    private PersonClientDtoResponse convertToDtoResponse(Client client, Person person) {
        return new PersonClientDtoResponse(
                client.getClientId(),
                person.getPersonId(),
                person.getPersonName(),
                person.getPersonAddress(),
                person.getPersonPhone(),
                client.getClientPassword(),
                client.getClientStatus()
        );
    }
}