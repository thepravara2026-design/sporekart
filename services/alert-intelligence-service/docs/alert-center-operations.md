# Alert Center Operations

## Dashboard
The Alert Center Dashboard provides a real-time view of all system alerts with metric cards and an actionable table.

## Metric Cards
| Metric | Description |
|---|---|
| Total Alerts | Count of all alerts in the system |
| Critical | Count of CRITICAL severity alerts |
| High | Count of HIGH severity alerts |
| Open | Count of OPEN status alerts |

## Alert Table Columns
- Severity (color-coded badge)
- Title
- Category
- Source
- Timestamp
- Status
- Actions (Acknowledge / Resolve)

## Action Flow
1. **Acknowledge** — Operator clicks "Acknowledge" → POST `/{id}/acknowledge` → Status becomes ACKNOWLEDGED → Badge updates
2. **Resolve** — Operator clicks "Resolve" → POST `/{id}/resolve` → Status becomes RESOLVED → Row updates

## Filtering & Querying
Supported query parameters: `category`, `severity`, `status`, `source`, `timerange`

## Alert Categories
- BUSINESS, OPERATIONAL, SECURITY, PERFORMANCE, MAINTENANCE, INFORMATIONAL
