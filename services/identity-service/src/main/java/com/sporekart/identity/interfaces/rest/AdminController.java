package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.AuditService;
import com.sporekart.identity.application.service.PermissionService;
import com.sporekart.identity.domain.model.AuditEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PermissionService permissionService;
    private final AuditService auditService;

    public AdminController(PermissionService permissionService, AuditService auditService) {
        this.permissionService = permissionService;
        this.auditService = auditService;
    }

    @GetMapping("/permissions")
    public ResponseEntity<Set<String>> allPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/audit/events")
    public ResponseEntity<List<AuditEvent>> auditEvents(@RequestParam(required = false) String eventType) {
        if (eventType != null) {
            return ResponseEntity.ok(auditService.getEventsByType(eventType));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/audit/stats")
    public ResponseEntity<Map<String, Long>> auditStats() {
        return ResponseEntity.ok(Map.of(
                "totalLogins", auditService.countByEventType("LOGIN_SUCCESS"),
                "totalLogouts", auditService.countByEventType("LOGOUT"),
                "totalRefresh", auditService.countByEventType("TOKEN_REFRESH"),
                "totalOtpLogins", auditService.countByEventType("OTP_LOGIN_SUCCESS")));
    }
}
