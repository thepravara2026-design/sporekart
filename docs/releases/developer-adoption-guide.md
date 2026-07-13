# Developer Adoption Guide — v1.0.0

> **Target Audience**: Application developers integrating the Enterprise Design System  
> **Prerequisites**: React 18+, TypeScript 5+, Node 18+

---

## Getting Started

### 1. Project Setup

Ensure your project meets minimum requirements:

```json
{
  "engines": { "node": ">=18" },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "typescript": "^5.0.0"
  }
}
```

### 2. Install Dependencies

```bash
npm install @enterprise/design-system @enterprise/tokens
```

Required packages:

| Package | Purpose |
|---------|---------|
| `@enterprise/design-system` | Component library |
| `@enterprise/tokens` | Design token CSS and TypeScript definitions |
| `@enterprise/icons` (optional) | Icon set |

### 3. Provider Setup

Wrap your application root with the design system providers:

```tsx
import { ThemeProvider } from '@enterprise/design-system';

function App() {
  return (
    <ThemeProvider theme="light">
      {/* Your application content */}
    </ThemeProvider>
  );
}
```

Additional providers as needed:

- `ToastProvider` — for toast notifications
- `ModalProvider` — for modal management

### 4. First Component

```tsx
import { Button } from '@enterprise/design-system';
import type { ButtonProps } from '@enterprise/design-system';

function MyPage() {
  return (
    <Button variant="primary" size="md">
      Submit
    </Button>
  );
}
```

---

## Import Conventions

Use deep imports for direct access. **Do not** import from barrel index files in application code.

```tsx
// Core components
import { Button } from '@enterprise/design-system/components/core/Button';
import { Input } from '@enterprise/design-system/components/core/Input';
import { Badge } from '@enterprise/design-system/components/core/Badge';

// Composite components
import { Card } from '@enterprise/design-system/components/composite/Card';
import { DataTable } from '@enterprise/design-system/components/composite/DataTable';
import { FormField } from '@enterprise/design-system/components/composite/FormField';

// Layout components
import { Container } from '@enterprise/design-system/components/layout/Container';
import { Grid } from '@enterprise/design-system/components/layout/Grid';
```

---

## Folder Structure Reference

```
@enterprise/design-system/
├── components/
│   ├── core/          # Atomic components (Button, Input, Badge, Icon)
│   ├── composite/     # Compound components (Card, DataTable, FormField)
│   └── layout/        # Layout primitives (Container, Grid, Stack)
├── tokens/            # Design token definitions (CSS, JS, TypeScript)
├── icons/             # SVG icon components
├── themes/            # Theme configurations (light, dark, high-contrast)
├── hooks/             # Shared React hooks (useMediaQuery, useTheme)
├── utils/             # Utility functions (cn, formatDate)
├── types/             # Shared TypeScript types and interfaces
├── test/              # Test utilities and helpers
└── docs/              # Documentation, migration guides, changelog
```

---

## Naming Conventions

| Artifact | Convention | Example |
|----------|------------|---------|
| React components | PascalCase | `Button`, `DataTable` |
| Utilities / hooks | camelCase | `useMediaQuery`, `formatDate` |
| CSS custom properties | kebab-case | `--color-primary-500` |
| Files | Match export name | `Button.tsx`, `useMediaQuery.ts` |
| Test files | `<name>.test.tsx` | `Button.test.tsx` |
| Style files | `<name>.styles.ts` | `Button.styles.ts` |

---

## Styling Rules

1. Use **CSS custom properties only** — reference tokens via `var(--token-name)`.
2. Inline styles **must** be cast as `React.CSSProperties`.
3. No hardcoded values — colors, spacing, typography, shadows, radius, and transitions must all use tokens.
4. Use semantic tokens over primitive tokens where possible.

```tsx
// ✅ Correct
const styles: React.CSSProperties = {
  color: 'var(--color-primary-500)',
  padding: 'var(--spacing-md)',
  borderRadius: 'var(--radius-md)',
};

// ❌ Incorrect
const styles = {
  color: '#3B82F6',           // hardcoded
  padding: '16px',            // hardcoded
  borderRadius: '6px',        // hardcoded
};
```

---

## Contribution Workflow

Follow this workflow when adding or modifying components:

1. **Fork / create branch** from `main` — name convention: `feat/<component-name>` or `fix/<issue-description>`.
2. **Implement** per the Component Governance Policy — all ten lifecycle stages.
3. **Add to component manifest** — update `docs/component-manifest.json`.
4. **Create playground preview** — add a Storybook story or Playground entry.
5. **Ensure WCAG 2.2 AA compliance** — run `npm run a11y:check`.
6. **Ensure responsive behavior** — verify at mobile (375px), tablet (768px), laptop (1024px), and desktop (1280px+).
7. **Submit for review** — open a pull request with all checklist items complete.

---

## Code Review Expectations

Every pull request will be evaluated against these criteria:

| Criterion | Requirement |
|-----------|-------------|
| TypeScript strict mode | Must pass `tsc --strict` with no errors |
| No unused imports or variables | `noUnusedLocals` and `noUnusedParameters` must pass |
| Token-based styling | Verified — no hardcoded values |
| Accessibility | WCAG 2.2 AA compliance verified |
| Responsive behavior | All breakpoints verified |
| Test coverage | ≥ 90% for new code |
| Documentation | Props table, usage examples, migration notes if applicable |

---

## Do NOT Duplicate Components

Before creating a new component:

1. Check the **Component Catalog** (`docs/component-manifest.json`) for existing components.
2. Check the **Experimental Catalog** for in-progress components that may cover your use case.
3. If a similar component exists, extend it or propose modifications rather than building from scratch.

Duplicate components fragment the system, increase maintenance burden, and confuse consumers. The Design System Architect may reject PRs that introduce unnecessary duplication.
