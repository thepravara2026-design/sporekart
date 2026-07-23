# Analytics Engines

## Revenue Analytics Engine

**Class:** `RevenueAnalyticsEngine`

Seeded with 12 months (Jan–Dec 2025) of synthetic revenue data generated with pseudo-random seed 42.

| Method | Description |
|--------|-------------|
| `getRevenueSummary(period)` | Revenue, orders, AOV, revenue per customer, growth rate, product/category/region/channel breakdowns |
| `getRevenueByProduct(period, category)` | Revenue per product, optional category filter |
| `getRevenueByRegion(period)` | Revenue by 5 regions (North, South, East, West, Central) |
| `getRevenueByChannel(period)` | Revenue by 4 channels (Online, Retail, Wholesale, Training) |
| `getRevenueTrend(months)` | TrendDataPoints with moving average, seasonal factor, trend line, deviation |
| `getAverageOrderValue(period)` | AOV for given period |
| `getRevenuePerCustomer(period)` | RPC for given period |
| `getGrowthRate(current, previous)` | Period-over-period growth |
| `getTopProducts(limit, period)` | Top N products by revenue |

**Seed data:** Monthly revenues range from Rs.250K to Rs.580K across 8 product categories.

---

## Customer Analytics Engine

**Class:** `CustomerAnalyticsEngine`

Seeded with 12 months (Jan–Dec 2025) of synthetic customer data, seed 101.

| Method | Description |
|--------|-------------|
| `getCustomerSummary(period)` | Total/new/churned customers, churn rate, CLV, CAC, retention, satisfaction |
| `getCustomerSegments()` | 6 customer segments with characteristics and strategies |
| `getCustomerAcquisitionTrend(months)` | Acquisition trend data points |
| `getChurnAnalysis(period)` | Churn breakdown by segment with top reasons |
| `getCustomerLifetimeValue(period)` | CLV calculation |
| `getRetentionRate(period)` | Retention rate |
| `getCustomerSatisfactionTrend(months)` | Satisfaction trend data points |

**Seed data:** Base 1000 customers growing to ~1350 over 12 months with churn rates declining from 3.5% to 1.2%.

**Segments:** New Growers, Active Enthusiasts, Commercial Farmers, Enterprise Buyers.

---

## Training Analytics Engine

**Class:** `TrainingAnalyticsEngine`

Seeded with 6 months (Jul–Dec 2025) of synthetic training data, seed 202.

| Method | Description |
|--------|-------------|
| `getTrainingSummary(period)` | Students, batches, attendance, scores, certifications, revenue, profit |
| `getStudentPerformanceByCourse()` | Performance breakdown per course |
| `getCertificationRate()` | Overall certification rate |
| `getTrainingRevenue()` | Total training revenue |
| `getTrainingProfitMargin()` | Net training profit margin |
| `getScoreDistributionByModule()` | Average scores across 8 modules |
| `getStudentRetention()` | Student retention rate |
| `getTopPerformingCourses()` | Courses ranked by average score |

**Seed data:** 3 courses across 6 months, monthly students 45–72, revenue Rs.180K–310K, completion rates 78%–87%.

---

## Cultivation Analytics Engine

**Class:** `CultivationAnalyticsEngine`

Seeded with 12 months (Jan–Dec 2025) of synthetic cultivation data, seed 303.

| Method | Description |
|--------|-------------|
| `getCultivationSummary(period)` | Total yield, avg yield per batch/batch, yield by species/region, cycle time, contamination, disease, growers |
| `getYieldBySpecies(period)` | Yield breakdown by 5 species |
| `getYieldByRegion(period)` | Yield breakdown by region |
| `getAverageCycleTime(species)` | Average cultivation cycle days |
| `getContaminationRateTrend(months)` | Contamination rate trend data points |
| `getDiseaseIncidenceRate()` | Disease incidence percentages |
| `getGrowerSatisfaction()` | Average grower satisfaction |
| `getTopGrowers(limit)` | Top N growers by revenue |

**Seed data:** 5 species (Oyster 3200kg, Shiitake 2800kg, etc.), 5 regions, contamination rates 4%–6%.

---

## Customer Segmentation Engine

**Class:** `CustomerSegmentationEngine`

6 pre-defined segments with marketing strategies:

| Segment | Customers | Avg Revenue | Churn |
|---------|-----------|-------------|-------|
| High-Value Growers | 250 | Rs.5,000 | 3.2% |
| New Growers | 850 | Rs.400 | 18.5% |
| Hobbyist Cultivators | 1200 | Rs.400 | 8.1% |
| Commercial Farms | 80 | Rs.22,500 | 1.8% |
| Distributors | 120 | Rs.8,000 | 5.4% |
| At-Risk Customers | 340 | Rs.600 | 45.0% |
