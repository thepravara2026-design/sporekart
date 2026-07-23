package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.TrainingCourse;
import com.sporekart.customer.infrastructure.training.TrainingServiceClient;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TrainingAssistant {

    private final TrainingServiceClient trainingServiceClient;

    public TrainingAssistant(TrainingServiceClient trainingServiceClient) {
        this.trainingServiceClient = trainingServiceClient;
    }

    public List<TrainingCourse> recommendCourses(String category) {
        var filters = new HashMap<String, String>();
        if (category != null && !category.isBlank()) {
            filters.put("category", category);
        }
        return trainingServiceClient.searchCourses(null, filters);
    }

    public List<TrainingCourse> searchTraining(String keyword) {
        return trainingServiceClient.searchCourses(keyword, null);
    }

    public Map<String, Object> getEnrollmentProcess(String courseId) {
        return trainingServiceClient.getEnrollmentInfo(courseId);
    }

    public List<Map<String, Object>> getSchedules(String courseId) {
        return trainingServiceClient.getUpcomingBatches(courseId);
    }

    public List<Map<String, String>> getTrainingFAQs() {
        return List.of(
            Map.of("question", "How do I enroll in a course?", "answer", "Browse courses and click 'Enroll'. Complete payment to secure your spot."),
            Map.of("question", "What is the refund policy?", "answer", "Full refund within 7 days of enrollment."),
            Map.of("question", "Are courses online or in-person?", "answer", "All courses are offered online with optional in-person workshops."),
            Map.of("question", "How long do I have access to course materials?", "answer", "You have lifetime access to all course materials."),
            Map.of("question", "Do I get a certificate?", "answer", "Yes, a completion certificate is provided for all courses.")
        );
    }

    public List<Map<String, Object>> getSchedulesWithFutureDates(String courseId) {
        return trainingServiceClient.getUpcomingBatches(courseId).stream()
            .filter(b -> {
                var dateStr = (String) b.get("date");
                if (dateStr == null) return false;
                try {
                    return OffsetDateTime.parse(dateStr).isAfter(OffsetDateTime.now());
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }
}
