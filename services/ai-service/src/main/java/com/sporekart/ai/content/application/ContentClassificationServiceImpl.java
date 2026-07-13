package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentClassificationService;
import com.sporekart.ai.content.domain.ContentClassificationRequest;
import com.sporekart.ai.content.domain.ContentClassificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentClassificationServiceImpl implements ContentClassificationService {

    private static final Logger log = LoggerFactory.getLogger(ContentClassificationServiceImpl.class);

    @Override
    public ContentClassificationResult classify(ContentClassificationRequest request) {
        log.info("Classifying content for request {}", request.id());
        return new ContentClassificationResult(
                UUID.randomUUID(),
                request.id(),
                Map.of("general", 0.95),
                "general",
                0.95,
                List.of("general"),
                OffsetDateTime.now()
        );
    }

    @Override
    public ContentClassificationResult classifyWithKeywords(ContentClassificationRequest request) {
        log.info("Classifying with keywords for request {}", request.id());
        return new ContentClassificationResult(
                UUID.randomUUID(),
                request.id(),
                Map.of("general", 0.95, "text", 0.80),
                "general",
                0.95,
                List.of("general", "text", "content"),
                OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ContentClassificationResult> getClassification(UUID id) {
        log.debug("Fetching classification {}", id);
        return Optional.empty();
    }
}
