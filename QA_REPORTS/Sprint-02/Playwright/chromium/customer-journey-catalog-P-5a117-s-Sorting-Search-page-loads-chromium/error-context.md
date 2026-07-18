# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-catalog.spec.ts >> Part 2 — Customer Journey: Phases 3-6 — Catalog, Search, Filters, Sorting >> Search page loads
- Location: tests\customer-journey-catalog.spec.ts:81:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/search
Call log:
  - navigating to "http://localhost:5173/search", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('Part 2 — Customer Journey: Phases 3-6 — Catalog, Search, Filters, Sorting', () => {
  4   | 
  5   |   // === PHASE 3: PRODUCT CATALOG ===
  6   | 
  7   |   test('Products page loads and shows category cards', async ({ page }) => {
  8   |     const response = await page.goto('/products', { waitUntil: 'networkidle' });
  9   |     expect(response?.status()).toBeLessThan(400);
  10  |     const bodyText = await page.locator('body').innerText();
  11  |     expect(bodyText.length).toBeGreaterThan(20);
  12  |   });
  13  | 
  14  |   test('Product categories are displayed', async ({ page }) => {
  15  |     await page.goto('/products', { waitUntil: 'networkidle' });
  16  |     const categoryCards = page.locator('[class*="card"], [class*="category"], [class*="CategoryCard"], section a');
  17  |     const count = await categoryCards.count();
  18  |     expect(count).toBeGreaterThanOrEqual(1);
  19  |   });
  20  | 
  21  |   test('Product category card links navigate somewhere', async ({ page }) => {
  22  |     await page.goto('/products', { waitUntil: 'networkidle' });
  23  |     const links = page.locator('a[href*="product"], a[href*="category"], section a');
  24  |     const count = await links.count();
  25  |     if (count > 0) {
  26  |       const href = await links.first().getAttribute('href');
  27  |       expect(href).toBeTruthy();
  28  |     }
  29  |   });
  30  | 
  31  |   test('No broken images on products page', async ({ page }) => {
  32  |     await page.goto('/products', { waitUntil: 'networkidle' });
  33  |     const images = page.locator('img');
  34  |     const count = await images.count();
  35  |     let broken = 0;
  36  |     for (let i = 0; i < count; i++) {
  37  |       const img = images.nth(i);
  38  |       const src = await img.getAttribute('src');
  39  |       if (!src || src === '' || src.startsWith('data:')) continue;
  40  |       const naturalWidth = await img.evaluate(el => (el as HTMLImageElement).naturalWidth);
  41  |       if (naturalWidth === 0) broken++;
  42  |     }
  43  |     expect(broken).toBe(0);
  44  |   });
  45  | 
  46  |   test('Featured products on homepage show product information', async ({ page }) => {
  47  |     await page.goto('/', { waitUntil: 'networkidle' });
  48  |     const productSections = page.locator('section').filter({ hasText: /Product/i });
  49  |     const sectionCount = await productSections.count();
  50  |     if (sectionCount > 0) {
  51  |       const section = productSections.first();
  52  |       const productCards = section.locator('[class*="card"], [class*="ProductCard"], [class*="product"]');
  53  |       const cardCount = await productCards.count();
  54  |       if (cardCount > 0) {
  55  |         await expect(productCards.first()).toBeVisible();
  56  |       }
  57  |     }
  58  |   });
  59  | 
  60  |   test('Products page handles loading state', async ({ page }) => {
  61  |     await page.goto('/products', { waitUntil: 'domcontentloaded' });
  62  |     const spinner = page.locator('[class*="spinner"], [class*="loading"], [class*="skeleton"], [class*="Loader"]');
  63  |     const present = await spinner.isVisible().catch(() => false);
  64  |     if (present) {
  65  |       await page.waitForLoadState('networkidle');
  66  |       await expect(spinner).not.toBeVisible({ timeout: 10000 });
  67  |     }
  68  |   });
  69  | 
  70  |   test('Products page handles empty state', async ({ page }) => {
  71  |     await page.goto('/products', { waitUntil: 'networkidle' });
  72  |     const emptyMsg = page.locator('text=No products, no results, empty, Nothing here, No items').first();
  73  |     const present = await emptyMsg.isVisible().catch(() => false);
  74  |     if (present) {
  75  |       await expect(emptyMsg).toBeVisible();
  76  |     }
  77  |   });
  78  | 
  79  |   // === PHASE 4: SEARCH ===
  80  | 
  81  |   test('Search page loads', async ({ page }) => {
> 82  |     const response = await page.goto('/search', { waitUntil: 'networkidle' });
      |                                 ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/search
  83  |     expect(response?.status()).toBeLessThan(400);
  84  |   });
  85  | 
  86  |   test('Search input field exists and is interactive', async ({ page }) => {
  87  |     await page.goto('/search', { waitUntil: 'networkidle' });
  88  |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i], textarea[placeholder*="search" i]');
  89  |     const count = await searchInput.count();
  90  |     if (count > 0) {
  91  |       await searchInput.first().fill('test');
  92  |       const value = await searchInput.first().inputValue();
  93  |       expect(value).toBe('test');
  94  |     }
  95  |   });
  96  | 
  97  |   test('Search returns results for exact match', async ({ page }) => {
  98  |     await page.goto('/search', { waitUntil: 'networkidle' });
  99  |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  100 |     if (await searchInput.isVisible().catch(() => false)) {
  101 |       await searchInput.first().fill('mushroom');
  102 |       await searchInput.first().press('Enter');
  103 |       await page.waitForTimeout(1000);
  104 |       const results = page.locator('[class*="result"], [class*="card"], article, li');
  105 |       const count = await results.count();
  106 |       expect(count).toBeGreaterThanOrEqual(0);
  107 |     }
  108 |   });
  109 | 
  110 |   test('Search with no results shows appropriate message', async ({ page }) => {
  111 |     await page.goto('/search', { waitUntil: 'networkidle' });
  112 |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  113 |     if (await searchInput.isVisible().catch(() => false)) {
  114 |       await searchInput.first().fill('zzzzzznotfound');
  115 |       await searchInput.first().press('Enter');
  116 |       await page.waitForTimeout(1000);
  117 |       const emptyMsg = page.locator('text=No results, no matches, nothing found, empty, Nothing here').first();
  118 |       const visible = await emptyMsg.isVisible().catch(() => false);
  119 |       if (!visible) {
  120 |         const results = page.locator('[class*="result"], article, li');
  121 |         const count = await results.count();
  122 |         expect(count).toBeGreaterThanOrEqual(0);
  123 |       }
  124 |     }
  125 |   });
  126 | 
  127 |   test('Case insensitive search works', async ({ page }) => {
  128 |     await page.goto('/search', { waitUntil: 'networkidle' });
  129 |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  130 |     if (await searchInput.isVisible().catch(() => false)) {
  131 |       await searchInput.first().fill('MUSHROOM');
  132 |       await searchInput.first().press('Enter');
  133 |       await page.waitForTimeout(1000);
  134 |     }
  135 |   });
  136 | 
  137 |   test('Search with special characters handles gracefully', async ({ page }) => {
  138 |     await page.goto('/search', { waitUntil: 'networkidle' });
  139 |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  140 |     if (await searchInput.isVisible().catch(() => false)) {
  141 |       await searchInput.first().fill('!@#$%^&*()');
  142 |       await searchInput.first().press('Enter');
  143 |       await page.waitForTimeout(1000);
  144 |       expect(page.url()).toContain('search');
  145 |     }
  146 |   });
  147 | 
  148 |   test('Search resets properly', async ({ page }) => {
  149 |     await page.goto('/search', { waitUntil: 'networkidle' });
  150 |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  151 |     if (await searchInput.isVisible().catch(() => false)) {
  152 |       await searchInput.first().fill('test');
  153 |       await searchInput.first().press('Enter');
  154 |       await page.waitForTimeout(500);
  155 |       await searchInput.first().fill('');
  156 |       await searchInput.first().press('Enter');
  157 |       await page.waitForTimeout(500);
  158 |     }
  159 |   });
  160 | 
  161 |   // === PHASE 5: FILTERS ===
  162 | 
  163 |   test('Filter controls exist on product/search pages', async ({ page }) => {
  164 |     await page.goto('/products', { waitUntil: 'networkidle' });
  165 |     const filters = page.locator('select, [class*="filter"], [class*="Filter"], button:has-text("Filter"), [aria-label*="filter" i]');
  166 |     const count = await filters.count();
  167 |     expect(count).toBeGreaterThanOrEqual(0);
  168 |   });
  169 | 
  170 |   test('Category filter interaction works if present', async ({ page }) => {
  171 |     await page.goto('/products', { waitUntil: 'networkidle' });
  172 |     const categoryFilter = page.locator('select[aria-label*="category" i], [class*="category"] select');
  173 |     if (await categoryFilter.isVisible().catch(() => false)) {
  174 |       await categoryFilter.first().selectOption(1);
  175 |       await page.waitForTimeout(500);
  176 |     }
  177 |   });
  178 | 
  179 |   test('Multiple filters can be combined if present', async ({ page }) => {
  180 |     await page.goto('/products', { waitUntil: 'networkidle' });
  181 |     const allFilters = page.locator('select, [class*="filter"] select');
  182 |     const count = await allFilters.count();
```