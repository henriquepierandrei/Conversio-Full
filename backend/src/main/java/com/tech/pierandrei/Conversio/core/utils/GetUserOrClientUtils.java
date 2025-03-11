package com.tech.pierandrei.Conversio.core.utils;


import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailRequestDto;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import com.tech.pierandrei.Conversio.api.v1.repositories.ClientRepository;
import com.tech.pierandrei.Conversio.api.v1.repositories.UserRepository;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetUserOrClientUtils {

    private static final Logger log = LoggerFactory.getLogger(GetUserOrClientUtils.class);
    @Autowired
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    @Autowired
    private ClientRepository clientRepository;

    public GetUserOrClientUtils(UserRepository userRepository, JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
    }

    public User getUserByJwt(Jwt jwt){
        String subClaim = jwt.getClaim("sub");
        UUID userId = UUID.fromString(subClaim);
        User user = getUserById(userId);
        return user;
    }

    public Set<String> getRoleByUser(Jwt jwt) {
        var user = getUserByJwt(jwt); // Supondo que esse método retorne um usuário com suas roles

        // Verifique o tipo de role. Caso seja uma coleção de roles:
        Set<String> roles = user.getRole().stream()  // Supondo que o usuário tenha um método getRoles() que retorna uma coleção
                .map(role -> role.getName()) // Convertendo a role para String
                .collect(Collectors.toSet()); // Coletando em um Set

        return roles;
    }


    public User getUserById(UUID uuid) {
        var user = userRepository.findById((uuid)).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user;
    }

    public boolean clientExists(EmailRequestDto emailRequestDto, User user){
        var clients = emailRequestDto.getClients();
        for (Clients client : clients){
            if (clientRepository.findByUuidAndUserOwner(client.getUuid(), user).isEmpty()){
                log.info("Client With ID: " + client.getUuid() + " not found!");
                return true;
            }
        }
        return false;
    }
}
