package com.sporekart.copilot.permission;

import com.sporekart.copilot.context.UserContext;

import java.util.List;

public interface PermissionChecker {

    boolean hasPermission(UserContext user, String resource, String action);

    List<CopilotPermission> getPermissions(UserContext user);
}
