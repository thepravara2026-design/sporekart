package com.sporekart.ai.providers.contracts;

import java.util.List;
import java.util.Map;

public record ToolCallingContract(
    String model,
    String prompt,
    List<Map<String, Object>> tools,
    String toolChoice,
    boolean parallelToolCalls
) {}
