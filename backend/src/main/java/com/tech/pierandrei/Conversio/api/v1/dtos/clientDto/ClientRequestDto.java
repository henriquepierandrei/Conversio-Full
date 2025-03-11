package com.tech.pierandrei.Conversio.api.v1.dtos.clientDto;

public class ClientRequestDto {
    private String name;
    private String email;

    // Construtor, Getters e Setters
    public ClientRequestDto() {}

    public ClientRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
