# Recommendation Engine

**Version:** 0.2.0
**Last Updated:** 2026-07-23
**Module:** Customer Copilot Service

---

## 1. Overview

The Recommendation Engine is a multi-strategy scoring system that generates personalized product recommendations for SporeKart customers. It combines six distinct recommendation strategies into a weighted ensemble, producing scored, ranked product suggestions with explainable reasoning.

The engine is invoked via:
- `POST /api/v1/copilot/customer/recommend` — direct API call
- `CopilotEngine.processMessage()` — when user intent is "recommendation" or "discovery"

---

## 2. Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     RecommendationEngine                         │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ Personalized  │  │ContextAware  │  │  Seasonal    │          │
│  │   Strategy    │  │  Strategy    │  │  Strategy    │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
│         │                 │                 │                    │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐          │
│  │ LocationAware│  │  CrossSell   │  │   Upsell     │          │
│  │   Strategy   │  │  Strategy    │  │   Strategy   │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
│         │                 │                 │                    │
│         └─────────────────┼─────────────────┘                    │
│                           │                                      │
│                    ┌──────▼───────┐                              │
│                    │    Scorer    │                              │
│                    │  (Ensemble)  │                              │
│                    └──────┬───────┘                              │
│                           │                                      │
│                    ┌──────▼───────┐                              │
│                    │   Filter    │                              │
│                    │  (Availability,                              │
│                    │   Exclusions)│                              │
│                    └──────┬───────┘                              │
│                           │                                      │
│                    ┌──────▼───────┐                              │
│                    │   Rank &    │                              │
│                    │   Explain   │                              │
│                    └──────────────┘                              │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3. Strategy Definitions

### 3.1 Personalized Strategy (Weight: 0.30)

Recommends products based on the customer's purchase history and explicitly stated preferences.

**Inputs:**
- Purchase history (last 12 months) from Order Service
- Customer preferences (categories, price range) from CustomerContextService
- Previously viewed/browsed products
- Wishlist items

**Scoring:**
```
for each catalog product p:
    categoryMatch = jaccardSimilarity(p.categories, user.preferredCategories)
    priceFit = gaussianScore(p.price, user.avgOrderValue, user.priceStdDev)
    rebuyScore = 1.0 if p.id in user.purchaseHistory else decay(user.daysSinceLastPurchase)
    brandAffinity = user.brandsPurchased.contains(p.brand) ? 0.8 : 0.0
    score = (categoryMatch * 0.4) + (priceFit * 0.2) + (rebuyScore * 0.3) + (brandAffinity * 0.1)
    normalizedScore = normalize(score, 0, 100)
```

**Output:** Scored products (0-100) weighted by personal relevance.

### 3.2 Context-Aware Strategy (Weight: 0.20)

Recommends products related to the user's current page, category, or section.

**Inputs:**
- Current page URL and title from PageContext
- Current category or section
- Entity type and ID (e.g., product detail page → related items)
- Search query (if user is searching)

**Scoring:**
```
for each catalog product p:
    categoryRelevance = p.category == pageContext.category ? 1.0 : 0.3
    sectionRelevance = p.tags.contains(pageContext.section) ? 0.8 : 0.2
    queryRelevance = textSimilarity(p.name + p.description, searchQuery) if query exists
    relatedProduct = getRelatedProducts(pageContext.entityId).contains(p.id) ? 1.0 : 0.0
    score = max(categoryRelevance, sectionRelevance, queryRelevance, relatedProduct)
    normalizedScore = score * 100
```

**Output:** Scored products (0-100) weighted by contextual relevance.

### 3.3 Seasonal Strategy (Weight: 0.15)

Recommends products relevant to the current time of year, festivals, or agricultural seasons.

**Inputs:**
- Current date and month
- Seasonal calendar (mushroom growing seasons, harvest periods)
- Festival calendar (Diwali, Pongal, Onam, etc.)
- Promotional calendar (currently active campaigns)

**Seasonal Calendar:**
```
January-March:   Winter mushroom varieties (Shiitake, Oyster cool-season)
April-June:      Summer resistant strains, spawn for monsoon prep
July-September:  Monsoon varieties,雨季 kits, disease prevention
October-December:Year-end harvest supplies, festive gift packs
```

