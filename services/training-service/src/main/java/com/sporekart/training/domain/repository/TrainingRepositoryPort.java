package com.sporekart.training.domain.repository;

import com.sporekart.training.domain.model.TrainingProgram;

import java.util.List;
import java.util.Optional;

public interface TrainingRepositoryPort {
    TrainingProgram save(TrainingProgram program);

    Optional<TrainingProgram> findById(String id);

    List<TrainingProgram> findAll();
}
