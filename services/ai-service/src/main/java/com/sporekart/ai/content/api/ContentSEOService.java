package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentSEOService {
    ContentSEOResult generateSEO(ContentSEORequest request);
    ContentSEOResult analyzeSEO(String content, String targetKeyword);
    Optional<ContentSEOResult> getSEOAnalysis(UUID id);
}