**Festival Mappings:**
```
Diwali (Oct-Nov):     Gift hampers, premium mushroom boxes, starter kits
Pongal (Jan):         Agricultural tools, spawn packs, training bundles
Onam (Aug-Sep):       Home cultivation kits, value-added products
New Year (Jan):       Goal-setting bundles, advanced training courses
```

**Scoring:**
```
for each catalog product p:
    seasonMatch = p.seasonalTags.contains(currentSeason) ? 1.0 : 0.0
    festivalMatch = p.festivalTags.contains(currentFestival) ? 1.0 : 0.0
    promotionMatch = p.isOnPromotion ? 0.5 : 0.0
    score = (seasonMatch * 0.5) + (festivalMatch * 0.3) + (promotionMatch * 0.2)
    normalizedScore = score * 100
```

**Output:** Scored products (0-100) weighted by seasonal/festival relevance.

### 3.4 Location-Aware Strategy (Weight: 0.10)

Recommends products that are available and suitable for the customer's geographic region.

**Inputs:**
- Customer's shipping address / region (from profile or IP geolocation)
- Regional product availability data
- Climate suitability for mushroom varieties
- Regional language preferences

**Regional Availability:**
```
North India:       Button mushroom kits, compost, cool-season spawn
South India:       Oyster mushroom kits, tropical varieties, summer spawn
East India:        Paddy straw mushroom, low-cost kits, training materials
West India:        Export-quality spawn, commercial equipment, packaging
```

**Scoring:**
```
for each catalog product p:
    regionalAvailability = p.availableRegions.contains(customerRegion) ? 1.0 : 0.0
    climateSuitability = p.recommendedClimates.contains(regionClimate) ? 1.0 : 0.0
    localLanguageContent = p.languages.contains(customerLanguage) ? 0.5 : 0.0
    score = (regionalAvailability * 0.5) + (climateSuitability * 0.3) + (localLanguageContent * 0.2)
    normalizedScore = score * 100
```

**Output:** Scored products (0-100) weighted by location relevance.

### 3.5 Cross-Sell Strategy (Weight: 0.15)

Recommends complementary products that pair well with items already in the cart or recently purchased.

**Inputs:**
- Current cart contents
- Recently purchased products (last 30 days)
- Product relationship graph (complementary mappings)

**Complementary Product Mappings:**
```
If customer has:            Recommend:
Spawn (mushroom seeds)      Substrate/bag, sterilization equipment, spray bottle
Growing kit                 Harvesting tools, storage bags, recipe book
Dried mushrooms             Rehydration pack, spice blend, gift box
Training course (basic)     Advanced course, live workshop, farm visit
Compost                     Casing soil, pH meter, thermometer
```

**Scoring:**
```
for each catalog product p:
    cartItemMatches = [complementaryProducts[cartItem.productId] for cartItem in cart]
    crossSellScore = p.id in cartItemMatches ? 1.0 : 0.0
    bundleScore = p.bundleWith in cart.productIds ? 0.8 : 0.0
    recencyBoost = 1.0 if p.lastPurchasedWith in recentPurchases else 0.0
    score = max(crossSellScore, bundleScore, recencyBoost)
    normalizedScore = score * 100
```

**Output:** Scored products (0-100) weighted by cross-sell potential.

### 3.6 Upsell Strategy (Weight: 0.10)

Recommends premium or higher-value alternatives to products the customer is viewing or has in their cart.

**Inputs:**
- Currently viewed product
- Cart items (targeting each item for upsell)
- Product tier hierarchy (standard → premium → pro)

**Tier Hierarchy:**
```
Standard                →   Premium                 →   Pro
──────────────────────────────────────────────────────────────
Basic spawn pack        →   Premium spawn pack      →   Commercial spawn pack
Home growing kit        →   Deluxe growing kit      →   Professional farm kit
Self-paced course       →   Mentor-guided course    →   Certification program
Manual spray bottle     →   Automated misting sys   →   Climate-controlled unit
```

**Scoring:**
```
for each catalog product p:
    tierPosition = getProductHierarchy(p.id)
    upsellCandidates = [cart item's upsell path for item in cart]
    isUpsell = p.id in upsellCandidates ? 1.0 : 0.0
    viewedUpsell = p.id in [viewed product's upsell path] ? 1.0 : 0.0
    pricePremium = (p.price - originalItem.price) / originalItem.price
    priceScore = sigmoid(pricePremium, midpoint=0.5) // favors moderate upsells
    score = (isUpsell * 0.4) + (viewedUpsell * 0.3) + (priceScore * 0.3)
    normalizedScore = score * 100
```

