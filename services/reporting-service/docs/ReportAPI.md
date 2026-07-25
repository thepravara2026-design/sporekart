# Report API

## Base URL
```
http://localhost:8096/api/v1/reports
```

## Authentication
All endpoints require Basic auth with ADMIN role. Public access to `/actuator/health`, `/swagger-ui/**`, `/v3/api-docs/**`.

## Health
| Method | Path | Description |
|---|---|---|
| GET | `/health` | Service health check |

## Reports
| Method | Path | Description |
|---|---|---|
| GET | `/` | List all reports |
| GET | `/{id}` | Get report by ID |
| GET | `/type/{type}` | By report type |
| GET | `/category/{category}` | By category |
| GET | `/status/{status}` | By status |
| POST | `/generate` | Generate seeded reports |
| POST | `/generate/{category}` | Generate by category |

## Templates
| Method | Path | Description |
|---|---|---|
| GET | `/templates` | List all templates |
| GET | `/templates/{id}` | Get by ID |
| GET | `/templates/active` | Active templates |
| POST | `/templates/generate` | Generate seeded templates |

## Schedules
| Method | Path | Description |
|---|---|---|
| GET | `/schedules` | List all schedules |
| GET | `/schedules/{id}` | Get by ID |
| GET | `/schedules/active` | Active schedules |
| POST | `/schedules` | Create schedule |
| POST | `/schedules/{id}/pause` | Pause schedule |
| POST | `/schedules/{id}/resume` | Resume |
| POST | `/schedules/{id}/execute` | Execute now |
| DELETE | `/schedules/{id}` | Delete |

## Exports
| Method | Path | Description |
|---|---|---|
| GET | `/exports` | List all exports |
| GET | `/exports/{id}` | Get by ID |
| POST | `/{reportId}/export/{format}` | Export (PDF/EXCEL/CSV/JSON) |

## BI Reports
| Method | Path | Description |
|---|---|---|
| GET | `/bi` | List BI reports |
| GET | `/bi/{id}` | Get by ID |
| POST | `/bi/generate` | Generate BI reports |

## Cache
| Method | Path | Description |
|---|---|---|
| GET | `/cache` | Cache stats |
| DELETE | `/cache` | Clear cache |

## Telemetry
| Method | Path | Description |
|---|---|---|
| GET | `/telemetry` | Current metrics |
| GET | `/telemetry/history` | Metric history + recent activity |

## Response Format
```json
{
  "id": "uuid",
  "title": "CEO Daily Report",
  "type": "DAILY",
  "category": "EXECUTIVE",
  "status": "GENERATED",
  "summary": "Strong revenue performance...",
  "kpis": { "revenueGrowth": 12.0 },
  "traceId": "uuid"
}
```
