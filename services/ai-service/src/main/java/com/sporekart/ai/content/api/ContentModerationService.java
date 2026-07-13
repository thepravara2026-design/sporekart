package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentModerationService {
    ContentModerationResult moderate(ContentModerationRequest request);
    ContentModerationResult review(UUID moderationId, UUID reviewerId, ModerationStatus decision);
    Optional<ContentModerationResult> getModeration(UUID id);
}
