package com.sporekart.copilot.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ToolRegistryImpl implements ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistryImpl.class);

    private final ConcurrentMap<String, CopilotTool> tools = new ConcurrentHashMap<>();

    public ToolRegistryImpl() {
        log.debug("ToolRegistryImpl initialized");
    }

    @Override
    public void register(CopilotTool tool) {
        tools.put(tool.getId(), tool);
        log.info("Registered tool: {} ({})", tool.getId(), tool.getName());
    }

    @Override
    public CopilotTool findById(String id) {
        return tools.get(id);
    }

    @Override
    public List<CopilotTool> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(tools.values()));
    }

    @Override
    public boolean isRegistered(String id) {
        return tools.containsKey(id);
    }
}
