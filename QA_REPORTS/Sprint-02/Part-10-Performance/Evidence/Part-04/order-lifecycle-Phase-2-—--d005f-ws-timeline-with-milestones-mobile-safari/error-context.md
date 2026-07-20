# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 2 — Order Details >> Order shows timeline with milestones
- Location: tests\order-lifecycle.spec.ts:105:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
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
            - heading "ORD-2026-8842" [level=1] [ref=e122]
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
                - link "Orders" [ref=e133]:
                  - /url: /dashboard/orders
                  - generic [ref=e134]: Orders
              - listitem [ref=e135]:
                - img [ref=e136]
              - listitem [ref=e138]:
                - generic [ref=e140]: ORD-2026-8842
          - generic [ref=e142]:
            - generic [ref=e143]:
              - button "Back to Orders" [ref=e144] [cursor=pointer]:
                - img [ref=e145]
                - text: Back to Orders
              - generic [ref=e147]:
                - button "Download Invoice" [ref=e148]:
                  - img [ref=e149]
                  - text: Download Invoice
                - button "Reorder Items" [ref=e152]
            - generic [ref=e153]:
              - generic [ref=e154]:
                - heading "Order details" [level=2] [ref=e155]
                - paragraph [ref=e156]:
                  - text: "Placed on 2026-07-11 · Transaction ID:"
                  - code [ref=e157]: TXN-99884210398
              - generic [ref=e158]:
                - generic [ref=e159]: "Status:"
                - generic [ref=e160]: In Transit
            - generic [ref=e161]:
              - generic [ref=e162]:
                - article [ref=e163]:
                  - generic [ref=e164]:
                    - 'heading "Order Lifecycle Status: In Transit" [level=3] [ref=e165]'
                    - generic [ref=e166]:
                      - generic [ref=e170]:
                        - generic [ref=e171]:
                          - heading "Order Created" [level=4] [ref=e172]
                          - generic [ref=e173]: 2026-07-11 14:30
                        - paragraph [ref=e174]: Order received and verified.
                      - generic [ref=e178]:
                        - generic [ref=e179]:
                          - heading "Payment Received" [level=4] [ref=e180]
                          - generic [ref=e181]: 2026-07-11 14:32
                        - paragraph [ref=e182]: Payment of ₹3,450.00 processed successfully via UPI.
                      - generic [ref=e186]:
                        - generic [ref=e187]:
                          - heading "Processing" [level=4] [ref=e188]
                          - generic [ref=e189]: 2026-07-12 09:15
                        - paragraph [ref=e190]: Spawn batch checked, bags sterilized and prepared.
                      - generic [ref=e194]:
                        - generic [ref=e195]:
                          - heading "Quality Check" [level=4] [ref=e196]
                          - generic [ref=e197]: 2026-07-12 11:30
                        - paragraph [ref=e198]: Passed visual inspection for contamination & moisture level.
                      - generic [ref=e202]:
                        - generic [ref=e203]:
                          - heading "Dispatched" [level=4] [ref=e204]
                          - generic [ref=e205]: 2026-07-12 15:45
                        - paragraph [ref=e206]: Handed over to courier partner Delhivery.
                      - generic [ref=e210]:
                        - generic [ref=e211]:
                          - heading "In Transit" [level=4] [ref=e212]
                          - generic [ref=e213]: 2026-07-13 18:20
                        - paragraph [ref=e214]: Shipment has departed sorting facility in Bengaluru Hub.
                      - generic [ref=e217]:
                        - generic [ref=e218]:
                          - heading "Out for Delivery" [level=4] [ref=e219]
                          - generic [ref=e220]: "--"
                        - paragraph [ref=e221]: Courier will deliver to HSR Layout area.
                      - generic [ref=e224]:
                        - generic [ref=e225]:
                          - heading "Delivered" [level=4] [ref=e226]
                          - generic [ref=e227]: "--"
                        - paragraph [ref=e228]: Secure OTP-based delivery to recipient.
                - article [ref=e229]:
                  - heading "Products Purchased (3)" [level=3] [ref=e230]
                  - generic [ref=e231]:
                    - generic [ref=e232]:
                      - generic [ref=e233]:
                        - generic [ref=e234]: 🍄
                        - generic [ref=e235]:
                          - heading "Pink Oyster Mushroom Grain Spawn (2kg)" [level=4] [ref=e236]
                          - generic [ref=e237]: "SKU: SKU-PO-GRN-2KG"
                          - generic [ref=e238]: "Qty: 2 · Price: ₹1,100.00"
                      - strong [ref=e239]: ₹2,200.00
                    - generic [ref=e240]:
                      - generic [ref=e241]:
                        - generic [ref=e242]: 🛍️
                        - generic [ref=e243]:
                          - heading "Autoclavable Spawn Bags (Pack of 10)" [level=4] [ref=e244]
                          - generic [ref=e245]: "SKU: SKU-BAG-ACV-10P"
                          - generic [ref=e246]: "Qty: 1 · Price: ₹1,000.00"
                      - strong [ref=e247]: ₹1,000.00
                - generic [ref=e248]:
                  - article [ref=e249]:
                    - heading "Shipping Address" [level=4] [ref=e250]:
                      - img [ref=e251]
                      - text: Shipping Address
                    - paragraph [ref=e254]:
                      - strong [ref=e255]: Jane Doe
                      - text: Flat 402, Green Meadows
                      - text: HSR Layout, Sector 3
                      - text: Bengaluru, Karnataka - 560102
                      - text: India
                      - text: "Phone: +91 98765 43210"
                  - article [ref=e256]:
                    - heading "Billing Address" [level=4] [ref=e257]:
                      - img [ref=e258]
                      - text: Billing Address
                    - paragraph [ref=e260]:
                      - strong [ref=e261]: Jane Doe
                      - text: Flat 402, Green Meadows
                      - text: HSR Layout, Sector 3
                      - text: Bengaluru, Karnataka - 560102
                      - text: India
                      - text: "Phone: +91 98765 43210"
              - generic [ref=e262]:
                - article [ref=e263]:
                  - heading "Order summary" [level=3] [ref=e264]
                  - generic [ref=e265]:
                    - generic [ref=e266]:
                      - generic [ref=e267]: Subtotal
                      - generic [ref=e268]: ₹3,200.00
                    - generic [ref=e269]:
                      - generic [ref=e270]: GST (Tax)
                      - generic [ref=e271]: ₹150.00
                    - generic [ref=e272]:
                      - generic [ref=e273]: Shipping Charges
                      - generic [ref=e274]: ₹200.00
                    - generic [ref=e275]:
                      - generic [ref=e276]: Discount (SPOREGROW100)
                      - generic [ref=e277]: "-₹100.00"
                    - generic [ref=e278]:
                      - generic [ref=e279]: Grand Total
                      - generic [ref=e280]: ₹3,450.00
                - article [ref=e281]:
                  - generic [ref=e282]:
                    - heading "Fulfillment Status" [level=4] [ref=e283]
                    - paragraph [ref=e284]:
                      - text: "Courier:"
                      - strong [ref=e285]: Delhivery
                    - paragraph [ref=e286]:
                      - text: "Tracking Number:"
                      - code [ref=e287]: DEL-2026-8842099
                  - button "Track Live Shipment" [ref=e288]:
                    - img [ref=e289]
                    - text: Track Live Shipment
                - article [ref=e294]:
                  - heading "Payment Details" [level=4] [ref=e295]
                  - paragraph [ref=e296]:
                    - text: "Method:"
                    - strong [ref=e297]: UPI (Razorpay)
                    - text: "Status: Paid"
                    - text: "Date: 2026-07-11 14:32:01"
                  - button "Download Receipt" [ref=e298] [cursor=pointer]:
                    - img [ref=e299]
                    - text: Download Receipt
                - article [ref=e302]:
                  - heading "Returns & Refunds" [level=4] [ref=e303]
                  - paragraph [ref=e304]: This order is not currently eligible for return or refund requests.
                - article [ref=e305]:
                  - heading "Need Help?" [level=4] [ref=e306]
                  - paragraph [ref=e307]: Have questions about cultivars, shipping delay, or payment?
                  - link "Contact SporeCare" [ref=e308]:
                    - /url: /dashboard/support?subject=Order%20Help%20ORD-2026-8842
                    - img [ref=e309]
                    - text: Contact SporeCare
          - generic [ref=e311]:
            - generic [ref=e312]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e313]:
              - link "Privacy" [ref=e314]:
                - /url: /privacy-policy
              - link "Terms" [ref=e315]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e316]:
                - /url: /support
