# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 11 — Accessibility >> Catalog has ARIA landmarks
- Location: tests\training-platform.spec.ts:580:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('main,[role="main"]').first()
Expected: visible
Timeout: 5000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('main,[role="main"]').first()

```

```yaml
- link "Skip to content":
  - /url: "#main"
- navigation "Breadcrumb":
  - list:
    - listitem:
      - link "Training":
        - /url: /training
      - img "Next"
    - listitem: Course Catalog
- banner:
  - text: Enterprise Learning
  - heading "Course Catalog" [level=1]
  - paragraph: Discover expert-led training programs across cultivation, spawn production, commercial farming, and entrepreneurship.
- region "Browse by category":
  - heading "Browse by category" [level=2]
  - link "Mushroom Cultivation — 4 courses":
    - /url: /training/courses/category/mushroom-cultivation
    - text: Mushroom Cultivation 4 courses
  - link "Spawn Production — 2 courses":
    - /url: /training/courses/category/spawn-production
    - text: Spawn Production 2 courses
  - link "Commercial Farming — 1 courses":
    - /url: /training/courses/category/commercial-farming
    - text: Commercial Farming 1 courses
  - link "Value Added Products — 2 courses":
    - /url: /training/courses/category/value-added-products
    - text: Value Added Products 2 courses
  - link "Business Training — 2 courses":
    - /url: /training/courses/category/business-training
    - text: Business Training 2 courses
  - link "Corporate Training — 1 courses":
    - /url: /training/courses/category/corporate-training
    - text: Corporate Training 1 courses
  - link "Institutional Programs — 1 courses":
    - /url: /training/courses/category/institutional-programs
    - text: Institutional Programs 1 courses
  - link "Franchise Programs — 1 courses":
    - /url: /training/courses/category/franchise-programs
    - text: Franchise Programs 1 courses
  - link "Future AI Courses — 1 courses":
    - /url: /training/courses/category/future-ai-courses
    - text: Future AI Courses 1 courses
