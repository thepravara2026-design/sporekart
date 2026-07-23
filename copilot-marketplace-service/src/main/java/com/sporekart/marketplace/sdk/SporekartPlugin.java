package com.sporekart.marketplace.sdk;

import java.util.Map;

public interface SporekartPlugin {

    PluginManifest getManifest();

    void onInstall(PluginContext context);

    void onEnable(PluginContext context);

    void onDisable(PluginContext context);

    void onUninstall(PluginContext context);

    void onUpgrade(PluginContext context, String previousVersion);

    void onDowngrade(PluginContext context, String previousVersion);

    Map<String, Object> execute(String action, Map<String, Object> params);

    Map<String, Object> healthCheck();
}