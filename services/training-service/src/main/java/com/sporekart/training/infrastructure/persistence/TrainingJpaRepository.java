package com.sporekart.training.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingJpaRepository extends JpaRepository<TrainingProgramEntity, String> {
}
