# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 14 — Visual Review >> No horizontal scroll on delivery queue page
- Location: tests\notification-platform.spec.ts:655:7

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
  3   | const BASE = 'http://localhost:5174';
  4   | 
  5   | async function login(page) {
> 6   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
      |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  7   |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  8   |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  9   |   await inp.fill('9876543210');
  10  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  11  |   await btn.click(); await page.waitForTimeout(2000);
  12  |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  13  |   const n = await otp.count();
  14  |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  15  | }
  16  | 
  17  | async function bodyText(page) { return (await page.locator('body').innerText()); }
  18  | async function hasText(page, t) { return (await bodyText(page)).includes(t); }
  19  | 
  20  | // ====================================================================
  21  | // PHASE 1 — EVENT TRIGGERS
  22  | // ====================================================================
  23  | test.describe('Phase 1 — Event Triggers', () => {
  24  |   test('IMPLEMENTATION GAP: No notification on registration', async ({ page }) => {
  25  |     const r = await page.goto('/register', { waitUntil: 'networkidle' });
  26  |     expect(r?.status()).toBeLessThan(400);
  27  |     const t = await bodyText(page);
  28  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  29  |   });
  30  | 
  31  |   test('IMPLEMENTATION GAP: No notification on login', async ({ page }) => {
  32  |     await page.goto('/login', { waitUntil: 'networkidle' });
  33  |     const t = await bodyText(page);
  34  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  35  |   });
  36  | 
  37  |   test('IMPLEMENTATION GAP: No OTP notification', async ({ page }) => {
  38  |     await page.goto('/login', { waitUntil: 'networkidle' });
  39  |     const t = await bodyText(page);
  40  |     expect(t.includes('OTP') || t.includes('otp')).toBeTruthy();
  41  |   });
  42  | 
  43  |   test('IMPLEMENTATION GAP: No password reset notification', async ({ page }) => {
  44  |     const r = await page.goto('/forgot-password', { waitUntil: 'networkidle' });
  45  |     expect(r?.status()).toBeLessThan(400);
  46  |     const t = await bodyText(page);
  47  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  48  |   });
  49  | 
  50  |   test('IMPLEMENTATION GAP: No order created notification', async ({ page }) => {
  51  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  52  |     const t = await bodyText(page);
  53  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  54  |   });
  55  | 
  56  |   test('IMPLEMENTATION GAP: No order confirmed notification', async ({ page }) => {
  57  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  58  |     const t = await bodyText(page);
  59  |     expect(t.includes('confirmed') && t.includes('notification')).toBeFalsy();
  60  |   });
  61  | 
  62  |   test('IMPLEMENTATION GAP: No order cancelled notification', async ({ page }) => {
  63  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  64  |     const t = await bodyText(page);
  65  |     expect(t.includes('cancel') && t.includes('notification')).toBeFalsy();
  66  |   });
  67  | 
  68  |   test('IMPLEMENTATION GAP: No training registration notification', async ({ page }) => {
  69  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  70  |     const t = await bodyText(page);
  71  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  72  |   });
  73  | 
  74  |   test('IMPLEMENTATION GAP: No training approval notification', async ({ page }) => {
  75  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  76  |     const t = await bodyText(page);
  77  |     expect(t.includes('approval') && t.includes('notification')).toBeFalsy();
  78  |   });
  79  | 
  80  |   test('IMPLEMENTATION GAP: No admin action notification', async ({ page }) => {
  81  |     await page.goto('/admin', { waitUntil: 'networkidle' });
  82  |     const t = await bodyText(page);
  83  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  84  |   });
  85  | 
  86  |   test('IMPLEMENTATION GAP: No system alert notification', async ({ page }) => {
  87  |     await page.goto('/admin', { waitUntil: 'networkidle' });
  88  |     const t = await bodyText(page);
  89  |     expect(t.includes('alert') || t.includes('Alert')).toBeFalsy();
  90  |   });
  91  | 
  92  |   test('IMPLEMENTATION GAP: No shipping update notification', async ({ page }) => {
  93  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  94  |     const t = await bodyText(page);
  95  |     expect(t.includes('shipping') && t.includes('notification')).toBeFalsy();
  96  |   });
  97  | 
  98  |   test('IMPLEMENTATION GAP: No payment status notification', async ({ page }) => {
  99  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  100 |     const t = await bodyText(page);
  101 |     expect(t.includes('payment') && t.includes('notification')).toBeFalsy();
  102 |   });
  103 | });
  104 | 
  105 | // ====================================================================
  106 | // PHASE 2 — EMAIL
```