package com.sporekart.workspace.domain;

import java.util.List;

public record RoutedMessage(
    String messageId,
    String originalMessage,
    String routedCopilotId,
    double confidence,
    String routingReason,
    String originalCopilotId,
    boolean requiresHandoff,
    List<String> collaboratingCopilotIds
) {}
