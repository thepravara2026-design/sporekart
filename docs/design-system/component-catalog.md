# Component Catalog

## Overview

The Component Catalog is the central, browsable directory of every design system component. It is powered by the `componentManifest.ts` data file and provides filtering, searching, and detail views for all components across the system.

## Manifest Structure

Components are defined in `frontend/web-app/src/design-system/playground/catalog/componentManifest.ts`. Each entry conforms to the `ComponentEntry` interface:

```typescript
interface ComponentEntry {
  id: string;                    // URL-safe unique identifier
  name: string;                  // Display name
  category: ComponentCategory;   // Grouping category
  description: string;           // One-line purpose
  version: string;               // Semantic version
  owner: string;                 // Responsible team/person
  status: 'stable' | 'beta' | 'deprecated';
  approvalStatus: 'approved' | 'pending' | 'in-review';
  accessibilityStatus: 'pass' | 'partial' | 'needs-review';
  responsiveStatus: 'pass' | 'partial' | 'not-tested';
  lastUpdated: string;           // ISO 8601 date
  docsPath: string;              // Relative path to docs
  previewPath: string;           // Route to preview page
  designPurpose: string;         // Why this component exists
  businessUsage: string;         // When to use in products
  variants: string[];            // Visual/style variants
  states: string[];              // Interactive states
  tokens: string[];              // CSS custom properties used
  dependencies: string[];        // Internal component deps
  relatedComponents: string[];   // Similar components
  knownLimitations: string[];    // Current constraints
  futureEnhancements: string[];  // Planned improvements
  keyboardShortcuts: { key: string; description: string }[];
  ariaRoles: string[];           // Assigned ARIA roles
  reviewStatus: 'approved' | 'pending' | 'changes-requested';
  reviewer?: string;
  reviewDate?: string;
  approvalDate?: string;
  pendingIssues: string[];
  knownBugs: string[];
}
```

## Auto-Discovery Mechanism

The manifest is the **single source of truth** for the catalog. There is no filesystem-walking auto-discovery — every component must be explicitly registered. This ensures:

- Full control over what appears (internal utilities are excluded)
- Complete metadata for every entry
- Consistent ordering and categorization
- Audit trail via version control

## Categories

| Category | Description | Example |
|----------|-------------|---------|
| `core` | Atomic interactive elements | Button, Input, Checkbox |
| `forms` | Form-specific composites | Select, FileUpload, MultiStepForm |
| `display` | Data presentation | Card, Table, Badge, Avatar |
| `navigation` | Movement between views | Header, Sidebar, Tabs, Breadcrumb |
| `feedback` | User notifications | Toast, Modal, Tooltip, Alert |
| `charts` | Data visualization | LineChart, BarChart, PieChart |
| `layout` | Page structure | PageContainer, Grid, Stack |

## Usage Guide for Engineers

### Adding a New Component

1. Create the component source in the appropriate layer directory
2. Create a preview page in `playground/pages/`
3. Add a `ComponentEntry` to the appropriate category array in `componentManifest.ts`
4. Add search entries in `searchIndex.ts`
5. Verify the component appears in the catalog at `/design-system/catalog`

### Updating Metadata

When a component's status changes (stable → deprecated, beta → stable), update only the `componentManifest.ts` entry. The catalog, search index, and quality dashboard will reflect the change on next build.

### Removing a Component

Before removing, mark the component as `status: 'deprecated'` in the manifest. After the deprecation period (minimum 12 months), remove the entry entirely.

## Manifest Validation

The manifest is validated at build time by a CI check that ensures:

- Every `id` is unique across all entries
- Every `docsPath` points to an existing file
- Every `previewPath` corresponds to a registered route
- `status` transitions follow the lifecycle (experimental → beta → stable → deprecated)
- Required fields are non-empty

## Catalog UI Features

- **Search** — filters by name, description, and category
- **Category filter** — pills for each category
- **Status badge** — color-coded (stable=green, beta=amber, deprecated=red)
- **Accessibility badge** — pass/partial/needs-review
- **Responsive badge** — pass/partial/not-tested
- **Detail view** — full metadata, preview, props table, code example
