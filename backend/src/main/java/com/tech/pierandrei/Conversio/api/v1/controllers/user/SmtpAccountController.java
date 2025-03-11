package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto.SmtpRequestDto;
import com.tech.pierandrei.Conversio.api.v1.services.email.SmtpAccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/smtpaccount")
@CrossOrigin
public class SmtpAccountController {


    private static final Logger log = LoggerFactory.getLogger(SmtpAccountController.class);
    private final SmtpAccountService smtpAccountService;

    public SmtpAccountController(SmtpAccountService smtpAccountService) {
        this.smtpAccountService = smtpAccountService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> createSmtpAccount(@RequestBody @Valid SmtpRequestDto smtpRequestDto) {
        try {
            var smtpResponse = smtpAccountService.createSmtpAccount(smtpRequestDto);
            if (smtpResponse.getStatus() == 400) {
                return ResponseEntity.badRequest().body(smtpResponse);
            }
            return ResponseEntity.ok(smtpResponse);
        } catch(Exception e){
                log.error("Error sending emailDto: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emailDto");
        }
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getSmtpAccounts() {
        try {
            var smtpConfigs = smtpAccountService.smtpConfigResponseDto();
            if (smtpConfigs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(smtpConfigs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> deleteSmtpAccount(@RequestParam(name = "id") Long id){
        try {
            var smtpResponse = smtpAccountService.deleteSmtp(id);
            if (smtpResponse == null){
                return ResponseEntity.internalServerError().build();
            }
            if (smtpResponse.getStatus() == 400){
                return ResponseEntity.badRequest().body(smtpResponse);
            }
            return ResponseEntity.ok(smtpResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> updateSmtpAccount(@RequestParam(name = "id") Long id, @RequestBody SmtpRequestDto smtpRequestDto){
        try {
            var smtpResponse = smtpAccountService.updateSmtp(id, smtpRequestDto);
            if (smtpResponse == null){
                return ResponseEntity.internalServerError().build();
            }
            if (smtpResponse.getStatus() == 400){
                return ResponseEntity.badRequest().body(smtpResponse);
            }
            return ResponseEntity.ok(smtpResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
