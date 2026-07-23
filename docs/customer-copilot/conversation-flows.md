# Conversation Flows

**Version:** 0.2.0
**Last Updated:** 2026-07-23
**Module:** Customer Copilot Service

---

## Flow 1: Product Search

**User Intent:** "show me oyster mushrooms"

### Flow Steps

```
User: "show me oyster mushrooms"
  │
  ├── IntentRouter.identify("product_search")
  │     Keywords: ["show", "find", "search", "looking for", "need"]
  │     Entities: { query: "oyster mushrooms", type: "product_search" }
  │
  ├── Context Assembly
  │     ├── User: profile, journey stage
  │     ├── Page: current URL/category
  │     └── History: previous searches
  │
  ├── Capability: Product Search & Discovery
  │     │
  │     ├── Tool: ProductSearchTool
  │     │     ├── Call Catalog Service: GET /products?q=oyster+mushrooms
  │     │     ├── Apply filters (price, category, availability)
  │     │     └── Return ProductItem[]
  │     │
  │     └── If category matches:
  │           ├── Tool: RecommendationEngine (context-aware)
  │           └── Attach related category recommendations
  │
  ├── Response Formatting
  │     ├── Primary: List matching products with prices and availability
  │     └── Secondary: Category filters, sorting options
  │
  └── Follow-up Suggestions
        ├── "Filter by price range"
        ├── "Show me growing kits"
        ├── "Compare oyster mushroom varieties"
        └── "What training is available for oyster mushrooms?"
```

### Example Response

```
I found 12 oyster mushroom products:

1. **Oyster Mushroom Spawn (100g)** — ₹299 — In Stock
2. **Oyster Mushroom Grow Kit** — ₹1,299 — In Stock
3. **Dried Oyster Mushrooms (500g)** — ₹599 — In Stock
...

Would you like to narrow by price range, compare varieties, or see growing guides?
```

---

## Flow 2: Recommendations

**User Intent:** "what should I grow?"

### Flow Steps

```
User: "what should I grow?"
  │
  ├── IntentRouter.identify("recommendation")
  │     Keywords: ["recommend", "suggest", "what should I", "what's good"]
  │     Entities: { intent: "discovery", type: "recommendation" }
  │
  ├── Context Assembly
  │     ├── User profile (preferences, location, experience level)
  │     ├── Purchase history
  │     ├── Seasonal calendar (current month)
  │     └── Journey stage (new/browsing/returning)
  │
  ├── Capability: Personalized Recommendations
  │     │
  │     ├── Tool: RecommendationEngine
  │     │     ├── PersonalizedStrategy (purchase history + preferences)
  │     │     ├── SeasonalStrategy (current season crops)
  │     │     ├── LocationAwareStrategy (regional suitability)
  │     │     └── Ensemble scorer → top recommendations
  │     │
  │     └── Return ProductRecommendation[] with scores and reasons
  │
  ├── Response Formatting
  │     ├── Primary: Top 5 recommendations with reasons
  │     └── Secondary: "Why this is recommended for you"
  │
  └── Follow-up Suggestions
        ├── "Tell me more about [product]"
        ├── "Show beginner-friendly options"
        ├── "What do I need to get started?"
        └── "Add [product] to cart"
```

### Example Response

```
Based on your profile and the current season, here's what I recommend:

1. **Oyster Mushroom Grow Kit** ⭐ — Great for beginners, perfect for monsoon season
2. **Premium Spawn Pack (Oyster)** — You've grown this before, time to scale up
3. **Shiitake Log Set** — Available in your region (Karnataka), ideal for Jul-Sep

Want me to help you get started with a beginner bundle?
```

---

## Flow 3: Order Tracking

**User Intent:** "where is my order?"

### Flow Steps

```
User: "where is my order?"
  │
  ├── IntentRouter.identify("order_tracking")
  │     Keywords: ["order", "tracking", "shipped", "delivery", "where is"]
  │     Entities: { intent: "order_status", orderId?: extracted }
  │
  ├── Context Assembly
  │     ├── User profile (customerId)
  │     ├── Active orders from Order Service
  │     └── Recent order IDs from conversation history
  │
  ├── Capability: Order Management & Tracking
  │     │
  │     ├── Tool: OrderTrackingTool
  │     │     ├── Call Order Service: GET /orders?customerId={id}
  │     │     ├── If orderId provided: GET /orders/{orderId}
  │     │     └── Return CustomerOrder[] with status and tracking info
  │     │
  │     └── If multiple orders found:
  │           ├── List active orders
  │           └── Ask which one to track
  │
  ├── Response Formatting
  │     ├── Order status with visual indicator (e.g., [=====>---] 70%)
  │     ├── Estimated delivery date
  │     ├── Current location (if tracking available)
  │     └── Tracking link
  │
  └── Follow-up Suggestions
        ├── "Track another order"
        ├── "Update delivery address"
        ├── "Start a return"
        └── "Contact support about this order"
```

