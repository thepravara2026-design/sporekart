package com.sporekart.trainer.copilot.dto;

import java.util.List;

import com.sporekart.trainer.copilot.domain.TrainingBatch;

public record BatchListResponse(
    List<TrainingBatch> batches,
    int totalCount,
    int activeCount,
    int upcomingCount,
    int completedCount
) {}
