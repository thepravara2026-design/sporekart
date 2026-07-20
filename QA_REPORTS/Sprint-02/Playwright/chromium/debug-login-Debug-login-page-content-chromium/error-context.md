# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: debug-login.spec.ts >> Debug login page content
- Location: tests\debug-login.spec.ts:3:5

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
  3  | test('Debug login page content', async ({ page }) => {
> 4  |   await page.goto('/login');
     |              ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  5  |   await page.waitForLoadState('networkidle');
  6  |   await page.waitForTimeout(3000);
  7  |   
  8  |   const html = await page.content();
  9  |   console.log('=== PAGE TITLE ===', await page.title());
  10 |   console.log('=== URL ===', page.url());
  11 |   
  12 |   // Check for key elements
  13 |   const bodyText = await page.locator('body').innerText();
  14 |   console.log('=== BODY TEXT ===');
  15 |   console.log(bodyText.substring(0, 2000));
  16 |   
  17 |   // Check breadcrumbs / loading / error states
  18 |   const hasLoading = await page.locator('.loading, .spinner, [role="status"]').count();
  19 |   console.log('=== LOADING INDICATORS ===', hasLoading);
  20 |   
  21 |   // Take screenshot
  22 |   await page.screenshot({ path: 'debug-login.png', fullPage: true });
  23 | });
  24 | 
```