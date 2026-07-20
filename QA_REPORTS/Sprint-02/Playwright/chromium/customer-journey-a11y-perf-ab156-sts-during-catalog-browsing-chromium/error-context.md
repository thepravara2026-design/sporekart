# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-a11y-perf.spec.ts >> Part 2 — Customer Journey: Phase 11 — Accessibility & Performance >> No failed network requests during catalog browsing
- Location: tests\customer-journey-a11y-perf.spec.ts:139:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
Call log:
  - navigating to "http://localhost:5173/products", waiting until "load"

```

# Test source

```ts
  44  |     expect(missingAlt).toBe(0);
  45  |   });
  46  | 
  47  |   test('Focus order is logical on homepage', async ({ page }) => {
  48  |     await page.goto('/');
  49  |     await page.keyboard.press('Tab');
  50  |     const focused = page.locator(':focus');
  51  |     const focusedTag = await focused.evaluate(el => el.tagName.toLowerCase());
  52  |     expect(['a', 'button', 'input', 'select', 'textarea']).toContain(focusedTag);
  53  |   });
  54  | 
  55  |   test('Color contrast is sufficient on text elements', async ({ page }) => {
  56  |     await page.goto('/');
  57  |     const textElements = page.locator('p, h1, h2, h3, h4, h5, h6, span, a');
  58  |     const count = await textElements.count();
  59  |     const sampleSize = Math.min(count, 30);
  60  |     for (let i = 0; i < sampleSize; i++) {
  61  |       const color = await textElements.nth(i).evaluate(el => getComputedStyle(el).color);
  62  |       expect(color).toBeTruthy();
  63  |     }
  64  |   });
  65  | 
  66  |   test('ARIA landmarks are present', async ({ page }) => {
  67  |     await page.goto('/');
  68  |     const main = page.locator('main, [role="main"]');
  69  |     const nav = page.locator('nav, [role="navigation"]');
  70  |     const footer = page.locator('footer, [role="contentinfo"]');
  71  |     await expect(main.first()).toBeVisible({ timeout: 5000 });
  72  |     await expect(nav.first()).toBeVisible({ timeout: 5000 });
  73  |     await expect(footer.first()).toBeVisible({ timeout: 5000 });
  74  |   });
  75  | 
  76  |   test('Form inputs have associated labels', async ({ page }) => {
  77  |     await page.goto('/login');
  78  |     const inputs = page.locator('input:not([type="hidden"])');
  79  |     const count = await inputs.count();
  80  |     let missingLabel = 0;
  81  |     for (let i = 0; i < count; i++) {
  82  |       const id = await inputs.nth(i).getAttribute('id');
  83  |       if (id) {
  84  |         const label = page.locator(`label[for="${id}"]`);
  85  |         if (await label.count() === 0) missingLabel++;
  86  |       } else {
  87  |         const ariaLabel = await inputs.nth(i).getAttribute('aria-label');
  88  |         if (!ariaLabel) missingLabel++;
  89  |       }
  90  |     }
  91  |     expect(missingLabel).toBeLessThanOrEqual(count);
  92  |   });
  93  | 
  94  |   test('Keyboard navigation works on products page', async ({ page }) => {
  95  |     await page.goto('/products');
  96  |     await page.waitForLoadState('networkidle');
  97  |     await page.keyboard.press('Tab');
  98  |     const focused = page.locator(':focus');
  99  |     await expect(focused).toBeVisible();
  100 |     for (let i = 0; i < 5; i++) {
  101 |       await page.keyboard.press('Tab');
  102 |       await page.waitForTimeout(100);
  103 |     }
  104 |   });
  105 | 
  106 |   // === PERFORMANCE ===
  107 | 
  108 |   test('Performance: Homepage load completes under 5s', async ({ page }) => {
  109 |     const start = Date.now();
  110 |     await page.goto('/');
  111 |     await page.waitForLoadState('networkidle');
  112 |     const loadTime = Date.now() - start;
  113 |     expect(loadTime).toBeLessThan(10000);
  114 |   });
  115 | 
  116 |   test('Performance: Products page load completes under 5s', async ({ page }) => {
  117 |     const start = Date.now();
  118 |     await page.goto('/products');
  119 |     await page.waitForLoadState('networkidle');
  120 |     const loadTime = Date.now() - start;
  121 |     expect(loadTime).toBeLessThan(10000);
  122 |   });
  123 | 
  124 |   test('Performance: Search response within threshold', async ({ page }) => {
  125 |     const start = Date.now();
  126 |     await page.goto('/search');
  127 |     await page.waitForLoadState('networkidle');
  128 |     const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
  129 |     if (await searchInput.isVisible().catch(() => false)) {
  130 |       await searchInput.first().fill('test');
  131 |       const searchStart = Date.now();
  132 |       await searchInput.first().press('Enter');
  133 |       await page.waitForTimeout(1500);
  134 |       const searchTime = Date.now() - searchStart;
  135 |       expect(searchTime).toBeLessThan(5000);
  136 |     }
  137 |   });
  138 | 
  139 |   test('No failed network requests during catalog browsing', async ({ page }) => {
  140 |     const failedRequests: string[] = [];
  141 |     page.on('requestfailed', request => {
  142 |       failedRequests.push(`${request.url()} (${request.failure()?.errorText})`);
  143 |     });
> 144 |     await page.goto('/products');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/products
  145 |     await page.waitForLoadState('networkidle');
  146 |     expect(failedRequests.length).toBe(0);
  147 |   });
  148 | 
  149 |   test('No console errors during catalog browsing', async ({ page }) => {
  150 |     const consoleErrors: string[] = [];
  151 |     page.on('console', msg => {
  152 |       if (msg.type() === 'error') consoleErrors.push(msg.text());
  153 |     });
  154 |     await page.goto('/products');
  155 |     await page.waitForLoadState('networkidle');
  156 |     expect(consoleErrors.length).toBe(0);
  157 |   });
  158 | 
  159 |   test('Memory usage stays stable across page navigations', async ({ page }) => {
  160 |     await page.goto('/');
  161 |     for (let i = 0; i < 5; i++) {
  162 |       await page.goto('/products');
  163 |       await page.waitForLoadState('networkidle');
  164 |       await page.goto('/');
  165 |       await page.waitForLoadState('networkidle');
  166 |       await page.goto('/about');
  167 |       await page.waitForLoadState('networkidle');
  168 |     }
  169 |     const metrics = await page.evaluate(() => (performance as any).memory ? {
  170 |       usedJSHeapSize: (performance as any).memory.usedJSHeapSize,
  171 |       totalJSHeapSize: (performance as any).memory.totalJSHeapSize,
  172 |     } : null);
  173 |     if (metrics) {
  174 |       expect(metrics.usedJSHeapSize).toBeLessThan(200 * 1024 * 1024);
  175 |     }
  176 |   });
  177 | 
  178 | });
  179 | 
```