package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentTranslationService;
import com.sporekart.ai.content.domain.ContentTranslationRequest;
import com.sporekart.ai.content.domain.ContentTranslationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentTranslationServiceImpl implements ContentTranslationService {

    private static final Logger log = LoggerFactory.getLogger(ContentTranslationServiceImpl.class);

    @Override
    public ContentTranslationResult translate(ContentTranslationRequest request) {
        log.info("Translating content for request {}", request.id());
        return new ContentTranslationResult(
                UUID.randomUUID(),
                request.id(),
                request.sourceText(),
                request.sourceLanguage() != null ? request.sourceLanguage() : "en",
                request.targetLanguage(),
                "en",
                0.95,
                OffsetDateTime.now()
        );
    }

    @Override
    public String detectLanguage(String text) {
        log.debug("Detecting language for text of length {}", text != null ? text.length() : 0);
        return "en";
    }

    @Override
    public ContentTranslationResult translateWithGlossary(ContentTranslationRequest request) {
        log.info("Translating with glossary for request {}", request.id());
        return new ContentTranslationResult(
                UUID.randomUUID(),
                request.id(),
                request.sourceText(),
                request.sourceLanguage() != null ? request.sourceLanguage() : "en",
                request.targetLanguage(),
                "en",
                0.95,
                OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ContentTranslationResult> getTranslation(UUID id) {
        log.debug("Fetching translation {}", id);
        return Optional.empty();
    }
}
