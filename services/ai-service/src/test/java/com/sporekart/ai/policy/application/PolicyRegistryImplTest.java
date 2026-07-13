package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyRegistryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyRegistryImplTest {
    @Mock private PolicyRegistryRepository repository;
    private PolicyRegistryImpl registryService;

    @BeforeEach void setUp() { registryService = new PolicyRegistryImpl(repository); }

    @Test void testRegister() {
        com.sporekart.ai.policy.domain.PolicyRegistry reg = new com.sporekart.ai.policy.domain.PolicyRegistry(
            UUID.randomUUID(), "test", "content", PolicyType.MODULE, PolicyScope.MODULE, true, true, Map.of(), OffsetDateTime.now(), OffsetDateTime.now());
        when(repository.save(any())).thenReturn(new com.sporekart.ai.policy.infrastructure.persistence.PolicyRegistryEntity());
        assertNotNull(registryService.register(reg));
    }

    @Test void testFindByModule() { when(repository.findByModuleAndIsDeletedFalse("c")).thenReturn(List.of()); assertTrue(registryService.findByModule("c").isEmpty()); }
}
