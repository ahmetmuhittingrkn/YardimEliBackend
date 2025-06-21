package com.yazilim.afet.controller;

import com.yazilim.afet.dto.LoginRequestDTO;
import com.yazilim.afet.dto.LoginResponseDTO;
import com.yazilim.afet.dto.RegisterRequestDTO;
import com.yazilim.afet.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Kayıt başarılı! Lütfen e-posta adresinizi doğrulayın."));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmEmail(@RequestParam String token) {
        String result = authService.confirmToken(token);
        return ResponseEntity.ok(Map.of("message", result));
    }
}
