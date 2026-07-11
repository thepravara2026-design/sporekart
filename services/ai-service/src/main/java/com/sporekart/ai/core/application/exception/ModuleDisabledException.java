package com.sporekart.ai.core.application.exception;

import com.sporekart.ai.core.domain.AiModule;

public class ModuleDisabledException extends AiCoreException {
    private final AiModule module;

    public ModuleDisabledException(AiModule module) {
        super("AI module [" + module.name() + "] is disabled");
        this.module = module;
    }

    public AiModule getModule() {
        return module;
    }
}
