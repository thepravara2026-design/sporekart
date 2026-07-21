package com.sporekart.ai.gateway.contract.request;

import java.util.Map;

public record FineTuningRequest(
    String model,
    String trainingFileId,
    String validationFileId,
    Map<String, Object> hyperparameters,
    String suffix,
    String userId
) {}
