package com.sporekart.ai.prompt.domain.exception;

import com.sporekart.ai.prompt.domain.valueobject.SemanticVersion;

public class DuplicateVersionException extends PromptDomainException {
    private final SemanticVersion version;

    public DuplicateVersionException(SemanticVersion version) {
        super("Version " + version + " already exists");
        this.version = version;
    }

    public SemanticVersion version() { return version; }
}
