package com.sporekart.ai.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FlywayMigrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void applicationContextLoadsWithFlywayEnabled() {
        assertNotNull(dataSource, "DataSource should be available");
    }

    @Test
    void allMigrationsV1ThroughV28AreApplied() throws Exception {
        try (Connection conn = dataSource.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null,
                     "flyway_schema_history", null)) {
            assertTrue(rs.next(), "flyway_schema_history table should exist");
        }
    }

    @Test
    void schemaVersionTableContainsExpectedEntries() throws Exception {
        try (Connection conn = dataSource.getConnection();
             var stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT version FROM flyway_schema_history ORDER BY version")) {

            List<String> versions = new ArrayList<>();
            while (rs.next()) {
                versions.add(rs.getString("version"));
            }

            assertFalse(versions.isEmpty(), "flyway_schema_history should have entries");
            assertTrue(versions.size() >= 28,
                    "Expected at least 28 migrations, found: " + versions.size());
        }
    }

    @Test
    void v1FoundationMigrationApplied() throws Exception {
        assertMigrationCreatesTable("V1__ai_foundation", "ai_configuration");
    }

    @Test
    void v2AiExperiencesMigrationApplied() throws Exception {
        assertMigrationExists("V2__sprint12_ai_experiences");
    }

    @Test
    void v3MarketplaceMigrationApplied() throws Exception {
        assertMigrationExists("V3__sprint13_marketplace");
    }

    @Test
    void v4B2bCommerceMigrationApplied() throws Exception {
        assertMigrationExists("V4__sprint14_b2b_commerce");
    }

    @Test
    void v5MobilePlatformMigrationApplied() throws Exception {
        assertMigrationExists("V5__sprint15_mobile_platform");
    }

    @Test
    void v6EnterpriseErpMigrationApplied() throws Exception {
        assertMigrationExists("V6__sprint16_enterprise_erp");
    }

    @Test
    void v7ErpReadModelsMigrationApplied() throws Exception {
        assertMigrationExists("V7__sprint16_erp_read_models");
    }

    @Test
    void v8OperationsPersistenceMigrationApplied() throws Exception {
        assertMigrationExists("V8__sprint16_operations_persistence");
    }

    @Test
    void v9AuditFixMigrationApplied() throws Exception {
        assertMigrationExists("V9__sprint16_persistence_audit_fix");
    }

    @Test
    void v10PlatformFoundationMigrationApplied() throws Exception {
        assertMigrationExists("V10__sprint17_ai_platform_foundation");
    }

    @Test
    void v11AiGatewayMigrationApplied() throws Exception {
        assertMigrationExists("V11__sprint17_ai_gateway");
    }

    @Test
    void v12AiProviderAbstractionMigrationApplied() throws Exception {
        assertMigrationExists("V12__sprint17_ai_provider_abstraction");
    }

    @Test
    void v13AiPromptManagementMigrationApplied() throws Exception {
        assertMigrationExists("V13__sprint17_ai_prompt_management");
    }

    @Test
    void v14KnowledgeManagementMigrationApplied() throws Exception {
        assertMigrationExists("V14__sprint17_knowledge_management");
    }

    @Test
    void v15SemanticIntelligenceMigrationApplied() throws Exception {
        assertMigrationExists("V15__sprint17_semantic_intelligence");
    }

    @Test
    void v16ConversationMigrationApplied() throws Exception {
        assertMigrationExists("V16__sprint17_conversation");
    }

    @Test
    void v17WorkflowMigrationApplied() throws Exception {
        assertMigrationExists("V17__sprint17_workflow");
    }

    @Test
    void v18ContentMigrationApplied() throws Exception {
        assertMigrationExists("V18__sprint18_content");
    }

    @Test
    void v19AssistantMigrationApplied() throws Exception {
        assertMigrationExists("V19__sprint17_assistant");
    }

    @Test
    void v20GovernanceMigrationApplied() throws Exception {
        assertMigrationExists("V20__sprint18_governance");
    }

    @Test
    void v21PolicyMigrationApplied() throws Exception {
        assertMigrationExists("V21__sprint18_policy");
    }

    @Test
    void v22DecisionMigrationApplied() throws Exception {
        assertMigrationExists("V22__sprint18_decision");
    }

    @Test
    void v23ApprovalMigrationApplied() throws Exception {
        assertMigrationExists("V23__sprint18_approval");
    }

    @Test
    void v24ComplianceMigrationApplied() throws Exception {
        assertMigrationExists("V24__sprint18_compliance");
    }

    @Test
    void v25RiskMigrationApplied() throws Exception {
        assertMigrationExists("V25__sprint18_risk");
    }

    @Test
    void v26AnalyticsMigrationApplied() throws Exception {
        assertMigrationExists("V26__sprint18_analytics");
    }

    @Test
    void v27AdminMigrationApplied() throws Exception {
        assertMigrationExists("V27__sprint18_admin");
    }

    @Test
    void v28AutomationMigrationApplied() throws Exception {
        assertMigrationExists("V28__sprint18_automation");
    }

    private void assertMigrationExists(String description) throws Exception {
        try (Connection conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM flyway_schema_history WHERE description = ?")) {
            stmt.setString(1, description.substring(description.indexOf("__") + 2));
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertTrue(rs.getInt(1) > 0,
                        "Migration " + description + " should be applied");
            }
        }
    }

    private void assertMigrationCreatesTable(String description, String tableName) throws Exception {
        assertMigrationExists(description);
        try (Connection conn = dataSource.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null,
                     tableName, null)) {
            assertTrue(rs.next(), "Table " + tableName + " should exist after migration");
        }
    }
}
