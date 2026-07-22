package com.sporekart.prompt.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class PromptSecurityService {

    private static final Logger log = LoggerFactory.getLogger(PromptSecurityService.class);

    private static final Set<String> ADMIN_ROLES = Set.of("AI_ADMIN", "PLATFORM_ADMIN");
    private static final Set<String> REVIEWER_ROLES = Set.of("AI_REVIEWER", "AI_ADMIN");
    private static final Set<String> DEVELOPER_ROLES = Set.of("AI_DEVELOPER", "AI_REVIEWER", "AI_ADMIN");
    private static final Set<String> VIEWER_ROLES = Set.of("VIEWER", "AI_DEVELOPER", "AI_REVIEWER", "AI_ADMIN");

    public boolean isAdmin(UUID userId, Set<String> roles) {
        return roles.stream().anyMatch(ADMIN_ROLES::contains);
    }

    public boolean isReviewer(UUID userId, Set<String> roles) {
        return roles.stream().anyMatch(REVIEWER_ROLES::contains);
    }

    public boolean isDeveloper(UUID userId, Set<String> roles) {
        return roles.stream().anyMatch(DEVELOPER_ROLES::contains);
    }

    public boolean canView(UUID userId, Set<String> roles) {
        return roles.stream().anyMatch(VIEWER_ROLES::contains);
    }

    public String sanitizeInput(String input) {
        if (input == null) return null;
        return input
                .replace("<script>", "")
                .replace("</script>", "")
                .replace("javascript:", "")
                .replace("onerror=", "")
                .replace("onload=", "")
                .replace("onclick=", "");
    }
}
