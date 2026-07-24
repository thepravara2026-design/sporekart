package com.sporekart.support.application.service;

import com.sporekart.support.application.dto.CreateTicketRequest;
import com.sporekart.support.application.dto.TicketResponse;
import com.sporekart.support.application.dto.UpdateTicketRequest;
import com.sporekart.support.common.exception.TicketNotFoundException;
import com.sporekart.support.domain.model.SupportCategory;
import com.sporekart.support.domain.model.SupportPriority;
import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;
import com.sporekart.support.domain.repository.SupportRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SupportServiceTest {

    @Test
    void createTicketPersistsAndReturnsResponse() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.createTicket(
                new CreateTicketRequest("user-1", "Login issue", "Cannot login to my account",
                        SupportCategory.ACCOUNT_ISSUE, SupportPriority.HIGH));

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("user-1", response.customerId());
        assertEquals("Login issue", response.subject());
        assertEquals(TicketStatus.OPEN, response.status());
    }

    @Test
    void getTicketThrowsWhenNotFound() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        when(repo.findById("nonexistent")).thenReturn(Optional.empty());
        SupportService service = new SupportService(repo);

        assertThrows(TicketNotFoundException.class, () -> service.getTicket("nonexistent"));
    }

    @Test
    void getTicketReturnsTicketWhenFound() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.getTicket(ticket.getId());

        assertNotNull(response);
        assertEquals(ticket.getId(), response.id());
    }

    @Test
    void getCustomerTicketsReturnsOnlyCustomerTickets() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket1 = SupportTicket.create("user-1", "Issue 1", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        SupportTicket ticket2 = SupportTicket.create("user-2", "Issue 2", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findByCustomerId("user-1")).thenReturn(List.of(ticket1));

        SupportService service = new SupportService(repo);
        List<TicketResponse> tickets = service.getCustomerTickets("user-1");

        assertEquals(1, tickets.size());
        assertEquals("user-1", tickets.get(0).customerId());
    }

    @Test
    void assignTicketOnlyWorksOnOpenTickets() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.assignTicket(ticket.getId(), "agent-1");

        assertEquals("agent-1", response.assignedTo());
    }

    @Test
    void assignTicketThrowsOnNonOpenTicket() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        ticket.updateStatus(TicketStatus.IN_PROGRESS);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        SupportService service = new SupportService(repo);

        assertThrows(IllegalStateException.class,
                () -> service.assignTicket(ticket.getId(), "agent-1"));
    }

    @Test
    void resolveTicketRequiresResolution() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.resolveTicket(ticket.getId(), "Fixed the issue");

        assertEquals(TicketStatus.RESOLVED, response.status());
        assertEquals("Fixed the issue", response.resolution());
        assertNotNull(response.resolvedAt());
    }

    @Test
    void resolveTicketThrowsOnBlankResolution() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        SupportService service = new SupportService(repo);

        assertThrows(IllegalArgumentException.class,
                () -> service.resolveTicket(ticket.getId(), ""));
    }

    @Test
    void updateTicketStatusFromResolvedToClosed() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        ticket.resolve("Fixed");
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.updateTicketStatus(ticket.getId(), TicketStatus.CLOSED, null);

        assertEquals(TicketStatus.CLOSED, response.status());
    }

    @Test
    void updateTicketStatusThrowsWhenNotResolvedBeforeClosing() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        SupportService service = new SupportService(repo);

        assertThrows(IllegalStateException.class,
                () -> service.updateTicketStatus(ticket.getId(), TicketStatus.CLOSED, null));
    }

    @Test
    void updateTicketStatusResolveRequiresResolution() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        SupportService service = new SupportService(repo);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateTicketStatus(ticket.getId(), TicketStatus.RESOLVED, ""));
    }

    @Test
    void addResolutionUpdatesResolution() {
        SupportRepositoryPort repo = Mockito.mock(SupportRepositoryPort.class);
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        when(repo.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SupportService service = new SupportService(repo);

        TicketResponse response = service.addResolution(ticket.getId(), "Working on it");

        assertEquals("Working on it", response.resolution());
    }
}