package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskClassificationService;
import com.sporekart.ai.risk.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskClassificationServiceImpl implements RiskClassificationService {

    @Override
    public RiskCategory classify(RiskAssessment assessment) {
        Map<String, Object> context = assessment.context();
        if (context == null || context.isEmpty()) {
            return RiskCategory.TECHNICAL;
        }

        if (context.containsKey("security") || context.containsKey("privacy")) {
            return RiskCategory.SECURITY;
        }
        if (context.containsKey("compliance") || context.containsKey("regulation")) {
            return RiskCategory.COMPLIANCE;
        }
        if (context.containsKey("financial") || context.containsKey("revenue")) {
            return RiskCategory.FINANCIAL;
        }
        if (context.containsKey("reputation") || context.containsKey("brand")) {
            return RiskCategory.REPUTATIONAL;
        }
        if (context.containsKey("ethical") || context.containsKey("fairness")) {
            return RiskCategory.ETHICAL;
        }
        if (context.containsKey("operational") || context.containsKey("process")) {
            return RiskCategory.OPERATIONAL;
        }

        log.info("Classified assessment {} as {}", assessment.id(), RiskCategory.TECHNICAL);
        return RiskCategory.TECHNICAL;
    }

    @Override
    public List<RiskCategory> classifyFactors(RiskAssessment assessment) {
        List<RiskCategory> categories = new ArrayList<>();
        Map<String, Object> context = assessment.context();

        if (context != null) {
            if (context.containsKey("security") || context.containsKey("privacy")) {
                categories.add(RiskCategory.SECURITY);
            }
            if (context.containsKey("compliance") || context.containsKey("regulation")) {
                categories.add(RiskCategory.COMPLIANCE);
            }
            if (context.containsKey("financial") || context.containsKey("revenue")) {
                categories.add(RiskCategory.FINANCIAL);
            }
            if (context.containsKey("reputation") || context.containsKey("brand")) {
                categories.add(RiskCategory.REPUTATIONAL);
            }
            if (context.containsKey("ethical") || context.containsKey("fairness")) {
                categories.add(RiskCategory.ETHICAL);
            }
            if (context.containsKey("operational") || context.containsKey("process")) {
                categories.add(RiskCategory.OPERATIONAL);
            }
        }

        if (categories.isEmpty()) {
            categories.add(RiskCategory.TECHNICAL);
        }

        return categories;
    }

    @Override
    public Map<RiskCategory, Double> categorizeScores(Map<RiskCategory, Double> scores) {
        return scores.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> (a + b) / 2.0,
                LinkedHashMap::new
            ));
    }
}
