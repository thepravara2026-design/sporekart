# Fixed Bug Register — Bug Fix Sprint B (P1)

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

| ID | Severity | Module | Title | Fix Commit | Status |
|----|----------|--------|-------|------------|--------|
| BUG-RT-007 | HIGH | Workspace | WorkspacePage placeholder instead of redirect | `f979743` | ✅ Fixed |
| BUG-RT-008 | HIGH | Session | No browser history clearing on logout | `f979743` | ✅ Fixed |
| BUG-RT-009 | HIGH | Session | No multi-tab session synchronization | `f979743` | ✅ Fixed |
| BUG-RT-010 | HIGH | Global | No global error boundary | `0279772` | ✅ Fixed |
| BUG-RT-011 | HIGH | Session | No session expiry auto-redirect | `f979743` | ✅ Fixed |
| BUG-PERF-002 | HIGH | Memory | Timer leak in SessionTimeoutWarning | `930b106` | ✅ Fixed |
| SEC-005 | HIGH | Auth | Role can be changed via UI dropdown | `9da8173` | ✅ Fixed |
| SEC-011 | HIGH | Auth | Mock OTP accepts any code | `9da8173` | ✅ Fixed |
| BUG-COMP-001 | HIGH | Admin | Admin KPI grid collapses poorly on mobile | `64e8678` | ✅ Fixed |
| BUG-COMP-002 | HIGH | Admin | Admin tables overflow without horizontal scroll | `64e8678` | ✅ Fixed |
| BUG-COMP-003 | HIGH | Profile | Profile page buttons overlap on mobile | `64e8678` | ✅ Fixed |
| BUG-COMP-004 | HIGH | Navigation | Sidebar overlay remains after navigation | `64e8678` | ✅ Fixed |
| BUG-MOB-001 | HIGH | Admin | KPI grid collapses to single column on mobile | `64e8678` | ✅ Fixed |
| BUG-MOB-002 | HIGH | Admin | Tables overflow without horizontal scroll | `64e8678` | ✅ Fixed |
| BUG-MOB-003 | HIGH | Profile | Profile action buttons overlap on small mobile | `64e8678` | ✅ Fixed |
| BUG-MOB-004 | HIGH | Navigation | Sidebar drawer overlay persists after navigation | `64e8678` | ✅ Fixed |
| BUG-MOB-006 | HIGH | UI | Touch targets below WCAG minimum size | `64e8678` | ✅ Fixed |
| InventoryItem.getId | BLOCKER | inventory-service | Pre-existing compile blocker (gate condition 1) | `ff00116` | ✅ Fixed |

**17 entries · 16 P1 defects + 1 gate-condition blocker resolved.**

## Deferred (tracked, out of P1 scope)
- `ai-service` deeper compile defects — dedicated sprint required.
- `BUG-API-013..020`, `SEC-006..008`, `SEC-012` — backend foundation / platform gateway work.

---

*End of Fixed Bug Register.*
