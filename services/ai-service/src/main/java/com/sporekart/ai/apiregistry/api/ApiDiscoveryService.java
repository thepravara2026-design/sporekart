package com.sporekart.ai.apiregistry.api;

import com.sporekart.ai.apiregistry.domain.ApiDependency;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;

import java.util.List;
import java.util.Map;

public interface ApiDiscoveryService {

    List<ApiRegistryEntry> discoverByModule(String module);

    List<ApiDependency> findDependencies(String apiId);

    Map<String, ApiRegistryEntry> getApiMap();
}
