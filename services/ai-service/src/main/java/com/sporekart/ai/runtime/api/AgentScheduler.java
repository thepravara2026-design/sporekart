package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentSchedule;

import java.util.List;
import java.util.Optional;

public interface AgentScheduler {
    AgentSchedule schedule(AgentSchedule schedule);
    void unschedule(String agentId);
    Optional<AgentSchedule> getSchedule(String agentId);
    List<AgentSchedule> listSchedules();
    AgentSchedule updateSchedule(AgentSchedule schedule);
    void pauseSchedule(String agentId);
    void resumeSchedule(String agentId);
    void triggerNow(String agentId);
}
