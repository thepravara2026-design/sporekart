# Routing Engine

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  

---

## Overview

The Routing Engine is responsible for detecting user intent from natural language input and dispatching the request to the appropriate copilot. It sits inside the Copilot Router component and supports both automatic intent detection and explicit copilot selection.

---

## Intent Detection Approach

Intent detection uses a **two-phase strategy**:

1. **Fast Path (Rule-Based)** — Keyword and pattern matching against known intent patterns. Returns immediately if confidence > 0.9.
2. **Deep Path (ML Classifier)** — A lightweight classifier (or LLM call) is invoked when the rule-based score is below 0.9.

The engine always returns an intent name and a confidence score (0.0 – 1.0).

---

## Routing Rules Table

| Intent Pattern | Keywords / Examples | Target Copilot | Min Confidence |
|---------------|---------------------|----------------|----------------|
| `campaign_create` | "create campaign", "new campaign", "run a promotion" | Marketing | 0.6 |
| `campaign_analyze` | "campaign performance", "how did campaign X do" | Marketing | 0.6 |
| `segment_query` | "find customers who", "segment of users" | Marketing | 0.6 |
| `lead_score` | "score this lead", "lead quality" | Sales | 0.6 |
| `quote_create` | "create a quote", "new quote for client" | Sales | 0.6 |
| `pipeline_forecast` | "pipeline forecast", "deal forecast" | Sales | 0.6 |
| `ticket_resolve` | "resolve ticket", "help with ticket" | Service | 0.6 |
| `knowledge_search` | "search knowledge base", "find article about" | Service | 0.6 |
| `case_escalate` | "escalate this case", "urgent ticket" | Service | 0.6 |
| `product_search` | "find product", "search catalog", "show me" | Commerce | 0.6 |
| `order_status` | "order status", "where is my order" | Commerce | 0.6 |
| `checkout_help` | "checkout issue", "can't complete purchase" | Commerce | 0.6 |
| `multi_intent` | Multiple distinct intents in one query | Multi (Collaboration) | 0.7 |
| `unknown` | No recognized pattern | Fallback (default copilot) | — |

---

## Confidence Scoring

- **0.9 – 1.0:** Direct match — route without hesitation.
- **0.7 – 0.89:** Strong match — route but include disambiguation in response.
- **0.6 – 0.69:** Weak match — route and ask user for confirmation.
- **< 0.6:** Fallback — use the workspace's active copilot or default.

---

## Fallback Strategy

When no intent meets the minimum confidence threshold:

1. If the workspace has an **active copilot**, route to it.
2. If no active copilot, route to the **default copilot** (configured per workspace or globally — default is Commerce Copilot).
3. Include `routed: false` and `confidence` in the response so the UI can show a fallback indicator.

---

## Adding New Routing Rules

1. Add a new entry to the **routing rules table** above.
2. If keyword-based, add patterns to the **keyword index** in `routing-rules.json`.
3. If ML-based, add training samples and retrain the classifier.
4. Update the **routing rules config map** and restart the service (or hot-reload if supported).
5. Document the new intent in this file and in the **Developer Guide**.

Config file location: `config/routing-rules.json`

```json
{
  "rules": [
    {
      "intent": "new_intent_name",
      "patterns": ["keyword1", "keyword2"],
      "targetCopilot": "marketing|sales|service|commerce",
      "minConfidence": 0.6
    }
  ]
}
```
