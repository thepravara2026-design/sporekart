# Microcopy Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory voice & patterns. All UI text follows this. Reuses Principle 3 (Clarity), Principle 9 (Trust), Principle 10 (Accessibility).

---

## 1. Voice & Tone

| Dimension | Guideline |
|-----------|-----------|
| **Professional** | Correct grammar, no slang, no emoji in body text (icons OK) |
| **Friendly** | Active voice, "you/your", contractions OK (you're, we'll) |
| **Simple** | Short sentences, common words, no jargon without definition |
| **Clear** | Specific over vague; numbers over words for quantities |
| **Farmer-friendly** | Hindi/Marathi terms in parentheses where helpful; avoid academic ag terms |
| **Enterprise quality** | Precise, consistent terminology; no marketing fluff |

**Tone Spectrum by Context:**
| Context | Tone |
|---------|------|
| Onboarding / Empty states | Encouraging, helpful |
| Errors / Warnings | Direct, calm, actionable |
| Success | Warm, brief |
| Settings / Admin | Authoritative, precise |
| Help / Tooltips | Instructive, scannable |
| Legal / Compliance | Formal, exact |

---

## 2. Word List (Canonical Terms)

| Use | Avoid |
|-----|-------|
| **Order** | Purchase, Transaction, Sale |
| **Product** | Item, SKU, Good (except "Good" in "Goods & Services") |
| **Customer** | Buyer, User (unless technical), Client |
| **Farmer** | Grower (use "Grower" for commercial role), Producer |
| **Distributor** | Dealer, Reseller |
| **Training** | Course, Workshop, Session (use specific child term) |
| **Session** | Class, Event (for Training children) |
| **Catalog** | Store, Shop, Marketplace |
| **Cart** | Basket, Bag |
| **Checkout** | Payment, Purchase flow |
| **Fulfillment** | Shipping, Dispatch, Delivery (use specific) |
| **Invoice** | Bill, Receipt (different things) |
| **Warehouse** | Godown (use "Warehouse" in English UI) |
| **Input** | Fertilizer, Seed, Pesticide (use specific category) |
| **Output** | Harvest, Produce, Yield |
| **Soil Test** | Soil Analysis, Soil Health Card |
| **Crop Advisory** | Recommendation, Prescription |
| **AI Assistant** | Bot, Chatbot, Agent |
| **Prompt** | Query, Question (technical term) |
| **Knowledge Base** | Help Center, FAQ (use KB for internal RAG) |
| **Workspace** | Organization, Team, Account (billing context) |
| **Invite** | Add user, Onboard |

---

## 3. Capitalization

| Element | Style | Example |
|---------|-------|---------|
| Page Title (H1) | Sentence case | "Orders" / "Create new order" |
| Section Heading (H2/H3) | Sentence case | "Order details" |
| Button (Primary) | Title Case | "Place Order" |
| Button (Secondary) | Title Case | "Cancel" |
| Link (inline) | Sentence case | "View all orders" |
| Label | Sentence case | "Email address" |
| Placeholder | Sentence case | "Enter your email" |
| Error Message | Sentence case | "Email is required" |
| Toast / Banner | Sentence case | "Order placed successfully" |
| Tooltip | Sentence case | "Maximum 500 characters" |
| Tab | Title Case | "Orders" / "Analytics" |
| Menu Item | Title Case | "New Order" |

---

## 4. Punctuation

- **No period** on: Button labels, menu items, tab labels, toast headlines, H1–H3
- **Period** on: Full sentences in body text, helper text, error descriptions, tooltips
- **Oxford comma:** Yes ("Orders, products, and training")
- **Ellipsis (…)** in button: Only if opens dialog with more steps ("Export…")
- **Colon:** In labels only if followed by input ("Email:"); not in headings

---

## 5. Numbers & Units

| Rule | Example |
|------|---------|
| 0–9 spelled out (except measurements) | "Five orders" / "5 kg" |
| 10+ numerals | "12 orders" |
| Measurements always numerals + unit | "5 kg", "2.5 ha", "100 mL" |
| Currency: symbol + numeral, no decimals if whole | "₹1,250" / "₹1,250.50" |
| Percent: numeral + % | "12%" |
| Dates: DD MMM YYYY | "12 Jul 2026" |
| Time: 24h or 12h with am/pm | "14:30" / "2:30 PM" |
| Phone: +91 XXXXX XXXXX | "+91 98765 43210" |
| Large numbers: Indian grouping (lakh/crore) | "1,25,000" / "1.25 Cr" |

---

## 6. Button Labels (Primary Actions)

