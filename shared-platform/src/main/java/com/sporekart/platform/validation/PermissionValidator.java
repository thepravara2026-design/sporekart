package com.sporekart.platform.validation;

import com.sporekart.platform.error.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PermissionValidator {
    private static final Logger log = LoggerFactory.getLogger(PermissionValidator.class);

    public static void requireAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }
    }

    public static void requireRole(Authentication authentication, String role) {
        requireAuthenticated(authentication);
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role))) {
            log.warn("Access denied: user lacks role {}", role);
            throw new SecurityException("Insufficient permissions", 403);
        }
    }

    public static void requireAnyRole(Authentication authentication, String... roles) {
        requireAuthenticated(authentication);
        for (String role : roles) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role))) {
                return;
            }
        }
        throw new SecurityException("Insufficient permissions", 403);
    }

    public static void requireOwnership(Authentication authentication, String ownerId, String resourceOwnerId) {
        requireAuthenticated(authentication);
        String userId = authentication.getName();
        if (!userId.equals(resourceOwnerId)) {
            log.warn("Access denied: user {} does not own resource owned by {}", userId, resourceOwnerId);
            throw new SecurityException("Resource does not belong to the current user", 403);
        }
    }

    public static void requireWorkspace(Authentication authentication, String workspaceId) {
        requireAuthenticated(authentication);
        if (workspaceId == null || workspaceId.isBlank()) {
            throw new SecurityException("Workspace context required", 403);
        }
    }
}
