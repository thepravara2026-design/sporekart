package com.sporekart.ai.providerregistry.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderStatus;
import com.sporekart.ai.providerregistry.domain.ProviderType;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryEntity;
import com.sporekart.ai.providerregistry.infrastructure.persistence.ProviderRegistryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProviderRegistryServiceTest {

    @Mock
    private ProviderRegistryRepository repository;

    @InjectMocks
    private ProviderRegistryServiceImpl service;

    @Test
    void registerProviderStoresEntity() {
        ProviderRegistryEntry entry = new ProviderRegistryEntry();
        entry.setProviderName("OpenAI");
        entry.setProviderType(ProviderType.CLOUD);
        entry.setVersion("1.0");
        entry.setStatus(ProviderStatus.ACTIVE);
        entry.setPriority(1);
        entry.setCapabilities(List.of(ProviderCapability.TEXT_GENERATION));

        when(repository.save(any(ProviderRegistryEntity.class))).thenAnswer(i -> i.getArgument(0));

        ProviderRegistryEntry saved = service.registerProvider(entry);

        assertNotNull(saved);
        assertEquals("OpenAI", saved.getProviderName());
        verify(repository).save(any(ProviderRegistryEntity.class));
    }

    @Test
    void getProviderReturnsDomain() {
        ProviderRegistryEntity entity = new ProviderRegistryEntity();
        entity.setProviderId("p1");
        entity.setProviderName("Azure");
        entity.setStatus(ProviderStatus.ACTIVE);
        when(repository.findById("p1")).thenReturn(Optional.of(entity));

        Optional<ProviderRegistryEntry> result = service.getProvider("p1");

        assertTrue(result.isPresent());
        assertEquals("Azure", result.get().getProviderName());
    }

    @Test
    void listProvidersDelegatesToRepository() {
        ProviderRegistryEntity entity = new ProviderRegistryEntity();
        entity.setProviderId("p1");
        entity.setProviderName("OpenAI");
        when(repository.findAll()).thenReturn(List.of(entity));

        assertEquals(1, service.listProviders().size());
    }

    @Test
    void searchProvidersFiltersByName() {
        ProviderRegistryEntity entity = new ProviderRegistryEntity();
        entity.setProviderId("p1");
        entity.setProviderName("OpenAI");
        when(repository.findByProviderNameContainingIgnoreCase("Open")).thenReturn(List.of(entity));

        List<ProviderRegistryEntry> result = service.searchProviders("Open");

        assertEquals(1, result.size());
        assertEquals("OpenAI", result.get(0).getProviderName());
    }
}
