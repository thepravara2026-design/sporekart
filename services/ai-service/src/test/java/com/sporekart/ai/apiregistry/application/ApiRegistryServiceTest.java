package com.sporekart.ai.apiregistry.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.apiregistry.api.ApiHealthService;
import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;
import com.sporekart.ai.apiregistry.domain.HttpMethod;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiHealthEntity;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiHealthRepository;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryEntity;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ApiRegistryServiceTest {

    @Mock
    private ApiRegistryRepository apiRegistryRepository;

    @Mock
    private ApiHealthRepository apiHealthRepository;

    @InjectMocks
    private ApiRegistryServiceImpl registryService;

    @InjectMocks
    private ApiHealthServiceImpl healthService;

    @Test
    void registerApiPersistsEntry() {
        ApiRegistryEntry entry = new ApiRegistryEntry();
        entry.setApiName("GetOrder");
        entry.setApiPath("/orders");
        entry.setHttpMethod(HttpMethod.GET);
        entry.setModule("order");

        when(apiRegistryRepository.save(any(ApiRegistryEntity.class))).thenAnswer(i -> i.getArgument(0));

        ApiRegistryEntry saved = registryService.registerApi(entry);

        assertNotNull(saved);
        assertEquals("GetOrder", saved.getApiName());
    }

    @Test
    void searchByModuleReturnsMatchingApis() {
        ApiRegistryEntity entity = new ApiRegistryEntity();
        entity.setModule("order");
        entity.setApiName("GetOrder");
        when(apiRegistryRepository.findByModule("order")).thenReturn(List.of(entity));

        List<ApiRegistryEntry> result = registryService.searchByModule("order");

        assertEquals(1, result.size());
        assertEquals("order", result.get(0).getModule());
    }

    @Test
    void getHealthReportAggregatesStatuses() {
        ApiHealthEntity entity = new ApiHealthEntity();
        entity.setApiId("a1");
        entity.setStatus(ApiHealthStatus.HEALTHY);
        entity.setCheckedAt(Instant.now());
        when(apiHealthRepository.findAll()).thenReturn(List.of(entity));

        List<ApiHealthService.ApiHealthReport> report = healthService.getHealthReport();

        assertEquals(1, report.size());
        assertEquals("a1", report.get(0).getApiId());
        assertEquals(ApiHealthStatus.HEALTHY, report.get(0).getStatus());
    }
}
