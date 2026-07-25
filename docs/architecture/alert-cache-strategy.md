# Alert Cache Strategy

## Purpose
In-memory TTL-based caching for alert queries to reduce redundant computation and improve response times.

## Implementation
- **Data Structure:** `ConcurrentHashMap<String, AlertCacheEntry>`
- **TTL:** Configurable per entry (default: 60000ms)
- **Eviction:** Lazy; entries removed on read if expired

## AlertCacheEntry
| Field | Type | Description |
|---|---|---|
| id | String | Cache key |
| data | T | Cached payload |
| timestamp | Instant | When cached |
| ttl | Duration | Time-to-live |
| hitCount | long | Access counter |

## Operations
| Operation | Description |
|---|---|
| `get(key)` | Returns cached value or null (auto-evicts expired) |
| `put(key, value, ttl)` | Stores with optional TTL |
| `invalidate(key)` | Removes single entry |
| `invalidateAll()` | Clears entire cache |
| `getStats()` | Returns cache size + hit counts |

## Key Methods (AlertCacheService)
- `cacheAlerts(key, alerts)` — Cache alert query results
- `getCachedAlerts(key)` — Retrieve cached alerts
- `clearCache()` — Invalidate all cached entries
- `getCacheSize()` — Return current cache entry count
