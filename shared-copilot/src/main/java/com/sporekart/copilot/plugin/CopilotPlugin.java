package com.sporekart.copilot.plugin;

import com.sporekart.copilot.capability.Capability;

import java.util.List;

public interface CopilotPlugin {

    String getId();

    String getName();

    String getVersion();

    void onLoad(CopilotPluginContext context);

    void onUnload();

    List<Capability> getProvidedCapabilities();
}
