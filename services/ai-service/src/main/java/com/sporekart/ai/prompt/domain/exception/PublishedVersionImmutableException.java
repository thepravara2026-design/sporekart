package com.sporekart.ai.prompt.domain.exception;

import com.sporekart.ai.prompt.domain.valueobject.SemanticVersion;

public class PublishedVersionImmutableException extends PromptDomainException {
    private final SemanticVersion version;

    public PublishedVersionImmutableException(SemanticVersion version) {
        super("Published version " + version + " is immutable and cannot be modified");
        this.version = version;
    }

    public SemanticVersion version() { return version; }
}
