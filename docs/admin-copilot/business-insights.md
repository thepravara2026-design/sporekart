# Business Insights Engine

## Overview

The Business Insights Engine is a core component of Admin Copilot that automatically generates actionable business intelligence by comparing current period data against historical baselines. It detects trends, anomalies, and patterns across the SporeKart platform and presents them with severity classifications and recommended actions.

## Insight Generation Methodology

Insights are generated through a three-phase pipeline:

```
Historical Data
    |
    v
Phase 1: Data Collection
    |-- Fetch current period metrics (today, this week, this month)
    |-- Fetch previous period metrics (yesterday, last week, last month)
    |-- Fetch year-over-year metrics (same period last year)
    |-- Normalize for day-of-week and seasonal effects
    |
    v
Phase 2: Comparison & Analysis
    |-- Calculate absolute and percentage change
    |-- Apply statistical significance thresholds
    |-- Detect trend direction (up, down, stable)
    |-- Identify anomalies via z-score analysis
    |-- Cross-reference related metrics for context
    |
    v
Phase 3: Insight Generation
    |-- Classify into insight category
    |-- Assign severity level
    |-- Generate human-readable summary
    |-- Compute action recommendations
    |-- Tag with relevant dimensions (product, region, category)
```

### Comparison Calculation

```
delta_absolute = current_value - previous_value
delta_percentage = (delta_absolute / previous_value) * 100
significance = |delta_percentage| >= threshold (category-specific)

trend_direction =
    delta_percentage > threshold_up   -> UP
    delta_percentage < threshold_down -> DOWN
    otherwise                          -> STABLE
```

### Statistical Significance Thresholds

| Category | Minimum Change (Warning) | Minimum Change (Critical) |
|---|---|---|
| Sales | +/- 5% | +/- 20% |
| Customer | +/- 3% | +/- 15% |
| Inventory | +/- 10% | +/- 25% |
| Operational | +/- 5% | +/- 15% |
| Risk | +/- 2% | +/- 10% |

## Insight Categories

### 1. Sales Insights

Monitors revenue, order volume, average order value (AOV), conversion rates, and product performance.

| Metric | Comparison | Example Insight |
|---|---|---|
| Revenue | vs previous period | "Revenue increased 12.3% this week compared to last week, driven by mushroom kit sales." |
| Order Volume | vs previous period | "Order volume dropped 8% today vs yesterday; investigate possible checkout issues." |
| AOV | vs previous period | "Average order value rose to $47.50 (+6%) as customers added more spawn products." |
| Conversion Rate | vs previous period | "Conversion rate fell from 4.2% to 3.1% (-26%); review traffic sources." |
| Top Products | vs previous period by revenue | "Oyster Mushroom Kit overtook Shiitake Log as top seller this week." |
| Revenue by Region | vs previous period | "West Coast revenue grew 22% vs last month; Central region flat." |

### 2. Customer Insights

Tracks acquisition, retention, churn, lifetime value, and engagement metrics.

| Metric | Comparison | Example Insight |
|---|---|---|
| New Customers | vs previous period | "New customer acquisition jumped 18% this month, correlating with the beginner workshop campaign." |
| Repeat Purchase Rate | vs previous period | "Repeat purchase rate declined to 34% (was 39%); consider loyalty program re-engagement." |
| Churn Rate | vs previous period | "Customer churn increased to 8.2% this quarter, highest in 6 months." |
| Customer Lifetime Value | cohort-based | "CLV for Q2 2026 cohort is $320, 15% higher than Q1 cohort." |
| Retention by Cohort | monthly cohorts | "3-month retention for January cohort is 62%, outperforming December's 55%." |
| Engagement Score | vs previous period | "Average sessions per user dropped from 4.2 to 3.1; mobile app usage declining." |

### 3. Inventory Insights

Monitors stock levels, turnover rates, stockouts, and inventory valuation.

| Metric | Comparison | Example Insight |
|---|---|---|
| Stock Turnover Ratio | vs previous period | "Inventory turnover slowed to 2.1x this month (was 2.8x); overstock risk for substrate products." |
| Stockout Rate | vs previous period | "Stockout rate increased to 3.4% of SKUs; spore syringes most affected." |
| Days of Supply | vs previous period | "Grow kits have 45 days of supply, above 30-day target." |
| Inventory Value | vs previous period | "Inventory value grew to $1.2M (+8%), driven by new product lines." |
| Slow-moving Stock | vs 90-day threshold | "12 SKUs have not moved in 90+ days; consider discounting or write-off." |
| ABC Classification Shift | vs previous period | "Premium substrate blend moved from B-class to A-class by revenue contribution." |

### 4. Operational Insights

Tracks system health, service performance, fulfillment metrics, and support load.

