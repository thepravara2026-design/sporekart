package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.config.MarketplaceConfig;
import com.sporekart.marketplace.domain.PluginInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class PluginSandbox {

    private static final Logger log = LoggerFactory.getLogger(PluginSandbox.class);

    private final MarketplaceConfig config;
    private final ConcurrentHashMap<String, SandboxContext> activeSandboxes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public PluginSandbox(MarketplaceConfig config) {
        this.config = config;
    }

    public SandboxContext createSandbox(PluginInstance instance) {
        var sandboxId = instance.getId();
        var context = new SandboxContext(
            sandboxId,
            config.getSandbox().getMaxExecutionTimeoutMs(),
            config.getSandbox().getMaxThreadsPerPlugin()
        );
        activeSandboxes.put(sandboxId, context);
        log.info("Sandbox created for plugin: {}", sandboxId);
        return context;
    }

    public Map<String, Object> executeInSandbox(String pluginId, Callable<Map<String, Object>> task) {
        var context = activeSandboxes.get(pluginId);
        if (context == null) {
            return Map.of("error", "No sandbox for plugin: " + pluginId);
        }
        try {
            var future = scheduler.submit(task);
            return future.get(config.getSandbox().getMaxExecutionTimeoutMs(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.warn("Plugin {} execution timed out", pluginId);
            context.incrementTimeoutCount();
            return Map.of("error", "Execution timed out", "pluginId", pluginId);
        } catch (Exception e) {
            log.error("Plugin {} execution failed: {}", pluginId, e.getMessage());
            context.incrementErrorCount();
            return Map.of("error", "Execution failed: " + e.getMessage(), "pluginId", pluginId);
        }
    }

    public void destroySandbox(String pluginId) {
        var context = activeSandboxes.remove(pluginId);
        if (context != null) {
            log.info("Sandbox destroyed for plugin: {}", pluginId);
        }
    }

    public int getActiveSandboxCount() { return activeSandboxes.size(); }

    public static class SandboxContext {
        private final String pluginId;
        private final long maxExecutionTimeMs;
        private final int maxThreads;
        private final ConcurrentHashMap<String, Object> state = new ConcurrentHashMap<>();
        private int timeoutCount = 0;
        private int errorCount = 0;

        public SandboxContext(String pluginId, long maxExecutionTimeMs, int maxThreads) {
            this.pluginId = pluginId;
            this.maxExecutionTimeMs = maxExecutionTimeMs;
            this.maxThreads = maxThreads;
        }

        public String pluginId() { return pluginId; }
        public long maxExecutionTimeMs() { return maxExecutionTimeMs; }
        public int maxThreads() { return maxThreads; }
        public void storeState(String key, Object value) { state.put(key, value); }
        public Object getState(String key) { return state.get(key); }
        public void incrementTimeoutCount() { timeoutCount++; }
        public void incrementErrorCount() { errorCount++; }
        public int getTimeoutCount() { return timeoutCount; }
        public int getErrorCount() { return errorCount; }
    }
}
