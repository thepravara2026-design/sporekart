package com.sporekart.ai.prompt.domain.valueobject;

import java.util.EnumSet;
import java.util.Set;

public enum PromptStatus {
    DRAFT,
    REVIEW,
    APPROVED,
    PUBLISHED,
    DEPRECATED,
    ARCHIVED;

    private static final Set<PromptStatus> ACTIVE_STATUSES = EnumSet.of(PUBLISHED);
    private static final Set<PromptStatus> EDITABLE_STATUSES = EnumSet.of(DRAFT, REVIEW);
    private static final Set<PromptStatus> PUBLISHABLE_STATUSES = EnumSet.of(APPROVED);
    private static final Set<PromptStatus> DELETABLE_STATUSES = EnumSet.of(DRAFT, ARCHIVED);
    private static final Set<PromptStatus> REVIEWABLE_STATUSES = EnumSet.of(DRAFT);
    private static final Set<PromptStatus> APPROVABLE_STATUSES = EnumSet.of(REVIEW);
    private static final Set<PromptStatus> DEPRECABLE_STATUSES = EnumSet.of(PUBLISHED);
    private static final Set<PromptStatus> ARCHIVABLE_STATUSES = EnumSet.of(DEPRECATED);

    public boolean canTransitionTo(PromptStatus target) {
        return allowedTransitions().contains(target);
    }

    public Set<PromptStatus> allowedTransitions() {
        return switch (this) {
            case DRAFT -> Set.of(REVIEW, ARCHIVED);
            case REVIEW -> Set.of(APPROVED, DRAFT);
            case APPROVED -> Set.of(PUBLISHED, DRAFT);
            case PUBLISHED -> Set.of(DEPRECATED, DRAFT);
            case DEPRECATED -> Set.of(ARCHIVED, PUBLISHED);
            case ARCHIVED -> Set.of(DRAFT);
        };
    }

    public boolean isActive() { return ACTIVE_STATUSES.contains(this); }
    public boolean isEditable() { return EDITABLE_STATUSES.contains(this); }
    public boolean isPublishable() { return PUBLISHABLE_STATUSES.contains(this); }
    public boolean isDeletable() { return DELETABLE_STATUSES.contains(this); }
    public boolean isReviewable() { return REVIEWABLE_STATUSES.contains(this); }
    public boolean isApproable() { return APPROVABLE_STATUSES.contains(this); }
    public boolean isDeprecable() { return DEPRECABLE_STATUSES.contains(this); }
    public boolean isArchivable() { return ARCHIVABLE_STATUSES.contains(this); }
}
