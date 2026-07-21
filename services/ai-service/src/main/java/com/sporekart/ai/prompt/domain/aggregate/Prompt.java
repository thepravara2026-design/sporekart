package com.sporekart.ai.prompt.domain.aggregate;

import com.sporekart.ai.prompt.domain.entity.PromptTemplate;
import com.sporekart.ai.prompt.domain.entity.PromptVersion;
import com.sporekart.ai.prompt.domain.event.*;
import com.sporekart.ai.prompt.domain.exception.*;
import com.sporekart.ai.prompt.domain.valueobject.*;
import java.time.Instant;
import java.util.*;

public class Prompt {
    private final PromptId id;
    private String name;
    private String description;
    private PromptCategory category;
    private PromptType type;
    private String owner;
    private PromptStatus status;
    private PromptVisibility visibility;
    private PromptScope scope;
    private PromptPriority priority;
    private String createdBy;
    private final Instant createdDate;
    private Instant updatedDate;
    private PromptMetadata metadata;
    private PromptExecutionPolicy executionPolicy;
    private PromptVersion currentVersion;
    private final SortedMap<SemanticVersion, PromptVersion> versions;
    private final Set<String> dependencies;
    private final Set<String> tags;
    private final Map<String, String> labels;
    private final String businessDomain;
    private final String moduleAssociation;
    private final List<PromptAuditEntry> auditTrail;
    private final List<PromptDomainEvent> pendingEvents;

    public Prompt(PromptId id, String name, String description, PromptCategory category,
                  PromptType type, String owner, PromptStatus status, PromptVisibility visibility,
                  PromptScope scope, PromptPriority priority, String createdBy,
                  PromptMetadata metadata, PromptExecutionPolicy executionPolicy,
                  Set<String> dependencies, Set<String> tags, Map<String, String> labels,
                  String businessDomain, String moduleAssociation) {
        this.id = Objects.requireNonNull(id, "PromptId must not be null");
        setName(name);
        this.description = description;
        this.category = Objects.requireNonNull(category, "Category must not be null");
        this.type = Objects.requireNonNull(type, "Type must not be null");
        this.owner = Objects.requireNonNull(owner, "Owner must not be null");
        this.status = Objects.requireNonNull(status, "Status must not be null");
        this.visibility = Objects.requireNonNull(visibility, "Visibility must not be null");
        this.scope = Objects.requireNonNull(scope, "Scope must not be null");
        this.priority = Objects.requireNonNull(priority, "Priority must not be null");
        this.createdBy = Objects.requireNonNull(createdBy, "createdBy must not be null");
        this.createdDate = Instant.now();
        this.updatedDate = this.createdDate;
        this.metadata = Objects.requireNonNull(metadata, "Metadata must not be null");
        this.executionPolicy = Objects.requireNonNull(executionPolicy, "ExecutionPolicy must not be null");
        this.currentVersion = null;
        this.versions = new TreeMap<>((a, b) -> b.compareTo(a));
        this.dependencies = dependencies == null ? new HashSet<>() : new HashSet<>(dependencies);
        this.tags = tags == null ? new HashSet<>() : new HashSet<>(tags);
        this.labels = labels == null ? new HashMap<>() : new HashMap<>(labels);
        this.businessDomain = businessDomain;
        this.moduleAssociation = moduleAssociation;
        this.auditTrail = new ArrayList<>();
        this.pendingEvents = new ArrayList<>();
    }

    public PromptId id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public PromptCategory category() { return category; }
    public PromptType type() { return type; }
    public String owner() { return owner; }
    public PromptStatus status() { return status; }
    public PromptVisibility visibility() { return visibility; }
    public PromptScope scope() { return scope; }
    public PromptPriority priority() { return priority; }
    public String createdBy() { return createdBy; }
    public Instant createdDate() { return createdDate; }
    public Instant updatedDate() { return updatedDate; }
    public PromptMetadata metadata() { return metadata; }
    public PromptExecutionPolicy executionPolicy() { return executionPolicy; }
    public Optional<PromptVersion> currentVersion() { return Optional.ofNullable(currentVersion); }
    public List<PromptVersion> allVersions() { return List.copyOf(versions.values()); }
    public Set<String> dependencies() { return Set.copyOf(dependencies); }
    public Set<String> tags() { return Set.copyOf(tags); }
    public Map<String, String> labels() { return Map.copyOf(labels); }
    public Optional<String> businessDomain() { return Optional.ofNullable(businessDomain); }
    public Optional<String> moduleAssociation() { return Optional.ofNullable(moduleAssociation); }
    public List<PromptAuditEntry> auditTrail() { return List.copyOf(auditTrail); }
    public List<PromptDomainEvent> pendingEvents() { return List.copyOf(pendingEvents); }
    public void clearEvents() { pendingEvents.clear(); }

