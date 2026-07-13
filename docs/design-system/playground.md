# Design Playground — Architecture & Purpose

## Purpose

The Design Playground is the **official review environment** for every component in the SporeKart Design System. It replaces ad-hoc testing by providing a dedicated, isolated, production-excluded environment where designers, engineers, and reviewers can inspect, interact with, and validate all components before they are frozen and released.

The Playground is **excluded from production builds** via tree-shaking and route-level code splitting.

## Review Pipeline

```
Design → Implement → Preview → Review → Approve → Freeze → Document → Release
```

| Stage | Owner | Artifact |
|-------|-------|----------|
| **Design** | Product Designer | Figma spec, design brief |
| **Implement** | Component Engineer | Component code, tests |
| **Preview** | Component Engineer | Playground preview page |
| **Review** | Design System Council | Review notes, feedback |
| **Approve** | Principal DS Architect | Approval signature |
| **Freeze** | Principal DS Architect | Freeze marker in manifest |
| **Document** | Technical Writer | Docs center entry |
| **Release** | Release Manager | npm publish, changelog |

## Playground Structure

```
playground/
├── catalog/
│   ├── componentManifest.ts      # Component metadata registry
│   ├── tokenManifest.ts          # Token metadata registry
│   └── searchIndex.ts            # Client-side search engine
├── components/
│   ├── ComponentPreview.tsx      # Live preview with prop controls
│   ├── ResponsivePreview.tsx     # Viewport switcher (desktop → mobile)
│   ├── ThemePreview.tsx          # Light/dark/high-contrast toggle
│   ├── PropsTable.tsx            # Auto-generated prop documentation
│   ├── CodeBlock.tsx             # Syntax-highlighted code with copy
│   └── TokenDisplay.tsx          # Token swatch/value card
├── pages/                        # Individual component preview pages
│   ├── ButtonsPreview.tsx
│   ├── InputsPreview.tsx
│   ├── CardsPreview.tsx
│   └── ... (one per component)
├── DesignPlayground.tsx          # Homepage / overview
├── ComponentCatalog.tsx          # Browsable catalog with filters
├── ComponentDetailPage.tsx       # Per-component detail view
├── TokenExplorer.tsx             # Token category overview
├── TokenCategoryPage.tsx         # Individual token type explorer
├── IconLibrary.tsx               # Searchable icon browser
├── AccessibilityCenter.tsx       # WCAG compliance dashboard
├── DocumentationCenter.tsx       # Docs hub
├── QualityDashboard.tsx          # Quality metrics
├── SearchOverlay.tsx             # Global search overlay
```

## Routes

| Route | Page | Description |
|-------|------|-------------|
| `/design-system` | DesignPlayground | Home/overview |
| `/design-system/catalog` | ComponentCatalog | All components |
| `/design-system/component/:id` | ComponentDetailPage | Single component |
| `/design-system/tokens` | TokenExplorer | Token overview |
| `/design-system/tokens/:category` | TokenCategoryPage | Category detail |
| `/design-system/icons` | IconLibrary | Icon gallery |
| `/design-system/accessibility` | AccessibilityCenter | A11y status |
| `/design-system/docs` | DocumentationCenter | Docs hub |
| `/design-system/quality` | QualityDashboard | Metrics |

## Adding a New Component to the Playground

1. Create preview page in `playground/pages/`
2. Register entry in `playground/catalog/componentManifest.ts`
3. Add search entry in `playground/catalog/searchIndex.ts`
4. Verify the component appears in the catalog and search

## Design Decisions

- **No production code** references the Playground — build tooling strips it via `process.env.NODE_ENV` guards
- **Single source of truth** — the manifest drives catalog, search, and quality dashboard
- **Preview pages own their layout** — each preview is fully self-contained
- **Tokens are read from JSON files** — the token explorer parses `tokens/semantic/*.json` directly
