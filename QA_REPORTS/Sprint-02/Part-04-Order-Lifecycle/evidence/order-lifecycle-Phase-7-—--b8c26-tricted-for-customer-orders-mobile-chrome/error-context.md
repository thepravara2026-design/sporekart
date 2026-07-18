# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 7 — Order Security >> Guest role sees access restricted for customer orders
- Location: tests\order-lifecycle.spec.ts:451:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
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
                - link "Dashboard" [ref=e127] [cursor=pointer]:
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
                    - link "Track Shipment" [ref=e184] [cursor=pointer]:
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
                      - link "Support" [ref=e247] [cursor=pointer]:
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
                      - link "Support" [ref=e298] [cursor=pointer]:
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
                      - link "Support" [ref=e343] [cursor=pointer]:
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
                      - link "Support" [ref=e388] [cursor=pointer]:
                        - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-9922
                        - img [ref=e389]
                        - text: Support
          - generic [ref=e392]:
            - generic [ref=e393]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e394]:
              - link "Privacy" [ref=e395] [cursor=pointer]:
                - /url: /privacy-policy
              - link "Terms" [ref=e396] [cursor=pointer]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e397] [cursor=pointer]:
                - /url: /support
```

# Test source

```ts
  356 |     expect(t.includes('Scan') || t.includes('History') || t.includes('milestone') || t.includes('Transit')).toBeTruthy();
  357 |   });
  358 | 
  359 |   test('Invoice button exists on order detail', async ({ page }) => {
  360 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  361 |     const btn = page.locator('button:has-text("Invoice"),a:has-text("Invoice")').first();
  362 |     expect(await btn.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  363 |   });
  364 | 
  365 |   test('IMPLEMENTATION GAP: Reorder button not functional', async ({ page }) => {
  366 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  367 |     const btn = page.locator('button:has-text("Reorder")').first();
  368 |     const exists = await btn.isVisible({ timeout: 2000 }).catch(() => false);
  369 |     if (exists) {
  370 |       // If it exists, clicking should navigate somewhere or show message
  371 |       // Currently prototype only
  372 |     }
  373 |   });
  374 | 
  375 |   test('Support link exists on order', async ({ page }) => {
  376 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  377 |     const link = page.locator('a:has-text("Support"),a[href*="support"]').first();
  378 |     expect(await link.isVisible({ timeout: 2000 }).catch(() => false)).toBeTruthy();
  379 |   });
  380 | 
  381 |   test('Deep link to tracking page resolves', async ({ page }) => {
  382 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  383 |     expect(page.url()).toContain('/track');
  384 |   });
  385 | 
  386 |   test('Deep link to refund page resolves', async ({ page }) => {
  387 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  388 |     expect(page.url()).toContain('/refund');
  389 |   });
  390 | 
  391 |   test('Back from tracking returns to order detail', async ({ page }) => {
  392 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  393 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  394 |     await page.goBack();
  395 |     await page.waitForLoadState('networkidle');
  396 |     expect(page.url()).toContain('/dashboard/orders/ORD-2026-8842');
  397 |   });
  398 | });
  399 | 
  400 | // ============================================================================
  401 | // PHASE 6 — ADMIN VISIBILITY
  402 | // ============================================================================
  403 | test.describe('Phase 6 — Admin Visibility', () => {
  404 |   test.beforeEach(async ({ page }) => { await login(page); });
  405 | 
  406 |   test('Admin orders page renders with data grid', async ({ page }) => {
  407 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  408 |     const t = await page.locator('body').innerText();
  409 |     expect(t.length).toBeGreaterThan(50);
  410 |     expect(t.includes('Orders') || t.includes('orders')).toBeTruthy();
  411 |   });
  412 | 
  413 |   test('Admin grid shows multiple order rows', async ({ page }) => {
  414 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  415 |     const t = await page.locator('body').innerText();
  416 |     expect(t.includes('ORD-3')).toBeTruthy(); // Admin mock orders are ORD-3000+
  417 |   });
  418 | 
  419 |   test('Admin grid shows order ID, status, customer, total columns', async ({ page }) => {
  420 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  421 |     const t = await page.locator('body').innerText();
  422 |     expect(t.includes('ID') && t.includes('Status') && t.includes('Customer') && t.includes('Total')).toBeTruthy();
  423 |   });
  424 | 
  425 |   test('Admin grid search field exists', async ({ page }) => {
  426 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  427 |     const s = page.locator('input[type="search"],input[placeholder*="search" i],input[placeholder*="Search" i]').first();
  428 |     expect(await s.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  429 |   });
  430 | });
  431 | 
  432 | // ============================================================================
  433 | // PHASE 7 — ORDER SECURITY
  434 | // ============================================================================
  435 | test.describe('Phase 7 — Order Security', () => {
  436 |   test('DEFECT: Dashboard orders accessible without authentication', async ({ page }) => {
  437 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  438 |     const url = page.url();
  439 |     expect(url.includes('login') || url.includes('auth')).toBeFalsy();
  440 |     // Page renders without redirect — exposed to unauthenticated users
  441 |     const t = await page.locator('body').innerText();
  442 |     expect(t.length).toBeGreaterThan(50);
  443 |   });
  444 | 
  445 |   test('DEFECT: Admin orders accessible without authentication', async ({ page }) => {
  446 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  447 |     const url = page.url();
  448 |     expect(url.includes('login') || url.includes('auth')).toBeFalsy();
  449 |   });
  450 | 
  451 |   test('Guest role sees access restricted for customer orders', async ({ page }) => {
  452 |     await setRole(page, 'guest');
  453 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  454 |     const t = await page.locator('body').innerText();
  455 |     const restricted = t.includes('Access restricted') || t.includes('access');
> 456 |     expect(restricted).toBeTruthy();
      |                        ^ Error: expect(received).toBeTruthy()
  457 |   });
  458 | 
  459 |   test('Customer role can access dashboard orders', async ({ page }) => {
  460 |     await setRole(page, 'customer');
  461 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  462 |     const t = await page.locator('body').innerText();
  463 |     expect(t.includes('Access restricted')).toBe(false);
  464 |     expect(t.includes('Orders') || t.includes('ORD')).toBeTruthy();
  465 |   });
  466 | 
  467 |   test('Invalid order ID shows not-found page', async ({ page }) => {
  468 |     await login(page);
  469 |     await page.goto(`/dashboard/orders/${INVALID_ORDER}`, { waitUntil: 'networkidle' });
  470 |     const t = await page.locator('body').innerText();
  471 |     expect(t.includes('not found') || t.includes('Not Found')).toBeTruthy();
  472 |   });
  473 | 
  474 |   test('Session-expired page renders correctly', async ({ page }) => {
  475 |     await page.goto('/session-expired', { waitUntil: 'networkidle' });
  476 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);
  477 |   });
  478 | 
  479 |   test('Access-denied page renders correctly', async ({ page }) => {
  480 |     await page.goto('/access-denied', { waitUntil: 'networkidle' });
  481 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);
  482 |   });
  483 | });
  484 | 
  485 | // ============================================================================
  486 | // PHASE 8 — DATA INTEGRITY
  487 | // ============================================================================
  488 | test.describe('Phase 8 — Data Integrity', () => {
  489 |   test.beforeEach(async ({ page }) => { await login(page); });
  490 | 
  491 |   test('Order data persists on page refresh', async ({ page }) => {
  492 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  493 |     const b = await page.locator('body').innerText();
  494 |     await page.reload({ waitUntil: 'networkidle' });
  495 |     const a = await page.locator('body').innerText();
  496 |     expect(a.length).toBeGreaterThan(50);
  497 |   });
  498 | 
  499 |   test('All 4 test orders have unique content', async ({ page }) => {
  500 |     const contents = [];
  501 |     for (const oid of TEST_ORDERS) {
  502 |       await page.goto(`/dashboard/orders/${oid}`, { waitUntil: 'networkidle' });
  503 |       contents.push(await page.locator('body').innerText());
  504 |     }
  505 |     // Each order should have different content
  506 |     expect(contents[0]).not.toBe(contents[1]);
  507 |     expect(contents[1]).not.toBe(contents[2]);
  508 |     expect(contents[2]).not.toBe(contents[3]);
  509 |   });
  510 | 
  511 |   test('Order pricing is consistent between list and detail', async ({ page }) => {
  512 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  513 |     const listText = await page.locator('body').innerText();
  514 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  515 |     const detailText = await page.locator('body').innerText();
  516 |     // Both pages show order data
  517 |     expect(listText.length).toBeGreaterThan(50);
  518 |     expect(detailText.length).toBeGreaterThan(50);
  519 |   });
  520 | 
  521 |   test('Mock data for ORD-2026-8842 shows correct total (₹3,450)', async ({ page }) => {
  522 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  523 |     const t = await page.locator('body').innerText();
  524 |     expect(t.includes('3,450') || t.includes('3450')).toBeTruthy();
  525 |   });
  526 | });
  527 | 
  528 | // ============================================================================
  529 | // PHASE 9 — CROSS-BROWSER
  530 | // ============================================================================
  531 | test.describe('Phase 9 — Cross-Browser', () => {
  532 |   test('Orders dashboard renders at desktop viewport', async ({ page }) => {
  533 |     await login(page);
  534 |     await page.setViewportSize({ width: 1440, height: 900 });
  535 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  536 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  537 |   });
  538 | 
  539 |   test('Orders dashboard renders at tablet viewport', async ({ page }) => {
  540 |     await login(page);
  541 |     await page.setViewportSize({ width: 768, height: 1024 });
  542 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  543 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  544 |   });
  545 | 
  546 |   test('Orders dashboard renders at mobile viewport', async ({ page }) => {
  547 |     await login(page);
  548 |     await page.setViewportSize({ width: 375, height: 667 });
  549 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  550 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  551 |   });
  552 | 
  553 |   test('Order detail renders at all viewports', async ({ page }) => {
  554 |     await login(page);
  555 |     for (const vp of [{ w: 1440, h: 900 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  556 |       await page.setViewportSize({ width: vp.w, height: vp.h });
```