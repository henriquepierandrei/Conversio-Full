package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientRequestDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.DefaultResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.services.email.ClientService;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@CrossOrigin
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final GetUserOrClientUtils getUserUtils;

    @Autowired
    private ClientService clientService;


    public ClientController(GetUserOrClientUtils getUserOrClientUtils) {
        this.getUserUtils = getUserOrClientUtils;
    }

    @PostMapping("/add/csv")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<DefaultResponseDto> uploadCsvFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) {
        try {

            // Processa o arquivo CSV e obtém a lista de usuários
            DefaultResponseDto responseDto = clientService.processCsv(file, jwt);

            if (responseDto.getStatus() == 400){
                return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
            }
            // Retorna a lista de usuários com status HTTP 200 (OK)
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            // Em caso de erro, retorna status HTTP 500 (Internal Server Error)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/get/all")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getAllClients(@AuthenticationPrincipal Jwt jwt, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ClientResponseDto> response = clientService.getClients(jwt, pageable);
        if (response.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/get/all/no-pageable")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getAllClientsNoPageable(@AuthenticationPrincipal Jwt jwt){
          var response = clientService.getClientsNoPageable(jwt);
          if (response.isEmpty()){
              return ResponseEntity.noContent().build();
          }
        return ResponseEntity.ok().body(response);
    }
}
