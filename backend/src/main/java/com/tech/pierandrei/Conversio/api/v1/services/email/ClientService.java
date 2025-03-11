package com.tech.pierandrei.Conversio.api.v1.services.email;

import ch.qos.logback.core.net.server.Client;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientRequestDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.DefaultResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import com.tech.pierandrei.Conversio.api.v1.repositories.ClientRepository;
import com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated.ClientCreateException;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private GetUserOrClientUtils getUserOrClientUtils;

    private List<Clients> getClientsList(Jwt jwt){
        User user = getUserOrClientUtils.getUserByJwt(jwt);
        return clientRepository.findByUserOwner(user);
    }


    @Transactional
    public DefaultResponseDto processCsv(MultipartFile file, Jwt jwt) throws Exception {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Configura o OpenCSV para converter cada linha do CSV em um objeto Usuario
            CsvToBean<ClientRequestDto> csvToBean = new CsvToBeanBuilder<ClientRequestDto>(reader)
                    .withType(ClientRequestDto.class) // Define a classe de destino
                    .withIgnoreLeadingWhiteSpace(true) // Ignora espaços em branco no início
                    .build();

            // Converte o CSV para uma lista de objetos Usuario
            List<ClientRequestDto> clientRequestDtos = csvToBean.parse();

            // Verifica o valor da claim "sub" do token JWT
            String subClaim = jwt.getClaim("sub");

            int totalClients = getClientsList(jwt).size();

            List<Clients> clients = new ArrayList<>();


            // Obtém todos os e-mails já cadastrados no banco
            Set<String> emailsExistentes = clientRepository.findAll()
                    .stream()
                    .map(Clients::getEmail)
                    .collect(Collectors.toSet());

            for (ClientRequestDto clientRequest : clientRequestDtos) {
                if (clientRequest.getEmail() == null || clientRequest.getEmail().isBlank()) {
                    continue;
                }

                if (!emailsExistentes.contains(clientRequest.getEmail())) {
                    if (totalClients < 100) {
                        Clients client = new Clients();
                        client.setEmail(clientRequest.getEmail());
                        client.setName(clientRequest.getName() == null ? "Nome Desconhecido" : clientRequest.getName().trim());

                        UUID userId = UUID.fromString(subClaim);
                        User user = getUserOrClientUtils.getUserById(userId);
                        client.setUserOwner(user);

                        client.setCreatedAt(Instant.now());
                        client.setUpdatedAt(Instant.now());
                        clientRepository.save(client);

                        // Adiciona o e-mail à lista de existentes
                        emailsExistentes.add(clientRequest.getEmail());
                        totalClients++;
                    }
                }
            }

            // Retorna a lista de usuários
            return new DefaultResponseDto("Clientes Salvos com sucesso!", 200);
        }catch (Exception e){
            return new DefaultResponseDto("Ocorreu um erro ao salvar os clientes!", 400);

        }
    }


    public Page<ClientResponseDto> getClients(Jwt jwt, Pageable pageable){
        User user = getUserOrClientUtils.getUserByJwt(jwt);

        Page<Clients> clientsPage = clientRepository.findByUserOwner(user, pageable);

        return clientsPage.map(client -> {
            ClientResponseDto clientResponseDto = new ClientResponseDto();
            clientResponseDto.setCreatedAt(client.getCreatedAt());
            clientResponseDto.setUpdatedAt(client.getUpdatedAt());
            clientResponseDto.setEmail(client.getEmail());
            clientResponseDto.setName(client.getName());
            return clientResponseDto;
        });
    }

    public List<Clients> getClientsNoPageable(Jwt jwt){
        User user = getUserOrClientUtils.getUserByJwt(jwt);
        List<Clients> listClient = new ArrayList<>();

        for (Clients client : user.getClients()){
            Clients newClient = new Clients();
            newClient.setUuid(client.getUuid());
            newClient.setName(client.getName());
            newClient.setEmail(client.getEmail());
            listClient.add(newClient);
        }
        return listClient;
    }



}


