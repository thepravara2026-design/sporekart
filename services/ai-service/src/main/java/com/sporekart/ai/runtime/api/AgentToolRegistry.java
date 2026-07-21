package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentTool;

import java.util.List;
import java.util.Optional;

public interface AgentToolRegistry {
    void register(AgentTool tool);
    void unregister(String name);
    Optional<AgentTool> get(String name);
    List<AgentTool> list();
    boolean exists(String name);
}