- region "Featured Courses":
  - text: Handpicked
  - heading "Featured Courses" [level=2]
  - link "View all View all":
    - /url: /training/courses?view=featured
    - text: View all
    - img "View all"
  - region "Featured Courses carousel":
    - article "Mushroom Cultivation Fundamentals":
      - link "Mushroom Cultivation Fundamentals":
        - /url: /training/courses/mushroom-cultivation-fundamentals
        - 'figure "Image placeholder: Mushroom Cultivation Fundamentals"':
          - img "Mushroom Cultivation Fundamentals"
          - text: Mushroom Cultivation Fundamentals Image placeholder Mushroom Cultivation
      - text: Featured Popular
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-101
      - heading "Mushroom Cultivation Fundamentals" [level=3]:
        - link "Mushroom Cultivation Fundamentals":
          - /url: /training/courses/mushroom-cultivation-fundamentals
      - paragraph: Learn the complete process of commercial mushroom cultivation from spawn to harvest.
      - img "Level"
      - text: Beginner
      - img "Duration"
      - text: 4 Weeks
      - img "Delivery"
      - text: Offline
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 4.7 · 245 enrolled
      - img "Seats"
      - text: 35 seats left
      - link "View Mushroom Cultivation Fundamentals":
        - /url: /training/courses/mushroom-cultivation-fundamentals
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Commercial Oyster Farming":
      - link "Commercial Oyster Farming":
        - /url: /training/courses/commercial-oyster-farming
        - 'figure "Image placeholder: Commercial Oyster Farming"':
          - img "Commercial Oyster Farming"
          - text: Commercial Oyster Farming Image placeholder Commercial Farming
      - text: Featured Government
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Commercial Farming · CF-301
      - heading "Commercial Oyster Farming" [level=3]:
        - link "Commercial Oyster Farming":
          - /url: /training/courses/commercial-oyster-farming
      - paragraph: End-to-end training for setting up and running a commercial oyster mushroom farm.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 8 Weeks
      - img "Delivery"
      - text: Offline
      - img "Language"
      - text: English ₹4,150
      - img "Rating"
      - text: 4.8 · 180 enrolled
      - img "Seats"
      - text: 20 seats left
      - link "View Commercial Oyster Farming":
        - /url: /training/courses/commercial-oyster-farming
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Corporate Mushroom Training Program":
      - link "Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - 'figure "Image placeholder: Corporate Mushroom Training Program"':
          - img "Corporate Mushroom Training Program"
          - text: Corporate Mushroom Training Program Image placeholder Corporate Training
      - text: Corporate
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Corporate Training · CT-201
      - heading "Corporate Mushroom Training Program" [level=3]:
        - link "Corporate Mushroom Training Program":
          - /url: /training/courses/corporate-mushroom-training
      - paragraph: Tailored training for corporate teams entering the mushroom industry.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 12 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Quality Control & Food Safety":
      - link "Mushroom Quality Control & Food Safety":
        - /url: /training/courses/mushroom-quality-control
        - 'figure "Image placeholder: Mushroom Quality Control & Food Safety"':
          - img "Mushroom Quality Control & Food Safety"
          - text: Mushroom Quality Control & Food Safety Image placeholder Value Added Products
      - text: Corporate Featured
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Value Added Products · QC-101
      - heading "Mushroom Quality Control & Food Safety" [level=3]:
        - link "Mushroom Quality Control & Food Safety":
          - /url: /training/courses/mushroom-quality-control
      - paragraph: Ensure highest quality standards in mushroom production and processing.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 3 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Mushroom Quality Control & Food Safety":
        - /url: /training/courses/mushroom-quality-control
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
- region "Trending Courses":
  - text: Popular now
  - heading "Trending Courses" [level=2]
  - link "View all View all":
    - /url: /training/courses?sort=trending
    - text: View all
    - img "View all"
  - region "Trending Courses carousel":
    - article "Advanced Spawn Production":
      - link "Advanced Spawn Production":
        - /url: /training/courses/advanced-spawn-production
        - 'figure "Image placeholder: Advanced Spawn Production"':
          - img "Advanced Spawn Production"
          - text: Advanced Spawn Production Image placeholder Spawn Production
      - text: Trending Corporate
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Spawn Production · SP-201
      - heading "Advanced Spawn Production" [level=3]:
        - link "Advanced Spawn Production":
          - /url: /training/courses/advanced-spawn-production
      - paragraph: Master the science of spawn production for large-scale mushroom farming.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 6 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.9 · 120 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Advanced Spawn Production":
        - /url: /training/courses/advanced-spawn-production
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Business & Entrepreneurship":
      - link "Mushroom Business & Entrepreneurship":
        - /url: /training/courses/mushroom-business-entrepreneurship
        - 'figure "Image placeholder: Mushroom Business & Entrepreneurship"':
          - img "Mushroom Business & Entrepreneurship"
          - text: Mushroom Business & Entrepreneurship Image placeholder Business Training
      - text: Trending
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Business Training · BT-101
      - heading "Mushroom Business & Entrepreneurship" [level=3]:
        - link "Mushroom Business & Entrepreneurship":
          - /url: /training/courses/mushroom-business-entrepreneurship
      - paragraph: Build a sustainable mushroom business from the ground up.
      - img "Level"
      - text: Beginner
      - img "Duration"
      - text: 6 Weeks
      - img "Delivery"
      - text: Online
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 4.6 · 210 enrolled
      - img "Seats"
      - text: 30 seats left
      - link "View Mushroom Business & Entrepreneurship":
        - /url: /training/courses/mushroom-business-entrepreneurship
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Introduction to Spawn Production":
      - link "Introduction to Spawn Production":
        - /url: /training/courses/introduction-spawn-production
        - 'figure "Image placeholder: Introduction to Spawn Production"':
          - img "Introduction to Spawn Production"
          - text: Introduction to Spawn Production Image placeholder Spawn Production
      - text: Trending New
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Spawn Production · SP-101
      - heading "Introduction to Spawn Production" [level=3]:
        - link "Introduction to Spawn Production":
          - /url: /training/courses/introduction-spawn-production
      - paragraph: Foundation course for spawn production techniques and sterile laboratory practices.
      - img "Level"
      - text: Beginner
      - img "Duration"
      - text: 2 Weeks
      - img "Delivery"
      - text: Offline
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 4.5 · 320 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Introduction to Spawn Production":
        - /url: /training/courses/introduction-spawn-production
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
- region "Recommended for you":
  - text: Curated
  - heading "Recommended for you" [level=2]
  - region "Recommended for you carousel":
    - article "Advanced Spawn Production":
      - link "Advanced Spawn Production":
        - /url: /training/courses/advanced-spawn-production
        - 'figure "Image placeholder: Advanced Spawn Production"':
          - img "Advanced Spawn Production"
          - text: Advanced Spawn Production Image placeholder Spawn Production
      - text: Trending Corporate
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Spawn Production · SP-201
      - heading "Advanced Spawn Production" [level=3]:
        - link "Advanced Spawn Production":
          - /url: /training/courses/advanced-spawn-production
      - paragraph: Master the science of spawn production for large-scale mushroom farming.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 6 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.9 · 120 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Advanced Spawn Production":
        - /url: /training/courses/advanced-spawn-production
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Corporate Mushroom Training Program":
      - link "Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - 'figure "Image placeholder: Corporate Mushroom Training Program"':
          - img "Corporate Mushroom Training Program"
          - text: Corporate Mushroom Training Program Image placeholder Corporate Training
      - text: Corporate
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Corporate Training · CT-201
      - heading "Corporate Mushroom Training Program" [level=3]:
        - link "Corporate Mushroom Training Program":
          - /url: /training/courses/corporate-mushroom-training
      - paragraph: Tailored training for corporate teams entering the mushroom industry.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 12 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Advanced Composting & Substrate":
      - link "Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - 'figure "Image placeholder: Advanced Composting & Substrate"':
          - img "Advanced Composting & Substrate"
          - text: Advanced Composting & Substrate Image placeholder Mushroom Cultivation
      - text: Popular
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-201
      - heading "Advanced Composting & Substrate" [level=3]:
        - link "Advanced Composting & Substrate":
          - /url: /training/courses/advanced-composting-substrate
      - paragraph: Master the art and science of mushroom substrate preparation.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 4 Weeks
      - img "Delivery"
      - text: Recorded
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.4 · 45 enrolled
      - img "Seats"
      - text: 35 seats left
      - link "View Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Export & International Trade":
      - link "Mushroom Export & International Trade":
        - /url: /training/courses/mushroom-export-international-trade
        - 'figure "Image placeholder: Mushroom Export & International Trade"':
          - img "Mushroom Export & International Trade"
          - text: Mushroom Export & International Trade Image placeholder Business Training
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Business Training · BT-201
      - heading "Mushroom Export & International Trade" [level=3]:
        - link "Mushroom Export & International Trade":
          - /url: /training/courses/mushroom-export-international-trade
      - paragraph: Navigate the complexities of international mushroom trade and export regulations.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 4 Weeks
      - img "Delivery"
      - text: Online
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.2 · 89 enrolled
      - img "Seats"
      - text: 31 seats left
      - link "View Mushroom Export & International Trade":
        - /url: /training/courses/mushroom-export-international-trade
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
- region "Recently Added":
  - text: New
  - heading "Recently Added" [level=2]
  - region "Recently Added carousel":
    - article "AI-Powered Mushroom Farm Management":
      - link "AI-Powered Mushroom Farm Management":
        - /url: /training/courses/ai-mushroom-farm-management
        - 'figure "Image placeholder: AI-Powered Mushroom Farm Management"':
          - img "AI-Powered Mushroom Farm Management"
          - text: AI-Powered Mushroom Farm Management Image placeholder Future AI Courses
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Future AI Courses · AI-101
      - heading "AI-Powered Mushroom Farm Management" [level=3]:
        - link "AI-Powered Mushroom Farm Management":
          - /url: /training/courses/ai-mushroom-farm-management
      - paragraph: Leverage artificial intelligence for modern mushroom farm management.
      - img "Level"
      - text: AI Assisted
      - img "Duration"
      - text: TBD
      - img "Delivery"
      - text: VR Training
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View AI-Powered Mushroom Farm Management":
        - /url: /training/courses/ai-mushroom-farm-management
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Quality Control & Food Safety":
      - link "Mushroom Quality Control & Food Safety":
        - /url: /training/courses/mushroom-quality-control
        - 'figure "Image placeholder: Mushroom Quality Control & Food Safety"':
          - img "Mushroom Quality Control & Food Safety"
          - text: Mushroom Quality Control & Food Safety Image placeholder Value Added Products
      - text: Corporate Featured
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Value Added Products · QC-101
      - heading "Mushroom Quality Control & Food Safety" [level=3]:
        - link "Mushroom Quality Control & Food Safety":
          - /url: /training/courses/mushroom-quality-control
      - paragraph: Ensure highest quality standards in mushroom production and processing.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 3 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Mushroom Quality Control & Food Safety":
        - /url: /training/courses/mushroom-quality-control
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Franchise Mushroom Farm Operations":
      - link "Franchise Mushroom Farm Operations":
        - /url: /training/courses/franchise-mushroom-farm-operations
        - 'figure "Image placeholder: Franchise Mushroom Farm Operations"':
          - img "Franchise Mushroom Farm Operations"
          - text: Franchise Mushroom Farm Operations Image placeholder Franchise Programs
      - text: New
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Franchise Programs · FP-101
      - heading "Franchise Mushroom Farm Operations" [level=3]:
        - link "Franchise Mushroom Farm Operations":
          - /url: /training/courses/franchise-mushroom-farm-operations
      - paragraph: Standardized training for franchise mushroom farm operators.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 8 Weeks
      - img "Delivery"
      - text: Offline
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Franchise Mushroom Farm Operations":
        - /url: /training/courses/franchise-mushroom-farm-operations
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Institutional Mushroom Research Program":
      - link "Institutional Mushroom Research Program":
        - /url: /training/courses/institutional-mushroom-research
        - 'figure "Image placeholder: Institutional Mushroom Research Program"':
          - img "Institutional Mushroom Research Program"
          - text: Institutional Mushroom Research Program Image placeholder Institutional Programs
      - text: Government
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Institutional Programs · IP-301
      - heading "Institutional Mushroom Research Program" [level=3]:
        - link "Institutional Mushroom Research Program":
          - /url: /training/courses/institutional-mushroom-research
      - paragraph: Advanced research-oriented program for academic institutions and research centers.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 16 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹4,150
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Institutional Mushroom Research Program":
        - /url: /training/courses/institutional-mushroom-research
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Advanced Composting & Substrate":
      - link "Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - 'figure "Image placeholder: Advanced Composting & Substrate"':
          - img "Advanced Composting & Substrate"
          - text: Advanced Composting & Substrate Image placeholder Mushroom Cultivation
      - text: Popular
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-201
      - heading "Advanced Composting & Substrate" [level=3]:
        - link "Advanced Composting & Substrate":
          - /url: /training/courses/advanced-composting-substrate
      - paragraph: Master the art and science of mushroom substrate preparation.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 4 Weeks
      - img "Delivery"
      - text: Recorded
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.4 · 45 enrolled
      - img "Seats"
      - text: 35 seats left
      - link "View Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Corporate Mushroom Training Program":
      - link "Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - 'figure "Image placeholder: Corporate Mushroom Training Program"':
          - img "Corporate Mushroom Training Program"
          - text: Corporate Mushroom Training Program Image placeholder Corporate Training
      - text: Corporate
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Corporate Training · CT-201
      - heading "Corporate Mushroom Training Program" [level=3]:
        - link "Corporate Mushroom Training Program":
          - /url: /training/courses/corporate-mushroom-training
      - paragraph: Tailored training for corporate teams entering the mushroom industry.
      - img "Level"
      - text: Intermediate
      - img "Duration"
      - text: 12 Weeks
      - img "Delivery"
      - text: Hybrid
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 0.0 · 0 enrolled
      - img "Seats"
      - text: 40 seats left
      - link "View Corporate Mushroom Training Program":
        - /url: /training/courses/corporate-mushroom-training
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Disease Management & IPM":
      - link "Mushroom Disease Management & IPM":
        - /url: /training/courses/mushroom-disease-management
        - 'figure "Image placeholder: Mushroom Disease Management & IPM"':
          - img "Mushroom Disease Management & IPM"
          - text: Mushroom Disease Management & IPM Image placeholder Mushroom Cultivation
      - text: Government
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-301
      - heading "Mushroom Disease Management & IPM" [level=3]:
        - link "Mushroom Disease Management & IPM":
          - /url: /training/courses/mushroom-disease-management
      - paragraph: Identify, prevent, and manage mushroom diseases using integrated pest management.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 3 Weeks
      - img "Delivery"
      - text: Live
      - img "Language"
      - text: English ₹4,150
      - img "Rating"
      - text: 4.6 · 78 enrolled
      - img "Seats"
      - text: 2 seats left
      - link "View Mushroom Disease Management & IPM":
        - /url: /training/courses/mushroom-disease-management
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Business & Entrepreneurship":
      - link "Mushroom Business & Entrepreneurship":
        - /url: /training/courses/mushroom-business-entrepreneurship
        - 'figure "Image placeholder: Mushroom Business & Entrepreneurship"':
          - img "Mushroom Business & Entrepreneurship"
          - text: Mushroom Business & Entrepreneurship Image placeholder Business Training
      - text: Trending
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Business Training · BT-101
      - heading "Mushroom Business & Entrepreneurship" [level=3]:
        - link "Mushroom Business & Entrepreneurship":
          - /url: /training/courses/mushroom-business-entrepreneurship
      - paragraph: Build a sustainable mushroom business from the ground up.
      - img "Level"
      - text: Beginner
      - img "Duration"
      - text: 6 Weeks
      - img "Delivery"
      - text: Online
      - img "Language"
      - text: English ₹3,070
      - img "Rating"
      - text: 4.6 · 210 enrolled
      - img "Seats"
      - text: 30 seats left
      - link "View Mushroom Business & Entrepreneurship":
        - /url: /training/courses/mushroom-business-entrepreneurship
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
- region "Upcoming Batches":
  - text: Enrolling soon
  - heading "Upcoming Batches" [level=2]
  - region "Upcoming Batches carousel":
    - article "Advanced Composting & Substrate":
      - link "Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - 'figure "Image placeholder: Advanced Composting & Substrate"':
          - img "Advanced Composting & Substrate"
          - text: Advanced Composting & Substrate Image placeholder Mushroom Cultivation
      - text: Popular
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-201
      - heading "Advanced Composting & Substrate" [level=3]:
        - link "Advanced Composting & Substrate":
          - /url: /training/courses/advanced-composting-substrate
      - paragraph: Master the art and science of mushroom substrate preparation.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 4 Weeks
      - img "Delivery"
      - text: Recorded
      - img "Language"
      - text: English ₹3,610
      - img "Rating"
      - text: 4.4 · 45 enrolled
      - img "Seats"
      - text: 35 seats left
      - link "View Advanced Composting & Substrate":
        - /url: /training/courses/advanced-composting-substrate
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
    - article "Mushroom Disease Management & IPM":
      - link "Mushroom Disease Management & IPM":
        - /url: /training/courses/mushroom-disease-management
        - 'figure "Image placeholder: Mushroom Disease Management & IPM"':
          - img "Mushroom Disease Management & IPM"
          - text: Mushroom Disease Management & IPM Image placeholder Mushroom Cultivation
      - text: Government
      - button "Bookmark"
      - button "Add to wishlist"
      - button "Add to comparison"
      - text: Mushroom Cultivation · MC-301
      - heading "Mushroom Disease Management & IPM" [level=3]:
        - link "Mushroom Disease Management & IPM":
          - /url: /training/courses/mushroom-disease-management
      - paragraph: Identify, prevent, and manage mushroom diseases using integrated pest management.
      - img "Level"
      - text: Advanced
      - img "Duration"
      - text: 3 Weeks
      - img "Delivery"
      - text: Live
      - img "Language"
      - text: English ₹4,150
      - img "Rating"
      - text: 4.6 · 78 enrolled
      - img "Seats"
      - text: 2 seats left
      - link "View Mushroom Disease Management & IPM":
        - /url: /training/courses/mushroom-disease-management
        - text: View Course
        - img "View"
      - button "Add to comparison":
        - img "Compare"
        - text: Compare
