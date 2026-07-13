package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface JobExecutionService {
    AutomationJob createJob(JobType type, String name, Map<String, Object> params);
    AutomationJob startJob(UUID id);
    AutomationJob completeJob(UUID id, Map<String, Object> result);
    AutomationJob failJob(UUID id, String error);
    AutomationJob cancelJob(UUID id);
    AutomationJob getJob(UUID id);
    List<AutomationJob> getJobsByStatus(AutomationStatus status);
}
