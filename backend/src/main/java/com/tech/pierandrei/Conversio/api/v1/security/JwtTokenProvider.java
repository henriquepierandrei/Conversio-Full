package com.tech.pierandrei.Conversio.api.v1.security;

import com.tech.pierandrei.Conversio.api.v1.entities.Roles;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final GetUserOrClientUtils getUserUtils;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, GetUserOrClientUtils getUserUtils) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.getUserUtils = getUserUtils;
    }

    public String generateAccessToken(String userId, Set<String> roles, int expirationTimeInSeconds) {
        Instant now = Instant.now();
        String scopes = roles.stream().collect(Collectors.joining(" "));

        var user = getUserUtils.getUserById(UUID.fromString(userId));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Conversio")
                .subject(userId)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationTimeInSeconds))
                .claim("scope", scopes)
                .claim("lastLogin", user.getLastLoginAt().toEpochMilli())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    // Gera o Refresh Token
    public String generateRefreshToken(String userId, int expirationTimeInSeconds) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("Conversio")
                .subject(userId)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationTimeInSeconds))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    public boolean validateToken(String token) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            String userId = decodedJwt.getSubject();
            long tokenLastLogin = decodedJwt.getClaim("lastLogin");

            var user = getUserUtils.getUserById(UUID.fromString(userId));
            long userLastLogin = user.getLastLoginAt().toEpochMilli();


            return tokenLastLogin == userLastLogin;
        } catch (JwtException e) {
            // Token inválido
            return false;
        }
    }


    public String refreshAccessToken(String refreshToken) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(refreshToken);
            String userId = decodedJwt.getSubject();

            // Verifique se o refresh token está expirado
            if (decodedJwt.getExpiresAt().isBefore(Instant.now())) {
                throw new JwtException("Refresh token expired");
            }

            // Aqui, podemos buscar o usuário por seu ID (UUID) e gerar um novo access token
            Set<String> roles = getUserUtils.getUserById(UUID.fromString(userId)).getRole().stream()
                    .map(Roles::getName)
                    .collect(Collectors.toSet());

            return generateAccessToken(userId, roles, 900);  // Gera um novo access token
        } catch (JwtException | IllegalArgumentException e) {
            // Caso o refresh token seja inválido ou não puder ser decodificado, lançamos um erro
            throw new JwtException("Invalid refresh token", e);
        }
    }



}