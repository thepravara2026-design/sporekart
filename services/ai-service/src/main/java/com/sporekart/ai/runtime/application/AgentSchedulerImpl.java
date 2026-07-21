package com.sporekart.ai.runtime.application;

import com.sporekart.ai.runtime.api.AgentScheduler;
import com.sporekart.ai.runtime.domain.AgentSchedule;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentSchedulerImpl implements AgentScheduler {

    @Override
    public AgentSchedule schedule(AgentSchedule schedule) {
        return null;
    }

    @Override
    public void unschedule(String agentId) {
    }

    @Override
    public Optional<AgentSchedule> getSchedule(String agentId) {
        return Optional.empty();
    }

    @Override
    public List<AgentSchedule> listSchedules() {
        return List.of();
    }

    @Override
    public AgentSchedule updateSchedule(AgentSchedule schedule) {
        return null;
    }

    @Override
    public void pauseSchedule(String agentId) {
    }

    @Override
    public void resumeSchedule(String agentId) {
    }

    @Override
    public void triggerNow(String agentId) {
    }
}
