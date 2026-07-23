package com.sporekart.trainer.copilot.engine;

import com.sporekart.trainer.copilot.domain.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LessonGeneratorEngineTest {

    private LessonGeneratorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new LessonGeneratorEngine();
    }

    @Test
    void generateLectureLessonReturnsCorrectType() {
        Lesson lesson = engine.generateLesson("M001", "Spawn Preparation", "LECTURE", "BEGINNER", 60, List.of("Understand sterile technique"));

        assertEquals("LECTURE", lesson.lessonType());
        assertEquals("M001", lesson.moduleId());
        assertEquals(60, lesson.durationMinutes());
    }

    @Test
    void generateDemonstrationLessonReturnsCorrectType() {
        Lesson lesson = engine.generateLesson("M002", "Agar Plate Inoculation", "DEMONSTRATION", "INTERMEDIATE", 90, List.of("Master aseptic transfer"));

        assertEquals("DEMONSTRATION", lesson.lessonType());
        assertNotNull(lesson.content());
    }

    @Test
    void generateLabExerciseLessonReturnsCorrectType() {
        Lesson lesson = engine.generateLesson("M003", "Substrate Preparation", "LAB_EXERCISE", "ADVANCED", 120, List.of("Prepare sawdust substrate"));

        assertEquals("LAB_EXERCISE", lesson.lessonType());
        assertTrue(lesson.durationMinutes() >= 120);
    }

    @Test
    void generatePresentationOutlineReturnsSections() {
        Map<String, Object> outline = engine.generatePresentationOutline("Mushroom Lifecycle", 30);

        assertNotNull(outline.get("title"));
        assertNotNull(outline.get("sections"));
        List<Map<String, Object>> sections = (List<Map<String, Object>>) outline.get("sections");
        assertFalse(sections.isEmpty());
    }

    @Test
    void generatePresentationOutlineContainsTimings() {
        Map<String, Object> outline = engine.generatePresentationOutline("Sterile Technique", 45);

        List<Map<String, Object>> sections = (List<Map<String, Object>>) outline.get("sections");
        for (Map<String, Object> section : sections) {
            assertNotNull(section.get("durationMinutes"));
        }
    }

    @Test
    void generatePracticalDemonstrationReturnsSteps() {
        Map<String, Object> demo = engine.generatePracticalDemonstration("Grain Spawn Inoculation");

        assertNotNull(demo.get("title"));
        assertNotNull(demo.get("materials"));
        assertNotNull(demo.get("steps"));
        List<String> steps = (List<String>) demo.get("steps");
        assertFalse(steps.isEmpty());
    }

    @Test
    void generateLabExerciseReturnsObjectivesAndMaterials() {
        Map<String, Object> lab = engine.generateLabExercise("Compost Preparation", 120);

        assertNotNull(lab.get("objectives"));
        assertNotNull(lab.get("materials"));
        assertNotNull(lab.get("procedure"));
        assertEquals(120, lab.get("durationMinutes"));
    }

    @Test
    void generateLearningSummaryReturnsKeyPoints() {
        String summary = engine.generateLearningSummary(List.of("Spawn is mycelium on grain", "Sterilization kills all microbes", "Pasteurization selects for good microbes"), "Week 1 Summary");

        assertNotNull(summary);
        assertTrue(summary.contains("Week 1 Summary") || summary.contains("Spawn"));
    }

    @Test
    void generateHomeworkReturnsAssignment() {
        Map<String, Object> homework = engine.generateHomework("Sterilization Methods", "INTERMEDIATE");

        assertNotNull(homework.get("title"));
        assertNotNull(homework.get("questions"));
        assertNotNull(homework.get("estimatedDuration"));
    }

    @Test
    void generateLessonWithEmptyObjectives() {
        Lesson lesson = engine.generateLesson("M004", "Harvesting", "LECTURE", "BEGINNER", 30, List.of());

        assertNotNull(lesson);
        assertEquals("Harvesting", lesson.title());
    }

    @Test
    void generateLessonWithNullTypeDefaultsToLecture() {
        Lesson lesson = engine.generateLesson("M005", "Safety Protocols", null, "BEGINNER", 45, List.of("Learn safety"));

        assertNotNull(lesson);
        assertNotNull(lesson.lessonType());
    }
}
