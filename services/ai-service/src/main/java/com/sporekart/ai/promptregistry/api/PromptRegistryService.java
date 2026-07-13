package com.sporekart.ai.promptregistry.api;

import com.sporekart.ai.promptregistry.domain.PromptComparisonResult;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptVersionInfo;

import java.util.List;
import java.util.Optional;

public interface PromptRegistryService {

    PromptRegistryEntry registerPrompt(PromptRegistryEntry entry);

    PromptRegistryEntry updatePrompt(PromptRegistryEntry entry);

    Optional<PromptRegistryEntry> getPrompt(String promptId);

    List<PromptRegistryEntry> listPrompts();

    List<PromptVersionInfo> getVersions(String promptId);

    PromptRegistryEntry rollbackToVersion(String promptId, int version);

    PromptComparisonResult compareVersions(String promptId, int versionA, int versionB);
}
