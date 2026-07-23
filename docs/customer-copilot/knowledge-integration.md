# Knowledge Integration Design

**Version:** 0.2.0
**Last Updated:** 2026-07-23
**Module:** Customer Copilot Service → Knowledge Platform

---

## 1. Overview

The Customer Copilot integrates with the **SporeKart Knowledge Platform** to provide grounded, citation-backed answers to user queries. The Knowledge Platform is the centralized document management and RAG (Retrieval-Augmented Generation) system that stores, chunks, and retrieves enterprise knowledge documents.

All knowledge queries flow through the following pipeline:

```
User Query → Customer Copilot → Knowledge Platform → Semantic Platform → RAG Response → Citations
```

The grounding principle is: **never hallucinate — every factual claim must be backed by a retrievable citation.**

---

## 2. Integration Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Customer Copilot Service                        │
│                                                                       │
│  User Message: "how to grow shiitake?"                               │
│       │                                                               │
│       ▼                                                               │
│  IntentRouter → "knowledge_query"                                    │
│       │                                                               │
│       ▼                                                               │
│  KnowledgeQueryTool                                                   │
│       │                                                               │
│  ┌────┴──────────────────────────────────────────────────────────┐   │
│  │  POST /api/v1/knowledge/retrieve                              │   │
│  │  {                                                             │   │
│  │    "query": "how to grow shiitake mushrooms",                 │   │
│  │    "category": "cultivation-guides",                           │   │
│  │    "language": "en",                                           │   │
│  │    "maxResults": 5                                             │   │
│  │  }                                                             │   │
│  └────────────────────────────────────────────────────────────────┘   │
│       │                                                               │
│       ▼                                                               │
│  Response: { documents, chunks, citations }                          │
│       │                                                               │
│       ▼                                                               │
│  Response Formatter                                                    │
│  - Build answer from citations                                        │
│  - Inline citation markers [1], [2]                                   │
│  - Sources section at end                                             │
│       │                                                               │
│       ▼                                                               │
│  ChatResponse with grounded answer                                    │
└─────────────────────────────────────────────────────────────────────┘
         │
         │  HTTPS / Internal Network
         ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Knowledge Platform (ai-service)                 │
│                                                                       │
│  KnowledgeRetrievalService.retrieve()                                 │
│       │                                                               │
│       ├── 1. Filter by category, language, visibility                │
│       ├── 2. Keyword match on document content                       │
│       ├── 3. Select chunks (max 3 per document)                      │
│       ├── 4. Optional: semantic search via Semantic Platform         │
│       ├── 5. Build citations with excerpts                           │
│       ├── 6. Log access (DOCUMENT_RETRIEVED)                         │
│       └── 7. Return RetrieveResponse                                 │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  Semantic Platform (optional, for semantic retrieval)         │   │
│  │  - Embed query via configured provider                        │   │
│  │  - Vector similarity search                                   │   │
│  │  - Hybrid search (keyword + semantic) with RRF fusion         │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  Data Stores:                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │ PostgreSQL   │  │ Redis Cache  │  │ Vector DB    │              │
│  │ (documents,  │  │ (retrieval   │  │ (pgvector /  │              │
│  │  chunks,     │  │  results)    │  │  Pinecone)   │              │
│  │  citations)  │  │              │  │              │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Query Flow Details

### Step 1: Intent Detection

The `IntentRouter` classifies user messages to identify knowledge queries:

| Intent Keywords | Example |
|---|---|
| "how to", "what is", "guide", "tips" | "how to grow shiitake" |
| "explain", "tell me about", "steps" | "explain the spawning process" |
| "difference between", "compare" | "difference between oyster and shiitake" |
| "best", "recommended", "should I" | "what's the best substrate for button mushrooms" |
| "why", "when", "where" | "why is my mushroom spawn not colonizing" |

### Step 2: Query Construction

The `KnowledgeQueryTool` constructs the retrieval request:

```json
{
  "query": "how to grow shiitake mushrooms",
  "filters": {
    "category": ["cultivation-guides", "faq", "troubleshooting"],
    "language": "en",
    "visibility": "PUBLIC",
    "maxResults": 5
  }
}
```

### Step 3: Knowledge Platform Retrieval

The Knowledge Platform executes the retrieval pipeline:
1. **Category filter** — restricts to relevant document categories
2. **Language filter** — matches document language
3. **Visibility filter** — only PUBLIC and INTERNAL documents (not RESTRICTED/CONFIDENTIAL)
4. **Keyword matching** — searches title, description, and content fields
5. **Chunk selection** — selects up to 3 most relevant chunks per document
6. **Citation building** — creates citations with source, excerpt, and URL

