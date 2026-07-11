package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.ContentRequest;
import com.sporekart.ai.content.domain.ContentResult;
import java.util.List;

public interface ContentService {
    ContentResult generate(ContentRequest request);
    List<ContentResult> generateBatch(List<ContentRequest> requests);
    ContentResult summarize(String text, int maxLength);
    ContentResult translate(String text, String targetLanguage);
}
