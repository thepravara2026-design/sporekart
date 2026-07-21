package com.sporekart.ai.prompt.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public record SemanticVersion(int major, int minor, int patch, String preRelease, String buildMetadata) {
    private static final Pattern VERSION_PATTERN =
        Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:-([\\w.]+))?(?:\\+([\\w.]+))?$");

    public SemanticVersion {
        if (major < 0) throw new IllegalArgumentException("Major version must be non-negative");
        if (minor < 0) throw new IllegalArgumentException("Minor version must be non-negative");
        if (patch < 0) throw new IllegalArgumentException("Patch version must be non-negative");
    }

    public SemanticVersion(int major, int minor, int patch) {
        this(major, minor, patch, null, null);
    }

    public static SemanticVersion parse(String version) {
        Objects.requireNonNull(version, "Version string must not be null");
        var matcher = VERSION_PATTERN.matcher(version.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid semantic version: " + version);
        }
        return new SemanticVersion(
            Integer.parseInt(matcher.group(1)),
            Integer.parseInt(matcher.group(2)),
            Integer.parseInt(matcher.group(3)),
            matcher.group(4),
            matcher.group(5)
        );
    }

    public static SemanticVersion initial() {
        return new SemanticVersion(1, 0, 0);
    }

    public SemanticVersion nextMajor() {
        return new SemanticVersion(major + 1, 0, 0);
    }

    public SemanticVersion nextMinor() {
        return new SemanticVersion(major, minor + 1, 0);
    }

    public SemanticVersion nextPatch() {
        return new SemanticVersion(major, minor, patch + 1);
    }

    public boolean isPreRelease() {
        return preRelease != null && !preRelease.isEmpty();
    }

    public boolean isStable() {
        return !isPreRelease();
    }

    public int compareTo(SemanticVersion other) {
        if (this.major != other.major) return Integer.compare(this.major, other.major);
        if (this.minor != other.minor) return Integer.compare(this.minor, other.minor);
        if (this.patch != other.patch) return Integer.compare(this.patch, other.patch);
        if (this.isPreRelease() != other.isPreRelease()) return this.isPreRelease() ? -1 : 1;
        return 0;
    }

    public boolean isDowngradeFrom(SemanticVersion other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(major).append('.').append(minor).append('.').append(patch);
        if (preRelease != null && !preRelease.isEmpty()) sb.append('-').append(preRelease);
        if (buildMetadata != null && !buildMetadata.isEmpty()) sb.append('+').append(buildMetadata);
        return sb.toString();
    }
}
