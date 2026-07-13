package com.sporekart.ai.apiregistry.api;

import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;

import java.util.List;
import java.util.Optional;

public interface ApiRegistryService {

    ApiRegistryEntry registerApi(ApiRegistryEntry entry);

    ApiRegistryEntry updateApi(ApiRegistryEntry entry);

    Optional<ApiRegistryEntry> getApi(String apiId);

    List<ApiRegistryEntry> listApis();

    List<ApiRegistryEntry> searchByModule(String module);

    List<ApiRegistryEntry> searchByPath(String path);

    List<ApiRegistryEntry> getByOwner(String owner);
}
