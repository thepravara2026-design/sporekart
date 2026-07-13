# Information Architecture — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Architecture Documentation. Reuses the Product Vision, Design
> Philosophy, Experience Principles, Personas, and Journeys from Part 1A.
> This document defines the organization of content into logical workspaces and
> the principles that govern how users move through the product.

---

## 1. IA Guiding Principles

Derived directly from the 10 Experience Principles (Part 1A):

1. **Predictable** — every workspace follows the same structural grammar.
2. **Minimal** — one primary action per view; chrome stays quiet.
3. **Context-aware** — the sidebar, breadcrumbs, and utility panel reflect the
   active workspace and the user's role.
4. **Search-friendly** — global search and a command palette are first-class,
   not buried.
5. **Keyboard accessible** — every navigation target is reachable by keyboard.
6. **Responsive** — one mental model from mobile browser to desktop.
7. **Role-aware** — users see only the navigation relevant to their
   responsibilities.
8. **Future-proof** — new features slot into an existing workspace or a new
   top-level workspace without restructuring the whole tree.

**Nesting rule:** Maximum depth of **3 levels** (Workspace → Section → Detail).
Deep nesting is explicitly avoided to minimize clicks (Principle 1, Simplicity).

---

## 2. Top-Level Workspaces

The product is organized into **12 workspaces**. Each is a bounded context with a
single purpose, a clear entry point, and a defined owner.

| # | Workspace | Route Root | Primary Users | Purpose (one line) |
|---|-----------|-----------|---------------|--------------------|
| 1 | Public | `/` | Guest, Customer, Farmer | Discover the platform, browse catalog, start a purchase. |
| 2 | Customer | `/account` | Customer, Farmer, Grower | Personal account: orders, profile, addresses, saved items. |
| 3 | Orders | `/orders` | Distributor, Support, Grower | Operational order processing, fulfillment, status. |
| 4 | Products | `/catalog` | Grower, Distributor, Admin | Catalog and inventory management. |
| 5 | Training | `/training` | Trainer, Customer, Farmer | Courses, sessions, registration, engagement. |
| 6 | AI Workspace | `/ai` | All roles (contextual) | AI assistant, prompts, knowledge, RAG. |
| 7 | Governance | `/governance` | Governance Manager, Admin | Policies, approvals, compliance, access control. |
| 8 | Analytics | `/analytics` | Business Owner, Admin, Grower | Metrics, drill-downs, exports. |
| 9 | Administration | `/admin` | Administrator | Users, content, config, monitoring. |
| 10 | CMS | `/cms` | Administrator, Trainer | Page and media content management. |
| 11 | Support | `/support` | Support Executive, Customer | Tickets, knowledge base, escalation. |
| 12 | Settings | `/settings` | All authenticated roles | Profile, security, workspace, billing. |

---

## 3. Workspace Definition Template

Every workspace is specified with the same nine attributes so reviewers can
compare them consistently.

- **Purpose** — what job the workspace does.
- **Entry Point** — the canonical route a user lands on.
- **Primary Users** — which personas rely on it most.
- **Child Pages** — the sections/detail routes it contains.
- **Dependencies** — other workspaces or APIs it relies on.
- **Permissions** — minimum role required to enter.
- **Breadcrumb Strategy** — how the trail is built.
- **Future Expansion Strategy** — where new features will go.

### 3.1 Public (`/`)
- **Purpose:** Top-of-funnel discovery and starting a purchase without an account.
- **Entry Point:** `/`
- **Primary Users:** Guest, Customer, Farmer.
- **Child Pages:** `/` (Home), `/products`, `/products/:id`, `/cart`, `/checkout`, `/search`.
- **Dependencies:** Products catalog API, Cart service, Pricing.
- **Permissions:** Public (no auth).
- **Breadcrumb Strategy:** Starts at "Home"; product detail shows Home › Products › {name}.
- **Future Expansion:** Promotions, regional landing pages, vernacular home variants.

### 3.2 Customer (`/account`)
- **Purpose:** Self-service personal account hub.
- **Entry Point:** `/account`
- **Primary Users:** Customer, Farmer, Grower.
- **Child Pages:** `/account` (overview), `/account/orders`, `/account/profile`,
  `/account/addresses`, `/account/wishlist`.
- **Dependencies:** Orders, Profile/Identity, Address service.
- **Permissions:** Authenticated (any customer-type role).
- **Breadcrumb Strategy:** Account › {section}.
- **Future Expansion:** Subscriptions, loyalty, household/multi-user accounts.

### 3.3 Orders (`/orders`)
- **Purpose:** Operational order processing and fulfillment visibility.
- **Entry Point:** `/orders`
- **Primary Users:** Distributor, Support, Grower.
- **Child Pages:** `/orders` (list), `/orders/:id` (detail), `/orders/:id/fulfill`.
- **Dependencies:** Order service, Inventory, Logistics.
- **Permissions:** Distributor, Support, or Admin.
- **Breadcrumb Strategy:** Orders › {order reference}.
- **Future Expansion:** Bulk fulfillment, returns/RMA, dispute handling.

### 3.4 Products (`/catalog`)
- **Purpose:** Catalog and inventory management for sellers/operators.
- **Entry Point:** `/catalog`
- **Primary Users:** Grower, Distributor, Admin.
- **Child Pages:** `/catalog` (list), `/catalog/:id`, `/catalog/new`.
- **Dependencies:** Product service, Inventory, Pricing, Authenticity/Certification.
- **Permissions:** Grower, Distributor, or Admin.
- **Breadcrumb Strategy:** Products › {product name/id}.
- **Future Expansion:** Variant management, batch/lot traceability, bulk import.

