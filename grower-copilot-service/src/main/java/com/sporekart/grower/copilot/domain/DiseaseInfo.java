package com.sporekart.grower.copilot.domain;

import java.util.List;

public record DiseaseInfo(
    String diseaseId,
    String diseaseName,
    String scientificName,
    String category,
    List<String> symptoms,
    List<String> possibleCauses,
    double probabilityScore,
    String treatmentPlan,
    List<String> preventionMethods,
    List<String> scientificReferences,
    boolean requiresEscalation,
    String severity,
    List<String> imageIndicators
) {}
