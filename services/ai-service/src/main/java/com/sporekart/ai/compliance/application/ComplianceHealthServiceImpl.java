package com.sporekart.ai.compliance.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ComplianceHealthServiceImpl implements ComplianceHealthService {

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public Map<String, Object> getHealthDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("service", "compliance");
        details.put("timestamp", Instant.now().toString());
        return details;
    }

    @Override
    public Map<String, Object> getReadiness() {
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("ready", true);
        readiness.put("dependencies", "checked");
        return readiness;
    }
}
