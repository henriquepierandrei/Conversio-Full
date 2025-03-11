package com.tech.pierandrei.Conversio.api.v1.services;

import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.AuthResponse;
import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.LoginRequest;
import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.RegisterRequest;
import com.tech.pierandrei.Conversio.api.v1.entities.Roles;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.repositories.RolesRepository;
import com.tech.pierandrei.Conversio.api.v1.repositories.UserRepository;
import com.tech.pierandrei.Conversio.api.v1.security.JwtTokenProvider;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.InvalidLoginException;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.RolesNotFoundException;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.UserAlreadyExistsException;
import com.tech.pierandrei.Conversio.core.utils.EmailLoggerUtils;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private EmailLoggerUtils emailLoggerUtils;
    @Autowired
    private GetUserOrClientUtils getGetUserUtils;

    private final JwtDecoder jwtDecoder;
    private final GetUserOrClientUtils getUserUtils;

    public AuthService(JwtDecoder jwtDecoder, GetUserOrClientUtils getUserUtils) {
        this.jwtDecoder = jwtDecoder;
        this.getUserUtils = getUserUtils;
    }

    @Transactional
    public AuthResponse loginAuth(LoginRequest loginRequest) {
        validateEmail(loginRequest.email());  // Validação de emailDto
        try {

            String maskedEmail = emailLoggerUtils.maskEmail(loginRequest.email());
            log.info("Attempting login for user with emailDto: {}", maskedEmail);

            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new InvalidLoginException("User not found or invalid credentials"));

            if (!user.isApprovedLogin(loginRequest, bCryptPasswordEncoder)) {
                log.warn("Invalid login attempt for emailDto: {}", maskedEmail);
                throw new InvalidLoginException("Invalid credentials");
            }

            log.info("Login successful for user with emailDto: {}", maskedEmail);
            updateLastLogin(user);


            Set<String> roles = user.getRole().stream()
                    .map(Roles::getName)
                    .collect(Collectors.toSet());

            String accessToken = jwtTokenProvider.generateAccessToken(user.getUuid().toString(), roles, 900);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUuid().toString(), 2592000);

            return new AuthResponse(accessToken, refreshToken, 900, 2592000);

        } catch (Exception e) {
            log.error("Error during login attempt", e);
            throw new InvalidLoginException("Login failed");
        }
    }

    @Transactional
    public void registerAuth(RegisterRequest registerRequest) {
        validateEmail(registerRequest.email());  // Validação de emailDto
        validatePassword(registerRequest.password()); // Validação de senha

        try {
            String maskedEmail = emailLoggerUtils.maskEmail(registerRequest.email());
            log.info("Attempting to register user with emailDto: {}", maskedEmail);

            Roles role = rolesRepository.findByName(Roles.RoleValues.USER.name())
                    .orElseThrow(RolesNotFoundException::new);

            if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
                log.warn("Email already exists: {}", maskedEmail);
                throw new UserAlreadyExistsException("Email already exists!");
            }

            User newUser = createUser(registerRequest, role);
            userRepository.save(newUser);
            log.info("User registered successfully with emailDto: {}", maskedEmail);

        } catch (Exception e) {
            log.error("Error during registration attempt", e);
            throw new UserAlreadyExistsException("User registration failed");
        }
    }

    public AuthResponse refreshAccessToken(String refreshToken) {

        try {
            // Decodificar o refresh token
            Jwt decodedJwt = jwtDecoder.decode(refreshToken);

            log.info("Iniciando refresh token");
            // Extraindo informações do token
            String userId = decodedJwt.getSubject();
            Instant expiration = decodedJwt.getExpiresAt();

            log.info("Token decodificado com sucesso!");

            // Verifica se o token está expirado
            if (expiration.isBefore(Instant.now())) {
                throw new RuntimeException("Refresh token expirado");
            }

            // Geração dos novos tokens
            String newAccessToken = jwtTokenProvider.generateAccessToken(userId, getUserUtils.getRoleByUser(decodedJwt), 900);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId, 2592000);

            log.info("Novos tokens gerados: " + newAccessToken + " || " + newRefreshToken);

            return new AuthResponse(newAccessToken, newRefreshToken, 900, 2592000);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh token expirado", e);
            throw new RuntimeException("Refresh token expirado", e);
        } catch (JwtException e) {
            log.warn("Erro" + refreshToken, e);
            throw new RuntimeException("Invalid refresh token", e);
        }
    }


    // Helper para extrair o ID do usuário do refresh token
    private UUID getUserFromToken(String refreshToken) {
        Jwt decodedJwt = jwtDecoder.decode(refreshToken);
        return UUID.fromString(decodedJwt.getSubject());
    }

    private User createUser(RegisterRequest registerRequest, Roles role) {
        User user = new User();
        user.setCompanyName(registerRequest.companyName());
        user.setEmail(registerRequest.email());
        user.setHashPassword(bCryptPasswordEncoder.encode(registerRequest.password()));
        user.setRole(Set.of(role));
        user.setCreatedAt(Instant.now());
        return user;
    }

    private void updateLastLogin(User user) {
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);  // Save the updated login timestamp
    }

    private void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid emailDto format");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    public boolean validateToken(String token) {
        try {
            // Decodificar o token
            Jwt decodedJwt = jwtDecoder.decode(token);

            // Verificar se o token está expirado
            if (decodedJwt.getExpiresAt().isBefore(Instant.now())) {
                log.error("Token expired");
                return false;
            }

            // Verificar o campo 'lastLogin' presente no token
            String userId = decodedJwt.getSubject();
            Long tokenLastLogin = decodedJwt.getClaim("lastLogin");

            if (tokenLastLogin == null) {
                throw new JwtException("Claim 'lastLogin' is missing in the token.");
            }

            // Obter o usuário do banco de dados ou repositório
            User user = getUserUtils.getUserById(UUID.fromString(userId));
            long userLastLogin = user.getLastLoginAt().toEpochMilli();

            // Validar se o 'lastLogin' do token coincide com o 'lastLoginAt' do usuário
            return tokenLastLogin == userLastLogin;

        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid token", e);
            return false;
        }
    }
}

