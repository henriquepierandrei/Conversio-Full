package com.tech.pierandrei.Conversio.api.v1.controllers;

import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.AuthResponse;
import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.LoginRequest;
import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.RefreshTokenRequest;
import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.RegisterRequest;
import com.tech.pierandrei.Conversio.api.v1.services.AuthService;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.ConflictException;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user login.
     * Returns access and refresh tokens if login is successful.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            return authService.loginAuth(loginRequest);
        } catch (InvalidRequestException e) {
            // If login fails due to invalid credentials, return 400 Bad Request
            throw new InvalidRequestException("Invalid emailDto or password.");
        } catch (Exception e) {
            // Catch any other exceptions and return a 500 Internal Server Error
            throw new RuntimeException("Internal server error");
        }
    }

    /**
     * Endpoint for user registration.
     * Returns 201 Created if the registration is successful.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.registerAuth(registerRequest);
        } catch (ConflictException e) {
            // If emailDto already exists, return 409 Conflict
            throw new ConflictException("Email already exists.");
        } catch (InvalidRequestException e) {
            // If data is invalid, return 400 Bad Request
            throw new InvalidRequestException("Invalid data provided.");
        } catch (Exception e) {
            // Catch any other exceptions and return a 500 Internal Server Error
            throw new RuntimeException("Internal server error");
        }
    }


    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return authService.refreshAccessToken(refreshToken.getRefreshToken());
    }
}
