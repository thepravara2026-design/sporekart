# Enterprise Course Taxonomy & Knowledge Information Architecture

## Overview

The Enterprise Course Taxonomy Platform is the permanent **Knowledge Information Architecture** for the SporeKart Enterprise LMS. It provides a hierarchical, unlimited-depth taxonomy (Categories → Subcategories → ...), tag management, topic graphs, competency frameworks, and discovery metadata. Every future capability (Curriculum Builder, Learning Paths, AI Recommendations, Search Ranking, Analytics, Certifications) builds upon this taxonomy.

## Folder Structure

```
course-taxonomy/
├── data/taxonomyMockData.ts          # Types, mock data, option arrays, section config
├── state/
│   ├── useTaxonomyState.ts           # useReducer-based state (9 slices)
│   └── TaxonomyContext.tsx           # React context provider
├── components/
│   ├── TaxonomyLayout.tsx            # Header + sidebar + panel content
│   ├── shared/TaxonomySidebar.tsx    # 12-section navigation rail
│   ├── panels/
│   │   ├── OverviewPanel.tsx         # Dashboard widgets + hierarchy preview
│   │   ├── CategoryTreePanel.tsx     # Category management
│   │   ├── TagsPanel.tsx             # Tag CRUD by type
│   │   ├── TopicsPanel.tsx           # Topic library CRUD
│   │   ├── SkillsPanel.tsx           # 9 skill levels
│   │   ├── CompetenciesPanel.tsx     # 9 competency areas
│   │   ├── LanguagesPanel.tsx        # 7 languages
│   │   ├── DeliveryPanel.tsx         # 12 delivery modes
│   │   ├── HierarchyExplorerPanel.tsx# Explorer + knowledge map
│   │   ├── RelationshipsPanel.tsx    # Topic relationships + graph
│   │   ├── DiscoveryPanel.tsx        # Discovery metadata prep
│   │   └── LearningPathsPanel.tsx    # Learning path architecture
│   ├── visualization/
│   │   ├── HierarchyTree.tsx         # Recursive ARIA tree (unlimited depth)
│   │   ├── TaxonomyBreadcrumbs.tsx   # Ancestor path breadcrumb
│   │   ├── CategoryExplorer.tsx      # Tree + detail + add subcategory
│   │   ├── TopicGraphPlaceholder.tsx # Future graph placeholder
│   │   ├── KnowledgeMapPlaceholder.tsx # Future heat-map placeholder
│   │   └── RelationshipViewer.tsx    # Topic dependency/related viewer
│   └── widgets/TaxonomyDashboardWidgets.tsx # Reusable stat widgets
└── pages/  (routes point directly to TaxonomyLayout)
```

## Component Inventory

| Component | Section | Key Features |
|-----------|---------|-------------|
| HierarchyTree | Categories/Explorer | Recursive tree (`role="tree"`), keyboard nav, expand/collapse |
| TaxonomyBreadcrumbs | All | Ancestor path navigation |
| CategoryExplorer | Categories | Tree + detail panel + inline subcategory add/delete |
| TaxonomyDashboardWidgets | Overview | 8 stat cards + popular/trending/most-used/unused widgets |
| Tag/Topics/Skills/Competencies/Languages/Delivery panels | respective | CRUD + classification display |
| TopicGraphPlaceholder / KnowledgeMapPlaceholder | Relationships/Explorer | Future visualization scaffolds |
| RelationshipViewer | Relationships | Dependency + related topic viewer |

## Knowledge Hierarchy Design

```
Agriculture
└─ Mushroom Cultivation
   └─ Oyster Mushroom
      └─ Spawn Production
         └─ Advanced Spawn Techniques
            └─ Laboratory Operations
```

Implemented as recursive `TaxonomyNode` with `children: TaxonomyNode[]` — **unlimited depth**. Tree operations (add/update/remove/expand) handled via pure reducer helpers (`addNode`, `updateNode`, `removeNode`) operating recursively.

## State Management Summary

`useReducer` + Context (`useTaxonomyState.ts` / `TaxonomyContext.tsx`), 9 slices:
- **section** — active panel
- **tree** — full category hierarchy
- **expandedIds** — collapsed/expanded node ids
- **selectedCategoryId** — current selection
- **tags** — tag list with usage/trending/popular flags
- **topics** — topic library with dependencies/relationships
- **search** / **filterType** — search & filter state
- **selectedTopicId** — current topic selection

## Metadata Standards Report

| Dimension | Mock Source | Status |
|-----------|-------------|--------|
| Category | `TaxonomyNode` (icon, color, status, visibility, order, featured) | Active |
| Tag | `TaxonomyTag` (type, usage, trending, popular, suggested) | Active |
| Topic | `TopicNode` (group, priority, dependsOn, relatedTopics) | Active |
| Skill | `SkillLevel` (rank, description) | Active (9 levels) |
| Competency | `Competency` (category, level) | Active (9 areas) |
| Language | `LanguageOption` (code, nativeLabel, rtl) | Active (7) |
| Delivery | `DeliveryMode` (future flag) | Active (12, 2 future) |

## Search & Filter Validation

Search and filter reuse existing enterprise inputs (`Input`, `Select`). Tag panel filters by type via `state.filterType` → `setFilter`. All filtering is client-side mock. No duplicate search/filter implementation.

## Responsive Validation Report

Layout uses CSS grid (`320px sidebar + 1fr`) collapsing gracefully. Tree indents by `depth * 20px`. Explorer grid `320px 1fr` adapts; panels use `Grid columns={2|3}` which wraps on tablet/mobile. No horizontal overflow.

## Accessibility Report

- `HierarchyTree` uses `role="tree"` / `role="treeitem"` / `role="group"`, `aria-expanded`, `aria-selected`, keyboard Enter/Space navigation.
- Sidebar uses `role="tab"` / `aria-selected`.
- Breadcrumbs use `nav aria-label="Breadcrumb"`.
- All icon buttons have `aria-label`.
- Semantic HTML, focus management, reduced-motion compatible (no animations defined inline).

## Performance Optimization Report

- Lazy-loaded via `React.lazy()` route splitting (`TrainingTaxonomyPage`).
- Context value memoized with `useMemo` to avoid re-renders.
- Recursive tree renders only expanded nodes (`expandedIds`).
- Pure reducer helpers avoid mutation; memoization-ready component structure.
- Dashboard widgets compute aggregates via `useMemo`-friendly pure functions (no side effects).

## Future AI & Learning Path Readiness Report

Prepared interfaces:
- **Curriculum Builder** — taxonomy drives module/lesson categorization
- **Learning Paths** — `LearningPathsPanel` defines Path → Course → Module → Lesson → Assessment → Certificate
- **AI Recommendations** — Discovery metadata (popular/trending/suggested) ready for ML
- **Search Ranking** — category/tag/topic counts feed ranking signals
- **Certificates/Assessments** — competency mapping prepared
- **Inventory-linked Training Kits**, **Business Intelligence**, **Future ERP** — taxonomy as single source of truth

## Routing

```
/admin/training/taxonomy              → TaxonomyLayout
/admin/training/taxonomy/:section     → TaxonomyLayout (route scaffolding)
```

## Engineering Standards

- SOLID / DRY / KISS, feature-first, atomic composition
- Design system reused: Card, Badge, Button, Input, Select, Stack, Inline, Grid
- Strict typing — full interfaces for all nodes
- Zero duplicate components, zero hardcoded data outside mock layer
- Zero regressions — only `App.tsx` modified among existing files; `tsc --noEmit` passes with zero errors
