# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-cross-browser.spec.ts >> Part 2 — Customer Journey: Phase 10 — Cross-Browser Validation >> Products page renders consistently across viewports
- Location: tests\customer-journey-cross-browser.spec.ts:36:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
Call log:
  - navigating to "http://localhost:5173/products", waiting until "load"

```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | 
  3  | test.describe('Part 2 — Customer Journey: Phase 10 — Cross-Browser Validation', () => {
  4  | 
  5  |   test('Homepage loads on all viewport sizes', async ({ page }) => {
  6  |     const viewports = [
  7  |       { width: 1440, height: 900, label: 'desktop' },
  8  |       { width: 1024, height: 768, label: 'laptop' },
  9  |       { width: 768, height: 1024, label: 'tablet' },
  10 |       { width: 375, height: 667, label: 'mobile' },
  11 |     ];
  12 |     for (const vp of viewports) {
  13 |       await page.setViewportSize({ width: vp.width, height: vp.height });
  14 |       const response = await page.goto('/');
  15 |       expect(response?.status()).toBeLessThan(400);
  16 |       await page.waitForLoadState('networkidle');
  17 |       const bodyText = await page.locator('body').innerText();
  18 |       expect(bodyText.length).toBeGreaterThan(20);
  19 |     }
  20 |   });
  21 | 
  22 |   test('Navigation bar adapts to all viewport sizes', async ({ page }) => {
  23 |     await page.setViewportSize({ width: 1440, height: 900 });
  24 |     await page.goto('/');
  25 |     await page.waitForLoadState('networkidle');
  26 |     const navDesktop = page.locator('nav, header').first();
  27 |     await expect(navDesktop).toBeVisible();
  28 | 
  29 |     await page.setViewportSize({ width: 375, height: 667 });
  30 |     await page.goto('/');
  31 |     await page.waitForLoadState('networkidle');
  32 |     const navMobile = page.locator('nav, header, [class*="mobile-menu"], [class*="drawer"]').first();
  33 |     await expect(navMobile).toBeVisible();
  34 |   });
  35 | 
  36 |   test('Products page renders consistently across viewports', async ({ page }) => {
  37 |     await page.setViewportSize({ width: 1440, height: 900 });
> 38 |     let response = await page.goto('/products');
     |                               ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
  39 |     expect(response?.status()).toBeLessThan(400);
  40 | 
  41 |     await page.setViewportSize({ width: 768, height: 1024 });
  42 |     response = await page.goto('/products');
  43 |     expect(response?.status()).toBeLessThan(400);
  44 | 
  45 |     await page.setViewportSize({ width: 375, height: 667 });
  46 |     response = await page.goto('/products');
  47 |     expect(response?.status()).toBeLessThan(400);
  48 |   });
  49 | 
  50 |   test('Key pages render correctly across all viewports', async ({ page }) => {
  51 |     const routes = ['/', '/products', '/about', '/contact', '/training', '/blog'];
  52 |     const viewports = [
  53 |       { width: 1440, height: 900 },
  54 |       { width: 768, height: 1024 },
  55 |       { width: 375, height: 667 },
  56 |     ];
  57 |     for (const route of routes) {
  58 |       for (const vp of viewports) {
  59 |         await page.setViewportSize({ width: vp.width, height: vp.height });
  60 |         const response = await page.goto(route);
  61 |         expect(response?.status()).toBeLessThan(400);
  62 |         await page.waitForLoadState('networkidle');
  63 |       }
  64 |     }
  65 |   });
  66 | 
  67 |   test('Responsive images load correctly on all viewports', async ({ page }) => {
  68 |     const viewports = [375, 768, 1024, 1440];
  69 |     for (const width of viewports) {
  70 |       await page.setViewportSize({ width, height: 900 });
  71 |       await page.goto('/');
  72 |       await page.waitForLoadState('networkidle');
  73 |       const images = page.locator('img');
  74 |       const count = await images.count();
  75 |       let broken = 0;
  76 |       for (let i = 0; i < Math.min(count, 10); i++) {
  77 |         const src = await images.nth(i).getAttribute('src');
  78 |         if (src && !src.startsWith('data:')) {
  79 |           const nw = await images.nth(i).evaluate(el => (el as HTMLImageElement).naturalWidth);
  80 |           if (nw === 0) broken++;
  81 |         }
  82 |       }
  83 |       expect(broken).toBe(0);
  84 |     }
  85 |   });
  86 | 
  87 |   test('Horizontal scroll is not present on any page', async ({ page }) => {
  88 |     const routes = ['/', '/products', '/about', '/contact', '/blog', '/training', '/search'];
  89 |     for (const route of routes) {
  90 |       await page.goto(route);
  91 |       await page.waitForLoadState('networkidle');
  92 |       const hasHorizontalScroll = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
  93 |       expect(hasHorizontalScroll).toBe(false);
  94 |     }
  95 |   });
  96 | 
  97 | });
  98 | 
```