### Example Response

```
Your order #SPK-2024-07842 is **out for delivery**! 🚚

Items: Oyster Mushroom Spawn (2x), Grow Kit (1x)
Estimated delivery: Today by 6 PM
Tracking: https://track.sporekart.com/SPK-2024-07842

Would you like to track another order or need help with something else?
```

---

## Flow 4: Training Guidance

**User Intent:** "beginner courses"

### Flow Steps

```
User: "beginner courses"
  │
  ├── IntentRouter.identify("training")
  │     Keywords: ["course", "training", "learn", "workshop", "class", "tutorial"]
  │     Entities: { level: "beginner", type: "training" }
  │
  ├── Context Assembly
  │     ├── User profile (experience level, region)
  │     ├── Past enrollments
  │     └── Language preference
  │
  ├── Capability: Training & Tutorial Access
  │     │
  │     ├── Tool: TrainingSearchTool
  │     │     ├── Call Training Service: GET /training?level=BEGINNER
  │     │     ├── Filter by language, region
  │     │     └── Return TrainingCourse[]
  │     │
  │     └── If user has no history:
  │           ├── Recommend foundational courses
  │           └── Suggest starting with free content
  │
  ├── Response Formatting
  │     ├── Course list with title, duration, instructor, next batch date
  │     ├── Level indicator (Beginner / Intermediate / Advanced)
  │     └── Enrollment status and link
  │
  └── Follow-up Suggestions
        ├── "Enroll in [course]"
        ├── "Show intermediate courses"
        ├── "What's covered in this course?"
        └── "Are there courses in Kannada?"
```

### Example Response

```
Here are beginner-friendly courses:

1. **Mushroom Cultivation 101** — 4 weeks — ₹999 — Next batch: Aug 5
   Learn the fundamentals: spawn production, substrate prep, harvesting
   
2. **Oyster Mushroom Starter** — 2 weeks — Free — Start anytime
   Quick-start guide specifically for oyster mushrooms

3. **Farm Setup for Beginners** — 6 weeks — ₹2,499 — Next batch: Aug 12
   From backyard to small-scale commercial farm

Would you like to enroll in any of these?
```

---

## Flow 5: Knowledge Query

**User Intent:** "how to grow shiitake?"

### Flow Steps

```
User: "how to grow shiitake?"
  │
  ├── IntentRouter.identify("knowledge_query")
  │     Keywords: ["how to", "what is", "guide", "tips", "explain", "steps"]
  │     Entities: { query: "grow shiitake", type: "knowledge" }
  │
  ├── Context Assembly
  │     ├── User profile (language, region)
  │     ├── Current page context (if on knowledge section)
  │     └── Previous knowledge queries
  │
  ├── Capability: Knowledge Base Search
  │     │
  │     ├── Tool: KnowledgeQueryTool
  │     │     ├── Call Knowledge Platform: POST /api/v1/knowledge/retrieve
  │     │     │     { query: "how to grow shiitake mushrooms", language: "en" }
  │     │     ├── Knowledge Platform performs:
  │     │     │     ├── Keyword matching on document content
  │     │     │     ├── Chunk selection (max 3 per document)
  │     │     │     └── Citation building with excerpts
  │     │     ├── Return KnowledgeArticle[] with citations
  │     │     └── If no results: fallback to general knowledge
  │     │
  │     └── Grounding check:
  │           ├── Always verify response is grounded in retrieved citations
  │           └── Never hallucinate — if no citations found, state limitation
  │
  ├── Response Formatting
  │     ├── Answer with inline citations [1], [2], [3]
  │     ├── Citations section with source and URL
  │     └── Key steps or takeaways
  │
  └── Follow-up Suggestions
        ├── "Show me a step-by-step guide"
        ├── "What equipment do I need?"
        ├── "Are there video tutorials?"
        └── "Where can I buy shiitake spawn?"
```

### Example Response

