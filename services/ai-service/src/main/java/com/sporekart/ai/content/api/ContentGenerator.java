package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.ContentRequest;
import com.sporekart.ai.content.domain.ContentResult;
import com.sporekart.ai.core.domain.ContentType;

public interface ContentGenerator {
    ContentResult generate(ContentRequest request);
    boolean supports(ContentType contentType);
}
