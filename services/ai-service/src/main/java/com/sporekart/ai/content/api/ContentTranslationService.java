package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentTranslationService {
    ContentTranslationResult translate(ContentTranslationRequest request);
    String detectLanguage(String text);
    ContentTranslationResult translateWithGlossary(ContentTranslationRequest request);
    Optional<ContentTranslationResult> getTranslation(UUID id);
}