### 3.5 Training (`/training`)
- **Purpose:** Agronomy education: courses, sessions, registration.
- **Entry Point:** `/training`
- **Primary Users:** Trainer, Customer, Farmer.
- **Child Pages:** `/training` (catalog), `/training/:id`, `/training/create`.
- **Dependencies:** Training service, User/Auth, Notifications.
- **Permissions:** Public catalog; create/manage requires Trainer or Admin.
- **Breadcrumb Strategy:** Training › {session title}.
- **Future Expansion:** Live sessions, certificates, attendance analytics.

### 3.6 AI Workspace (`/ai`)
- **Purpose:** Conversational AI, prompt management, knowledge/RAG.
- **Entry Point:** `/ai`
- **Primary Users:** All roles (contextual assistant + expert tools).
- **Child Pages:** `/ai` (assistant), `/ai/chat`, `/ai/prompts`, `/ai/knowledge`.
- **Dependencies:** AI Platform (Part 17), RAG, Search, Content.
- **Permissions:** Authenticated (assistant); prompts/knowledge require expert role.
- **Breadcrumb Strategy:** AI › {section}.
- **Future Expansion:** Workflows, provider comparison, evaluation dashboards.

### 3.7 Governance (`/governance`)
- **Purpose:** Policy, approval, compliance, and access control oversight.
- **Entry Point:** `/governance`
- **Primary Users:** Governance Manager, Admin.
- **Child Pages:** `/governance` (overview), `/governance/policies`,
  `/governance/approvals`, `/governance/compliance`, `/governance/access`.
- **Dependencies:** Governance services (automation, analytics, admin, compliance).
- **Permissions:** Governance Manager or Admin.
- **Breadcrumb Strategy:** Governance › {section}.
- **Future Expansion:** Audit streaming, policy simulation, risk scoring.

### 3.8 Analytics (`/analytics`)
- **Purpose:** Trusted, sourced metrics and drill-downs.
- **Entry Point:** `/analytics`
- **Primary Users:** Business Owner, Admin, Grower.
- **Child Pages:** `/analytics` (overview), `/analytics/sales`, `/analytics/operations`.
- **Dependencies:** Analytics/Reporting, Order, Catalog data.
- **Permissions:** Business Owner, Admin, Grower (scoped).
- **Breadcrumb Strategy:** Analytics › {section}.
- **Future Expansion:** Custom reports, scheduled exports, cohort analysis.

### 3.9 Administration (`/admin`)
- **Purpose:** Platform administration: users, content, config, monitoring.
- **Entry Point:** `/admin`
- **Primary Users:** Administrator.
- **Child Pages:** `/admin` (overview), `/admin/users`, `/admin/content`,
  `/admin/config`, `/admin/monitoring`.
- **Dependencies:** Identity, Config, Observability, all services.
- **Permissions:** Administrator.
- **Breadcrumb Strategy:** Admin › {section}.
- **Future Expansion:** Feature flags UI, tenancy, cost/usage.

### 3.10 CMS (`/cms`)
- **Purpose:** Marketing and educational content: pages and media.
- **Entry Point:** `/cms`
- **Primary Users:** Administrator, Trainer.
- **Child Pages:** `/cms` (overview), `/cms/pages`, `/cms/media`.
- **Dependencies:** Content service, Media storage.
- **Permissions:** Admin or Trainer (scoped).
- **Breadcrumb Strategy:** CMS › {section}.
- **Future Expansion:** Localization, scheduling, preview environments.

### 3.11 Support (`/support`)
- **Purpose:** Customer support: tickets and knowledge base.
- **Entry Point:** `/support`
- **Primary Users:** Support Executive, Customer.
- **Child Pages:** `/support` (overview), `/support/tickets`, `/support/kb`.
- **Dependencies:** Ticketing, User context, Order context, KB.
- **Permissions:** Support (agent view); Customer sees own tickets only.
- **Breadcrumb Strategy:** Support › {section}.
- **Future Expansion:** SLA dashboards, macro library, CSAT analytics.

### 3.12 Settings (`/settings`)
- **Purpose:** Account and workspace configuration.
- **Entry Point:** `/settings`
- **Primary Users:** All authenticated roles.
- **Child Pages:** `/settings` (overview), `/settings/profile`, `/settings/security`,
  `/settings/workspace`, `/settings/billing`.
- **Dependencies:** Identity, Billing, Workspace/Org.
- **Permissions:** Authenticated.
- **Breadcrumb Strategy:** Settings › {section}.
- **Future Expansion:** API keys, integrations, notification preferences.

---

## 4. Cross-Workspace Rules

- **Single active workspace at a time.** The sidebar highlights exactly one
  workspace; breadcrumbs and the utility panel scope to it.
- **Workspace switching is explicit** via the sidebar or the command palette; it
  never happens implicitly.
- **Shared concepts live once.** e.g., "Order" detail is owned by Orders; the
  Customer account links *into* it rather than duplicating it.
- **AI is ambient, not a silo.** The AI assistant is reachable from any workspace
  (launcher + Cmd/Ctrl+K) but also has a dedicated `/ai` workspace for expert tools.

---

## 5. Future-Proofing

New features are absorbed by:
1. Adding a child page to the most relevant existing workspace, or
2. Promoting a high-growth child page to a new workspace, or
3. Adding a brand-new workspace at the top level (rare, governed).

The navigation config (`frontend/web-app/src/config/navigation.ts`) is the single
source of truth, so adding a page is a data change, not a structural rewrite.
