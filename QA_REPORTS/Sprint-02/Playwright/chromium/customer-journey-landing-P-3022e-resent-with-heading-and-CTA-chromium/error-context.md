# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-landing.spec.ts >> Part 2 — Customer Journey: Phase 1 — Landing Page Validation >> Hero section is present with heading and CTA
- Location: tests\customer-journey-landing.spec.ts:14:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
Call log:
  - navigating to "http://localhost:5173/", waiting until "load"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('Part 2 — Customer Journey: Phase 1 — Landing Page Validation', () => {
  4   | 
  5   |   test('Homepage loads successfully', async ({ page }) => {
  6   |     const start = Date.now();
  7   |     await page.goto('/');
  8   |     await page.waitForLoadState('networkidle');
  9   |     const loadTime = Date.now() - start;
  10  |     expect(loadTime).toBeLessThan(10000);
  11  |     expect(await page.title()).toBeTruthy();
  12  |   });
  13  | 
  14  |   test('Hero section is present with heading and CTA', async ({ page }) => {
> 15  |     await page.goto('/');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
  16  |     const hero = page.locator('section').first();
  17  |     await expect(hero).toBeVisible({ timeout: 5000 });
  18  |     const heading = hero.locator('h1, h2').first();
  19  |     await expect(heading).toBeVisible();
  20  |     const cta = hero.locator('a, button').first();
  21  |     await expect(cta).toBeVisible();
  22  |   });
  23  | 
  24  |   test('Navigation bar is present and contains main links', async ({ page }) => {
  25  |     await page.goto('/');
  26  |     const nav = page.locator('nav, header');
  27  |     await expect(nav.first()).toBeVisible({ timeout: 5000 });
  28  |     const links = ['Products', 'Training', 'Blog', 'About', 'Contact'];
  29  |     for (const label of links) {
  30  |       const link = nav.first().locator(`text=${label}`).first();
  31  |       if (await link.isVisible().catch(() => false)) {
  32  |         await expect(link).toBeVisible();
  33  |       }
  34  |     }
  35  |   });
  36  | 
  37  |   test('Footer is present with links', async ({ page }) => {
  38  |     await page.goto('/');
  39  |     const footer = page.locator('footer');
  40  |     await expect(footer).toBeVisible({ timeout: 5000 });
  41  |     const footerLinks = footer.locator('a');
  42  |     const count = await footerLinks.count();
  43  |     expect(count).toBeGreaterThan(5);
  44  |   });
  45  | 
  46  |   test('Featured products section renders', async ({ page }) => {
  47  |     await page.goto('/');
  48  |     const featured = page.locator('text=Featured Products, Products, Best Sellers').first();
  49  |     if (await featured.isVisible().catch(() => false)) {
  50  |       await expect(featured).toBeVisible();
  51  |     }
  52  |     const productCards = page.locator('[class*="product"], [class*="card"], [class*="ProductCard"]');
  53  |     const count = await productCards.count();
  54  |     expect(count).toBeGreaterThanOrEqual(0);
  55  |   });
  56  | 
  57  |   test('No broken images on homepage', async ({ page }) => {
  58  |     await page.goto('/');
  59  |     const images = page.locator('img');
  60  |     const count = await images.count();
  61  |     let broken = 0;
  62  |     for (let i = 0; i < count; i++) {
  63  |       const img = images.nth(i);
  64  |       const src = await img.getAttribute('src');
  65  |       if (!src || src === '' || src.startsWith('data:')) continue;
  66  |       const naturalWidth = await img.evaluate(el => (el as HTMLImageElement).naturalWidth);
  67  |       if (naturalWidth === 0) broken++;
  68  |     }
  69  |     expect(broken).toBe(0);
  70  |   });
  71  | 
  72  |   test('No broken links on homepage', async ({ page }) => {
  73  |     await page.goto('/');
  74  |     await page.waitForLoadState('networkidle');
  75  |     const links = page.locator('a[href]');
  76  |     const count = await links.count();
  77  |     let brokenCount = 0;
  78  |     for (let i = 0; i < Math.min(count, 30); i++) {
  79  |       const href = await links.nth(i).getAttribute('href');
  80  |       if (!href || href.startsWith('#') || href.startsWith('mailto:') || href.startsWith('tel:') || href.startsWith('http')) continue;
  81  |       try {
  82  |         const response = await page.goto(href);
  83  |         if (response && response.status() >= 400) brokenCount++;
  84  |         await page.goBack();
  85  |       } catch { brokenCount++; }
  86  |     }
  87  |     expect(brokenCount).toBe(0);
  88  |   });
  89  | 
  90  |   test('Page scrolls smoothly and content remains', async ({ page }) => {
  91  |     await page.goto('/');
  92  |     await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
  93  |     await page.waitForTimeout(500);
  94  |     const scrollY = await page.evaluate(() => window.scrollY);
  95  |     expect(scrollY).toBeGreaterThan(0);
  96  |     await page.evaluate(() => window.scrollTo(0, 0));
  97  |     await page.waitForTimeout(500);
  98  |     const scrollY2 = await page.evaluate(() => window.scrollY);
  99  |     expect(scrollY2).toBe(0);
  100 |   });
  101 | 
  102 |   test('Responsive layout renders at desktop viewport', async ({ page }) => {
  103 |     await page.setViewportSize({ width: 1440, height: 900 });
  104 |     await page.goto('/');
  105 |     await page.waitForLoadState('networkidle');
  106 |     const vp = page.viewportSize();
  107 |     expect(vp?.width).toBe(1440);
  108 |     const content = page.locator('section, main').first();
  109 |     await expect(content).toBeVisible({ timeout: 5000 });
  110 |   });
  111 | 
  112 |   test('Responsive layout renders at tablet viewport', async ({ page }) => {
  113 |     await page.setViewportSize({ width: 768, height: 1024 });
  114 |     await page.goto('/');
  115 |     await page.waitForLoadState('networkidle');
```