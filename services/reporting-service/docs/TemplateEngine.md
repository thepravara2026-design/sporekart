# Template Engine

## Purpose
Manages reusable report templates that define the structure, sections, and default configuration for each report category.

## Template Categories
| Category | Templates |
|---|---|
| EXECUTIVE | Executive Summary, CEO Daily Brief |
| BUSINESS_HEALTH | Business Overview, Business Health |
| REVENUE | Revenue Summary, Revenue Forecast |
| OPERATIONAL | Order Operations, Inventory Health |
| AI | AI Platform Utilization, Automation Operations |
| HEALTH | Platform Health (Daily, Weekly) |
| RISK | Risk Summary |
| COMPLIANCE | Compliance Status |

## Template Schema
```json
{
  "id": "uuid",
  "name": "Executive Summary Template",
  "description": "Standard executive report layout",
  "category": "EXECUTIVE",
  "type": "EXECUTIVE",
  "owner": "Executive",
  "sections": ["Executive Overview", "Key Metrics", "Business Health", "Recommendations", "Risk Flags"],
  "defaultConfig": { "includeCharts": true, "maxKpis": 10, "theme": "professional" },
  "active": true,
  "createdAt": "...",
  "updatedAt": "..."
}
```

## Template Lifecycle
1. **CREATE** — TemplateEngine.createTemplate()
2. **ACTIVATE/DEACTIVATE** — Toggle active status
3. **UPDATE** — Update name, description, sections, config
4. **DELETE** — Deactivate (templates are soft-deleted via active flag)

## Template Usage Tracking
- Recorded via ReportTelemetryService.recordTemplateUsage()
- Tracked per template ID for utilization analytics
