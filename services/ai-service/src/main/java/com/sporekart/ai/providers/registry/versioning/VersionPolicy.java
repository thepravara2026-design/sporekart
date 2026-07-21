package com.sporekart.ai.providers.registry.versioning;

public interface VersionPolicy {
    boolean supportsSemanticVersioning();
    boolean requiresPreReleaseApproval();
    boolean allowsExperimentalVersions();
    int maxVersionHistory();
    boolean requiresCompatibilityMatrix();
}
