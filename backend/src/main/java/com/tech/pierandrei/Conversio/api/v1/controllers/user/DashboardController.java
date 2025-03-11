package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.tech.pierandrei.Conversio.api.v1.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin
public class DashboardController {
    @Autowired
    private EmailService emailService;


    @GetMapping("/get/datas")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getAllDatas(@AuthenticationPrincipal Jwt jwt){
        var response = emailService.getDashboardData(jwt);
        return ResponseEntity.ok(response);
    }

}
