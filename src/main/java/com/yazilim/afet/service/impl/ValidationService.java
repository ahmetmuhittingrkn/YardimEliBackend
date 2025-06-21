package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.RegisterRequestDTO;
import com.yazilim.afet.exception.ConflictException;
import com.yazilim.afet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {

    private final PersonRepository personRepository;

    public ValidationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validateUniqueFields(RegisterRequestDTO registerRequestDTO) {
        List<String> errors = new ArrayList<>();

        if (personRepository.existsByEmail(registerRequestDTO.getEmail()))
            errors.add("Bu email zaten kullanılıyor.");
        if (personRepository.existsByPhoneNumber(registerRequestDTO.getPhoneNumber()))
            errors.add("Bu telefon numarası zaten kullanılıyor.");
        if (personRepository.existsByTc(registerRequestDTO.getTc()))
            errors.add("Bu TC zaten kullanılıyor.");

        if (!errors.isEmpty())
            throw new ConflictException(errors);
    }
}