| Metric | Comparison | Example Insight |
|---|---|---|
| Order Fulfillment Time | vs previous period | "Average fulfillment time increased to 3.2 days from 2.1 days; fulfillment bottleneck detected." |
| Payment Success Rate | vs previous period | "Payment success rate dropped to 95.2% (was 98.1%); investigate payment gateway." |
| Return Rate | vs previous period | "Return rate for spawn products increased to 4.1%; possible quality issue." |
| Support Ticket Volume | vs previous period | "Support ticket volume spiked 40% today; correlated with new product launch." |
| API Response Times | vs previous period | "Analytics service P99 latency increased to 1.2s (was 450ms); investigate query performance." |
| Service Uptime | vs previous period | "Platform uptime was 99.95% this month; one brief catalog-service outage on July 15." |

### 5. Risk Insights

Monitors fraud indicators, compliance metrics, payment risks, and platform security.

| Metric | Comparison | Example Insight |
|---|---|---|
| Fraud Detection Rate | vs previous period | "Flagged transactions increased 250% after new fraud model deployment; 92% confirmed fraud." |
| Refund Rate | vs previous period | "Refund rate exceeded 5% threshold for the third consecutive week." |
| Failed Login Attempts | vs previous period | "Brute-force login attempts increased 300%; rate limiting activated." |
| Chargeback Rate | vs previous period | "Chargeback rate is 0.8%, approaching the 1.0% processor threshold." |
| Compliance Deadlines | upcoming | "GDPR data retention purge deadline in 14 days; 60% of records still pending." |
| High-Risk Orders | vs previous period | "Orders flagged as high-risk doubled this week; concentrated in new customer accounts." |

## Severity Levels

### INFO

Normal variation or positive trends that do not require action. Used for awareness.

```
Severity: INFO
Example: "Website traffic is up 3% week-over-week within normal variation."
Color: Blue
Alert: No
```

### WARNING

Significant change that may require attention but is not immediately critical.

```
Severity: WARNING
Example: "Inventory turnover dropped 18%; review purchasing patterns before next order cycle."
Color: Yellow/Amber
Alert: Digest notification (daily)
```

### CRITICAL

Urgent change requiring immediate investigation or action.

```
Severity: CRITICAL
Example: "Payment success rate dropped to 88%; critical impact on revenue. Investigate immediately."
Color: Red
Alert: Real-time (push notification, email, PagerDuty)
```

## Trend Detection Algorithm

The trend detection engine uses a combination of statistical methods:

### Short-term Trend (7-day moving average)

```python
def detect_short_term_trend(daily_values, threshold=0.05):
    ma_7 = rolling_average(daily_values, window=7)
    ma_3 = rolling_average(daily_values, window=3)
    change = (ma_3[-1] - ma_7[-1]) / ma_7[-1]
    if change > threshold:
        return "ACCELERATING"
    elif change < -threshold:
        return "DECELERATING"
    else:
        return "STABLE"
```

### Long-term Trend (90-day linear regression)

```python
def detect_long_term_trend(values, days=90):
    x = list(range(len(values[-days:])))
    y = values[-days:]
    slope, intercept = linear_regression(x, y)
    annualized_slope = slope * 365
    if annualized_slope > 0:
        return "GROWING", annualized_slope
    elif annualized_slope < 0:
        return "DECLINING", annualized_slope
    else:
        return "FLAT", annualized_slope
```

### Anomaly Detection (z-score)

```python
def detect_anomaly(value, history, z_threshold=2.5):
    mean = statistics.mean(history)
    std = statistics.stdev(history)
    z_score = (value - mean) / std
    if abs(z_score) > z_threshold:
        return True, z_score
    return False, z_score
```

## Action Recommendation Generation

Each insight is paired with one or more recommended actions. Recommendations are generated based on the insight category, severity, and detected pattern.

| Pattern | Category | Recommended Action |
|---|---|---|
| Revenue declining | Sales | "Review pricing strategy and consider promotion for top-10 products." |
| Revenue growing | Sales | "Investigate what's driving growth and consider increasing inventory for top performers." |
| Churn increasing | Customer | "Launch re-engagement campaign for customers aged 60-90 days." |
| Inventory overstock | Inventory | "Schedule flash sale for slow-moving SKUs identified by ABC analysis." |
| Stockout risk | Inventory | "Trigger reorder for products below safety stock levels." |
| Fulfillment slowing | Operational | "Escalate to fulfillment team; review pick/pack bottleneck at warehouse." |
| Payment failures | Operational | "Contact payment gateway provider; check for API or certificate issues." |
| Fraud spike | Risk | "Review fraud rules; consider temporarily tightening verification requirements." |
| Chargeback near threshold | Risk | "Flag to finance team; review dispute documentation process." |
| Support volume spike | Operational | "Activate support overflow protocol; consider knowledge base update for known issue." |

## Examples

### Sales Insight (WARNING)

```json
{
  "id": "insight-sales-001",
  "category": "SALES",
  "severity": "WARNING",
  "title": "Week-over-Week Revenue Decline",
  "summary": "Revenue for the current week (July 17-23) is $84,500, which is 12.3% lower than the previous week ($96,300). The decline is primarily driven by a 15% drop in mushroom kit sales.",
  "currentValue": 84500.00,
  "previousValue": 96300.00,
  "deltaPercentage": -12.3,
  "trend": "DECLINING",
  "dimensions": {
    "productCategory": "Mushroom Kits",
    "region": "All"
  },
  "recommendedActions": [
    "Review pricing and promotion calendar for mushroom kits.",
    "Check inventory availability for top-selling kit variants.",
    "Analyze traffic sources to identify any acquisition channel drops."
  ],
  "generatedAt": "2026-07-23T09:35:00Z",
  "expiresAt": "2026-07-24T09:35:00Z"
}
```

