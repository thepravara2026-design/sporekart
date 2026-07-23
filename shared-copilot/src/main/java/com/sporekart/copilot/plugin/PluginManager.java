package com.sporekart.copilot.plugin;

import java.util.List;

public interface PluginManager {

    void loadPlugin(CopilotPlugin plugin);

    void unloadPlugin(String pluginId);

    List<CopilotPlugin> getLoadedPlugins();

    CopilotPlugin getPlugin(String pluginId);
}
