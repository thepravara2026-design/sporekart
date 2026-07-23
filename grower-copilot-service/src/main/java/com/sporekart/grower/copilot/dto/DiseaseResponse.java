package com.sporekart.grower.copilot.dto;

import java.util.List;

public record DiseaseResponse(
    String diseaseId,
    String diseaseName,
    String scientificName,
    String category,
    List<String> matchedSymptoms,
    List<String> possibleCauses,
    double probabilityScore,
    String severity,
    String treatmentPlan,
    List<String> preventionMethods,
    List<String> scientificReferences,
    boolean requiresEscalation
) {}
