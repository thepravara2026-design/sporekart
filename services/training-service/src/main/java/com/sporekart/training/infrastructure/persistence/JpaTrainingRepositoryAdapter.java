package com.sporekart.training.infrastructure.persistence;

import com.sporekart.training.domain.model.GrowerProfile;
import com.sporekart.training.domain.model.TrainingProgram;
import com.sporekart.training.domain.repository.TrainingRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaTrainingRepositoryAdapter implements TrainingRepositoryPort {

    private final TrainingJpaRepository trainingJpaRepository;
    private final GrowerProfileJpaRepository growerProfileJpaRepository;

    public JpaTrainingRepositoryAdapter(TrainingJpaRepository trainingJpaRepository,
                                        GrowerProfileJpaRepository growerProfileJpaRepository) {
        this.trainingJpaRepository = trainingJpaRepository;
        this.growerProfileJpaRepository = growerProfileJpaRepository;
    }

    @Override
    public TrainingProgram save(TrainingProgram program) {
        return trainingJpaRepository.save(TrainingProgramEntity.fromDomain(program)).toDomain();
    }

    @Override
    public Optional<TrainingProgram> findById(String id) {
        return trainingJpaRepository.findById(id).map(TrainingProgramEntity::toDomain);
    }

    @Override
    public List<TrainingProgram> findAll() {
        return trainingJpaRepository.findAll().stream().map(TrainingProgramEntity::toDomain).toList();
    }
}
