package com.sporekart.copilot.tool;

public interface ToolExecutor {

    ToolResult execute(String toolId, ToolExecutionContext context);
}