```
Here's how to grow shiitake mushrooms:

**Step 1: Get Shiitake Spawn** — Purchase high-quality shiitake spawn from a reputable supplier [1].

**Step 2: Prepare Logs or Substrate** — Shiitake grows best on hardwood logs (oak, maple) or sawdust blocks. Logs should be 3-6 inches in diameter and cut in late winter [2].

**Step 3: Inoculate** — Drill holes in a diamond pattern, insert spawn, and seal with wax [1].

**Step 4: Incubate** — Stack logs in a shady, moist area. Shiitake mycelium colonizes in 6-12 months [2].

**Step 5: Fruit** — Soak logs in cold water for 24 hours to trigger fruiting. Harvest in 7-10 days [3].

Sources:
[1] SporeKart Knowledge Base: Shiitake Cultivation Guide
[2] ICAR Technical Bulletin: Mushroom Production
[3] SporeKart Training Module: Advanced Mushroom Farming
```

---

## Flow 6: Cart Assistance

**User Intent:** "help with checkout"

### Flow Steps

```
User: "help with checkout"
  │
  ├── IntentRouter.identify("cart_assistance")
  │     Keywords: ["cart", "checkout", "buy", "purchase", "order"]
  │     Entities: { intent: "checkout_help", type: "cart" }
  │
  ├── Context Assembly
  │     ├── Cart contents from CustomerContextService
  │     ├── Cart total, item count
  │     ├── Available promotions
  │     └── User profile (address, payment methods)
  │
  ├── Capability: Shopping Cart Assistance
  │     │
  │     ├── Tool: CartAnalysisTool
  │     │     ├── Analyze cart (items, total, missing essentials)
  │     │     ├── Check for applicable promotions
  │     │     └── Cross-sell opportunities
  │     │
  │     └── If checkout initiated:
  │           ├── Verify address completeness
  │           ├── Suggest shipping options
  │           └── Apply available coupon codes
  │
  ├── Response Formatting
  │     ├── Cart summary
  │     ├── Promotion applied (if any)
  │     ├── Cross-sell suggestions (optional)
  │     └── Checkout readiness status
  │
  └── Follow-up Suggestions
        ├── "Proceed to checkout"
        ├── "Add [cross-sell product]"
        ├── "Apply coupon code"
        └── "Clear cart and start over"
```

### Example Response

```
Your cart is ready for checkout:

🛒 Cart Summary (3 items):
- Oyster Mushroom Spawn (100g) × 2 = ₹598
- Grow Kit (Standard) × 1 = ₹899
- Spray Bottle × 1 = ₹149
**Total: ₹1,646**

🎉 Good news! You qualify for free shipping (orders above ₹1,500).
💡 Tip: Add substrate bags (₹299) — they pair perfectly with your spawn.

Ready to checkout? I can help with addresses and payment.
```

---

## Flow 7: Cross-Sell/Upsell

**User Intent:** "what goes with this?"

### Flow Steps

```
User: "what goes with this?"
  │
  ├── IntentRouter.identify("cross_sell_upsell")
  │     Keywords: ["goes with", "compatible", "also need", "accessories"]
  │     Entities: { type: "cross_sell", productId?: extracted }
  │
  ├── Context Assembly
  │     ├── Current product being viewed (from PageContext)
  │     ├── Cart contents
  │     └── Purchase history
  │
  ├── Capability: Product Comparisons / Cross-sell & Upsell
  │     │
  │     ├── Tool: CrossSellTool
  │     │     ├── Get complementary product mappings
  │     │     └── Return ProductItem[]
  │     │
  │     ├── Tool: UpsellTool
  │     │     ├── Get premium tier alternatives
  │     │     └── Return ProductItem[]
  │     │
  │     └── Tools executed in parallel
  │
  ├── Response Formatting
  │     ├── Cross-sell: "You'll also need..."
  │     ├── Upsell: "Consider upgrading to..."
  │     └── Bundle savings (if applicable)
  │
  └── Follow-up Suggestions
        ├── "Add all to cart"
        ├── "Compare premium vs standard"
        ├── "Tell me more about [product]"
        └── "Show me the complete starter bundle"
```

### Example Response

```
Great question! Here's what pairs well with the **Oyster Mushroom Spawn**:

**You'll also need:**
1. **Substrate Bags (5-pack)** — ₹499 — Essential for growing
2. **Sterilization Kit** — ₹799 — Prevent contamination
3. **Mist Spray Bottle** — ₹149 — Maintain humidity

**Consider upgrading to:**
🌟 **Premium Spawn Pack** (₹599) — Higher yield, disease-resistant strain
  vs Standard Spawn (₹299) — Only ₹300 more for 2x the yield!

**Bundle deal:** Complete Starter Kit (spawn + substrate + sprayer) at ₹1,199
  Save ₹248 vs buying separately!
```

---

