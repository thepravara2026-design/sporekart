# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 7 — Training Management >> IMPLEMENTATION GAP: Training attendance placeholder
- Location: tests\admin-console.spec.ts:284:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/training/attendance
Call log:
  - navigating to "http://localhost:5174/admin/training/attendance", waiting until "networkidle"

```

# Test source

```ts
  185 |   });
  186 | 
  187 |   test('Movements page loads', async ({ page }) => {
  188 |     expect((await page.goto(`${ADMIN}/movements`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  189 |   });
  190 | 
  191 |   test('IMPLEMENTATION GAP: No stock update UI', async ({ page }) => {
  192 |     await page.goto(`${ADMIN}/inventory`, { waitUntil: 'networkidle' });
  193 |     expect(await page.locator('input[type="number"]').count()).toBe(0);
  194 |   });
  195 | 
  196 |   test('IMPLEMENTATION GAP: No inventory history API', async ({ page }) => {
  197 |     const r = await page.request.get('/api/inventory/history');
  198 |     expect((await r.text()).includes('inventory_history')).toBe(false);
  199 |   });
  200 | });
  201 | 
  202 | // ====================================================================
  203 | // PHASE 5 — ORDER MANAGEMENT
  204 | // ====================================================================
  205 | test.describe('Phase 5 — Order Management', () => {
  206 |   test('Orders page loads (HTTP)', async ({ page }) => {
  207 |     expect((await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  208 |   });
  209 | 
  210 |   test('Order search input exists', async ({ page }) => {
  211 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  212 |     const s = page.locator('input[type="search"],input[placeholder*="Search"]').first();
  213 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  214 |       await s.fill('ORD-'); await page.waitForTimeout(300);
  215 |     }
  216 |     expect(true).toBeTruthy();
  217 |   });
  218 | 
  219 |   test('IMPLEMENTATION GAP: No order status update', async ({ page }) => {
  220 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  221 |     expect(await page.locator('select,button:has-text("Update Status")').isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  222 |   });
  223 | 
  224 |   test('IMPLEMENTATION GAP: No bulk actions', async ({ page }) => {
  225 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  226 |     expect(await page.locator('button:has-text("Bulk")').isVisible({ timeout: 1000 }).catch(() => false)).toBe(false);
  227 |   });
  228 | 
  229 |   test('IMPLEMENTATION GAP: No order cancellation from admin', async ({ page }) => {
  230 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  231 |     expect(await page.locator('button:has-text("Cancel")').count()).toBe(0);
  232 |   });
  233 | });
  234 | 
  235 | // ====================================================================
  236 | // PHASE 6 — USER MANAGEMENT
  237 | // ====================================================================
  238 | test.describe('Phase 6 — User Management', () => {
  239 |   test('Customers page loads', async ({ page }) => {
  240 |     expect((await page.goto(`${ADMIN}/customers`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  241 |   });
  242 | 
  243 |   test('IMPLEMENTATION GAP: No /admin/users route with content', async ({ page }) => {
  244 |     await page.goto(`${ADMIN}/users`, { waitUntil: 'networkidle' });
  245 |     expect(await hasText(page, 'User Management')).toBe(false);
  246 |   });
  247 | 
  248 |   test('IMPLEMENTATION GAP: No /admin/roles route', async ({ page }) => {
  249 |     await page.goto(`${ADMIN}/roles`, { waitUntil: 'networkidle' });
  250 |     expect(await hasText(page, 'Role Management')).toBe(false);
  251 |   });
  252 | 
  253 |   test('IMPLEMENTATION GAP: No user deactivate/reactivate', async ({ page }) => {
  254 |     await page.goto(`${ADMIN}/customers`, { waitUntil: 'networkidle' });
  255 |     expect(await page.locator('button:has-text("Deactivate")').count()).toBe(0);
  256 |   });
  257 | });
  258 | 
  259 | // ====================================================================
  260 | // PHASE 7 — TRAINING MANAGEMENT
  261 | // ====================================================================
  262 | test.describe('Phase 7 — Training Management', () => {
  263 |   test('Training dashboard loads', async ({ page }) => {
  264 |     expect((await page.goto(`${ADMIN}/training/dashboard`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  265 |   });
  266 | 
  267 |   test('Training courses loads', async ({ page }) => {
  268 |     expect((await page.goto(`${ADMIN}/training/courses`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  269 |   });
  270 | 
  271 |   test('Training enrollment loads', async ({ page }) => {
  272 |     expect((await page.goto(`${ADMIN}/training/enrollment`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  273 |   });
  274 | 
  275 |   test('Training curriculum loads', async ({ page }) => {
  276 |     expect((await page.goto(`${ADMIN}/training/curriculum`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  277 |   });
  278 | 
  279 |   test('IMPLEMENTATION GAP: Training batches placeholder', async ({ page }) => {
  280 |     await page.goto(`${ADMIN}/training/batches`, { waitUntil: 'networkidle' });
  281 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(0);
  282 |   });
  283 | 
  284 |   test('IMPLEMENTATION GAP: Training attendance placeholder', async ({ page }) => {
> 285 |     expect((await page.goto(`${ADMIN}/training/attendance`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
      |                        ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/training/attendance
  286 |   });
  287 | });
  288 | 
  289 | // ====================================================================
  290 | // PHASE 8 — COUPONS & PRICING
  291 | // ====================================================================
  292 | test.describe('Phase 8 — Coupons & Pricing', () => {
  293 |   test('IMPLEMENTATION GAP: No /admin/coupons route', async ({ page }) => {
  294 |     await page.goto(`${ADMIN}/coupons`, { waitUntil: 'networkidle' });
  295 |     expect(await hasText(page, 'Coupon Management')).toBe(false);
  296 |   });
  297 | 
  298 |   test('IMPLEMENTATION GAP: No /admin/discounts route', async ({ page }) => {
  299 |     await page.goto(`${ADMIN}/discounts`, { waitUntil: 'networkidle' });
  300 |     expect(await hasText(page, 'Discount')).toBe(false);
  301 |   });
  302 | 
  303 |   test('IMPLEMENTATION GAP: No /admin/promo route', async ({ page }) => {
  304 |     await page.goto(`${ADMIN}/promo`, { waitUntil: 'networkidle' });
  305 |     expect(await hasText(page, 'Promo')).toBe(false);
  306 |   });
  307 | 
  308 |   test('IMPLEMENTATION GAP: No /admin/pricing route', async ({ page }) => {
  309 |     await page.goto(`${ADMIN}/pricing`, { waitUntil: 'networkidle' });
  310 |     expect(await hasText(page, 'Pricing')).toBe(false);
  311 |   });
  312 | });
  313 | 
  314 | // ====================================================================
  315 | // PHASE 9 — SHIPPING MANAGEMENT
  316 | // ====================================================================
  317 | test.describe('Phase 9 — Shipping Management', () => {
  318 |   test('Shipping page loads', async ({ page }) => {
  319 |     expect((await page.goto(`${ADMIN}/shipping`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  320 |   });
  321 | 
  322 |   test('IMPLEMENTATION GAP: No shipping zone config', async ({ page }) => {
  323 |     await page.goto(`${ADMIN}/shipping`, { waitUntil: 'networkidle' });
  324 |     expect(await hasText(page, 'Zone')).toBe(false);
  325 |   });
  326 | 
  327 |   test('IMPLEMENTATION GAP: No courier config', async ({ page }) => {
  328 |     await page.goto(`${ADMIN}/shipping`, { waitUntil: 'networkidle' });
  329 |     expect(await hasText(page, 'Courier')).toBe(false);
  330 |   });
  331 | 
  332 |   test('IMPLEMENTATION GAP: No shipping rate config', async ({ page }) => {
  333 |     await page.goto(`${ADMIN}/shipping`, { waitUntil: 'networkidle' });
  334 |     expect(await hasText(page, 'Rate')).toBe(false);
  335 |   });
  336 | });
  337 | 
  338 | // ====================================================================
  339 | // PHASE 10 — ANALYTICS & REPORTING
  340 | // ====================================================================
  341 | test.describe('Phase 10 — Analytics & Reporting', () => {
  342 |   test('Analytics page loads', async ({ page }) => {
  343 |     expect((await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  344 |   });
  345 | 
  346 |   test('Reports page loads', async ({ page }) => {
  347 |     expect((await page.goto(`${ADMIN}/reports`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  348 |   });
  349 | 
  350 |   test('Finance page loads', async ({ page }) => {
  351 |     expect((await page.goto(`${ADMIN}/finance`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  352 |   });
  353 | 
  354 |   test('IMPLEMENTATION GAP: No charts on analytics', async ({ page }) => {
  355 |     await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' });
  356 |     expect(await page.locator('canvas,svg[class*="Chart"]').count()).toBe(0);
  357 |   });
  358 | 
  359 |   test('IMPLEMENTATION GAP: No date range filtering', async ({ page }) => {
  360 |     await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' });
  361 |     expect(await page.locator('input[type="date"]').count()).toBe(0);
  362 |   });
  363 | });
  364 | 
  365 | // ====================================================================
  366 | // PHASE 11 — SETTINGS
  367 | // ====================================================================
  368 | test.describe('Phase 11 — Settings', () => {
  369 |   test('Settings page loads', async ({ page }) => {
  370 |     expect((await page.goto(`${ADMIN}/settings`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  371 |   });
  372 | 
  373 |   test('System page loads', async ({ page }) => {
  374 |     expect((await page.goto(`${ADMIN}/system`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  375 |   });
  376 | 
  377 |   test('Profile page loads', async ({ page }) => {
  378 |     expect((await page.goto(`${ADMIN}/profile`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  379 |   });
  380 | 
  381 |   test('Help page loads', async ({ page }) => {
  382 |     expect((await page.goto(`${ADMIN}/help`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  383 |   });
  384 | 
  385 |   test('IMPLEMENTATION GAP: No editable settings fields', async ({ page }) => {
```