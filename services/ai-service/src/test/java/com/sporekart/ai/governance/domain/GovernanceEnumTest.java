package com.sporekart.ai.governance.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceEnumTest {

    @Test
    void testGovernanceScopeValues() {
        assertEquals(7, GovernanceScope.values().length);
        assertEquals("REQUEST", GovernanceScope.REQUEST.name());
        assertEquals("RESPONSE", GovernanceScope.RESPONSE.name());
        assertEquals("CONTEXT", GovernanceScope.CONTEXT.name());
        assertEquals("CONFIGURATION", GovernanceScope.CONFIGURATION.name());
        assertEquals("AUDIT", GovernanceScope.AUDIT.name());
        assertEquals("METRICS", GovernanceScope.METRICS.name());
        assertEquals("ALL", GovernanceScope.ALL.name());
    }

    @Test
    void testGovernanceDecisionValues() {
        assertEquals(5, GovernanceDecision.values().length);
        assertEquals("ALLOW", GovernanceDecision.ALLOW.name());
        assertEquals("DENY", GovernanceDecision.DENY.name());
        assertEquals("REVIEW", GovernanceDecision.REVIEW.name());
        assertEquals("LOG", GovernanceDecision.LOG.name());
        assertEquals("BYPASS", GovernanceDecision.BYPASS.name());
    }

    @Test
    void testGovernanceStatusValues() {
        assertEquals(5, GovernanceStatus.values().length);
        assertEquals("ACTIVE", GovernanceStatus.ACTIVE.name());
        assertEquals("INACTIVE", GovernanceStatus.INACTIVE.name());
        assertEquals("DRAFT", GovernanceStatus.DRAFT.name());
        assertEquals("ARCHIVED", GovernanceStatus.ARCHIVED.name());
        assertEquals("DEPRECATED", GovernanceStatus.DEPRECATED.name());
    }

    @Test
    void testGovernanceSeverityValues() {
        assertEquals(4, GovernanceSeverity.values().length);
        assertEquals("INFO", GovernanceSeverity.INFO.name());
        assertEquals("WARNING", GovernanceSeverity.WARNING.name());
        assertEquals("ERROR", GovernanceSeverity.ERROR.name());
        assertEquals("CRITICAL", GovernanceSeverity.CRITICAL.name());
    }

    @Test
    void testGovernanceModeValues() {
        assertEquals(5, GovernanceMode.values().length);
        assertEquals("DEVELOPMENT", GovernanceMode.DEVELOPMENT.name());
        assertEquals("TESTING", GovernanceMode.TESTING.name());
        assertEquals("PRODUCTION", GovernanceMode.PRODUCTION.name());
        assertEquals("AUDIT_ONLY", GovernanceMode.AUDIT_ONLY.name());
        assertEquals("ENFORCE", GovernanceMode.ENFORCE.name());
    }

    @Test
    void testValueOf() {
        assertNotNull(GovernanceScope.valueOf("ALL"));
        assertNotNull(GovernanceDecision.valueOf("ALLOW"));
        assertNotNull(GovernanceStatus.valueOf("ACTIVE"));
        assertNotNull(GovernanceSeverity.valueOf("CRITICAL"));
        assertNotNull(GovernanceMode.valueOf("PRODUCTION"));
    }
}
