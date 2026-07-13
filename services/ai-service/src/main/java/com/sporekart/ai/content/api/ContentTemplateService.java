package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContentTemplateService {
    ContentTemplate createTemplate(ContentTemplate template);
    ContentTemplate updateTemplate(ContentTemplate template);
    void deleteTemplate(UUID id);
    Optional<ContentTemplate> getTemplate(UUID id);
    List<ContentTemplate> listTemplates(ContentCategory category);
    ContentTemplate renderTemplate(UUID templateId, java.util.Map<String, Object> variables);
}
