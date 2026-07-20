# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 3 — Order History >> Browser restart preserves order session
- Location: tests\order-lifecycle.spec.ts:225:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
Call log:
  - navigating to "http://localhost:5173/login", waiting until "load"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const PHONE = '9876543210';
  4   | const TEST_ORDERS = ['ORD-2026-8842', 'ORD-2026-7715', 'ORD-2026-5541', 'ORD-2026-9922'];
  5   | const INVALID_ORDER = 'ORD-9999-9999';
  6   | 
  7   | async function login(page) {
> 8   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
      |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
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
  108 |     expect(t.includes('Timeline') || t.includes('milestone')).toBeTruthy();
```