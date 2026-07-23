package com.sporekart.marketplace.domain;

public record PluginVersion(
    String version,
    String minPlatformVersion,
    String maxPlatformVersion,
    int compatibilityScore
) implements Comparable<PluginVersion> {

    @Override
    public int compareTo(PluginVersion o) {
        return compareVersions(this.version, o.version);
    }

    private int compareVersions(String v1, String v2) {
        var parts1 = v1.split("\\.");
        var parts2 = v2.split("\\.");
        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            int cmp = Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
            if (cmp != 0) return cmp;
        }
        return Integer.compare(parts1.length, parts2.length);
    }
}