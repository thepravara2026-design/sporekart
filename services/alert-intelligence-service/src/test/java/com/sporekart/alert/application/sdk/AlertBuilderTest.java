package com.sporekart.alert.application.sdk;

import com.sporekart.alert.domain.model.AlertCategory;
import com.sporekart.alert.domain.model.AlertSeverity;
import com.sporekart.alert.domain.model.AlertPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertBuilderTest {

    @Test
    void buildShouldReturnAlertWithGivenValues() {
        var alert = AlertBuilder.builder()
                .withTitle("Test Alert")
                .withDescription("Desc")
                .withCategory(AlertCategory.SECURITY)
                .withSeverity(AlertSeverity.CRITICAL)
                .withPriority(AlertPriority.P1)
                .withDomain("security")
                .build();
        assertEquals("Test Alert", alert.title());
        assertEquals(AlertCategory.SECURITY, alert.category());
        assertEquals(AlertSeverity.CRITICAL, alert.severity());
        assertEquals(AlertPriority.P1, alert.priority());
        assertEquals("security", alert.domain());
    }

    @Test
    void buildShouldThrowWhenTitleMissing() {
        assertThrows(IllegalStateException.class, () ->
                AlertBuilder.builder().withDomain("inv").build());
    }

    @Test
    void buildShouldThrowWhenDomainMissing() {
        assertThrows(IllegalStateException.class, () ->
                AlertBuilder.builder().withTitle("Test").build());
    }

    @Test
    void addMetricShouldIncludeInAlert() {
        var alert = AlertBuilder.builder()
                .withTitle("Test").withDomain("inv")
                .addMetric("env", "prod")
                .addMetric("region", "us-east")
                .build();
        assertEquals("prod", alert.supportingMetrics().get("env"));
        assertEquals("us-east", alert.supportingMetrics().get("region"));
    }
}
