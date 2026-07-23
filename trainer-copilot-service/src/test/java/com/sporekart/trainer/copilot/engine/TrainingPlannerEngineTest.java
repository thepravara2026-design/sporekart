package com.sporekart.trainer.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingPlannerEngineTest {

    private TrainingPlannerEngine engine;

    @BeforeEach
    void setUp() {
        engine = new TrainingPlannerEngine();
    }

    @Test
    void generateTrainingPlanContainsAllSections() {
        Map<String, Object> plan = engine.generateTrainingPlan("Mushroom Cultivation Mastery", 8, "Beginner to Advanced");

        assertNotNull(plan.get("agenda"));
        assertNotNull(plan.get("schedule"));
        assertNotNull(plan.get("milestones"));
        assertNotNull(plan.get("checklist"));
        assertNotNull(plan.get("learningObjectives"));
    }

    @Test
    void generateTrainingPlanHasCorrectWeekCount() {
        Map<String, Object> plan = engine.generateTrainingPlan("Mushroom Cultivation Mastery", 8, "Beginner to Advanced");
        List<Map<String, Object>> schedule = (List<Map<String, Object>>) plan.get("schedule");

        assertEquals(8, schedule.size());
    }

    @Test
    void generateTrainingPlanHasMilestones() {
        Map<String, Object> plan = engine.generateTrainingPlan("Mushroom Cultivation Mastery", 8, "Beginner to Advanced");
        List<Map<String, Object>> milestones = (List<Map<String, Object>>) plan.get("milestones");

        assertFalse(milestones.isEmpty());
        assertEquals(8, milestones.get(milestones.size() - 1).get("week"));
    }

    @Test
    void generateDailyScheduleHasTimeSlots() {
        Map<String, Object> schedule = engine.generateDailySchedule(LocalDate.of(2026, 7, 23), "B001");

        assertEquals("B001", schedule.get("batchId"));
        List<Map<String, Object>> timeSlots = (List<Map<String, Object>>) schedule.get("timeSlots");
        assertEquals(9, timeSlots.size());
    }

    @Test
    void generateDailyScheduleIncludesAllRequiredActivities() {
        Map<String, Object> schedule = engine.generateDailySchedule(LocalDate.of(2026, 7, 23), "B001");
        List<Map<String, Object>> timeSlots = (List<Map<String, Object>>) schedule.get("timeSlots");

        assertTrue(timeSlots.stream().anyMatch(s -> "Morning Briefing".equals(s.get("activity"))));
        assertTrue(timeSlots.stream().anyMatch(s -> "Theory Session".equals(s.get("activity"))));
        assertTrue(timeSlots.stream().anyMatch(s -> "Practical Lab".equals(s.get("activity"))));
    }

    @Test
    void generateWeeklyCurriculumHasTopicsAndAssessments() {
        Map<String, Object> curriculum = engine.generateWeeklyCurriculum(1, "Mushroom Cultivation Mastery");

        assertEquals(1, curriculum.get("week"));
        List<Map<String, Object>> topics = (List<Map<String, Object>>) curriculum.get("topics");
        List<Map<String, Object>> assessments = (List<Map<String, Object>>) curriculum.get("assessments");

        assertFalse(topics.isEmpty());
        assertFalse(assessments.isEmpty());
    }

    @Test
    void generateWeeklyCurriculumHasActivities() {
        Map<String, Object> curriculum = engine.generateWeeklyCurriculum(3, "Mushroom Cultivation Mastery");
        List<String> activities = (List<String>) curriculum.get("activities");

        assertFalse(activities.isEmpty());
        assertTrue(activities.stream().anyMatch(a -> a.contains("lab")));
    }

    @Test
    void generateLearningRoadmapReturnsSteps() {
        List<String> topics = List.of("Spawn Preparation", "Substrate Formulation", "Inoculation", "Fruiting");
        Map<String, Object> roadmap = engine.generateLearningRoadmap(topics, 80);

        assertEquals("Structured Learning Path", roadmap.get("title"));
        List<Map<String, Object>> path = (List<Map<String, Object>>) roadmap.get("path");
        assertEquals(4, path.size());
    }

    @Test
    void generateLearningRoadmapHasMilestones() {
        List<String> topics = List.of("Spawn Preparation", "Substrate Formulation", "Inoculation", "Fruiting", "Harvesting", "Disease Management");
        Map<String, Object> roadmap = engine.generateLearningRoadmap(topics, 120);

        List<Map<String, Object>> milestones = (List<Map<String, Object>>) roadmap.get("milestones");
        assertFalse(milestones.isEmpty());
    }

    @Test
    void generateLearningRoadmapHoursDistribution() {
        List<String> topics = List.of("Topic A", "Topic B", "Topic C");
        Map<String, Object> roadmap = engine.generateLearningRoadmap(topics, 60);

        List<Map<String, Object>> path = (List<Map<String, Object>>) roadmap.get("path");
        int totalHours = path.stream().mapToInt(s -> (int) s.get("hours")).sum();
        assertEquals(60, totalHours);
    }

    @Test
    void generateTrainingPlanNonMushroomCourse() {
        Map<String, Object> plan = engine.generateTrainingPlan("General Biology", 4, "Beginner");

        assertEquals("General Biology", ((Map<String, String>) plan.get("agenda")).get("course"));
        List<Map<String, Object>> schedule = (List<Map<String, Object>>) plan.get("schedule");
        assertEquals(4, schedule.size());
    }
}
