package com.tech.pierandrei.Conversio.api.v1.dtos.authDto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        int accessTokenExpiresIn,
        int refreshTokenExpiresIn

) {
}
