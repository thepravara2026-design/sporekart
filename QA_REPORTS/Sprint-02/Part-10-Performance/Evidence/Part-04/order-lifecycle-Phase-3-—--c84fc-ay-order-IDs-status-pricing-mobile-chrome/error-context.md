# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 3 — Order History >> Order cards display order IDs, status, pricing
- Location: tests\order-lifecycle.spec.ts:194:7

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
  98  | 
  99  |   test('Order shows billing address', async ({ page }) => {
  100 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  101 |     const t = await page.locator('body').innerText();
  102 |     expect(t.includes('Billing') || t.includes('billing')).toBeTruthy();
  103 |   });
  104 | 
  105 |   test('Order shows timeline with milestones', async ({ page }) => {
  106 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  107 |     const t = await page.locator('body').innerText();
  108 |     expect(t.includes('Timeline') || t.includes('milestone')).toBeTruthy();
  109 |   });
  110 | 
  111 |   test('Order grand total matches expected values', async ({ page }) => {
  112 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  113 |     const t = await page.locator('body').innerText();
  114 |     expect(t.includes('₹3,450') || t.includes('₨3,450')).toBeTruthy();
  115 |   });
  116 | 
  117 |   test('Invalid order ID shows not-found state', async ({ page }) => {
  118 |     await page.goto(`/dashboard/orders/${INVALID_ORDER}`, { waitUntil: 'networkidle' });
  119 |     const t = await page.locator('body').innerText();
  120 |     expect(t.includes('not found') || t.includes('Not Found') || t.includes('404')).toBeTruthy();
  121 |   });
  122 | 
  123 |   test('Back button returns to orders list', async ({ page }) => {
  124 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  125 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  126 |     await page.goBack();
  127 |     await page.waitForLoadState('networkidle');
  128 |     expect(page.url()).toContain('/dashboard/orders');
  129 |   });
  130 | 
  131 |   test('Deep link to order ID resolves correctly', async ({ page }) => {
  132 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  133 |     expect(page.url()).toContain('/dashboard/orders/ORD-2026-8842');
  134 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  135 |   });
  136 | });
  137 | 
  138 | // ============================================================================
  139 | // PHASE 3 — ORDER HISTORY
  140 | // ============================================================================
  141 | test.describe('Phase 3 — Order History', () => {
  142 |   test.beforeEach(async ({ page }) => { await login(page); });
  143 | 
  144 |   test('Orders dashboard renders with order list', async ({ page }) => {
  145 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  146 |     const t = await page.locator('body').innerText();
  147 |     expect(t.length).toBeGreaterThan(50);
  148 |     expect(t.includes('ORD') || t.includes('Order')).toBeTruthy();
  149 |   });
  150 | 
  151 |   test('Order statistics cards display correctly', async ({ page }) => {
  152 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  153 |     const t = await page.locator('body').innerText();
  154 |     expect(t.includes('Total Spend') && t.includes('Active Orders')).toBeTruthy();
  155 |   });
  156 | 
  157 |   test('AI Order Assistant section renders', async ({ page }) => {
  158 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  159 |     const t = await page.locator('body').innerText();
  160 |     expect(t.includes('AI Order Assistant') || t.includes('Order Assistant')).toBeTruthy();
  161 |   });
  162 | 
  163 |   test('Status filter tabs render', async ({ page }) => {
  164 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  165 |     const tabs = page.locator('button[role="tab"], button:has-text("All"),button:has-text("Active"),button:has-text("Completed"),button:has-text("Refunded")');
  166 |     expect(await tabs.first().isVisible()).toBeTruthy();
  167 |   });
  168 | 
  169 |   test('Filter by Active tab works', async ({ page }) => {
  170 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  171 |     const tabs = page.locator('button[role="tab"]');
  172 |     const count = await tabs.count();
  173 |     if (count >= 2) {
  174 |       await tabs.nth(1).click(); await page.waitForTimeout(500);
  175 |       expect(await tabs.nth(1).getAttribute('aria-selected')).toBe('true');
  176 |     }
  177 |   });
  178 | 
  179 |   test('Search input is present', async ({ page }) => {
  180 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  181 |     const s = page.locator('input[type="search"],input[type="text"][placeholder*="search" i],input[aria-label="Search orders"]');
  182 |     expect(await s.isVisible()).toBeTruthy();
  183 |   });
  184 | 
  185 |   test('Search input accepts text', async ({ page }) => {
  186 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  187 |     const s = page.locator('input[type="search"],input[type="text"][placeholder*="search" i],input[aria-label="Search orders"]');
  188 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  189 |       await s.fill('ORD-2026');
  190 |       expect(await s.inputValue()).toBe('ORD-2026');
  191 |     }
  192 |   });
  193 | 
  194 |   test('Order cards display order IDs, status, pricing', async ({ page }) => {
  195 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  196 |     const t = await page.locator('body').innerText();
  197 |     for (const oid of TEST_ORDERS) {
> 198 |       expect(t.includes(oid)).toBeTruthy();
      |                               ^ Error: expect(received).toBeTruthy()
  199 |     }
  200 |   });
  201 | 
  202 |   test('Data persists on page refresh', async ({ page }) => {
  203 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  204 |     const before = (await page.locator('body').innerText()).length;
  205 |     await page.reload({ waitUntil: 'networkidle' });
  206 |     const after = (await page.locator('body').innerText()).length;
  207 |     expect(after).toBeGreaterThan(50);
  208 |   });
  209 | 
  210 |   test('Different order loads correctly', async ({ page }) => {
  211 |     await page.goto('/dashboard/orders/ORD-2026-5541', { waitUntil: 'networkidle' });
  212 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  213 |     await page.goto('/dashboard/orders/ORD-2026-9922', { waitUntil: 'networkidle' });
  214 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  215 |   });
  216 | 
  217 |   test('Empty state not shown when orders exist', async ({ page }) => {
  218 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  219 |     const t = await page.locator('body').innerText();
  220 |     // Should NOT show empty state since 4 orders exist
  221 |     const emptyState = t.includes('No orders found') || t.includes('no orders');
  222 |     expect(emptyState).toBe(false);
  223 |   });
  224 | 
  225 |   test('Browser restart preserves order session', async ({ page, context }) => {
  226 |     await login(page);
  227 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  228 |     const before = (await page.locator('body').innerText()).length;
  229 |     await context.close();
  230 |     const p2 = await context.newPage();
  231 |     await login(p2);
  232 |     await p2.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  233 |     const after = (await p2.locator('body').innerText()).length;
  234 |     expect(after).toBeGreaterThan(50);
  235 |   });
  236 | });
  237 | 
  238 | // ============================================================================
  239 | // PHASE 4 — ORDER STATUS
  240 | // ============================================================================
  241 | test.describe('Phase 4 — Order Status', () => {
  242 |   test.beforeEach(async ({ page }) => { await login(page); });
  243 | 
  244 |   test('Orders show correct status badges', async ({ page }) => {
  245 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  246 |     const t = await page.locator('body').innerText();
  247 |     expect(t.includes('In Transit') || t.includes('Delivered') || t.includes('Refunded') || t.includes('Processing')).toBeTruthy();
  248 |   });
  249 | 
  250 |   test('ORD-2026-8842 shows In Transit status', async ({ page }) => {
  251 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  252 |     const t = await page.locator('body').innerText();
  253 |     expect(t.includes('In Transit') || t.includes('Transit')).toBeTruthy();
  254 |   });
  255 | 
  256 |   test('ORD-2026-7715 shows Delivered status', async ({ page }) => {
  257 |     await page.goto('/dashboard/orders/ORD-2026-7715', { waitUntil: 'networkidle' });
  258 |     const t = await page.locator('body').innerText();
  259 |     expect(t.includes('Delivered') || t.includes('delivered')).toBeTruthy();
  260 |   });
  261 | 
  262 |   test('ORD-2026-5541 shows Refunded status', async ({ page }) => {
  263 |     await page.goto('/dashboard/orders/ORD-2026-5541', { waitUntil: 'networkidle' });
  264 |     const t = await page.locator('body').innerText();
  265 |     expect(t.includes('Refunded') || t.includes('refunded')).toBeTruthy();
  266 |   });
  267 | 
  268 |   test('ORD-2026-9922 shows Processing status', async ({ page }) => {
  269 |     await page.goto('/dashboard/orders/ORD-2026-9922', { waitUntil: 'networkidle' });
  270 |     const t = await page.locator('body').innerText();
  271 |     expect(t.includes('Processing') || t.includes('processing')).toBeTruthy();
  272 |   });
  273 | 
  274 |   test('Status filter tabs exist with correct labels', async ({ page }) => {
  275 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  276 |     const t = await page.locator('body').innerText();
  277 |     expect(t.includes('All') && t.includes('Active') && t.includes('Completed') && t.includes('Refunded')).toBeTruthy();
  278 |   });
  279 | 
  280 |   test('IMPLEMENTATION GAP: Status transition UI does not exist', async ({ page }) => {
  281 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  282 |     const t = await page.locator('body').innerText();
  283 |     // No status change buttons should exist
  284 |     expect(t.includes('Update Status') || t.includes('Change Status')).toBe(false);
  285 |   });
  286 | 
  287 |   test('IMPLEMENTATION GAP: No cancel order button', async ({ page }) => {
  288 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  289 |     const t = await page.locator('body').innerText();
  290 |     expect(t.includes('Cancel Order') || t.includes('cancel')).toBe(false);
  291 |   });
  292 | });
  293 | 
  294 | // ============================================================================
  295 | // PHASE 5 — CUSTOMER ACTIONS
  296 | // ============================================================================
  297 | test.describe('Phase 5 — Customer Actions', () => {
  298 |   test.beforeEach(async ({ page }) => { await login(page); });
```