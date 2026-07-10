package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.service.IdentityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdentityService identityService;

    @Test
    void registerEndpointAcceptsRequest() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"email\":\"user@example.com\",\"password\":\"secret123\",\"firstName\":\"Ada\",\"lastName\":\"Lovelace\",\"channel\":\"email\"}"))
                .andExpect(status().isBadRequest());
    }
}
