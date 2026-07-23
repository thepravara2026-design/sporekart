package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PluginVersionTest {

    @Test
    void shouldConstructWithAllFields() {
        var v = new PluginVersion("1.2.3", "1.0.0", "2.0.0", 85);
        assertThat(v.version()).isEqualTo("1.2.3");
        assertThat(v.minPlatformVersion()).isEqualTo("1.0.0");
        assertThat(v.maxPlatformVersion()).isEqualTo("2.0.0");
        assertThat(v.compatibilityScore()).isEqualTo(85);
    }

    @Test
    void shouldBeEqualForSameValues() {
        var v1 = new PluginVersion("1.0", "1", "2", 50);
        var v2 = new PluginVersion("1.0", "1", "2", 50);
        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void shouldCompareVersionsCorrectly() {
        var v1 = new PluginVersion("1.0.0", "1", "2", 0);
        var v2 = new PluginVersion("2.0.0", "1", "2", 0);
        assertThat(v1.compareTo(v2)).isNegative();
        assertThat(v2.compareTo(v1)).isPositive();
    }

    @Test
    void shouldConsiderEqualVersionsAsEqual() {
        var v1 = new PluginVersion("1.0.0", "1", "2", 0);
        var v2 = new PluginVersion("1.0.0", "2", "3", 0);
        assertThat(v1.compareTo(v2)).isZero();
    }

    @Test
    void shouldCompareMultiDigitVersions() {
        var v1 = new PluginVersion("1.10.0", "1", "2", 0);
        var v2 = new PluginVersion("1.2.0", "1", "2", 0);
        assertThat(v1.compareTo(v2)).isPositive();
    }

    @Test
    void shouldCompareVersionsWithDifferentLengths() {
        var v1 = new PluginVersion("1.0", "1", "2", 0);
        var v2 = new PluginVersion("1.0.0", "1", "2", 0);
        assertThat(v1.compareTo(v2)).isNegative();
    }

    @Test
    void shouldReturnToString() {
        var v = new PluginVersion("1.0.0", "1", "2", 80);
        assertThat(v.toString()).contains("1.0.0");
    }
}
