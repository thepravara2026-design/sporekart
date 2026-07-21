package com.sporekart.ai.providers.registry.versioning;

import java.util.List;

public record VersionCompatibility(
    String providerId,
    String currentVersion,
    List<String> compatibleVersions,
    String latestVersion,
    String stableVersion,
    String experimentalVersion,
    List<String> deprecatedVersions,
    List<UpgradePath> upgradePaths,
    List<UpgradePath> rollbackPaths
) {
    public record UpgradePath(
        String fromVersion,
        String toVersion,
        boolean backwardCompatible,
        List<String> breakingChanges
    ) {}
}
