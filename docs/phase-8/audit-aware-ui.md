# Audit-Aware UI

## Architecture

Reusable components for displaying audit metadata, timelines, and version history throughout the admin interface. All components accept typed data and render consistently with the enterprise design system.

```
AuditInfo (metadata display — created by, updated by, version)
  ├─ meta: AuditMeta
  └─ compact?: boolean

AuditTimeline (vertical activity timeline)
  ├─ entries: AuditEntry[]
  └─ maxItems?: number (default 10)

VersionHistory (version list with restore)
  ├─ versions: VersionEntry[]
  └─ onRestore?: (version: number) => void
```

## Types

```ts
interface AuditMeta {
  createdBy: string;
  createdAt: string;
  updatedBy?: string;
  updatedAt?: string;
  version?: number;
}

interface AuditEntry {
  id: string;
  action: string;
  performedBy: string;
  timestamp: string;
  details?: string;
  resource?: string;
  resourceId?: string;
}
```

## Usage

```tsx
// Metadata display
<AuditInfo meta={{ createdBy: 'John Doe', createdAt: '2026-07-10', updatedBy: 'Jane Smith', updatedAt: '2026-07-14', version: 3 }} />
<AuditInfo meta={meta} compact />

// Activity timeline
<AuditTimeline entries={auditEntries} maxItems={5} />

// Version history
<VersionHistory versions={versions} onRestore={(v) => handleRestore(v)} />
```

## Extension Points

- Connect `AuditTimeline` to backend activity log API
- Add filtering by resource type, user, or date range
- Link version restore to backend revision system
- Extend `AuditMeta` with custom fields as needed
