package com.yazilim.afet.mapper;

import com.yazilim.afet.dto.RegisterRequestDTO;
import com.yazilim.afet.dto.PersonResponseDTO;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.Stk;
import com.yazilim.afet.enums.Role;
import com.yazilim.afet.exception.NotFoundException;
import com.yazilim.afet.repository.StkMemberRepository;
import com.yazilim.afet.repository.StkRepository;
import com.yazilim.afet.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    private final PasswordEncoder passwordEncoder;
    private final StkMemberRepository stkMemberRepository;
    private final StkRepository stkRepository;

    public PersonMapper(PasswordEncoder passwordEncoder, StkMemberRepository stkMemberRepository, StkRepository stkRepository) {
        this.passwordEncoder = passwordEncoder;
        this.stkMemberRepository = stkMemberRepository;
        this.stkRepository = stkRepository;
    }

    public Person toEntity(RegisterRequestDTO registerRequestDTO) {
        Person person = new Person();
        person.setName(registerRequestDTO.getName());
        person.setSurname(registerRequestDTO.getSurname());
        person.setEmail(registerRequestDTO.getEmail());
        person.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        person.setTc(registerRequestDTO.getTc());
        person.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        // ✅ Ek güvenlik kontrolü: stkMember false ama stkName gönderilmişse hata fırlat
        if (Boolean.FALSE.equals(registerRequestDTO.getStkMember()) && registerRequestDTO.getStkName() != null) {
            throw new BadRequestException("STK üyesi değilseniz 'stkName' alanı gönderilmemelidir.");
        }

        if (Boolean.TRUE.equals(registerRequestDTO.getStkMember())) {
            Stk stk = stkRepository.findByName(registerRequestDTO.getStkName())
                    .orElseThrow(() -> new NotFoundException("STK bulunamadı: " + registerRequestDTO.getStkName()));

            boolean validMember = stkMemberRepository.existsByTcAndStkId(registerRequestDTO.getTc(), stk.getId());
            if (!validMember) {
                throw new BadRequestException("STK üyesi olarak kayıtlı değilsiniz.");
            }

            person.setStk(stk);
            person.setRole(Role.STK_GONULLUSU);
        } else {
            person.setRole(Role.USER);
        }

        return person;
    }

    public PersonResponseDTO toResponseDTO(Person person) {
        return new PersonResponseDTO(person);
    }

}