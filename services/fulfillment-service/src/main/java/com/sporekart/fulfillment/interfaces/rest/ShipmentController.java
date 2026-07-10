package com.sporekart.fulfillment.interfaces.rest;

import com.sporekart.fulfillment.application.service.ShipmentService;
import com.sporekart.fulfillment.domain.model.Shipment;
import com.sporekart.fulfillment.domain.model.ShipmentItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<Shipment> create(@RequestBody CreateShipmentRequest request) {
        Shipment shipment = shipmentService.create(request.orderId(), request.customerId(), request.shippingCharge(),
                request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> list(@RequestParam String customerId) {
        return ResponseEntity.ok(shipmentService.listByCustomer(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getById(@PathVariable String id) {
        return shipmentService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Shipment> cancel(@PathVariable String id) {
        return ResponseEntity.ok(shipmentService.cancel(id));
    }

    @PostMapping("/{id}/pickup")
    public ResponseEntity<Shipment> pickup(@PathVariable String id) {
        return ResponseEntity.ok(shipmentService.schedulePickup(id));
    }

    public record CreateShipmentRequest(String orderId, String customerId, BigDecimal shippingCharge,
            List<ShipmentItem> items) {
    }
}
