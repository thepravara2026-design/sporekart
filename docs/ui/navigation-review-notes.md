# Navigation Review Notes — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Reviewer guide + open questions. This sprint **stops for review**.
> Use this document to record feedback. Items here feed Sprint 19 Part 1C.

---

## 1. What to Review
1. **Sitemap** (`application-sitemap.md`) — are the 12 workspaces and child pages
   the right decomposition? Anything missing or misplaced?
2. **Route hierarchy** (`route-architecture.md`) — do access rules and breadcrumb
   trails match expectations?
3. **Role matrix** (`role-based-navigation.md`) — does each role see the right
   workspaces? Any over/under-exposure?
4. **Layout shell** (live prototype) — header, sidebar, breadcrumb, content,
   utility panel, command palette, footer. Proportions and regions.
5. **Responsive behavior** — resize the prototype from desktop to mobile.
6. **Accessibility** — Tab from the skip link; verify landmarks and focus order.

---

## 2. Live Preview Routes
Run from `frontend/web-app`:
```
npm install
npm run dev
```
Then open the printed local URL. Try, for review:
- `/` (Public shell)
- `/products`, `/products/1` (detail breadcrumb)
- `/orders` then switch role to Distributor
- `/governance` (role: Governance Manager)
- `/admin` (role: Administrator)
- Press `Cmd/Ctrl+K` to open the command palette
- Use the header **Role switcher** to validate the visibility matrix

---

## 3. Open Questions for Part 1C
1. Should **Customer** and **Public** remain separate workspaces, or should
   Public fold into a marketing surface and Customer become the post-auth home?
2. Is the **AI Workspace** correctly scoped, or should expert tools (prompts/
   knowledge) live under Admin/Governance instead of `/ai`?
3. Do we need a dedicated **Distributor** workspace, or is Orders+Products enough?
4. Should **Settings** be global or per-workspace (workspace settings inside each
   workspace)? Current design: global Settings + workspace config where relevant.
5. Footer: full legal footer on Public only, or also on authenticated workspaces?
6. Confirm max-depth-3 rule vs. any anticipated deep workflow (e.g., Order ›
   Fulfillment › Batch).

---

## 4. Known Limitations (this sprint)
- No real authentication; role switcher is review-only.
- Command palette lists static commands; no search index.
- All content panels are empty placeholders (by design).
- No design tokens, theming, or component library yet (later sprints).
- No business data, forms, tables, or charts.

---

## 5. Review Gate
This blueprint must pass the **UX review gate** (Part 1A `review-process.md`)
before Part 1C proceeds. Record sign-off here:

| Reviewer | Role | Decision | Date |
|----------|------|----------|------|
| __________ | __________ | ☐ Approve ☐ Changes | ______ |
