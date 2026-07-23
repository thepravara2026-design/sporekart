package com.sporekart.copilot.tool;

import java.util.List;

public interface ToolRegistry {

    void register(CopilotTool tool);

    CopilotTool findById(String id);

    List<CopilotTool> findAll();

    boolean isRegistered(String id);
}
