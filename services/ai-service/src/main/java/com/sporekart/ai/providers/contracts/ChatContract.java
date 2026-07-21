package com.sporekart.ai.providers.contracts;

import java.util.List;

public record ChatContract(
    String model,
    List<Message> messages,
    Double temperature,
    Integer maxTokens,
    Boolean stream
) {
    public record Message(String role, String content) {}
}
