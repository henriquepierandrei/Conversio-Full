package com.tech.pierandrei.Conversio.api.v1.controllers.user;

import com.tech.pierandrei.Conversio.api.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal Jwt jwt) throws Exception {
        var response = userService.getUserData(jwt);
        if (response.toString().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/company-name")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<?> updateCompanyName(@RequestParam(name = "companyName") String companyName, @AuthenticationPrincipal Jwt jwt) throws Exception {
        var response = userService.changeCompanyName(companyName, jwt);
        if (response.toString().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


}