**Output:** Scored products (0-100) weighted by upsell opportunity.

---

## 4. Ensemble Scoring Algorithm

```
function scoreProducts(customerId, context, limit, excludeProductIds):

    // 1. Gather candidates from all strategies
    candidates = new HashMap<ProductId, ScoreAggregate>()

    for each strategy in [Personalized, ContextAware, Seasonal, LocationAware, CrossSell, Upsell]:
        results = strategy.execute(customerId, context)
        for each (productId, score) in results:
            aggregate = candidates.getOrCreate(productId)
            aggregate.addScore(strategy.name, score, strategy.weight)

    // 2. Ensemble calculation
    for each (productId, aggregate) in candidates:
        aggregate.finalScore = 0
        for each (strategyName, score, weight) in aggregate.scores:
            aggregate.finalScore += score * weight

    // 3. Filter
    candidates.removeAll(excludeProductIds)
    candidates.filter(p -> p.isAvailable && p.stockLevel > 0)

    // 4. Rank
    ranked = candidates.sortByDescending(aggregate.finalScore)

    // 5. Deduplicate
    seenCategories = new Set()
    ranked = ranked.filter(p -> {
        if seenCategories.contains(p.category) && seenCategories.size() >= 3:
            return false
        seenCategories.add(p.category)
        return true
    })

    // 6. Limit
    return ranked.take(limit)
```

### Scoring Example

| Strategy | Weight | Product A (Oyster Kit) | Product B (Compost) | Product C (Premium Spawn) |
|---|---|---|---|---|
| Personalized | 0.30 | 80 → 24.0 | 40 → 12.0 | 60 → 18.0 |
| Context-Aware | 0.20 | 90 → 18.0 | 30 → 6.0 | 50 → 10.0 |
| Seasonal | 0.15 | 70 → 10.5 | 60 → 9.0 | 80 → 12.0 |
| Location-Aware | 0.10 | 85 → 8.5 | 50 → 5.0 | 70 → 7.0 |
| Cross-Sell | 0.15 | 20 → 3.0 | 90 → 13.5 | 30 → 4.5 |
| Upsell | 0.10 | 10 → 1.0 | 20 → 2.0 | 85 → 8.5 |
| **Final Score** | | **65.0** | **47.5** | **60.0** |

Product A wins with highest ensemble score.

---

## 5. Explanation Generation

Each recommendation includes a human-readable `reason` field:

| Strategy | Example Reason |
|---|---|
| Personalized | "Based on your past purchases in Oyster mushrooms" |
| Context-Aware | "Since you're browsing growing supplies" |
| Seasonal | "Perfect for the current growing season" |
| Location-Aware | "Available in your region — Karnataka" |
| Cross-Sell | "Pairs well with the oyster spawn in your cart" |
| Upsell | "Premium upgrade to your basic kit — 40% more yield" |

---

## 6. Future ML Integration Points

| Phase | Enhancement | Description |
|---|---|---|
| Phase 1 | Collaborative Filtering | Matrix factorization on purchase history to find similar users |
| Phase 2 | Embedding-Based Similarity | Use product embeddings (from Semantic Platform) for semantic similarity |
| Phase 3 | Reinforcement Learning | Optimize strategy weights via bandit algorithms based on click/conversion data |
| Phase 4 | Real-Time Personalization | Update scores based on real-time browsing behavior (clickstream) |
| Phase 5 | Multi-Armed Bandit | Dynamically select best strategy per user session |
| Phase 6 | Contextual Bandits | Strategy selection conditioned on context (time, device, location) |
| Phase 7 | Deep Learning | Transformer-based recommendation model (user sequence → product) |
| Phase 8 | A/B Testing Framework | Built-in experimentation platform for strategy evaluation |

---

## 7. Performance Considerations

- **Caching strategy scores**: Pre-compute seasonal and location scores daily
- **Batch scoring**: Personalized scores computed asynchronously (cache TTL: 1 hour)
- **Real-time scoring**: Context-aware and cross-sell computed per-request (target: <200ms)
- **Cold start**: New users receive context-aware + seasonal recommendations until purchase history accumulates
- **Product catalog size**: Algorithm scales to 100K+ products with pre-filtering by category
