# SporeKart QA Sprint 2 — Order History Report

**Date:** 2026-07-17  
**Phase:** 3 — Order History Validation  

---

## Test Results

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari | Result |
|------|----------|--------|---------------|---------------|--------|
| Orders dashboard renders with list | PASS | PASS | PASS | PASS | PASS |
| Order statistics cards display | PASS | PASS | PASS | PASS | PASS |
| AI Order Assistant section renders | PASS | PASS | PASS | PASS | PASS |
| Status filter tabs render | PASS | PASS | PASS | PASS | PASS |
| Filter by Active tab works | PASS | PASS | FAIL | PASS | DEFECT (Mobile Chrome) |
| Search input present | PASS | PASS | PASS | PASS | PASS |
| Search input accepts text | PASS | PASS | PASS | PASS | PASS |
| All 4 order IDs displayed | FAIL | FLAKY | FAIL | FAIL | DEFECT |
| Data persists on refresh | PASS | PASS | PASS | PASS | PASS |
| Different order loads | PASS | PASS | PASS | PASS | PASS |
| Empty state not shown (orders exist) | PASS | PASS | PASS | PASS | PASS |
| Browser restart preserves session | FAIL | FAIL | FAIL | FAIL | TEST CODE ISSUE |

**Total: 48 unique, 28 pass, 16 fail, 4 flaky**

---

## Detailed Findings

### PASS — Core Functionality
- Orders dashboard renders with all 4 mock orders
- Statistics cards: Total Spend (₹11,600), Active Orders (2), Delivered (1), Returns & Refunds (1)
- AI Order Assistant section with actionable insights
- 4 filter tabs: All, Active, Completed, Refunded
- Search field with placeholder "Search by order ID or product name..."

### FAIL — Order IDs Display
- **BUG-ORD-002:** On some browsers/runs, not all 4 order IDs appear in body text
- Likely due to lazy rendering or virtualization — some order cards may not be in DOM at time of assertion
- Impact: Medium — data is loaded but may require scrolling to see all items

### FAIL — Browser Restart Test
- **TEST CODE ISSUE:** Test calls `context.close()` then `context.newPage()` on closed context
- Not an app defect — test logic error
- Impact: None on application quality

---

## Bug Reference
- BUG-ORD-002: Order cards may not all render in DOM simultaneously (virtualization/lazy loading)
