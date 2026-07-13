package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionRegistryEntity;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionRegistryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DecisionRegistryServiceImplTest {

    @Mock private DecisionRegistryRepository repository;

    private DecisionRegistryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionRegistryServiceImpl(repository);
    }

    @Test
    void registerCallsSave() {
        UUID id = UUID.randomUUID();
        DecisionRegistry registry = new DecisionRegistry(id, "reg1", "mod1", "http://ep",
            true, false, Map.of("k", "v"), OffsetDateTime.now(), OffsetDateTime.now());

        DecisionRegistry result = service.register(registry);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(repository).save(any(DecisionRegistryEntity.class));
    }

    @Test
    void unregisterSetsIsDeleted() {
        UUID id = UUID.randomUUID();
        DecisionRegistryEntity entity = new DecisionRegistryEntity();
        entity.setId(id);
        entity.setIsDeleted(false);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.unregister(id);

        ArgumentCaptor<DecisionRegistryEntity> captor = ArgumentCaptor.forClass(DecisionRegistryEntity.class);
        verify(repository).save(captor.capture());
        assertTrue(captor.getValue().getIsDeleted());
    }
}
