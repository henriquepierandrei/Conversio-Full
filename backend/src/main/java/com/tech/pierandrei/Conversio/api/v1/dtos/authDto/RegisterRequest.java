package com.tech.pierandrei.Conversio.api.v1.dtos.authDto;


import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String companyName,

        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
