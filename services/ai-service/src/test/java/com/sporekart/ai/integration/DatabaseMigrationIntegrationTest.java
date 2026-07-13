package com.sporekart.ai.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseMigrationIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testV20GovernanceTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_governance");
        assertTableExists(jdbc, "ai_governance_configuration");
        assertTableExists(jdbc, "ai_governance_registry");
        assertTableExists(jdbc, "ai_governance_scope");
        assertTableExists(jdbc, "ai_governance_audit");
        assertTableExists(jdbc, "ai_governance_metrics");
        assertIndexExists(jdbc, "idx_governance_scope");
        assertIndexExists(jdbc, "idx_governance_status");
        assertIndexExists(jdbc, "idx_governance_created_at");
    }

    @Test
    void testV21PolicyTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_policies");
        assertTableExists(jdbc, "ai_policy_rules");
        assertTableExists(jdbc, "ai_policy_conditions");
        assertTableExists(jdbc, "ai_policy_versions");
        assertTableExists(jdbc, "ai_policy_evaluations");
        assertTableExists(jdbc, "ai_policy_audit");
        assertTableExists(jdbc, "ai_policy_registry");
        assertIndexExists(jdbc, "idx_policies_type");
        assertIndexExists(jdbc, "idx_policies_module");
        assertIndexExists(jdbc, "idx_policy_rules_policy");
    }

    @Test
    void testV22DecisionTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_decisions");
        assertTableExists(jdbc, "ai_decision_rules");
        assertTableExists(jdbc, "ai_decision_audit");
        assertTableExists(jdbc, "ai_decision_explanations");
        assertTableExists(jdbc, "ai_decision_history");
        assertTableExists(jdbc, "ai_decision_registry");
        assertIndexExists(jdbc, "idx_decision_request_id");
        assertIndexExists(jdbc, "idx_decision_action");
        assertIndexExists(jdbc, "idx_decision_status");
    }

    @Test
    void testV23ApprovalTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_approval_requests");
        assertTableExists(jdbc, "ai_approval_workflows");
        assertTableExists(jdbc, "ai_approval_reviewers");
        assertTableExists(jdbc, "ai_approval_assignments");
        assertTableExists(jdbc, "ai_approval_history");
        assertTableExists(jdbc, "ai_approval_comments");
        assertTableExists(jdbc, "ai_approval_audit");
        assertTableExists(jdbc, "ai_approval_escalations");
        assertTableExists(jdbc, "ai_approval_delegations");
        assertIndexExists(jdbc, "idx_approval_requests_user");
        assertIndexExists(jdbc, "idx_approval_requests_status");
        assertIndexExists(jdbc, "idx_approval_requests_module");
    }

    @Test
    void testV24ComplianceTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_compliance_frameworks");
        assertTableExists(jdbc, "ai_compliance_rules");
        assertTableExists(jdbc, "ai_compliance_assessments");
        assertTableExists(jdbc, "ai_compliance_evidence");
        assertTableExists(jdbc, "ai_compliance_reports");
        assertTableExists(jdbc, "ai_compliance_violations");
        assertTableExists(jdbc, "ai_compliance_exceptions");
        assertTableExists(jdbc, "ai_compliance_audit");
        assertIndexExists(jdbc, "idx_compliance_rules_framework");
        assertIndexExists(jdbc, "idx_compliance_rules_active");
        assertIndexExists(jdbc, "idx_compliance_assessments_framework");
    }

    @Test
    void testV25RiskTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_risk_assessments");
        assertTableExists(jdbc, "ai_risk_scores");
        assertTableExists(jdbc, "ai_risk_factors");
        assertTableExists(jdbc, "ai_trust_scores");
        assertTableExists(jdbc, "ai_confidence_scores");
        assertTableExists(jdbc, "ai_risk_history");
        assertTableExists(jdbc, "ai_risk_recommendations");
        assertTableExists(jdbc, "ai_risk_audit");
        assertIndexExists(jdbc, "idx_risk_assessments_module");
        assertIndexExists(jdbc, "idx_risk_assessments_status");
        assertIndexExists(jdbc, "idx_risk_scores_assessment");
    }

    @Test
    void testV26AnalyticsTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_governance_metrics");
        assertTableExists(jdbc, "ai_governance_dashboards");
        assertTableExists(jdbc, "ai_governance_reports");
        assertTableExists(jdbc, "ai_governance_kpis");
        assertTableExists(jdbc, "ai_governance_snapshots");
        assertTableExists(jdbc, "ai_governance_exports");
        assertTableExists(jdbc, "ai_governance_report_schedule");
        assertIndexExists(jdbc, "idx_governance_metrics_module");
        assertIndexExists(jdbc, "idx_governance_metrics_name");
        assertIndexExists(jdbc, "idx_governance_kpis_module");
    }

    @Test
    void testV27AdminTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_admin_configuration");
        assertTableExists(jdbc, "ai_feature_flags");
        assertTableExists(jdbc, "ai_environment_profiles");
        assertTableExists(jdbc, "ai_configuration_versions");
        assertTableExists(jdbc, "ai_configuration_snapshots");
        assertTableExists(jdbc, "ai_admin_audit");
        assertTableExists(jdbc, "ai_admin_operations");
        assertIndexExists(jdbc, "idx_admin_config_key");
        assertIndexExists(jdbc, "idx_admin_config_module");
        assertIndexExists(jdbc, "idx_feature_flags_key");
    }

    @Test
    void testV28AutomationTablesExist() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        assertTableExists(jdbc, "ai_lifecycle_definitions");
        assertTableExists(jdbc, "ai_lifecycle_history");
        assertTableExists(jdbc, "ai_automation_jobs");
        assertTableExists(jdbc, "ai_scheduler_tasks");
        assertTableExists(jdbc, "ai_workflow_history");
        assertTableExists(jdbc, "ai_retry_policies");
        assertTableExists(jdbc, "ai_escalation_policies");
        assertTableExists(jdbc, "ai_expiration_policies");
        assertTableExists(jdbc, "ai_automation_audit");
        assertIndexExists(jdbc, "idx_lifecycle_history_entity");
        assertIndexExists(jdbc, "idx_automation_jobs_status");
        assertIndexExists(jdbc, "idx_automation_jobs_type");
    }

    private void assertTableExists(JdbcTemplate jdbc, String tableName) {
        List<Map<String, Object>> result = jdbc.queryForList(
            "SELECT table_name FROM information_schema.tables WHERE table_name = ?",
            tableName.toLowerCase()
        );
        assertFalse(result.isEmpty(), "Table " + tableName + " should exist");
    }

    private void assertIndexExists(JdbcTemplate jdbc, String indexName) {
        List<Map<String, Object>> result = jdbc.queryForList(
            "SELECT index_name FROM information_schema.indexes WHERE index_name = ?",
            indexName.toLowerCase()
        );
        assertFalse(result.isEmpty(), "Index " + indexName + " should exist");
    }
}
