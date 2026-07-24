package com.sporekart.support.infrastructure.persistence;

import com.sporekart.support.domain.model.SupportCategory;
import com.sporekart.support.domain.model.SupportPriority;
import com.sporekart.support.domain.model.SupportTicket;
import com.sporekart.support.domain.model.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemorySupportRepositoryTest {

    private InMemorySupportRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySupportRepository();
    }

    @Test
    void saveAndFindByIdRoundTrip() {
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        SupportTicket saved = repository.save(ticket);

        Optional<SupportTicket> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void findByCustomerIdReturnsOnlyMatching() {
        SupportTicket ticket1 = SupportTicket.create("user-1", "Issue 1", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        SupportTicket ticket2 = SupportTicket.create("user-2", "Issue 2", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        repository.save(ticket1);
        repository.save(ticket2);

        List<SupportTicket> result = repository.findByCustomerId("user-1");

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getCustomerId());
    }

    @Test
    void findByStatusReturnsOnlyMatching() {
        SupportTicket ticket1 = SupportTicket.create("user-1", "Issue 1", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        SupportTicket ticket2 = SupportTicket.create("user-2", "Issue 2", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        ticket2.updateStatus(TicketStatus.IN_PROGRESS);
        repository.save(ticket1);
        repository.save(ticket2);

        List<SupportTicket> openTickets = repository.findByStatus(TicketStatus.OPEN);
        List<SupportTicket> inProgressTickets = repository.findByStatus(TicketStatus.IN_PROGRESS);

        assertEquals(1, openTickets.size());
        assertEquals(1, inProgressTickets.size());
    }

    @Test
    void findByAssignedToReturnsOnlyMatching() {
        SupportTicket ticket1 = SupportTicket.create("user-1", "Issue 1", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        SupportTicket ticket2 = SupportTicket.create("user-2", "Issue 2", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        ticket1.assignTo("agent-1");
        repository.save(ticket1);
        repository.save(ticket2);

        List<SupportTicket> result = repository.findByAssignedTo("agent-1");

        assertEquals(1, result.size());
        assertEquals("agent-1", result.get(0).getAssignedTo());
    }

    @Test
    void findAllReturnsAllTickets() {
        repository.save(SupportTicket.create("user-1", "Issue 1", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW));
        repository.save(SupportTicket.create("user-2", "Issue 2", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void deleteByIdRemovesTicket() {
        SupportTicket ticket = SupportTicket.create("user-1", "Issue", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW);
        repository.save(ticket);

        repository.deleteById(ticket.getId());
        assertTrue(repository.findById(ticket.getId()).isEmpty());
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        assertTrue(repository.findById("nonexistent").isEmpty());
    }
}