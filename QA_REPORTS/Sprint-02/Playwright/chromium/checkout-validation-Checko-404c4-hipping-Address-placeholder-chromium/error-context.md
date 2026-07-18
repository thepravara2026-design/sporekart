# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-validation.spec.ts >> Checkout Entry,Address,Shipping >> Address placeholder
- Location: tests\checkout-validation.spec.ts:25:7

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
  14 | test.describe('Checkout Entry,Address,Shipping', () => {
  15 |   test('Cart 404',async({page})=>{await page.goto('/cart',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  16 |   test('Checkout 404',async({page})=>{await page.goto('/checkout',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  17 |   test('Unauth redirect',async({page})=>{await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
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