```

# Test source

```ts
  8   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  9   |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  10  |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  11  |   await inp.fill(PHONE);
  12  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  13  |   await btn.click(); await page.waitForTimeout(2000);
  14  |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  15  |   const n = await otp.count();
  16  |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  17  | }
  18  | 
  19  | async function setRole(page, role) {
  20  |   await page.goto('/'); await page.waitForLoadState('networkidle');
  21  |   const sel = page.locator('select[aria-label="Switch review role"]');
  22  |   if (await sel.isVisible().catch(() => false)) await sel.selectOption(role);
  23  | }
  24  | 
  25  | // ============================================================================
  26  | // PHASE 1 — ORDER CREATION VALIDATION
  27  | // ============================================================================
  28  | test.describe('Phase 1 — Order Creation', () => {
  29  |   test('IMPLEMENTATION GAP: No order creation UI exists', async ({ page }) => {
  30  |     // Cart and checkout are placeholders — no way to create orders
  31  |     await page.goto('/cart', { waitUntil: 'networkidle' });
  32  |     const t = await page.locator('body').innerText();
  33  |     expect(t.includes('Cart — empty panel') || t.includes('Navigation Prototype')).toBeTruthy();
  34  |   });
  35  | 
  36  |   test('IMPLEMENTATION GAP: No checkout flow for order submission', async ({ page }) => {
  37  |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  38  |     const t = await page.locator('body').innerText();
  39  |     expect(t.includes('Checkout — empty panel') || t.includes('Navigation Prototype')).toBeTruthy();
  40  |   });
  41  | 
  42  |   test('No order creation API endpoint exposed', async ({ page }) => {
  43  |     const resp = await page.request.post('/api/orders', { data: {} });
  44  |     expect(resp.status() === 404 || resp.status() === 405).toBeTruthy();
  45  |   });
  46  | });
  47  | 
  48  | // ============================================================================
  49  | // PHASE 2 — ORDER DETAILS
  50  | // ============================================================================
  51  | test.describe('Phase 2 — Order Details', () => {
  52  |   test.beforeEach(async ({ page }) => { await login(page); });
  53  | 
  54  |   for (const orderId of TEST_ORDERS) {
  55  |     test(`Order ${orderId} loads with correct page content`, async ({ page }) => {
  56  |       await page.goto(`/dashboard/orders/${orderId}`, { waitUntil: 'networkidle' });
  57  |       const t = await page.locator('body').innerText();
  58  |       expect(t.length).toBeGreaterThan(50);
  59  |       expect(t.includes(orderId)).toBeTruthy();
  60  |     });
  61  |   }
  62  | 
  63  |   test('Order shows pricing with INR currency', async ({ page }) => {
  64  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  65  |     const t = await page.locator('body').innerText();
  66  |     expect(t.includes('INR') || t.includes('₹') || t.includes('Total') || t.includes('total')).toBeTruthy();
  67  |   });
  68  | 
  69  |   test('Order shows subtotal and tax breakdown', async ({ page }) => {
  70  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  71  |     const t = await page.locator('body').innerText();
  72  |     expect(t.includes('Subtotal') || t.includes('subtotal') || t.includes('Tax') || t.includes('GST')).toBeTruthy();
  73  |   });
  74  | 
  75  |   test('Order shows items with quantity and SKU', async ({ page }) => {
  76  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  77  |     const t = await page.locator('body').innerText();
  78  |     expect(t.includes('Qty') || t.includes('SKU') || t.includes('sku')).toBeTruthy();
  79  |   });
  80  | 
  81  |   test('Order shows discount/coupon info', async ({ page }) => {
  82  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  83  |     const t = await page.locator('body').innerText();
  84  |     expect(t.includes('Discount') || t.includes('discount') || t.includes('Coupon')).toBeTruthy();
  85  |   });
  86  | 
  87  |   test('Order shows payment information', async ({ page }) => {
  88  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  89  |     const t = await page.locator('body').innerText();
  90  |     expect(t.includes('Payment') || t.includes('payment') || t.includes('Razorpay') || t.includes('UPI')).toBeTruthy();
  91  |   });
  92  | 
  93  |   test('Order shows shipping address', async ({ page }) => {
  94  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  95  |     const t = await page.locator('body').innerText();
  96  |     expect(t.includes('Shipping') || t.includes('shipping') || t.includes('Address') || t.includes('address')).toBeTruthy();
  97  |   });
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
> 108 |     expect(t.includes('Timeline') || t.includes('milestone')).toBeTruthy();
      |                                                               ^ Error: expect(received).toBeTruthy()
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
  198 |       expect(t.includes(oid)).toBeTruthy();
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
```