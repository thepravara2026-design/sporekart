# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: mobile-responsive.spec.ts >> Part 7 — Mobile & Responsive Validation >> Desktop layout (1920x1080) details
- Location: tests\mobile-responsive.spec.ts:5:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
Call log:
  - navigating to "http://localhost:5173/login", waiting until "load"

```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | 
  3  | test.describe('Part 7 — Mobile & Responsive Validation', () => {
  4  | 
  5  |   test('Desktop layout (1920x1080) details', async ({ page }) => {
  6  |     await page.setViewportSize({ width: 1920, height: 1080 });
> 7  |     await page.goto('/login');
     |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  8  |     await page.waitForLoadState('networkidle');
  9  | 
  10 |     // Desktop: brand column and auth card both visible side-by-side
  11 |     await expect(page.locator('.auth-layout__brand')).toBeVisible();
  12 |     await expect(page.locator('.auth-card')).toBeVisible();
  13 | 
  14 |     // Verify grid layout properties
  15 |     const brandBox = await page.locator('.auth-layout__brand').boundingBox();
  16 |     const cardBox = await page.locator('.auth-card').boundingBox();
  17 |     
  18 |     expect(brandBox).toBeTruthy();
  19 |     expect(cardBox).toBeTruthy();
  20 |     // They should sit side-by-side (brand is left of card)
  21 |     expect(brandBox!.x).toBeLessThan(cardBox!.x);
  22 | 
  23 |     await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-desktop.png' });
  24 |   });
  25 | 
  26 |   test('Tablet layout (768x1024) details', async ({ page }) => {
  27 |     await page.setViewportSize({ width: 768, height: 1024 });
  28 |     await page.goto('/login');
  29 |     await page.waitForLoadState('networkidle');
  30 | 
  31 |     // Tablet: brand and auth card visible, check layout collapse rules
  32 |     await expect(page.locator('.auth-card')).toBeVisible();
  33 |     await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-tablet.png' });
  34 |   });
  35 | 
  36 |   test('Mobile layout (375x812) details', async ({ page }) => {
  37 |     await page.setViewportSize({ width: 375, height: 812 });
  38 |     await page.goto('/login');
  39 |     await page.waitForLoadState('networkidle');
  40 | 
  41 |     // Mobile: brand column is hidden (display: none / collapsed), only auth card is visible
  42 |     await expect(page.locator('.auth-layout__brand')).not.toBeVisible();
  43 |     await expect(page.locator('.auth-card')).toBeVisible();
  44 | 
  45 |     // Verify header mobile layout elements
  46 |     await page.goto('/');
  47 |     await page.waitForLoadState('networkidle');
  48 |     // Check that the shell has the mobile menu toggle button visible
  49 |     await expect(page.locator('button[aria-label="Open sidebar"]')).toBeVisible();
  50 | 
  51 |     await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-mobile.png' });
  52 |   });
  53 | });
  54 | 
```