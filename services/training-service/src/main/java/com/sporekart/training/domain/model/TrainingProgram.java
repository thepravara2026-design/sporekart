package com.sporekart.training.domain.model;

import java.time.Instant;
import java.util.UUID;

public class TrainingProgram {
    private final String id;
    private final String title;
    private final String category;
    private final String difficulty;
    private final String language;
    private final int durationHours;
    private final int maxSeats;
    private final TrainingStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public TrainingProgram(String id, String title, String category, String difficulty, String language,
            int durationHours, int maxSeats, TrainingStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.language = language;
        this.durationHours = durationHours;
        this.maxSeats = maxSeats;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TrainingProgram create(String title, String category, String difficulty, String language,
            int durationHours, int maxSeats) {
        return new TrainingProgram(UUID.randomUUID().toString(), title, category, difficulty, language,
                durationHours, maxSeats, TrainingStatus.DRAFT, Instant.now(), Instant.now());
    }

    public TrainingProgram publish() {
        return new TrainingProgram(id, title, category, difficulty, language, durationHours, maxSeats,
                TrainingStatus.PUBLISHED, createdAt, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLanguage() {
        return language;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
