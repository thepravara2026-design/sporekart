package com.sporekart.ai.providers.registry.versioning;

public record SemanticVersion(
    int major,
    int minor,
    int patch,
    String preRelease,
    String buildMetadata
) implements Comparable<SemanticVersion> {

    public static SemanticVersion parse(String version) {
        var parts = version.split("\\+", 2);
        var core = parts[0].split("-", 2);
        var semver = core[0].split("\\.");
        var preRelease = core.length > 1 ? core[1] : "";
        var build = parts.length > 1 ? parts[1] : "";
        return new SemanticVersion(
            Integer.parseInt(semver[0]),
            Integer.parseInt(semver[1]),
            Integer.parseInt(semver[2]),
            preRelease,
            build
        );
    }

    @Override
    public int compareTo(SemanticVersion other) {
        if (major != other.major) return Integer.compare(major, other.major);
        if (minor != other.minor) return Integer.compare(minor, other.minor);
        if (patch != other.patch) return Integer.compare(patch, other.patch);
        if (!preRelease.isEmpty() && other.preRelease.isEmpty()) return -1;
        if (preRelease.isEmpty() && !other.preRelease.isEmpty()) return 1;
        return preRelease.compareTo(other.preRelease);
    }

    public boolean isStable() {
        return preRelease.isEmpty();
    }

    public boolean isPreRelease() {
        return !preRelease.isEmpty();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(major).append('.').append(minor).append('.').append(patch);
        if (!preRelease.isEmpty()) sb.append('-').append(preRelease);
        if (!buildMetadata.isEmpty()) sb.append('+').append(buildMetadata);
        return sb.toString();
    }
}