### Step 4: Response Formatting

The formatter builds the response:

```
Answer with inline citations [1], [2], [3]

Key steps or takeaways (summarized from chunks)

Sources:
[1] Document Title — Source — URL
[2] Document Title — Source — URL
[3] Document Title — Source — URL
```

### Step 5: Grounding Validation

Before returning the response, the system validates:
- Each factual claim maps to at least one citation
- Citation excerpts support the claim made
- No claims are made without retrievable evidence

If validation fails, the response falls back to:
```
"I don't have enough information to answer that question confidently.
Please contact our support team or try rephrasing your question."
```

---

## 4. Citation Format

### Inline Citations

Citations use numbered brackets inline in the response text:

```
Shiitake mushrooms grow best on hardwood logs such as oak or maple [1].
The logs should be 3-6 inches in diameter and cut in late winter [2].
After inoculation, the spawn run takes 6-12 months depending on conditions [1][3].
```

### Citation Entry

Each citation entry in the sources section contains:

| Field | Description | Example |
|---|---|---|
| Number | Sequential citation number | [1] |
| Title | Document title | Shiitake Cultivation Guide |
| Source | Source attribution | SporeKart Knowledge Base |
| Snippet | Relevant excerpt | "Shiitake (Lentinula edodes)..."
| URL | Link to full document | https://sporekart.com/knowledge/shiitake |
| Relevance | Score 0.0-1.0 | 0.95 |

### Rendering in CopilotPanel

The frontend `CopilotPanel` renders citations as:
- Superscript links `[1]` in the response text
- Expandable sources section at the bottom
- Clicking a citation opens the source document in a new tab

---

## 5. Supported Knowledge Categories

| Category | Description | Document Types |
|---|---|---|
| `cultivation-guides` | Step-by-step growing instructions | PDF, MARKDOWN |
| `faq` | Frequently asked questions | MARKDOWN |
| `troubleshooting` | Problem diagnosis and solutions | MARKDOWN |
| `product-guides` | Product usage instructions | PDF, MARKDOWN |
| `recipes` | Mushroom cooking recipes | MARKDOWN |
| `business-guides` | Commercial farming guides | PDF, MARKDOWN |
| `safety` | Safety and handling guidelines | PDF |
| `regulatory` | Government regulations and compliance | PDF |
| `research` | Research papers and studies | PDF |
| `training-materials` | Course companion materials | MARKDOWN, PDF |
| `seasonal-tips` | Season-specific growing advice | MARKDOWN |
| `regional-guides` | Region-specific cultivation advice | MARKDOWN |

---

## 6. Grounding Strategy

### Core Principles

1. **Always return citations** — Every factual response must include at least one citation
2. **Never hallucinate** — If information is not in retrieved documents, do not fabricate
3. **Confidence threshold** — Only use chunks with relevance score > 0.6
4. **Source attribution** — Always name the source of information
5. **Version awareness** — Use the latest version of each document
6. **Language match** — Only retrieve documents in the user's language

### Grounding Decision Tree

```
User Query
    │
    ▼
Knowledge Platform Retrieval
    │
    ├── Results found (relevance ≥ 0.6) ──→ Build grounded response with citations
    │
    ├── Results found (relevance < 0.6) ──→ Use best available chunks
    │                                       Include confidence disclaimer
    │                                       "Based on available information..."
    │
    ├── Partial results ──────────────────→ Answer with what is known
    │                                       "I found information about X, but not Y."
    │                                       "Would you like me to connect you with an expert?"
    │
    └── No results ──────────────────────→ Polite disclaimer
                                            "I don't have information about that yet."
                                            "Our knowledge base is growing — suggest this topic?"
                                            → Suggest alternative: training, support, or expert contact
```

### Fallback Responses

| Scenario | Fallback |
|---|---|
| No relevant documents | "I couldn't find specific information about that. Would you like me to connect you with our support team?" |
| Low confidence | "Based on the available information, here's what I can tell you..." |
| Language unavailable | "I don't have information in your preferred language yet. Here are English results." |
| Category unavailable | "I don't have information in that category yet. Try searching our training courses instead." |

---

## 7. Government Agricultural Content Integration

### Source Types

The Knowledge Platform ingests content from Indian government agricultural sources:

