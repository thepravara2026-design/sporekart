package com.sporekart.ai.application.service;

import com.sporekart.ai.common.exception.WarehouseException;
import com.sporekart.ai.infrastructure.persistence.repository.WarehouseRepository;
import com.sporekart.ai.infrastructure.persistence.repository.WarehouseStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceValidationTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseStockRepository warehouseStockRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void shouldRejectWarehouseCapacityBelowZero() {
        assertThrows(WarehouseException.class, () -> warehouseService.createWarehouse("WH1", "Main", "DISTRIBUTION",
                new BigDecimal("-1")));
    }

    @Test
    void shouldRejectTransferWhenSourceWarehouseMissing() {
        when(warehouseStockRepository.findAll()).thenReturn(java.util.List.of());
        assertThrows(WarehouseException.class,
                () -> warehouseService.transferStock(UUID.randomUUID(), UUID.randomUUID(),
                        UUID.randomUUID(), BigDecimal.ONE));
    }
}
