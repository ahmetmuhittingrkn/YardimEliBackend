package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.LoginRequestDTO;
import com.yazilim.afet.dto.LoginResponseDTO;
import com.yazilim.afet.dto.RegisterRequestDTO;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.exception.BadRequestException;
import com.yazilim.afet.exception.UnauthorizedException;
import com.yazilim.afet.mapper.PersonMapper;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.service.AuthService;
import com.yazilim.afet.service.EmailService;
import com.yazilim.afet.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final ValidationService validationService;
    private final PersonMapper personMapper;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequestDTO registerRequestDTO) {
        validationService.validateUniqueFields(registerRequestDTO);
        
        Person person = personMapper.toEntity(registerRequestDTO);
        person = personRepository.save(person);

        // VerificationToken işlemlerini ayrı service'e taşıdık
        var verificationToken = verificationTokenService.createVerificationToken(person);
        emailService.sendVerificationEmail(person.getEmail(), verificationToken.getToken());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Person person = personRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("E-posta veya şifre hatalı"));

        if (!person.getIsActive()) {
            throw new BadRequestException("Lütfen e-postanızı doğrulayın.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), person.getPassword())) {
            throw new UnauthorizedException("E-posta veya şifre hatalı");
        }

        return new LoginResponseDTO(
                person.getId(),
                person.getName(),
                person.getEmail(),
                person.getRole().name()
        );
    }

    @Override
    public String confirmToken(String token) {
        try {
            verificationTokenService.verifyAndActivateUser(token);
            return "E-posta doğrulaması başarılı.";
        } catch (BadRequestException e) {
            return e.getMessage();
        }
    }
}
