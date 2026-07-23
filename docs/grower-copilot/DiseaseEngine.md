# Disease Advisory Engine

## Supported Diseases (15+)

| # | Disease | Pathogen Type | Affected Species | Primary Symptoms |
|---|---------|---------------|------------------|------------------|
| 1 | Dry Bubble (_Verticillium fungicola_) | Fungal | White button, Oyster | Brown spots, necrotic lesions on cap |
| 2 | Wet Bubble (_Mycogone perniciosa_) | Fungal | White button | Amber droplets, soft rot, malformed fruiting bodies |
| 3 | Green Mold (_Trichoderma harzianum_) | Fungal | All species | Green sporulation on compost, yield loss |
| 4 | Cobweb Mold (_Cladobotryum dendroides_) | Fungal | White button, Oyster | Cottony white mycelium, cobweb-like growth |
| 5 | Bacterial Blotch (_Pseudomonas tolaasii_) | Bacterial | White button, Oyster | Yellow/brown sunken lesions, foul smell |
| 6 | Brown Blotch (_Pseudomonas agarici_) | Bacterial | White button | Brown discoloration on cap surface |
| 7 | Soft Rot (_Burkholderia gladioli_) | Bacterial | White button | Water-soaked lesions, complete tissue breakdown |
| 8 | Fire Blight (_Pseudomonas fluorescens_) | Bacterial | Oyster, Shiitake | Rapid browning, leaf-like tissue necrosis |
| 9 | Pink Mold (_Neurospora crassa_) | Fungal | Compost only | Pink-orange sporulation, compost overheating |
| 10 | Mushroom Virus X (MVX) | Viral | White button | Delayed pinning, malformed caps, brown discoloration |
| 11 | Dieback (_Trichoderma aggressivum_) | Fungal | White button | Compost degradation, pinhead abortion |
| 12 | Bacterial Pit (_Bacillus spp._) | Bacterial | All species | Small pits on stipe and cap, internal discoloration |
| 13 | Black Mold (_Aspergillus niger_) | Fungal | Oyster, Shiitake | Black sporulation, respiratory risk to workers |
| 14 | Mildew (_Erysiphe spp._) | Fungal | Oyster | White powdery coating on caps |
| 15 | Yellow Mold (_Myceliophthora lutea_) | Fungal | Compost | Yellow patches in compost, delayed fruiting |
| 16 | Sooty Mold (_Capnodium spp._) | Fungal | Shiitake | Black sooty coating, reduced photosynthesis in log |

## Symptom Matching

The symptom matching pipeline uses a hybrid approach:

1. **Text Normalization** — User-described symptoms are parsed and normalized against a controlled vocabulary.
2. **Keyword Extraction** — Key symptom terms are extracted (e.g., "brown spots", "soft rot", "green mold").
3. **Fuzzy Matching** — Levenshtein-based matching against disease symptom profiles.
4. **Confidence Scoring** — Each match scored 0.0–1.0 based on symptom overlap.
5. **Ranked Results** — Top-3 matches returned with confidence scores.

### Match Confidence Thresholds

| Score Range | Classification | Action |
|-------------|----------------|--------|
| 0.80–1.00 | High confidence | Auto-diagnose with treatment |
| 0.50–0.79 | Moderate confidence | Suggest top match + differential |
| 0.00–0.49 | Low confidence | Request more details, escalate |

## Severity Classification

| Class | Criteria | Response |
|-------|----------|----------|
| Mild | Localized symptoms, <10% affected | Self-care treatment plan |
| Moderate | Spreading, 10–30% affected | Treatment + monitoring schedule |
| Severe | >30% affected, crop-threatening | Immediate treatment + escalation |
| Critical | Total crop risk, potential facility contamination | Emergency escalation to horticulture officer |

## Treatment Recommendations

Treatments are structured with three tiers:

- **Chemical** — Approved fungicides/bactericides (e.g., Carbendazim, Copper Oxychloride), dosage, application method, withholding period.
- **Biological** — Beneficial microbes (e.g., _Trichoderma viride_, _Pseudomonas fluorescens_), application timing.
- **Cultural** — Hygiene protocols, airflow adjustment, temperature/humidity correction, compost management.
- **Integrated** — Combined approach tailored to severity and growth stage.

All recommendations include:
- Trade names available in Indian market
- Dosage per square meter / per kg compost
- Safety interval (days before harvest)
- Personal protective equipment (PPE) requirements

## Prevention

- **Sanitation** — Foot baths, sterilized tools, facility disinfection protocol
- **Environmental control** — Temperature (22–28°C), humidity (80–90%), CO₂ (<1000 ppm)
- **Compost quality** — Proper pasteurization (58–60°C for 6 hrs), pH 7.0–7.5
- **Spawn quality** — Certified disease-free spawn from approved labs
- **Monitoring** — Daily visual inspection, log-keeping, early detection triggers

## Escalation Criteria

Escalation is triggered when ANY of the following is true:

1. **Severity classification** is Critical or Severe with >30% spread
2. **Unknown pathogen** — no match above 0.50 confidence
3. **Recurrence** — same disease in same facility within 30 days
4. **Novel outbreak** — disease not in knowledge base
5. **Worker safety** — suspected mycotoxin or respiratory hazard

Escalation sends an alert to:
- Grower (SMS/in-app notification)
- Regional horticulture officer (email)
- SporeKart disease monitoring team (dashboard)

## Future: Image Diagnosis Architecture (Planned)

```
  Image Capture (Mobile)
       │
       ▼
  Image Preprocessor ──► Resize, normalize, augment
       │
       ▼
  CNN Classifier ──► MobileNet / EfficientNet fine-tuned on mushroom disease dataset
       │
       ├──► High confidence ──► Diagnosis + treatment
       └──► Low confidence  ──► Human expert review queue
```

Image diagnosis will be introduced in Phase 2 with:
- On-device inference via TensorFlow Lite
- Federated learning for privacy-preserving model improvement
- Human-in-the-loop validation for low-confidence predictions
- Integration with the symptom text pipeline for multi-modal diagnosis
