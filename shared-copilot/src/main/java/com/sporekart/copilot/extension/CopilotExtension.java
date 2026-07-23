package com.sporekart.copilot.extension;

public interface CopilotExtension {

    String getId();

    String getName();

    String getVersion();

    void initialize(CopilotExtensionContext context);

    void shutdown();
}
