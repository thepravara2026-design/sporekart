# Breadcrumb Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Defines breadcrumb semantics and the per-route
> breadcrumb construction. Reuses the Consistency (2), Clarity (3), and
> Accessibility (5) experience principles.

---

## 1. Purpose
Breadcrumbs answer two questions at once: **Where am I?** and **How do I go back
up one level?** They are an orientation aid, not a full history trail.

## 2. Structure
`Workspace › Section › Detail`

- **Workspace** — the active top-level workspace (always present, always a link
  except on the workspace root).
- **Section** — the child page within the workspace (a link).
- **Detail** — the record/`:id` view (current page, **not** a link, `aria-current="page"`).

## 3. Semantic Markup
```html
<nav aria-label="Breadcrumb">
  <ol>
    <li><a href="/orders">Orders</a></li>
    <li><a href="/orders/ORD-123">ORD-123</a></li>
    <li aria-current="page">Fulfill</li>
  </ol>
</nav>
```
- Use an ordered list (`<ol>`) for proper screen-reader enumeration.
- The last item carries `aria-current="page"`.
- A visual separator (`/`) is decorative and `aria-hidden`.

## 4. Rules
1. **Always present** except on the workspace root, where it collapses to a single
   non-link workspace label (no self-referential link).
2. **Never truncate** below 2 levels on desktop; on mobile, show
   `… › Current` with the root reachable via the sidebar.
3. **Record names** in detail crumbs use the human label when available
   (e.g., product name), falling back to a stable reference (e.g., `ORD-123`).
4. **Cross-workspace context links** do NOT change the breadcrumb. Breadcrumbs
   reflect the *current* workspace hierarchy, not the link origin. (The user can
   always return via browser back or the originating workspace in the sidebar.)
5. **Home icon** in public contexts links to `/`; inside authenticated workspaces
   the trail starts at the workspace, not Home.
6. **One click to go up** any single level.

## 5. Per-Workspace Breadcrumb Strategy

| Workspace | Root crumb | Detail crumb example |
|-----------|-----------|----------------------|
| Public | Home | Home › Products › {name} |
| Customer | Account | Account › Orders |
| Orders | Orders | Orders › {ref} › Fulfill |
| Products | Products | Products › {name} |
| Training | Training | Training › {title} |
| AI | AI | AI › Prompts |
| Governance | Governance | Governance › Policies |
| Analytics | Analytics | Analytics › Sales |
| Admin | Admin | Admin › Users |
| CMS | CMS | CMS › Pages |
| Support | Support | Support › Tickets |
| Settings | Settings | Settings › Security |

## 6. Empty / Restricted States
- **Restricted route:** breadcrumb shows the workspace + "Access restricted" as
  the current page (no fake section).
- **Not found:** breadcrumb shows the workspace + "Not found".
