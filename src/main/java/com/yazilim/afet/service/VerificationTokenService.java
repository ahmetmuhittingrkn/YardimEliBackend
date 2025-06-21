package com.yazilim.afet.service;

import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    
    VerificationToken createVerificationToken(Person person);
    
    Optional<VerificationToken> findByToken(String token);
    
    void deleteToken(VerificationToken token);
    
    boolean isTokenExpired(VerificationToken token);
    
    void verifyAndActivateUser(String token);
} 