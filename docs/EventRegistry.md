# Event Registry

## Overview

The Event Registry is a centralized catalog of all event types in the SporeKart platform. It provides discovery, validation, versioning, deprecation, and documentation for every event.

## API

| Method | Description |
|--------|-------------|
| `register(eventType, description, domain)` | Register a new event type |
| `register(eventType, description, domain, version)` | Register with specific version |
| `isRegistered(eventType)` | Check if event type exists |
| `getDescriptor(eventType)` | Get event metadata |
| `getAll()` | List all registered events |
| `getByDomain(domain)` | List events for a domain |
| `deprecate(eventType)` | Mark event as deprecated |
| `search(query)` | Search events by name/description |
| `validate(eventType)` | Validate event is registered and not deprecated |

## Registration Format

Events are registered as `{domain}.{action}` using lowercase dot notation:

```
order.created
payment.succeeded
inventory.low
customer.registered
```

## Validation

The registry enforces:
- All events must be registered before publishing (optional, controlled by configuration)
- Deprecated events cannot be published
- Event type naming convention: `{domain}.{action}` using past tense verbs

## Versioning

Events carry a version field. When a breaking change is required:
1. Register a new event type with incremented version (e.g., `order.created.v2`)
2. Deprecate the old version
3. Allow both versions during migration period
4. Remove old version subscribers after migration
