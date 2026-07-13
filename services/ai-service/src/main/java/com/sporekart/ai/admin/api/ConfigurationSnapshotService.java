package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationSnapshot;

import java.util.List;
import java.util.UUID;

public interface ConfigurationSnapshotService {
    ConfigurationSnapshot createSnapshot(String name, String environment, String description, UUID capturedBy);
    ConfigurationSnapshot getSnapshot(UUID id);
    List<ConfigurationSnapshot> getSnapshotsByEnvironment(String environment);
    AdminConfiguration restoreSnapshot(UUID snapshotId, UUID restoredBy);
}
