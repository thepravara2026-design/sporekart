package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentModerationService;
import com.sporekart.ai.content.domain.ContentModerationRequest;
import com.sporekart.ai.content.domain.ContentModerationResult;
import com.sporekart.ai.content.domain.ModerationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentModerationServiceImpl implements ContentModerationService {

    private static final Logger log = LoggerFactory.getLogger(ContentModerationServiceImpl.class);

    @Override
    public ContentModerationResult moderate(ContentModerationRequest request) {
        log.info("Moderating content for request {}", request.id());
        return new ContentModerationResult(
                UUID.randomUUID(),
                request.id(),
                ModerationStatus.APPROVED,
                false,
                false,
                false,
                0.99,
                List.of(),
                false,
                null,
                null
        );
    }

    @Override
    public ContentModerationResult review(UUID moderationId, UUID reviewerId, ModerationStatus decision) {
        log.info("Reviewing moderation {} by {} with decision {}", moderationId, reviewerId, decision);
        return new ContentModerationResult(
                moderationId,
                null,
                decision,
                false,
                false,
                false,
                1.0,
                List.of(),
                decision == ModerationStatus.FLAGGED_FOR_REVIEW,
                reviewerId,
                OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ContentModerationResult> getModeration(UUID id) {
        log.debug("Fetching moderation {}", id);
        return Optional.empty();
    }
}
