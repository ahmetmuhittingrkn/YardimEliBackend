package com.yazilim.afet.service.impl;

import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.VerificationToken;
import com.yazilim.afet.exception.BadRequestException;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.repository.VerificationTokenRepository;
import com.yazilim.afet.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final PersonRepository personRepository;

    @Override
    public VerificationToken createVerificationToken(Person person) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPerson(person);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        return tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public boolean isTokenExpired(VerificationToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    @Override
    public void verifyAndActivateUser(String token) {
        VerificationToken verificationToken = findByToken(token)
                .orElseThrow(() -> new BadRequestException("Geçersiz doğrulama bağlantısı."));

        if (isTokenExpired(verificationToken)) {
            deleteToken(verificationToken);
            throw new BadRequestException("Doğrulama bağlantısının süresi dolmuş.");
        }

        Person person = verificationToken.getPerson();
        person.setIsActive(true);
        personRepository.save(person);
        deleteToken(verificationToken);
    }
} 