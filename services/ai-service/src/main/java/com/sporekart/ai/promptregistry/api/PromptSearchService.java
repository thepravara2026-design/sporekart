package com.sporekart.ai.promptregistry.api;

import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;

import java.util.List;

public interface PromptSearchService {

    List<PromptRegistryEntry> searchByName(String name);

    List<PromptRegistryEntry> searchByTags(List<String> tags);

    List<PromptRegistryEntry> searchByStatus(PromptStatus status);

    List<PromptRegistryEntry> searchByText(String text);
}
