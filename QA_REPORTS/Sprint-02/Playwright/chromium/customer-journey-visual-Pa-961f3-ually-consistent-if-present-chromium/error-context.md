# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-visual.spec.ts >> Part 2 — Customer Journey: Phase 12 — Visual Review >> Loading skeletons are visually consistent if present
- Location: tests\customer-journey-visual.spec.ts:43:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
Call log:
  - navigating to "http://localhost:5173/products", waiting until "domcontentloaded"

```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | 
  3  | test.describe('Part 2 — Customer Journey: Phase 12 — Visual Review', () => {
  4  | 
  5  |   test('Homepage layout is visually correct at desktop', async ({ page }) => {
  6  |     await page.setViewportSize({ width: 1440, height: 900 });
  7  |     await page.goto('/');
  8  |     await page.waitForLoadState('networkidle');
  9  |     const sections = page.locator('section');
  10 |     const count = await sections.count();
  11 |     expect(count).toBeGreaterThanOrEqual(3);
  12 |     await expect(page.locator('body')).toHaveScreenshot('homepage-desktop.png', { maxDiffPixels: 500 });
  13 |   });
  14 | 
  15 |   test('Homepage layout is visually correct at mobile', async ({ page }) => {
  16 |     await page.setViewportSize({ width: 375, height: 667 });
  17 |     await page.goto('/');
  18 |     await page.waitForLoadState('networkidle');
  19 |     await expect(page.locator('body')).toHaveScreenshot('homepage-mobile.png', { maxDiffPixels: 500 });
  20 |   });
  21 | 
  22 |   test('Products page layout is visually correct', async ({ page }) => {
  23 |     await page.setViewportSize({ width: 1440, height: 900 });
  24 |     await page.goto('/products');
  25 |     await page.waitForLoadState('networkidle');
  26 |     await expect(page.locator('body')).toHaveScreenshot('products-page-desktop.png', { maxDiffPixels: 500 });
  27 |   });
  28 | 
  29 |   test('Product card spacing and alignment look correct', async ({ page }) => {
  30 |     await page.goto('/');
  31 |     await page.waitForLoadState('networkidle');
  32 |     const productCards = page.locator('[class*="card"], [class*="ProductCard"], [class*="product"]');
  33 |     const count = await productCards.count();
  34 |     if (count > 1) {
  35 |       const firstBox = await productCards.nth(0).boundingBox();
  36 |       const secondBox = await productCards.nth(1).boundingBox();
  37 |       if (firstBox && secondBox) {
  38 |         expect(Math.abs(firstBox.y - secondBox.y)).toBeLessThan(firstBox.height + 10);
  39 |       }
  40 |     }
  41 |   });
  42 | 
  43 |   test('Loading skeletons are visually consistent if present', async ({ page }) => {
> 44 |     await page.goto('/products', { waitUntil: 'domcontentloaded' });
     |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
  45 |     const skeletons = page.locator('[class*="skeleton"], [class*="Skeleton"], [class*="placeholder"]');
  46 |     const count = await skeletons.count();
  47 |     if (count > 0) {
  48 |       const firstSkeleton = skeletons.first();
  49 |       const box = await firstSkeleton.boundingBox();
  50 |       expect(box).toBeTruthy();
  51 |       expect(box!.width).toBeGreaterThan(0);
  52 |       expect(box!.height).toBeGreaterThan(0);
  53 |     }
  54 |   });
  55 | 
  56 |   test('Typography is consistent across pages', async ({ page }) => {
  57 |     const pages = ['/', '/products', '/about', '/contact'];
  58 |     for (const p of pages) {
  59 |       await page.goto(p);
  60 |       await page.waitForLoadState('networkidle');
  61 |       const h1 = page.locator('h1').first();
  62 |       if (await h1.isVisible().catch(() => false)) {
  63 |         const fontSize = await h1.evaluate(el => getComputedStyle(el).fontSize);
  64 |         expect(parseFloat(fontSize)).toBeGreaterThan(16);
  65 |       }
  66 |     }
  67 |   });
  68 | 
  69 |   test('Animations and transitions do not cause layout shift', async ({ page }) => {
  70 |     await page.goto('/');
  71 |     await page.waitForLoadState('networkidle');
  72 |     const bodyBox = await page.locator('body').boundingBox();
  73 |     const initialHeight = bodyBox!.height;
  74 |     await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
  75 |     await page.waitForTimeout(1000);
  76 |     await page.evaluate(() => window.scrollTo(0, 0));
  77 |     await page.waitForTimeout(500);
  78 |     const finalBox = await page.locator('body').boundingBox();
  79 |     expect(Math.abs(finalBox!.height - initialHeight)).toBeLessThan(50);
  80 |   });
  81 | 
  82 |   test('Error states should be visually clear and meaningful', async ({ page }) => {
  83 |     const response = await page.goto('/this-route-does-not-exist', { waitUntil: 'networkidle' });
  84 |     const errorEl = page.locator('[class*="error"], [class*="Error"], [class*="404"]').first();
  85 |     const present = await errorEl.isVisible().catch(() => false);
  86 |     if (present) {
  87 |       await expect(errorEl).toBeVisible();
  88 |     }
  89 |     const bodyText = await page.locator('body').innerText();
  90 |     expect(bodyText.length).toBeGreaterThan(5);
  91 |   });
  92 | 
  93 | });
  94 | 
```