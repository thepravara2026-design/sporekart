package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PromptManagementService {
    private final List<Prompt> prompts = new ArrayList<>();

    public Prompt createPrompt(String category, String name, String template, String version) {
        Prompt prompt = new Prompt(UUID.randomUUID(), category, name, template, version);
        prompts.add(prompt);
        return prompt;
    }

    public List<Prompt> listPrompts() {
        return new ArrayList<>(prompts);
    }
}
