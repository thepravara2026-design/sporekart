package com.sporekart.ai.prompt.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sporekart.ai.prompt.domain.AuditAction;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class PromptImportExportService {

    private final PromptCategoryRepository categoryRepository;
    private final PromptTemplateRepository templateRepository;
    private final PromptVariableRepository variableRepository;
    private final PromptAuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    public PromptImportExportService(PromptCategoryRepository categoryRepository,
                                     PromptTemplateRepository templateRepository,
                                     PromptVariableRepository variableRepository,
                                     PromptAuditRepository auditRepository,
                                     ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
        this.variableRepository = variableRepository;
        this.auditRepository = auditRepository;
        this.objectMapper = objectMapper;
    }

    public String exportAll() {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("version", "1.0");
        root.put("exportedAt", OffsetDateTime.now().toString());

        ArrayNode categoriesArray = root.putArray("categories");
        List<PromptCategoryEntity> categories = categoryRepository.findByIsDeletedFalseOrderByDisplayOrder();
        for (PromptCategoryEntity cat : categories) {
            ObjectNode catNode = categoriesArray.addObject();
            catNode.put("name", cat.getName());
            catNode.put("description", cat.getDescription());
            catNode.put("icon", cat.getIcon());
            catNode.put("displayOrder", cat.getDisplayOrder());

            ArrayNode templatesArray = catNode.putArray("templates");
            List<PromptTemplateEntity> templates = templateRepository.findByCategoryIdAndIsDeletedFalse(cat.getId());
            for (PromptTemplateEntity tmpl : templates) {
                ObjectNode tmplNode = templatesArray.addObject();
                tmplNode.put("name", tmpl.getName());
                tmplNode.put("description", tmpl.getDescription());
                tmplNode.put("templateText", tmpl.getTemplateText());
                tmplNode.put("status", tmpl.getStatus());

                ArrayNode varsArray = tmplNode.putArray("variables");
                List<PromptVariableEntity> vars = variableRepository
                        .findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(tmpl.getId());
                for (PromptVariableEntity var : vars) {
                    ObjectNode varNode = varsArray.addObject();
                    varNode.put("name", var.getName());
                    varNode.put("type", var.getVarType());
                    varNode.put("required", var.isRequired());
                    varNode.put("defaultValue", var.getDefaultValue());
                    varNode.put("description", var.getDescription());
                }
            }
        }
        return root.toPrettyString();
    }

    @Transactional
    public int importPrompts(String jsonContent, UUID importedBy) {
        try {
            JsonNode root = objectMapper.readTree(jsonContent);
            if (!root.has("categories")) {
                throw new PromptValidationException("Invalid import format: missing categories");
            }
            int count = 0;
            JsonNode categories = root.get("categories");
            for (JsonNode catNode : categories) {
                String catName = catNode.get("name").asText();
                PromptCategoryEntity category = categoryRepository.findByNameAndIsDeletedFalse(catName)
                        .orElseGet(() -> {
                            PromptCategoryEntity newCat = new PromptCategoryEntity(
                                    catName,
                                    catNode.has("description") ? catNode.get("description").asText() : null,
                                    catNode.has("icon") ? catNode.get("icon").asText() : null,
                                    catNode.has("displayOrder") ? catNode.get("displayOrder").asInt() : 0);
                            newCat.setCreatedBy(importedBy);
                            return categoryRepository.save(newCat);
                        });

                if (catNode.has("templates")) {
                    for (JsonNode tmplNode : catNode.get("templates")) {
                        String tmplName = tmplNode.get("name").asText();
                        if (templateRepository.existsByNameAndCategoryIdAndIsDeletedFalse(tmplName, category.getId())) {
                            continue;
                        }
                        PromptTemplateEntity template = new PromptTemplateEntity();
                        template.setCategory(category);
                        template.setName(tmplName);
                        template.setDescription(tmplNode.has("description") ? tmplNode.get("description").asText() : null);
                        template.setTemplateText(tmplNode.get("templateText").asText());
                        template.setStatus(tmplNode.has("status") ? tmplNode.get("status").asText() : "DRAFT");
                        template.setCreatedBy(importedBy);
                        template.setCreatedAt(OffsetDateTime.now());
                        PromptTemplateEntity savedTemplate = templateRepository.save(template);

                        if (tmplNode.has("variables")) {
                            for (JsonNode varNode : tmplNode.get("variables")) {
                                PromptVariableEntity var = new PromptVariableEntity();
                                var.setTemplate(savedTemplate);
                                var.setName(varNode.get("name").asText());
                                var.setVarType(varNode.has("type") ? varNode.get("type").asText() : "STRING");
                                var.setRequired(!varNode.has("required") || varNode.get("required").asBoolean());
                                var.setDefaultValue(varNode.has("defaultValue") ? varNode.get("defaultValue").asText() : null);
                                var.setDescription(varNode.has("description") ? varNode.get("description").asText() : null);
                                var.setCreatedAt(OffsetDateTime.now());
                                variableRepository.save(var);
                            }
                        }
                        count++;
                    }
                }
            }
            auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                    null, null, AuditAction.IMPORT_EXPORT.name(), "IMPORT",
                    null, null, null, importedBy, "Imported " + count + " prompts"));
            return count;
        } catch (JsonProcessingException e) {
            throw new PromptValidationException("Invalid JSON format: " + e.getMessage());
        }
    }
}