| Pattern | Example |
|---------|---------|
| Verb + Noun (create) | "Create order" |
| Verb + Noun (action) | "Place order" |
| Verb + Noun (navigation) | "View details" |
| Verb (single, destructive) | "Delete" |
| Verb + "all" (bulk) | "Select all" |
| Noun (toggle) | "Filters" |

**Destructive:** "Delete order" (not "Remove") — red button
**Cancel (dialog):** "Cancel" (ghost button)
**Confirm (dialog):** Matches action — "Delete order" (red)

---

## 7. Form Field Patterns

| Element | Pattern | Example |
|---------|---------|---------|
| **Label** | Noun phrase | "Email address" |
| **Required indicator** | ` *` in label | "Email address *" |
| **Placeholder** | Action + example | "Enter email@domain.com" |
| **Helper** | Constraint / tip | "We'll never share your email" |
| **Error** | Specific + fix | "Enter a valid email address" |
| **Success hint** | Confirmation | "Email verified" |

---

## 8. Empty State Copy (Explain → Guide → Act)

| Component | Headline | Body | CTA |
|-----------|----------|------|-----|
| Orders | No orders yet | When you place an order, it will appear here. | Place your first order |
| Products | No products found | Try adjusting your filters or search terms. | Clear filters |
| Training | No sessions yet | Published sessions will appear here. | Create session |
| AI Chat | No conversations yet | Ask a question to start your first chat. | Ask AI |
| Search | No results for "query" | Try different keywords or check spelling. | Clear search |
| Notifications | No new notifications | You're all caught up. | Mark all read |

---

## 9. Error Copy Patterns

| Error Type | Headline | Body | CTA |
|------------|----------|------|-----|
| 404 | Page not found | The page you're looking for doesn't exist or has moved. | Go to Home |
| 403 | Access restricted | This page requires Distributor access. Your role: Customer. | Request Access |
| 401 | Session expired | Please sign in to continue. We'll bring you back. | Sign In |
| 500 | Something went wrong | Our team has been notified. Reference: ERR-ABC123 | Retry |
| Network | Connection lost | Working offline. Changes will sync when reconnected. | Retry Now |
| Validation | Check your input | One or more fields need correction. | (Focus first error) |
| Conflict | Already exists | An order with this reference already exists. | View Existing |
| Timeout | Request timed out | Your data is saved. Try again. | Retry |

---

## 10. Toast / Notification Copy

| Type | Pattern | Example |
|------|---------|---------|
| **Success** | [Noun] [verb-past] | "Order placed" / "Profile saved" |
| **Success + Undo** | [Noun] [verb-past] · [Undo] | "Order deleted · Undo" |
| **Warning** | [Context]: [Issue] | "Sync pending: 3 changes offline" |
| **Error** | [Action] failed: [Reason] | "Save failed: Server unavailable" |
| **Info** | [Update] | "New training session available" |

---

## 11. Tooltip / Help Text Patterns

| Trigger | Content |
|---------|---------|
| Icon-only button | Action name ("Create order") |
| Truncated text | Full text |
| Complex field | Constraint + example ("PAN: 10 chars, e.g., ABCDE1234F") |
| Advanced setting | What it does + impact ("Enables auto-reorder when stock < 10%") |
| Permission badge | Role required + action ("Distributor: Can fulfill orders") |

---

## 12. Loading Copy

| State | Text |
|-------|------|
| Route skeleton | (No text — visual only) |
| Section skeleton | (No text) |
| Button loading | "Saving…" / "Loading…" / "Verifying…" |
| Search | "Searching…" |
| AI thinking | "Thinking…" |

**Never:** "Loading...", "Please wait...", "Processing..."

---

## 13. Accessibility in Copy

- **No "Click here"** — use descriptive link text ("View order ORD-123")
- **No "See below/above"** — use "In the next section" / "In the order details"
- **No color-only references** — "The red button" → "The Delete button"
- **Abbreviations:** First use spelled out ("Goods and Services Tax (GST)")
- **Units in labels:** "Quantity (kg)" not "Quantity"

---

## 14. Localization Notes (Future)

- **Hindi/Marathi:** Devanagari numerals option; RTL not needed
- **String keys:** `t('orders.empty.headline')` — namespace by feature
- **Pluralization:** ICU MessageFormat (`{count, plural, one{Order} other{Orders}}`)
- **Gender:** Avoid; use neutral ("Your profile" not "His/Her profile")
- **Date/Number:** ICU formatters per locale

---

## 15. Prototype Microcopy Gallery

Route: `/demo/microcopy` — All patterns rendered: buttons, forms, errors, toasts, empty states, tooltips, loading. Toggle language (EN / HI placeholder). Copy/paste ready for translators.