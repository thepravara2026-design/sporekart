package com.sporekart.admin.interfaces.rest;

import com.sporekart.admin.application.service.AdminOperationsService;
import com.sporekart.admin.domain.model.ApprovalRequest;
import com.sporekart.admin.domain.model.SupportTicket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminOperationsService adminOperationsService;

    public AdminController(AdminOperationsService adminOperationsService) {
        this.adminOperationsService = adminOperationsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(adminOperationsService.getDashboardSummary());
    }

    @GetMapping("/support")
    public ResponseEntity<List<SupportTicket>> supportTickets() {
        return ResponseEntity.ok(adminOperationsService.listSupportTickets());
    }

    @PostMapping("/support")
    public ResponseEntity<SupportTicket> createSupportTicket(@RequestBody CreateSupportTicketRequest request) {
        SupportTicket ticket = adminOperationsService.createSupportTicket(request.subject(), request.description(),
                request.requester());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/approvals")
    public ResponseEntity<ApprovalRequest> createApproval(@RequestBody CreateApprovalRequest request) {
        ApprovalRequest approval = adminOperationsService.createApproval(request.targetType(), request.targetId());
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }

    public record CreateSupportTicketRequest(String subject, String description, String requester) {
    }

    public record CreateApprovalRequest(String targetType, String targetId) {
    }
}
