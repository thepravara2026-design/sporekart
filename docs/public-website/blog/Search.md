# Search Experience (`/search`)

Client-side, placeholder-ready search over the blog data set, designed for a
future CMS/backend integration.

## Behaviour
- Reads `?q=` from the URL via `useSearchParams`; the hero `SearchBar` updates the
  param on submit (and clears it when empty).
- `searchArticles(query)` does a case-insensitive substring match across title,
  excerpt, category, tags, and body text.
- **No query** → shows "Popular searches" + "Browse by tag" (`TagCloud`, limit 12)
  and a note that recent searches / live suggestions are placeholder-ready.
- **Results** → count line + responsive card grid.
- **No results** → `EmptyState` with suggested popular searches.

## Components
- `SearchBar` — `role="search"` form, labelled input, accent submit button.
- `EmptyState` — icon, title, message, suggestion chips.

## SEO
- `noindex: true` (search pages must not be indexed).
- Canonical includes the `?q=` when present.

## Accessibility
- Labelled search input; form has `role="search"`.
- Result count announced in a paragraph.
- Empty state offers keyboard-navigable suggestion links.

## Future integration
- Replace `searchArticles` with an API call; add debounced autocomplete, recent
  searches (localStorage), and server-side pagination.
