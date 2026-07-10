package com.sporekart.identity.application.service;

import com.sporekart.identity.application.dto.RegisterRequest;
import com.sporekart.identity.domain.model.UserAccount;
import com.sporekart.identity.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class IdentityServiceTest {

    @Test
    void registerCreatesUserWhenEmailAvailable() {
        UserRepositoryPort repositoryPort = Mockito.mock(UserRepositoryPort.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        when(repositoryPort.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        IdentityService identityService = new IdentityService(repositoryPort, passwordEncoder);
        UserAccount user = identityService
                .register(new RegisterRequest("user@example.com", "secret123", "Ada", "Lovelace", "email", null));

        assertNotNull(user);
    }
}
