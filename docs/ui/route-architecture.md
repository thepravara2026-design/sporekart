# Route Architecture — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Complete route hierarchy with purpose, access
> rules, navigation location, breadcrumb, page owner, and future mobile mapping.
> This is the authoritative route list; the prototype router
> (`frontend/web-app/src/App.tsx`) and config (`navigation.ts`) implement it.

---

## Route Table

Access: `public` = no auth · `auth` = signed in · `role:<name>` = min role.

| Route | Purpose | Access | Nav Location | Breadcrumb | Page Owner | Mobile Mapping |
|-------|---------|--------|--------------|-----------|------------|----------------|
| `/` | Home / landing | public | Sidebar: Public | Home | Web/Marketing | Home tab |
| `/search` | Search results | public | Header search | Home › Search | Search | Search screen |
| `/products` | Catalog | public | Sidebar: Public | Home › Products | Catalog | Catalog tab |
| `/products/:id` | Product detail | public | List → detail | Home › Products › {name} | Catalog | Product detail push |
| `/cart` | Cart | public | Header/Cart | Home › Cart | Checkout | Cart screen |
| `/checkout` | Checkout | public→auth | Cart → checkout | Home › Checkout | Checkout | Checkout flow |
| `/account` | Account overview | auth | Sidebar: Customer | Account | Identity | Account tab |
| `/account/orders` | My orders | auth | Customer children | Account › Orders | Identity | Account sub |
| `/account/profile` | My profile | auth | Customer children | Account › Profile | Identity | Account sub |
| `/account/addresses` | Addresses | auth | Customer children | Account › Addresses | Identity | Account sub |
| `/account/wishlist` | Saved items | auth | Customer children | Account › Wishlist | Identity | Account sub |
| `/orders` | Order queue | role:distributor | Sidebar: Orders | Orders | Fulfillment | Orders tab |
| `/orders/:id` | Order detail | role:distributor | List → detail | Orders › {ref} | Fulfillment | Order detail push |
| `/orders/:id/fulfill` | Fulfill | role:distributor | Detail action | Orders › {ref} › Fulfill | Fulfillment | Sheet/modal |
| `/catalog` | Catalog mgmt | role:grower | Sidebar: Products | Products | Catalog | Catalog tab |
| `/catalog/:id` | SKU detail | role:grower | List → detail | Products › {name} | Catalog | Detail push |
| `/catalog/new` | New product | role:grower | Action button | Products › New | Catalog | Create sheet |
| `/training` | Training catalog | public | Sidebar: Training | Training | Training | Training tab |
| `/training/:id` | Session detail | public | List → detail | Training › {title} | Training | Detail push |
| `/training/create` | Create session | role:trainer | Action button | Training › New | Training | Create sheet |
| `/ai` | Assistant home | auth | Sidebar: AI | AI | AI Platform | AI tab |
| `/ai/chat` | Conversations | auth | AI children | AI › Chat | AI Platform | AI sub |
| `/ai/prompts` | Prompt library | role:expert | AI children | AI › Prompts | AI Platform | AI sub |
| `/ai/knowledge` | Knowledge/RAG | role:expert | AI children | AI › Knowledge | AI Platform | AI sub |
| `/governance` | Gov overview | role:governance | Sidebar: Governance | Governance | Governance | Governance tab |
| `/governance/policies` | Policies | role:governance | Gov children | Governance › Policies | Governance | Gov sub |
| `/governance/approvals` | Approvals | role:governance | Gov children | Governance › Approvals | Governance | Gov sub |
| `/governance/compliance` | Compliance | role:governance | Gov children | Governance › Compliance | Governance | Gov sub |
| `/governance/access` | Access control | role:governance | Gov children | Governance › Access | Governance | Gov sub |
| `/analytics` | Analytics overview | role:business | Sidebar: Analytics | Analytics | Analytics | Analytics tab |
| `/analytics/sales` | Sales | role:business | Analytics children | Analytics › Sales | Analytics | Analytics sub |
| `/analytics/operations` | Operations | role:business | Analytics children | Analytics › Operations | Analytics | Analytics sub |
| `/admin` | Admin overview | role:admin | Sidebar: Admin | Admin | Platform | Admin tab |
| `/admin/users` | Users | role:admin | Admin children | Admin › Users | Platform | Admin sub |
| `/admin/content` | Content mod | role:admin | Admin children | Admin › Content | Platform | Admin sub |
| `/admin/config` | Config | role:admin | Admin children | Admin › Config | Platform | Admin sub |
| `/admin/monitoring` | Monitoring | role:admin | Admin children | Admin › Monitoring | Platform | Admin sub |
| `/cms` | CMS overview | role:admin | Sidebar: CMS | CMS | Content | CMS tab |
| `/cms/pages` | Pages | role:admin | CMS children | CMS › Pages | Content | CMS sub |
| `/cms/media` | Media | role:admin | CMS children | CMS › Media | Content | CMS sub |
| `/support` | Support home | auth | Sidebar: Support | Support | Support | Support tab |
| `/support/tickets` | Tickets | auth | Support children | Support › Tickets | Support | Support sub |
| `/support/kb` | Knowledge base | public | Support children | Support › Knowledge Base | Support | Support sub |
| `/settings` | Settings overview | auth | Sidebar: Settings | Settings | Identity | Settings tab |
| `/settings/profile` | Profile/prefs | auth | Settings children | Settings › Profile | Identity | Settings sub |
| `/settings/security` | Security | auth | Settings children | Settings › Security | Identity | Settings sub |
| `/settings/workspace` | Workspace/org | auth | Settings children | Settings › Workspace | Identity | Settings sub |
| `/settings/billing` | Billing | auth | Settings children | Settings › Billing | Identity | Settings sub |

---

## Routing Rules
- **Catch-all:** unknown routes → `/` with a "not found" empty panel (no dead ends).
- **Auth guard:** `auth`/`role` routes redirect guests to `/` (or a future sign-in).
  The prototype renders an "Access restricted — role required" empty panel for
  review instead of a hard redirect, so reviewers can see the gate.
- **Detail routes** are never permanent sidebar items; reached via list selection.
- **One primary action** per route, surfaced in the content header action slot.
- **Breadcrumb root** = workspace; the workspace root route shows only the
  workspace name (no self-link).
