# Risk Engine Design

## Purpose
Assesses business risks across 10 categories with severity classification and score computation.

## Risk Categories
| Category | Examples |
|---|---|
| REVENUE | Revenue concentration, pricing risk |
| INVENTORY | Stockout, overstock risk |
| OPERATIONAL | Process bottlenecks |
| MARKETPLACE | Market share, competitive pressure |
| CUSTOMER | NPS decline, churn risk |
| VENDOR | Vendor dependency |
| TRAINING | Pipeline velocity |
| PLATFORM | Infrastructure capacity |
| AUTOMATION | Automation coverage |
| AI | Model drift |

## Risk Scoring
- **Score: 0–100** — Composite of likelihood × impact
- **Severity:** CRITICAL (>80), HIGH (>60), MEDIUM (>40), LOW (≤40)
- **Summary endpoint** returns distribution + average score

## Key Methods
- `generateAllRisks()` — Generates 10 risks (one per category)
- `getRiskSummary()` — Returns count by severity + average score
