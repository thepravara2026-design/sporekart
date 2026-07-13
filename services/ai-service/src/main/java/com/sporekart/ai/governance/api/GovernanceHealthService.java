package com.sporekart.ai.governance.api;

import com.sporekart.ai.governance.domain.*;
import java.util.Map;

public interface GovernanceHealthService {
    Map<String, Object> checkHealth();
    Map<String, Object> getStatus();
    boolean isOperational();
    Map<String, Object> getMetrics();
}
