# Empty State Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory pattern. Every empty state follows **Explain → Guide → Act**. Reuses Principle 8 (Discoverability), Principle 9 (Trust).

---

## 1. The Formula

Every empty state = **Illustration + Headline + Body + Primary CTA (+ Secondary)**

```
┌──────────────────────────────────────────────┐
│  [Illustration: meaningful, not decorative]  │
│                                              │
│  Headline: What is empty (1 sentence)        │
│                                              │
│  Body: Why empty + what user can do (1-2)    │
│                                              │
│  [ Primary Action Button ]  [ Secondary ]    │
└──────────────────────────────────────────────┘
```

---

## 2. Standard Empty States by Workspace

| Workspace | Context | Headline | Body | Primary CTA | Secondary |
|-----------|---------|----------|------|-------------|-----------|
| **Public / Products** | No results | No products found | Try adjusting your filters or search terms. | Clear filters | Browse all products |
| **Public / Products** | Empty catalog (admin) | No products yet | Add your first product to start selling. | Add product | Import catalog |
| **Customer / Orders** | No orders | No orders yet | When you place an order, it will appear here. | Shop products | View wishlist |
| **Customer / Wishlist** | Empty | Your wishlist is empty | Save products you like to find them later. | Browse products | — |
| **Customer / Addresses** | No addresses | No saved addresses | Add an address to speed up checkout. | Add address | — |
| **Orders / Queue** | No orders assigned | No orders to process | Orders assigned to you will appear here. | Refresh | — |
| **Orders / Detail** | Not found | Order not found | This order may have been removed or you don't have access. | Go to orders | Contact support |
| **Products / Catalog** | No products | No products in catalog | Create your first product or import a catalog. | New product | Import CSV |
| **Products / Detail** | Not found | Product not found | This product may have been archived. | View catalog | — |
| **Training / Catalog** | No sessions | No training sessions | Published sessions will appear here. | Create session | Browse catalog |
| **Training / My Trainings** | Not enrolled | You're not enrolled yet | Find a session and register to get started. | Browse training | — |
| **AI / Assistant** | No chats | No conversations yet | Ask a question to start your first chat. | Ask AI | View prompt library |
| **AI / Prompts** | No prompts | No prompts saved | Save your favorite prompts for reuse. | Create prompt | Browse library |
| **AI / Knowledge** | No sources | No knowledge sources | Add documents to enable RAG answers. | Upload document | — |
| **Governance / Policies** | No policies | No policies defined | Create policies to govern platform behavior. | New policy | Import template |
| **Governance / Approvals** | No pending | No pending approvals | Items requiring your review will appear here. | Refresh | View all |
| **Governance / Compliance** | All good | All compliant | No compliance issues detected. | Run scan | View history |
| **Governance / Access** | No grants | No access grants | Grant access to users or groups. | Grant access | View audit log |
| **Analytics / Overview** | No data | No data available | Data appears after your first transaction. | View documentation | — |
| **Analytics / Sales** | No sales | No sales yet | Sales metrics appear after orders are placed. | View orders | — |
| **Analytics / Operations** | No ops data | No operations data | Operational metrics require activity. | View orders | — |
| **Admin / Users** | No users | No users found | Invite team members to get started. | Invite user | Import users |
| **Admin / Content** | No content | No content items | Create pages or upload media. | New page | Upload media |
| **Admin / Config** | Defaults | Default settings active | No custom configuration. | Edit settings | View defaults |
| **Admin / Monitoring** | Healthy | All systems healthy | No active alerts or incidents. | View logs | — |
| **CMS / Pages** | No pages | No pages created | Build your first page. | New page | — |
| **CMS / Media** | No media | No media uploaded | Upload images, PDFs, videos. | Upload | — |
| **Support / Tickets** | No tickets | No tickets | Create a ticket if you need help. | New ticket | Browse KB |
| **Support / KB** | No articles | No help articles | Articles will appear as they're published. | — | Contact support |
| **Settings / Profile** | Incomplete | Profile incomplete | Add your details to personalize your experience. | Edit profile | — |
| **Settings / Security** | No 2FA | Two-factor not enabled | Enable 2FA for extra security. | Enable 2FA | — |
| **Settings / Workspace** | No org | No workspace set up | Create a workspace to manage team billing. | Create workspace | — |
| **Settings / Billing** | No plan | No active plan | Choose a plan to unlock features. | View plans | — |
| **Search** | No results | No results for "query" | Try different keywords or check spelling. | Clear search | Browse categories |
| **Notifications** | None | No new notifications | You're all caught up. | Mark all read | Settings |

---

## 3. Variant Behaviors

### 3.1 First-Time Empty (Onboarding)
- **Illustration:** Larger (160px), friendly, brand accent
- **Body:** Value prop + first step
- **Dismiss:** "Remind me later" → stores preference, shows compact next time

### 3.2 Filtered Empty (Results = 0)
- **Illustration:** Search/filter icon
- **Headline:** "No results for [active filter]"
- **CTA:** "Clear filters" (primary) + "Search all" (secondary)

### 3.3 Permission Empty (No Access)
- **Illustration:** Lock
- **Headline:** "You don't have access"
- **Body:** "This section requires [Role] permissions"
- **CTA:** "Request access" → opens access request flow

### 3.4 Error Empty (Failed to Load)
- **Illustration:** Warning triangle
- **Headline:** "Unable to load [resource]"
- **Body:** "Check your connection or try again"
- **CTA:** "Retry" (primary) + "View cached" (secondary)

---

## 4. Visual Specification

| Property | Value |
|----------|-------|
| Container | Centered, max-width 400px, padding 32px |
| Illustration | 120×120px (standard), 160×160px (first-time), `aria-hidden="true"` |
| Headline | H2 (24px), weight 600, `--color-text-primary` |
| Body | Body (16px), weight 400, `--color-text-secondary`, max-width 360px |
| Primary Button | 48px height, full-width on mobile, `--color-primary` |
| Secondary Link | 16px, `--color-primary`, underline on hover |
| Spacing | 24px illustration→headline, 16px headline→body, 24px body→CTA |
| Background | `--color-surface`, border `--color-border`, radius 8px |

---

## 5. Accessibility

- **Landmark:** `<section aria-labelledby="empty-title">`
- **Heading:** `<h2 id="empty-title">` = headline text
- **Illustration:** `aria-hidden="true"` + `role="img"` if SVG has `<title>`
- **Focus:** Primary CTA receives focus on mount (if no other focus target)
- **Color:** Not sole conveyor (icon + text + button)
- **Reduced motion:** No animation on illustration
- **Zoom 200%:** No horizontal scroll; text wraps

---

## 6. Prototype Empty State Gallery

Route: `/demo/empty` — All 30+ states in searchable grid.
- Tabs: Standard / First-time / Filtered / Permission / Error
- Test: Keyboard nav, screen reader, 200% zoom, reduced motion
- Each state: Copy/paste JSON for translators