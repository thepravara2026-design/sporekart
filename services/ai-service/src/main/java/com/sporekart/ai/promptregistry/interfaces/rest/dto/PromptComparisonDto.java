package com.sporekart.ai.promptregistry.interfaces.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class PromptComparisonDto {

    private String promptId;
    private int versionA;
    private int versionB;
    private List<String> differences = new ArrayList<>();

    public PromptComparisonDto() {
    }

    public PromptComparisonDto(String promptId, int versionA, int versionB, List<String> differences) {
        this.promptId = promptId;
        this.versionA = versionA;
        this.versionB = versionB;
        this.differences = differences != null ? differences : new ArrayList<>();
    }

    public String getPromptId() {
        return promptId;
    }

    public void setPromptId(String promptId) {
        this.promptId = promptId;
    }

    public int getVersionA() {
        return versionA;
    }

    public void setVersionA(int versionA) {
        this.versionA = versionA;
    }

    public int getVersionB() {
        return versionB;
    }

    public void setVersionB(int versionB) {
        this.versionB = versionB;
    }

    public List<String> getDifferences() {
        return differences;
    }

    public void setDifferences(List<String> differences) {
        this.differences = differences != null ? differences : new ArrayList<>();
    }
}
