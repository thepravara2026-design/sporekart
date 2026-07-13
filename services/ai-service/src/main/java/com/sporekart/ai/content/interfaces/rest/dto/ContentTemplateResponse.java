package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentTemplate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentTemplateResponse(
        UUID id,
        String name,
        String description,
        String category,
        String contentType,
        String templateContent,
        List<String> variables,
        boolean isActive,
        int version,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ContentTemplateResponse from(ContentTemplate template) {
        return new ContentTemplateResponse(
                template.id(), template.name(), template.description(),
                template.category().name(), template.contentType().name(),
                template.templateContent(), template.variables(),
                template.isActive(), template.version(),
                template.createdAt(), template.updatedAt()
        );
    }
}
