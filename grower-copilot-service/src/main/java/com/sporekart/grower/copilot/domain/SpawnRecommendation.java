package com.sporekart.grower.copilot.domain;

import java.util.List;

public record SpawnRecommendation(
    String spawnId,
    String speciesName,
    String variety,
    String spawnType,
    String difficulty,
    double optimalTempLow,
    double optimalTempHigh,
    double optimalHumidityLow,
    double optimalHumidityHigh,
    int spawnRunDays,
    int pinningDays,
    int harvestDays,
    double expectedYieldKg,
    String climateSuitability,
    String commercialSuitability,
    List<String> trainingRecommendations,
    String description,
    List<String> advantages,
    List<String> disadvantages
) {}
