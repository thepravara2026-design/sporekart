# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-visual.spec.ts >> Checkout Visual Review >> Typography is consistent
- Location: tests\checkout-visual.spec.ts:6:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
Call log:
  - navigating to "http://localhost:5173/", waiting until "load"

```

# Test source

```ts
  1  | ﻿import { test, expect } from '@playwright/test';
  2  | test.describe('Checkout Visual Review', () => {
  3  |   test('Homepage renders at desktop',async({page})=>{await page.setViewportSize({width:1440,height:900});await page.goto('/');await page.waitForLoadState('networkidle');expect(await page.locator('section').count()).toBeGreaterThanOrEqual(3);});
  4  |   test('Homepage renders at mobile',async({page})=>{await page.setViewportSize({width:375,height:667});await page.goto('/');await page.waitForLoadState('networkidle');expect(await page.locator('body').innerText()).toBeTruthy();});
  5  |   test('404 page has content',async({page})=>{await page.goto('/this-does-not-exist',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(5);});
> 6  |   test('Typography is consistent',async({page})=>{await page.goto('/');const h1=page.locator('h1').first();if(await h1.isVisible().catch(()=>false)){const fs=await h1.evaluate(el=>getComputedStyle(el).fontSize);expect(parseFloat(fs)).toBeGreaterThan(16);}});
     |                                                              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
  7  |   test('No horizontal scroll',async({page})=>{await page.goto('/');const hs=await page.evaluate(()=>document.documentElement.scrollWidth>document.documentElement.clientWidth);expect(hs).toBe(false);});
  8  |   test('Cross-browser: key pages render at desktop',async({page})=>{const routes=['/','/products','/about','/contact','/login'];for(const r of routes){await page.setViewportSize({width:1440,height:900});const res=await page.goto(r);expect(res?.status()).toBeLessThan(400);await page.waitForLoadState('networkidle');}});
  9  |   test('Cross-browser: key pages render at mobile',async({page})=>{const routes=['/','/login','/products'];for(const r of routes){await page.setViewportSize({width:375,height:667});const res=await page.goto(r);expect(res?.status()).toBeLessThan(400);await page.waitForLoadState('networkidle');}});
  10 | });
  11 | 
```