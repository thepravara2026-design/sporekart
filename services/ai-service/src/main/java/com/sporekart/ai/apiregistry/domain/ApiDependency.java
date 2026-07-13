package com.sporekart.ai.apiregistry.domain;

public record ApiDependency(
        String dependencyApiId,
        String dependencyName,
        ApiDependency.DependencyType dependencyType) {

    public enum DependencyType {
        INTERNAL,
        EXTERNAL
    }
}
