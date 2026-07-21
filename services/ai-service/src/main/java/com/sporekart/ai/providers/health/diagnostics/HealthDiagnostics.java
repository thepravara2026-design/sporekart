package com.sporekart.ai.providers.health.diagnostics;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HealthDiagnostics {
    DiagnosticResult diagnose(String providerId);
    List<DiagnosticResult> diagnoseAll();
    Optional<String> getRootCause(String providerId);
    Map<String, Object> getDiagnosticData(String providerId);
}

record DiagnosticResult(String providerId, boolean healthy, List<String> issues, Map<String, Object> details) {}
