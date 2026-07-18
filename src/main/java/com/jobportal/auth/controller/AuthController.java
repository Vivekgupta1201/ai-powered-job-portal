package com.jobportal.auth.controller;


import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobportal.auth.dto.AuthResponse;
import com.jobportal.auth.dto.LoginRequest;
import com.jobportal.auth.dto.LoginResponse;
import com.jobportal.auth.dto.MeResponse;
import com.jobportal.auth.dto.RegisterRequest;
import com.jobportal.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/job-seeker")
    public ResponseEntity<AuthResponse> registerJobSeeker(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerJobSeeker(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<AuthResponse> registerRecruiter(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerRecruiter(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        String email = authentication.getName();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("");

        return ResponseEntity.ok(new MeResponse(email, role));
    }


}

