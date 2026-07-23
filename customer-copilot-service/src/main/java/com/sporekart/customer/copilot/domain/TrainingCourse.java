package com.sporekart.customer.copilot.domain;

import java.time.OffsetDateTime;

public record TrainingCourse(
    String id,
    String title,
    String description,
    String category,
    String level,
    String duration,
    String instructor,
    OffsetDateTime nextBatchDate,
    String enrollmentStatus
) {
    public TrainingCourse {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (description == null) {
            description = "";
        }
        if (category == null || category.isBlank()) {
            category = "General";
        }
        if (level == null || level.isBlank()) {
            level = "BEGINNER";
        }
        if (duration == null || duration.isBlank()) {
            duration = "";
        }
        if (instructor == null) {
            instructor = "TBD";
        }
        if (enrollmentStatus == null || enrollmentStatus.isBlank()) {
            enrollmentStatus = "OPEN";
        }
    }
}
