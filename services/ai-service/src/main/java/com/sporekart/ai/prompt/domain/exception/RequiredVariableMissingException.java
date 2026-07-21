package com.sporekart.ai.prompt.domain.exception;

public class RequiredVariableMissingException extends PromptDomainException {
    private final String variableName;

    public RequiredVariableMissingException(String variableName) {
        super("Required variable '" + variableName + "' is missing");
        this.variableName = variableName;
    }

    public String variableName() { return variableName; }
}
