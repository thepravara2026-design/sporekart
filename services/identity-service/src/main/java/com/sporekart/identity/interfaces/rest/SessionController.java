package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.SessionService;
import com.sporekart.identity.domain.model.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Session>> mySessions(Authentication authentication) {
        var userId = authentication.getName();
        return ResponseEntity.ok(sessionService.getActiveSessions(userId));
    }

    @GetMapping
    public ResponseEntity<List<Session>> userSessions(@RequestParam String userId) {
        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> revokeSession(@PathVariable String sessionId) {
        sessionService.revokeSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> revokeAllMySessions(Authentication authentication) {
        sessionService.revokeAllSessions(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
