# Part 8 — Mock Module Inventory

The 11 mock modules created for reuse validation. Each is a `ModulePage` instance driven by `moduleData.ts`. No business logic is implemented — data is static mock rows.

| Module | Route | Permission Action | Feature Gate | Mock Rows | Columns | KPIs |
|--------|-------|-------------------|--------------|-----------|---------|------|
| Products | `/admin/products` | `view` | — | 24 | 5 | 4 |
| Inventory | `/admin/inventory` | `view` | — | 24 | 5 | 4 |
| Orders | `/admin/orders` | `view` | — | 24 | 5 | 4 |
| Customers | `/admin/customers` | `view` | — | 24 | 5 | 4 |
| CRM | `/admin/crm` | `view` | — | 24 | 5 | 4 |
| Training | `/admin/training` | `view` | — | 24 | 5 | 4 |
| Shipping | `/admin/shipping` | `view` | — | 24 | 5 | 4 |
| Finance | `/admin/finance` | `view` | — | 24 | 5 | 4 |
| Reports | `/admin/reports` | `view` | — | 24 | 5 | 4 |
| Settings | `/admin/settings` | `view` | — | 12 | 4 | 0 |
| Analytics | `/admin/analytics` | `view` | — | 24 | 5 | 4 |

## File Map

```
src/admin/modules/
  moduleData.ts                         # MOCK_MODULES, MockModule, mockCol()
  ModulePage.tsx                        # reusable memo'd template
  dashboardWidgets.ts                   # MODULE_KPIS, getAllModuleKPIs()
  products/ProductsPage.tsx
  inventory/InventoryPage.tsx
  orders/OrdersPage.tsx
  customers/CustomersPage.tsx
  crm/CrmPage.tsx
  training/TrainingPage.tsx
  shipping/ShippingPage.tsx
  finance/FinancePage.tsx
  reports/ReportsPage.tsx
  settings/SettingsPage.tsx
  analytics/AnalyticsPage.tsx
```

## Notes

- `settings` uses 12 rows and 4 columns (config-style list) — demonstrates non-grid-shaped modules still fit the template.
- All mock data is `Record<string, any>[]`; real modules should replace with typed domain models + API hooks.
- KPI color tokens reuse the dashboard palette (`--color-success`, etc. via inline hex).
