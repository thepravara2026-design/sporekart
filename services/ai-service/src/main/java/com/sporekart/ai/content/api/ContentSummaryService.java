package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentSummaryService {
    ContentSummaryResult summarize(ContentSummaryRequest request);
    ContentSummaryResult summarizeWithKeyPoints(ContentSummaryRequest request);
    Optional<ContentSummaryResult> getSummary(UUID id);
}
