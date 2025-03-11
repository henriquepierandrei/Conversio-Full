package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.tech.pierandrei.Conversio.api.v1.dtos.clientDto.ClientResponseDto;
import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.LogResponseDto;
import com.tech.pierandrei.Conversio.api.v1.services.email.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/log")
@CrossOrigin
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/get/all")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getAllLogs(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<LogResponseDto> response = logService.getAll(pageable);
        if (response.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/get/smtp-account")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getLogByAccount(@RequestParam(name="smtpUsername") String username, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<LogResponseDto> response = logService.getLogBySmtpAccount(username, pageable);
        if (response.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(response);
    }

}
