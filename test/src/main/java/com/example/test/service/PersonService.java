package com.example.test.service;

import com.example.test.dto.PersonClientDto;
import com.example.test.model.Person;
import com.example.test.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Mono<Person> savePerson(Person person) {
        return personRepository.save(person);
    }

    public Mono<Person> getPersonById(Integer id) {
        return personRepository.findById(id);
    }

    public Mono<Void> deletePersonById(Integer id) {
        return personRepository.deleteById(id);
    }
}
