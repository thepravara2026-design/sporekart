package com.sporekart.copilot.context;

import java.util.Objects;

public record PageContext(
    String pageUrl,
    String pageTitle,
    String section,
    String entityType,
    String entityId
) {
    public PageContext {
        pageUrl = pageUrl == null ? "" : pageUrl;
        pageTitle = pageTitle == null ? "" : pageTitle;
        section = section == null ? "" : section;
        entityType = entityType == null ? "" : entityType;
        entityId = entityId == null ? "" : entityId;
    }

    public static PageContext empty() {
        return new PageContext(null, null, null, null, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String pageUrl;
        private String pageTitle;
        private String section;
        private String entityType;
        private String entityId;

        public Builder pageUrl(String pageUrl) { this.pageUrl = pageUrl; return this; }
        public Builder pageTitle(String pageTitle) { this.pageTitle = pageTitle; return this; }
        public Builder section(String section) { this.section = section; return this; }
        public Builder entityType(String entityType) { this.entityType = entityType; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }

        public PageContext build() {
            return new PageContext(pageUrl, pageTitle, section, entityType, entityId);
        }
    }
}
