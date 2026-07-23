package com.sporekart.copilot.tool;

import com.sporekart.copilot.permission.PermissionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ToolExecutorImpl implements ToolExecutor {

    private static final Logger log = LoggerFactory.getLogger(ToolExecutorImpl.class);

    private static final int MAX_RETRIES = 3;
    private static final long TIMEOUT_MS = 30_000L;

    private final ToolRegistry toolRegistry;
    private final PermissionChecker permissionChecker;

    public ToolExecutorImpl(ToolRegistry toolRegistry, PermissionChecker permissionChecker) {
        this.toolRegistry = toolRegistry;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public ToolResult execute(String toolId, ToolExecutionContext context) {
        long startTime = System.currentTimeMillis();

        CopilotTool tool = toolRegistry.findById(toolId);
        if (tool == null) {
            log.warn("Tool not found: {}", toolId);
            return ToolResult.error("Tool not found: " + toolId, System.currentTimeMillis() - startTime);
        }

        if (!validateParameters(tool, context.parameters())) {
            log.warn("Parameter validation failed for tool: {}", toolId);
            return ToolResult.error("Parameter validation failed for tool: " + toolId,
                System.currentTimeMillis() - startTime);
        }

        if (tool.requiresAuthorization()) {
            boolean authorized = checkAuthorization(tool, context);
            if (!authorized) {
                log.warn("Authorization denied for tool: {}, user: {}", toolId, context.userContext().userId());
                return ToolResult.error("Not authorized to use tool: " + toolId,
                    System.currentTimeMillis() - startTime);
            }
        }

        Exception lastException = null;
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.debug("Executing tool: {}, attempt {}/{}", toolId, attempt, MAX_RETRIES);

                ToolResult result = executeWithTimeout(tool, context);

                if (result.success()) {
                    log.info("Tool '{}' executed successfully in {}ms (attempt {})",
                        toolId, result.executionTimeMs(), attempt);
                } else {
                    log.warn("Tool '{}' failed: {} (attempt {})", toolId, result.errorMessage(), attempt);
                }

                Map<String, Object> metrics = Map.of(
                    "toolId", toolId,
                    "success", result.success(),
                    "executionTimeMs", result.executionTimeMs(),
                    "attempt", attempt
                );
                log.debug("Tool execution metrics: {}", metrics);

                return result;

            } catch (Exception e) {
                lastException = e;
                log.error("Tool '{}' failed on attempt {}/{}: {}", toolId, attempt, MAX_RETRIES, e.getMessage());
                if (attempt < MAX_RETRIES) {
                    long backoff = (long) Math.pow(2, attempt) * 100L;
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return ToolResult.error("Execution interrupted for tool: " + toolId,
                            System.currentTimeMillis() - startTime);
                    }
                }
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;
        log.error("Tool '{}' exhausted all {} retries in {}ms", toolId, MAX_RETRIES, totalTime);
        return ToolResult.error(
            "Tool execution failed after " + MAX_RETRIES + " attempts: "
                + (lastException != null ? lastException.getMessage() : "unknown error"),
            totalTime
        );
    }

    private boolean validateParameters(CopilotTool tool, Map<String, Object> parameters) {
        Map<String, Object> inputSchema = tool.getInputSchema();
        if (inputSchema == null || inputSchema.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, Object> entry : inputSchema.entrySet()) {
            String paramName = entry.getKey();
            String expectedType = String.valueOf(entry.getValue());
            Object value = parameters.get(paramName);
            if (value != null && !isTypeMatch(value, expectedType)) {
                log.warn("Parameter '{}' expected type '{}' but got value '{}'", paramName, expectedType, value);
                return false;
            }
        }
        return true;
    }

    private boolean isTypeMatch(Object value, String expectedType) {
        return switch (expectedType.toLowerCase()) {
            case "string" -> value instanceof String;
            case "integer", "int" -> value instanceof Integer || value instanceof Long;
            case "number", "double", "float" -> value instanceof Number;
            case "boolean", "bool" -> value instanceof Boolean;
            case "array" -> value instanceof java.util.List;
            case "object" -> value instanceof Map;
            default -> true;
        };
    }

    private boolean checkAuthorization(CopilotTool tool, ToolExecutionContext context) {
        String resource = "tool:" + tool.getId();
        String action = "execute";
        return permissionChecker.hasPermission(context.userContext(), resource, action);
    }

    private ToolResult executeWithTimeout(CopilotTool tool, ToolExecutionContext context) {
        Thread executionThread = Thread.currentThread();
        long startTime = System.currentTimeMillis();

        final ToolResult[] resultHolder = new ToolResult[1];
        final boolean[] completed = {false};
        final Exception[] exceptionHolder = new Exception[1];

        Thread timeoutWatcher = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                if (!completed[0]) {
                    executionThread.interrupt();
                }
            } catch (InterruptedException ignored) {
            }
        });
        timeoutWatcher.setDaemon(true);
        timeoutWatcher.start();

        try {
            resultHolder[0] = tool.execute(context);
            completed[0] = true;
        } catch (Exception e) {
            exceptionHolder[0] = e;
        } finally {
            timeoutWatcher.interrupt();
        }

        if (exceptionHolder[0] != null) {
            throw new RuntimeException("Tool execution threw exception", exceptionHolder[0]);
        }

        long elapsed = System.currentTimeMillis() - startTime;
        return new ToolResult(
            resultHolder[0].success(),
            resultHolder[0].data(),
            resultHolder[0].errorMessage(),
            elapsed
        );
    }
}