| Source | Content Type | Update Frequency |
|---|---|---|
| ICAR (Indian Council of Agricultural Research) | Technical bulletins, research papers | Quarterly |
| NHB (National Horticulture Board) | Cultivation guidelines, subsidy info | Monthly |
| State Agricultural Universities | Regional cultivation guides | As published |
| DAC&FW (Department of Agriculture) | Policy documents, schemes | Monthly |
| MSAMB (Maharashtra State Agril. Marketing Board) | Market prices, trends | Daily |
| KAU (Kerala Agricultural University) | Tropical cultivation guides | As published |
| UAS (University of Agricultural Sciences) | Karnataka-specific guides | As published |

### Integration Flow

```
Government PDF
    │
    ▼
Knowledge Platform Ingestion Pipeline:
    ├── 1. Document upload (PDF/MARKDOWN)
    ├── 2. Content validation & categorization
    ├── 3. Language detection (English, Hindi, Kannada)
    ├── 4. Chunking (1000 chars, 100 overlap)
    ├── 5. Embedding generation (via Semantic Platform)
    ├── 6. Index storage (pgvector)
    └── 7. Publish (visibility: PUBLIC)
```

### Government Content Retrieval

Government documents are tagged with:
- `source: "ICAR"`, `source: "NHB"`, etc.
- `government: true`
- `language: "en"`, `language: "kn"`, etc.
- `region`: applicable geographic region

When the copilot retrieves government content, the citation includes:
```
[1] ICAR Technical Bulletin: Mushroom Production — ICAR, 2024
    https://icar.gov.in/mushroom-production-guide
```

---

## 8. Multilingual Knowledge Support (Kannada-Ready)

### Language Strategy

| Language | Code | Status |
|---|---|---|
| English | `en` | Active — full support |
| Kannada | `kn` | Ready — documents being ingested |
| Hindi | `hi` | Planned — Phase 2 |

### Language Detection

The copilot detects the user's language from:
1. `UserContext.language` — from profile preferences
2. Message content analysis — basic language detection
3. Falls back to `en` if undetermined

### Kannada Knowledge Retrieval

When a user's language is Kannada:

1. Query is sent in Kannada script
2. Knowledge Platform filters by `language: "kn"`
3. Kannada documents are retrieved and chunked
4. Response is formatted in Kannada
5. Citations include Kannada-titled sources

```json
{
  "query": "ಶಿಟೇಕ್ ಅಣಬೆ ಬೆಳೆಯುವುದು ಹೇಗೆ?",
  "language": "kn",
  "filters": { "category": "cultivation-guides" }
}
```

### Cross-Language Fallback

If Kannada documents are not available:
- Return English documents with a note
- Offer to translate or find Kannada content
- Log the gap for content team awareness

```
"I don't have Kannada content for this topic yet.
Here are the available English resources.
ನಿಮ್ಮ ಭಾಷೆಯಲ್ಲಿ ಈ ಮಾಹಿತಿ ಲಭ್ಯವಿಲ್ಲ.
Would you like me to notify you when Kannada content is added?"
```

---

## 9. Performance Considerations

### Caching

| Cache | TTL | Scope |
|---|---|---|
| Knowledge retrieval results | 10 minutes | Per query hash |
| Citation metadata | 15 minutes | Per document ID |
| Category listings | 60 minutes | Global |

### Latency Targets

| Operation | Target | Degraded |
|---|---|---|
| Knowledge retrieval | < 500ms | < 2000ms |
| Citation building | < 50ms | < 200ms |
| Semantic search (optional) | < 1000ms | < 3000ms |

### Rate Limiting

| Source | Limit | Burst |
|---|---|---|
| Knowledge Platform API | 100 req/min per tenant | 200 |
| Semantic Platform API | 50 req/min per tenant | 100 |

---

## 10. Monitoring & Quality

### Metrics

| Metric | Description | Alert |
|---|---|---|
| `knowledge.retrieval.count` | Total retrievals | - |
| `knowledge.retrieval.latency` | Retrieval latency in ms | > 1000ms |
| `knowledge.retrieval.no_results` | Queries with zero results | > 10% rate |
| `knowledge.retrieval.low_confidence` | Queries below confidence threshold | > 5% rate |
| `knowledge.citation.count` | Average citations per response | < 1.0 |

### Quality Checks

- **Grounding rate**: % of responses with citations (target: > 95%)
- **Citation accuracy**: Verified citations (sampled weekly)
- **Coverage**: % of unique queries with successful retrieval
- **Language coverage**: % of non-English queries answered
