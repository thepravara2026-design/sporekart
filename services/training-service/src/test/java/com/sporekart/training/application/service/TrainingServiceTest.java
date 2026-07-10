package com.sporekart.training.application.service;

import com.sporekart.training.domain.model.TrainingProgram;
import com.sporekart.training.domain.model.TrainingStatus;
import com.sporekart.training.infrastructure.persistence.InMemoryTrainingRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingServiceTest {
    @Test
    void createAndPublishTraining() {
        TrainingService service = new TrainingService(new InMemoryTrainingRepository());
        TrainingProgram program = service.create("Mushroom Basics", "Cultivation", "Beginner", "Kannada", 6, 40);

        assertNotNull(program);
        assertEquals(TrainingStatus.DRAFT, program.getStatus());

        TrainingProgram published = service.publish(program.getId());
        assertEquals(TrainingStatus.PUBLISHED, published.getStatus());
    }
}
