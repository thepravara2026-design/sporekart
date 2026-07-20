# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: performance-validation.spec.ts >> Phase 1 — Application Startup >> First Contentful Paint within threshold
- Location: tests\performance-validation.spec.ts:144:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
Call log:
  - navigating to "http://localhost:5173/login", waiting until "load"

```

# Test source

```ts
  45  | async function measurePageTiming(page: Page) {
  46  |   return page.evaluate(() => {
  47  |     const nav = performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming | undefined;
  48  |     const paint = performance.getEntriesByType('paint');
  49  |     const fcpEntry = paint.find(e => e.name === 'first-contentful-paint');
  50  |     return {
  51  |       domContentLoaded: nav ? nav.domContentLoadedEventEnd - nav.domContentLoadedEventStart : -1,
  52  |       loadEventEnd: nav ? nav.loadEventEnd - nav.loadEventStart : -1,
  53  |       domInteractive: nav ? nav.domInteractive : -1,
  54  |       domComplete: nav ? nav.domComplete : -1,
  55  |       domContentLoadedEventEnd: nav ? nav.domContentLoadedEventEnd : -1,
  56  |       requestStart: nav ? nav.requestStart : -1,
  57  |       responseEnd: nav ? nav.responseEnd : -1,
  58  |       firstContentfulPaint: fcpEntry ? fcpEntry.startTime : -1,
  59  |       fcpTime: fcpEntry ? fcpEntry.startTime : -1,
  60  |     };
  61  |   });
  62  | }
  63  | 
  64  | async function measureDomSize(page: Page) {
  65  |   return page.evaluate(() => {
  66  |     return {
  67  |       totalNodes: document.querySelectorAll('*').length,
  68  |       divCount: document.querySelectorAll('div').length,
  69  |       spanCount: document.querySelectorAll('span').length,
  70  |       imgCount: document.querySelectorAll('img').length,
  71  |       scriptCount: document.querySelectorAll('script').length,
  72  |       linkCount: document.querySelectorAll('link').length,
  73  |       inputCount: document.querySelectorAll('input').length,
  74  |       buttonCount: document.querySelectorAll('button').length,
  75  |     };
  76  |   });
  77  | }
  78  | 
  79  | async function measureResourceCount(page: Page) {
  80  |   return page.evaluate(() => {
  81  |     const resources = performance.getEntriesByType('resource');
  82  |     const jsResources = resources.filter(r => r.name.endsWith('.js') || r.name.includes('.js?'));
  83  |     const cssResources = resources.filter(r => r.name.endsWith('.css') || r.name.includes('.css?'));
  84  |     const imgResources = resources.filter(r => /\.(png|jpg|jpeg|gif|svg|webp)/i.test(r.name));
  85  |     const totalSize = resources.reduce((sum, r:any) => sum + (r.transferSize || r.encodedBodySize || 0), 0);
  86  |     return {
  87  |       total: resources.length,
  88  |       js: jsResources.length,
  89  |       css: cssResources.length,
  90  |       images: imgResources.length,
  91  |       totalTransferSize: totalSize,
  92  |     };
  93  |   });
  94  | }
  95  | 
  96  | async function measureMemory(page: Page) {
  97  |   return page.evaluate(() => {
  98  |     const mem = (performance as any).memory;
  99  |     return mem ? {
  100 |       usedJSHeapSize: mem.usedJSHeapSize,
  101 |       totalJSHeapSize: mem.totalJSHeapSize,
  102 |       jsHeapSizeLimit: mem.jsHeapSizeLimit,
  103 |     } : null;
  104 |   });
  105 | }
  106 | 
  107 | async function measureInteractionDelay(page: Page, selector: string) {
  108 |   const start = Date.now();
  109 |   const el = page.locator(selector);
  110 |   await el.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  111 |   const foundTime = Date.now() - start;
  112 |   if (await el.isVisible().catch(() => false)) {
  113 |     const respStart = Date.now();
  114 |     await el.click();
  115 |     const respTime = Date.now() - respStart;
  116 |     return { found: foundTime, response: respTime };
  117 |   }
  118 |   return { found: foundTime, response: -1 };
  119 | }
  120 | 
  121 | async function navigateAndMeasure(page: Page, path: string) {
  122 |   const start = Date.now();
  123 |   await page.goto(path);
  124 |   await page.waitForLoadState('networkidle');
  125 |   const loadTime = Date.now() - start;
  126 |   const timing = await measurePageTiming(page);
  127 |   const dom = await measureDomSize(page);
  128 |   const resources = await measureResourceCount(page);
  129 |   const mem = await measureMemory(page);
  130 |   return { loadTime, timing, dom, resources, mem, path };
  131 | }
  132 | 
  133 | // ===================================================================
  134 | // PHASE 1 — APPLICATION STARTUP
  135 | // ===================================================================
  136 | test.describe('Phase 1 — Application Startup', () => {
  137 | 
  138 |   test('Cold start — initial page load within threshold', async ({ page }) => {
  139 |     const result = await navigateAndMeasure(page, '/login');
  140 |     expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  141 |     expect(result.dom.totalNodes).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes);
  142 |   });
  143 | 
  144 |   test('First Contentful Paint within threshold', async ({ page }) => {
> 145 |     await page.goto('/login');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/login
  146 |     await page.waitForLoadState('networkidle');
  147 |     const timing = await measurePageTiming(page);
  148 |     if (timing.firstContentfulPaint > 0) {
  149 |       expect(timing.firstContentfulPaint).toBeLessThan(PERFORMANCE_THRESHOLDS.fcp);
  150 |     }
  151 |   });
  152 | 
  153 |   test('DOM Content Loaded completes within threshold', async ({ page }) => {
  154 |     await page.goto('/');
  155 |     await page.waitForLoadState('networkidle');
  156 |     const timing = await measurePageTiming(page);
  157 |     expect(timing.domContentLoadedEventEnd).toBeGreaterThan(0);
  158 |   });
  159 | 
  160 |   test('Minimal JavaScript bundle size', async ({ page }) => {
  161 |     await page.goto('/login');
  162 |     await page.waitForLoadState('networkidle');
  163 |     const resources = await measureResourceCount(page);
  164 |     expect(resources.js).toBeLessThan(30);
  165 |     expect(resources.total).toBeLessThan(PERFORMANCE_THRESHOLDS.requestsPerPage);
  166 |   });
  167 | 
  168 |   test('Loading indicator shown during initial load', async ({ page }) => {
  169 |     await page.goto('/');
  170 |     const hasSpinner = await page.locator('.sk-loading, [class*="spinner"], [class*="loader"], .sk-skeleton, [role="progressbar"]')
  171 |       .first().isVisible().catch(() => false);
  172 |   });
  173 | 
  174 |   test('Warm start — subsequent page load faster', async ({ page }) => {
  175 |     await page.goto('/');
  176 |     await page.waitForLoadState('networkidle');
  177 |     const warmStart = Date.now();
  178 |     await page.goto('/login');
  179 |     await page.waitForLoadState('networkidle');
  180 |     const warmLoadTime = Date.now() - warmStart;
  181 |     expect(warmLoadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  182 |   });
  183 | 
  184 |   test('No JavaScript errors during application bootstrap', async ({ page }) => {
  185 |     const errors: string[] = [];
  186 |     page.on('pageerror', err => errors.push(err.message));
  187 |     await page.goto('/');
  188 |     await page.waitForLoadState('networkidle');
  189 |     expect(errors.length).toBe(0);
  190 |   });
  191 | 
  192 |   test('Application shell renders correctly', async ({ page }) => {
  193 |     await page.goto('/');
  194 |     await page.waitForLoadState('networkidle');
  195 |     await expect(page.locator('body')).toBeVisible();
  196 |     const body = await page.locator('body').innerText();
  197 |     expect(body.length).toBeGreaterThan(0);
  198 |   });
  199 | });
  200 | 
  201 | // ===================================================================
  202 | // PHASE 2 — PAGE PERFORMANCE
  203 | // ===================================================================
  204 | test.describe('Phase 2 — Page Performance', () => {
  205 | 
  206 |   KEY_ROUTES.forEach(route => {
  207 |     test(`${route.name} page loads within threshold`, async ({ page }) => {
  208 |       const result = await navigateAndMeasure(page, route.path);
  209 |       expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  210 |       expect(result.dom.totalNodes).toBeGreaterThan(0);
  211 |       expect(result.resources.total).toBeGreaterThan(0);
  212 |     });
  213 |   });
  214 | 
  215 |   test('All admin routes load within threshold', async ({ page }) => {
  216 |     for (const route of ADMIN_ROUTES) {
  217 |       const result = await navigateAndMeasure(page, route);
  218 |       expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  219 |     }
  220 |   });
  221 | 
  222 |   test('All training routes load within threshold', async ({ page }) => {
  223 |     for (const route of TRAINING_ROUTES) {
  224 |       const result = await navigateAndMeasure(page, route);
  225 |       expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  226 |     }
  227 |   });
  228 | });
  229 | 
  230 | // ===================================================================
  231 | // PHASE 3 — NAVIGATION PERFORMANCE
  232 | // ===================================================================
  233 | test.describe('Phase 3 — Navigation Performance', () => {
  234 | 
  235 |   test('Route transition completes within threshold', async ({ page }) => {
  236 |     await page.goto('/');
  237 |     await page.waitForLoadState('networkidle');
  238 |     const start = Date.now();
  239 |     await page.goto('/products');
  240 |     await page.waitForLoadState('networkidle');
  241 |     const transitionTime = Date.now() - start;
  242 |     expect(transitionTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  243 |   });
  244 | 
  245 |   test('Browser back navigation restores previous page', async ({ page }) => {
```