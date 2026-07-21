package com.sporekart.ai.prompt.domain.exception;

import com.sporekart.ai.prompt.domain.valueobject.PromptStatus;

public class InvalidStatusTransitionException extends PromptDomainException {
    private final PromptStatus from;
    private final PromptStatus to;

    public InvalidStatusTransitionException(PromptStatus from, PromptStatus to) {
        super("Cannot transition from " + from + " to " + to);
        this.from = from;
        this.to = to;
    }

    public PromptStatus from() { return from; }
    public PromptStatus to() { return to; }
}
