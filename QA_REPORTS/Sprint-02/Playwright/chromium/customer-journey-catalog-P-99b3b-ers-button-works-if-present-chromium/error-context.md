# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-catalog.spec.ts >> Part 2 — Customer Journey: Phases 3-6 — Catalog, Search, Filters, Sorting >> Clear filters button works if present
- Location: tests\customer-journey-catalog.spec.ts:190:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
Call log:
  - navigating to "http://localhost:5173/products", waiting until "networkidle"

```

# Test source

```ts
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
  183 |     if (count >= 2) {
  184 |       await allFilters.nth(0).selectOption(1);
  185 |       await allFilters.nth(1).selectOption(1);
  186 |       await page.waitForTimeout(500);
  187 |     }
  188 |   });
  189 | 
  190 |   test('Clear filters button works if present', async ({ page }) => {
> 191 |     await page.goto('/products', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
  192 |     const clearBtn = page.locator('button:has-text("Clear"), button:has-text("Reset"), [class*="clear"]');
  193 |     if (await clearBtn.isVisible().catch(() => false)) {
  194 |       await clearBtn.click();
  195 |       await page.waitForTimeout(500);
  196 |     }
  197 |   });
  198 | 
  199 |   // === PHASE 6: SORTING ===
  200 | 
  201 |   test('Sort controls exist on product/search pages', async ({ page }) => {
  202 |     await page.goto('/products', { waitUntil: 'networkidle' });
  203 |     const sortControls = page.locator('select[aria-label*="sort" i], button:has-text("Sort"), [class*="sort"], [class*="Sort"]');
  204 |     const count = await sortControls.count();
  205 |     expect(count).toBeGreaterThanOrEqual(0);
  206 |   });
  207 | 
  208 |   test('Sort by price ascending works if present', async ({ page }) => {
  209 |     await page.goto('/products', { waitUntil: 'networkidle' });
  210 |     const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
  211 |     if (await sortSelect.isVisible().catch(() => false)) {
  212 |       const options = await sortSelect.locator('option').allTextContents();
  213 |       const ascOption = options.findIndex(o => o.toLowerCase().includes('low') || o.toLowerCase().includes('asc') || o.toLowerCase().includes('price'));
  214 |       if (ascOption >= 0) {
  215 |         await sortSelect.selectOption(ascOption);
  216 |         await page.waitForTimeout(500);
  217 |       }
  218 |     }
  219 |   });
  220 | 
  221 |   test('Sort by price descending works if present', async ({ page }) => {
  222 |     await page.goto('/products', { waitUntil: 'networkidle' });
  223 |     const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
  224 |     if (await sortSelect.isVisible().catch(() => false)) {
  225 |       const options = await sortSelect.locator('option').allTextContents();
  226 |       const descOption = options.findIndex(o => o.toLowerCase().includes('high') || o.toLowerCase().includes('desc') || o.toLowerCase().includes('price'));
  227 |       if (descOption >= 0) {
  228 |         await sortSelect.selectOption(descOption);
  229 |         await page.waitForTimeout(500);
  230 |       }
  231 |     }
  232 |   });
  233 | 
  234 |   test('Sorting persists after filtering if both exist', async ({ page }) => {
  235 |     await page.goto('/products', { waitUntil: 'networkidle' });
  236 |     const sortSelect = page.locator('select[aria-label*="sort" i], select.sort');
  237 |     const filterSelect = page.locator('select[aria-label*="category" i], [class*="filter"] select');
  238 |     if (await sortSelect.isVisible().catch(() => false) && await filterSelect.isVisible().catch(() => false)) {
  239 |       await sortSelect.first().selectOption(1);
  240 |       await filterSelect.first().selectOption(1);
  241 |       await page.waitForTimeout(500);
  242 |     }
  243 |   });
  244 | 
  245 | });
  246 | 
```