package com.sporekart.support.interfaces.rest;

import com.sporekart.support.application.dto.CreateTicketRequest;
import com.sporekart.support.application.dto.TicketResponse;
import com.sporekart.support.application.dto.UpdateTicketRequest;
import com.sporekart.support.application.service.SupportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supportService.createTicket(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String id) {
        return ResponseEntity.ok(supportService.getTicket(id));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listTickets(
            @RequestParam(required = false) String customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(supportService.getCustomerTickets(customerId));
        }
        return ResponseEntity.ok(List.of());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable String id,
            @Valid @RequestBody UpdateTicketRequest request) {
        if (request.assignedTo() != null) {
            return ResponseEntity.ok(supportService.assignTicket(id, request.assignedTo()));
        }
        if (request.status() != null) {
            return ResponseEntity.ok(
                    supportService.updateTicketStatus(id, request.status(), request.resolution()));
        }
        if (request.resolution() != null) {
            return ResponseEntity.ok(supportService.addResolution(id, request.resolution()));
        }
        return ResponseEntity.ok(supportService.getTicket(id));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<TicketResponse> resolveTicket(
            @PathVariable String id,
            @RequestBody String resolution) {
        return ResponseEntity.ok(supportService.resolveTicket(id, resolution));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketResponse>> getCustomerTickets(@PathVariable String customerId) {
        return ResponseEntity.ok(supportService.getCustomerTickets(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(List.of());
    }
}