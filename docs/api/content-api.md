# Content API Reference

**Base URL:** `/api/v1/content`
**Module:** ai-service

---

## Authentication

All endpoints require authentication via Bearer JWT token. Include `Authorization: Bearer <token>` header.

## Rate Limiting

| Operation | Limit | Window |
|-----------|-------|--------|
| Generation | 50 req/min | 60 seconds |
| Other operations | 100 req/min | 60 seconds |

---

## Content Generation

### Generate Content

`POST /api/v1/content/generate`

**Request Body:**
```json
{
  "type": "PRODUCT_DESC",
  "format": "MARKDOWN",
  "tone": "PROFESSIONAL",
  "templateId": "uuid",
  "parameters": {
    "productName": "Organic Tomato Seeds",
    "variety": "Cherry Red",
    "growingTime": "60-70 days",
    "features": ["non-GMO", "heirloom", "high yield"]
  },
  "wordCount": 200,
  "language": "en",
  "sessionId": "uuid",
  "metadata": {}
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "type": "PRODUCT_DESC",
  "format": "MARKDOWN",
  "tone": "PROFESSIONAL",
  "content": "**Organic Cherry Red Tomato Seeds**\n\nOur premium Organic Cherry Red Tomato seeds are carefully selected for home gardeners and commercial growers alike...",
  "wordCount": 198,
  "status": "APPROVED",
  "moderationStatus": "ALLOW",
  "createdAt": "2026-07-12T10:00:00+05:30",
  "metadata": {
    "templateId": "uuid",
    "templateName": "Product Description Template",
    "promptTokens": 120,
    "completionTokens": 85,
    "totalTokens": 205,
    "latencyMs": 2340
  }
}
```

---

## Summarization

### Summarize Content

`POST /api/v1/content/summarize`

