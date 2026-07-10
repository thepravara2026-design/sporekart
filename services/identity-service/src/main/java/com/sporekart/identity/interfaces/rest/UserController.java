package com.sporekart.identity.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    public UserController() {
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("identity-ok");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> byId(@PathVariable String id) {
        return ResponseEntity.ok(id);
    }
}
