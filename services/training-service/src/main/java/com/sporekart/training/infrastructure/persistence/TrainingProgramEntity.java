package com.sporekart.training.infrastructure.persistence;

import com.sporekart.training.domain.model.TrainingProgram;
import com.sporekart.training.domain.model.TrainingStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "training_programs")
public class TrainingProgramEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "difficulty", length = 50)
    private String difficulty;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "duration_hours", nullable = false)
    private int durationHours;

    @Column(name = "max_seats", nullable = false)
    private int maxSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TrainingStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected TrainingProgramEntity() {}

    public TrainingProgramEntity(String id, String title, String category, String difficulty, String language,
                                 int durationHours, int maxSeats, TrainingStatus status,
                                 Instant createdAt, Instant updatedAt) {
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

    public static TrainingProgramEntity fromDomain(TrainingProgram program) {
        return new TrainingProgramEntity(
            program.getId(), program.getTitle(), program.getCategory(), program.getDifficulty(),
            program.getLanguage(), program.getDurationHours(), program.getMaxSeats(),
            program.getStatus(), program.getCreatedAt(), program.getUpdatedAt());
    }

    public TrainingProgram toDomain() {
        return new TrainingProgram(id, title, category, difficulty, language, durationHours, maxSeats,
            status, createdAt, updatedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }
    public int getMaxSeats() { return maxSeats; }
    public void setMaxSeats(int maxSeats) { this.maxSeats = maxSeats; }
    public TrainingStatus getStatus() { return status; }
    public void setStatus(TrainingStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
