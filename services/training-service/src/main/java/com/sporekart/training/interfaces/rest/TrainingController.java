package com.sporekart.training.interfaces.rest;

import com.sporekart.training.application.service.TrainingService;
import com.sporekart.training.domain.model.TrainingProgram;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgram>> list() {
        return ResponseEntity.ok(trainingService.listAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingProgram> create(@RequestBody CreateTrainingRequest request) {
        TrainingProgram program = trainingService.create(request.title(), request.category(), request.difficulty(),
                request.language(), request.durationHours(), request.maxSeats());
        return ResponseEntity.status(HttpStatus.CREATED).body(program);
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainingProgram> publish(@PathVariable String id) {
        return ResponseEntity.ok(trainingService.publish(id));
    }

    public record CreateTrainingRequest(String title, String category, String difficulty, String language,
            int durationHours, int maxSeats) {
    }
}
