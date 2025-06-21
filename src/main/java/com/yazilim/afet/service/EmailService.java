package com.yazilim.afet.service;

public interface EmailService {

    void sendVerificationEmail(String to, String token);

}
