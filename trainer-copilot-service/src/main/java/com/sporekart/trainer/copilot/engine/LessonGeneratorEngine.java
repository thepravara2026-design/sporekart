package com.sporekart.trainer.copilot.engine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LessonGeneratorEngine {

    private static final Logger log = LoggerFactory.getLogger(LessonGeneratorEngine.class);

    public LessonGeneratorEngine() {
        log.info("LessonGeneratorEngine initialized");
    }

    public Map<String, Object> generateLesson(String moduleId, String topic, String lessonType,
                                               String difficulty, int durationMinutes, List<String> objectives) {
        log.info("Generating lesson: moduleId='{}', topic='{}', type='{}', difficulty='{}', duration={}min",
                moduleId, topic, lessonType, difficulty, durationMinutes);

        Map<String, Object> lesson = new LinkedHashMap<>();
        lesson.put("lessonId", UUID.randomUUID().toString().substring(0, 8));
        lesson.put("moduleId", moduleId);
        lesson.put("topic", topic);
        lesson.put("lessonType", lessonType);
        lesson.put("difficulty", difficulty);
        lesson.put("durationMinutes", durationMinutes);
        lesson.put("generatedAt", LocalDateTime.now().toString());
        lesson.put("objectives", objectives != null && !objectives.isEmpty() ? objectives : defaultObjectives(topic));

        List<Map<String, Object>> sections = generateLessonSections(topic, lessonType, difficulty, durationMinutes);
        lesson.put("sections", sections);

        List<Map<String, Object>> materials = generateLessonMaterials(topic, lessonType, difficulty);
        lesson.put("materials", materials);

        List<Map<String, Object>> activities = generateLessonActivities(topic, lessonType, difficulty);
        lesson.put("activities", activities);

        lesson.put("summary", generateContentSummary(topic, difficulty));
        lesson.put("keyTakeaways", generateKeyTakeaways(topic, difficulty));

        log.info("Lesson generated with {} sections, {} activities", sections.size(), activities.size());
        return lesson;
    }

    public Map<String, Object> generatePresentationOutline(String topic, String lessonType, int slideCount) {
        log.info("Generating presentation outline for topic='{}', type='{}', {} slides", topic, lessonType, slideCount);

        Map<String, Object> outline = new LinkedHashMap<>();
        outline.put("title", topic + " - " + lessonType);
        outline.put("totalSlides", slideCount);
        outline.put("estimatedDurationMinutes", slideCount * 2);

        List<Map<String, Object>> slides = new ArrayList<>();
        int remaining = slideCount;
        int sectionCount = Math.min(6, slideCount - 2);

        slides.add(Map.of("slideNumber", 1, "type", "title", "title", topic, "content", "Title slide with learning objectives"));
        remaining--;

        int perSection = Math.max(1, remaining / Math.max(1, sectionCount));
        for (int i = 1; i <= sectionCount && remaining > 0; i++) {
            int slideCountForSection = Math.min(perSection, remaining);
            for (int j = 0; j < slideCountForSection && remaining > 0; j++) {
                slides.add(Map.of(
                    "slideNumber", slides.size() + 1,
                    "type", j == 0 ? "section-header" : "content",
                    "title", getSectionTitle(topic, i, j),
                    "content", getSectionContent(topic, i, j),
                    "notes", getSpeakerNotes(topic, i, j)
                ));
                remaining--;
            }
        }

        if (remaining > 0) {
            slides.add(Map.of(
                "slideNumber", slides.size() + 1,
                "type", "summary",
                "title", "Summary & Key Takeaways",
                "content", generateContentSummary(topic, "Intermediate"),
                "notes", "Review main points, ask for questions"
            ));
            remaining--;
        }

        if (remaining > 0) {
            slides.add(Map.of(
                "slideNumber", slides.size() + 1,
                "type", "qa",
                "title", "Q&A Session",
                "content", "Open floor for questions and discussion",
                "notes", "Prepare 3-5 discussion questions in advance"
            ));
            remaining--;
        }

        outline.put("slides", slides);
        log.info("Presentation outline generated with {} slides", slides.size());
        return outline;
    }

    public Map<String, Object> generatePracticalDemonstration(String skill, String difficulty) {
        log.info("Generating practical demonstration for skill='{}', difficulty='{}'", skill, difficulty);

        Map<String, Object> demo = new LinkedHashMap<>();
        demo.put("skill", skill);
        demo.put("difficulty", difficulty);
        demo.put("estimatedDuration", getDemoDuration(skill, difficulty));
        demo.put("prerequisites", getDemoPrerequisites(skill));
        demo.put("safetyWarnings", getSafetyWarnings(skill));

        List<Map<String, Object>> steps = new ArrayList<>();
        String[] stepDescriptions = getDemoSteps(skill);
        for (int i = 0; i < stepDescriptions.length; i++) {
            Map<String, Object> step = new LinkedHashMap<>();
            step.put("stepNumber", i + 1);
            step.put("instruction", stepDescriptions[i]);
            step.put("duration", getStepDuration(i, stepDescriptions.length));
            step.put("tips", getStepTips(skill, i));
            step.put("commonMistakes", getCommonMistakes(skill, i));
            steps.add(step);
        }
        demo.put("steps", steps);

        demo.put("expectedOutcome", getExpectedOutcome(skill));
        demo.put("successCriteria", getSuccessCriteria(skill));
        demo.put("troubleshooting", getTroubleshootingTips(skill));

        log.info("Practical demonstration generated with {} steps", steps.size());
        return demo;
    }

    public Map<String, Object> generateLabExercise(String concept, String difficulty) {
        log.info("Generating lab exercise for concept='{}', difficulty='{}'", concept, difficulty);

        Map<String, Object> exercise = new LinkedHashMap<>();
        exercise.put("title", "Lab Exercise: " + concept);
        exercise.put("concept", concept);
        exercise.put("difficulty", difficulty);
        exercise.put("estimatedDuration", getLabDuration(concept, difficulty));
        exercise.put("labType", "Hands-on Practical");

        List<String> objectives = new ArrayList<>();
        objectives.add("Understand the principles of " + concept);
        objectives.add("Apply " + concept + " in a controlled environment");
        objectives.add("Record observations and analyze results");
        objectives.add("Troubleshoot common issues in " + concept);
        exercise.put("objectives", objectives);

        List<Map<String, Object>> materials = new ArrayList<>();
        String[] materialNames = getLabMaterials(concept);
        for (int i = 0; i < materialNames.length; i++) {
            materials.add(Map.of("item", materialNames[i], "quantity", getMaterialQuantity(concept, i), "notes", getMaterialNotes(concept, i)));
        }
        exercise.put("materials", materials);

        List<Map<String, Object>> steps = new ArrayList<>();
        String[] labSteps = getLabSteps(concept);
        for (int i = 0; i < labSteps.length; i++) {
            steps.add(Map.of("step", i + 1, "instruction", labSteps[i], "safetyNote", getLabSafetyNote(concept, i)));
        }
        exercise.put("steps", steps);

        List<String> expectedOutcomes = new ArrayList<>();
        expectedOutcomes.add(getExpectedOutcome(concept));
        expectedOutcomes.add("Observations recorded in lab journal");
        expectedOutcomes.add("Data analyzed and conclusions drawn");
        exercise.put("expectedOutcomes", expectedOutcomes);

        exercise.put("evaluationCriteria", getLabEvaluationCriteria(concept));
        exercise.put("followUpQuestions", getFollowUpQuestions(concept));

        log.info("Lab exercise generated for concept='{}'", concept);
        return exercise;
    }

    public Map<String, Object> generateLearningSummary(String moduleId, List<String> keyPoints) {
        log.info("Generating learning summary for moduleId='{}'", moduleId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("moduleId", moduleId);
        summary.put("generatedAt", LocalDateTime.now().toString());
        summary.put("type", "Learning Summary");

        List<String> points = keyPoints != null && !keyPoints.isEmpty() ? keyPoints : defaultSummaryPoints(moduleId);
        summary.put("keyPoints", points);

        List<String> conceptsMastered = new ArrayList<>();
        List<String> conceptsReviewNeeded = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            if (i % 3 == 2) {
                conceptsReviewNeeded.add(points.get(i));
            } else {
                conceptsMastered.add(points.get(i));
            }
        }
        summary.put("conceptsMastered", conceptsMastered);
        summary.put("conceptsToReview", conceptsReviewNeeded);

        summary.put("recommendedNextSteps", List.of(
            "Review any concepts marked for revision",
            "Attempt the practice quiz for " + moduleId,
            "Proceed to the next module after achieving >= 80% on assessment"
        ));

        log.info("Learning summary generated with {} key points", points.size());
        return summary;
    }

    public Map<String, Object> generateHomework(String topic, String difficulty) {
        log.info("Generating homework for topic='{}', difficulty='{}'", topic, difficulty);

        Map<String, Object> homework = new LinkedHashMap<>();
        homework.put("title", "Homework: " + topic);
        homework.put("topic", topic);
        homework.put("difficulty", difficulty);
        homework.put("estimatedDuration", getHomeworkDuration(difficulty));
        homework.put("submissionInstructions", "Submit via online portal before next session");

        List<Map<String, Object>> questions = new ArrayList<>();
        String[][] qa = getHomeworkQuestions(topic, difficulty);
        for (int i = 0; i < qa.length; i++) {
            questions.add(Map.of(
                "questionNumber", i + 1,
                "question", qa[i][0],
                "type", getQuestionType(i),
                "hint", getHomeworkHint(topic, difficulty, i),
                "maxPoints", getQuestionMaxPoints(difficulty, i)
            ));
        }
        homework.put("questions", questions);
        homework.put("totalQuestions", questions.size());
        homework.put("totalPoints", questions.size() * 10);

        homework.put("referenceMaterials", getHomeworkReferences(topic));
        homework.put("additionalResources", List.of(
            "Video tutorial: " + topic,
            "Lab manual chapter reference",
            "Online discussion forum for doubts"
        ));

        log.info("Homework generated with {} questions", questions.size());
        return homework;
    }

    private List<Map<String, Object>> generateLessonSections(String topic, String lessonType, String difficulty, int durationMinutes) {
        List<Map<String, Object>> sections = new ArrayList<>();
        int sectionCount = Math.max(3, durationMinutes / 15);
        int minutesPerSection = durationMinutes / sectionCount;

        for (int i = 0; i < sectionCount; i++) {
            Map<String, Object> section = new LinkedHashMap<>();
            section.put("sequence", i + 1);
            section.put("title", getSectionTitle(topic, i + 1, 0));
            section.put("durationMinutes", i == sectionCount - 1 ? durationMinutes - (minutesPerSection * (sectionCount - 1)) : minutesPerSection);
            section.put("content", getSectionContent(topic, i + 1, 0));
            section.put("teachingMethod", getTeachingMethod(i));
            sections.add(section);
        }
        return sections;
    }

    private List<Map<String, Object>> generateLessonMaterials(String topic, String lessonType, String difficulty) {
        List<Map<String, Object>> materials = new ArrayList<>();
        materials.add(Map.of("type", "Presentation", "description", "Slide deck for " + topic, "format", "PPTX"));
        materials.add(Map.of("type", "Handout", "description", "Reference notes on " + topic, "format", "PDF"));
        materials.add(Map.of("type", "Video", "description", "Demonstration video for " + topic, "format", "MP4"));
        if (difficulty.equalsIgnoreCase("Advanced") || difficulty.equalsIgnoreCase("Intermediate")) {
            materials.add(Map.of("type", "Research Paper", "description", "Recent publication on " + topic, "format", "PDF"));
        }
        return materials;
    }

    private List<Map<String, Object>> generateLessonActivities(String topic, String lessonType, String difficulty) {
        List<Map<String, Object>> activities = new ArrayList<>();
        activities.add(Map.of("type", "Discussion", "description", "Group discussion on " + topic, "duration", 10));
        activities.add(Map.of("type", "Quiz", "description", "Quick knowledge check on " + topic, "duration", 5));
        activities.add(Map.of("type", "Hands-on", "description", "Practical exercise related to " + topic, "duration", 15));
        if (difficulty.equalsIgnoreCase("Advanced")) {
            activities.add(Map.of("type", "Case Study", "description", "Analyze real-world scenario involving " + topic, "duration", 20));
        }
        return activities;
    }

    private String getSectionTitle(String topic, int sectionNum, int subIndex) {
        if (topic.contains("Spawn") || topic.contains("spawn")) {
            return switch (sectionNum) {
                case 1 -> "What is Spawn?";
                case 2 -> "Spawn Production Process";
                case 3 -> "Quality Assessment of Spawn";
                case 4 -> "Spawn Storage & Handling";
                case 5 -> "Commercial Spawn Suppliers";
                default -> topic + " - Part " + sectionNum;
            };
        }
        if (topic.contains("Substrate") || topic.contains("substrate")) {
            return switch (sectionNum) {
                case 1 -> "Introduction to Substrates";
                case 2 -> "Substrate Formulation";
                case 3 -> "Supplementation Strategies";
                case 4 -> "Pasteurization vs Sterilization";
                case 5 -> "Substrate Testing & pH Adjustment";
                default -> topic + " - Part " + sectionNum;
            };
        }
        if (topic.contains("Steril") || topic.contains("steril")) {
            return switch (sectionNum) {
                case 1 -> "Why Sterilization Matters";
                case 2 -> "Heat Sterilization Methods";
                case 3 -> "Chemical Sterilization";
                case 4 -> "Sterility Testing & Validation";
                case 5 -> "Aseptic Technique Fundamentals";
                default -> topic + " - Part " + sectionNum;
            };
        }
        return switch (sectionNum) {
            case 1 -> "Introduction to " + topic;
            case 2 -> "Core Concepts of " + topic;
            case 3 -> topic + " - Deep Dive";
            case 4 -> topic + " - Practical Application";
            case 5 -> topic + " - Advanced Topics";
            default -> topic + " Section " + sectionNum;
        };
    }

    private String getSectionContent(String topic, int sectionNum, int subIndex) {
        if (topic.contains("Spawn") && sectionNum == 1) {
            return "Spawn is mycelium grown on a carrier material (grain, sawdust, or plugs) used to inoculate bulk substrates. " +
                   "Understanding spawn types, quality indicators, and contamination signs is fundamental to successful cultivation.";
        }
        if (topic.contains("Substrate") && sectionNum == 1) {
            return "Substrate provides nutrients and structure for mycelial growth. Common substrates include straw, sawdust, " +
                   "compost, coffee grounds, and supplemented hardwoods. Each mushroom species has specific substrate preferences.";
        }
        if (topic.contains("Steril") && sectionNum == 1) {
            return "Sterilization eliminates all microorganisms including spores. It is essential for nutrient-rich substrates. " +
                   "Methods include autoclaving (121°C, 15 PSI, 90 min), tyndallization, and chemical sterilization with hydrogen peroxide.";
        }
        return "Detailed content for " + topic + " section " + sectionNum + ". This covers theoretical foundations, " +
               "practical techniques, and quality considerations relevant to the topic at the appropriate depth.";
    }

    private String getSpeakerNotes(String topic, int sectionNum, int subIndex) {
        return "Emphasize key terminology. Use visual aids. " +
               "Ask students about their prior experience with " + topic + ". " +
               "Prepare a demonstration if applicable.";
    }

    private String getTeachingMethod(int index) {
        return switch (index % 4) {
            case 0 -> "Lecture with slides";
            case 1 -> "Interactive discussion";
            case 2 -> "Demonstration";
            case 3 -> "Group activity";
            default -> "Lecture";
        };
    }

    private List<String> defaultObjectives(String topic) {
        return List.of(
            "Understand core concepts of " + topic,
            "Apply " + topic + " techniques in practice",
            "Evaluate results and troubleshoot issues"
        );
    }

    private String generateContentSummary(String topic, String difficulty) {
        return "This session covered " + topic + " at a " + difficulty + " level. " +
               "Students should now understand the fundamental principles, be able to apply key techniques, " +
               "and recognize common challenges and their solutions.";
    }

    private List<String> generateKeyTakeaways(String topic, String difficulty) {
        return List.of(
            topic + " is a critical skill in mushroom cultivation",
            "Proper technique ensures consistent results",
            "Regular monitoring and adjustment prevents failures",
            "Documentation of observations aids in troubleshooting"
        );
    }

    private List<String> defaultSummaryPoints(String moduleId) {
        return List.of(
            "Completed module " + moduleId + " theory components",
            "Practiced corresponding lab exercises",
            "Achieved proficiency in core techniques",
            "Understood quality control parameters"
        );
    }

    private int getDemoDuration(String skill, String difficulty) {
        if (difficulty.equalsIgnoreCase("Advanced")) return 60;
        if (difficulty.equalsIgnoreCase("Intermediate")) return 45;
        return 30;
    }

    private List<String> getDemoPrerequisites(String skill) {
        List<String> prereqs = new ArrayList<>();
        prereqs.add("Clean lab coat and gloves");
        prereqs.add("Understanding of sterile technique");
        prereqs.add("Basic knowledge of " + skill);
        if (skill.contains("Steril") || skill.contains("Inocul")) {
            prereqs.add("Completion of safety orientation");
        }
        return prereqs;
    }

    private List<String> getSafetyWarnings(String skill) {
        List<String> warnings = new ArrayList<>();
        warnings.add("Always work in a well-ventilated area");
        warnings.add("Use personal protective equipment (PPE)");
        if (skill.contains("Steril") || skill.contains("autoclave") || skill.contains("pressure")) {
            warnings.add("High temperature! Allow equipment to cool before opening");
            warnings.add("Never open pressure vessel while pressurized");
        }
        if (skill.contains("Chemical") || skill.contains("alcohol") || skill.contains("bleach")) {
            warnings.add("Flammable materials - keep away from open flame");
            warnings.add("Avoid skin contact with chemicals");
        }
        return warnings;
    }

    private String[] getDemoSteps(String skill) {
        if (skill.contains("Spawn") || skill.contains("spawn")) {
            return new String[]{
                "Gather materials: grain jars, inoculant, 70% isopropyl alcohol, sterile gloves, laminar flow hood",
                "Sanitize all surfaces and equipment with 70% alcohol",
                "Don sterile gloves and wipe down the work area",
                "Flame-sterilize the inoculation needle until red hot",
                "Cool the needle for 10-15 seconds",
                "Open the spawn jar lid slightly under the flow hood",
                "Insert the needle and inject 1-2 mL of liquid culture per jar",
                "Quickly close the lid and shake gently to distribute",
                "Label each jar with date, strain, and inoculant type",
                "Place jars in incubation at 22-25°C in darkness",
                "Monitor daily for mycelial growth and contamination signs"
            };
        }
        if (skill.contains("Substrate") || skill.contains("substrate")) {
            return new String[]{
                "Select appropriate substrate material (straw, sawdust, or compost)",
                "Chop straw into 2-4 inch pieces using sterile shears",
                "Hydrate substrate in clean water for 4-6 hours",
                "Drain excess water - target moisture content 60-65%",
                "Fill substrate into heat-resistant bags or jars",
                "Heat to 65-70°C for pasteurization (straw) or 121°C for sterilization (sawdust)",
                "Maintain pasteurization temperature for 90 minutes",
                "Cool substrate to 25-30°C before inoculation",
                "Test pH - adjust to 6.5-7.0 using lime or gypsum if needed",
                "Supplement with bran or soy hulls at 10-20% for sawdust"
            };
        }
        if (skill.contains("steril") || skill.contains("Steril") || skill.contains("autoclave") || skill.contains("Autoclave")) {
            return new String[]{
                "Prepare items for sterilization in autoclave-safe bags or wrapped in foil",
                "Add distilled water to autoclave chamber to indicated level",
                "Load items with space between for steam circulation",
                "Close and seal the autoclave door securely",
                "Set temperature to 121°C and pressure to 15 PSI",
                "Set timer: 30 min for tools, 90 min for substrate bags",
                "Start cycle and monitor temperature/pressure gauges",
                "Allow natural cooling until pressure reaches zero",
                "Open door slightly for final cooling (15-20 min)",
                "Remove items using heat-resistant gloves",
                "Verify sterility with indicator tape or biological indicators"
            };
        }
        if (skill.contains("Inocul") || skill.contains("inocul")) {
            return new String[]{
                "Prepare laminar flow hood or still-air box",
                "Sanitize interior with 70% isopropyl alcohol",
                "Place sterile substrate bags/jars and inoculant inside",
                "Wipe all exterior surfaces with alcohol",
                "Flame-sterilize inoculation loop or needle",
                "Cool for 15 seconds before contacting mycelium",
                "Open inoculant container under sterile airflow",
                "Transfer mycelium to substrate using sterile technique",
                "Seal containers immediately after inoculation",
                "Label with strain, date, and inoculation method",
                "Transfer to incubation area at appropriate temperature"
            };
        }
        if (skill.contains("Harvest") || skill.contains("harvest")) {
            return new String[]{
                "Check mushroom maturity - cap should be fully formed but not yet dropping spores",
                "Sanitize hands and harvesting knife with 70% alcohol",
                "Grasp mushroom at base near the substrate surface",
                "Gently twist and pull to remove the entire stem",
                "Trim the dirty base of the stem with a clean knife",
                "Grade mushrooms by size, shape, and quality",
                "Place first-grade mushrooms in clean collection trays",
                "Set aside blemished specimens for immediate processing",
                "Record harvest weight, count, and quality metrics",
                "Clean harvesting area between flushes",
                "Rehydrate substrate for subsequent flushes if needed"
            };
        }
        return new String[]{
            "Step 1: Prepare workspace and gather materials",
            "Step 2: Sanitize all equipment and surfaces",
            "Step 3: Execute primary technique for " + skill,
            "Step 4: Monitor and document results",
            "Step 5: Clean up and store materials properly"
        };
    }

    private int getStepDuration(int stepIndex, int totalSteps) {
        if (stepIndex < 3) return 5;
        if (stepIndex > totalSteps - 3) return 3;
        return 4;
    }

    private String getStepTips(String skill, int stepIndex) {
        return "Take your time; rushing leads to contamination. Double-check each action.";
    }

    private String getCommonMistakes(String skill, int stepIndex) {
        if (stepIndex == 0) return "Skipping surface sanitization";
        if (stepIndex == 1) return "Using insufficient alcohol concentration (< 70%)";
        if (stepIndex == 2) return "Not replacing gloves when contaminated";
        return "Rushing through the step without proper attention to detail";
    }

    private String getExpectedOutcome(String skill) {
        if (skill.contains("Spawn") || skill.contains("spawn")) {
            return "Healthy white mycelium visible throughout grain within 10-14 days";
        }
        if (skill.contains("Substrate") || skill.contains("substrate")) {
            return "Uniformly colonized substrate ready for fruiting conditions";
        }
        if (skill.contains("steril") || skill.contains("Steril")) {
            return "All microorganisms eliminated - confirmed by sterility indicators";
        }
        if (skill.contains("Inocul") || skill.contains("inocul")) {
            return "Visible mycelial growth from inoculation points within 5-7 days";
        }
        if (skill.contains("Harvest") || skill.contains("harvest")) {
            return "Clean harvest with minimal damage to mushrooms and substrate";
        }
        return "Successful completion of " + skill + " procedure";
    }

    private String getSuccessCriteria(String skill) {
        if (skill.contains("Spawn") || skill.contains("spawn")) {
            return "No contamination in >= 90% of jars, full colonization within 14 days";
        }
        return "Procedure completed without contamination, all steps followed correctly";
    }

    private List<String> getTroubleshootingTips(String skill) {
        return List.of(
            "If contamination appears, isolate and dispose immediately",
            "Document any deviations from standard procedure",
            "Check temperature and humidity logs regularly",
            "Consult the troubleshooting guide for specific issues"
        );
    }

    private int getLabDuration(String concept, String difficulty) {
        if (difficulty.equalsIgnoreCase("Advanced")) return 120;
        if (difficulty.equalsIgnoreCase("Intermediate")) return 90;
        return 60;
    }

    private String[] getLabMaterials(String concept) {
        if (concept.contains("Spawn") || concept.contains("spawn")) {
            return new String[]{"Rye grain", "Quart mason jars", "Lid with injection port", "Pressure cooker", "Liquid culture syringe", "70% isopropyl alcohol", "Laminar flow hood", "Permanent marker", "Parafilm", "Incubation chamber"};
        }
        if (concept.contains("Substrate") || concept.contains("substrate")) {
            return new String[]{"Wheat straw", "Hardwood sawdust", "Gypsum", "pH meter", "Heat-resistant bags", "Autoclave or drum", "Digital thermometer", "Water source", "Weighing scale", "Hydrated lime"};
        }
        if (concept.contains("steril") || concept.contains("Steril")) {
            return new String[]{"Autoclave", "Autoclave-safe bags", "Sterilization indicator tape", "Biological indicator strips", "Distilled water", "Heat-resistant gloves", "Timer", "Cooling rack"};
        }
        return new String[]{"Lab coat", "Gloves", "Safety goggles", "70% ethanol", "Sterile containers", "Labels", "Notebook", "Camera"};
    }

    private String getMaterialQuantity(String concept, int index) {
        return switch (index) {
            case 0 -> "5 kg";
            case 1 -> "10 units";
            case 2 -> "1 bottle";
            case 3 -> "1 unit";
            case 4 -> "2 L";
            default -> "As needed";
        };
    }

    private String getMaterialNotes(String concept, int index) {
        return switch (index) {
            case 0 -> "Organic, untreated preferred";
            case 1 -> "Must be autoclave-safe";
            case 2 -> "70% concentration for disinfection";
            case 3 -> "Check calibration before use";
            default -> "";
        };
    }

    private String[] getLabSteps(String concept) {
        if (concept.contains("Spawn") || concept.contains("spawn")) {
            return new String[]{
                "Rinse rye grain thoroughly and soak in water for 12-24 hours",
                "Drain grain and load into mason jars, filling to 2/3 capacity",
                "Add lids and tighten to finger-tight, then back off 1/4 turn",
                "Pressure cook jars at 15 PSI for 90 minutes",
                "Allow jars to cool to room temperature (24-48 hours)",
                "Under laminar flow, inject 1-2 mL liquid culture per jar",
                "Cover injection port with parafilm",
                "Place jars in incubation at 24°C in complete darkness",
                "Check daily; shake at 30% colonization to distribute mycelium",
                "Record colonization progress and contamination observations"
            };
        }
        if (concept.contains("Substrate") || concept.contains("substrate")) {
            return new String[]{
                "Chop straw into 2-4 inch pieces and rinse with clean water",
                "Soak straw in water for 4-6 hours for hydration",
                "Drain and place in heat-resistant bag or drum",
                "Heat water to 65-70°C for pasteurization",
                "Maintain temperature for 90 minutes",
                "Drain and allow to cool to 25-30°C",
                "Test moisture content by squeezing - should release a few drops",
                "Adjust pH to 6.5-7.0 using gypsum or lime",
                "Supplement sawdust with 10-20% bran if using wood-based substrate",
                "Pack into grow bags or trays at appropriate density"
            };
        }
        if (concept.contains("steril") || concept.contains("Steril")) {
            return new String[]{
                "Inspect autoclave for proper water level and clean chamber",
                "Wrap items in autoclave paper or place in sterilization pouches",
                "Apply sterilization indicator tape to each package",
                "Load items with adequate spacing for steam penetration",
                "Close door and engage locking mechanism",
                "Select cycle: 121°C liquid cycle for substrates",
                "Start cycle and verify target temperature is reached",
                "Monitor pressure gauge stays at 15 PSI throughout",
                "Allow slow cool-down until pressure reaches zero",
                "Open door carefully, remove items, check indicator tape color change"
            };
        }
        return new String[]{
            "Set up workstation with all required materials",
            "Follow standard operating procedure for " + concept,
            "Document all observations and measurements",
            "Clean and store equipment after completion"
        };
    }

    private String getLabSafetyNote(String concept, int stepIndex) {
        if (concept.contains("steril") || concept.contains("Steril")) {
            if (stepIndex == 8) return "WARNING: Never open autoclave while pressurized";
            if (stepIndex == 9) return "Use heat-resistant gloves; contents will be hot";
        }
        if (concept.contains("alcohol") || concept.contains("chemical")) {
            if (stepIndex == 2 || stepIndex == 4) return "Perform in well-ventilated area, avoid inhalation";
        }
        return "Standard lab safety practices apply";
    }

    private List<String> getLabEvaluationCriteria(String concept) {
        return List.of(
            "All steps completed in correct order",
            "No contamination observed",
            "Observations recorded in detail",
            "Equipment cleaned and stored properly",
            "Results within expected parameters"
        );
    }

    private List<String> getFollowUpQuestions(String concept) {
        return List.of(
            "What was the most challenging part of this exercise?",
            "How would you scale this process for commercial production?",
            "What indicators would suggest contamination in " + concept + "?",
            "How does " + concept + " differ between mushroom species?"
        );
    }

    private int getHomeworkDuration(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "advanced" -> 120;
            case "intermediate" -> 90;
            default -> 60;
        };
    }

    private String[][] getHomeworkQuestions(String topic, String difficulty) {
        if (topic.contains("Spawn") || topic.contains("spawn")) {
            return new String[][]{
                {"Explain the difference between grain spawn and sawdust spawn. When would you use each type?", "short-answer"},
                {"List three signs of healthy mycelial growth in grain spawn jars.", "short-answer"},
                {"Calculate: If 10 kg of substrate requires 5% spawn, how much spawn is needed? Show your work.", "calculation"},
                {"Describe the step-by-step process of making grain spawn, including sterilization parameters.", "short-answer"},
                {"What are the most common contaminants in spawn production and how can they be prevented?", "short-answer"},
                {"Compare the shelf life of refrigerated vs room-temperature spawn. What factors affect longevity?", "comparison"}
            };
        }
        if (topic.contains("Substrate") || topic.contains("substrate")) {
            return new String[][]{
                {"What is the ideal C:N ratio for oyster mushroom substrate? How do you achieve it?", "short-answer"},
                {"List three different substrate formulations for shiitake cultivation.", "short-answer"},
                {"Explain the difference between pasteurization and sterilization, including temperature and duration.", "short-answer"},
                {"How do you test moisture content of substrate? What is the target percentage?", "short-answer"},
                {"What role does gypsum play in substrate preparation?", "short-answer"},
                {"Design a substrate formulation for 50 kg of sawdust-based substrate, including supplements.", "design"}
            };
        }
        if (topic.contains("steril") || topic.contains("Steril") || topic.contains("Sterilization")) {
            return new String[][]{
                {"Explain the principles of autoclave sterilization. Why 121°C and 15 PSI?", "short-answer"},
                {"What is biological indicator testing and why is it important?", "short-answer"},
                {"Compare three sterilization methods: autoclaving, tyndallization, and chemical sterilization.", "comparison"},
                {"How long should you sterilize grain spawn jars at 15 PSI? Why?", "short-answer"},
                {"What safety precautions must be observed when operating an autoclave?", "short-answer"}
            };
        }
        if (topic.contains("Inocul") || topic.contains("inocul")) {
            return new String[][]{
                {"Describe aseptic technique for inoculating substrate bags under laminar flow.", "short-answer"},
                {"What is the recommended inoculation rate for bulk substrate? How does it vary by species?", "short-answer"},
                {"Explain the purpose of shaking spawn jars at 30% colonization.", "short-answer"},
                {"How do you identify contamination versus healthy mycelium during the first week post-inoculation?", "short-answer"},
                {"What environmental conditions are optimal during the incubation phase?", "short-answer"}
            };
        }
        if (topic.contains("Harvest") || topic.contains("harvest") || topic.contains("Fruit") || topic.contains("fruit")) {
            return new String[][]{
                {"At what stage should oyster mushrooms be harvested? What are the visual indicators?", "short-answer"},
                {"Explain the proper harvesting technique to maximize yield for subsequent flushes.", "short-answer"},
                {"How do you grade harvested mushrooms? What are the quality parameters?", "short-answer"},
                {"Compare fresh, dried, and vacuum-packed storage methods for shelf life and quality.", "comparison"},
                {"What is the ideal storage temperature and humidity for fresh mushrooms?", "short-answer"},
                {"Calculate expected yield: If 10 kg substrate produces 20% BE in first flush, what is the harvest weight?", "calculation"}
            };
        }
        return new String[][]{
            {"Explain the concept of " + topic + " in mushroom cultivation.", "short-answer"},
            {"Describe three practical applications of " + topic + ".", "short-answer"},
            {"What are common mistakes when performing " + topic + " and how to avoid them?", "short-answer"},
            {"How would you optimize " + topic + " for commercial-scale production?", "design"}
        };
    }

    private String getQuestionType(int index) {
        return switch (index % 4) {
            case 0 -> "short-answer";
            case 1 -> "descriptive";
            case 2 -> "calculation";
            case 3 -> "comparison";
            default -> "short-answer";
        };
    }

    private String getHomeworkHint(String topic, String difficulty, int questionIndex) {
        return "Refer to lab manual chapter on " + topic + " and class notes.";
    }

    private int getQuestionMaxPoints(String difficulty, int questionIndex) {
        return switch (difficulty.toLowerCase()) {
            case "advanced" -> 15;
            case "intermediate" -> 12;
            default -> 10;
        };
    }

    private List<String> getHomeworkReferences(String topic) {
        return List.of(
            "Course textbook: Chapter on " + topic,
            "Lab manual: Practical guide for " + topic,
            "Online resource: Video demonstration of " + topic
        );
    }
}
