# Sprint 24 Part 7 — Enterprise Product Pricing, Commercial Rules & Tax Management Framework

## Overview

Establishes the permanent Pricing & Commercial Rules foundation for the SporeKart Enterprise Commerce Platform. All data is mock. No backend, no database, no financial calculations.

## Files Created

### Pricing Module (`src/admin/modules/products/pricing/`)

| File | Purpose |
|------|---------|
| `types.ts` | Domain types: PricingEntity, DiscountRule, CommercialRule, TaxRule, GSTEntry, HSNEntry, CurrencySetting, PriceSchedule, PriceHistoryEntry, PromotionalCampaign, BulkPricingOperation, PricePreviewData |
| `permissions.ts` | 5-tier role matrix (viewer → administrator) with `canPricing()` |
| `PricingPage.tsx` | 13-section workspace with sidebar, toolbar, content area, footer |
| `Pricing.css` | Responsive layout with mobile sidebar collapse |

### Mock Data (`mock/`)

| File | Data |
|------|------|
| `mockPrices.ts` | 15 products across 4 categories, 9 price tiers each, with GST/HSN/status |
| `mockDiscounts.ts` | 10 discount rules (percentage, fixed, volume, wholesale) |
| `mockTaxes.ts` | 5 tax rules (GST 0/5/12/18, Cess 1%) |
| `mockGst.ts` | 7 GST entries with CGST/SGST/IGST split |
| `mockHsn.ts` | 11 HSN codes with tax categories and commodity types |
| `mockCampaigns.ts` | 8 promotional campaigns across 8 types |
| `mockHistory.ts` | 15 price history timeline events |
| `mockScheduledPricing.ts` | 7 price schedules across 6 schedule types |
| `mockCommercialRules.ts` | 10 rules (min/max/suggested price, margin, cost, approval) |
| `mockCurrency.ts` | 7 currencies (INR default, USD, EUR, GBP, AED, SGD, JPY) |
| `mockActivity.ts` | 15 activity timeline events |

### State (`state/`)

| File | Purpose |
|------|---------|
| `usePricingState.ts` | Central hook: section nav, search/filter/sort, filtered data, selections |
| `usePricingAnalytics.ts` | Derived analytics: KPIs, GST distribution, status breakdown |
| `PricingNav.tsx` | Config-driven 13-section navigation sidebar |

### Components (`components/`)

| Component | Purpose |
|-----------|---------|
| `PricingDashboard` | Overview with 10 KPI cards, status/GST distribution bars, activity feed |
| `ProductPricingManager` | Product pricing with table/card view toggle, search/sort |
| `PricingTable` | Sortable table with MRP/Selling/Wholesale/GST/Discount/Status |
| `PriceCard` | Card grid view with 6 tier prices, discount badge |
| `CommercialRulesPanel` | Grid of 10 commercial rules with type badges and details |
| `DiscountManager` | Full discount rules table with type/value/period/status |
| `PromotionalPricing` | Campaign cards with type badges, discount, period, status |
| `TaxManager` | Tax rules table with type/percentage/category/applicability |
| `GSTManager` | GST rate cards with percentage circle, CGST/SGST/IGST split |
| `HSNManager` | HSN codes table with description/tax category/commodity/GST |
| `CurrencySettings` | Currencies table with symbol/precision/exchange rate/default |
| `PricePreview` | 3-panel (customer/admin/marketplace) live preview + summary bar |
| `PriceComparison` | Side-by-side comparison across 5 products with difference highlighting |
| `PricingHistory` | Timeline with color-coded events, price change badges |
| `ScheduledPricing` | Schedule cards with status/period/priority/prices/actions |
| `BulkPricing` | 8 operations in 3 groups with mock confirmation dialog |
| `PricingToolbar` | Search + sort + active filter badge bar |
| `PricingLoading` | Skeleton table, card, and stat card loading states |
| `PricingEmptyStates` | 7 empty states (no pricing, discounts, campaigns, scheduled, history, search, permission) |

### Preview Routes (`preview/`)

| Route | Content |
|-------|---------|
| `/preview/products/pricing/workspace` | Full pricing workspace |
| `/preview/products/pricing/analytics` | Pricing dashboard |
| `/preview/products/pricing/rules` | Commercial rules |
| `/preview/products/pricing/history` | Price history |
| `/preview/products/pricing/bulk` | Bulk operations |

Also available within the existing product preview at `/preview/products/pricing/*`.

## Architecture

- **Pricing Model**: 9 price tiers (MRP, Selling, Wholesale, Distributor, Dealer, Retail, Farmer, Training, Bundle) with automatic discount calculation, GST computation, and final price determination
- **Commercial Rules**: 10 rules covering minimum/maximum/suggested price, margin, cost, and approval requirements
- **Discount Engine**: Percentage, fixed, volume, wholesale, and coupon-based rules with conditions, stackable settings, and priority ordering
- **Tax Framework**: GST with CGST/SGST/IGST split at 0/3/5/12/18/28/0.25% rates, plus HSN/SAC code classification
- **Currency Framework**: INR default with multi-currency ready (USD, EUR, GBP, AED, SGD, JPY)
- **Analytics**: 10 KPI cards, GST/status distribution, activity timeline, price comparison

## Quality Gate

- ✓ Pricing Workspace completed
- ✓ Product Pricing completed
- ✓ Commercial Rules completed
- ✓ Discount Framework completed
- ✓ GST Framework completed
- ✓ HSN Framework completed
- ✓ Price Preview completed
- ✓ Price History completed
- ✓ Scheduled Pricing completed
- ✓ Bulk Pricing completed
- ✓ Responsive (sidebar collapse at 1023px, horizontal nav at 767px)
- ✓ Accessibility (aria-current, aria-label, aria-selected, role, keyboard)
- ✓ Performance (React.memo, useMemo, useCallback)
- ✓ Existing modules unaffected
- ✓ No backend, payments, GST calculation, tax filing, financial transactions
- ✓ Documentation

## Recommendations for Sprint 24 Part 8 (Enterprise Product Variants)

- Product Variant Architecture (size, color, pack, grade)
- SKU Generation Engine
- Packaging Configuration (retail, wholesale, bulk, gift)
- Barcode/UPC Management
- Variant Pricing & Inventory
- Bundle/Pack Composition
- Attribute Management Framework
- Variant Comparison & Selection UI
