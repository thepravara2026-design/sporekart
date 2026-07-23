package com.sporekart.trainer.copilot.engine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AssessmentEngine {

    private static final Logger log = LoggerFactory.getLogger(AssessmentEngine.class);

    private static final Random RANDOM = new Random();

    private static final List<Map<String, Object>> MCQ_BANK = buildMCQBank();

    public AssessmentEngine() {
        log.info("AssessmentEngine initialized with {} seed MCQ questions", MCQ_BANK.size());
    }

    public Map<String, Object> generateAssessment(String moduleId, String title, String type,
                                                   String difficulty, int numQuestions, int durationMinutes) {
        log.info("Generating assessment: moduleId='{}', title='{}', type='{}', difficulty='{}', {} questions, {} min",
                moduleId, title, type, difficulty, numQuestions, durationMinutes);

        Map<String, Object> assessment = new LinkedHashMap<>();
        assessment.put("assessmentId", "ASSESS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        assessment.put("moduleId", moduleId);
        assessment.put("title", title);
        assessment.put("type", type);
        assessment.put("difficulty", difficulty);
        assessment.put("totalQuestions", numQuestions);
        assessment.put("durationMinutes", durationMinutes);
        assessment.put("maxScore", numQuestions * 10);
        assessment.put("passingScore", Math.round(numQuestions * 10 * 0.6));
        assessment.put("generatedAt", LocalDateTime.now().toString());

        List<Map<String, Object>> questions = switch (type.toLowerCase()) {
            case "mcq", "multiple-choice" -> generateMCQQuestions(difficulty, numQuestions);
            case "short-answer", "shortanswer" -> generateShortAnswerQuestions(difficulty, numQuestions);
            case "scenario", "scenario-based" -> generateScenarioQuestions(difficulty, numQuestions);
            case "mixed" -> generateMixedQuestions(difficulty, numQuestions);
            default -> generateMCQQuestions(difficulty, numQuestions);
        };

        assessment.put("questions", questions);
        assessment.put("answerKey", generateAnswerKeyFromQuestions(questions));

        log.info("Assessment generated with {} questions", questions.size());
        return assessment;
    }

    public Map<String, Object> generateMCQ(String topic, String difficulty) {
        log.info("Generating MCQ set for topic='{}', difficulty='{}'", topic, difficulty);

        List<Map<String, Object>> questions = generateMCQQuestionsForTopic(topic, difficulty, 10);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("topic", topic);
        result.put("difficulty", difficulty);
        result.put("questions", questions);
        result.put("totalQuestions", questions.size());
        result.put("answerKey", generateAnswerKeyFromQuestions(questions));

        log.info("MCQ set generated with {} questions", questions.size());
        return result;
    }

    public Map<String, Object> generateShortAnswer(String topic, String difficulty) {
        log.info("Generating short answer questions for topic='{}', difficulty='{}'", topic, difficulty);

        List<Map<String, Object>> questions = generateShortAnswerQuestionsForTopic(topic, difficulty, 5);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("topic", topic);
        result.put("difficulty", difficulty);
        result.put("questions", questions);

        log.info("Short answer question set generated with {} questions", questions.size());
        return result;
    }

    public Map<String, Object> generateScenarioQuestion(String scenario, String difficulty) {
        log.info("Generating scenario question for scenario='{}', difficulty='{}'", scenario, difficulty);

        Map<String, Object> question = new LinkedHashMap<>();
        question.put("questionId", "SCEN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        question.put("type", "scenario");
        question.put("difficulty", difficulty);
        question.put("scenario", scenario);

        List<String> subQuestions = switch (difficulty.toLowerCase()) {
            case "advanced" -> List.of(
                "Analyze the scenario: What are the root causes of the described issue?",
                "Propose a step-by-step solution with justification for each step.",
                "What preventive measures would you implement to avoid this issue in the future?",
                "How would you modify the process to improve outcomes?"
            );
            case "intermediate" -> List.of(
                "Identify the main problem described in the scenario.",
                "What immediate actions should be taken?",
                "What long-term changes would you recommend?"
            );
            default -> List.of(
                "What is happening in this scenario?",
                "What is the most likely cause?",
                "What should be done first to address the issue?"
            );
        };
        question.put("questions", subQuestions);

        Map<String, Object> modelAnswer = generateScenarioModelAnswer(scenario, difficulty);
        question.put("modelAnswer", modelAnswer);
        question.put("evaluationCriteria", generateScenarioEvaluationCriteria(difficulty));

        log.info("Scenario question generated");
        return question;
    }

    public Map<String, Object> generatePracticalAssessment(String skill, String difficulty) {
        log.info("Generating practical assessment for skill='{}', difficulty='{}'", skill, difficulty);

        Map<String, Object> assessment = new LinkedHashMap<>();
        assessment.put("assessmentId", "PRAC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        assessment.put("skill", skill);
        assessment.put("difficulty", difficulty);
        assessment.put("estimatedDuration", switch (difficulty.toLowerCase()) {
            case "advanced" -> 90;
            case "intermediate" -> 60;
            default -> 45;
        });

        List<Map<String, Object>> tasks = generatePracticalTasks(skill, difficulty);
        assessment.put("tasks", tasks);

        List<Map<String, Object>> rubric = generatePracticalRubric(skill, tasks.size());
        assessment.put("evaluationRubric", rubric);

        assessment.put("materials", getPracticalMaterials(skill));
        assessment.put("safetyRequirements", getPracticalSafety(skill));
        assessment.put("passingScore", 80);

        log.info("Practical assessment generated with {} tasks", tasks.size());
        return assessment;
    }

    public Map<String, Object> generateCertificationTest(String courseName) {
        log.info("Generating certification test for course='{}'", courseName);

        Map<String, Object> test = new LinkedHashMap<>();
        test.put("testId", "CERT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        test.put("courseName", courseName);
        test.put("title", courseName + " - Certification Examination");
        test.put("totalQuestions", 50);
        test.put("durationMinutes", 120);
        test.put("maxScore", 100);
        test.put("passingScore", 60);
        test.put("sections", generateCertificationSections(courseName));

        List<Map<String, Object>> allQuestions = new ArrayList<>();
        allQuestions.addAll(generateMCQQuestions("Beginner", 15));
        allQuestions.addAll(generateMCQQuestions("Intermediate", 20));
        allQuestions.addAll(generateMCQQuestions("Advanced", 10));
        allQuestions.addAll(generateShortAnswerQuestions("Intermediate", 5));
        test.put("questions", allQuestions);
        test.put("answerKey", generateAnswerKeyFromQuestions(allQuestions));

        log.info("Certification test generated with {} questions", allQuestions.size());
        return test;
    }

    public Map<String, Object> evaluateAssessment(List<String> studentAnswers, Map<String, Object> assessment) {
        log.info("Evaluating assessment with {} student answers", studentAnswers.size());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>) assessment.get("questions");
        @SuppressWarnings("unchecked")
        Map<String, Object> answerKey = (Map<String, Object>) assessment.get("answerKey");

        int totalQuestions = questions.size();
        int correct = 0;
        int partial = 0;
        int wrong = 0;
        double totalScore = 0.0;
        double maxScore = totalQuestions * 10.0;

        List<Map<String, Object>> feedbackList = new ArrayList<>();

        for (int i = 0; i < totalQuestions && i < studentAnswers.size(); i++) {
            String studentAnswer = studentAnswers.get(i) != null ? studentAnswers.get(i).trim().toLowerCase() : "";
            String correctAnswer = extractCorrectAnswer(questions.get(i), answerKey);

            double earned = 0;
            String status;

            if (studentAnswer.equalsIgnoreCase(correctAnswer)) {
                earned = 10.0;
                correct++;
                status = "correct";
            } else if (isPartiallyCorrect(studentAnswer, correctAnswer)) {
                earned = 5.0;
                partial++;
                status = "partial";
            } else {
                earned = 0.0;
                wrong++;
                status = "incorrect";
            }
            totalScore += earned;

            Map<String, Object> fb = new LinkedHashMap<>();
            fb.put("questionNumber", i + 1);
            fb.put("status", status);
            fb.put("earnedScore", earned);
            fb.put("maxScore", 10.0);
            fb.put("correctAnswer", correctAnswer);
            fb.put("feedback", getAnswerFeedback(status, questions.get(i)));
            feedbackList.add(fb);
        }

        double percentage = maxScore > 0 ? (totalScore / maxScore) * 100.0 : 0.0;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("assessmentId", assessment.get("assessmentId"));
        result.put("totalQuestions", totalQuestions);
        result.put("answered", studentAnswers.size());
        result.put("correct", correct);
        result.put("partialCorrect", partial);
        result.put("incorrect", wrong);
        result.put("totalScore", Math.round(totalScore * 10.0) / 10.0);
        result.put("maxScore", maxScore);
        result.put("percentage", Math.round(percentage * 10.0) / 10.0);
        result.put("passed", percentage >= 60.0);
        result.put("questionFeedback", feedbackList);

        String grade;
        if (percentage >= 90) grade = "A - Excellent";
        else if (percentage >= 80) grade = "B - Very Good";
        else if (percentage >= 70) grade = "C - Good";
        else if (percentage >= 60) grade = "D - Satisfactory";
        else grade = "F - Needs Improvement";
        result.put("grade", grade);

        log.info("Assessment evaluated: score={}%, passed={}, grade='{}'", Math.round(percentage), percentage >= 60.0, grade);
        return result;
    }

    public Map<String, Object> generateAnswerKey(Map<String, Object> assessment) {
        log.info("Generating answer key for assessmentId='{}'", assessment.get("assessmentId"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> questions = (List<Map<String, Object>>) assessment.get("questions");
        Map<String, Object> answerKey = generateAnswerKeyFromQuestions(questions);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("assessmentId", assessment.get("assessmentId"));
        result.put("title", assessment.get("title"));
        result.put("answers", answerKey);
        result.put("totalQuestions", questions.size());

        log.info("Answer key generated for {} questions", questions.size());
        return result;
    }

    // ---- Private helpers ----

    private List<Map<String, Object>> generateMCQQuestions(String difficulty, int count) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> q : MCQ_BANK) {
            String qDiff = (String) q.get("difficulty");
            if (qDiff != null && qDiff.equalsIgnoreCase(difficulty)) {
                filtered.add(q);
            }
        }
        if (filtered.size() < count) {
            filtered.addAll(MCQ_BANK);
        }
        Collections.shuffle(filtered, RANDOM);
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, filtered.size()); i++) {
            Map<String, Object> q = new LinkedHashMap<>(filtered.get(i));
            q.put("questionNumber", i + 1);
            result.add(q);
        }
        return result;
    }

    private List<Map<String, Object>> generateShortAnswerQuestions(String difficulty, int count) {
        String[][] saBank = SHORT_ANSWER_BANK;
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < saBank.length; i++) {
            String d = saBank[i].length > 2 ? saBank[i][2] : "Intermediate";
            if (d.equalsIgnoreCase(difficulty)) {
                indices.add(i);
            }
        }
        if (indices.size() < count) {
            for (int i = 0; i < saBank.length; i++) indices.add(i);
        }
        Collections.shuffle(indices, RANDOM);
        for (int i = 0; i < Math.min(count, indices.size()); i++) {
            int idx = indices.get(i);
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("questionNumber", i + 1);
            q.put("type", "short-answer");
            q.put("question", saBank[idx][0]);
            q.put("modelAnswer", saBank[idx][1]);
            q.put("difficulty", saBank[idx].length > 2 ? saBank[idx][2] : "Intermediate");
            q.put("maxScore", 10);
            result.add(q);
        }
        return result;
    }

    private List<Map<String, Object>> generateScenarioQuestions(String difficulty, int count) {
        String[][] scenarioBank = SCENARIO_BANK;
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < scenarioBank.length; i++) indices.add(i);
        Collections.shuffle(indices, RANDOM);
        for (int i = 0; i < Math.min(count, indices.size()); i++) {
            int idx = indices.get(i);
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("questionNumber", i + 1);
            q.put("type", "scenario");
            q.put("scenario", scenarioBank[idx][0]);
            q.put("questions", List.of(scenarioBank[idx][1].split("\\|")));
            q.put("modelAnswer", scenarioBank[idx][2]);
            q.put("difficulty", scenarioBank[idx].length > 3 ? scenarioBank[idx][3] : difficulty);
            q.put("maxScore", 20);
            result.add(q);
        }
        return result;
    }

    private List<Map<String, Object>> generateMixedQuestions(String difficulty, int count) {
        List<Map<String, Object>> mixed = new ArrayList<>();
        int mcqCount = count / 2;
        int saCount = count - mcqCount;
        mixed.addAll(generateMCQQuestions(difficulty, mcqCount));
        mixed.addAll(generateShortAnswerQuestions(difficulty, saCount));
        for (int i = 0; i < mixed.size(); i++) {
            mixed.get(i).put("questionNumber", i + 1);
        }
        return mixed;
    }

    private List<Map<String, Object>> generateMCQQuestionsForTopic(String topic, String difficulty, int count) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        String lowerTopic = topic.toLowerCase();
        for (Map<String, Object> q : MCQ_BANK) {
            String qTopic = ((String) q.getOrDefault("topic", "")).toLowerCase();
            if (qTopic.contains(lowerTopic) || qTopic.isEmpty()) {
                String qDiff = (String) q.get("difficulty");
                if (qDiff != null && qDiff.equalsIgnoreCase(difficulty)) {
                    filtered.add(q);
                }
            }
        }
        if (filtered.isEmpty()) {
            filtered.addAll(MCQ_BANK);
        }
        Collections.shuffle(filtered, RANDOM);
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, filtered.size()); i++) {
            Map<String, Object> q = new LinkedHashMap<>(filtered.get(i));
            q.put("questionNumber", i + 1);
            result.add(q);
        }
        return result;
    }

    private List<Map<String, Object>> generateShortAnswerQuestionsForTopic(String topic, String difficulty, int count) {
        List<Map<String, Object>> result = new ArrayList<>();
        String[][] bank = SHORT_ANSWER_BANK;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < bank.length; i++) {
            String d = bank[i].length > 2 ? bank[i][2] : "Intermediate";
            if (d.equalsIgnoreCase(difficulty)) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) {
            for (int i = 0; i < bank.length; i++) indices.add(i);
        }
        Collections.shuffle(indices, RANDOM);
        for (int i = 0; i < Math.min(count, indices.size()); i++) {
            int idx = indices.get(i);
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("questionNumber", i + 1);
            q.put("type", "short-answer");
            q.put("question", bank[idx][0]);
            q.put("modelAnswer", bank[idx][1]);
            q.put("difficulty", bank[idx].length > 2 ? bank[idx][2] : "Intermediate");
            result.add(q);
        }
        return result;
    }

    private Map<String, Object> generateScenarioModelAnswer(String scenario, String difficulty) {
        Map<String, Object> answer = new LinkedHashMap<>();
        answer.put("problemSummary", "Based on the scenario, the primary issue involves " + scenario.substring(0, Math.min(50, scenario.length())) + "...");
        answer.put("immediateActions", List.of(
            "Isolate affected materials to prevent spread",
            "Document symptoms and environmental conditions",
            "Consult standard operating procedures"
        ));
        answer.put("rootCause", "Likely causes include contamination, improper environmental control, or suboptimal technique");
        answer.put("preventiveMeasures", List.of(
            "Review and reinforce sterile technique protocols",
            "Monitor environmental parameters more frequently",
            "Implement stricter quality checks at each stage"
        ));
        return answer;
    }

    private List<Map<String, Object>> generateScenarioEvaluationCriteria(String difficulty) {
        return List.of(
            Map.of("criterion", "Problem Identification", "weight", 25, "description", "Correctly identifies the core issue"),
            Map.of("criterion", "Analysis Depth", "weight", 25, "description", "Demonstrates understanding of underlying causes"),
            Map.of("criterion", "Solution Quality", "weight", 30, "description", "Proposes practical, effective solutions"),
            Map.of("criterion", "Prevention Strategy", "weight", 20, "description", "Suggests appropriate preventive measures")
        );
    }

    private List<Map<String, Object>> generatePracticalTasks(String skill, String difficulty) {
        List<Map<String, Object>> tasks = new ArrayList<>();
        int taskCount = switch (difficulty.toLowerCase()) {
            case "advanced" -> 5;
            case "intermediate" -> 4;
            default -> 3;
        };

        for (int i = 0; i < taskCount; i++) {
            Map<String, Object> task = new LinkedHashMap<>();
            task.put("taskNumber", i + 1);
            task.put("description", "Task " + (i + 1) + ": " + getPracticalTaskDescription(skill, i));
            task.put("maxScore", 20);
            task.put("estimatedTime", 10 + i * 5);
            task.put("keySkillsAssessed", List.of(skill + " technique", "attention to detail", "safety compliance"));
            tasks.add(task);
        }
        return tasks;
    }

    private List<Map<String, Object>> generatePracticalRubric(String skill, int taskCount) {
        List<Map<String, Object>> rubric = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            Map<String, Object> criteria = new LinkedHashMap<>();
            criteria.put("task", i + 1);
            criteria.put("excellent", "Performs task flawlessly with no errors (18-20 pts)");
            criteria.put("good", "Minor errors but overall correct (14-17 pts)");
            criteria.put("satisfactory", "Major errors but demonstrates understanding (10-13 pts)");
            criteria.put("needsImprovement", "Fails to complete task correctly (< 10 pts)");
            rubric.add(criteria);
        }
        return rubric;
    }

    private String getPracticalTaskDescription(String skill, int index) {
        return switch (index) {
            case 0 -> "Prepare the workspace and materials following sterile protocol";
            case 1 -> "Execute the primary " + skill + " procedure within time limit";
            case 2 -> "Document observations and identify any issues encountered";
            case 3 -> "Troubleshoot a simulated problem related to " + skill;
            case 4 -> "Clean and store all equipment according to SOP";
            default -> "Demonstrate " + skill + " proficiency";
        };
    }

    private List<String> getPracticalMaterials(String skill) {
        return List.of("PPE kit (gloves, mask, lab coat)", "70% isopropyl alcohol", "Sterile tools", "Materials for " + skill, "Timer", "Documentation sheet");
    }

    private List<String> getPracticalSafety(String skill) {
        return List.of("Wear complete PPE at all times", "Work within designated sterile area", "Report any accidents immediately", "Handle sharps and chemicals with care");
    }

    private List<Map<String, Object>> generateCertificationSections(String courseName) {
        List<Map<String, Object>> sections = new ArrayList<>();
        sections.add(Map.of("section", "Section A", "title", "Multiple Choice (Beginner)", "questions", 15, "marksPerQuestion", 1, "totalMarks", 15));
        sections.add(Map.of("section", "Section B", "title", "Multiple Choice (Intermediate)", "questions", 20, "marksPerQuestion", 2, "totalMarks", 40));
        sections.add(Map.of("section", "Section C", "title", "Multiple Choice (Advanced)", "questions", 10, "marksPerQuestion", 3, "totalMarks", 30));
        sections.add(Map.of("section", "Section D", "title", "Short Answer", "questions", 5, "marksPerQuestion", 3, "totalMarks", 15));
        return sections;
    }

    private Map<String, Object> generateAnswerKeyFromQuestions(List<Map<String, Object>> questions) {
        Map<String, Object> key = new LinkedHashMap<>();
        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> q = questions.get(i);
            String qNum = String.valueOf(i + 1);
            String answer = (String) q.get("correctAnswer");
            if (answer == null) {
                answer = (String) q.getOrDefault("modelAnswer", "N/A");
            }
            key.put(qNum, answer);
        }
        return key;
    }

    private String extractCorrectAnswer(Map<String, Object> question, Map<String, Object> answerKey) {
        Object qNum = question.get("questionNumber");
        if (qNum != null && answerKey.containsKey(qNum.toString())) {
            return (String) answerKey.get(qNum.toString());
        }
        String answer = (String) question.get("correctAnswer");
        if (answer == null) {
            answer = (String) question.getOrDefault("modelAnswer", "");
        }
        return answer;
    }

    private boolean isPartiallyCorrect(String studentAnswer, String correctAnswer) {
        if (correctAnswer == null || studentAnswer == null) return false;
        String[] studentWords = studentAnswer.split("\\s+");
        String[] correctWords = correctAnswer.split("\\s+");
        int matchCount = 0;
        for (String sw : studentWords) {
            for (String cw : correctWords) {
                if (sw.equalsIgnoreCase(cw)) {
                    matchCount++;
                    break;
                }
            }
        }
        double ratio = correctWords.length > 0 ? (double) matchCount / correctWords.length : 0;
        return ratio >= 0.3 && ratio < 0.9;
    }

    private String getAnswerFeedback(String status, Map<String, Object> question) {
        return switch (status) {
            case "correct" -> "Correct answer. Well done!";
            case "partial" -> "Partially correct. Review the key concepts and try again.";
            case "incorrect" -> "Incorrect. The correct answer is provided above. Review the material and retry.";
            default -> "No feedback available.";
        };
    }

    // ---- Seed MCQ Bank (50+ questions) ----

    private static List<Map<String, Object>> buildMCQBank() {
        List<Map<String, Object>> bank = new ArrayList<>();

        // Sterilization (Beginner)
        bank.add(mcq("What temperature is required for autoclave sterilization?", "121°C", List.of("100°C", "121°C", "150°C", "180°C"), "Beginner", "Sterilization"));
        bank.add(mcq("What pressure is typically used in autoclave sterilization?", "15 PSI", List.of("10 PSI", "15 PSI", "20 PSI", "25 PSI"), "Beginner", "Sterilization"));
        bank.add(mcq("How long should grain spawn jars be sterilized at 15 PSI?", "90 minutes", List.of("30 minutes", "60 minutes", "90 minutes", "120 minutes"), "Beginner", "Sterilization"));
        bank.add(mcq("Which of the following is NOT a sterilization method?", "Pasteurization", List.of("Autoclaving", "Tyndallization", "Pasteurization", "Chemical sterilization"), "Beginner", "Sterilization"));
        bank.add(mcq("What does biological indicator testing confirm?", "Sterility", List.of("pH level", "Sterility", "Moisture content", "Nutrient level"), "Beginner", "Sterilization"));
        bank.add(mcq("Why is 70% isopropyl alcohol preferred over 90% for disinfection?", "70% penetrates better", List.of("70% is cheaper", "70% evaporates slower", "70% penetrates better", "90% is toxic to mycelium"), "Beginner", "Sterilization"));
        bank.add(mcq("What is tyndallization?", "Intermittent sterilization", List.of("Chemical sterilization", "Intermittent sterilization", "Cold sterilization", "UV sterilization"), "Intermediate", "Sterilization"));

        // Pasteurization (Beginner/Intermediate)
        bank.add(mcq("What temperature range is used for pasteurization of straw?", "65-70°C", List.of("50-55°C", "65-70°C", "80-85°C", "95-100°C"), "Beginner", "Pasteurization"));
        bank.add(mcq("How long should straw be pasteurized?", "90 minutes", List.of("30 minutes", "60 minutes", "90 minutes", "180 minutes"), "Beginner", "Pasteurization"));
        bank.add(mcq("What is the main purpose of pasteurization?", "Kill harmful organisms while preserving beneficial microbes", List.of("Kill all microorganisms", "Kill harmful organisms while preserving beneficial microbes", "Add nutrients to substrate", "Dry the substrate"), "Intermediate", "Pasteurization"));
        bank.add(mcq("Which mushroom species typically requires pasteurized substrate rather than sterilized?", "Oyster mushroom", List.of("Shiitake", "Oyster mushroom", "Reishi", "Lion's Mane"), "Intermediate", "Pasteurization"));

        // Substrate Preparation (Beginner/Intermediate)
        bank.add(mcq("What is the ideal moisture content for mushroom substrate?", "60-65%", List.of("40-45%", "50-55%", "60-65%", "75-80%"), "Beginner", "Substrate"));
        bank.add(mcq("What is the purpose of adding gypsum to substrate?", "Buffers pH and provides calcium", List.of("Increases nitrogen", "Buffers pH and provides calcium", "Prevents contamination", "Speeds up colonization"), "Intermediate", "Substrate"));
        bank.add(mcq("Which substrate is most commonly used for shiitake cultivation?", "Hardwood sawdust", List.of("Wheat straw", "Hardwood sawdust", "Coffee grounds", "Compost"), "Beginner", "Substrate"));
        bank.add(mcq("What does C:N ratio refer to in substrate formulation?", "Carbon to Nitrogen ratio", List.of("Calcium to Nitrogen ratio", "Carbon to Nitrogen ratio", "Copper to Nickel ratio", "Compost to Nutrient ratio"), "Intermediate", "Substrate"));
        bank.add(mcq("What is the recommended C:N ratio for mushroom substrate?", "30:1", List.of("10:1", "30:1", "60:1", "100:1"), "Intermediate", "Substrate"));
        bank.add(mcq("What is a common nitrogen supplement for sawdust substrates?", "Wheat bran", List.of("Sand", "Wheat bran", "Charcoal", "Clay"), "Beginner", "Substrate"));
        bank.add(mcq("Which method tests substrate moisture content?", "Squeeze test", List.of("pH test", "Squeeze test", "Density test", "Float test"), "Beginner", "Substrate"));

        // Spawn Running (Intermediate)
        bank.add(mcq("What is spawn?", "Mycelium grown on a carrier material", List.of("Mushroom spores", "Mycelium grown on a carrier material", "Sterilized grain", "Mushroom fruit body"), "Beginner", "Spawn"));
        bank.add(mcq("What is the recommended spawning rate for bulk substrate?", "5-10%", List.of("1-2%", "5-10%", "20-30%", "50%"), "Intermediate", "Spawn"));
        bank.add(mcq("Why are spawn jars shaken at 30% colonization?", "To distribute mycelium evenly", List.of("To increase temperature", "To distribute mycelium evenly", "To introduce fresh air", "To check for contamination"), "Intermediate", "Spawn"));
        bank.add(mcq("What temperature is ideal for spawn incubation of oyster mushrooms?", "22-25°C", List.of("15-18°C", "22-25°C", "28-32°C", "35-38°C"), "Beginner", "Spawn"));
        bank.add(mcq("How long does it typically take for grain spawn to fully colonize?", "10-14 days", List.of("3-5 days", "10-14 days", "21-28 days", "30-45 days"), "Intermediate", "Spawn"));

        // Pinning & Fruiting (Intermediate)
        bank.add(mcq("What environmental change typically triggers pinning?", "Drop in temperature and increase in fresh air", List.of("Increase in light", "Drop in temperature and increase in fresh air", "Rise in CO2 levels", "Decrease in humidity"), "Intermediate", "Pinning"));
        bank.add(mcq("What is the casing layer?", "A non-nutritive layer applied to colonized substrate", List.of("A non-nutritive layer applied to colonized substrate", "The outer skin of a mushroom", "A type of spawn", "A sterilization wrapper"), "Advanced", "Pinning"));
        bank.add(mcq("What is the ideal CO2 level for fruiting chamber?", "500-1000 ppm", List.of("Below 500 ppm", "500-1000 ppm", "2000-4000 ppm", "Above 5000 ppm"), "Intermediate", "Fruiting"));
        bank.add(mcq("What humidity level is required during mushroom fruiting?", "85-95%", List.of("60-70%", "75-85%", "85-95%", "100%"), "Beginner", "Fruiting"));
        bank.add(mcq("What does FAE stand for in mushroom cultivation?", "Fresh Air Exchange", List.of("Fungal Area Environment", "Fresh Air Exchange", "Fast Air Evacuation", "Fruiting Air Entry"), "Beginner", "Fruiting"));
        bank.add(mcq("Do mushrooms require light for fruiting?", "Yes, indirect light", List.of("No, complete darkness", "Yes, indirect light", "Yes, direct sunlight", "Only UV light"), "Intermediate", "Fruiting"));

        // Diseases (Intermediate/Advanced)
        bank.add(mcq("What is green mold disease caused by?", "Trichoderma species", List.of("Bacteria", "Trichoderma species", "Viruses", "Nematodes"), "Intermediate", "Diseases"));
        bank.add(mcq("What are symptoms of bacterial blotch?", "Yellow to brown lesions on caps", List.of("White fluffy growth", "Yellow to brown lesions on caps", "Black spots on stems", "Green discoloration"), "Intermediate", "Diseases"));
        bank.add(mcq("What causes cobweb mold?", "Dactylium (Cladobotryum) species", List.of("Aspergillus", "Penicillium", "Dactylium (Cladobotryum) species", "Bacillus"), "Advanced", "Diseases"));
        bank.add(mcq("How can Trichoderma contamination be identified?", "Green spore masses on substrate", List.of("Green spore masses on substrate", "Black liquid oozing from substrate", "Yellow discoloration of mycelium", "White powdery patches"), "Intermediate", "Diseases"));
        bank.add(mcq("What is the best practice for handling contaminated spawn?", "Isolate and dispose immediately", List.of("Treat with fungicide", "Isolate and dispose immediately", "Remove visible mold only", "Increase temperature to kill mold"), "Beginner", "Diseases"));
        bank.add(mcq("What causes wet bubble disease?", "Mycogone perniciosa", List.of("Pseudomonas bacteria", "Mycogone perniciosa", "Trichoderma viride", "Lecanicillium fungicola"), "Advanced", "Diseases"));
        bank.add(mcq("What is the primary cause of mushroom viruses?", "Infected spawn", List.of("Poor air quality", "Infected spawn",("High humidity"), "Overwatering"), "Advanced", "Diseases"));

        // Harvesting (Beginner/Intermediate)
        bank.add(mcq("When should oyster mushrooms be harvested?", "When caps are fully formed but before spore drop", List.of("When pins first appear", "When caps are fully formed but before spore drop", "After caps flatten completely",("When stems reach maximum height")), "Beginner", "Harvesting"));
        bank.add(mcq("What is the proper harvesting technique?", "Twist and pull gently at the base", List.of("Cut at the cap", "Twist and pull gently at the base",("Pull straight up quickly")), "Beginner", "Harvesting"));
        bank.add(mcq("What does biological efficiency (BE) measure?", "Ratio of fresh mushroom weight to dry substrate weight", List.of("Ratio of fresh mushroom weight to dry substrate weight", "Percentage of substrate colonized",("Number of mushrooms per square meter")), "Intermediate", "Harvesting"));
        bank.add(mcq("What is a typical BE for oyster mushrooms on straw?", "100-200%", List.of("20-40%", "50-80%", "100-200%", "300-500%"), "Intermediate", "Harvesting"));
        bank.add(mcq("How many flushes can be expected from a typical oyster mushroom block?", "2-3 flushes", List.of("1 flush", "2-3 flushes", "5-6 flushes", "7-10 flushes"), "Beginner", "Harvesting"));

        // Packaging & Storage (Beginner/Intermediate)
        bank.add(mcq("What is the ideal storage temperature for fresh mushrooms?", "2-4°C", List.of("-5°C", "2-4°C", "10-12°C", "Room temperature"), "Beginner", "Storage"));
        bank.add(mcq("What humidity level should be maintained for mushroom storage?", "90-95%", List.of("50-60%", "70-80%", "90-95%", "100%"), "Intermediate", "Storage"));
        bank.add(mcq("How long can fresh mushrooms be stored under optimal conditions?", "7-14 days", List.of("1-2 days", "7-14 days", "30-45 days",("3-6 months")), "Beginner", "Storage"));
        bank.add(mcq("What is the advantage of vacuum packaging?", "Extended shelf life up to 3 weeks", List.of("Lower cost", "Extended shelf life up to 3 weeks",("Better appearance")), "Intermediate", "Storage"));
        bank.add(mcq("What is the recommended moisture content for dried mushrooms?", "10-12%", List.of("5%", "10-12%", "15-20%",("25-30%")), "Intermediate", "Storage"));

        // Incubation (Intermediate)
        bank.add(mcq("What happens if incubation temperature exceeds 30°C for most gourmet mushrooms?", "Mycelium may die or become stressed", List.of("Faster growth", "Mycelium may die or become stressed", ("Increased yield")), "Intermediate", "Incubation"));
        bank.add(mcq("Should light be provided during incubation?", "Usually no - mycelium grows best in darkness", List.of("Yes, 12 hours per day", "Usually no - mycelium grows best in darkness",("Only blue light")), "Intermediate", "Incubation"));
        bank.add(mcq("What gas accumulates during incubation and can inhibit growth?", "Carbon dioxide (CO2)", List.of("Oxygen", "Carbon dioxide (CO2)", "Nitrogen",("Hydrogen")), "Intermediate", "Incubation"));
        bank.add(mcq("What does 'full colonization' of substrate look like?", "Uniform white mycelium throughout", List.of("White patches on surface", "Uniform white mycelium throughout",("Brown discoloration"), "Yellow liquid droplets"), "Beginner", "Incubation"));

        // Advanced topics (Advanced)
        bank.add(mcq("What is the role of exothermic composting in Phase II composting?", "Eliminates ammonia and creates selective substrate", List.of("Adds nutrients", "Eliminates ammonia and creates selective substrate",("Increases temperature"), "Kills pests"), "Advanced", "Composting"));
        bank.add(mcq("What is the difference between supplement and additive in substrate?", "Supplements add nutrition; additives adjust physical properties", List.of("They are the same", "Supplements add nutrition; additives adjust physical properties",("Supplements are liquid; additives are solid"), "Supplements are for fruiting; additives are for spawning"), "Advanced", "Substrate"));
        bank.add(mcq("What causes 'stroma' formation in mushroom cultivation?", "Excessive CO2 during incubation", List.of("Bacterial infection", "Excessive CO2 during incubation",("Too much light"), "High temperature"), "Advanced", "Diseases"));

        return bank;
    }

    private static Map<String, Object> mcq(String question, String correct, List<String> options, String difficulty, String topic) {
        Map<String, Object> q = new LinkedHashMap<>();
        q.put("type", "mcq");
        q.put("question", question);
        q.put("options", options);
        q.put("correctAnswer", correct);
        q.put("difficulty", difficulty);
        q.put("topic", topic);
        return q;
    }

    private static final String[][] SHORT_ANSWER_BANK = {
        {"Explain the difference between sterilization and pasteurization, including temperature ranges and purposes.",
         "Sterilization (121°C, 15 PSI) kills all microorganisms including spores. Pasteurization (65-70°C) kills harmful organisms while preserving beneficial heat-resistant microbes. Sterilization is used for nutrient-rich substrates like grain spawn; pasteurization for straw-based substrates.",
         "Intermediate"},
        {"Describe the mushroom lifecycle from spore to harvest.",
         "Spore germination -> Primary mycelium -> Mating -> Secondary mycelium -> Colonization of substrate -> Pinning -> Primordia formation -> Fruiting body development -> Spore production -> Spore release. The cycle takes 3-8 weeks depending on species.",
         "Beginner"},
        {"What factors influence pinning initiation in mushroom cultivation?",
         "Temperature drop (5-8°C), increased fresh air exchange (FAE), higher humidity (90-95%), light exposure (indirect), and reduction in CO2 levels (<1000 ppm). These environmental changes signal the mycelium to transition from vegetative growth to reproductive phase.",
         "Intermediate"},
        {"How do you identify and treat green mold (Trichoderma) contamination?",
         "Identification: Green spore masses, rapid spread, sweet or musty smell, white mycelium turning green. Treatment: Immediate isolation and disposal of contaminated materials, sterilization of equipment, review of sterile technique. Prevention: Proper sterilization protocols, clean air handling, and regular monitoring.",
         "Intermediate"},
        {"What are the optimal fruiting conditions for shiitake mushrooms?",
         "Temperature: 15-20°C, Humidity: 85-90%, FAE: 4-6 air exchanges per hour, Light: 500-2000 lux indirect light, CO2: <1000 ppm. Fruiting is triggered by temperature shock and increased air exchange after full substrate colonization.",
         "Advanced"},
        {"Explain the process of making grain spawn from start to finish.",
         "1. Select and clean grain (rye, wheat, millet) 2. Soak for 12-24 hours 3. Boil for 15-20 minutes 4. Drain and dry surface moisture 5. Fill jars 2/3 full 6. Sterilize at 15 PSI for 90 minutes 7. Cool to room temperature 8. Inoculate with liquid culture or agar under sterile conditions 9. Incubate at 22-25°C 10. Shake at 30% colonization 11. Use when fully colonized (10-14 days).",
         "Intermediate"},
        {"What is biological efficiency and how is it calculated?",
         "Biological Efficiency (BE) = (Fresh mushroom weight / Dry substrate weight) x 100. It measures how efficiently the substrate is converted into mushrooms. A BE of 100% means 1 kg of dry substrate produces 1 kg of fresh mushrooms. Oyster mushrooms on straw typically achieve 100-200% BE.",
         "Intermediate"},
        {"Describe the integrated pest management approach for mushroom cultivation.",
         "IPM combines: 1) Prevention - proper sterilization, filtration, hygiene protocols 2) Monitoring - regular inspection, sticky traps, environmental logging 3) Identification - accurate pest/disease diagnosis 4) Intervention - biological controls (beneficial microbes), physical barriers, targeted treatments as last resort. Chemical pesticides are minimized.",
         "Advanced"},
        {"What are the key parameters for quality control in commercial mushroom production?",
         "1. Substrate quality (moisture, pH, C:N ratio, sterility) 2. Spawn quality (vigor, contamination-free, storage conditions) 3. Environmental control (temperature, humidity, CO2, light consistency) 4. Harvest timing (maturity stage, cleanliness) 5. Post-harvest handling (cooling, grading, packaging) 6. Traceability (batch records, labeling).",
         "Advanced"},
        {"How does the supplementation of sawdust substrates work and what are common supplements?",
         "Supplementation adds nitrogen-rich materials to sawdust (which is carbon-heavy) to balance the C:N ratio. Common supplements: wheat bran (10-20%), soy hulls, rice bran, corn meal, alfalfa meal. Supplements increase yield but also increase contamination risk, so sterilization is essential for supplemented substrates.",
         "Intermediate"}
    };

    private static final String[][] SCENARIO_BANK = {
        {"You open an incubation room and notice 3 out of 20 grain spawn jars have green patches. Describe your actions.",
         "What is the first thing you should do?|How would you investigate the cause?|What preventive measures would you implement?",
         "Immediately isolate affected jars and dispose of them outside the facility. Inspect remaining jars daily. Review sterilization logs, check autoclave temperature records, and observe aseptic technique of staff. Implement stricter hygiene protocols and retrain staff on sterile technique.",
         "Intermediate"},
        {"Your fruiting room has produced small, long-stemmed mushrooms with tiny caps. What is the likely cause?",
         "What environmental parameter is likely imbalanced?|How would you correct it?|What long-term monitoring would you set up?",
         "Small caps with long stems (etiolation) indicate insufficient fresh air exchange and high CO2 levels. Increase FAE by adjusting fan timers or opening passive vents. Ensure CO2 levels stay below 1000 ppm. Install CO2 monitor with alerts for long-term control.",
         "Advanced"},
        {"A farmer has been getting only 30% BE from oyster mushrooms on straw. Analyze possible reasons.",
         "What factors could cause low biological efficiency?|How would you diagnose the issue?|What specific improvements would you recommend?",
         "Low BE can be caused by: poor spawn quality, inadequate substrate moisture (too dry or wet), incorrect C:N ratio, low-quality straw (treated with fungicides), improper pasteurization, or poor environmental control during fruiting. Recommend testing each variable: moisture test, spawn test on agar, check pasteurization temperature logs, and review environmental data.",
         "Advanced"}
    };
}
