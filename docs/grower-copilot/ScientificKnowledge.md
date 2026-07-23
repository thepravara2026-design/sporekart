# Scientific Knowledge Platform Integration

## Overview

The Scientific Knowledge engine provides access to a curated repository of 40+ knowledge entries covering mushroom science, cultivation best practices, disease management, and regulatory guidelines specific to Indian horticulture.

## Knowledge Entry Structure

Each entry follows a standardized format:

```json
{
  "id": "SK-024",
  "title": "Integrated disease management for white button mushroom",
  "type": "practice_guide",
  "category": "disease_management",
  "species": ["white_button", "oyster"],
  "tags": ["idm", "sanitation", "fungicide", "biological_control"],
  "summary": "...",
  "content": "...",
  "citations": ["Singh et al. 2021", "DMR Solan Guide 2022"],
  "source": "Directorate of Mushroom Research, Solan",
  "language": "en",
  "version": 3,
  "lastUpdated": "2026-03-15",
  "applicableRegions": ["himachal", "uttarakhand", "punjab", "haryana"]
}
```

## Entry Categories

| Category | Count | Description |
|----------|-------|-------------|
| disease_management | 12 | Disease identification, treatment, IDM strategies |
| cultivation_practices | 10 | Substrate prep, composting, casing, harvesting |
| spawn_technology | 5 | Spawn production, quality testing, storage |
| post_harvest | 4 | Storage, packaging, shelf-life extension, value addition |
| nutrition | 3 | Nutritional composition, health benefits |
| regulatory | 4 | FSSAI standards, organic certification, subsidy schemes |
| economics | 3 | Cost of cultivation, market analysis, export potential |
| climate_adaptation | 3 | Climate-resilient practices, polyhouse design |

## Citation Format

All knowledge entries include citations in a standardized format:

- **Research Papers:** `Author(s). (Year). Title. Journal, Volume(Issue), Pages. DOI`
- **Government Publications:** `Department/Agency. (Year). Title. Publication ID.`
- **Conference Proceedings:** `Author(s). (Year). Title. In: Proceedings of Conference Name, Pages.`
- **Extension Bulletins:** `Publisher. (Year). Title. Bulletin Number.`

Inline references use the format `[Author Year]` (e.g., `[Sharma 2022]`) with full bibliography at the end of each entry.

## Scientific References

The knowledge base draws from the following key sources:

| Source | Type | Focus Area |
|--------|------|------------|
| Directorate of Mushroom Research (DMR), Solan | Government | All aspects of mushroom cultivation |
| ICAR-Indian Agricultural Research Institute | Research | Disease management, spawn technology |
| National Horticulture Board (NHB) | Government | Economics, market data |
| Indian Phytopathological Society | Journal | Disease epidemiology |
| Mushroom Society of India | Conference | Recent advances |
| FSSAI | Regulatory | Food safety standards |
| State Agricultural Universities (HPKV, UAHS, TNAU) | Research | Regional cultivation practices |
| FAO Technical Papers | International | Best practice guidelines |

## Government Guidelines

### Key Regulatory Documents

| Document | Issuer | Relevance |
|----------|--------|-----------|
| Mushroom Cultivation Guide (2020) | DMR Solan | Complete cultivation reference |
| FSSAI Mushroom Standards (2021) | FSSAI | Quality and safety compliance |
| NHB Mushroom Database (Annual) | NHB | Price trends, production data |
| MIDH Guidelines (2022) | Ministry of Agriculture | Subsidy schemes for mushroom units |
| Spawn Certification Protocol (2019) | DMR Solan | Quality assurance for spawn producers |

## Query Interface

The knowledge engine supports:

- **Keyword search** — Full-text search across titles, summaries, content, and tags
- **Category filter** — Narrow by category
- **Species filter** — Filter by affected mushroom species
- **Region filter** — Filter by applicable Indian region
- **Semantic search** — Vector-based similarity search (planned, Phase 2)
- **Q&A mode** — Extract answer from relevant entry using the LLM (planned, Phase 2)

## Integration with Disease Engine

When a disease diagnosis is returned, the Disease Engine automatically attaches relevant knowledge entries:

- Matching disease entries (symptoms, treatment)
- Related species entries
- Regional practice guides
- Regulatory requirements for chemical use

## Localization

- English: All 40+ entries available
- Hindi: 25 priority entries translated (Phase 1)
- Other regional languages: Planned for Phase 2

## Curation Workflow

1. **Source identification** — Domain experts identify new research/government publications
2. **Entry creation** — Standardized format with full citations
3. **Review** — Two peer reviewers from the SporeKart scientific advisory board
4. **Approval** — Sign-off by the lead mycologist
5. **Publishing** — Entry indexed and available via API
6. **Versioning** — Each update increments version number; old versions retained for audit
