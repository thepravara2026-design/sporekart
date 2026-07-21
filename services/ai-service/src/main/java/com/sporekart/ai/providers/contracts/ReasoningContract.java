package com.sporekart.ai.providers.contracts;

import java.util.List;
import java.util.Map;

public record ReasoningContract(
    String model,
    String prompt,
    List<Map<String, Object>> messages,
    String reasoningEffort
) {}
