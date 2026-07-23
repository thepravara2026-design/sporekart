package com.sporekart.copilot.context;

import com.sporekart.copilot.domain.ConversationMessage;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CopilotContext(
    UserContext user,
    PageContext page,
    CopilotType copilotType,
    SessionId sessionId,
    List<ConversationMessage> recentHistory,
    Map<String, Object> contextualData,
    OffsetDateTime timestamp
) {
    public CopilotContext {
        Objects.requireNonNull(user, "user must not be null");
        Objects.requireNonNull(copilotType, "copilotType must not be null");
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        page = page == null ? PageContext.empty() : page;
        recentHistory = recentHistory == null ? Collections.emptyList() : List.copyOf(recentHistory);
        contextualData = contextualData == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(contextualData));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UserContext user;
        private PageContext page;
        private CopilotType copilotType;
        private SessionId sessionId;
        private List<ConversationMessage> recentHistory;
        private Map<String, Object> contextualData;
        private OffsetDateTime timestamp;

        public Builder user(UserContext user) { this.user = user; return this; }
        public Builder page(PageContext page) { this.page = page; return this; }
        public Builder copilotType(CopilotType copilotType) { this.copilotType = copilotType; return this; }
        public Builder sessionId(SessionId sessionId) { this.sessionId = sessionId; return this; }
        public Builder recentHistory(List<ConversationMessage> recentHistory) { this.recentHistory = recentHistory; return this; }
        public Builder contextualData(Map<String, Object> contextualData) { this.contextualData = contextualData; return this; }
        public Builder timestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; return this; }

        public CopilotContext build() {
            return new CopilotContext(user, page, copilotType, sessionId, recentHistory, contextualData,
                timestamp != null ? timestamp : OffsetDateTime.now());
        }
    }
}
