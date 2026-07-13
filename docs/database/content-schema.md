# Content Platform Database Schema

**Version:** 1.0.0
**Migration:** V18__sprint17_content_intelligence.sql
**Engine:** PostgreSQL (H2 compatible for dev/test)

---

## Tables

### content_templates

Stores content template definitions for AI content generation.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(255) | NOT NULL | Template name |
| description | TEXT | | Template description |
| type | VARCHAR(50) | NOT NULL | Content type (ARTICLE, BLOG, PRODUCT_DESC, etc.) |
| format | VARCHAR(50) | NOT NULL | Output format |
| template_text | TEXT | NOT NULL | Template content with {{variable}} placeholders |
| variables | TEXT | | JSON array of variable definitions |
| category | VARCHAR(100) | | Template category |
| tags | TEXT | | JSON array of tags |
| is_active | BOOLEAN | NOT NULL, DEFAULT TRUE | Active flag |
| version | INTEGER | NOT NULL, DEFAULT 1 | Version number |
| created_by | VARCHAR(255) | NOT NULL | Creator user ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_content_templates_type` (type), `idx_content_templates_category` (category), `idx_content_templates_active` (is_active)

---

### content_generations

Records all AI content generation requests and results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| template_id | UUID | FK to content_templates | Template used for generation |
| type | VARCHAR(50) | NOT NULL | Content type |
| format | VARCHAR(50) | NOT NULL | Output format |
| tone | VARCHAR(50) | | Tone configuration |
| source_text | TEXT | | Input/source text |
| generated_text | TEXT | NOT NULL | Generated content |
| parameters | TEXT | | JSON generation parameters |
| word_count | INTEGER | NOT NULL, DEFAULT 0 | Generated word count |
| language | VARCHAR(10) | NOT NULL, DEFAULT 'en' | Content language |
| status | VARCHAR(50) | NOT NULL | DRAFT, APPROVED, ARCHIVED |
| moderation_status | VARCHAR(50) | | Moderation result (ALLOW, FLAG, BLOCK) |
| prompt_tokens | INTEGER | | Token count for prompt |
| completion_tokens | INTEGER | | Token count for completion |
| total_tokens | INTEGER | | Total token usage |
| latency_ms | INTEGER | | Execution time in milliseconds |
| created_by | VARCHAR(255) | NOT NULL | Requester user ID |
| session_id | VARCHAR(255) | | Optional conversation session ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| completed_at | TIMESTAMP | | Completion timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_content_generations_template` (template_id), `idx_content_generations_type` (type), `idx_content_generations_created_by` (created_by), `idx_content_generations_created_at` (created_at)

---

### content_versions

Versions of generated content for tracking changes.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| generation_id | UUID | NOT NULL, FK to content_generations | Parent generation |
| version_number | INTEGER | NOT NULL | Version sequence number |
| content | TEXT | NOT NULL | Version content |
| change_summary | TEXT | | Summary of changes |
| created_by | VARCHAR(255) | NOT NULL | Author user ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_versions_generation` (generation_id)

---

### content_classifications

Stores content classification results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| content_id | UUID | NOT NULL | Related content identifier |
| source_content | TEXT | NOT NULL | Original classified content |
| categories | TEXT | | JSON array of category assignments |
| tags | TEXT | | JSON array of predicted tags |
| sentiment | VARCHAR(50) | | POSITIVE, NEGATIVE, NEUTRAL, MIXED |
| sentiment_score | DOUBLE | | Sentiment confidence (0-1) |
| language | VARCHAR(10) | | Detected language |
| language_confidence | DOUBLE | | Language detection confidence |
| topics | TEXT | | JSON array of extracted topics |
| entities | TEXT | | JSON array of recognized entities |
| confidence | DOUBLE | NOT NULL | Overall classification confidence |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_classifications_content` (content_id), `idx_content_classifications_sentiment` (sentiment)

---

### content_translations

Stores translation records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| content_id | UUID | NOT NULL | Related content identifier |
| source_language | VARCHAR(10) | NOT NULL | Source language code |
| target_language | VARCHAR(10) | NOT NULL | Target language code |
| source_text | TEXT | NOT NULL | Original text |
| translated_text | TEXT | NOT NULL | Translated text |
| provider | VARCHAR(50) | | Translation provider |
| confidence | DOUBLE | NOT NULL | Translation confidence (0-1) |
| latency_ms | INTEGER | | Translation time in milliseconds |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_translations_content` (content_id), `idx_content_translations_target` (target_language)

---

### content_moderation

Stores content moderation results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| content_id | UUID | NOT NULL | Related content identifier |
| content_text | TEXT | NOT NULL | Moderated content |
| severity | VARCHAR(50) | NOT NULL | HARMLESS, OFFENSIVE, HATEFUL, HARMFUL, SPAM |
| action | VARCHAR(50) | NOT NULL | ALLOW, FLAG, BLOCK, REVIEW |
| categories | TEXT | | JSON array of violation categories |
| confidence | DOUBLE | NOT NULL | Moderation confidence (0-1) |
| details | TEXT | | Detailed moderation explanation |
| reviewed_by | VARCHAR(255) | | Reviewer user ID |
| reviewed_at | TIMESTAMP | | Review timestamp |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_moderation_content` (content_id), `idx_content_moderation_severity` (severity), `idx_content_moderation_action` (action)

---

### content_seo_data

Stores SEO optimization results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| content_id | UUID | NOT NULL | Related content identifier |
| original_content | TEXT | NOT NULL | Original content for SEO |
| seo_title | VARCHAR(255) | | Optimized SEO title |
| meta_description | TEXT | | Meta description |
| keywords | TEXT | | JSON array of SEO keywords |
| slug | VARCHAR(255) | | URL slug |
| canonical_url | VARCHAR(500) | | Canonical URL |
| og_title | VARCHAR(255) | | Open Graph title |
| og_description | TEXT | | Open Graph description |
| score | INTEGER | NOT NULL, DEFAULT 0 | SEO score (0-100) |
| suggestions | TEXT | | JSON array of improvement suggestions |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_seo_content` (content_id)

---

### content_recommendations

Stores content recommendation results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| source_content_id | VARCHAR(255) | NOT NULL | Source content identifier |
| strategy | VARCHAR(50) | NOT NULL | Recommendation strategy |
| recommendations | TEXT | NOT NULL | JSON array of recommendations with scores |
| limit_value | INTEGER | NOT NULL, DEFAULT 10 | Max requested recommendations |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_content_recommendations_source` (source_content_id), `idx_content_recommendations_strategy` (strategy)

---

## Relationships

```
content_templates ──┐
                    │
                    ├──< content_generations ──< content_versions
                    │
content_generations ──── content_classifications
                    │
                    └─── content_translations
                    │
                    └─── content_moderation
                    │
                    └─── content_seo_data
                    
content_recommendations (standalone, references content by ID)
```

---

## Summary

| Table | Purpose | Key Indexes |
|-------|---------|-------------|
| content_templates | Template definitions | type, category, active |
| content_generations | Generation records | template, type, created_by, created_at |
| content_versions | Version tracking | generation_id |
| content_classifications | Classification results | content_id, sentiment |
| content_translations | Translation records | content_id, target_language |
| content_moderation | Moderation results | content_id, severity, action |
| content_seo_data | SEO optimization | content_id |
| content_recommendations | Recommendation results | source_content_id, strategy |
