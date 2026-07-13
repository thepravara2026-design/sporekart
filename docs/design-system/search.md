# Search Implementation

## Overview

The Design System provides client-side search across components, tokens, and documentation. The search engine is implemented in `frontend/web-app/src/design-system/playground/catalog/searchIndex.ts` and operates entirely in the browser — no server calls required.

## Architecture

```
                 ┌─────────────────────┐
                 │   SearchEngine       │
                 │   (singleton)        │
                 └──────┬──────────────┘
                        │
          ┌─────────────┼─────────────┐
          ▼             ▼             ▼
   ┌──────────┐  ┌──────────┐  ┌──────────┐
   │Component │  │  Token   │  │  Doc     │
   │ Index    │  │  Index   │  │  Index   │
   └──────────┘  └──────────┘  └──────────┘
```

## Index Structure

The search index is an array of `SearchResult` objects built at initialization from the component manifest, token manifest, and static documentation entries.

```typescript
interface SearchResult {
  id: string;
  type: 'component' | 'token' | 'doc';
  title: string;
  description: string;
  category: string;
  url: string;
  keywords: string[];
}
```

### Component Index Entries

Generated from `componentManifest.ts`. Each component produces one entry with its name, description, category, and known keywords from variants and states.

### Token Index Entries

Generated from `tokenManifest.ts`. Each token produces one entry with its name, value, and category. Token names are keyword-enriched with common user queries (e.g., "green", "shadow", "font").

### Doc Index Entries

Statically defined in the search index file. Each key design system document is manually indexed with title, description, relevant keywords, and URL.

## Query Syntax

### Simple Search

```
button           → matches name, description, keywords
primary button   → matches both terms (AND logic)
```

### Scoring Algorithm

The search engine ranks results by:

1. **Exact match (100 points)** — query exactly equals title
2. **Prefix match (80 points)** — query matches start of a term
3. **Word boundary match (60 points)** — query matches at word boundary (camelCase or space)
4. **Fuzzy match (40 points)** — query has Damerau-Levenshtein distance ≤ 2
5. **Keyword match (30 points)** — query matches a keyword
6. **Description match (20 points)** — query matches description
7. **Category boost (+10 points)** — query matches the category name

Scores are summed across all matched terms. Results are returned in descending score order, capped at 20 results.

### Fuzzy Matching

Fuzzy matching uses a simplified Damerau-Levenshtein distance algorithm. A query term matches an indexed term if the edit distance is 1 and the indexed term is at least 4 characters, or distance 2 and the indexed term is at least 6 characters.

### Auto-Completion

`getSuggestions(query)` returns the top 5 suggestions based on prefix matching against component and token names. Suggestions are returned as strings.

## Searchable Fields

| Type | Searchable Fields | Priority |
|------|------------------|----------|
| Component | `name`, `description`, `category`, `variants[]`, `states[]`, `keywords[]` | High |
| Token | `name`, `category`, `value`, `keywords[]` | Medium |
| Doc | `title`, `description`, `keywords[]` | High |

## Search UI

The search is exposed via the `SearchOverlay` component:

- **Trigger**: `Cmd/Ctrl + K` keyboard shortcut
- **Behavior**: Opens a modal overlay with search input and results
- **Results**: Grouped by type (components, tokens, docs)
- **Navigation**: Arrow keys to navigate, Enter to open
- **Close**: Escape, click outside, or blur

## How to Extend the Search Index

### Adding a New Component

When you register a new component in `componentManifest.ts`, update the `SearchEngine` initialization in `searchIndex.ts`:

```typescript
// In buildIndex() — components are auto-indexed from the manifest
// No manual step needed if the manifest builder is used.
```

The search index is rebuilt whenever the manifest is updated and the application recompiles.

### Adding a New Doc Entry

Open `searchIndex.ts` and add a new entry to the `docEntries` array:

```typescript
{
  id: 'component-review-process',
  type: 'doc',
  title: 'Component Review Process',
  description: 'The 8-stage pipeline for reviewing and approving components',
  category: 'process',
  url: '/design-system/docs/component-review-process',
  keywords: ['review', 'pipeline', 'approval', 'gate', 'stages', 'freeze'],
}
```

### Adding Keywords

Add relevant keywords to the `keywords` array of the existing entry. Keywords are weighted equally with the name/description but cannot override exact or prefix matches.

## Performance

- Index size: ~50–100KB for the full design system
- Build time: < 10ms
- Query time: < 2ms for typical queries
- Memory: ~200KB for the index object
- Debounced input: 150ms delay before searching
- Result cap: 20 results maximum
