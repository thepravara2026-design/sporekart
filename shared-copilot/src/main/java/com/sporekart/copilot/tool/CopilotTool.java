package com.sporekart.copilot.tool;

import java.util.Map;

public interface CopilotTool {

    String getId();

    String getName();

    String getDescription();

    Map<String, Object> getInputSchema();

    Map<String, Object> getOutputSchema();

    ToolResult execute(ToolExecutionContext context);

    boolean requiresAuthorization();
}
