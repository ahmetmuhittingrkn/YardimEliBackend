package com.yazilim.afet.service;

import com.yazilim.afet.dto.LoginRequestDTO;
import com.yazilim.afet.dto.LoginResponseDTO;
import com.yazilim.afet.dto.RegisterRequestDTO;

public interface AuthService {
    void register(RegisterRequestDTO registerRequestDTO);

    String confirmToken(String token);

    LoginResponseDTO login(LoginRequestDTO dto);
}
