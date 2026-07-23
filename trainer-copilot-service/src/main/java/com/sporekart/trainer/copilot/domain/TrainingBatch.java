package com.sporekart.trainer.copilot.domain;

import java.util.List;
import java.util.Map;

public record TrainingBatch(
    String batchId,
    String batchName,
    String courseName,
    String startDate,
    String endDate,
    String status,
    int capacity,
    int enrolledCount,
    String trainerName,
    String location,
    List<String> moduleIds,
    Map<String, Object> metadata
) {}
