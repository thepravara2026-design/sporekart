package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.config.MarketplaceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResourceLimiterTest {

    private MarketplaceConfig config;
    private MarketplaceConfig.SandboxConfig sandboxConfig;
    private ResourceLimiter resourceLimiter;

    @BeforeEach
    void setUp() {
        config = mock(MarketplaceConfig.class);
        sandboxConfig = mock(MarketplaceConfig.SandboxConfig.class);
        when(config.getSandbox()).thenReturn(sandboxConfig);
        when(sandboxConfig.getMaxThreadsPerPlugin()).thenReturn(5);

        resourceLimiter = new ResourceLimiter(config);
    }

    @Test
    void allocateResources_shouldSucceedWhenUnderLimit() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);

        assertTrue(resourceLimiter.allocateResources("plugin1"));
        assertEquals(1, resourceLimiter.getActivePluginCount());
    }

    @Test
    void allocateResources_shouldFailWhenAtLimit() {
        when(config.getMaxInstalledPlugins()).thenReturn(2);

        assertTrue(resourceLimiter.allocateResources("plugin1"));
        assertTrue(resourceLimiter.allocateResources("plugin2"));
        assertFalse(resourceLimiter.allocateResources("plugin3"));
        assertEquals(2, resourceLimiter.getActivePluginCount());
    }

    @Test
    void allocateResources_shouldNotIncreaseCountForExistingPlugin() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);

        assertTrue(resourceLimiter.allocateResources("plugin1"));
        assertTrue(resourceLimiter.allocateResources("plugin1"));
        assertEquals(1, resourceLimiter.getActivePluginCount());
    }

    @Test
    void releaseResources_shouldRemovePlugin() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);

        resourceLimiter.allocateResources("plugin1");
        assertEquals(1, resourceLimiter.getActivePluginCount());

        resourceLimiter.releaseResources("plugin1");
        assertEquals(0, resourceLimiter.getActivePluginCount());
    }

    @Test
    void releaseResources_shouldDoNothingForUnknownPlugin() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);

        resourceLimiter.allocateResources("plugin1");
        resourceLimiter.releaseResources("unknown");
        assertEquals(1, resourceLimiter.getActivePluginCount());
    }

    @Test
    void releaseResources_shouldFreeSlotForNewPlugin() {
        when(config.getMaxInstalledPlugins()).thenReturn(1);

        resourceLimiter.allocateResources("plugin1");
        resourceLimiter.releaseResources("plugin1");

        assertTrue(resourceLimiter.allocateResources("plugin2"));
    }

    @Test
    void isWithinLimits_shouldReturnTrueWhenThreadCountWithinLimit() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");

        assertTrue(resourceLimiter.isWithinLimits("plugin1"));
    }

    @Test
    void isWithinLimits_shouldReturnTrueWhenThreadCountEqualsLimit() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");

        var usage = resourceLimiter.getUsage("plugin1");
        assertNotNull(usage);
        for (int i = 0; i < 5; i++) {
            usage.incrementThreads();
        }

        assertTrue(resourceLimiter.isWithinLimits("plugin1"));
    }

    @Test
    void isWithinLimits_shouldReturnFalseWhenThreadCountExceedsLimit() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");

        var usage = resourceLimiter.getUsage("plugin1");
        assertNotNull(usage);
        for (int i = 0; i < 6; i++) {
            usage.incrementThreads();
        }

        assertFalse(resourceLimiter.isWithinLimits("plugin1"));
    }

    @Test
    void isWithinLimits_shouldReturnFalseForUnknownPlugin() {
        assertFalse(resourceLimiter.isWithinLimits("unknown"));
    }

    @Test
    void getUsage_shouldReturnNullForUnknownPlugin() {
        assertNull(resourceLimiter.getUsage("unknown"));
    }

    @Test
    void getUsage_shouldReturnUsageForExistingPlugin() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");

        var usage = resourceLimiter.getUsage("plugin1");
        assertNotNull(usage);
        assertEquals(0, usage.getThreadCount());
        assertEquals(0, usage.getExecutionCount());
        assertEquals(0, usage.getTotalExecutionTimeMs());
    }

    @Test
    void threadTracking_shouldTrackThreadCount() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");
        var usage = resourceLimiter.getUsage("plugin1");

        usage.incrementThreads();
        assertEquals(1, usage.getThreadCount());

        usage.incrementThreads();
        assertEquals(2, usage.getThreadCount());

        usage.decrementThreads();
        assertEquals(1, usage.getThreadCount());
    }

    @Test
    void executionCount_shouldTrackExecutions() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");
        var usage = resourceLimiter.getUsage("plugin1");

        usage.incrementExecutions();
        usage.incrementExecutions();
        usage.incrementExecutions();

        assertEquals(3, usage.getExecutionCount());
    }

    @Test
    void timeTracking_shouldTrackExecutionTime() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");
        var usage = resourceLimiter.getUsage("plugin1");

        usage.addExecutionTime(1000);
        usage.addExecutionTime(2000);

        assertEquals(3000, usage.getTotalExecutionTimeMs());
    }

    @Test
    void timeTracking_shouldAccumulateOverMultiplePlugins() {
        when(config.getMaxInstalledPlugins()).thenReturn(100);
        resourceLimiter.allocateResources("plugin1");
        resourceLimiter.allocateResources("plugin2");

        var usage1 = resourceLimiter.getUsage("plugin1");
        var usage2 = resourceLimiter.getUsage("plugin2");

        usage1.addExecutionTime(1000);
        usage2.addExecutionTime(2000);

        assertEquals(1000, usage1.getTotalExecutionTimeMs());
        assertEquals(2000, usage2.getTotalExecutionTimeMs());
    }

    @Test
    void getActivePluginCount_shouldReturnZeroInitially() {
        assertEquals(0, resourceLimiter.getActivePluginCount());
    }

    @Test
    void resourceLimiter_shouldHandleManyAllocations() {
        when(config.getMaxInstalledPlugins()).thenReturn(10);

        for (int i = 0; i < 10; i++) {
            assertTrue(resourceLimiter.allocateResources("plugin" + i));
        }
        assertEquals(10, resourceLimiter.getActivePluginCount());
        assertFalse(resourceLimiter.allocateResources("plugin11"));
    }
}
