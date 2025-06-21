package com.yazilim.afet.controller;

import com.yazilim.afet.dto.PersonResponseDTO;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.mapper.PersonMapper;
import com.yazilim.afet.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable Long id) {
        Person person = personService.getById(id);
        return ResponseEntity.ok(personMapper.toResponseDTO(person));
    }

}
