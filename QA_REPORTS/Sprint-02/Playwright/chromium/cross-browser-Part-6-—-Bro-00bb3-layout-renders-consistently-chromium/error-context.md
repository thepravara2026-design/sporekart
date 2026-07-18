# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: cross-browser.spec.ts >> Part 6 — Browser Compatibility >> Login layout renders consistently
- Location: tests\cross-browser.spec.ts:5:7

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
  3  | test.describe('Part 6 — Browser Compatibility', () => {
  4  | 
  5  |   test('Login layout renders consistently', async ({ page }, testInfo) => {
  6  |     // 1. Navigate to login
> 7  |     await page.goto('/login');
     |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  8  |     await page.waitForLoadState('networkidle');
  9  | 
  10 |     // 2. Verify key layout components are rendered correctly
  11 |     await expect(page.locator('.auth-card')).toBeVisible();
  12 |     await expect(page.locator('.auth-card__title')).toHaveText('Access your workspace');
  13 |     
  14 |     // 3. Verify brand section exists (hidden on mobile screen sizes, but visible on desktop)
  15 |     const viewportWidth = page.viewportSize()?.width ?? 1280;
  16 |     if (viewportWidth >= 768) {
  17 |       await expect(page.locator('.auth-layout__brand')).toBeVisible();
  18 |     }
  19 | 
  20 |     // 4. Capture screenshot for layout validation
  21 |     const browserName = testInfo.project.name;
  22 |     await page.screenshot({
  23 |       path: `../QA_REPORTS/Sprint-02/Evidence/Screenshots/login-layout-${browserName}.png`,
  24 |       fullPage: true,
  25 |     });
  26 |   });
  27 | });
  28 | 
```