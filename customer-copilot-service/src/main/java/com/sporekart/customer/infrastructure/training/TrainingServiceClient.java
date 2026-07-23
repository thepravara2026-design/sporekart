package com.sporekart.customer.infrastructure.training;

import com.sporekart.customer.copilot.domain.TrainingCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class TrainingServiceClient {

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceClient.class);

    private final List<TrainingCourse> courses = new CopyOnWriteArrayList<>();
    private final Map<String, Map<String, Object>> courseMetadata = new HashMap<>();

    public TrainingServiceClient() {
        log.info("Initializing TrainingServiceClient with simulated training data");
        seedCourse("CRS-001", "Mushroom Cultivation 101", "Perfect for absolute beginners. Learn the basics of mushroom growing.",
            "Cultivation", "BEGINNER", "4 weeks", "Dr. Mycelium", OffsetDateTime.now().plusDays(14));
        seedCourse("CRS-002", "Advanced Oyster Mushroom Farming", "Scaling up oyster production for small commercial operations.",
            "Cultivation", "ADVANCED", "8 weeks", "Dr. Mycelium", OffsetDateTime.now().plusDays(30));
        seedCourse("CRS-003", "Mushroom Nutrition and Health", "Understanding the nutritional and medicinal properties of mushrooms.",
            "Health", "BEGINNER", "2 weeks", "NutriMyco Institute", OffsetDateTime.now().plusDays(7));
        seedCourse("CRS-004", "Commercial Shiitake Production", "Large-scale shiitake cultivation on logs and sawdust blocks.",
            "Cultivation", "ADVANCED", "10 weeks", "Shiitake Masters", OffsetDateTime.now().plusDays(45));
        seedCourse("CRS-005", "Mushroom Business Planning", "Starting and scaling a mushroom farming business.",
            "Business", "INTERMEDIATE", "6 weeks", "AgriBusiness Academy", OffsetDateTime.now().plusDays(21));
        seedCourse("CRS-006", "Indoor Mushroom Farming", "Setting up a profitable indoor mushroom farm in any climate.",
            "Cultivation", "INTERMEDIATE", "6 weeks", "IndoorGrow Pro", OffsetDateTime.now().plusDays(10));
        seedCourse("CRS-007", "Medicinal Mushroom Cultivation", "Growing reishi, lion's mane, and cordyceps for supplements.",
            "Cultivation", "ADVANCED", "8 weeks", "MedicMyco Labs", OffsetDateTime.now().plusDays(60));
        seedCourse("CRS-008", "Mushroom Spawn Production", "Learn to produce your own spawn and cultures.",
            "Cultivation", "ADVANCED", "12 weeks", "SpawnTech Inc.", OffsetDateTime.now().plusDays(90));
        seedCourse("CRS-009", "Sustainable Mushroom Farming", "Eco-friendly and organic mushroom cultivation practices.",
            "Sustainability", "INTERMEDIATE", "4 weeks", "EcoMushroom", OffsetDateTime.now().plusDays(20));
        seedCourse("CRS-010", "Mushroom Cooking and Preservation", "Culinary techniques and preservation methods for mushrooms.",
            "Lifestyle", "BEGINNER", "2 weeks", "Chef Fungi", OffsetDateTime.now().plusDays(5));
    }

    private void seedCourse(String id, String title, String description, String category,
                            String level, String duration, String instructor, OffsetDateTime nextBatch) {
        var course = new TrainingCourse(id, title, description, category, level, duration,
            instructor, nextBatch, "OPEN");
        courses.add(course);
        var meta = new HashMap<String, Object>();
        meta.put("syllabus", List.of("Week 1: Introduction", "Week 2: Core Concepts", "Week 3: Advanced Topics", "Week 4: Practical Application"));
        meta.put("prerequisites", level.equals("BEGINNER") ? List.of() : List.of("Basic mushroom knowledge"));
        meta.put("maxStudents", 30);
        meta.put("price", level.equals("ADVANCED") ? 299.99 : level.equals("INTERMEDIATE") ? 199.99 : 99.99);
        meta.put("rating", 4.7 + Math.random() * 0.3);
        meta.put("materials", List.of("Course workbook", "Video lectures", "Lab access", "Certificate"));
        meta.put("trainerBio", instructor + " has over 15 years of experience in mushroom cultivation and education.");
        meta.put("trainerEmail", instructor.toLowerCase().replaceAll("\\s+", ".") + "@sporekart.com");
        meta.put("upcomingBatches", List.of(
            Map.of("date", nextBatch.toString(), "seatsRemaining", 15, "location", "Online"),
            Map.of("date", nextBatch.plusDays(30).toString(), "seatsRemaining", 22, "location", "Online"),
            Map.of("date", nextBatch.plusDays(60).toString(), "seatsRemaining", 28, "location", "Online")
        ));
        courseMetadata.put(id, meta);
    }

    public List<TrainingCourse> searchCourses(String query, Map<String, String> filters) {
        log.debug("TrainingServiceClient.searchCourses called with query='{}', filters={}", query, filters);
        return courses.stream()
            .filter(c -> {
                if (query == null || query.isBlank()) return true;
                String q = query.toLowerCase();
                return c.title().toLowerCase().contains(q)
                    || c.description().toLowerCase().contains(q)
                    || c.category().toLowerCase().contains(q);
            })
            .filter(c -> {
                if (filters == null) return true;
                String category = filters.get("category");
                if (category != null && !c.category().equalsIgnoreCase(category)) return false;
                String level = filters.get("level");
                if (level != null && !c.level().equalsIgnoreCase(level)) return false;
                String instructor = filters.get("instructor");
                if (instructor != null && !c.instructor().toLowerCase().contains(instructor.toLowerCase())) return false;
                return true;
            })
            .toList();
    }

    public Optional<TrainingCourse> getCourse(String id) {
        log.debug("TrainingServiceClient.getCourse called for id='{}'", id);
        return courses.stream().filter(c -> c.id().equals(id)).findFirst();
    }

    public Map<String, Object> getEnrollmentInfo(String courseId) {
        log.debug("TrainingServiceClient.getEnrollmentInfo called for courseId='{}'", courseId);
        var meta = courseMetadata.get(courseId);
        if (meta == null) {
            return Map.of("error", "Course not found");
        }
        var result = new LinkedHashMap<String, Object>();
        result.put("courseId", courseId);
        result.put("price", meta.get("price"));
        result.put("maxStudents", meta.get("maxStudents"));
        result.put("prerequisites", meta.get("prerequisites"));
        result.put("materials", meta.get("materials"));
        result.put("steps", List.of(
            "Submit enrollment form",
            "Make payment (credit card / PayPal)",
            "Receive confirmation email",
            "Access course materials on day 1"
        ));
        result.put("refundPolicy", "Full refund within 7 days of enrollment");
        return result;
    }

    public List<Map<String, Object>> getUpcomingBatches(String courseId) {
        log.debug("TrainingServiceClient.getUpcomingBatches called for courseId='{}'", courseId);
        var meta = courseMetadata.get(courseId);
        if (meta == null) return List.of();
        return (List<Map<String, Object>>) meta.getOrDefault("upcomingBatches", List.of());
    }
}
