package com.sporekart.ai.promptregistry.interfaces.rest.dto;

import com.sporekart.ai.promptregistry.domain.PromptStatus;

import java.util.ArrayList;
import java.util.List;

public class PromptSearchRequestDto {

    private String name;
    private PromptStatus status;
    private List<String> tags = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PromptStatus getStatus() {
        return status;
    }

    public void setStatus(PromptStatus status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }
}
