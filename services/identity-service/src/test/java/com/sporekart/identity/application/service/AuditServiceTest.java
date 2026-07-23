package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.AuditEvent;
import com.sporekart.identity.domain.repository.AuditEventRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditEventRepositoryPort auditEventRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    void shouldRecordEvent() {
        auditService.recordEvent("LOGIN_SUCCESS", "user-1", "Login from 192.168.1.1", "192.168.1.1");

        verify(auditEventRepository).save(any(AuditEvent.class));
    }

    @Test
    void shouldGetUserEvents() {
        var userId = "user-1";
        var events = List.of(
                new AuditEvent(1L, "LOGIN_SUCCESS", userId, "Login from 10.0.0.1", "10.0.0.1", Instant.now()),
                new AuditEvent(2L, "LOGOUT", userId, "User logout", "10.0.0.1", Instant.now()));
        when(auditEventRepository.findByUserId(userId)).thenReturn(events);

        var result = auditService.getUserEvents(userId);

        assertEquals(2, result.size());
        verify(auditEventRepository).findByUserId(userId);
    }

    @Test
    void shouldGetEventsByType() {
        var eventType = "LOGIN_FAILED";
        var events = List.of(
                new AuditEvent(1L, eventType, "user-1", "Invalid password from 10.0.0.1", "10.0.0.1", Instant.now()));
        when(auditEventRepository.findByEventType(eventType)).thenReturn(events);

        var result = auditService.getEventsByType(eventType);

        assertEquals(1, result.size());
        assertEquals(eventType, result.getFirst().getEventType());
        verify(auditEventRepository).findByEventType(eventType);
    }

    @Test
    void shouldCountByEventType() {
        var eventType = "LOGIN_SUCCESS";
        when(auditEventRepository.countByEventType(eventType)).thenReturn(42L);

        var count = auditService.countByEventType(eventType);

        assertEquals(42L, count);
        verify(auditEventRepository).countByEventType(eventType);
    }
}
