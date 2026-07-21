package com.sporekart.ai.providers.factory;

import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.ProviderConfiguration;

import java.util.List;
import java.util.Optional;

public interface DependencyResolver {
    <T> Optional<T> resolve(Class<T> dependencyType);
    <T> Optional<T> resolve(String qualifier, Class<T> dependencyType);
    boolean isSatisfied(ProviderConfiguration configuration);
    List<String> getMissingDependencies(ProviderConfiguration configuration);
}
