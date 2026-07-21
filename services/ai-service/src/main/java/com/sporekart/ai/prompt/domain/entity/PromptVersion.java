package com.sporekart.ai.prompt.domain.entity;

import com.sporekart.ai.prompt.domain.valueobject.SemanticVersion;
import java.time.Instant;
import java.util.Objects;

public class PromptVersion {
    private final SemanticVersion version;
    private final String changeSummary;
    private final Instant publishedDate;
    private final String publishedBy;
    private final PromptTemplate template;
    private final boolean draft;
    private final boolean rollback;
    private final boolean deprecated;
    private final boolean immutable;

    public PromptVersion(SemanticVersion version, String changeSummary, Instant publishedDate,
                        String publishedBy, PromptTemplate template, boolean draft,
                        boolean rollback, boolean deprecated, boolean immutable) {
        this.version = Objects.requireNonNull(version, "Version must not be null");
        this.changeSummary = changeSummary;
        this.publishedDate = publishedDate;
        this.publishedBy = publishedBy;
        this.template = Objects.requireNonNull(template, "Template must not be null");
        this.draft = draft;
        this.rollback = rollback;
        this.deprecated = deprecated;
        this.immutable = immutable;
    }

    public SemanticVersion version() { return version; }
    public String changeSummary() { return changeSummary; }
    public Instant publishedDate() { return publishedDate; }
    public String publishedBy() { return publishedBy; }
    public PromptTemplate template() { return template; }
    public boolean isDraft() { return draft; }
    public boolean isRollback() { return rollback; }
    public boolean isDeprecated() { return deprecated; }
    public boolean isImmutable() { return immutable; }

    public PromptVersion markAsPublished(String publishedBy) {
        return new PromptVersion(version, changeSummary, Instant.now(), publishedBy,
            template, false, rollback, deprecated, true);
    }

    public PromptVersion markAsDeprecated() {
        return new PromptVersion(version, changeSummary, publishedDate, publishedBy,
            template, draft, rollback, true, immutable);
    }

    public PromptVersion markAsDraft() {
        return new PromptVersion(version, changeSummary, publishedDate, publishedBy,
            template, true, rollback, deprecated, false);
    }

    public void assertNotImmutable() {
        if (immutable) {
            throw new IllegalStateException("Cannot modify published version " + version);
        }
    }

    public String toSnapshot() {
        return "v" + version + "|draft=" + draft + "|deprecated=" + deprecated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromptVersion that)) return false;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return "PromptVersion{v=" + version + ", draft=" + draft + ", immutable=" + immutable + "}";
    }
}
