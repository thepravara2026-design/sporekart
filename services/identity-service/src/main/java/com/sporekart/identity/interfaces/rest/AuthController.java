package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.dto.AuthResponse;
import com.sporekart.identity.application.dto.RegisterRequest;
import com.sporekart.identity.application.service.IdentityService;
import com.sporekart.identity.domain.model.UserAccount;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final IdentityService identityService;

    public AuthController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAccount> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(identityService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(identityService.login(username, password));
    }
}
