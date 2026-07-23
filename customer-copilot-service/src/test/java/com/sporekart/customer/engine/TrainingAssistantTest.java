package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.TrainingCourse;
import com.sporekart.customer.infrastructure.training.TrainingServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingAssistantTest {

    @Mock
    private TrainingServiceClient trainingServiceClient;

    private TrainingAssistant trainingAssistant;

    private TrainingCourse course1;
    private TrainingCourse course2;

    @BeforeEach
    void setUp() {
        trainingAssistant = new TrainingAssistant(trainingServiceClient);

        course1 = new TrainingCourse("CRS-001", "Mushroom Cultivation 101",
            "Beginner course", "Cultivation", "BEGINNER", "4 weeks",
            "Dr. Mycelium", OffsetDateTime.now().plusDays(14), "OPEN");

        course2 = new TrainingCourse("CRS-002", "Advanced Oyster Farming",
            "Advanced techniques", "Cultivation", "ADVANCED", "8 weeks",
            "Dr. Mycelium", OffsetDateTime.now().plusDays(30), "OPEN");
    }

    @Test
    void recommendCoursesByCategoryReturnsMatchingCourses() {
        when(trainingServiceClient.searchCourses(isNull(), any()))
            .thenReturn(List.of(course1, course2));

        var results = trainingAssistant.recommendCourses("Cultivation");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(c -> assertEquals("Cultivation", c.category()));
    }

    @Test
    void searchTrainingByKeywordReturnsMatchingResults() {
        when(trainingServiceClient.searchCourses(eq("Mushroom"), any()))
            .thenReturn(List.of(course1));

        var results = trainingAssistant.searchTraining("Mushroom");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).title().toLowerCase().contains("mushroom"));
    }

    @Test
    void getEnrollmentProcessReturnsSteps() {
        var enrollmentInfo = new LinkedHashMap<String, Object>();
        enrollmentInfo.put("courseId", "CRS-001");
        enrollmentInfo.put("steps", List.of(
            "Submit enrollment form",
            "Make payment",
            "Receive confirmation email",
            "Access course materials"
        ));

        when(trainingServiceClient.getEnrollmentInfo("CRS-001")).thenReturn(enrollmentInfo);

        var result = trainingAssistant.getEnrollmentProcess("CRS-001");

        assertNotNull(result);
        assertTrue(result.containsKey("steps"));
        var steps = (List<String>) result.get("steps");
        assertNotNull(steps);
        assertFalse(steps.isEmpty());
    }

    @Test
    void getSchedulesReturnsFutureDates() {
        var futureBatch = Map.of(
            "date", OffsetDateTime.now().plusDays(14).toString(),
            "seatsRemaining", 15,
            "location", "Online"
        );

        when(trainingServiceClient.getUpcomingBatches("CRS-001"))
            .thenReturn(List.of(futureBatch));

        var schedules = trainingAssistant.getSchedulesWithFutureDates("CRS-001");

        assertNotNull(schedules);
        assertFalse(schedules.isEmpty());
    }

    @Test
    void getTrainingFAQsReturnsQuestions() {
        var faqs = trainingAssistant.getTrainingFAQs();

        assertNotNull(faqs);
        assertFalse(faqs.isEmpty());
        faqs.forEach(faq -> {
            assertTrue(faq.containsKey("question"));
            assertTrue(faq.containsKey("answer"));
            assertNotNull(faq.get("question"));
            assertNotNull(faq.get("answer"));
        });
    }

    @Test
    void recommendCoursesWithNoCategoryReturnsAll() {
        when(trainingServiceClient.searchCourses(isNull(), any()))
            .thenReturn(List.of(course1, course2));

        var results = trainingAssistant.recommendCourses(null);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void searchTrainingWithNoMatchReturnsEmpty() {
        when(trainingServiceClient.searchCourses(anyString(), any()))
            .thenReturn(List.of());

        var results = trainingAssistant.searchTraining("zzzznonexistent");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
