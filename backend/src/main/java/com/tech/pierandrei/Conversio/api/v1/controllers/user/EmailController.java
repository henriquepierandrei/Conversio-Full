package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailRequestDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.EmailResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.services.email.EmailService;
import com.tech.pierandrei.Conversio.core.utils.GetUserOrClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class EmailController {
    private static final Logger log = LoggerFactory.getLogger(EmailController.class);
    private final GetUserOrClientUtils getUserUtils;

    @Autowired
    private EmailService emailService;
    public EmailController(GetUserOrClientUtils getUserOrClientUtils) {
        this.getUserUtils = getUserOrClientUtils;
    }


    @PostMapping("/send/email")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequestDto emailRequest, @AuthenticationPrincipal Jwt jwt) {
        try {
            // Obtém o usuário do JWT
            User user = getUserUtils.getUserByJwt(jwt);

            // Aguarda a conclusão da tarefa assíncrona e obtém o EmailResponseDto
            CompletableFuture<EmailResponseDto> emailTask = emailService.sendEmailsAsync(emailRequest, user);
            EmailResponseDto emailResponse = emailTask.get();  // Aguarda a conclusão do envio e pega a resposta

            // Retorna o EmailResponseDto com os dados
            return ResponseEntity.ok(emailResponse);
        } catch (Exception e) {
            log.error("Error sending emailDto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emailDto");
        }
    }


    @PostMapping("/send/email/upload/csv")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> sendEmail(
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(emailService.processCsvEmailSend(file));
        } catch (Exception e) {
            log.error("Error sending emailDto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emailDto");
        }
    }
}