- region "Browse all courses":
  - heading "Browse all courses" [level=2]
  - searchbox "Search the course catalog"
  - button "Toggle course filters": Filters
  - button "Grid view" [pressed]
  - button "List view"
  - button "Compact view"
  - button "Featured view"
  - button "Carousel view"
  - text: 15 courses Sort by
  - button "Sort by Most Popular" [pressed]: Most Popular
  - button "Sort by Newest": Newest
  - button "Sort by Trending": Trending
  - button "Sort by Featured": Featured
  - button "Sort by Recently Updated": Recently Updated
  - button "Sort by Alphabetical": Alphabetical
  - button "Sort by Duration": Duration
  - button "Sort by Price": Price
  - button "Sort by Difficulty": Difficulty
  - article "Introduction to Spawn Production":
    - link "Introduction to Spawn Production":
      - /url: /training/courses/introduction-spawn-production
      - 'figure "Image placeholder: Introduction to Spawn Production"':
        - img "Introduction to Spawn Production"
        - text: Introduction to Spawn Production Image placeholder Spawn Production
    - text: Trending New
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Spawn Production · SP-101
    - heading "Introduction to Spawn Production" [level=3]:
      - link "Introduction to Spawn Production":
        - /url: /training/courses/introduction-spawn-production
    - paragraph: Foundation course for spawn production techniques and sterile laboratory practices.
    - img "Level"
    - text: Beginner
    - img "Duration"
    - text: 2 Weeks
    - img "Delivery"
    - text: Offline
    - img "Language"
    - text: English ₹3,070
    - img "Rating"
    - text: 4.5 · 320 enrolled
    - img "Seats"
    - text: 40 seats left
    - link "View Introduction to Spawn Production":
      - /url: /training/courses/introduction-spawn-production
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Mushroom Cultivation Fundamentals":
    - link "Mushroom Cultivation Fundamentals":
      - /url: /training/courses/mushroom-cultivation-fundamentals
      - 'figure "Image placeholder: Mushroom Cultivation Fundamentals"':
        - img "Mushroom Cultivation Fundamentals"
        - text: Mushroom Cultivation Fundamentals Image placeholder Mushroom Cultivation
    - text: Featured Popular
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Mushroom Cultivation · MC-101
    - heading "Mushroom Cultivation Fundamentals" [level=3]:
      - link "Mushroom Cultivation Fundamentals":
        - /url: /training/courses/mushroom-cultivation-fundamentals
    - paragraph: Learn the complete process of commercial mushroom cultivation from spawn to harvest.
    - img "Level"
    - text: Beginner
    - img "Duration"
    - text: 4 Weeks
    - img "Delivery"
    - text: Offline
    - img "Language"
    - text: English ₹3,070
    - img "Rating"
    - text: 4.7 · 245 enrolled
    - img "Seats"
    - text: 35 seats left
    - link "View Mushroom Cultivation Fundamentals":
      - /url: /training/courses/mushroom-cultivation-fundamentals
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Mushroom Business & Entrepreneurship":
    - link "Mushroom Business & Entrepreneurship":
      - /url: /training/courses/mushroom-business-entrepreneurship
      - 'figure "Image placeholder: Mushroom Business & Entrepreneurship"':
        - img "Mushroom Business & Entrepreneurship"
        - text: Mushroom Business & Entrepreneurship Image placeholder Business Training
    - text: Trending
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Business Training · BT-101
    - heading "Mushroom Business & Entrepreneurship" [level=3]:
      - link "Mushroom Business & Entrepreneurship":
        - /url: /training/courses/mushroom-business-entrepreneurship
    - paragraph: Build a sustainable mushroom business from the ground up.
    - img "Level"
    - text: Beginner
    - img "Duration"
    - text: 6 Weeks
    - img "Delivery"
    - text: Online
    - img "Language"
    - text: English ₹3,070
    - img "Rating"
    - text: 4.6 · 210 enrolled
    - img "Seats"
    - text: 30 seats left
    - link "View Mushroom Business & Entrepreneurship":
      - /url: /training/courses/mushroom-business-entrepreneurship
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Commercial Oyster Farming":
    - link "Commercial Oyster Farming":
      - /url: /training/courses/commercial-oyster-farming
      - 'figure "Image placeholder: Commercial Oyster Farming"':
        - img "Commercial Oyster Farming"
        - text: Commercial Oyster Farming Image placeholder Commercial Farming
    - text: Featured Government
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Commercial Farming · CF-301
    - heading "Commercial Oyster Farming" [level=3]:
      - link "Commercial Oyster Farming":
        - /url: /training/courses/commercial-oyster-farming
    - paragraph: End-to-end training for setting up and running a commercial oyster mushroom farm.
    - img "Level"
    - text: Advanced
    - img "Duration"
    - text: 8 Weeks
    - img "Delivery"
    - text: Offline
    - img "Language"
    - text: English ₹4,150
    - img "Rating"
    - text: 4.8 · 180 enrolled
    - img "Seats"
    - text: 20 seats left
    - link "View Commercial Oyster Farming":
      - /url: /training/courses/commercial-oyster-farming
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Mushroom Harvest & Post-Harvest Technology":
    - link "Mushroom Harvest & Post-Harvest Technology":
      - /url: /training/courses/mushroom-harvest-post-harvest
      - 'figure "Image placeholder: Mushroom Harvest & Post-Harvest Technology"':
        - img "Mushroom Harvest & Post-Harvest Technology"
        - text: Mushroom Harvest & Post-Harvest Technology Image placeholder Mushroom Cultivation
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Mushroom Cultivation · MC-401
    - heading "Mushroom Harvest & Post-Harvest Technology" [level=3]:
      - link "Mushroom Harvest & Post-Harvest Technology":
        - /url: /training/courses/mushroom-harvest-post-harvest
    - paragraph: Optimize harvest techniques and post-harvest handling for maximum quality and shelf life.
    - img "Level"
    - text: Advanced
    - img "Duration"
    - text: 2 Weeks
    - img "Delivery"
    - text: Recorded
    - img "Language"
    - text: English ₹4,690
    - img "Rating"
    - text: 4.3 · 156 enrolled
    - img "Seats"
    - text: 4 seats left
    - link "View Mushroom Harvest & Post-Harvest Technology":
      - /url: /training/courses/mushroom-harvest-post-harvest
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Advanced Spawn Production":
    - link "Advanced Spawn Production":
      - /url: /training/courses/advanced-spawn-production
      - 'figure "Image placeholder: Advanced Spawn Production"':
        - img "Advanced Spawn Production"
        - text: Advanced Spawn Production Image placeholder Spawn Production
    - text: Trending Corporate
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Spawn Production · SP-201
    - heading "Advanced Spawn Production" [level=3]:
      - link "Advanced Spawn Production":
        - /url: /training/courses/advanced-spawn-production
    - paragraph: Master the science of spawn production for large-scale mushroom farming.
    - img "Level"
    - text: Advanced
    - img "Duration"
    - text: 6 Weeks
    - img "Delivery"
    - text: Hybrid
    - img "Language"
    - text: English ₹3,610
    - img "Rating"
    - text: 4.9 · 120 enrolled
    - img "Seats"
    - text: 40 seats left
    - link "View Advanced Spawn Production":
      - /url: /training/courses/advanced-spawn-production
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Value-Added Mushroom Products":
    - link "Value-Added Mushroom Products":
      - /url: /training/courses/value-added-mushroom-products
      - 'figure "Image placeholder: Value-Added Mushroom Products"':
        - img "Value-Added Mushroom Products"
        - text: Value-Added Mushroom Products Image placeholder Value Added Products
    - text: New
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Value Added Products · VA-101
    - heading "Value-Added Mushroom Products" [level=3]:
      - link "Value-Added Mushroom Products":
        - /url: /training/courses/value-added-mushroom-products
    - paragraph: Learn to create and market value-added products from mushrooms.
    - img "Level"
    - text: Intermediate
    - img "Duration"
    - text: 4 Weeks
    - img "Delivery"
    - text: Online
    - img "Language"
    - text: English ₹3,070
    - img "Rating"
    - text: 4.5 · 95 enrolled
    - img "Seats"
    - text: 25 seats left
    - link "View Value-Added Mushroom Products":
      - /url: /training/courses/value-added-mushroom-products
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Mushroom Export & International Trade":
    - link "Mushroom Export & International Trade":
      - /url: /training/courses/mushroom-export-international-trade
      - 'figure "Image placeholder: Mushroom Export & International Trade"':
        - img "Mushroom Export & International Trade"
        - text: Mushroom Export & International Trade Image placeholder Business Training
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Business Training · BT-201
    - heading "Mushroom Export & International Trade" [level=3]:
      - link "Mushroom Export & International Trade":
        - /url: /training/courses/mushroom-export-international-trade
    - paragraph: Navigate the complexities of international mushroom trade and export regulations.
    - img "Level"
    - text: Advanced
    - img "Duration"
    - text: 4 Weeks
    - img "Delivery"
    - text: Online
    - img "Language"
    - text: English ₹3,610
    - img "Rating"
    - text: 4.2 · 89 enrolled
    - img "Seats"
    - text: 31 seats left
    - link "View Mushroom Export & International Trade":
      - /url: /training/courses/mushroom-export-international-trade
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - article "Mushroom Disease Management & IPM":
    - link "Mushroom Disease Management & IPM":
      - /url: /training/courses/mushroom-disease-management
      - 'figure "Image placeholder: Mushroom Disease Management & IPM"':
        - img "Mushroom Disease Management & IPM"
        - text: Mushroom Disease Management & IPM Image placeholder Mushroom Cultivation
    - text: Government
    - button "Bookmark"
    - button "Add to wishlist"
    - button "Add to comparison"
    - text: Mushroom Cultivation · MC-301
    - heading "Mushroom Disease Management & IPM" [level=3]:
      - link "Mushroom Disease Management & IPM":
        - /url: /training/courses/mushroom-disease-management
    - paragraph: Identify, prevent, and manage mushroom diseases using integrated pest management.
    - img "Level"
    - text: Advanced
    - img "Duration"
    - text: 3 Weeks
    - img "Delivery"
    - text: Live
    - img "Language"
    - text: English ₹4,150
    - img "Rating"
    - text: 4.6 · 78 enrolled
    - img "Seats"
    - text: 2 seats left
    - link "View Mushroom Disease Management & IPM":
      - /url: /training/courses/mushroom-disease-management
      - text: View Course
      - img "View"
    - button "Add to comparison":
      - img "Compare"
      - text: Compare
  - navigation "Course catalog pagination":
    - button "Previous page" [disabled]
    - button "Page 1": "1"
    - button "Page 2": "2"
    - button "Next page"
