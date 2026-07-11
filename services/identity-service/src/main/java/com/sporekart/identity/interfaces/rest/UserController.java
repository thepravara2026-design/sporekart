package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.IdentityService;
import com.sporekart.identity.domain.model.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IdentityService identityService;

    public UserController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("identity-ok");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> byId(@PathVariable String id) {
        return identityService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
