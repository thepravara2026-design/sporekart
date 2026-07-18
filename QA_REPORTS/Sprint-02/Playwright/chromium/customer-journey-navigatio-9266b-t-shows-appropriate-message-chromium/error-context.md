# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-navigation.spec.ts >> Part 2 — Customer Journey: Phase 2 — Navigation Validation >> Unimplemented route Cart (not implemented) (/cart) shows appropriate message
- Location: tests\customer-journey-navigation.spec.ts:42:9

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/cart
Call log:
  - navigating to "http://localhost:5173/cart", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const MAIN_ROUTES = [
  4   |   { path: '/', label: 'Home' },
  5   |   { path: '/products', label: 'Products' },
  6   |   { path: '/training', label: 'Training' },
  7   |   { path: '/blog', label: 'Blog' },
  8   |   { path: '/about', label: 'About' },
  9   |   { path: '/contact', label: 'Contact' },
  10  |   { path: '/login', label: 'Login' },
  11  |   { path: '/register', label: 'Register' },
  12  |   { path: '/faq', label: 'FAQ' },
  13  |   { path: '/support', label: 'Support' },
  14  |   { path: '/certifications', label: 'Certifications' },
  15  | ];
  16  | 
  17  | const PLACEHOLDER_ROUTES = [
  18  |   { path: '/cart', label: 'Cart (not implemented)' },
  19  |   { path: '/checkout', label: 'Checkout (not implemented)' },
  20  | ];
  21  | 
  22  | const ERROR_ROUTES = [
  23  |   { path: '/dashboard', label: 'Dashboard (needs auth)' },
  24  |   { path: '/admin', label: 'Admin (needs auth)' },
  25  | ];
  26  | 
  27  | test.describe('Part 2 — Customer Journey: Phase 2 — Navigation Validation', () => {
  28  | 
  29  |   for (const route of MAIN_ROUTES) {
  30  |     test(`Navigating to ${route.label} (${route.path}) returns 200 and renders content`, async ({ page }) => {
  31  |       const response = await page.goto(route.path, { waitUntil: 'networkidle' });
  32  |       expect(response?.status()).toBeLessThan(400);
  33  |       const title = await page.title();
  34  |       expect(title).toBeTruthy();
  35  |       const bodyContent = page.locator('body');
  36  |       const text = await bodyContent.innerText();
  37  |       expect(text.length).toBeGreaterThan(10);
  38  |     });
  39  |   }
  40  | 
  41  |   for (const route of PLACEHOLDER_ROUTES) {
  42  |     test(`Unimplemented route ${route.label} (${route.path}) shows appropriate message`, async ({ page }) => {
> 43  |       const response = await page.goto(route.path, { waitUntil: 'networkidle' });
      |                                   ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/cart
  44  |       if (response?.status() === 404) {
  45  |         await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
  46  |       } else {
  47  |         expect(response?.status()).toBeLessThan(400);
  48  |       }
  49  |     });
  50  |   }
  51  | 
  52  |   for (const route of ERROR_ROUTES) {
  53  |     test(`Protected route ${route.label} (${route.path}) redirects or shows auth error`, async ({ page }) => {
  54  |       const response = await page.goto(route.path, { waitUntil: 'networkidle' });
  55  |       const currentUrl = page.url();
  56  |       const isRedirected = currentUrl.includes('login') || currentUrl.includes('auth') || currentUrl.includes('access-denied');
  57  |       expect(response?.status() === 200 || isRedirected).toBeTruthy();
  58  |     });
  59  |   }
  60  | 
  61  |   test('Browser back button returns to previous page', async ({ page }) => {
  62  |     await page.goto('/');
  63  |     await page.waitForLoadState('networkidle');
  64  |     await page.goto('/about');
  65  |     await page.waitForLoadState('networkidle');
  66  |     await page.goBack();
  67  |     await page.waitForLoadState('networkidle');
  68  |     expect(page.url()).not.toContain('/about');
  69  |   });
  70  | 
  71  |   test('Browser forward button returns to next page', async ({ page }) => {
  72  |     await page.goto('/');
  73  |     await page.waitForLoadState('networkidle');
  74  |     await page.goto('/about');
  75  |     await page.waitForLoadState('networkidle');
  76  |     await page.goBack();
  77  |     await page.waitForLoadState('networkidle');
  78  |     await page.goForward();
  79  |     await page.waitForLoadState('networkidle');
  80  |     expect(page.url()).toContain('/about');
  81  |   });
  82  | 
  83  |   test('Deep link to /training resolves correctly', async ({ page }) => {
  84  |     const response = await page.goto('/training', { waitUntil: 'networkidle' });
  85  |     expect(response?.status()).toBeLessThan(400);
  86  |     expect(page.url()).toContain('/training');
  87  |   });
  88  | 
  89  |   test('Deep link to /blog resolves correctly', async ({ page }) => {
  90  |     const response = await page.goto('/blog', { waitUntil: 'networkidle' });
  91  |     expect(response?.status()).toBeLessThan(400);
  92  |     expect(page.url()).toContain('/blog');
  93  |   });
  94  | 
  95  |   test('Invalid route returns 404 page', async ({ page }) => {
  96  |     const response = await page.goto('/this-route-does-not-exist-xyz', { waitUntil: 'networkidle' });
  97  |     const status = response?.status() ?? 200;
  98  |     if (status === 200) {
  99  |       const body = page.locator('body');
  100 |       const text = await body.innerText();
  101 |       const has404 = text.includes('404') || text.includes('not found') || text.includes('Not Found');
  102 |       expect(has404 || page.url().includes('404')).toBeTruthy();
  103 |     } else {
  104 |       expect(status).toBe(404);
  105 |     }
  106 |   });
  107 | 
  108 |   test('Header logo or brand link navigates to home', async ({ page }) => {
  109 |     await page.goto('/about');
  110 |     await page.waitForLoadState('networkidle');
  111 |     const logo = page.locator('a[href="/"], a[href="/home"], header a').first();
  112 |     if (await logo.isVisible().catch(() => false)) {
  113 |       await logo.click();
  114 |       await page.waitForLoadState('networkidle');
  115 |       expect(page.url()).toBe(page.url().includes('?') ? page.url() : page.url().replace(/\/$/, '') || '/');
  116 |     }
  117 |   });
  118 | 
  119 |   test('Login link navigates to /login', async ({ page }) => {
  120 |     await page.goto('/');
  121 |     const loginLink = page.locator('a[href="/login"], a[href="/auth"], text=Sign In, text=Login').first();
  122 |     if (await loginLink.isVisible().catch(() => false)) {
  123 |       await loginLink.click();
  124 |       await page.waitForLoadState('networkidle');
  125 |       expect(page.url()).toContain('login');
  126 |     }
  127 |   });
  128 | 
  129 |   test('Navigation link active state reflects current page', async ({ page }) => {
  130 |     await page.goto('/about');
  131 |     await page.waitForLoadState('networkidle');
  132 |     const activeLinks = page.locator('nav a[class*="active"], nav a[aria-current="page"]');
  133 |     const count = await activeLinks.count();
  134 |     if (count > 0) {
  135 |       await expect(activeLinks.first()).toBeVisible();
  136 |     }
  137 |   });
  138 | 
  139 | });
  140 | 
```