### Customer Insight (CRITICAL)

```json
{
  "id": "insight-cust-003",
  "category": "CUSTOMER",
  "severity": "CRITICAL",
  "title": "Churn Rate Exceeds Threshold",
  "summary": "Monthly churn rate reached 9.4%, the highest value in 12 months and 2.3x above the 12-month average of 4.1%. The spike is concentrated in customers acquired during the June promotion campaign.",
  "currentValue": 9.4,
  "previousValue": 4.1,
  "deltaPercentage": 129.3,
  "trend": "ACCELERATING",
  "dimensions": {
    "cohort": "June 2026 Promotion",
    "customerSegment": "New"
  },
  "recommendedActions": [
    "Immediately launch retention campaign for June promotion cohort.",
    "Analyze June cohort behavior to identify drop-off triggers.",
    "Review promotion qualification criteria to improve customer fit.",
    "Schedule executive review of customer onboarding flow."
  ],
  "generatedAt": "2026-07-23T09:35:00Z",
  "expiresAt": "2026-07-24T09:35:00Z"
}
```

### Inventory Insight (INFO)

```json
{
  "id": "insight-inv-002",
  "category": "INVENTORY",
  "severity": "INFO",
  "title": "ABC Classification Change Detected",
  "summary": "Premium Substrate Blend has moved from B-class to A-class based on trailing 90-day revenue contribution (now 12.3% of total). Consider increasing safety stock levels.",
  "currentValue": 12.3,
  "previousValue": 5.1,
  "deltaPercentage": 141.2,
  "trend": "GROWING",
  "dimensions": {
    "productSku": "SUB-PREM-001",
    "classification": "A"
  },
  "recommendedActions": [
    "Review and update safety stock levels for Premium Substrate Blend.",
    "Ensure supplier lead times are adequate for increased demand.",
    "Consider increasing forecast quantity for next procurement cycle."
  ],
  "generatedAt": "2026-07-23T09:35:00Z",
  "expiresAt": "2026-07-30T09:35:00Z"
}
```

### Operational Insight (CRITICAL)

```json
{
  "id": "insight-ops-001",
  "category": "OPERATIONAL",
  "severity": "CRITICAL",
  "title": "Payment Gateway Degradation",
  "summary": "Payment success rate dropped to 88.2% in the last hour (from 98.5% baseline). 412 payments failed out of 3,480 attempts. Error pattern suggests gateway timeout issues.",
  "currentValue": 88.2,
  "previousValue": 98.5,
  "deltaPercentage": -10.5,
  "trend": "DECLINING",
  "dimensions": {
    "gateway": "stripe",
    "errorCode": "timeout"
  },
  "recommendedActions": [
    "Contact Stripe support immediately regarding timeout errors.",
    "Activate fallback payment gateway if available.",
    "Notify affected customers of potential payment delays.",
    "Monitor recovery and confirm success rate returns to 98%+."
  ],
  "generatedAt": "2026-07-23T09:35:00Z",
  "expiresAt": "2026-07-23T10:35:00Z"
}
```

### Risk Insight (WARNING)

```json
{
  "id": "insight-risk-002",
  "category": "RISK",
  "severity": "WARNING",
  "title": "Chargeback Rate Approaching Threshold",
  "summary": "Monthly chargeback rate is 0.82%, approaching the 1.0% processor threshold. If exceeded, the payment processor may impose penalties or terminate the merchant account.",
  "currentValue": 0.82,
  "previousValue": 0.45,
  "deltaPercentage": 82.2,
  "trend": "ACCELERATING",
  "dimensions": {
    "processor": "stripe",
    "threshold": "1.0%"
  },
  "recommendedActions": [
    "Review dispute documentation process with fulfillment team.",
    "Analyze common chargeback reasons to address root causes.",
    "Consider implementing 3D Secure for high-value transactions.",
    "Flag to finance team for monthly business review."
  ],
  "generatedAt": "2026-07-23T09:35:00Z",
  "expiresAt": "2026-07-30T09:35:00Z"
}
```

## Configuration

The Business Insights Engine is configured via `application.yml`:

```yaml
sporekart:
  copilot:
    admin:
      insights:
        enabled: true
        lookback-days: 30
        comparison-periods:
          - PREVIOUS_DAY
          - PREVIOUS_WEEK
          - PREVIOUS_MONTH
          - YEAR_OVER_YEAR
        significance-thresholds:
          sales: 5.0
          customer: 3.0
          inventory: 10.0
          operational: 5.0
          risk: 2.0
        trend:
          short-term-window: 7
          long-term-window: 90
          anomaly-z-threshold: 2.5
        severity:
          critical-warning-multiplier: 4.0
          warning-info-multiplier: 1.5
        schedule: "0 */30 * * * *" # every 30 minutes
```
