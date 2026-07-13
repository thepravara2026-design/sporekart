package com.sporekart.ai.configregistry.api;

import com.sporekart.ai.configregistry.domain.ConfigSnapshot;
import java.util.List;
import java.util.Map;

public interface ConfigSnapshotService {

    ConfigSnapshot createSnapshot(String name, String description, String createdBy);

    List<ConfigSnapshot> listSnapshots();

    void rollbackToSnapshot(String snapshotId);

    Map<String, String> compareSnapshots(String id1, String id2);
}
