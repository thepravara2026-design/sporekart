package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.IdentityService;
import com.sporekart.identity.domain.model.RoleType;
import com.sporekart.identity.domain.model.UserAccount;
import com.sporekart.identity.domain.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdentityService identityService;

    @Test
    void registerEndpointAcceptsRequest() throws Exception {
        when(identityService.register(any())).thenReturn(
                new UserAccount("id", "user@example.com", null, "encoded", "Ada", "Lovelace",
                        UserStatus.PENDING, false, false, Instant.now(), Instant.now(), null, false, Set.of(RoleType.CUSTOMER)));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"email\":\"user@example.com\",\"password\":\"secret123\",\"firstName\":\"Ada\",\"lastName\":\"Lovelace\",\"channel\":\"email\"}"))
                .andExpect(status().isCreated());
    }
}
