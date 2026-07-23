package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.domain.PluginVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class VersionManager {

    private static final Logger log = LoggerFactory.getLogger(VersionManager.class);

    private static final String PLATFORM_VERSION = "1.0.0";

    private final Map<String, List<PluginVersion>> versionHistory = new HashMap<>();

    public boolean isCompatible(PluginVersion version) {
        return compareVersions(version.minPlatformVersion(), PLATFORM_VERSION) <= 0
            && (version.maxPlatformVersion() == null || version.maxPlatformVersion().isBlank()
                || compareVersions(PLATFORM_VERSION, version.maxPlatformVersion()) <= 0);
    }

    public boolean isCompatible(String minVersion, String maxVersion) {
        if (minVersion != null && compareVersions(PLATFORM_VERSION, minVersion) < 0) return false;
        if (maxVersion != null && !maxVersion.isBlank() && compareVersions(PLATFORM_VERSION, maxVersion) > 0) return false;
        return true;
    }

    public boolean canUpgrade(String currentVersion, String targetVersion) {
        return compareVersions(targetVersion, currentVersion) > 0;
    }

    public boolean canDowngrade(String currentVersion, String targetVersion) {
        return compareVersions(targetVersion, currentVersion) < 0;
    }

    public void recordVersion(String pluginId, PluginVersion version) {
        versionHistory.computeIfAbsent(pluginId, k -> new ArrayList<>()).add(version);
        log.debug("Version recorded: {} v{}", pluginId, version.version());
    }

    public List<PluginVersion> getVersionHistory(String pluginId) {
        var history = versionHistory.get(pluginId);
        return history != null ? Collections.unmodifiableList(history) : List.of();
    }

    public Optional<PluginVersion> getLatestVersion(String pluginId) {
        return versionHistory.getOrDefault(pluginId, List.of()).stream()
            .max(Comparator.naturalOrder());
    }

    public int compareVersions(String v1, String v2) {
        try {
            var parts1 = v1.split("\\.");
            var parts2 = v2.split("\\.");
            for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
                int cmp = Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
                if (cmp != 0) return cmp;
            }
            return Integer.compare(parts1.length, parts2.length);
        } catch (NumberFormatException e) {
            log.warn("Invalid version format: {} vs {}", v1, v2);
            return 0;
        }
    }

    public String getPlatformVersion() { return PLATFORM_VERSION; }
}
