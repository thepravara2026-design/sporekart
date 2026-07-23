package com.sporekart.copilot.context;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record UserContext(
    String userId,
    String userName,
    String email,
    List<String> roles,
    String workspaceId,
    String organizationId,
    String tenantId,
    String deviceType,
    String language,
    String timeZone,
    Map<String, Object> featureFlags
) {
    public UserContext {
        Objects.requireNonNull(userId, "userId must not be null");
        if (userId.isBlank()) throw new IllegalArgumentException("userId must not be blank");
        roles = roles == null ? Collections.emptyList() : List.copyOf(roles);
        featureFlags = featureFlags == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(featureFlags));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String userId;
        private String userName;
        private String email;
        private List<String> roles;
        private String workspaceId;
        private String organizationId;
        private String tenantId;
        private String deviceType;
        private String language;
        private String timeZone;
        private Map<String, Object> featureFlags;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder userName(String userName) { this.userName = userName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder roles(List<String> roles) { this.roles = roles; return this; }
        public Builder workspaceId(String workspaceId) { this.workspaceId = workspaceId; return this; }
        public Builder organizationId(String organizationId) { this.organizationId = organizationId; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder deviceType(String deviceType) { this.deviceType = deviceType; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder timeZone(String timeZone) { this.timeZone = timeZone; return this; }
        public Builder featureFlags(Map<String, Object> featureFlags) { this.featureFlags = featureFlags; return this; }

        public UserContext build() {
            return new UserContext(userId, userName, email, roles, workspaceId, organizationId,
                tenantId, deviceType, language, timeZone, featureFlags);
        }
    }
}
