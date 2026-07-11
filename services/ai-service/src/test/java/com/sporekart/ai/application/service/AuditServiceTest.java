package com.sporekart.ai.application.service;

import com.sporekart.ai.infrastructure.persistence.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditService.class)
class AuditServiceTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void shouldPersistAuditEntryForOperationalAction() {
        auditService.logAction("warehouse", "wh-001", "CREATED", "Warehouse created");

        assertThat(auditLogRepository.findAll()).hasSize(1);
        assertThat(auditLogRepository.findAll().get(0).getEntityType()).isEqualTo("warehouse");
        assertThat(auditLogRepository.findAll().get(0).getAction()).isEqualTo("CREATED");
    }
}
