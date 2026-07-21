package com.sporekart.ai.runtime.application;

import com.sporekart.ai.runtime.api.AgentToolRegistry;
import com.sporekart.ai.runtime.domain.AgentTool;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentToolRegistryImpl implements AgentToolRegistry {

    private final ConcurrentHashMap<String, AgentTool> tools = new ConcurrentHashMap<>();

    @Override
    public void register(AgentTool tool) {
        tools.put(tool.name(), tool);
    }

    @Override
    public void unregister(String name) {
        tools.remove(name);
    }

    @Override
    public Optional<AgentTool> get(String name) {
        return Optional.ofNullable(tools.get(name));
    }

    @Override
    public List<AgentTool> list() {
        return List.copyOf(tools.values());
    }

    @Override
    public boolean exists(String name) {
        return tools.containsKey(name);
    }
}
