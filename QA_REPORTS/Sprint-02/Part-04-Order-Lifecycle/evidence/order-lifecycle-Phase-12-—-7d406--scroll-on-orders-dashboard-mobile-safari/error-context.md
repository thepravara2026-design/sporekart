# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 12 — Visual Review >> No horizontal scroll on orders dashboard
- Location: tests\order-lifecycle.spec.ts:684:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: false
Received: true
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - navigation "Account actions" [ref=e6]:
        - menuitem "Settings" [ref=e8] [cursor=pointer]:
          - img [ref=e10]
          - generic [ref=e13]: Settings
        - menuitem "Help" [ref=e15] [cursor=pointer]:
          - img [ref=e17]
          - generic [ref=e20]: Help
        - menuitem "Logout" [ref=e22] [cursor=pointer]:
          - img [ref=e24]
          - generic [ref=e27]: Logout
    - generic [ref=e28]:
      - complementary [ref=e29]:
        - navigation "Customer workspace navigation" [ref=e30]:
          - generic [ref=e31]:
            - generic [ref=e33]:
              - img [ref=e35]
              - generic [ref=e38]: SporeKart
            - button "Collapse sidebar" [ref=e39] [cursor=pointer]:
              - img [ref=e40]
          - menu [ref=e43]:
            - menuitem "Dashboard" [ref=e47] [cursor=pointer]:
              - img [ref=e49]
              - generic [ref=e51]: Dashboard
            - menuitem "Orders" [ref=e55] [cursor=pointer]:
              - img [ref=e57]
              - generic [ref=e60]: Orders
            - menuitem "Wishlist" [ref=e64] [cursor=pointer]:
              - img [ref=e66]
              - generic [ref=e68]: Wishlist
            - menuitem "Training" [ref=e72] [cursor=pointer]:
              - img [ref=e74]
              - generic [ref=e77]: Training
            - menuitem "Products" [ref=e81] [cursor=pointer]:
              - img [ref=e83]
              - generic [ref=e87]: Products
            - menuitem "Addresses" [ref=e91] [cursor=pointer]:
              - img [ref=e93]
              - generic [ref=e96]: Addresses
            - menuitem "Support" [ref=e100] [cursor=pointer]:
              - img [ref=e102]
              - generic [ref=e105]: Support
            - menuitem "Notifications" [ref=e109] [cursor=pointer]:
              - img [ref=e111]
              - generic [ref=e114]: Notifications
          - generic [ref=e116]: v1.0.0-beta · Customer Workspace
      - main [ref=e117]:
        - generic [ref=e119]:
          - generic [ref=e121]:
            - heading "Orders" [level=1] [ref=e122]
            - paragraph [ref=e123]: Dashboard
          - navigation "Breadcrumb" [ref=e124]:
            - list [ref=e125]:
              - listitem [ref=e126]:
                - link "Dashboard" [ref=e127]:
                  - /url: /dashboard
                  - generic [ref=e128]: Dashboard
              - listitem [ref=e129]:
                - img [ref=e130]
              - listitem [ref=e132]:
                - generic [ref=e134]: Orders
          - generic [ref=e136]:
            - region "Order statistics" [ref=e137]:
              - generic [ref=e138]:
                - article [ref=e139]:
                  - img [ref=e141]
                  - generic [ref=e144]:
                    - generic [ref=e145]: Total Spend
                    - strong [ref=e146]: ₹11,600.00
                - article [ref=e147]:
                  - img [ref=e149]
                  - generic [ref=e154]:
                    - generic [ref=e155]: Active Orders
                    - strong [ref=e156]: "2"
                - article [ref=e157]:
                  - img [ref=e159]
                  - generic [ref=e162]:
                    - generic [ref=e163]: Delivered
                    - strong [ref=e164]: "1"
                - article [ref=e165]:
                  - img [ref=e167]
                  - generic [ref=e171]:
                    - generic [ref=e172]: Returns & Refunds
                    - strong [ref=e173]: "1"
            - region "AI Order Assistant Insights" [ref=e174]:
              - generic [ref=e175]:
                - img [ref=e177]
                - generic [ref=e180]:
                  - heading "AI Order Assistant (Beta)" [level=4] [ref=e181]
                  - paragraph [ref=e182]:
                    - text: Your shipment
                    - strong [ref=e183]: ORD-2026-8842
                    - text: containing Pink Oyster spawn is in transit and departing Bengaluru Hub. Expected arrival is tomorrow. Click
                    - link "Track Shipment" [ref=e184]:
                      - /url: /dashboard/orders/ORD-2026-8842/track
                    - text: to see live milestones, or explore spawn bag sterilization prep guides.
            - generic [ref=e185]:
              - generic [ref=e186]:
                - tablist "Order status filters" [ref=e187]:
                  - tab "All" [selected] [ref=e188] [cursor=pointer]
                  - tab "Active" [ref=e189] [cursor=pointer]
                  - tab "Completed" [ref=e190] [cursor=pointer]
                  - tab "Refunded" [ref=e191] [cursor=pointer]
                - generic [ref=e192]:
                  - img [ref=e194]
                  - textbox "Search orders" [ref=e197]:
                    - /placeholder: Search by order ID or product name...
              - generic [ref=e198]:
                - article [ref=e199]:
                  - generic [ref=e200]:
                    - generic [ref=e201]:
                      - generic [ref=e202]:
                        - generic [ref=e203]: ORDER PLACED
                        - text: 2026-07-11
                      - generic [ref=e204]:
                        - generic [ref=e205]: TOTAL
                        - generic [ref=e206]: ₹3,450.00
                      - generic [ref=e207]:
                        - generic [ref=e208]: SHIP TO
                        - text: Jane Doe
                    - generic [ref=e209]:
                      - generic [ref=e210]: "ORDER # ORD-2026-8842"
                      - generic [ref=e211]: In Transit
                  - generic [ref=e212]:
                    - generic [ref=e213]:
                      - generic [ref=e214]:
                        - generic [ref=e215]: 🍄
                        - generic [ref=e216]:
                          - heading "Pink Oyster Mushroom Grain Spawn (2kg)" [level=4] [ref=e217]
                          - paragraph [ref=e218]: "Qty: 2 · Price: ₹1,100.00"
                          - generic [ref=e219]: "SKU: SKU-PO-GRN-2KG"
                      - generic [ref=e220]:
                        - generic [ref=e221]: 🛍️
                        - generic [ref=e222]:
                          - heading "Autoclavable Spawn Bags (Pack of 10)" [level=4] [ref=e223]
                          - paragraph [ref=e224]: "Qty: 1 · Price: ₹1,000.00"
                          - generic [ref=e225]: "SKU: SKU-BAG-ACV-10P"
                    - generic [ref=e226]:
                      - generic [ref=e227]:
                        - img [ref=e228]
                        - generic [ref=e233]:
                          - text: "Estimated Delivery:"
                          - strong [ref=e234]: 2026-07-14
                          - text: via Delhivery
                      - generic [ref=e236]:
                        - text: "Payment Method:"
                        - strong [ref=e237]: UPI (Razorpay)
                        - text: (Paid)
                  - generic [ref=e238]:
                    - generic [ref=e239]:
                      - button "View Details" [ref=e240]
                      - button "Track Order" [ref=e241]
                    - generic [ref=e242]:
                      - button "Invoice" [ref=e243] [cursor=pointer]:
                        - img [ref=e244]
                        - text: Invoice
                      - link "Support" [ref=e247]:
                        - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-8842
                        - img [ref=e248]
                        - text: Support
                - article [ref=e251]:
                  - generic [ref=e252]:
                    - generic [ref=e253]:
                      - generic [ref=e254]:
                        - generic [ref=e255]: ORDER PLACED
                        - text: 2026-06-25
                      - generic [ref=e256]:
                        - generic [ref=e257]: TOTAL
                        - generic [ref=e258]: ₹2,150.00
                      - generic [ref=e259]:
                        - generic [ref=e260]: SHIP TO
                        - text: Jane Doe
                    - generic [ref=e261]:
                      - generic [ref=e262]: "ORDER # ORD-2026-7715"
                      - generic [ref=e263]: Delivered
                  - generic [ref=e264]:
                    - generic [ref=e265]:
                      - generic [ref=e266]:
                        - generic [ref=e267]: 🦁
                        - generic [ref=e268]:
                          - heading "Lion's Mane Mushroom Grow Kit" [level=4] [ref=e269]
                          - paragraph [ref=e270]: "Qty: 1 · Price: ₹1,450.00"
                          - generic [ref=e271]: "SKU: SKU-LM-KIT-STD"
                      - generic [ref=e272]:
                        - generic [ref=e273]: 💦
                        - generic [ref=e274]:
                          - heading "Mushroom Spray Mister (300ml)" [level=4] [ref=e275]
                          - paragraph [ref=e276]: "Qty: 1 · Price: ₹500.00"
                          - generic [ref=e277]: "SKU: SKU-MIS-300ML"
                    - generic [ref=e278]:
                      - generic [ref=e279]:
                        - img [ref=e280]
                        - generic [ref=e285]: Delivered on 2026-06-28
                      - generic [ref=e287]:
                        - text: "Payment Method:"
                        - strong [ref=e288]: Credit Card (Visa)
                        - text: (Paid)
                  - generic [ref=e289]:
                    - generic [ref=e290]:
                      - button "View Details" [ref=e291]
                      - button "Return / Refund" [ref=e292]
                    - generic [ref=e293]:
                      - button "Invoice" [ref=e294] [cursor=pointer]:
                        - img [ref=e295]
                        - text: Invoice
                      - link "Support" [ref=e298]:
                        - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-7715
                        - img [ref=e299]
                        - text: Support
                - article [ref=e302]:
                  - generic [ref=e303]:
                    - generic [ref=e304]:
                      - generic [ref=e305]:
                        - generic [ref=e306]: ORDER PLACED
                        - text: 2026-05-18
                      - generic [ref=e307]:
                        - generic [ref=e308]: TOTAL
                        - generic [ref=e309]: ₹1,200.00
                      - generic [ref=e310]:
                        - generic [ref=e311]: SHIP TO
                        - text: Jane Doe
                    - generic [ref=e312]:
                      - generic [ref=e313]: "ORDER # ORD-2026-5541"
                      - generic [ref=e314]: Refunded
                  - generic [ref=e315]:
                    - generic [ref=e317]:
                      - generic [ref=e318]: 🧫
                      - generic [ref=e319]:
                        - heading "Pre-poured MEA Agar Plates (Pack of 20)" [level=4] [ref=e320]
                        - paragraph [ref=e321]: "Qty: 1 · Price: ₹1,100.00"
                        - generic [ref=e322]: "SKU: SKU-AGR-MEA-20P"
                    - generic [ref=e323]:
                      - generic [ref=e324]:
                        - img [ref=e325]
                        - generic [ref=e330]:
                          - text: "Estimated Delivery:"
                          - strong [ref=e331]: 2026-05-21
                          - text: via Delhivery
                      - generic [ref=e333]:
                        - text: "Payment Method:"
                        - strong [ref=e334]: UPI (GPay)
                        - text: (Refunded)
                  - generic [ref=e335]:
                    - button "View Details" [ref=e337]
                    - generic [ref=e338]:
                      - button "Invoice" [ref=e339] [cursor=pointer]:
                        - img [ref=e340]
                        - text: Invoice
                      - link "Support" [ref=e343]:
                        - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-5541
                        - img [ref=e344]
                        - text: Support
                - article [ref=e347]:
                  - generic [ref=e348]:
                    - generic [ref=e349]:
                      - generic [ref=e350]:
                        - generic [ref=e351]: ORDER PLACED
                        - text: 2026-07-13
                      - generic [ref=e352]:
                        - generic [ref=e353]: TOTAL
                        - generic [ref=e354]: ₹4,800.00
                      - generic [ref=e355]:
                        - generic [ref=e356]: SHIP TO
                        - text: Jane Doe
                    - generic [ref=e357]:
                      - generic [ref=e358]: "ORDER # ORD-2026-9922"
                      - generic [ref=e359]: Processing
                  - generic [ref=e360]:
                    - generic [ref=e362]:
                      - generic [ref=e363]: 🍄
                      - generic [ref=e364]:
                        - heading "Premium Cultivar Starter Kit" [level=4] [ref=e365]
                        - paragraph [ref=e366]: "Qty: 1 · Price: ₹4,500.00"
                        - generic [ref=e367]: "SKU: SKU-KIT-PRM-STR"
                    - generic [ref=e368]:
                      - generic [ref=e369]:
                        - img [ref=e370]
                        - generic [ref=e375]:
                          - text: "Estimated Delivery:"
                          - strong [ref=e376]: 2026-07-16
                          - text: via Delhivery
                      - generic [ref=e378]:
                        - text: "Payment Method:"
                        - strong [ref=e379]: Netbanking
                        - text: (Paid)
                  - generic [ref=e380]:
                    - button "View Details" [ref=e382]
                    - generic [ref=e383]:
                      - button "Invoice" [ref=e384] [cursor=pointer]:
                        - img [ref=e385]
                        - text: Invoice
                      - link "Support" [ref=e388]:
                        - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-9922
                        - img [ref=e389]
                        - text: Support
          - generic [ref=e392]:
            - generic [ref=e393]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e394]:
              - link "Privacy" [ref=e395]:
                - /url: /privacy-policy
              - link "Terms" [ref=e396]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e397]:
                - /url: /support