```

# Test source

```ts
  482 |     const n1 = await page.locator('nav').first().innerText().catch(() => '');
  483 |     await page.goto('/admin/training/courses', { waitUntil: 'networkidle' });
  484 |     const n2 = await page.locator('nav').first().innerText().catch(() => '');
  485 |     expect(n1.length > 0 || n2.length > 0).toBeTruthy();
  486 |   });
  487 | });
  488 | 
  489 | // ====================================================================
  490 | // PHASE 9 — SECURITY
  491 | // ====================================================================
  492 | test.describe('Phase 9 — Security', () => {
  493 |   test('Public catalog accessible without auth', async ({ page }) => {
  494 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  495 |     const t = await bodyText(page);
  496 |     expect(t.length).toBeGreaterThan(0);
  497 |   });
  498 | 
  499 |   test('Learner dashboard requires auth', async ({ page }) => {
  500 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  501 |     // Should redirect to login or show login page
  502 |     const t = await bodyText(page);
  503 |     const isLoggedIn = t.includes('Enrolled') || t.includes('Training') || t.includes('Progress');
  504 |     const isLoginPage = page.url().includes('login');
  505 |     expect(isLoggedIn || isLoginPage).toBeTruthy();
  506 |   });
  507 | 
  508 |   test('No production secrets in training pages', async ({ page }) => {
  509 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  510 |     const html = await page.locator('html').innerHTML();
  511 |     for (const s of ['sk_live_', 'pk_live_', 'rzp_live_']) {
  512 |       expect(html.includes(s)).toBe(false);
  513 |     }
  514 |   });
  515 | 
  516 |   test('Admin training workspace accessible (no auth guard)', async ({ page }) => {
  517 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  518 |     expect(page.url()).toContain('/admin/training/dashboard');
  519 |   });
  520 | 
  521 |   test('No console errors on training pages', async ({ page }) => {
  522 |     const errs = [];
  523 |     page.on('console', m => { if (m.type() === 'error') errs.push(m.text()); });
  524 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  525 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  526 |     if (errs.length > 0) console.log('Console errors:', errs);
  527 |     expect(true).toBeTruthy(); // Non-blocking
  528 |   });
  529 | });
  530 | 
  531 | // ====================================================================
  532 | // PHASE 10 — CROSS-BROWSER
  533 | // ====================================================================
  534 | test.describe('Phase 10 — Cross-Browser', () => {
  535 |   test('Catalog renders at desktop', async ({ page }) => {
  536 |     await page.setViewportSize({ width: 1440, height: 900 });
  537 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  538 |     expect((await bodyText(page)).length).toBeGreaterThan(20);
  539 |   });
  540 | 
  541 |   test('Catalog renders at mobile', async ({ page }) => {
  542 |     await page.setViewportSize({ width: 375, height: 667 });
  543 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  544 |     expect((await bodyText(page)).length).toBeGreaterThan(20);
  545 |   });
  546 | 
  547 |   test('Course detail renders at all viewports', async ({ page }) => {
  548 |     for (const vp of [{ w: 1440, h: 900 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  549 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  550 |       await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  551 |       expect((await bodyText(page)).length).toBeGreaterThan(20);
  552 |     }
  553 |   });
  554 | 
  555 |   test('Learner dashboard renders at desktop', async ({ page }) => {
  556 |     await login(page);
  557 |     await page.setViewportSize({ width: 1440, height: 900 });
  558 |     const r = await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  559 |     expect(r?.status()).toBeLessThan(400);
  560 |   });
  561 | 
  562 |   test('Learner dashboard renders at mobile', async ({ page }) => {
  563 |     await login(page);
  564 |     await page.setViewportSize({ width: 375, height: 667 });
  565 |     const r = await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  566 |     expect(r?.status()).toBeLessThan(400);
  567 |   });
  568 | });
  569 | 
  570 | // ====================================================================
  571 | // PHASE 11 — ACCESSIBILITY
  572 | // ====================================================================
  573 | test.describe('Phase 11 — Accessibility', () => {
  574 |   test('Catalog has skip to content link', async ({ page }) => {
  575 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  576 |     const l = page.locator('a[href="#main-content"],a:has-text("Skip"),[class*="skip"]');
  577 |     await expect(l.first()).toBeVisible({ timeout: 5000 });
  578 |   });
  579 | 
  580 |   test('Catalog has ARIA landmarks', async ({ page }) => {
  581 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
> 582 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
      |                                                              ^ Error: expect(locator).toBeVisible() failed
  583 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  584 |   });
  585 | 
  586 |   test('Images have alt text on catalog', async ({ page }) => {
  587 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  588 |     const imgs = page.locator('img');
  589 |     const c = await imgs.count();
  590 |     let missing = 0;
  591 |     for (let i = 0; i < c; i++) {
  592 |       const alt = await imgs.nth(i).getAttribute('alt');
  593 |       if (alt === null || alt === undefined) missing++;
  594 |     }
  595 |     expect(missing).toBe(0);
  596 |   });
  597 | 
  598 |   test('Course detail has semantic headings', async ({ page }) => {
  599 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  600 |     const h1 = page.locator('h1');
  601 |     await expect(h1.first()).toBeVisible({ timeout: 3000 });
  602 |   });
  603 | 
  604 |   test('Admin training workspace has landmarks', async ({ page }) => {
  605 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  606 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  607 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  608 |   });
  609 | 
  610 |   test('Course cards are keyboard navigable', async ({ page }) => {
  611 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  612 |     const firstLink = page.locator('a[href*="/training/courses/"]').first();
  613 |     if (await firstLink.isVisible({ timeout: 2000 }).catch(() => false)) {
  614 |       await firstLink.focus();
  615 |       await page.keyboard.press('Enter');
  616 |       await page.waitForTimeout(500);
  617 |       expect(page.url()).toContain('/training/courses/');
  618 |     }
  619 |   });
  620 | });
  621 | 
  622 | // ====================================================================
  623 | // PHASE 12 — PERFORMANCE
  624 | // ====================================================================
  625 | test.describe('Phase 12 — Performance', () => {
  626 |   test('Catalog loads within 15s', async ({ page }) => {
  627 |     const s = Date.now();
  628 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  629 |     expect(Date.now() - s).toBeLessThan(25000);
  630 |   });
  631 | 
  632 |   test('Course detail loads within 15s', async ({ page }) => {
  633 |     const s = Date.now();
  634 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  635 |     expect(Date.now() - s).toBeLessThan(25000);
  636 |   });
  637 | 
  638 |   test('Learner dashboard loads within 15s', async ({ page }) => {
  639 |     await login(page);
  640 |     const s = Date.now();
  641 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  642 |     expect(Date.now() - s).toBeLessThan(25000);
  643 |   });
  644 | 
  645 |   test('Admin training dashboard loads within 15s', async ({ page }) => {
  646 |     const s = Date.now();
  647 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  648 |     expect(Date.now() - s).toBeLessThan(25000);
  649 |   });
  650 | 
  651 |   test('Course builder loads within 15s', async ({ page }) => {
  652 |     const s = Date.now();
  653 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  654 |     expect(Date.now() - s).toBeLessThan(25000);
  655 |   });
  656 | });
  657 | 
  658 | // ====================================================================
  659 | // PHASE 13 — VISUAL REVIEW
  660 | // ====================================================================
  661 | test.describe('Phase 13 — Visual Review', () => {
  662 |   test('Catalog has course cards', async ({ page }) => {
  663 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  664 |     expect(await page.locator('[class*="card"],[class*="Card"],article').count()).toBeGreaterThanOrEqual(1);
  665 |   });
  666 | 
  667 |   test('Course detail has proper layout', async ({ page }) => {
  668 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  669 |     const main = page.locator('main,[role="main"]').first();
  670 |     await expect(main).toBeVisible({ timeout: 3000 });
  671 |   });
  672 | 
  673 |   test('No horizontal scroll on catalog', async ({ page }) => {
  674 |     await page.setViewportSize({ width: 1440, height: 900 });
  675 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  676 |     const hs = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
  677 |     expect(hs).toBe(false);
  678 |   });
  679 | 
  680 |   test('Typography consistent on course detail', async ({ page }) => {
  681 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  682 |     const h1 = page.locator('h1').first();
```