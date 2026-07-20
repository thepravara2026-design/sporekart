# Implementation Gap Register — Admin Console

| Gap ID | Module | Current State | Expected State | Dependencies | Impact | Suggested Sprint |
|--------|--------|---------------|----------------|--------------|--------|------------------|
| GAP-ADM-001 | Auth | No auth guard on /admin routes | Authentication required before accessing any admin route | Identity service integration | P0 — Any user can access admin | Sprint 3 |
| GAP-ADM-002 | Products | No CRUD operations | Product create/edit/delete/duplicate forms | Product workspace, Backend API | P0 — Cannot manage products | Sprint 4 |
| GAP-ADM-003 | Orders | No order management (status, cancel, bulk) | Order status updates, cancellation, bulk actions | Order service API | P1 — Cannot process orders | Sprint 4 |
| GAP-ADM-004 | Users | No /admin/users route | User list, search, role assignment, status management | Identity service API | P0 — Cannot manage users | Sprint 3 |
| GAP-ADM-005 | Roles | No role management UI | Role create/edit/delete, permission assignment | Permission system | P1 — Cannot configure access | Sprint 4 |
| GAP-ADM-006 | Coupons | No coupon/discount/promo routes | Coupon create/edit/disable/delete, price rules | Payment service | P1 — Cannot manage promotions | Sprint 5 |
| GAP-ADM-007 | Shipping | No zone/courier/rate config | Shipping zones, courier config, rate tables, weight rules | Fulfillment service | P1 — Cannot configure shipping | Sprint 5 |
| GAP-ADM-008 | Analytics | No charts or dashboards | Charts, KPIs, date range filters, data exports | Analytics service | P2 — No business intelligence | Sprint 6 |
| GAP-ADM-009 | Settings | No editable fields | Business info, app config, save/validation | Settings API | P2 — Cannot configure platform | Sprint 6 |
| GAP-ADM-010 | Inventory | No stock update UI | Stock quantity adjustment, low stock alerts, history | Inventory service | P0 — Cannot manage stock | Sprint 4 |
| GAP-ADM-011 | Audit Logs | No audit logs route | Audit log viewer with filters, export | Backend audit API | P2 — No compliance tracking | Sprint 6 |
| GAP-ADM-012 | Media | Full implementation exists | ✅ Complete | None | N/A | Implemented |
| GAP-ADM-013 | Training | Partial implementation | ✅ Most complete admin module | None | N/A | In progress |
| GAP-ADM-014 | DataGrid | Read-only generic wrapper | Module-specific workflows and custom data views | All backends | P1 — Generic UI only | Sprint 4+ |
