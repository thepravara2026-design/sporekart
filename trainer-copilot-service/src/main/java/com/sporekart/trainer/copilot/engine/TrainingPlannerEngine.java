package com.sporekart.trainer.copilot.engine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainingPlannerEngine {

    private static final Logger log = LoggerFactory.getLogger(TrainingPlannerEngine.class);

    private static final String SEED_COURSE = "Mushroom Cultivation Mastery";
    private static final int SEED_WEEKS = 8;
    private static final String SEED_DIFFICULTY = "Beginner to Advanced";

    public TrainingPlannerEngine() {
        log.info("TrainingPlannerEngine initialized");
    }

    public Map<String, Object> generateTrainingPlan(String courseName, int durationWeeks, String difficulty) {
        log.info("Generating training plan for course='{}', duration={} weeks, difficulty='{}'", courseName, durationWeeks, difficulty);

        Map<String, Object> plan = new LinkedHashMap<>();

        Map<String, String> agenda = new LinkedHashMap<>();
        agenda.put("course", courseName);
        agenda.put("duration", durationWeeks + " weeks");
        agenda.put("difficulty", difficulty);
        agenda.put("mode", "Blended (Online + Hands-on Lab)");
        agenda.put("prerequisites", "Basic biology knowledge, willingness to work in sterile environment");
        plan.put("agenda", agenda);

        List<Map<String, Object>> schedule = new ArrayList<>();
        for (int w = 1; w <= durationWeeks; w++) {
            Map<String, Object> weekEntry = new LinkedHashMap<>();
            weekEntry.put("week", w);
            weekEntry.put("theme", getWeekTheme(courseName, w));
            weekEntry.put("topics", getWeekTopics(courseName, w));
            weekEntry.put("hours", getWeekHours(w));
            schedule.add(weekEntry);
        }
        plan.put("schedule", schedule);

        List<Map<String, Object>> milestones = new ArrayList<>();
        int milestoneStep = Math.max(1, durationWeeks / 4);
        for (int w = milestoneStep; w <= durationWeeks; w += milestoneStep) {
            Map<String, Object> milestone = new LinkedHashMap<>();
            milestone.put("week", w);
            milestone.put("milestone", getMilestone(courseName, w));
            milestone.put("criteria", getMilestoneCriteria(courseName, w));
            milestones.add(milestone);
        }
        if (milestones.isEmpty() || milestones.get(milestones.size() - 1).get("week") != (Integer) durationWeeks) {
            Map<String, Object> finalMilestone = new LinkedHashMap<>();
            finalMilestone.put("week", durationWeeks);
            finalMilestone.put("milestone", "Course Completion & Certification");
            finalMilestone.put("criteria", "Pass final assessment with >= 60%, complete all practicals");
            milestones.add(finalMilestone);
        }
        plan.put("milestones", milestones);

        List<String> checklist = new ArrayList<>();
        checklist.add("Complete pre-course reading materials");
        checklist.add("Attend all lab safety orientations");
        checklist.add("Submit weekly progress reports");
        checklist.add("Complete all practical exercises");
        checklist.add("Pass weekly quizzes (>= 50%)");
        checklist.add("Submit final project report");
        checklist.add("Complete peer review assignments");
        for (int w = 1; w <= durationWeeks; w++) {
            checklist.add("Week " + w + ": " + getWeekChecklistItem(courseName, w));
        }
        plan.put("checklist", checklist);

        List<String> objectives = new ArrayList<>();
        objectives.add("Understand mushroom biology and lifecycle");
        objectives.add("Master sterile techniques and contamination control");
        objectives.add("Prepare and sterilize cultivation substrates");
        objectives.add("Perform spawn inoculation and incubation");
        objectives.add("Manage fruiting conditions for optimal yield");
        objectives.add("Identify and treat common mushroom diseases");
        objectives.add("Harvest, package, and store mushrooms commercially");
        objectives.add("Develop a complete cultivation business plan");
        plan.put("learningObjectives", objectives);

        log.info("Training plan generated for course='{}' with {} milestones", courseName, milestones.size());
        return plan;
    }

    public Map<String, Object> generateDailySchedule(LocalDate date, String batchId) {
        log.info("Generating daily schedule for date={}, batchId='{}'", date, batchId);

        Map<String, Object> schedule = new LinkedHashMap<>();
        schedule.put("date", date.toString());
        schedule.put("batchId", batchId);
        schedule.put("dayOfWeek", date.getDayOfWeek().toString());

        List<Map<String, Object>> timeSlots = new ArrayList<>();

        timeSlots.add(buildTimeSlot("09:00", "09:30", "Morning Briefing", "Review previous day, preview today's activities", "All"));
        timeSlots.add(buildTimeSlot("09:30", "11:00", "Theory Session", "Core lecture with multimedia presentation", "Lecture Hall A"));
        timeSlots.add(buildTimeSlot("11:00", "11:15", "Tea Break", null, "Cafeteria"));
        timeSlots.add(buildTimeSlot("11:15", "13:00", "Practical Lab", "Hands-on cultivation exercise in sterile lab", "Lab 2 (Laminar Flow Area)"));
        timeSlots.add(buildTimeSlot("13:00", "14:00", "Lunch Break", null, "Cafeteria"));
        timeSlots.add(buildTimeSlot("14:00", "15:30", "Group Activity", "Collaborative problem-solving or case study", "Seminar Room B"));
        timeSlots.add(buildTimeSlot("15:30", "16:00", "Assessment / Quiz", "Daily knowledge check", "Online Portal"));
        timeSlots.add(buildTimeSlot("16:00", "16:30", "Q&A and Wrap-up", "Clarify doubts, assign readings", "Lecture Hall A"));
        timeSlots.add(buildTimeSlot("16:30", "17:30", "Self-Study / Lab Practice", "Optional extended lab access", "Lab 2"));

        schedule.put("timeSlots", timeSlots);
        log.info("Daily schedule generated with {} time slots", timeSlots.size());
        return schedule;
    }

    public Map<String, Object> generateWeeklyCurriculum(int weekNumber, String courseName) {
        log.info("Generating weekly curriculum for week={}, course='{}'", weekNumber, courseName);

        Map<String, Object> curriculum = new LinkedHashMap<>();
        curriculum.put("week", weekNumber);
        curriculum.put("course", courseName);
        curriculum.put("theme", getWeekTheme(courseName, weekNumber));
        curriculum.put("totalHours", getWeekHours(weekNumber));

        List<Map<String, Object>> topics = new ArrayList<>();
        List<String> topicNames = getWeekTopics(courseName, weekNumber);
        for (int i = 0; i < topicNames.size(); i++) {
            Map<String, Object> topic = new LinkedHashMap<>();
            topic.put("sequence", i + 1);
            topic.put("title", topicNames.get(i));
            topic.put("durationHours", 1.5);
            topic.put("mode", i % 2 == 0 ? "Lecture" : "Lab");
            topics.add(topic);
        }
        curriculum.put("topics", topics);

        List<String> activities = new ArrayList<>();
        activities.add("Hands-on lab: " + getWeekLabActivity(courseName, weekNumber));
        activities.add("Case study discussion");
        activities.add("Group presentation on " + topicNames.get(0));
        activities.add("Daily journal entry");
        curriculum.put("activities", activities);

        List<Map<String, Object>> assessments = new ArrayList<>();
        assessments.add(Map.of("type", "Quiz", "description", "Week " + weekNumber + " quiz on " + topicNames.get(0), "duration", 20, "weightage", 5));
        assessments.add(Map.of("type", "Practical", "description", getWeekLabActivity(courseName, weekNumber) + " evaluation", "duration", 45, "weightage", 10));
        curriculum.put("assessments", assessments);

        curriculum.put("resources", getWeekResources(weekNumber));

        log.info("Weekly curriculum generated for week {} with {} topics", weekNumber, topics.size());
        return curriculum;
    }

    public Map<String, Object> generateLearningRoadmap(List<String> topics, int totalHours) {
        log.info("Generating learning roadmap with {} topics, {} total hours", topics.size(), totalHours);

        Map<String, Object> roadmap = new LinkedHashMap<>();
        roadmap.put("title", "Structured Learning Path");
        roadmap.put("totalHours", totalHours);
        roadmap.put("estimatedDuration", Math.max(1, totalHours / 20) + " weeks (at ~20 hrs/week)");

        List<Map<String, Object>> path = new ArrayList<>();
        int hoursPerTopic = totalHours / Math.max(1, topics.size());
        int remaining = totalHours;

        for (int i = 0; i < topics.size(); i++) {
            int allocated = (i == topics.size() - 1) ? remaining : hoursPerTopic;
            remaining -= allocated;

            Map<String, Object> step = new LinkedHashMap<>();
            step.put("step", i + 1);
            step.put("topic", topics.get(i));
            step.put("hours", allocated);

            List<String> subTopics = new ArrayList<>();
            subTopics.add(topics.get(i) + " - Fundamentals");
            subTopics.add(topics.get(i) + " - Intermediate Concepts");
            subTopics.add(topics.get(i) + " - Advanced Techniques");
            subTopics.add(topics.get(i) + " - Practical Application");
            step.put("subTopics", subTopics);

            List<String> resources = new ArrayList<>();
            resources.add("Video lecture: " + topics.get(i));
            resources.add("Reference manual chapter on " + topics.get(i));
            resources.add("Lab exercise: " + topics.get(i) + " practice");
            resources.add("Research paper: Recent advances in " + topics.get(i));
            step.put("resources", resources);

            path.add(step);
        }
        roadmap.put("path", path);

        List<Map<String, Object>> milestones = new ArrayList<>();
        int checkpoints = Math.min(4, topics.size());
        int stepSize = Math.max(1, topics.size() / checkpoints);
        for (int i = stepSize - 1; i < topics.size(); i += stepSize) {
            milestones.add(Map.of(
                "atStep", i + 1,
                "topic", topics.get(i),
                "assessmentType", i % 2 == 0 ? "Quiz" : "Practical"
            ));
        }
        roadmap.put("milestones", milestones);

        log.info("Learning roadmap generated with {} steps", path.size());
        return roadmap;
    }

    private Map<String, Object> buildTimeSlot(String from, String to, String activity, String description, String location) {
        Map<String, Object> slot = new LinkedHashMap<>();
        slot.put("start", from);
        slot.put("end", to);
        slot.put("activity", activity);
        slot.put("description", description != null ? description : "");
        slot.put("location", location);
        return slot;
    }

    private String getWeekTheme(String course, int week) {
        if (course.contains("Mushroom") || course.contains("mushroom") || course.contains("Cultivation")) {
            return switch (week) {
                case 1 -> "Introduction to Mushroom Biology & Cultivation";
                case 2 -> "Spawn Preparation & Sterilization Techniques";
                case 3 -> "Substrate Formulation & Preparation";
                case 4 -> "Inoculation & Incubation Management";
                case 5 -> "Fruiting Conditions & Environment Control";
                case 6 -> "Disease Management & Quality Control";
                case 7 -> "Harvesting, Post-Harvest & Packaging";
                case 8 -> "Business Planning, Certification & Final Assessment";
                default -> "Week " + week + " - Advanced Topics";
            };
        }
        return "Week " + week + " - " + course + " Module";
    }

    private List<String> getWeekTopics(String course, int week) {
        if (course.contains("Mushroom") || course.contains("mushroom") || course.contains("Cultivation")) {
            return switch (week) {
                case 1 -> List.of("Mushroom Lifecycle & Taxonomy", "Cultivation Methods Overview", "Lab Safety & Sterile Technique Fundamentals");
                case 2 -> List.of("Spawn Production Techniques", "Sterilization vs Pasteurization", "Laminar Flow & Aseptic Work");
                case 3 -> List.of("Straw-Based Substrates", "Sawdust & Supplemented Substrates", "Compost Preparation for Button Mushrooms");
                case 4 -> List.of("Grain Spawn Inoculation", "Bulk Substrate Spawning", "Incubation Parameters & Monitoring");
                case 5 -> List.of("Pinning Induction & Casing Layer", "Temperature, Humidity & FAE Control", "Lighting Strategies for Fruiting");
                case 6 -> List.of("Common Diseases (Green Mold, Cobweb, Bacterial Blotch)", "Integrated Pest Management", "Quality Assurance Protocols");
                case 7 -> List.of("Harvesting Techniques by Species", "Post-Harvest Handling & Storage", "Packaging (Vacuum, Fresh, Dried)");
                case 8 -> List.of("Cultivation Business Plan", "Certification & Regulatory Compliance", "Final Assessment & Review");
                default -> List.of("Advanced Topic A", "Advanced Topic B", "Advanced Practical");
            };
        }
        return List.of("Module " + week + " Topic 1", "Module " + week + " Topic 2", "Module " + week + " Practical");
    }

    private double getWeekHours(int week) {
        return switch (week) {
            case 1 -> 20.0;
            case 8 -> 15.0;
            default -> 25.0;
        };
    }

    private String getMilestone(String course, int week) {
        if (course.contains("Mushroom") || course.contains("mushroom") || course.contains("Cultivation")) {
            return switch (week) {
                case 2 -> "Demonstrate sterile technique proficiency";
                case 4 -> "Successfully inoculate and incubate first spawn batch";
                case 6 -> "Manage fruiting conditions and identify diseases";
                case 8 -> "Complete final assessment and business plan";
                default -> "Complete Week " + week + " objectives";
            };
        }
        return "Complete Week " + week + " objectives";
    }

    private String getMilestoneCriteria(String course, int week) {
        return switch (week) {
            case 2 -> "Score >= 80% on sterile technique practical";
            case 4 -> "At least 90% of spawn jars show healthy mycelial growth";
            case 6 -> "Achieve >= 70% yield target and correctly identify 5 diseases";
            case 8 -> "Assessment score >= 60%, practical completion >= 80%";
            default -> "Complete all assigned tasks with >= 70% score";
        };
    }

    private String getWeekChecklistItem(String course, int week) {
        if (course.contains("Mushroom") || course.contains("mushroom") || course.contains("Cultivation")) {
            return switch (week) {
                case 1 -> "Read Chapter 1-3 of cultivation manual, complete safety quiz";
                case 2 -> "Prepare 5 grain spawn jars under laminar flow";
                case 3 -> "Formulate and pasteurize 10 kg of substrate";
                case 4 -> "Inoculate 10 substrate bags and monitor temperature daily";
                case 5 -> "Record pin formation and adjust humidity/FAE";
                case 6 -> "Submit disease identification journal with photographs";
                case 7 -> "Harvest first flush, weigh yield, package samples";
                case 8 -> "Submit business plan and sit for final exam";
                default -> "Complete week " + week + " assignments";
            };
        }
        return "Complete week " + week + " assignments";
    }

    private String getWeekLabActivity(String course, int week) {
        if (course.contains("Mushroom") || course.contains("mushroom") || course.contains("Cultivation")) {
            return switch (week) {
                case 1 -> "Microscope identification of mushroom spores and mycelium";
                case 2 -> "Sterile technique: pouring agar plates and inoculating";
                case 3 -> "Substrate preparation: straw pasteurization and sawdust supplementation";
                case 4 -> "Bulk spawn run in monotub configuration";
                case 5 -> "Casing layer application and fruiting chamber setup";
                case 6 -> "Disease diagnosis: microscopy and cultural characteristics";
                case 7 -> "Harvesting, grading, and vacuum packaging demonstration";
                case 8 -> "Final project: complete cultivation cycle presentation";
                default -> "Lab practical for week " + week;
            };
        }
        return "Lab practical for week " + week;
    }

    private List<String> getWeekResources(int week) {
        return List.of(
            "Video tutorial - Week " + week + " core concepts",
            "Lab manual chapter " + week,
            "Research article reference",
            "Online quiz practice set"
        );
    }
}
