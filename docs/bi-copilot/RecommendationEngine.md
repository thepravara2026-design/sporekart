# Recommendation Engine

## Overview

The Recommendation Engine provides decision support by generating prioritized, explainable business recommendations based on analytics data. Each recommendation includes WHY rationale, expected impact, confidence score, and supporting data.

## Recommendation Generation Flow

```
Analytics Data (from any engine)
    |
    v
[Rule Evaluation] -- Threshold-based rules across all domains
    |                  - Revenue drops > 10%
    |                  - Customer churn > 15%
    |                  - Inventory stockouts
    |                  - Training underperformance
    |                  - Growth opportunities
    v
[Priority Classification]
    |                  - Critical: Immediate action required
    |                  - High: Action within current cycle
    |                  - Medium: Action within next cycle
    |                  - Low: Monitor and plan
    v
[Explanation Assembly]
    |                  - WHY: Root cause analysis
    |                  - Impact: Quantified business impact
    |                  - Confidence: Statistical confidence score
    |                  - Data: Supporting metrics and trends
    v
[Action Items] -- Concrete, actionable steps
    |
    v
DecisionRecommendation
```

## Priority Classification

| Priority | Criteria | Response Time |
|----------|----------|---------------|
| **Critical** | Revenue impact > 20%, churn risk > 25%, stockout imminent, compliance risk | Hours |
| **High** | Revenue impact 10-20%, churn risk 15-25%, inventory below safety stock | Days |
| **Medium** | Revenue impact 5-10%, optimization opportunities, process improvements | This cycle |
| **Low** | Minor improvements, nice-to-have optimizations, monitoring alerts | Next cycle |

## Explanation Engine

Each recommendation includes a structured explanation with four components:

### 1. WHY (Rationale)
Root cause identified from data analysis, e.g.:
- "Revenue declined due to 15% drop in Mushroom Products category, driven by reduced Fresh Oyster Mushroom sales in Maharashtra"
- "Customer churn increased as 200+ Home Growers have not purchased in 90+ days"

### 2. Impact
Quantified business impact expressed in INR or percentage:
- "Recovering this segment could restore 4.2L in monthly revenue"
- "Reducing churn by 5% would retain 150 customers worth 22.5L annually"

### 3. Confidence
Statistical confidence score (0.0 - 1.0) based on:
- Data volume supporting the insight
- Historical pattern strength
- Signal-to-noise ratio in the metric

### 4. Supporting Data
Key data points that substantiate the recommendation, e.g.:
- Trend chart references
- Comparison tables (period-over-period)
- Segment breakdowns

## Seed Templates

### Revenue Recovery
```json
{
  "title": "Revenue Recovery Opportunity",
  "category": "revenue",
  "priority": "High",
  "rationale": "Revenue declined {x}% MoM driven by {category} in {region}",
  "expectedImpact": "Restoring {category} sales could recover INR {amount}",
  "actionItems": ["Run promotion on {product}", "Adjust pricing for {segment}"]
}
```

### Customer Retention
```json
{
  "title": "Customer Retention Alert",
  "category": "customers",
  "priority": "Critical",
  "rationale": "{count} customers in {segment} segment have been inactive for {months}+ months",
  "expectedImpact": "Re-engaging {pct}% could retain INR {amount} in annual revenue",
  "actionItems": ["Launch re-engagement campaign", "Offer loyalty discount"]
}
```

### Inventory Restock
```json
{
  "title": "Inventory Restock Required",
  "category": "inventory",
  "priority": "High",
  "rationale": "{count} items below safety stock in {category}",
  "expectedImpact": "Stockout risk of {pct}% could impact {amount} in revenue",
  "actionItems": ["Place reorder for {items}", "Expedite shipping for critical items"]
}
```

### Training Optimization
```json
{
  "title": "Training Program Optimization",
  "category": "training",
  "priority": "Medium",
  "rationale": "{course} has {rate}% completion rate vs average of {avg}%",
  "expectedImpact": "Improving completion by {x}% could increase certification revenue by INR {amount}",
  "actionItems": ["Review {course} curriculum", "Schedule additional trainer support"]
}
```

### Growth Opportunity
```json
{
  "title": "Growth Opportunity Identified",
  "category": "growth",
  "priority": "Medium",
  "rationale": "{product} sales grew {x}% with {region} showing highest demand",
  "expectedImpact": "Scaling {product} distribution could add INR {amount} in revenue",
  "actionItems": ["Increase {product} inventory by {pct}%", "Expand marketing in {region}"]
}
```
