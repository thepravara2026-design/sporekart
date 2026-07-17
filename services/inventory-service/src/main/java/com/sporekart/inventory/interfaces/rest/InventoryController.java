package com.sporekart.inventory.interfaces.rest;

import com.sporekart.inventory.application.service.InventoryService;
import com.sporekart.inventory.domain.model.InventoryItem;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryItem> get(@PathVariable String productId) {
        return ResponseEntity.ok(inventoryService.snapshot(productId));
    }
}
