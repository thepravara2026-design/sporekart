# Publishing Workflow

## Purpose
A draft-and-publish workflow that mirrors Shopify Plus / Adobe Commerce / Salesforce Commerce Cloud conventions, in Mock Mode.

## Actions (all mock, all audited)
Orchestrated by `PublishingPanel.tsx` and `useProductEditState`:

| Action | Behaviour | Activity |
|--------|-----------|----------|
| **Save Draft** | `sessionStorage` snapshot (key `sporekart:product-edit:SK-PROD-1001`) | `draft_saved` |
| **Submit for Review** | → `under_review` (or `published` if `settings.requireApproval` is false) | `review_requested` / `published` |
| **Approve** | → `approved` | `approved` |
| **Reject** | → `draft` | `rejected` |
| **Schedule Publish** | → `scheduled` (optional date, placeholder) | `scheduled` |
| **Publish** | → `published` | `published` |
| **Unpublish** | → `inactive` | `edited` |
| **Archive** | → `archived` (confirmed) | `archived` |
| **Restore** | → `draft` (confirmed) | `restored` |
| **Delete** | → `deleted` (confirmed) | `deleted` |

## Settings (mock)
`EditingSettings { requireApproval, autoPublish, lockOnReview, notifyOnPublish }` from `mockEditData.ts` drive conditional flows (e.g. auto-publish when approval not required).

## Permission Gating
`PublishingPanel` receives `canPublish`, `canApprove`, `canArchive`, `canRestore`, `canDelete` derived from `canProduct(CURRENT_ROLE, action)`. Controls are disabled when the role lacks the grant. Destructive actions always open a `Dialog` confirmation (`archive` / `restore` / `delete`).

## Disable Rules
- Submit disabled while `under_review` or `deleted`.
- Approve/Reject enabled only in `under_review`.
- Publish disabled when already `published`/`active`.
- Unpublish only from `published`/`active`.
- Archive disabled from `archived`/`deleted`; Delete disabled from `deleted`.
- Restore only from `archived`/`deleted`.

## Routes
- In-workspace: `publishing` section (Lifecycle + Publishing panels).
- Standalone overview: `/preview/products/lifecycle`.

## Future Backend Integration
Each `applyTransition` maps to a future transitions API. `saveDraft`'s `sessionStorage` becomes a server draft; `schedulePublish`'s date becomes a real scheduled-job payload. The permission gating already matches a server-authoritative RBAC model.
