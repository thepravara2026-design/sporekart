package com.sporekart.training.infrastructure.persistence;

import com.sporekart.training.domain.model.TrainingProgram;
import com.sporekart.training.domain.repository.TrainingRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTrainingRepository implements TrainingRepositoryPort {
    private final Map<String, TrainingProgram> programsById = new ConcurrentHashMap<>();

    @Override
    public TrainingProgram save(TrainingProgram program) {
        programsById.put(program.getId(), program);
        return program;
    }

    @Override
    public Optional<TrainingProgram> findById(String id) {
        return Optional.ofNullable(programsById.get(id));
    }

    @Override
    public List<TrainingProgram> findAll() {
        return new ArrayList<>(programsById.values());
    }
}
