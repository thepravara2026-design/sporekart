# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: accessibility.spec.ts >> Part 8 — Accessibility Validation >> RegisterPage accessibility scan (WCAG 2.1 AA)
- Location: tests\accessibility.spec.ts:36:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/register
Call log:
  - navigating to "http://localhost:5173/register", waiting until "load"

```

# Test source

```ts
  1  | import { test, expect } from '@playwright/test';
  2  | import AxeBuilder from '@axe-core/playwright';
  3  | 
  4  | test.describe('Part 8 — Accessibility Validation', () => {
  5  | 
  6  |   test('Skip to content link is present and functional', async ({ page }) => {
  7  |     await page.goto('/');
  8  |     await page.waitForLoadState('networkidle');
  9  | 
  10 |     // 1. Verify skip link exists and has correct href
  11 |     const skipLink = page.locator('text=Skip to content');
  12 |     await expect(skipLink).toBeVisible();
  13 |     await expect(skipLink).toHaveAttribute('href', '#main');
  14 | 
  15 |     // 2. Tab focus on skip link and check focus ring visibility
  16 |     await page.keyboard.press('Tab');
  17 |     await expect(skipLink).toBeFocused();
  18 |   });
  19 | 
  20 |   test('LoginPage accessibility scan (WCAG 2.1 AA)', async ({ page }) => {
  21 |     await page.goto('/login');
  22 |     await page.waitForLoadState('networkidle');
  23 | 
  24 |     // Run axe-core accessibility audit
  25 |     const results = await new AxeBuilder({ page })
  26 |       .withTags(['wcag2a', 'wcag2aa'])
  27 |       .analyze();
  28 | 
  29 |     // Verify there are no critical accessibility violations
  30 |     const criticalViolations = results.violations.filter(
  31 |       (v) => v.impact === 'critical' || v.impact === 'serious'
  32 |     );
  33 |     expect(criticalViolations.length).toBe(0);
  34 |   });
  35 | 
  36 |   test('RegisterPage accessibility scan (WCAG 2.1 AA)', async ({ page }) => {
> 37 |     await page.goto('/register');
     |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/register
  38 |     await page.waitForLoadState('networkidle');
  39 | 
  40 |     const results = await new AxeBuilder({ page })
  41 |       .withTags(['wcag2a', 'wcag2aa'])
  42 |       .analyze();
  43 | 
  44 |     const criticalViolations = results.violations.filter(
  45 |       (v) => v.impact === 'critical' || v.impact === 'serious'
  46 |     );
  47 |     expect(criticalViolations.length).toBe(0);
  48 |   });
  49 | 
  50 |   test('Semantic HTML and keyboard navigation on login inputs', async ({ page }) => {
  51 |     await page.goto('/login');
  52 |     await page.waitForLoadState('networkidle');
  53 | 
  54 |     // Check that we have a single H1 header tag for proper semantic outline
  55 |     const h1 = page.locator('h1');
  56 |     await expect(h1).toHaveCount(1);
  57 | 
  58 |     // Verify label inputs are linked correctly via htmlFor or nested labels
  59 |     const identifierInput = page.locator('input[type="tel"]');
  60 |     await expect(identifierInput).toHaveAttribute('required');
  61 | 
  62 |     // Tab through fields sequentially
  63 |     await page.click('input[type="tel"]');
  64 |     await page.keyboard.press('Tab'); // Tabbing out of input
  65 |     const rememberCheckbox = page.locator('input[type="checkbox"]').first();
  66 |     await expect(rememberCheckbox).toBeFocused();
  67 |   });
  68 | });
  69 | 
```