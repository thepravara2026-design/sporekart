package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.IdentityService;
import com.sporekart.identity.domain.model.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IdentityService identityService;

    public UserController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(g -> g.getAuthority()).toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> byId(@PathVariable String id) {
        return identityService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
