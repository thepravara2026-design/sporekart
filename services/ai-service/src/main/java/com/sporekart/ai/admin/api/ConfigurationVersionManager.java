package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationVersion;

import java.util.List;
import java.util.UUID;

public interface ConfigurationVersionManager {
    ConfigurationVersion createVersion(UUID configId, String value, String changeReason, UUID changedBy);
    List<ConfigurationVersion> getVersions(UUID configId);
    ConfigurationVersion getVersion(UUID configId, int version);
    AdminConfiguration rollback(UUID configId, int targetVersion, UUID rolledBackBy);
}