## Flow 8: Seasonal Suggestions

**User Intent:** "Diwali offers"

### Flow Steps

```
User: "Diwali offers"
  │
  ├── IntentRouter.identify("seasonal")
  │     Keywords: ["Diwali", "festival", "seasonal", "offer", "sale", "discount"]
  │     Entities: { festival: "diwali", type: "seasonal" }
  │
  ├── Context Assembly
  │     ├── Current date/season
  │     ├── Festival calendar lookup
  │     ├── Active promotions
  │     └── User's past festival purchases
  │
  ├── Capability: Pricing & Promotions / Seasonal suggestions
  │     │
  │     ├── Tool: SeasonalRecommendationTool
  │     │     ├── Get festival-specific product catalog
  │     │     ├── Apply active promotion rules
  │     │     └── Return scored seasonal products
  │     │
  │     └── If no active festival:
  │           ├── Show seasonal growing tips
  │           └── Suggest current-season products
  │
  ├── Response Formatting
  │     ├── Festival greeting (personalized)
  │     ├── Offer/product highlights
  │     ├── Discount details
  │     └── Validity period
  │
  └── Follow-up Suggestions
        ├── "Show gift hampers"
        ├── "Apply Diwali coupon"
        ├── "Recommend festive gifts"
        └── "Set delivery before Diwali"
```

### Example Response

```
🪔 **Happy Diwali!** Here are our festive offers:

**Diwali Gift Hampers:**
1. **Festive Mushroom Box** (₹999) — Assorted dried mushrooms + recipes
2. **Grow-Your-Own Gift Set** (₹1,499) — Kit + spawn + decorative pot
3. **Premium Gift Hamper** (₹2,499) — Everything above + training voucher

🎉 **Diwali Discount:** Flat 15% off on all gift hampers (use code: DIWALI15)
⏰ Valid until: November 10

Would you like me to help you pick the perfect gift?
```

---

## Intent Routing Matrix

| Intent | Capability | Primary Tool | Downstream Service |
|---|---|---|---|
| `product_search` | Product Search & Discovery | ProductSearchTool | Catalog Service |
| `recommendation` | Personalized Recommendations | RecommendationEngine | Multiple (ensemble) |
| `order_tracking` | Order Management & Tracking | OrderTrackingTool | Order Service |
| `training` | Training & Tutorial Access | TrainingSearchTool | Training Service |
| `knowledge_query` | Knowledge Base Search | KnowledgeQueryTool | Knowledge Platform |
| `cart_assistance` | Shopping Cart Assistance | CartAnalysisTool | Cart Service |
| `cross_sell_upsell` | Product Comparisons | CrossSellTool + UpsellTool | Catalog Service |
| `seasonal` | Pricing & Promotions | SeasonalRecommendationTool | Catalog + Promotions |
| `account` | Account & Profile Management | ProfileTool | Identity Service |
| `wishlist` | Wishlist Management | WishlistTool | Cart/Wishlist Service |
| `support` | Customer Support & FAQs | SupportTool | Support Service |
| `returns` | Returns & Refunds | ReturnsTool | Order Service |
| `feedback` | Feedback & Ratings | FeedbackTool | CustomerContextService |
| `pricing` | Pricing & Promotions | PricingTool | Catalog Service |
| `inventory` | Inventory & Availability | InventoryTool | Inventory Service |

---

## Multi-Turn State Machine

```
                        ┌─────────────┐
                        │   IDLE      │
                        │ (no session)│
                        └──────┬──────┘
                               │ User sends first message
                               ▼
                        ┌─────────────┐
                        │  PROCESSING │
                        │ (intent     │
                        │  routing)   │
                        └──────┬──────┘
                               │
                    ┌──────────┼──────────┐
                    ▼          ▼          ▼
            ┌──────────┐ ┌──────────┐ ┌──────────┐
            │ SEARCH   │ │ RECOMMEND│ │ ORDER    │
            │ (product │ │ (product │ │ (track)  │
            │  results)│ │  recs)   │ │          │
            └────┬─────┘ └────┬─────┘ └────┬─────┘
                 │            │            │
            ┌────▼─────┐ ┌────▼─────┐ ┌────▼─────┐
            │ FILTER   │ │ COMPARE  │ │ DETAIL   │
            │ (refine) │ │ (products)│ │ (status) │
            └────┬─────┘ └────┬─────┘ └────┬─────┘
                 │            │            │
                 └────────────┼────────────┘
                              │ User continues or ends
                              ▼
                        ┌─────────────┐
                        │ IDLE (wait) │
                        └─────────────┘
```
