package com.sporekart.ai.provider.selectors;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderCapabilities;
import com.sporekart.ai.provider.registry.ProviderRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProviderSelector {
    private final ProviderRegistry registry;
    private final Map<String, SelectionStrategy> strategies = new LinkedHashMap<>();
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

    public ProviderSelector(ProviderRegistry registry) {
        this.registry = registry;
        registerDefaultStrategies();
    }

    public void registerStrategy(SelectionStrategy strategy) {
        strategies.put(strategy.name(), strategy);
    }

    public Optional<AIProvider> select(String strategyName, SelectionContext context) {
        var strategy = strategies.get(strategyName);
        if (strategy == null) {
            strategy = strategies.get("first-available");
        }

        var candidates = resolveCandidates(context);
        return strategy.select(candidates, context);
    }

    public Optional<AIProvider> select(SelectionContext context) {
        if (context.preferredProvider().isPresent()) {
            var preferred = context.preferredProvider().get();
            var byName = registry.lookupByType(preferred);
            if (byName.isPresent() && byName.get().isAvailable()) {
                return byName;
            }
        }
        return select("first-available", context);
    }

    public Optional<AIProvider> selectFallback(AIProvider failedProvider, SelectionContext context) {
        var candidates = registry.getAllProviders().stream()
                .filter(p -> !p.providerId().equals(failedProvider.providerId()))
                .filter(AIProvider::isAvailable)
                .collect(Collectors.toList());

        if (candidates.isEmpty()) return Optional.empty();

        var excluded = new ArrayList<>(context.routingHints().keySet());
        return candidates.stream()
                .filter(p -> !excluded.contains(p.providerId()))
                .findFirst();
    }

    public List<SelectionStrategy> getRegisteredStrategies() {
        return List.copyOf(strategies.values());
    }

    private List<AIProvider> resolveCandidates(SelectionContext context) {
        return registry.getAvailableProviders();
    }

    private void registerDefaultStrategies() {
        strategies.put("first-available", new FirstAvailableStrategy());
        strategies.put("lowest-latency", new LowestLatencyStrategy(registry));
        strategies.put("cheapest", new CheapestStrategy());
        strategies.put("preferred", new PreferredStrategy());
        strategies.put("random", new RandomStrategy());
        strategies.put("weighted", new WeightedStrategy());
        strategies.put("round-robin", new RoundRobinStrategy(roundRobinCounter));

        strategies.put("manual-override", (providers, context) -> {
            if (context.preferredProvider().isPresent()) {
                var preferred = context.preferredProvider().get();
                return providers.stream()
                        .filter(p -> p.providerName().equalsIgnoreCase(preferred)
                                || p.providerId().equalsIgnoreCase(preferred))
                        .findFirst();
            }
            return Optional.empty();
        });
    }

    private static class FirstAvailableStrategy implements SelectionStrategy {
        @Override
        public String name() { return "first-available"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            return providers.stream().filter(AIProvider::isAvailable).findFirst();
        }
    }

    private static class LowestLatencyStrategy implements SelectionStrategy {
        private final ProviderRegistry registry;

        LowestLatencyStrategy(ProviderRegistry registry) { this.registry = registry; }

        @Override
        public String name() { return "lowest-latency"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            return providers.stream()
                    .filter(AIProvider::isAvailable)
                    .min(Comparator.comparingLong(p -> {
                        var health = registry.getAllProviders().stream()
                                .filter(h -> h.providerId().equals(p.providerId()))
                                .findFirst();
                        return 0;
                    }));
        }
    }

    private static class CheapestStrategy implements SelectionStrategy {
        @Override
        public String name() { return "cheapest"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            return providers.stream()
                    .filter(AIProvider::isAvailable)
                    .min(Comparator.comparingDouble(p -> p.costInfo().costPerInputToken()));
        }
    }

    private static class PreferredStrategy implements SelectionStrategy {
        @Override
        public String name() { return "preferred"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            if (context.preferredProvider().isEmpty()) {
                return providers.stream().filter(AIProvider::isAvailable).findFirst();
            }
            var preferred = context.preferredProvider().get();
            return providers.stream()
                    .filter(p -> p.providerName().equalsIgnoreCase(preferred)
                            || p.providerId().equalsIgnoreCase(preferred))
                    .filter(AIProvider::isAvailable)
                    .findFirst();
        }
    }

    private static class RandomStrategy implements SelectionStrategy {
        private final Random random = new Random();

        @Override
        public String name() { return "random"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            var available = providers.stream().filter(AIProvider::isAvailable).toList();
            if (available.isEmpty()) return Optional.empty();
            return Optional.of(available.get(random.nextInt(available.size())));
        }
    }

    private static class WeightedStrategy implements SelectionStrategy {
        private final Random random = new Random();

        @Override
        public String name() { return "weighted"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            var available = providers.stream().filter(AIProvider::isAvailable).toList();
            if (available.isEmpty()) return Optional.empty();

            int totalWeight = available.stream()
                    .mapToInt(p -> Math.max(1, 100 - (int)(p.costInfo().costPerInputToken() * 100)))
                    .sum();

            int target = random.nextInt(totalWeight);
            int cumulative = 0;
            for (var provider : available) {
                int weight = Math.max(1, 100 - (int)(provider.costInfo().costPerInputToken() * 100));
                cumulative += weight;
                if (target < cumulative) return Optional.of(provider);
            }
            return Optional.of(available.get(available.size() - 1));
        }
    }

    private static class RoundRobinStrategy implements SelectionStrategy {
        private final AtomicInteger counter;

        RoundRobinStrategy(AtomicInteger counter) { this.counter = counter; }

        @Override
        public String name() { return "round-robin"; }

        @Override
        public Optional<AIProvider> select(List<AIProvider> providers, SelectionContext context) {
            var available = providers.stream().filter(AIProvider::isAvailable).toList();
            if (available.isEmpty()) return Optional.empty();
            int index = Math.abs(counter.getAndIncrement()) % available.size();
            return Optional.of(available.get(index));
        }
    }
}
