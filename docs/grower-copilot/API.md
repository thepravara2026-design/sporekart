# Grower Copilot — API Reference

## Base URL

- **Production:** `https://api.sporekart.example/v1/grower-copilot`
- **Staging:** `https://staging.api.sporekart.example/v1/grower-copilot`
- **Local:** `http://localhost:8102/v1/grower-copilot`

## Authentication

All endpoints require a valid JWT bearer token obtained from the Identity Service (see `identity-service.yaml`).

## Common Response Format

```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "meta": {
    "requestId": "req_abc123",
    "processingTimeMs": 342,
    "engineVersion": "1.2.0"
  }
}
```

Error responses use RFC 9457 Problem Details:

```json
{
  "type": "https://api.sporekart.example/errors/invalid-input",
  "title": "Invalid Request",
  "status": 400,
  "detail": "species field is required",
  "instance": "/v1/grower-copilot/chat"
}
```

---

## 1. POST /chat

Send a conversational message to the Grower Copilot.

**Request:**

```json
{
  "message": "My mushrooms have brown spots on the cap",
  "context": {
    "species": "white_button",
    "region": "himachal",
    "sessionId": "sess_xyz789",
    "language": "en"
  },
  "attachments": [
    {
      "type": "image",
      "url": "https://cdn.sporekart.example/uploads/photo_1.jpg"
    }
  ]
}
```

**Response:**

