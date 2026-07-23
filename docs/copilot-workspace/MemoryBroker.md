# Memory Broker

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  

---

## Overview

The Memory Broker provides a shared, namespaced key-value store that is accessible to all copilots within a workspace. It enables cross-copilot memory without tight coupling, allowing one copilot to store data that another copilot can retrieve later (e.g., a Sales copilot storing a lead ID that the Marketing copilot uses for targeting).

---

## Memory Namespaces

| Namespace | Scope | Example Keys |
|-----------|-------|-------------|
| `workspace:{id}:shared` | All copilots in the workspace | `workspace:ws_123:shared:last_lead_id` |
| `workspace:{id}:copilot:{name}` | Specific copilot within a workspace | `workspace:ws_123:copilot:marketing:campaign_ref` |
| `user:{id}:preferences` | User-level preferences across workspaces | `user:u_456:preferences:language` |
| `global` | Global shared data (read-mostly) | `global:store_hours` |

---

## TTL and Expiration

| Entry Type | Default TTL | Configurable |
|-----------|-------------|-------------|
| Session-scoped memory | 24 hours | Yes, per set operation |
| Copilot scratch data | 1 hour | Yes, per set operation |
| User preferences | 7 days | Yes, per set operation |
| Global data | No TTL | N/A |

Keys expire automatically via the backing store (Redis TTL). Clients can also explicitly delete memory entries.

---

## Search Capabilities

The Memory Broker supports prefix-based and pattern-based key search:

- **List by prefix**: `GET /api/v1/workspace/{id}/memory?prefix=workspace:ws_123:shared`
- **List by namespace**: `GET /api/v1/workspace/{id}/memory?namespace=sales`
- **Pattern match**: `GET /api/v1/workspace/{id}/memory?pattern=*lead*`

Each search returns `key`, `value`, `ttl_remaining`, and `namespace`.

---

## Integration with Copilots

Copilots interact with the Memory Broker through the Copilot Workspace API:

- `POST /workspace/{id}/memory` — Set a memory entry
- `GET /workspace/{id}/memory/{key}` — Get a memory entry
- `DELETE /workspace/{id}/memory/{key}` — Delete a memory entry
- `GET /workspace/{id}/memory` — Search memory entries

Copilots do not access the Redis store directly — all memory operations go through the Workspace Service.