    public void setName(String name) {
        Objects.requireNonNull(name, "Name must not be null");
        if (name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        if (name.length() > 200) throw new IllegalArgumentException("Name must not exceed 200 characters");
        this.name = name;
    }

    public Optional<PromptVersion> version(SemanticVersion version) {
        return Optional.ofNullable(versions.get(version));
    }

    public PromptVersion activeVersion() {
        if (currentVersion == null) throw new IllegalStateException("No active version exists");
        return currentVersion;
    }

    // ========= LIFECYCLE =========

    public void transitionTo(PromptStatus target, String performedBy, String reason) {
        Objects.requireNonNull(target, "Target status must not be null");
        if (!status.canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(status, target);
        }
        var previous = this.status;
        this.status = target;
        this.updatedDate = Instant.now();
        appendAudit(AuditAction.STATUS_CHANGED, performedBy,
            previous.name(), target.name(), reason);
    }

    public void submitForReview(String performedBy) {
        transitionTo(PromptStatus.REVIEW, performedBy, "Submitted for review");
    }

    public void approve(String performedBy, String reviewNotes) {
        transitionTo(PromptStatus.APPROVED, performedBy, "Approved: " + reviewNotes);
    }

    public void publish(String performedBy) {
        if (!status.isPublishable()) {
            throw new InvalidStatusTransitionException(status, PromptStatus.PUBLISHED);
        }
        if (currentVersion == null) {
            throw new IllegalStateException("Cannot publish without a version");
        }
        this.status = PromptStatus.PUBLISHED;
        this.updatedDate = Instant.now();
        currentVersion = currentVersion.markAsPublished(performedBy);
        pendingEvents.add(new PromptPublishedEvent(id.value(), currentVersion.version().toString(), performedBy));
        appendAudit(AuditAction.VERSION_PUBLISHED, performedBy,
            null, currentVersion.version().toString(), "Prompt published");
    }

    public void deprecate(String performedBy, String reason) {
        transitionTo(PromptStatus.DEPRECATED, performedBy, reason);
        if (currentVersion != null) {
            currentVersion = currentVersion.markAsDeprecated();
        }
        pendingEvents.add(new PromptDeprecatedEvent(id.value(), performedBy, reason));
    }

    public void archive(String performedBy, String reason) {
        transitionTo(PromptStatus.ARCHIVED, performedBy, reason);
        pendingEvents.add(new PromptArchivedEvent(id.value(), performedBy, reason));
    }

    // ========= VERSION MANAGEMENT =========

    public PromptVersion createVersion(SemanticVersion version, PromptTemplate template,
                                        String changeSummary, String createdBy) {
        Objects.requireNonNull(version, "Version must not be null");
        Objects.requireNonNull(template, "Template must not be null");
        validateTemplate(template);

        if (version.isDowngradeFrom(semanticVersionOfCurrent())) {
            throw new IllegalArgumentException("Cannot downgrade semantic version");
        }
        if (versions.containsKey(version)) {
            throw new DuplicateVersionException(version);
        }

        var promptVersion = new PromptVersion(version, changeSummary, null, createdBy,
            template, true, false, false, false);
        versions.put(version, promptVersion);
        currentVersion = promptVersion;
        updatedDate = Instant.now();
        pendingEvents.add(new PromptVersionCreatedEvent(id.value(), version.toString(), createdBy));
        appendAudit(AuditAction.VERSION_CREATED, createdBy, null, version.toString(), changeSummary);
        return promptVersion;
    }

    public void setCurrentVersion(SemanticVersion version) {
        var ver = versions.get(version);
        if (ver == null) throw new IllegalArgumentException("Version " + version + " not found");
        this.currentVersion = ver;
        this.updatedDate = Instant.now();
    }

    public PromptVersion createInitialVersion(PromptTemplate template, String createdBy) {
        var version = SemanticVersion.initial();
        return createVersion(version, template, "Initial version", createdBy);
    }

    public PromptVersion createNextVersion(PromptTemplate template, String changeSummary, String createdBy) {
        var nextVersion = currentVersion != null
            ? currentVersion.version().nextPatch()
            : SemanticVersion.initial();
        return createVersion(nextVersion, template, changeSummary, createdBy);
    }

    // ========= VARAIBLE MANAGEMENT =========

    public void addVariable(PromptVariable variable, String performedBy) {
        Objects.requireNonNull(variable, "Variable must not be null");
        assertEditable();
        var existing = findVariable(variable.name());
        if (existing != null) {
            throw new IllegalArgumentException("Variable '" + variable.name() + "' already exists");
        }
        var template = currentVersion.template();
        var updatedVariables = new ArrayList<>(template.variables());
        updatedVariables.add(variable);
        currentVersion = new PromptVersion(currentVersion.version(), currentVersion.changeSummary(),
            currentVersion.publishedDate(), currentVersion.publishedBy(),
            template.withUpdatedVariables(updatedVariables),
            currentVersion.isDraft(), currentVersion.isRollback(),
            currentVersion.isDeprecated(), currentVersion.isImmutable());
        appendAudit(AuditAction.VARIABLE_ADDED, performedBy, null, variable.name(), null);
    }

    public void removeVariable(String variableName, String performedBy) {
        Objects.requireNonNull(variableName, "Variable name must not be null");
        assertEditable();
        var template = currentVersion.template();
        var variable = template.findVariable(variableName);
        if (variable == null) throw new IllegalArgumentException("Variable '" + variableName + "' not found");
        if (variable.required()) throw new IllegalArgumentException("Cannot remove required variable '" + variableName + "'");
        var updatedVariables = new ArrayList<>(template.variables());
        updatedVariables.remove(variable);
        currentVersion = new PromptVersion(currentVersion.version(), currentVersion.changeSummary(),
            currentVersion.publishedDate(), currentVersion.publishedBy(),
            template.withUpdatedVariables(updatedVariables),
            currentVersion.isDraft(), currentVersion.isRollback(),
            currentVersion.isDeprecated(), currentVersion.isImmutable());
        appendAudit(AuditAction.VARIABLE_REMOVED, performedBy, variableName, null, null);
    }

    // ========= METADATA MANAGEMENT =========

    public void updateMetadata(PromptMetadata newMetadata, String performedBy) {
        Objects.requireNonNull(newMetadata, "Metadata must not be null");
        var previous = this.metadata;
        this.metadata = newMetadata;
        this.updatedDate = Instant.now();
        pendingEvents.add(new PromptMetadataUpdatedEvent(id.value(), performedBy));
        appendAudit(AuditAction.METADATA_UPDATED, performedBy,
            previous.owner(), newMetadata.owner(), "Metadata updated");
    }

    public void updateExecutionPolicy(PromptExecutionPolicy newPolicy, String performedBy) {
        Objects.requireNonNull(newPolicy, "Execution policy must not be null");
        var previous = this.executionPolicy;
        this.executionPolicy = newPolicy;
        this.updatedDate = Instant.now();
        pendingEvents.add(new PromptExecutionPolicyChangedEvent(id.value(), performedBy));
        appendAudit(AuditAction.POLICY_CHANGED, performedBy,
            previous.toString(), newPolicy.toString(), "Execution policy updated");
    }

    // ========= OWNERSHIP =========

    public void changeOwner(String newOwner, String performedBy) {
        Objects.requireNonNull(newOwner, "New owner must not be null");
        if (newOwner.isBlank()) throw new IllegalArgumentException("New owner must not be blank");
        var previous = this.owner;
        this.owner = newOwner;
        this.updatedDate = Instant.now();
        appendAudit(AuditAction.OWNER_CHANGED, performedBy, previous, newOwner, null);
    }

    // ========= BUSINESS RULES =========

    private void validateTemplate(PromptTemplate template) {
        if (template == null || !template.isValid()) {
            throw new InvalidTemplateException("Template is invalid or empty");
        }
        template.validateContent();
    }

    private SemanticVersion semanticVersionOfCurrent() {
        return currentVersion != null ? currentVersion.version() : new SemanticVersion(0, 0, 0);
    }

    private void assertEditable() {
        if (!status.isEditable()) {
            throw new IllegalStateException("Prompt is not editable in status: " + status);
        }
    }

    public void assertNotImmutable() {
        if (currentVersion != null) currentVersion.assertNotImmutable();
    }

    private void appendAudit(AuditAction action, String performedBy,
                              String previous, String newValue, String reason) {
        auditTrail.add(new PromptAuditEntry(action, performedBy, Instant.now(),
            previous, newValue, reason, null, null, null));
    }

    public void updateName(String name, String performedBy) {
        var previous = this.name;
        setName(name);
        this.updatedDate = Instant.now();
        appendAudit(AuditAction.PROMPT_UPDATED, performedBy, previous, name, "Name updated");
    }

    public void updateDescription(String description, String performedBy) {
        var previous = this.description;
        this.description = description;
        this.updatedDate = Instant.now();
        appendAudit(AuditAction.PROMPT_UPDATED, performedBy, previous, description, "Description updated");
    }

    public void addTag(String tag) {
        Objects.requireNonNull(tag, "Tag must not be null");
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public void putLabel(String key, String value) {
        Objects.requireNonNull(key, "Label key must not be null");
        labels.put(key, value);
    }

    public boolean hasDependency(String promptId) {
        return dependencies.contains(promptId);
    }

    public boolean isOwnedBy(String userId) {
        return owner.equals(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prompt prompt)) return false;
        return Objects.equals(id, prompt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Prompt{id=" + id + ", name='" + name + "', status=" + status + "}";
    }

    private PromptVariable findVariable(String name) {
        if (currentVersion == null) return null;
        return currentVersion.template().findVariable(name);
    }
}
