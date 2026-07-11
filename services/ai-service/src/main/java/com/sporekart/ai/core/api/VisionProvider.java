package com.sporekart.ai.core.api;

import java.util.List;

public interface VisionProvider {
    String analyzeImage(String imageBytes, String prompt, String model);
    String analyzeImageUrl(String imageUrl, String prompt, String model);
    List<String> extractTextFromImage(String imageBytes, String model);
}