```json
{
  "reply": "Based on your description of brown spots on white button mushroom caps, this could be Dry Bubble (Verticillium fungicola) or Bacterial Blotch (Pseudomonas tolaasii). Here is a detailed analysis...",
  "intent": "disease",
  "enginesUsed": ["disease", "knowledge"],
  "confidence": 0.87,
  "suggestions": [
    { "label": "View disease details", "action": "disease_detail", "payload": { ... } },
    { "label": "Check treatment options", "action": "treatment", "payload": { ... } }
  ],
  "knowledgeEntries": [
    { "id": "SK-005", "title": "Bacterial blotch identification and management" }
  ]
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| message | string | yes | Free-text message from grower |
| context.species | string | no | Target species identifier |
| context.region | string | no | Geographic region |
| context.sessionId | string | no | Session identifier for continuity |
| context.language | string | no | ISO language code (en/hi) |
| attachments | array | no | Image or document attachments |

---

## 2. POST /recommend

Get a structured recommendation for a specific query.

**Request:**

```json
{
  "type": "cultivation_plan",
  "species": "oyster",
  "region": "maharashtra",
  "parameters": {
    "substrate": "paddy_straw",
    "bags": 500,
    "experience_level": "intermediate"
  },
  "season": "kharif"
}
```

**Response:**

```json
{
  "recommendations": [
    {
      "category": "spawning",
      "priority": "high",
      "title": "Use 3% spawn rate for paddy straw substrate",
      "description": "...",
      "rationale": "Higher spawn rate compensates for lower substrate nutrition",
      "references": ["SK-015"]
    }
  ],
  "timeline": {
    "spawn_date": "2026-08-01",
    "first_harvest": "2026-08-22",
    "total_duration_days": 45
  }
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| type | string | yes | `cultivation_plan` / `treatment_plan` / `assessment` |
| species | string | yes | Mushroom species |
| region | string | yes | Indian state/region |
| parameters | object | no | Additional parameters |
| season | string | no | `kharif` / `rabi` / `summer` / `winter` |

---

## 3. POST /disease/analyze

Analyze disease symptoms and return diagnosis.

**Request:**

```json
{
  "symptoms": ["brown_spots", "necrotic_lesions", "cap_malformation"],
  "species": "white_button",
  "region": "himachal",
  "severity": "moderate",
  "images": [
    { "url": "https://cdn.sporekart.example/disease_1.jpg", "format": "jpeg" }
  ],
  "environment": {
    "temperature": 24,
    "humidity": 88,
    "co2": 1200
  }
}
```

**Response:**

```json
{
  "matches": [
    {
      "disease": "dry_bubble",
      "diseaseName": "Dry Bubble (Verticillium fungicola)",
      "confidence": 0.91,
      "symptoms_matched": ["brown_spots", "necrotic_lesions"],
      "symptoms_unmatched": ["cap_malformation"]
    }
  ],
  "severity": {
    "classification": "moderate",
    "score": 0.55,
    "affected_percentage_estimated": 22
  },
  "treatment": {
    "chemical": [
      {
        "name": "Carbendazim 50% WP",
        "dosage": "1 g/L water",
        "application": "Foliar spray, 300 ml/m²",
        "withholding_period_days": 7,
        "ppe": "Gloves, mask, goggles"
      }
    ],
    "biological": [
      {
        "agent": "Trichoderma viride",
        "application": "2 kg/ton compost at spawning",
        "timing": "At spawning stage"
      }
    ],
    "cultural": [
      {
        "measure": "Reduce humidity to 80-85%",
        "timing": "Immediately",
        "duration": "Until symptoms subside"
      }
    ],
    "integrated_idm": "Apply carbendazim spray + improve ventilation + reduce humidity. Follow with T. viride at next spawning cycle."
  },
  "prevention": [
    "Maintain humidity below 85% during cropping",
    "Ensure 4-6 air changes per hour",
    "Use foot baths with 2% formalin at entry",
    "Remove and destroy affected fruiting bodies immediately"
  ],
  "escalation": {
    "required": false,
    "reason": null
  },
  "references": ["SK-002", "SK-005", "DMR Guide 2020"]
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| symptoms | array | yes | Symptom identifiers from controlled vocabulary |
| species | string | yes | Target mushroom species |
| region | string | no | Growing region |
| severity | string | no | `mild` / `moderate` / `severe` / `critical` |
| images | array | no | Image attachments (Phase 1: URL only) |
| environment | object | no | Current growing environment readings |

---

## 4. POST /yield/predict

Predict yield for a given scenario.

**Request:**

```json
{
  "species": "white_button",
  "cultivation_config": {
    "area_sqm": 100,
    "compost_kg": 5000,
    "flushes_expected": 6
  },
  "environment": {
    "avg_temperature": 20,
    "avg_humidity": 85,
    "season": "winter"
  },
  "quality_factors": {
    "compost_quality": "good",
    "spawn_quality": "good"
  },
  "price_per_kg": 180
}
```

**Response:**

```json
{
  "predicted_yield_kg": 2700,
  "yield_per_sqm": 27.0,
  "efficiency_percentage": 90,
  "efficiency_label": "Excellent",
  "revenue_estimated": 486000,
  "cost_estimated": 325000,
  "net_profit_estimated": 161000,
  "roi_percentage": 49.5,
  "risk_score": 22,
  "risk_level": "low",
  "harvest_window": {
    "first_flush_earliest": "2026-09-15",
    "first_flush_latest": "2026-09-21",
    "total_cycle_days_min": 60,
    "total_cycle_days_max": 75
  },
  "breakdown_by_flush": [
    { "flush": 1, "yield_kg": 945, "percentage": 35, "estimated_days": 21 },
    { "flush": 2, "yield_kg": 675, "percentage": 25, "estimated_days": 14 },
    { "flush": 3, "yield_kg": 486, "percentage": 18, "estimated_days": 12 },
    { "flush": 4, "yield_kg": 594, "percentage": 22, "estimated_days": 18 }
  ],
  "sensitivity": {
    "temp_plus_2": { "yield_change_percent": -8, "risk_change": 5 },
    "temp_minus_2": { "yield_change_percent": -12, "risk_change": 8 },
    "compost_minus": { "yield_change_percent": -15, "risk_change": 10 }
  }
}
```

---

## 5. POST /weather

Get weather data and climate advisory for a location.

**Request:**

```json
{
  "location": {
    "lat": 30.9,
    "lon": 77.1,
    "name": "Solan"
  },
  "species": "white_button",
  "forecast_days": 7
}
```

**Response:**

```json
{
  "location": { "lat": 30.9, "lon": 77.1, "name": "Solan, HP" },
  "current": {
    "temperature": 24.5,
    "humidity": 82,
    "precipitation_mm": 2.3,
    "wind_speed": 5.2,
    "timestamp": "2026-07-23T10:00:00+05:30"
  },
  "forecast": [
    {
      "date": "2026-07-24",
      "temp_high": 26.0,
      "temp_low": 17.5,
      "humidity_avg": 80,
      "precipitation_probability": 30,
      "condition": "partly_cloudy"
    }
  ],
  "advisory": {
    "risk_level": "moderate",
    "recommendation": "Monitor humidity — levels above 85% increase bacterial blotch risk.",
    "action_items": [
      "Increase ventilation during humid hours",
      "Delay watering if rain exceeds 5mm"
    ],
    "affected_species": ["white_button"]
  },
  "provider": "open-meteo"
}
```

---

## 6. POST /plan

Generate a cultivation calendar and resource plan.

**Request:**

```json
{
  "species": ["white_button", "oyster", "milky"],
  "total_area_sqm": 200,
  "start_date": "2026-08-01",
  "region": "pune",
  "resource_constraints": {
    "max_labor_hours_per_week": 40,
    "available_compost_kg": 3000,
    "available_straw_kg": 1000
  },
  "rotation_gap_days": 7
}
```

**Response:**

```json
{
  "plan": {
    "start_date": "2026-08-01",
    "end_date": "2026-12-15",
    "total_cycles": 4
  },
  "calendar": [
    {
      "species": "white_button",
      "phase": "composting",
      "start_date": "2026-08-01",
      "end_date": "2026-08-21",
      "tasks": [
        { "day": 1, "task": "Prepare compost mix", "labor_hours": 8 },
        { "day": 3, "task": "Pasteurization start", "labor_hours": 4 }
      ]
    }
  ],
  "resource_summary": {
    "total_labor_hours": 320,
    "compost_required_kg": 2500,
    "straw_required_kg": 800,
    "water_required_liters": 12000
  },
  "conflicts": [
    { "date": "2026-09-15", "issue": "White button and oyster both require harvesting", "suggestion": "Consider staggering planting by 2 weeks" }
  ],
  "rotation_plan": {
    "recommended_sequence": ["white_button", "oyster", "milky"],
    "gap_days": 7,
    "annual_cycles_possible": 3
  }
}
```

---

## 7. POST /knowledge/search

Search the scientific knowledge repository.

**Request:**

```json
{
  "query": "bacterial blotch treatment carbendazim",
  "categories": ["disease_management"],
  "species": ["white_button"],
  "region": "himachal",
  "language": "en",
  "limit": 5
}
```

**Response:**

```json
{
  "results": [
    {
      "id": "SK-005",
      "title": "Bacterial blotch identification and management",
      "type": "practice_guide",
      "category": "disease_management",
      "species": ["white_button", "oyster"],
      "summary": "Detailed guide on identifying and managing bacterial blotch...",
      "citations": ["Singh et al. 2021", "DMR Solan Guide 2022"],
      "source": "Directorate of Mushroom Research, Solan",
      "relevance_score": 0.95
    }
  ],
  "total": 3,
  "limit": 5,
  "offset": 0
}
```

---

## 8. GET /health

Health check endpoint.

**Response:**

```json
{
  "status": "healthy",
  "version": "1.2.0",
  "uptime_seconds": 84231,
  "engines": {
    "disease": "healthy",
    "yield": "healthy",
    "cultivation": "healthy",
    "weather": "degraded",
    "knowledge": "healthy",
    "planning": "healthy",
    "recommendation": "healthy",
    "risk": "healthy",
    "reporting": "healthy"
  },
  "dependencies": {
    "identity_service": "healthy",
    "enterprise_ai_platform": "healthy",
    "knowledge_platform": "healthy",
    "weather_provider": "degraded"
  }
}
```

---

## Standard Error Codes

| HTTP Status | Code | Description |
|-------------|------|-------------|
| 400 | invalid-input | Request validation failed |
| 401 | unauthenticated | Missing or invalid authentication |
| 403 | unauthorized | Insufficient permissions |
| 404 | not-found | Resource not found |
| 422 | unprocessable-entity | Business logic validation failed |
| 429 | rate-limited | Too many requests |
| 500 | internal-error | Unexpected server error |
| 503 | service-unavailable | Dependency unavailable |

## Rate Limiting

- **Standard tier:** 60 requests/minute
- **Premium tier:** 300 requests/minute
- **Headers:** `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`
