package com.sporekart.training.application.service;

import com.sporekart.training.domain.model.TrainingProgram;
import com.sporekart.training.domain.repository.TrainingRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private final TrainingRepositoryPort repositoryPort;

    public TrainingService(TrainingRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public TrainingProgram create(String title, String category, String difficulty, String language,
            int durationHours, int maxSeats) {
        TrainingProgram program = TrainingProgram.create(title, category, difficulty, language, durationHours,
                maxSeats);
        return repositoryPort.save(program);
    }

    public Optional<TrainingProgram> getById(String id) {
        return repositoryPort.findById(id);
    }

    public List<TrainingProgram> listAll() {
        return repositoryPort.findAll();
    }

    public TrainingProgram publish(String id) {
        TrainingProgram program = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));
        return repositoryPort.save(program.publish());
    }
}
