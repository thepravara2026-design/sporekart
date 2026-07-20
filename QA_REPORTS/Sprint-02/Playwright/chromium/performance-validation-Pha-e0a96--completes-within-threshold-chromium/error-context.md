# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: performance-validation.spec.ts >> Phase 3 — Navigation Performance >> Route transition completes within threshold
- Location: tests\performance-validation.spec.ts:235:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
Call log:
  - navigating to "http://localhost:5173/", waiting until "load"

```

# Test source

```ts
  136 | test.describe('Phase 1 — Application Startup', () => {
  137 | 
  138 |   test('Cold start — initial page load within threshold', async ({ page }) => {
  139 |     const result = await navigateAndMeasure(page, '/login');
  140 |     expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  141 |     expect(result.dom.totalNodes).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes);
  142 |   });
  143 | 
  144 |   test('First Contentful Paint within threshold', async ({ page }) => {
  145 |     await page.goto('/login');
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
> 236 |     await page.goto('/');
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
  237 |     await page.waitForLoadState('networkidle');
  238 |     const start = Date.now();
  239 |     await page.goto('/products');
  240 |     await page.waitForLoadState('networkidle');
  241 |     const transitionTime = Date.now() - start;
  242 |     expect(transitionTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  243 |   });
  244 | 
  245 |   test('Browser back navigation restores previous page', async ({ page }) => {
  246 |     await page.goto('/products');
  247 |     await page.waitForLoadState('networkidle');
  248 |     await page.goto('/cart');
  249 |     await page.waitForLoadState('networkidle');
  250 |     const start = Date.now();
  251 |     await page.goBack();
  252 |     await page.waitForLoadState('networkidle');
  253 |     const backTime = Date.now() - start;
  254 |     expect(backTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  255 |     expect(page.url()).toContain('products');
  256 |   });
  257 | 
  258 |   test('Browser forward navigation restores next page', async ({ page }) => {
  259 |     await page.goto('/');
  260 |     await page.waitForLoadState('networkidle');
  261 |     await page.goto('/orders');
  262 |     await page.waitForLoadState('networkidle');
  263 |     await page.goBack();
  264 |     await page.waitForLoadState('networkidle');
  265 |     const start = Date.now();
  266 |     await page.goForward();
  267 |     await page.waitForLoadState('networkidle');
  268 |     const forwardTime = Date.now() - start;
  269 |     expect(forwardTime).toBeLessThan(PERFORMANCE_THRESHOLDS.navigation);
  270 |   });
  271 | 
  272 |   test('Page refresh maintains stability', async ({ page }) => {
  273 |     await page.goto('/dashboard');
  274 |     await page.waitForLoadState('networkidle');
  275 |     const start = Date.now();
  276 |     await page.reload();
  277 |     await page.waitForLoadState('networkidle');
  278 |     const reloadTime = Date.now() - start;
  279 |     expect(reloadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  280 |   });
  281 | 
  282 |   test('Deep links resolve correctly', async ({ page }) => {
  283 |     const deepRoutes = ['/training/courses', '/dashboard/orders', '/admin/dashboard'];
  284 |     for (const route of deepRoutes) {
  285 |       const start = Date.now();
  286 |       await page.goto(route);
  287 |       await page.waitForLoadState('networkidle');
  288 |       const loadTime = Date.now() - start;
  289 |       expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  290 |     }
  291 |   });
  292 | 
  293 |   test('Repeated navigation between routes remains fast', async ({ page }) => {
  294 |     await page.goto('/');
  295 |     await page.waitForLoadState('networkidle');
  296 |     for (let i = 0; i < 5; i++) {
  297 |       const start = Date.now();
  298 |       for (const route of ['/', '/products', '/cart', '/orders']) {
  299 |         await page.goto(route);
  300 |         await page.waitForLoadState('networkidle');
  301 |       }
  302 |       const cycleTime = Date.now() - start;
  303 |       expect(cycleTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad * 4);
  304 |     }
  305 |   });
  306 | 
  307 |   test('CSS transitions and animations complete smoothly', async ({ page }) => {
  308 |     await page.goto('/');
  309 |     await page.waitForLoadState('networkidle');
  310 |     const hasAnimations = await page.evaluate(() => {
  311 |       const styles = document.querySelectorAll('style, link[rel="stylesheet"]');
  312 |       return styles.length > 0;
  313 |     });
  314 |     expect(hasAnimations).toBe(true);
  315 |   });
  316 | });
  317 | 
  318 | // ===================================================================
  319 | // PHASE 4 — DATA LOADING
  320 | // ===================================================================
  321 | test.describe('Phase 4 — Data Loading', () => {
  322 | 
  323 |   test('Product list renders with acceptable DOM size', async ({ page }) => {
  324 |     const result = await navigateAndMeasure(page, '/products');
  325 |     expect(result.dom.totalNodes).toBeLessThan(PERFORMANCE_THRESHOLDS.domNodes);
  326 |     const hasItems = await page.locator('[class*="product"], [class*="card"], li, tr').first().isVisible().catch(() => false);
  327 |   });
  328 | 
  329 |   test('Order history loads within threshold', async ({ page }) => {
  330 |     const result = await navigateAndMeasure(page, '/dashboard/orders');
  331 |     expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  332 |   });
  333 | 
  334 |   test('Training list loads within threshold', async ({ page }) => {
  335 |     const result = await navigateAndMeasure(page, '/training/courses');
  336 |     expect(result.loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
```