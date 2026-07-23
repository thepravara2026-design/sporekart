# Marketing Copilot API Reference

Base URL: `http://localhost:8107/api/v1/copilot/marketing`

All authenticated endpoints require JWT Bearer token.

## Universal Query Endpoint

### POST /query
Route any marketing intent to the appropriate engine.

```json
{
  "query": "string",
  "intent": "content|campaign|seo|aeogeo|social|email|whatsapp|analytics|brand|growth|strategy",
  "audience": "string (optional)",
  "campaignId": "string (optional)"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "queryId": "uuid",
    "intent": "content",
    "summary": "Generated content for: ...",
    "data": { ... },
    "recommendations": ["..."],
    "processingTimeMs": 123
  }
}
```

---

## Content

### POST /content/generate
Generate marketing content (blog, product description, landing page, etc.).

**Request:**
```json
{
  "contentType": "blog_article|product_description|landing_page|social_post|email|whatsapp_message|ad_copy|press_release|newsletter",
  "topic": "string",
  "audience": "string (optional)",
  "tone": "string (optional)",
  "locale": "string (optional)",
  "keywords": ["string"] (optional),
  "wordCount": 500 (optional),
  "campaignId": "string (optional)",
  "brandVoice": "string (optional)"
}
```

---

## Campaign

### POST /campaign/plan
Plan a multi-channel marketing campaign.

**Request:**
```json
{
  "name": "string",
  "type": "launch|seasonal|brand_awareness|lead_generation|retention|reactivation|cross_sell|upsell|promotional|educational",
  "objective": "string",
  "targetAudience": "string (optional)",
  "startDate": "2026-01-01 (optional)",
  "endDate": "2026-02-01 (optional)",
  "budget": 50000.0,
  "channels": ["email", "social", "paid_ads"] (optional),
  "brandVoice": "string (optional)"
}
```

---

## SEO

### POST /seo/analyze
Analyze and optimize SEO for a URL or keyword.

**Request:**
```json
{
  "url": "https://sporekart.com/example",
  "targetKeyword": "string",
  "locale": "en_IN (optional)",
  "contentType": "article (optional)",
  "currentContent": "string (optional)"
}
```

---

## AEO/GEO

### POST /aeogeo/analyze
Analyze answer engine optimization and generative engine optimization.

**Request:**
```json
{
  "query": "string",
  "content": "string",
  "locale": "en_IN (optional)"
}
```

---

## Social Media

### POST /social/generate
Generate platform-specific social media content.

**Request:**
```json
{
  "platform": "instagram|facebook|linkedin|youtube|threads|pinterest|twitter",
  "topic": "string",
  "postType": "IMAGE|VIDEO|CAROUSEL|STORY|REEL|TEXT_ONLY|POLL|LIVE (optional)",
  "tone": "string (optional)",
  "hashtags": ["string"] (optional),
  "callToAction": "string (optional)",
  "campaignId": "string (optional)"
}
```

---

## Email

### POST /email/generate
Generate email campaign content.

**Request:**
```json
{
  "subject": "string",
  "emailType": "promotional|transactional|newsletter|abandoned_cart|welcome|reengagement|educational|event",
  "targetSegment": "string (optional)",
  "tone": "string (optional)",
  "callToAction": "string (optional)",
  "campaignId": "string (optional)"
}
```

---

## WhatsApp

### POST /whatsapp/generate
Generate WhatsApp marketing messages.

**Request:**
```json
{
  "messageType": "text|image|video|document|interactive|template",
  "content": "string",
  "recipientSegment": "string (optional)",
  "campaignId": "string (optional)",
  "callToAction": "string (optional)"
}
```

---

## Analytics

### POST /analytics/campaign
Analyze campaign performance.

**Request:**
```json
{
  "campaignId": "string",
  "startDate": "2026-01-01 (optional)",
  "endDate": "2026-03-31 (optional)"
}
```

---

## Brand Governance

### POST /brand/check
Check content for brand consistency.

**Request:**
```json
{
  "content": "string",
  "brandVoice": "string (optional)",
  "contentType": "string (optional)"
}
```

---

## Growth Recommendations

### POST /growth/recommendations
Get data-driven growth recommendations.

**Parameters:**
- `audience` (query, default: "general")
- `segment` (query, default: "all")

---

## Strategy

### POST /strategy/brief
Generate a marketing strategy brief.

**Request:**
```json
{
  "objective": "string",
  "targetAudience": "string",
  "keyMessage": "string (optional)",
  "channels": ["string"] (optional),
  "budget": 50000.0 (optional),
  "timeline": "string (optional)"
}
```

---

## Health

### GET /health
Health check endpoint — no authentication required.

**Response:**
```json
{
  "success": true,
  "message": "Marketing Copilot is operational",
  "data": null
}
```
