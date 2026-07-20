# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: payment-validation.spec.ts >> Phase 7 — Order Synchronization >> Transaction ID displayed on order detail
- Location: tests\payment-validation.spec.ts:317:7

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
  4   | 
  5   | async function login(page) {
> 6   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
      |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  7   |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  8   |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  9   |   await inp.fill(PHONE);
  10  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  11  |   await btn.click(); await page.waitForTimeout(2000);
  12  |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  13  |   const n = await otp.count();
  14  |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  15  | }
  16  | 
  17  | // ============================================================================
  18  | // PHASE 0 — MOCK ENVIRONMENT VALIDATION
  19  | // ============================================================================
  20  | test.describe('Phase 0 — Mock Environment Validation', () => {
  21  |   test('MOCK_MODE is enabled on dev server', async ({ page }) => {
  22  |     const resp = await page.goto('/');
  23  |     expect(resp?.status()).toBeLessThan(400);
  24  |   });
  25  | 
  26  |   test('Mock Razorpay key does not contain production values', async ({ page }) => {
  27  |     await page.goto('/');
  28  |     const html = await page.locator('html').getAttribute('data-env');
  29  |     // The env mock key should not contain live Razorpay patterns
  30  |     const scripts = await page.locator('script').allInnerTexts();
  31  |     for (const s of scripts) {
  32  |       if (s.includes('rzp_live')) {
  33  |         throw new Error('Production Razorpay key detected in client');
  34  |       }
  35  |     }
  36  |   });
  37  | });
  38  | 
  39  | // ============================================================================
  40  | // PHASE 1 — PAYMENT INITIALIZATION (IMPLEMENTATION GAP)
  41  | // ============================================================================
  42  | test.describe('Phase 1 — Payment Initialization', () => {
  43  |   test('IMPLEMENTATION GAP: Checkout page not implemented', async ({ page }) => {
  44  |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  45  |     const t = await page.locator('body').innerText();
  46  |     expect(t.includes('Checkout') || t.includes('Navigation Prototype')).toBeTruthy();
  47  |   });
  48  | 
  49  |   test('IMPLEMENTATION GAP: Cart page not implemented', async ({ page }) => {
  50  |     await page.goto('/cart', { waitUntil: 'networkidle' });
  51  |     const t = await page.locator('body').innerText();
  52  |     expect(t.includes('Cart') || t.includes('Navigation Prototype')).toBeTruthy();
  53  |   });
  54  | 
  55  |   test('IMPLEMENTATION GAP: No payment initialization flow exists', async ({ page }) => {
  56  |     const resp = await page.request.post('/api/payments/create-order', { data: {} });
  57  |     const body = await resp.text();
  58  |     const isImplementingPayment = body.includes('order_id') || body.includes('razorpay_order') || body.includes('payment_link');
  59  |     expect(isImplementingPayment).toBe(false);
  60  |   });
  61  | 
  62  |   test('IMPLEMENTATION GAP: No gateway initialization endpoint', async ({ page }) => {
  63  |     const resp = await page.request.get('/api/payments/gateway');
  64  |     const body = await resp.text();
  65  |     const isImplementingPayment = body.includes('gateway_url') || body.includes('razorpay_checkout') || body.includes('payment_session');
  66  |     expect(isImplementingPayment).toBe(false);
  67  |   });
  68  | 
  69  |   test('IMPLEMENTATION GAP: No transaction creation UI', async ({ page }) => {
  70  |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  71  |     const ctas = page.locator('button:has-text("Pay"),button:has-text("Place order")');
  72  |     const exists = await ctas.isVisible({ timeout: 2000 }).catch(() => false);
  73  |     // Button may exist in prototype but is non-functional
  74  |     if (exists) {
  75  |       const text = await ctas.innerText();
  76  |       expect(text.length).toBeGreaterThan(0);
  77  |     }
  78  |   });
  79  | });
  80  | 
  81  | // ============================================================================
  82  | // PHASE 2 — PAYMENT METHOD VALIDATION (IMPLEMENTATION GAP + DISPLAY)
  83  | // ============================================================================
  84  | test.describe('Phase 2 — Payment Method Validation', () => {
  85  |   test.beforeEach(async ({ page }) => { await login(page); });
  86  | 
  87  |   test('Payment information displays on order details', async ({ page }) => {
  88  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  89  |     const t = await page.locator('body').innerText();
  90  |     expect(t.includes('UPI') || t.includes('Razorpay') || t.includes('Payment')).toBeTruthy();
  91  |   });
  92  | 
  93  |   test('Multiple payment methods displayed in mock data', async ({ page }) => {
  94  |     const methods = [];
  95  |     const orders = ['ORD-2026-8842', 'ORD-2026-7715', 'ORD-2026-5541', 'ORD-2026-9922'];
  96  |     for (const oid of orders) {
  97  |       await page.goto(`/dashboard/orders/${oid}`, { waitUntil: 'networkidle' });
  98  |       const t = await page.locator('body').innerText();
  99  |       if (t.includes('UPI')) methods.push('UPI');
  100 |       else if (t.includes('Credit Card') || t.includes('Visa')) methods.push('Credit Card');
  101 |       else if (t.includes('Netbanking')) methods.push('Netbanking');
  102 |     }
  103 |     // At least 2 different payment methods should be visible across orders
  104 |     const unique = [...new Set(methods)];
  105 |     expect(unique.length).toBeGreaterThanOrEqual(2);
  106 |   });
```