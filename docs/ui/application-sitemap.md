# Application Sitemap — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Complete application sitemap. Routes marked
> `(public)` require no authentication; `(auth)` requires sign-in; `(role)`
> lists the minimum role. Detail routes use `:param` placeholders. This sitemap
> is the human-readable mirror of `frontend/web-app/src/config/navigation.ts`.

---

## Public Workspace (`/`) — Guest, Customer, Farmer
```
/                         Home / Landing            (public)
/search                   Search results            (public)
/products                 Product catalog           (public)
/products/:id             Product detail            (public)
/cart                     Shopping cart             (public)
/checkout                 Checkout                  (public, becomes auth at pay)
```

## Customer Workspace (`/account`) — Customer, Farmer, Grower  (auth)
```
/account                  Account overview          (auth)
/account/orders           My orders                 (auth)
/account/profile          My profile                (auth)
/account/addresses        Saved addresses           (auth)
/account/wishlist         Saved items               (auth)
```

## Orders Workspace (`/orders`) — Distributor, Support, Admin  (role)
```
/orders                   Order list / queue        (role)
/orders/:id               Order detail              (role)
/orders/:id/fulfill       Fulfillment action        (role)
```

## Products Workspace (`/catalog`) — Grower, Distributor, Admin  (role)
```
/catalog                  Catalog list              (role)
/catalog/:id              Product / SKU detail      (role)
/catalog/new              Create product            (role)
```

## Training Workspace (`/training`) — Trainer, Customer, Farmer  (mixed)
```
/training                 Training catalog          (public)
/training/:id             Session detail            (public)
/training/create          Create session            (Trainer, Admin)
```

## AI Workspace (`/ai`) — All (contextual)  (mixed)
```
/ai                       Assistant home            (auth)
/ai/chat                  Conversations             (auth)
/ai/prompts               Prompt library            (expert role)
/ai/knowledge             Knowledge / RAG sources   (expert role)
```

## Governance Workspace (`/governance`) — Governance Manager, Admin  (role)
```
/governance               Governance overview       (role)
/governance/policies      Policy registry           (role)
/governance/approvals     Approval queue            (role)
/governance/compliance    Compliance status         (role)
/governance/access        Access control            (role)
```

## Analytics Workspace (`/analytics`) — Business Owner, Admin, Grower  (role)
```
/analytics                Analytics overview        (role)
/analytics/sales          Sales metrics             (role)
/analytics/operations     Operations metrics        (role)
```

## Administration Workspace (`/admin`) — Administrator  (role)
```
/admin                    Admin overview            (role)
/admin/users              User management           (role)
/admin/content            Content moderation        (role)
/admin/config             Platform config           (role)
/admin/monitoring         Monitoring / health       (role)
```

## CMS Workspace (`/cms`) — Admin, Trainer  (role)
```
/cms                      CMS overview              (role)
/cms/pages                Page management           (role)
/cms/media                Media library             (role)
```

## Support Workspace (`/support`) — Support Executive, Customer  (mixed)
```
/support                  Support home              (auth)
/support/tickets          Ticket queue / my tickets (auth)
/support/kb               Knowledge base            (public)
```

## Settings Workspace (`/settings`) — All authenticated  (auth)
```
/settings                 Settings overview         (auth)
/settings/profile         Profile & preferences     (auth)
/settings/security        Security & sessions       (auth)
/settings/workspace       Workspace / org           (auth)
/settings/billing         Billing & plans           (auth)
```

---

## Sitemap Summary Counts
- **12** workspaces
- **~50** routes (including detail/action routes)
- **Max depth:** 3 levels (Workspace › Section › Detail)
- **Public entry points:** `/`, `/search`, `/products`, `/products/:id`, `/cart`,
  `/checkout`, `/training`, `/training/:id`, `/support/kb`
- **Auth entry points:** all `/account*`, `/ai*`, `/settings*`, `/support`
- **Role-gated entry points:** `/orders*`, `/catalog*`, `/governance*`,
  `/analytics*`, `/admin*`, `/cms*`, `/training/create`, `/ai/prompts`,
  `/ai/knowledge`

## Future Mobile Mapping
Every web route maps 1:1 to a native destination on Android/iOS:
- Web workspace root → native tab or top-level stack.
- `:id` detail routes → native detail screen pushed on the stack.
- Breadcrumbs → native back-stack + section header.
- Command palette → native spotlight/search controller.
The IA is platform-agnostic; only the navigation *presentation* differs per OS.
