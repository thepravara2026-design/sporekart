package com.sporekart.ai.prompt.domain;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.entity.PromptTemplate;
import com.sporekart.ai.prompt.domain.entity.PromptVersion;
import com.sporekart.ai.prompt.domain.exception.*;
import com.sporekart.ai.prompt.domain.factory.PromptFactory;
import com.sporekart.ai.prompt.domain.valueobject.*;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PromptAggregateTest {

    private PromptFactory factory;
    private PromptMetadata metadata;
    private PromptTemplate template;
    private PromptExecutionPolicy policy;

    @BeforeEach
    void setUp() {
        factory = new PromptFactory();
        metadata = PromptMetadata.builder()
            .createdBy("user-1")
            .businessUnit("Engineering")
            .department("AI")
            .build();
        template = new PromptTemplate(PromptType.DYNAMIC, "Answer the following: {{question}}",
            List.of(new TemplateSection(SectionType.INSTRUCTION, "Answer the following: {{question}}", 0)),
            List.of(new PromptVariable("question", VariableType.STRING, true, null,
                VariableValidation.none(), "The user question", "What is AI?", false,
                PromptVisibility.PUBLIC, false)),
            PromptExecutionPolicy.defaults());
        policy = PromptExecutionPolicy.defaults();
    }

    // ========= AGGREGATE CREATION =========

    @Test
    @Order(1)
    void shouldCreatePromptAggregate() {
        var prompt = factory.createWithVersion("Test Prompt", "A test prompt",
            PromptCategory.CHAT, PromptType.DYNAMIC, "owner-1",
            PromptVisibility.PUBLIC, PromptScope.GLOBAL, PromptPriority.MEDIUM,
            "creator-1", metadata, template);

        assertThat(prompt.id()).isNotNull();
        assertThat(prompt.name()).isEqualTo("Test Prompt");
        assertThat(prompt.status()).isEqualTo(PromptStatus.DRAFT);
        assertThat(prompt.currentVersion()).isPresent();
        assertThat(prompt.auditTrail()).isNotEmpty();
    }

    @Test
    @Order(2)
    void shouldGenerateUniquePromptIds() {
        var p1 = factory.createWithVersion("P1", "", PromptCategory.CHAT, PromptType.STATIC,
            "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL, PromptPriority.LOW,
            "creator", metadata, template);
        var p2 = factory.createWithVersion("P2", "", PromptCategory.CHAT, PromptType.STATIC,
            "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL, PromptPriority.LOW,
            "creator", metadata, template);

        assertThat(p1.id()).isNotEqualTo(p2.id());
    }

    @Test
    @Order(3)
    void shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class, () ->
            factory.createWithVersion("", "", PromptCategory.CHAT, PromptType.STATIC,
                "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL, PromptPriority.LOW,
                "creator", metadata, template));
    }

    @Test
    @Order(4)
    void shouldRejectNullCategory() {
        assertThrows(NullPointerException.class, () ->
            new Prompt(PromptId.generate(), "name", "", null, PromptType.STATIC,
                "owner", PromptStatus.DRAFT, PromptVisibility.PUBLIC, PromptScope.GLOBAL,
                PromptPriority.LOW, "creator", metadata, policy, Set.of(), Set.of(),
                null, null, null));
    }

    // ========= VERSION MANAGEMENT =========

    @Test
    @Order(5)
    void shouldCreateVersion() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var v2 = prompt.createNextVersion(template, "Bug fix", "creator-1");

        assertThat(v2.version().toString()).isEqualTo("1.0.1");
        assertThat(prompt.allVersions()).hasSize(2);
    }

    @Test
    @Order(6)
    void shouldRejectDuplicateVersion() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(DuplicateVersionException.class, () ->
            prompt.createVersion(new SemanticVersion(1, 0, 0), template, "dup", "creator"));
    }

    @Test
    @Order(7)
    void shouldRejectVersionDowngrade() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(IllegalArgumentException.class, () ->
            prompt.createVersion(new SemanticVersion(0, 9, 0), template, "downgrade", "creator"));
    }

    @Test
    @Order(8)
    void initialVersionShouldBeOneZeroZero() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var initial = prompt.currentVersion().orElseThrow();
        assertThat(initial.version().toString()).isEqualTo("1.0.0");
        assertThat(initial.isDraft()).isTrue();
    }

    // ========= LIFECYCLE STATE MACHINE =========

    @Test
    @Order(9)
    void shouldTransitionFromDraftToReview() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        assertThat(prompt.status()).isEqualTo(PromptStatus.REVIEW);
    }

    @Test
    @Order(10)
    void shouldTransitionFromReviewToApproved() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "Looks good");
        assertThat(prompt.status()).isEqualTo(PromptStatus.APPROVED);
    }

    @Test
    @Order(11)
    void shouldTransitionFromApprovedToPublished() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "Approved");
        prompt.publish("admin-1");
        assertThat(prompt.status()).isEqualTo(PromptStatus.PUBLISHED);
    }

    @Test
    @Order(12)
    void shouldBlockIllegalTransition() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(InvalidStatusTransitionException.class, () ->
            prompt.transitionTo(PromptStatus.PUBLISHED, "creator-1", "direct publish"));
    }

    @Test
    @Order(13)
    void shouldTransitionFromDraftToArchived() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.archive("admin-1", "Abandoned");
        assertThat(prompt.status()).isEqualTo(PromptStatus.ARCHIVED);
    }

    @Test
    @Order(14)
    void shouldTransitionThroughFullLifecycle() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        assertThat(prompt.status()).isEqualTo(PromptStatus.REVIEW);

        prompt.approve("reviewer-1", "Approved");
        assertThat(prompt.status()).isEqualTo(PromptStatus.APPROVED);

        prompt.publish("admin-1");
        assertThat(prompt.status()).isEqualTo(PromptStatus.PUBLISHED);

        prompt.deprecate("admin-1", "Superseded by v2");
        assertThat(prompt.status()).isEqualTo(PromptStatus.DEPRECATED);

        prompt.archive("admin-1", "End of life");
        assertThat(prompt.status()).isEqualTo(PromptStatus.ARCHIVED);
    }

    @Test
    @Order(15)
    void shouldAllowReversionFromArchivedToDraft() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.archive("admin-1", "Archived");
        prompt.transitionTo(PromptStatus.DRAFT, "admin-1", "Restored");
        assertThat(prompt.status()).isEqualTo(PromptStatus.DRAFT);
    }

    // ========= VARAIBLE MANAGEMENT =========

    @Test
    @Order(16)
    void shouldAddVariable() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var newVar = new PromptVariable("context", VariableType.STRING, false, "default",
            null, "Context info", null, true, PromptVisibility.INTERNAL, false);
        prompt.addVariable(newVar, "creator-1");

        assertThat(prompt.currentVersion().orElseThrow().template().variables()).hasSize(2);
    }

    @Test
    @Order(17)
    void shouldNotAddDuplicateVariable() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var dup = new PromptVariable("question", VariableType.STRING, false, null,
            null, "", null, false, PromptVisibility.PUBLIC, false);
        assertThrows(IllegalArgumentException.class, () ->
            prompt.addVariable(dup, "creator-1"));
    }

    @Test
    @Order(18)
    void shouldRemoveVariable() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.removeVariable("question", "creator-1");
        assertThat(prompt.currentVersion().orElseThrow().template().variables()).isEmpty();
    }

    @Test
    @Order(19)
    void shouldNotRemoveRequiredVariable() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(IllegalArgumentException.class, () ->
            prompt.removeVariable("question", "creator-1"));
    }

    // ========= METADATA MANAGEMENT =========

    @Test
    @Order(20)
    void shouldUpdateMetadata() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var newMetadata = PromptMetadata.builder()
            .createdBy("user-1")
            .owner("new-owner")
            .businessUnit("Marketing")
            .build();
        prompt.updateMetadata(newMetadata, "admin-1");

        assertThat(prompt.metadata().businessUnit()).isEqualTo("Marketing");
    }

    @Test
    @Order(21)
    void shouldUpdateExecutionPolicy() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var newPolicy = PromptExecutionPolicy.builder()
            .maxTokens(4096)
            .temperature(0.5)
            .streamingEnabled(true)
            .build();
        prompt.updateExecutionPolicy(newPolicy, "admin-1");

        assertThat(prompt.executionPolicy().maxTokens()).isEqualTo(4096);
        assertThat(prompt.executionPolicy().temperature()).isEqualTo(0.5);
        assertThat(prompt.executionPolicy().streamingEnabled()).isTrue();
    }

    // ========= OWNERSHIP =========

    @Test
    @Order(22)
    void shouldChangeOwner() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner-1", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.changeOwner("owner-2", "admin-1");
        assertThat(prompt.owner()).isEqualTo("owner-2");
    }

    @Test
    @Order(23)
    void shouldVerifyOwnership() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner-1", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThat(prompt.isOwnedBy("owner-1")).isTrue();
        assertThat(prompt.isOwnedBy("stranger")).isFalse();
    }

    // ========= AUDIT TRAIL =========

    @Test
    @Order(24)
    void shouldRecordAuditEntries() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.changeOwner("new-owner", "admin-1");

        assertThat(prompt.auditTrail()).hasSizeGreaterThanOrEqualTo(4);
        assertThat(prompt.auditTrail()).anyMatch(a ->
            a.action() == AuditAction.OWNER_CHANGED);
        assertThat(prompt.auditTrail()).anyMatch(a ->
            a.action() == AuditAction.STATUS_CHANGED);
    }

    // ========= DOMAIN EVENTS =========

    @Test
    @Order(25)
    void shouldGenerateDomainEvents() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "ok");
        prompt.publish("admin-1");

        var events = prompt.pendingEvents();
        assertThat(events).isNotEmpty();
        assertThat(events).anyMatch(e ->
            e.eventType().equals("PROMPT_PUBLISHED"));
    }

    // ========= BUSINESS RULES =========

    @Test
    @Order(26)
    void shouldNotPublishWithoutApproval() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(InvalidStatusTransitionException.class, () ->
            prompt.publish("admin-1"));
    }

    @Test
    @Order(27)
    void shouldNotPublishWithoutVersion() {
        var prompt = new Prompt(PromptId.generate(), "Test", "", PromptCategory.CHAT,
            PromptType.STATIC, "owner", PromptStatus.APPROVED, PromptVisibility.PUBLIC,
            PromptScope.GLOBAL, PromptPriority.LOW, "creator", metadata, policy,
            Set.of(), Set.of(), null, null, null);

        assertThrows(IllegalStateException.class, () ->
            prompt.publish("admin-1"));
    }

    @Test
    @Order(28)
    void publishedVersionShouldBeImmutable() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "ok");
        prompt.publish("admin-1");

        var publishedVersion = prompt.activeVersion();
        assertThat(publishedVersion.isImmutable()).isTrue();
    }

    @Test
    @Order(29)
    void shouldDeprecatePublishedVersion() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "ok");
        prompt.publish("admin-1");
        prompt.deprecate("admin-1", "Replaced by v2");

        assertThat(prompt.status()).isEqualTo(PromptStatus.DEPRECATED);
        assertThat(prompt.activeVersion().isDeprecated()).isTrue();
    }

    // ========= EDGE CASES =========

    @Test
    @Order(30)
    void shouldHandleMultipleVersions() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.createNextVersion(template, "v1.0.1", "dev");
        prompt.createNextVersion(template, "v1.0.2", "dev");
        prompt.createNextVersion(template, "v1.0.3", "dev");

        assertThat(prompt.allVersions()).hasSize(4);
    }

    @Test
    @Order(31)
    void shouldFindVersionBySemanticVersion() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        var v2 = prompt.createNextVersion(template, "v1.0.1", "dev");
        var found = prompt.version(v2.version());

        assertThat(found).isPresent();
        assertThat(found.get().version().toString()).isEqualTo("1.0.1");
    }

    @Test
    @Order(32)
    void shouldSupportTagsAndLabels() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.addTag("critical");
        prompt.addTag("customer-facing");
        prompt.putLabel("team", "AI Squad");

        assertThat(prompt.tags()).contains("critical", "customer-facing");
        assertThat(prompt.labels()).containsEntry("team", "AI Squad");
    }

    @Test
    @Order(33)
    void shouldTrackDependencies() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThat(prompt.hasDependency("nonexistent")).isFalse();
    }

    @Test
    @Order(34)
    void shouldRejectInvalidTemplate() {
        var invalidTemplate = new PromptTemplate(PromptType.STATIC, "",
            List.of(), List.of(), PromptExecutionPolicy.defaults());

        var prompt = new Prompt(PromptId.generate(), "Test", "", PromptCategory.CHAT,
            PromptType.STATIC, "owner", PromptStatus.DRAFT, PromptVisibility.PUBLIC,
            PromptScope.GLOBAL, PromptPriority.LOW, "creator", metadata, policy,
            Set.of(), Set.of(), null, null, null);

        assertThrows(com.sporekart.ai.prompt.domain.exception.InvalidTemplateException.class,
            () -> prompt.createInitialVersion(invalidTemplate, "creator"));
    }

    @Test
    @Order(35)
    void shouldReviewThenRevertToDraft() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        assertThat(prompt.status()).isEqualTo(PromptStatus.REVIEW);

        prompt.transitionTo(PromptStatus.DRAFT, "creator-1", "Needs more work");
        assertThat(prompt.status()).isEqualTo(PromptStatus.DRAFT);
    }

    @Test
    @Order(36)
    void shouldApproveThenRevertToDraft() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.submitForReview("creator-1");
        prompt.approve("reviewer-1", "ok");
        prompt.transitionTo(PromptStatus.DRAFT, "admin-1", "Revisions needed");

        assertThat(prompt.status()).isEqualTo(PromptStatus.DRAFT);
    }

    @Test
    @Order(37)
    void shouldUpdatePromptName() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        prompt.updateName("New Name", "admin-1");
        assertThat(prompt.name()).isEqualTo("New Name");
    }

    @Test
    @Order(38)
    void shouldRejectNameLongerThan200Chars() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThrows(IllegalArgumentException.class, () ->
            prompt.updateName("x".repeat(201), "admin-1"));
    }

    @Test
    @Order(39)
    void shouldClearDomainEvents() {
        var prompt = factory.createWithVersion("Test", "", PromptCategory.CHAT,
            PromptType.DYNAMIC, "owner", PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, template);

        assertThat(prompt.pendingEvents()).isNotEmpty();
        prompt.clearEvents();
        assertThat(prompt.pendingEvents()).isEmpty();
    }

    @Test
    @Order(40)
    void shouldCompareEqualityById() {
        var id = PromptId.generate();
        var p1 = new Prompt(id, "A", "", PromptCategory.CHAT, PromptType.STATIC,
            "owner", PromptStatus.DRAFT, PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, policy, Set.of(), Set.of(),
            null, null, null);
        var p2 = new Prompt(id, "B", "", PromptCategory.CHAT, PromptType.STATIC,
            "owner", PromptStatus.DRAFT, PromptVisibility.PUBLIC, PromptScope.GLOBAL,
            PromptPriority.LOW, "creator", metadata, policy, Set.of(), Set.of(),
            null, null, null);

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}
