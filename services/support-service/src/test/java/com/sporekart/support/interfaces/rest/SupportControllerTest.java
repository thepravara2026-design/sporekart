package com.sporekart.support.interfaces.rest;

import com.sporekart.support.application.dto.TicketResponse;
import com.sporekart.support.application.dto.CreateTicketRequest;
import com.sporekart.support.application.service.SupportService;
import com.sporekart.support.domain.model.SupportCategory;
import com.sporekart.support.domain.model.SupportPriority;
import com.sporekart.support.domain.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupportControllerTest {

    @Test
    void createTicketReturnsCreated() {
        SupportService service = mock(SupportService.class);
        TicketResponse response = new TicketResponse("id-1", "user-1", "Subject", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW, TicketStatus.OPEN,
                null, null, Instant.now(), Instant.now(), null);
        when(service.createTicket(new CreateTicketRequest("user-1", "Subject", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW))).thenReturn(response);

        SupportController controller = new SupportController(service);
        var result = controller.createTicket(
                new CreateTicketRequest("user-1", "Subject", "Desc",
                        SupportCategory.GENERAL, SupportPriority.LOW));

        assertNotNull(result);
        assertEquals(201, result.getStatusCode().value());
        assertEquals("id-1", result.getBody().id());
    }

    @Test
    void getTicketReturnsTicket() {
        SupportService service = mock(SupportService.class);
        TicketResponse response = new TicketResponse("id-1", "user-1", "Subject", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW, TicketStatus.OPEN,
                null, null, Instant.now(), Instant.now(), null);
        when(service.getTicket("id-1")).thenReturn(response);

        SupportController controller = new SupportController(service);
        var result = controller.getTicket("id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("id-1", result.getBody().id());
    }

    @Test
    void getCustomerTicketsReturnsList() {
        SupportService service = mock(SupportService.class);
        TicketResponse response = new TicketResponse("id-1", "user-1", "Subject", "Desc",
                SupportCategory.GENERAL, SupportPriority.LOW, TicketStatus.OPEN,
                null, null, Instant.now(), Instant.now(), null);
        when(service.getCustomerTickets("user-1")).thenReturn(List.of(response));

        SupportController controller = new SupportController(service);
        var result = controller.getCustomerTickets("user-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals("user-1", result.getBody().get(0).customerId());
    }
}