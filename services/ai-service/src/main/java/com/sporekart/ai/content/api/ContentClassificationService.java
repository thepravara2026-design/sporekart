package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentClassificationService {
    ContentClassificationResult classify(ContentClassificationRequest request);
    ContentClassificationResult classifyWithKeywords(ContentClassificationRequest request);
    Optional<ContentClassificationResult> getClassification(UUID id);
}
