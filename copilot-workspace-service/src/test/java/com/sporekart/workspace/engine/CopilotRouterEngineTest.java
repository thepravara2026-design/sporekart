package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.CopilotInfo;
import com.sporekart.workspace.domain.RoutedMessage;
import com.sporekart.workspace.domain.RoutingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CopilotRouterEngineTest {

    private CopilotRouterEngine routerEngine;

    @BeforeEach
    void setUp() {
        routerEngine = new CopilotRouterEngine();
    }

    @Test
    void routeMessage_ShouldRouteToCustomer_WhenQueryIsAboutOrder() {
        RoutedMessage result = routerEngine.routeMessage("show my order", List.of());

        assertEquals(CopilotInfo.TYPE_CUSTOMER, result.routedCopilotId());
    }

    @Test
    void routeMessage_ShouldRouteToAdmin_WhenQueryIsAboutRevenueReport() {
        RoutedMessage result = routerEngine.routeMessage("generate revenue report", List.of());

        assertEquals(CopilotInfo.TYPE_ADMIN, result.routedCopilotId());
    }

    @Test
    void routeMessage_ShouldRouteToTrainer_WhenQueryIsAboutStudentAttendance() {
        RoutedMessage result = routerEngine.routeMessage("student attendance", List.of());

        assertEquals(CopilotInfo.TYPE_TRAINER, result.routedCopilotId());
    }

    @Test
    void routeMessage_ShouldRouteToGrower_WhenQueryIsAboutContamination() {
        RoutedMessage result = routerEngine.routeMessage("mushroom bags contaminated", List.of());

        assertEquals(CopilotInfo.TYPE_GROWER, result.routedCopilotId());
    }

    @Test
    void routeMessage_ShouldRouteToHighestConfidence_WhenQueryIsAboutLearningFarming() {
        RoutedMessage result = routerEngine.routeMessage("I want to learn farming", List.of());

        assertThat(result.routedCopilotId()).isIn(CopilotInfo.TYPE_TRAINER, CopilotInfo.TYPE_GROWER);
        assertThat(result.confidence()).isGreaterThan(0.5);
    }

    @Test
    void routeMessage_ShouldFallbackToAdmin_WhenQueryIsUnknown() {
        RoutedMessage result = routerEngine.routeMessage("xyznonsense123 unknown query", List.of());

        assertEquals(CopilotInfo.TYPE_ADMIN, result.routedCopilotId());
    }

    @Test
    void routeMessage_ShouldCalculateConfidenceCorrectly() {
        RoutedMessage result = routerEngine.routeMessage("show my order", List.of());

        assertThat(result.confidence()).isBetween(0.0, 1.0);
    }

    @Test
    void addRoutingRule_ShouldAddRuleSuccessfully() {
        RoutingRule rule = new RoutingRule("rule-1", "order.*", CopilotInfo.TYPE_CUSTOMER, 0.7, 1, List.of(), CopilotInfo.TYPE_ADMIN, false, List.of());
        routerEngine.addRoutingRule(rule);

        List<RoutingRule> rules = routerEngine.getRoutingRules();
        assertThat(rules).contains(rule);
    }

    @Test
    void removeRoutingRule_ShouldRemoveRule() {
        RoutingRule rule = new RoutingRule("rule-1", "order.*", CopilotInfo.TYPE_CUSTOMER, 0.7, 1, List.of(), CopilotInfo.TYPE_ADMIN, false, List.of());
        routerEngine.addRoutingRule(rule);
        routerEngine.removeRoutingRule("rule-1");

        assertThat(routerEngine.getRoutingRules()).doesNotContain(rule);
    }

    @Test
    void getRoutingRules_ShouldReturnAllRules() {
        assertThat(routerEngine.getRoutingRules()).isNotNull();
    }
}
