package com.sporekart.ai.providers.registry.health;

public record HealthScore(
    double score,
    HealthLevel level
) {
    public enum HealthLevel {
        CRITICAL(0.0, 0.2),
        DEGRADED(0.2, 0.5),
        STABLE(0.5, 0.8),
        HEALTHY(0.8, 1.0);

        private final double min;
        private final double max;

        HealthLevel(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public static HealthLevel fromScore(double score) {
            for (var level : values()) {
                if (score >= level.min && score < level.max) {
                    return level;
                }
            }
            return HEALTHY;
        }
    }
}
