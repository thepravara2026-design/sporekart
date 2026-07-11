package com.sporekart.ai.monitoring.api;

import java.util.Map;

public interface AuditLogger {
    void log(String action, String module, String user, Map<String, Object> details);
}
