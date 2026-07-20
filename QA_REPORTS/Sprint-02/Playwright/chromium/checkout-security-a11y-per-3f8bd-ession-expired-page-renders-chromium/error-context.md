# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-security-a11y-perf.spec.ts >> Checkout Security, A11y, Performance >> Session-expired page renders
- Location: tests\checkout-security-a11y-perf.spec.ts:17:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/session-expired
Call log:
  - navigating to "http://localhost:5173/session-expired", waiting until "networkidle"

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
  14 | test.describe('Checkout Security, A11y, Performance', () => {
  15 |   test('Unauth /admin/orders redirects',async({page})=>{await page.goto('/admin/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  16 |   test('Unauth /dashboard/addresses redirects',async({page})=>{await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
> 17 |   test('Session-expired page renders',async({page})=>{await page.goto('/session-expired',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
     |                                                                  ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/session-expired
  18 |   test('Access-denied page renders',async({page})=>{await page.goto('/access-denied',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  19 |   test('Skip to content link',async({page})=>{await page.goto('/');const l=page.locator('a[href="#main-content"],a[href="#content"],a:has-text("Skip"),[class*="skip"]');await expect(l.first()).toBeVisible({timeout:5000});});
  20 |   test('ARIA landmarks',async({page})=>{await page.goto('/');await expect(page.locator('main,[role="main"]').first()).toBeVisible({timeout:5000});await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({timeout:5000});await expect(page.locator('footer,[role="contentinfo"]').first()).toBeVisible({timeout:5000});});
  21 |   test('Images have alt text',async({page})=>{await page.goto('/');const imgs=page.locator('img');const c=await imgs.count();let missing=0;for(let i=0;i<c;i++){const alt=await imgs.nth(i).getAttribute('alt');if(alt===null||alt===undefined)missing++;}expect(missing).toBe(0);});
  22 |   test('Performance: orders load <10s',async({page})=>{const s=Date.now();await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(Date.now()-s).toBeLessThan(15000);});
  23 |   test('No console errors on orders',async({page})=>{const errs=[];page.on('console',m=>{if(m.type()==='error')errs.push(m.text())});await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(errs.length).toBe(0);});
  24 |   test('No failed network requests',async({page})=>{const fails=[];page.on('requestfailed',r=>fails.push(r.url()));await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(fails.length).toBe(0);});
  25 | });
  26 | 
```