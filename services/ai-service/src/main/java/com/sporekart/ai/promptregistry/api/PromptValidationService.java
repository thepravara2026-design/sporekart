package com.sporekart.ai.promptregistry.api;

import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;

import java.util.List;

public interface PromptValidationService {

    List<String> validateStructure(PromptRegistryEntry entry);

    List<String> validateVariables(PromptRegistryEntry entry);

    List<String> validateLength(PromptRegistryEntry entry);
}
