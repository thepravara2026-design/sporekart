package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RiskAssessmentService {
    RiskAssessment createAssessment(String module, String action, Map<String, Object> context);
    RiskAssessment getAssessment(UUID id);
    List<RiskAssessment> getAssessmentsByModule(String module);
    List<RiskAssessment> getAssessmentsByStatus(RiskAssessmentStatus status);
}
