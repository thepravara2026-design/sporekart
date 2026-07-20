# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 3 — Order History >> Browser restart preserves order session
- Location: tests\order-lifecycle.spec.ts:225:7

# Error details

```
Error: browserContext.newPage: Target page, context or browser has been closed
```

# Test source

```ts
  130 | 
  131 |   test('Deep link to order ID resolves correctly', async ({ page }) => {
  132 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  133 |     expect(page.url()).toContain('/dashboard/orders/ORD-2026-8842');
  134 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  135 |   });
  136 | });
  137 | 
  138 | // ============================================================================
  139 | // PHASE 3 — ORDER HISTORY
  140 | // ============================================================================
  141 | test.describe('Phase 3 — Order History', () => {
  142 |   test.beforeEach(async ({ page }) => { await login(page); });
  143 | 
  144 |   test('Orders dashboard renders with order list', async ({ page }) => {
  145 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  146 |     const t = await page.locator('body').innerText();
  147 |     expect(t.length).toBeGreaterThan(50);
  148 |     expect(t.includes('ORD') || t.includes('Order')).toBeTruthy();
  149 |   });
  150 | 
  151 |   test('Order statistics cards display correctly', async ({ page }) => {
  152 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  153 |     const t = await page.locator('body').innerText();
  154 |     expect(t.includes('Total Spend') && t.includes('Active Orders')).toBeTruthy();
  155 |   });
  156 | 
  157 |   test('AI Order Assistant section renders', async ({ page }) => {
  158 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  159 |     const t = await page.locator('body').innerText();
  160 |     expect(t.includes('AI Order Assistant') || t.includes('Order Assistant')).toBeTruthy();
  161 |   });
  162 | 
  163 |   test('Status filter tabs render', async ({ page }) => {
  164 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  165 |     const tabs = page.locator('button[role="tab"], button:has-text("All"),button:has-text("Active"),button:has-text("Completed"),button:has-text("Refunded")');
  166 |     expect(await tabs.first().isVisible()).toBeTruthy();
  167 |   });
  168 | 
  169 |   test('Filter by Active tab works', async ({ page }) => {
  170 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  171 |     const tabs = page.locator('button[role="tab"]');
  172 |     const count = await tabs.count();
  173 |     if (count >= 2) {
  174 |       await tabs.nth(1).click(); await page.waitForTimeout(500);
  175 |       expect(await tabs.nth(1).getAttribute('aria-selected')).toBe('true');
  176 |     }
  177 |   });
  178 | 
  179 |   test('Search input is present', async ({ page }) => {
  180 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  181 |     const s = page.locator('input[type="search"],input[type="text"][placeholder*="search" i],input[aria-label="Search orders"]');
  182 |     expect(await s.isVisible()).toBeTruthy();
  183 |   });
  184 | 
  185 |   test('Search input accepts text', async ({ page }) => {
  186 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  187 |     const s = page.locator('input[type="search"],input[type="text"][placeholder*="search" i],input[aria-label="Search orders"]');
  188 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  189 |       await s.fill('ORD-2026');
  190 |       expect(await s.inputValue()).toBe('ORD-2026');
  191 |     }
  192 |   });
  193 | 
  194 |   test('Order cards display order IDs, status, pricing', async ({ page }) => {
  195 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  196 |     const t = await page.locator('body').innerText();
  197 |     for (const oid of TEST_ORDERS) {
  198 |       expect(t.includes(oid)).toBeTruthy();
  199 |     }
  200 |   });
  201 | 
  202 |   test('Data persists on page refresh', async ({ page }) => {
  203 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  204 |     const before = (await page.locator('body').innerText()).length;
  205 |     await page.reload({ waitUntil: 'networkidle' });
  206 |     const after = (await page.locator('body').innerText()).length;
  207 |     expect(after).toBeGreaterThan(50);
  208 |   });
  209 | 
  210 |   test('Different order loads correctly', async ({ page }) => {
  211 |     await page.goto('/dashboard/orders/ORD-2026-5541', { waitUntil: 'networkidle' });
  212 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  213 |     await page.goto('/dashboard/orders/ORD-2026-9922', { waitUntil: 'networkidle' });
  214 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  215 |   });
  216 | 
  217 |   test('Empty state not shown when orders exist', async ({ page }) => {
  218 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  219 |     const t = await page.locator('body').innerText();
  220 |     // Should NOT show empty state since 4 orders exist
  221 |     const emptyState = t.includes('No orders found') || t.includes('no orders');
  222 |     expect(emptyState).toBe(false);
  223 |   });
  224 | 
  225 |   test('Browser restart preserves order session', async ({ page, context }) => {
  226 |     await login(page);
  227 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  228 |     const before = (await page.locator('body').innerText()).length;
  229 |     await context.close();
> 230 |     const p2 = await context.newPage();
      |                              ^ Error: browserContext.newPage: Target page, context or browser has been closed
  231 |     await login(p2);
  232 |     await p2.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  233 |     const after = (await p2.locator('body').innerText()).length;
  234 |     expect(after).toBeGreaterThan(50);
  235 |   });
  236 | });
  237 | 
  238 | // ============================================================================
  239 | // PHASE 4 — ORDER STATUS
  240 | // ============================================================================
  241 | test.describe('Phase 4 — Order Status', () => {
  242 |   test.beforeEach(async ({ page }) => { await login(page); });
  243 | 
  244 |   test('Orders show correct status badges', async ({ page }) => {
  245 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  246 |     const t = await page.locator('body').innerText();
  247 |     expect(t.includes('In Transit') || t.includes('Delivered') || t.includes('Refunded') || t.includes('Processing')).toBeTruthy();
  248 |   });
  249 | 
  250 |   test('ORD-2026-8842 shows In Transit status', async ({ page }) => {
  251 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  252 |     const t = await page.locator('body').innerText();
  253 |     expect(t.includes('In Transit') || t.includes('Transit')).toBeTruthy();
  254 |   });
  255 | 
  256 |   test('ORD-2026-7715 shows Delivered status', async ({ page }) => {
  257 |     await page.goto('/dashboard/orders/ORD-2026-7715', { waitUntil: 'networkidle' });
  258 |     const t = await page.locator('body').innerText();
  259 |     expect(t.includes('Delivered') || t.includes('delivered')).toBeTruthy();
  260 |   });
  261 | 
  262 |   test('ORD-2026-5541 shows Refunded status', async ({ page }) => {
  263 |     await page.goto('/dashboard/orders/ORD-2026-5541', { waitUntil: 'networkidle' });
  264 |     const t = await page.locator('body').innerText();
  265 |     expect(t.includes('Refunded') || t.includes('refunded')).toBeTruthy();
  266 |   });
  267 | 
  268 |   test('ORD-2026-9922 shows Processing status', async ({ page }) => {
  269 |     await page.goto('/dashboard/orders/ORD-2026-9922', { waitUntil: 'networkidle' });
  270 |     const t = await page.locator('body').innerText();
  271 |     expect(t.includes('Processing') || t.includes('processing')).toBeTruthy();
  272 |   });
  273 | 
  274 |   test('Status filter tabs exist with correct labels', async ({ page }) => {
  275 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  276 |     const t = await page.locator('body').innerText();
  277 |     expect(t.includes('All') && t.includes('Active') && t.includes('Completed') && t.includes('Refunded')).toBeTruthy();
  278 |   });
  279 | 
  280 |   test('IMPLEMENTATION GAP: Status transition UI does not exist', async ({ page }) => {
  281 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  282 |     const t = await page.locator('body').innerText();
  283 |     // No status change buttons should exist
  284 |     expect(t.includes('Update Status') || t.includes('Change Status')).toBe(false);
  285 |   });
  286 | 
  287 |   test('IMPLEMENTATION GAP: No cancel order button', async ({ page }) => {
  288 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  289 |     const t = await page.locator('body').innerText();
  290 |     expect(t.includes('Cancel Order') || t.includes('cancel')).toBe(false);
  291 |   });
  292 | });
  293 | 
  294 | // ============================================================================
  295 | // PHASE 5 — CUSTOMER ACTIONS
  296 | // ============================================================================
  297 | test.describe('Phase 5 — Customer Actions', () => {
  298 |   test.beforeEach(async ({ page }) => { await login(page); });
  299 | 
  300 |   test('View Details link navigates to order detail', async ({ page }) => {
  301 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  302 |     const btn = page.locator('button:has-text("View Details")').first();
  303 |     if (await btn.isVisible({ timeout: 3000 }).catch(() => false)) {
  304 |       await btn.click(); await page.waitForLoadState('networkidle');
  305 |       expect(page.url()).toContain('/dashboard/orders/');
  306 |     }
  307 |   });
  308 | 
  309 |   test('Track Order button visible for In Transit order', async ({ page }) => {
  310 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  311 |     const btn = page.locator('button:has-text("Track"),a:has-text("Track")').first();
  312 |     expect(await btn.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  313 |   });
  314 | 
  315 |   test('Return/Refund button visible for Delivered eligible order', async ({ page }) => {
  316 |     await page.goto('/dashboard/orders/ORD-2026-7715', { waitUntil: 'networkidle' });
  317 |     const btn = page.locator('button:has-text("Return"),button:has-text("Refund")').first();
  318 |     expect(await btn.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  319 |   });
  320 | 
  321 |   test('Refund page renders return request form', async ({ page }) => {
  322 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  323 |     const t = await page.locator('body').innerText();
  324 |     expect(t.length).toBeGreaterThan(50);
  325 |     expect(t.includes('Return') || t.includes('Refund')).toBeTruthy();
  326 |   });
  327 | 
  328 |   test('Refund form has item selection', async ({ page }) => {
  329 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  330 |     const cb = page.locator('input[type="checkbox"]').first();
```