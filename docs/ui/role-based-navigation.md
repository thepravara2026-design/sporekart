# Role-Based Navigation — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Maps each role to the workspaces and routes it can
> see. Reuses the 8 personas from Part 1A. Navigation is filtered on load by the
> user's effective role(s); a user may hold multiple roles (e.g., Business Owner
> who is also an Admin).

---

## 1. Roles
Guest, Customer, Grower, Trainer, Support, Administrator, Business Owner,
Governance Manager.

(Note: "Farmer" from Part 1A personas maps to the **Customer** workspace with
field-context; the navigation taxonomy uses role *capabilities*, so Farmer is
treated as Customer for navigation visibility. "Distributor" maps to the
**Orders/Products** operational capability.)

## 2. Visibility Matrix

Legend: ● full · ◐ scoped (subset/own-only) · — hidden

| Workspace / Route Root | Guest | Customer | Grower | Trainer | Support | Admin | Biz Owner | Gov Mgr |
|------------------------|:-----:|:------:|:-----:|:------:|:------:|:----:|:--------:|:------:|
| Public `/` | ● | ● | ● | ● | ● | ● | ● | ● |
| Customer `/account` | — | ● | ● | — | — | — | ● | — |
| Orders `/orders` | — | — | ◐ | — | ● | ● | ◐ | — |
| Products `/catalog` | — | — | ● | — | — | ● | ◐ | — |
| Training `/training` | ● | ● | — | ● | — | ● | — | — |
| AI `/ai` (+chat) | — | ◐ | ◐ | ◐ | ◐ | ● | ◐ | ◐ |
| AI `/ai/prompts`,`/knowledge` | — | — | — | ◐ | — | ● | — | — |
| Governance `/governance` | — | — | — | — | — | ● | — | ● |
| Analytics `/analytics` | — | — | ◐ | — | — | ● | ● | — |
| Administration `/admin` | — | — | — | — | — | ● | — | — |
| CMS `/cms` | — | — | — | ◐ | — | ● | — | — |
| Support `/support` | — | ◐ | — | — | ● | ● | — | — |
| Settings `/settings` | — | ● | ● | ● | ● | ● | ● | ● |

### Scoped (◐) explanations
- **Orders (Grower/Biz Owner):** see only their own organization's orders.
- **Products (Biz Owner):** read-only view of relevant catalog.
- **AI:** assistant + chat available to all auth roles; expert tools gated.
- **Analytics (Grower):** scoped to their operation's metrics.
- **Support (Customer):** sees only their own tickets, not the agent queue.
- **Settings:** every authenticated role sees their own profile/security;
  workspace/billing scoped to owners/admins.

---

## 3. How Filtering Works (prototype)
- The navigation config tags every node with `roles: string[]` (or `public`).
- On load, the active role is read (header role switcher in the prototype for
  review; real auth in later sprints).
- Sidebar and command palette render only nodes whose `roles` include the active
  role (or are `public`).
- Deep links to a restricted route render an "Access restricted" empty panel
  rather than a hard error, so reviewers can inspect the gate.

---

## 4. Multi-Role Handling
When a user holds multiple roles, the **union** of visible workspaces is shown.
The header shows the active role; switching role re-filters (prototype behavior).
A future "role context" may scope data without hiding navigation.

---

## 5. Guest Experience
Guests see only **Public**. The sidebar collapses to a minimal set: Home,
Products, Search, Training, Support/KB, plus Sign in. All else is revealed after
authentication.

---

## 6. Review Aid
The prototype header includes a **Role switcher** (review-only) with all 8 roles
so reviewers can verify the visibility matrix live without a backend.
