package com.yazilim.afet.service.impl;

import com.yazilim.afet.entity.Person;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Override
    public Person getById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));
    }

}