**Request Body:**
```json
{
  "content": "Long content text to summarize...",
  "ratio": 0.3,
  "format": "PLAIN_TEXT",
  "language": "en"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "originalLength": 5000,
  "summaryLength": 1500,
  "ratio": 0.3,
  "summary": "Summarized content text...",
  "format": "PLAIN_TEXT",
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Translation

### Translate Content

`POST /api/v1/content/translate`

**Request Body:**
```json
{
  "content": "How to grow organic tomatoes in your backyard",
  "sourceLanguage": "en",
  "targetLanguage": "es",
  "format": "PLAIN_TEXT"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "sourceLanguage": "en",
  "targetLanguage": "es",
  "sourceText": "How to grow organic tomatoes in your backyard",
  "translatedText": "Cómo cultivar tomates orgánicos en tu patio trasero",
  "confidence": 0.97,
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Classification

### Classify Content

`POST /api/v1/content/classify`

**Request Body:**
```json
{
  "content": "Organic tomato seeds are now available in our store with special discounts for bulk purchases.",
  "categories": ["PRODUCT", "MARKETING", "SALES"],
  "maxCategories": 5,
  "includeSentiment": true,
  "includeEntities": true,
  "includeTopics": true
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "categories": [
    {"name": "PRODUCT", "confidence": 0.92},
    {"name": "MARKETING", "confidence": 0.78},
    {"name": "SALES", "confidence": 0.65}
  ],
  "sentiment": "POSITIVE",
  "sentimentScore": 0.85,
  "language": "en",
  "languageConfidence": 0.99,
  "topics": ["organic farming", "seeds", "discounts"],
  "entities": [
    {"name": "Organic tomato seeds", "type": "PRODUCT"},
    {"name": "store", "type": "LOCATION"}
  ],
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Moderation

### Moderate Content

`POST /api/v1/content/moderate`

**Request Body:**
```json
{
  "content": "Content to moderate for policy compliance...",
  "severityThreshold": "OFFENSIVE",
  "categories": ["HATE_SPEECH", "SPAM", "VIOLENCE", "SEXUAL"]
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "severity": "HARMLESS",
  "action": "ALLOW",
  "categories": [],
  "confidence": 0.99,
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

**Response when flagged (200):**
```json
{
  "id": "uuid",
  "severity": "OFFENSIVE",
  "action": "FLAG",
  "categories": ["SPAM"],
  "confidence": 0.88,
  "details": "Content detected as promotional spam",
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## SEO Optimization

### Optimize Content for SEO

`POST /api/v1/content/seo/optimize`

**Request Body:**
```json
{
  "content": "How to grow organic tomatoes - complete guide for beginners. Learn about soil preparation, watering, and harvesting.",
  "title": "How to Grow Organic Tomatoes",
  "keywords": ["organic tomatoes", "gardening"],
  "url": "/guides/grow-organic-tomatoes",
  "language": "en"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "seoTitle": "How to Grow Organic Tomatoes: Complete Beginner's Guide",
  "metaDescription": "Learn how to grow organic tomatoes from seed to harvest. Complete guide covering soil preparation, watering schedules, pest control, and harvesting tips for beginners.",
  "keywords": ["organic tomatoes", "gardening for beginners", "tomato growing guide", "organic gardening", "vegetable garden"],
  "slug": "how-to-grow-organic-tomatoes-complete-guide",
  "canonicalUrl": "/guides/grow-organic-tomatoes",
  "ogTitle": "How to Grow Organic Tomatoes: Complete Beginner's Guide",
  "ogDescription": "Step-by-step guide to growing organic tomatoes at home. From seed selection to harvesting.",
  "score": 85,
  "suggestions": [
    "Add more internal links to related guides",
    "Include customer reviews and ratings",
    "Consider adding FAQ schema markup"
  ],
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Templates

### List Templates

`GET /api/v1/content/templates?type=PRODUCT_DESC&format=MARKDOWN&page=0&size=20`

**Response (200):**
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Product Description Template",
      "type": "PRODUCT_DESC",
      "format": "MARKDOWN",
      "description": "Standard template for product descriptions",
      "variables": ["productName", "variety", "growingTime", "features"],
      "isActive": true,
      "version": 2,
      "createdAt": "2026-07-11T10:00:00+05:30"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 20
}
```

### Create Template

`POST /api/v1/content/templates`

**Request Body:**
```json
{
  "name": "Product Description Template",
  "type": "PRODUCT_DESC",
  "format": "MARKDOWN",
  "description": "Standard template for product descriptions",
  "templateText": "**{{productName}}**\n\n{{description}}\n\n**Key Features:**\n{{#features}}- {{feature}}\n{{/features}}\n\n**Growing Time:** {{growingTime}}",
  "variables": [
    {"name": "productName", "type": "STRING", "required": true},
    {"name": "description", "type": "STRING", "required": true},
    {"name": "features", "type": "LIST", "required": true},
    {"name": "growingTime", "type": "STRING", "required": false}
  ],
  "category": "PRODUCT",
  "tags": ["product", "description", "ecommerce"]
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "name": "Product Description Template",
  "type": "PRODUCT_DESC",
  "format": "MARKDOWN",
  "version": 1,
  "isActive": true,
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Recommendations

### Get Content Recommendations

`POST /api/v1/content/recommend`

**Request Body:**
```json
{
  "contentId": "uuid",
  "strategy": "SIMILARITY",
  "limit": 5,
  "categories": ["GROWER_GUIDE", "PRODUCT"],
  "language": "en"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "sourceContentId": "uuid",
  "strategy": "SIMILARITY",
  "recommendations": [
    {
      "contentId": "uuid",
      "title": "Organic Fertilizer Guide",
      "score": 0.92,
      "reason": "Similar content category and topics"
    },
    {
      "contentId": "uuid",
      "title": "Seasonal Planting Calendar",
      "score": 0.85,
      "reason": "Related gardening topics"
    }
  ],
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

---

## Health

### Health Check

`GET /api/v1/content/health`

**Response (200):**
```json
{
  "status": "UP",
  "generationCount": 0,
  "cacheAvailable": true,
  "modules": {
    "generation": "UP",
    "summarization": "UP",
    "translation": "UP",
    "classification": "UP",
    "moderation": "UP",
    "seo": "UP",
    "recommendation": "UP",
    "templates": "UP"
  }
}
```

---

## Error Responses

All errors follow RFC 9457 Problem Details format:

| Status | Type | Description |
|--------|------|-------------|
| 400 | `validation-error` | Invalid input parameters |
| 401 | `unauthorized` | Missing or invalid authentication |
| 403 | `forbidden` | Insufficient permissions |
| 404 | `not-found` | Template or resource not found |
| 429 | `rate-limit-exceeded` | Rate limit exceeded |
| 500 | `internal-error` | Internal server error |

**Error Response Example (400):**
```json
{
  "type": "validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Content type is required",
  "instance": "/api/v1/content/generate",
  "correlationId": "uuid",
  "timestamp": "2026-07-12T10:00:00+05:30"
}
```
