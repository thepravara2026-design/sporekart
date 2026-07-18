# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-validation.spec.ts >> Checkout Entry,Address,Shipping >> Unauth redirect
- Location: tests\checkout-validation.spec.ts:17:7

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
  1  | ﻿import { test, expect } from '@playwright/test';
  2  | const PHONE = '9876543210';
  3  | async function login(page) {
  4  |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  5  |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  6  |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  7  |   await inp.fill(PHONE);
  8  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  9  |   await btn.click(); await page.waitForTimeout(2000);
  10 |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  11 |   const n = await otp.count();
  12 |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  13 | }
  14 | test.describe('Checkout Entry,Address,Shipping', () => {
  15 |   test('Cart 404',async({page})=>{await page.goto('/cart',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  16 |   test('Checkout 404',async({page})=>{await page.goto('/checkout',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
> 17 |   test('Unauth redirect',async({page})=>{await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
     |                                                                                                                                                                           ^ Error: expect(received).toBeTruthy()
  18 |   test('Login works',async({page})=>{await login(page);expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  19 |   test('Orders dashboard',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(50);expect(t.includes('ORD')||t.includes('Order')).toBeTruthy();});
  20 |   test('Order details',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  21 |   test('Shipment tracking',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/track',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  22 |   test('Returns refunds',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/refund',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  23 |   test('Tab filters',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(await page.locator('button:has-text("All"),button:has-text("Active")').first().isVisible().catch(()=>false)).toBeTruthy();});
  24 |   test('Search input',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const s=page.locator('input[type="search"],input[placeholder*="search" i]');if(await s.isVisible({timeout:3000}).catch(()=>false)){await s.fill('ORD-2026');expect(await s.inputValue()).toBe('ORD-2026');}});
  25 |   test('Address placeholder',async({page})=>{await login(page);await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(10);expect(t.includes('Address')||t.includes('address')).toBeTruthy();});
  26 |   test('Address new route',async({page})=>{await login(page);await page.goto('/dashboard/addresses/new',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  27 |   test('Admin shipping',async({page})=>{await login(page);await page.goto('/admin/shipping',{waitUntil:'domcontentloaded'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  28 | });
  29 | 
```