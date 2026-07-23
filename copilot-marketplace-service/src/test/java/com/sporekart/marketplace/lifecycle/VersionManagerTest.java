package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.domain.PluginVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VersionManagerTest {

    private VersionManager versionManager;

    @BeforeEach
    void setUp() {
        versionManager = new VersionManager();
    }

    @Test
    void platformVersion_shouldBeOneDotZeroDotZero() {
        assertEquals("1.0.0", versionManager.getPlatformVersion());
    }

    @Test
    void isCompatible_shouldReturnTrueWhenVersionMatchesPlatform() {
        var version = new PluginVersion("1.0.0", "1.0.0", null, 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnTrueWhenMinBelowPlatform() {
        var version = new PluginVersion("2.0.0", "0.5.0", null, 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnFalseWhenMinAbovePlatform() {
        var version = new PluginVersion("1.0.0", "2.0.0", null, 100);
        assertFalse(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnTrueWhenMaxNull() {
        var version = new PluginVersion("1.0.0", "0.5.0", null, 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnTrueWhenMaxBlank() {
        var version = new PluginVersion("1.0.0", "0.5.0", "", 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnTrueWhenMaxAbovePlatform() {
        var version = new PluginVersion("1.0.0", "0.5.0", "2.0.0", 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnFalseWhenMaxBelowPlatform() {
        var version = new PluginVersion("1.0.0", "0.5.0", "0.9.0", 100);
        assertFalse(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_shouldReturnTrueWhenVersionExactlyAtBoundaries() {
        var version = new PluginVersion("1.0.0", "1.0.0", "1.0.0", 100);
        assertTrue(versionManager.isCompatible(version));
    }

    @Test
    void isCompatible_withStrings_shouldReturnTrueWhenCompatible() {
        assertTrue(versionManager.isCompatible("0.5.0", "2.0.0"));
    }

    @Test
    void isCompatible_withStrings_shouldReturnFalseWhenMinAbovePlatform() {
        assertFalse(versionManager.isCompatible("2.0.0", "3.0.0"));
    }

    @Test
    void isCompatible_withStrings_shouldReturnFalseWhenMaxBelowPlatform() {
        assertFalse(versionManager.isCompatible("0.5.0", "0.8.0"));
    }

    @Test
    void isCompatible_withStrings_shouldReturnTrueWhenMinNull() {
        assertTrue(versionManager.isCompatible(null, "2.0.0"));
    }

    @Test
    void isCompatible_withStrings_shouldReturnTrueWhenMaxNull() {
        assertTrue(versionManager.isCompatible("0.5.0", null));
    }

    @Test
    void isCompatible_withStrings_shouldReturnTrueWhenMaxBlank() {
        assertTrue(versionManager.isCompatible("0.5.0", ""));
    }

    @Test
    void canUpgrade_shouldReturnTrueWhenTargetHigher() {
        assertTrue(versionManager.canUpgrade("1.0.0", "2.0.0"));
    }

    @Test
    void canUpgrade_shouldReturnFalseWhenTargetLower() {
        assertFalse(versionManager.canUpgrade("2.0.0", "1.0.0"));
    }

    @Test
    void canUpgrade_shouldReturnFalseWhenSameVersion() {
        assertFalse(versionManager.canUpgrade("1.0.0", "1.0.0"));
    }

    @Test
    void canUpgrade_shouldHandlePatchVersions() {
        assertTrue(versionManager.canUpgrade("1.0.0", "1.0.1"));
    }

    @Test
    void canDowngrade_shouldReturnTrueWhenTargetLower() {
        assertTrue(versionManager.canDowngrade("2.0.0", "1.0.0"));
    }

    @Test
    void canDowngrade_shouldReturnFalseWhenTargetHigher() {
        assertFalse(versionManager.canDowngrade("1.0.0", "2.0.0"));
    }

    @Test
    void canDowngrade_shouldReturnFalseWhenSameVersion() {
        assertFalse(versionManager.canDowngrade("1.0.0", "1.0.0"));
    }

    @Test
    void recordVersion_shouldStoreVersion() {
        var version = new PluginVersion("1.0.0", "1.0.0", null, 100);
        versionManager.recordVersion("plugin1", version);
        var history = versionManager.getVersionHistory("plugin1");
        assertEquals(1, history.size());
        assertSame(version, history.get(0));
    }

    @Test
    void recordVersion_shouldAppendMultipleVersions() {
        versionManager.recordVersion("plugin1", new PluginVersion("1.0.0", "1.0.0", null, 100));
        versionManager.recordVersion("plugin1", new PluginVersion("2.0.0", "1.0.0", null, 100));
        versionManager.recordVersion("plugin1", new PluginVersion("3.0.0", "1.0.0", null, 100));
        assertEquals(3, versionManager.getVersionHistory("plugin1").size());
    }

    @Test
    void recordVersion_shouldNotAffectOtherPlugins() {
        versionManager.recordVersion("plugin1", new PluginVersion("1.0.0", "1.0.0", null, 100));
        versionManager.recordVersion("plugin2", new PluginVersion("2.0.0", "1.0.0", null, 100));
        assertEquals(1, versionManager.getVersionHistory("plugin1").size());
        assertEquals(1, versionManager.getVersionHistory("plugin2").size());
    }

    @Test
    void getVersionHistory_shouldReturnEmptyListForUnknownPlugin() {
        var history = versionManager.getVersionHistory("unknown");
        assertTrue(history.isEmpty());
    }

    @Test
    void getVersionHistory_shouldReturnUnmodifiableList() {
        versionManager.recordVersion("plugin1", new PluginVersion("1.0.0", "1.0.0", null, 100));
        var history = versionManager.getVersionHistory("plugin1");
        assertThrows(UnsupportedOperationException.class, () -> history.add(
            new PluginVersion("2.0.0", "1.0.0", null, 100)
        ));
    }

    @Test
    void getLatestVersion_shouldReturnHighestVersion() {
        versionManager.recordVersion("plugin1", new PluginVersion("1.0.0", "1.0.0", null, 100));
        versionManager.recordVersion("plugin1", new PluginVersion("3.0.0", "1.0.0", null, 100));
        versionManager.recordVersion("plugin1", new PluginVersion("2.0.0", "1.0.0", null, 100));

        var latest = versionManager.getLatestVersion("plugin1");
        assertTrue(latest.isPresent());
        assertEquals("3.0.0", latest.get().version());
    }

    @Test
    void getLatestVersion_shouldReturnEmptyForUnknownPlugin() {
        var latest = versionManager.getLatestVersion("unknown");
        assertTrue(latest.isEmpty());
    }

    @Test
    void getLatestVersion_shouldReturnOnlyVersionWhenOneExists() {
        versionManager.recordVersion("plugin1", new PluginVersion("1.0.0", "1.0.0", null, 100));
        var latest = versionManager.getLatestVersion("plugin1");
        assertTrue(latest.isPresent());
        assertEquals("1.0.0", latest.get().version());
    }

    @Test
    void compareVersions_shouldReturnNegativeWhenV1Lower() {
        assertTrue(versionManager.compareVersions("1.0.0", "2.0.0") < 0);
    }

    @Test
    void compareVersions_shouldReturnPositiveWhenV1Higher() {
        assertTrue(versionManager.compareVersions("3.0.0", "1.0.0") > 0);
    }

    @Test
    void compareVersions_shouldReturnZeroWhenEqual() {
        assertEquals(0, versionManager.compareVersions("1.0.0", "1.0.0"));
    }

    @Test
    void compareVersions_shouldHandleMajorVersionDifferences() {
        assertTrue(versionManager.compareVersions("1.0.0", "2.0.0") < 0);
        assertTrue(versionManager.compareVersions("5.0.0", "3.0.0") > 0);
    }

    @Test
    void compareVersions_shouldHandleMinorVersionDifferences() {
        assertTrue(versionManager.compareVersions("1.0.0", "1.1.0") < 0);
        assertTrue(versionManager.compareVersions("1.2.0", "1.1.0") > 0);
    }

    @Test
    void compareVersions_shouldHandlePatchVersionDifferences() {
        assertTrue(versionManager.compareVersions("1.0.0", "1.0.1") < 0);
        assertTrue(versionManager.compareVersions("1.0.5", "1.0.1") > 0);
    }

    @Test
    void compareVersions_shouldHandleDifferentLengthVersions() {
        assertTrue(versionManager.compareVersions("1.0", "1.0.0") < 0);
        assertTrue(versionManager.compareVersions("1.0.0", "1.0") > 0);
    }

    @Test
    void compareVersions_shouldReturnZeroOnInvalidFormat() {
        assertEquals(0, versionManager.compareVersions("invalid", "1.0.0"));
    }

    @Test
    void canUpgrade_shouldHandleMajorVersions() {
        assertTrue(versionManager.canUpgrade("1.0.0", "2.0.0"));
        assertTrue(versionManager.canUpgrade("1.9.9", "2.0.0"));
    }

    @Test
    void canDowngrade_shouldHandleMajorVersions() {
        assertTrue(versionManager.canDowngrade("2.0.0", "1.0.0"));
        assertTrue(versionManager.canDowngrade("3.0.0", "2.9.9"));
    }
}
