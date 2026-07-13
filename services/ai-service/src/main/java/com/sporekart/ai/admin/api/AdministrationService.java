package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.AdminOperation;
import com.sporekart.ai.admin.domain.AdminOperationType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AdministrationService {
    AdminOperation performOperation(AdminOperationType type, String description, Map<String, Object> details, UUID performedBy, String ipAddress);
    List<AdminOperation> getOperationHistory();
    List<AdminOperation> getOperationsByType(AdminOperationType type);
}
