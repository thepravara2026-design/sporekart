package com.sporekart.grower.copilot.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DiseaseRequest(
    @NotEmpty List<String> symptoms,
    String mushroomType,
    String growthStage,
    String environmentDescription,
    List<String> images
) {}
