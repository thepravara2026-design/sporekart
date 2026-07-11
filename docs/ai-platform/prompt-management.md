# Prompt Management Platform

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Status:** Implemented (Sprint 17 Part 4)

---

## Purpose

The Enterprise Prompt Management Platform provides centralized prompt template management for all AI interactions. Prompts are never hardcoded inside business services — every AI interaction loads prompts from this platform.

---

## Prompt Categories

| Category | Description | Default |
|----------|-------------|---------|
| Customer Support | Support agent prompts | ✓ |
| Grower Assistant | Farming/cultivation assistant | ✓ |
| Training | Training content prompts | ✓ |
| Product Recommendations | Product suggestion prompts | ✓ |
| Marketplace | Marketplace assistant | ✓ |
| SEO | SEO optimization prompts | ✓ |
| Marketing | Marketing content | ✓ |
| Content Generation | Content creation | ✓ |
| Analytics | Data analysis prompts | ✓ |
| ERP | ERP integration | ✓ |
| Internal Assistant | Internal operations | ✓ |
| System Prompt | System-level prompts | ✓ |
| Developer Prompt | Developer assistance | ✓ |

13 default categories seeded automatically via `AiPromptConfig`.

---

## Template Variables

### Standard Placeholders

| Variable | Type | Description |
|----------|------|-------------|
| `{{customerName}}` | String | Customer display name |
| `{{productName}}` | String | Product display name |
| `{{trainingName}}` | String | Training module name |
| `{{language}}` | String | Language code (en, hi, mr, etc.) |
| `{{region}}` | String | Region/locale |
| `{{conversationHistory}}` | String | Prior conversation context |
| `{{currentDate}}` | Date | Current date |
| `{{userRole}}` | String | User role identifier |

### Custom Variables

Any custom variable can be defined per template with:

- Name (unique per template)
- Type (STRING, NUMBER, BOOLEAN, DATE, LIST, OBJECT)
- Required flag
- Default value
- Validation regex
- Display order
- Description

---

## Template Engine Behavior

| Scenario | Behavior |
|----------|----------|
| Missing required variable | Throws `PromptValidationException` |
| Unresolved `{{variable}}` | Throws `PromptRenderException` |
| Injection pattern detected | Throws `PromptValidationException` |
| Payload exceeds 100KB | Throws `PromptValidationException` |
| Variable exceeds 10KB | Throws `PromptValidationException` |
| Template exceeds 50KB | Throws `PromptValidationException` |
| Regex validation fails | Throws `PromptValidationException` |
| All variables resolved | Returns rendered string |

---

## Version States

| State | Description | Can Transition To |
|-------|-------------|-------------------|
| DRAFT | Initial editable state | PENDING_APPROVAL |
| PENDING_APPROVAL | Submitted for review | APPROVED, DRAFT |
| APPROVED | Reviewed and accepted | PUBLISHED |
| PUBLISHED | Active production version | DEPRECATED |
| DEPRECATED | No longer recommended | ARCHIVED |
| ARCHIVED | End of life | (terminal) |

---

## Approval Workflow

```
Author creates/edits template (DRAFT)
        │
        ▼
Author submits for approval (PENDING_APPROVAL)
        │
        ├── Reviewer approves → (APPROVED) → Publisher publishes → (PUBLISHED)
        └── Reviewer rejects  → (DRAFT) → Author revises
```

---

## Security

| Role | Capabilities |
|------|-------------|
| Prompt Author | Create templates, edit own drafts, submit for approval |
| Prompt Reviewer | View pending prompts, approve/reject |
| Prompt Publisher | Publish approved prompts, deprecate, rollback |
| Administrator | Full access: create/edit/delete/approve/publish/import/export |

### Input Validation

- Prompt injection detection via regex patterns
- Variable injection detection (nested `{{}}`, `${}`, `<script>`, `javascript:`)
- Payload size limits
- Variable regex validation
- Template length limits

---

## Events

| Event | Trigger | Payload |
|-------|---------|---------|
| `PromptCreated` | Template created | templateId, name, createdBy, timestamp |
| `PromptUpdated` | Template updated | templateId, name, updatedBy, timestamp |
| `PromptPublished` | Template published | templateId, versionNumber, publishedBy, timestamp |
| `PromptDeprecated` | Template deprecated | templateId, deprecatedBy, timestamp |
| `PromptRolledBack` | Version rollback | templateId, fromVersion, toVersion, rolledBackBy, timestamp |
| `PromptExecutionStarted` | Prompt execution | templateId, correlationId, timestamp |
| `PromptExecutionCompleted` | Execution completed | templateId, correlationId, durationMs, success, timestamp |

---

## Database

6 tables in Flyway V13:

| Table | Records | Key Columns |
|-------|---------|-------------|
| `ai_prompt_categories` | Categories | name (unique), display_order, is_active |
| `ai_prompt_templates` | Templates | category_id (FK), name, template_text, status, current_version |
| `ai_prompt_versions` | Versions | template_id (FK), version_number, template_text, status, approved_by |
| `ai_prompt_variables` | Variables | template_id (FK), name, var_type, required, validation_regex |
| `ai_prompt_audit` | Audit trail | template_id, action, previous_value (JSONB), new_value (JSONB) |
| `ai_prompt_execution_log` | Execution log | template_id, prompt_text, rendered_text, duration_ms, success |
