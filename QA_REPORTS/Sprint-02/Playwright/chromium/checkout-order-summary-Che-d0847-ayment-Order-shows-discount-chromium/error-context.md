# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-order-summary.spec.ts >> Checkout Order Summary, Coupons, Payment >> Order shows discount
- Location: tests\checkout-order-summary.spec.ts:18:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
Call log:
  - navigating to "http://localhost:5173/login", waiting until "load"

```

# Test source

```ts
  1  | ﻿import { test, expect } from '@playwright/test';
  2  | const PHONE = '9876543210';
  3  | async function login(page) {
> 4  |   await page.goto('/login'); await page.waitForLoadState('networkidle');
     |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  5  |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  6  |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  7  |   await inp.fill(PHONE);
  8  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  9  |   await btn.click(); await page.waitForTimeout(2000);
  10 |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  11 |   const n = await otp.count();
  12 |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  13 | }
  14 | test.describe('Checkout Order Summary, Coupons, Payment', () => {
  15 |   test('Order shows pricing',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('INR')||t.includes('Rs')||t.includes('Total')||t.includes('total')).toBeTruthy();});
  16 |   test('Order shows subtotal tax',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Subtotal')||t.includes('subtotal')||t.includes('Tax')||t.includes('tax')).toBeTruthy();});
  17 |   test('Order shows items qty',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Qty')||t.includes('qty')||t.includes('x ')||t.includes('×')).toBeTruthy();});
  18 |   test('Order shows discount',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Discount')||t.includes('discount')||t.includes('Coupon')||t.includes('coupon')).toBeTruthy();});
  19 |   test('Order shows payment info',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Payment')||t.includes('payment')||t.includes('Razorpay')||t.includes('UPI')).toBeTruthy();});
  20 |   test('Order shows shipping address',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Shipping')||t.includes('shipping')||t.includes('Address')||t.includes('address')).toBeTruthy();});
  21 |   test('Data persists on refresh',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const b=await page.locator('body').innerText();await page.reload({waitUntil:'networkidle'});const a=await page.locator('body').innerText();expect(a.length).toBeGreaterThan(50);});
  22 |   test('Different order loads',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-5541',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  23 | });
  24 | 
```