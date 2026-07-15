# Pricing Framework

## Architecture

The Pricing Framework supports 9 distinct price tiers across all products:

| Tier | Usage | Margin Level |
|------|-------|-------------|
| MRP | Maximum Retail Price | 100% |
| Selling | Standard selling price | 85% |
| Wholesale | Bulk purchase price | 70% |
| Distributor | Distributor channel price | 60% |
| Dealer | Dealer channel price | 65% |
| Retail | Retail channel price | 90% |
| Farmer | Direct farmer price | 55% |
| Training | Educational institution price | 50% |
| Bundle | Combined product price | 75% |

## Key Features

- **Automatic Calculations**: Discount %, GST amount, final price computed from raw prices
- **Status Workflow**: draft → pending → active → archived with approval gating
- **Bulk Operations**: Update, discount, GST, HSN, approve, archive, publish, schedule
- **Scheduling**: Campaign, seasonal, limited-time, flash sale, weekend, festival, launch pricing
- **History**: Full audit trail with price changes, type, user, reason, restore placeholder

## Future Backend Integration

Pricing data stored in Supabase with real-time sync to Elasticsearch for search. Dynamic pricing engine via Azure Functions. AI-assisted recommendations via Azure OpenAI.
