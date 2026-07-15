# Intelligence Forecasting

## Overview
The Forecasting module provides 12-month demand, stock, purchase, capacity, expiry, and reorder forecasts. Data is mock-generated with monthly granularity.

## Forecast Data Arrays
| Array | Description | Range |
|-------|-------------|-------|
| demandForecast | Predicted demand per month | 4,500–6,600 |
| stockForecast | Predicted stock levels per month | 12,400–15,200 |
| purchaseForecast | Predicted purchase volumes per month | 3,000–4,400 |
| capacityForecast | Predicted warehouse capacity % per month | 78%–87% |
| expiryForecast | Predicted expiring items per month | 12–40 |
| reorderForecast | Predicted reorder events per month | 8–19 |

## Month Labels
12-month cycle: Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec

## Visualization
- Demand Forecast trend line chart
- Forecast by period bar chart
- Confidence indicators on forecast cards

## Mock Mode
All forecast data is static mock data. No ML, AI, or predictive algorithms are used. The architecture supports future integration with forecasting engines.
