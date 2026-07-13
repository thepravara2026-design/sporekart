package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentSummaryService;
import com.sporekart.ai.content.domain.ContentSummaryRequest;
import com.sporekart.ai.content.domain.ContentSummaryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentSummaryServiceImpl implements ContentSummaryService {

    private static final Logger log = LoggerFactory.getLogger(ContentSummaryServiceImpl.class);

    @Override
    public ContentSummaryResult summarize(ContentSummaryRequest request) {
        log.info("Summarizing content for request {}", request.id());
        return new ContentSummaryResult(
                UUID.randomUUID(),
                request.id(),
                "Stub summary of: " + (request.sourceText().length() > 50
                        ? request.sourceText().substring(0, 50) + "..."
                        : request.sourceText()),
                request.sourceText().length(),
                50,
                0.5,
                request.language() != null ? request.language() : "en",
                OffsetDateTime.now()
        );
    }

    @Override
    public ContentSummaryResult summarizeWithKeyPoints(ContentSummaryRequest request) {
        log.info("Summarizing with key points for request {}", request.id());
        return new ContentSummaryResult(
                UUID.randomUUID(),
                request.id(),
                "Stub summary with key points of: " + (request.sourceText().length() > 50
                        ? request.sourceText().substring(0, 50) + "..."
                        : request.sourceText()),
                request.sourceText().length(),
                50,
                0.5,
                request.language() != null ? request.language() : "en",
                OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ContentSummaryResult> getSummary(UUID id) {
        log.debug("Fetching summary {}", id);
        return Optional.empty();
    }
}
