package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.LoginRequestDTO;
import com.yazilim.afet.dto.LoginResponseDTO;
import com.yazilim.afet.dto.RegisterRequestDTO;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.VerificationToken;
import com.yazilim.afet.mapper.PersonMapper;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.repository.VerificationTokenRepository;
import com.yazilim.afet.service.AuthService;
import com.yazilim.afet.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final ValidationService validationService;
    private final PersonMapper personMapper;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PersonRepository personRepository, ValidationService validationService, PersonMapper personMapper, VerificationTokenRepository tokenRepository, EmailService emailService, PasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.validationService = validationService;
        this.personMapper = personMapper;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void register(RegisterRequestDTO registerRequestDTO) {

        validationService.validateUniqueFields(registerRequestDTO);
        Person person = personMapper.toEntity(registerRequestDTO);
        personRepository.save(person);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPerson(person);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(person.getEmail(), token);

    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Person person = personRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-posta veya şifre hatalı"));

        if (!person.getIsActive()) {
            throw new IllegalStateException("Lütfen e-postanızı doğrulayın.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), person.getPassword())) {
            throw new IllegalArgumentException("E-posta veya şifre hatalı");
        }

        return new LoginResponseDTO(
                person.getId(),
                person.getName(),
                person.getEmail(),
                person.getRole().name()
        );
    }

    public String confirmToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return "Geçersiz doğrulama bağlantısı.";
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            return "Doğrulama bağlantısının süresi dolmuş.";
        }

        Person person = verificationToken.getPerson();
        person.setIsActive(true);
        personRepository.save(person);
        tokenRepository.delete(verificationToken);

        return "E-posta doğrulaması başarılı.";
    }



}