```

# Test source

```ts
  587 | 
  588 |   test('Images have alt text on orders dashboard', async ({ page }) => {
  589 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  590 |     const imgs = page.locator('img');
  591 |     const c = await imgs.count();
  592 |     let missing = 0;
  593 |     for (let i = 0; i < c; i++) {
  594 |       const alt = await imgs.nth(i).getAttribute('alt');
  595 |       if (alt === null || alt === undefined) missing++;
  596 |     }
  597 |     expect(missing).toBe(0);
  598 |   });
  599 | 
  600 |   test('Orders dashboard heading is descriptive', async ({ page }) => {
  601 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  602 |     const h1 = page.locator('h1');
  603 |     expect(await h1.isVisible()).toBeTruthy();
  604 |     const text = await h1.innerText();
  605 |     expect(text.length).toBeGreaterThan(0);
  606 |   });
  607 | 
  608 |   test('Tab order is preserved on orders page', async ({ page }) => {
  609 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  610 |     const focusable = page.locator('button, a, input, select, textarea, [tabindex]:not([tabindex="-1"])');
  611 |     const count = await focusable.count();
  612 |     expect(count).toBeGreaterThan(0);
  613 |   });
  614 | });
  615 | 
  616 | // ============================================================================
  617 | // PHASE 11 — PERFORMANCE
  618 | // ============================================================================
  619 | test.describe('Phase 11 — Performance', () => {
  620 |   test('Order history loads within 15s', async ({ page }) => {
  621 |     const start = Date.now();
  622 |     await login(page);
  623 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  624 |     expect(Date.now() - start).toBeLessThan(20000);
  625 |   });
  626 | 
  627 |   test('Order detail loads within 15s', async ({ page }) => {
  628 |     const start = Date.now();
  629 |     await login(page);
  630 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  631 |     expect(Date.now() - start).toBeLessThan(20000);
  632 |   });
  633 | 
  634 |   test('No console errors on orders dashboard', async ({ page }) => {
  635 |     const errors = [];
  636 |     page.on('console', m => { if (m.type() === 'error') errors.push(m.text()); });
  637 |     await login(page);
  638 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  639 |     expect(errors.length).toBe(0);
  640 |   });
  641 | 
  642 |   test('No console errors on order detail', async ({ page }) => {
  643 |     const errors = [];
  644 |     page.on('console', m => { if (m.type() === 'error') errors.push(m.text()); });
  645 |     await login(page);
  646 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  647 |     expect(errors.length).toBe(0);
  648 |   });
  649 | 
  650 |   test('No failed network requests on orders', async ({ page }) => {
  651 |     const fails = [];
  652 |     page.on('requestfailed', r => fails.push(r.url()));
  653 |     await login(page);
  654 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  655 |     expect(fails.length).toBe(0);
  656 |   });
  657 | 
  658 |   test('Repeated navigation is performant', async ({ page }) => {
  659 |     await login(page);
  660 |     for (let i = 0; i < 3; i++) {
  661 |       const s = Date.now();
  662 |       await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  663 |       await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  664 |       expect(Date.now() - s).toBeLessThan(25000);
  665 |     }
  666 |   });
  667 | });
  668 | 
  669 | // ============================================================================
  670 | // PHASE 12 — VISUAL REVIEW
  671 | // ============================================================================
  672 | test.describe('Phase 12 — Visual Review', () => {
  673 |   test.beforeEach(async ({ page }) => { await login(page); });
  674 | 
  675 |   test('Orders dashboard has consistent typography', async ({ page }) => {
  676 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  677 |     const h1 = page.locator('h1').first();
  678 |     if (await h1.isVisible().catch(() => false)) {
  679 |       const fs = await h1.evaluate(el => getComputedStyle(el).fontSize);
  680 |       expect(parseFloat(fs)).toBeGreaterThan(16);
  681 |     }
  682 |   });
  683 | 
  684 |   test('No horizontal scroll on orders dashboard', async ({ page }) => {
  685 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  686 |     const hs = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
> 687 |     expect(hs).toBe(false);
      |                ^ Error: expect(received).toBe(expected) // Object.is equality
  688 |   });
  689 | 
  690 |   test('Order cards have proper spacing and alignment', async ({ page }) => {
  691 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  692 |     const cards = page.locator('article, [class*="card"], [class*="Card"]');
  693 |     const count = await cards.count();
  694 |     expect(count).toBeGreaterThan(0);
  695 |   });
  696 | 
  697 |   test('Status badges have visible text', async ({ page }) => {
  698 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  699 |     const badges = page.locator('[class*="badge"], [class*="Badge"], [class*="status"], [class*="Status"]');
  700 |     const count = await badges.count();
  701 |     if (count > 0) {
  702 |       const text = await badges.first().innerText();
  703 |       expect(text.length).toBeGreaterThan(0);
  704 |     }
  705 |   });
  706 | });
  707 | 
  708 | // ============================================================================
  709 | // PHASE 13 — EVIDENCE (captured automatically via Playwright config)
  710 | // ============================================================================
  711 | test.describe('Phase 13 — Evidence Collection', () => {
  712 |   test('Screenshots captured automatically (config: screenshot=on)', async ({ page }) => {
  713 |     // Evidence collection is configured in playwright.config.ts:
  714 |     // screenshot: 'on', video: 'on', trace: { mode: 'on', snapshots: true, screenshots: true }
  715 |     await login(page);
  716 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  717 |     expect(true).toBeTruthy();
  718 |   });
  719 | 
  720 |   test('Traces captured automatically (config: trace=on)', async ({ page }) => {
  721 |     await login(page);
  722 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  723 |     expect(true).toBeTruthy();
  724 |   });
  725 | });
  726 | 
```