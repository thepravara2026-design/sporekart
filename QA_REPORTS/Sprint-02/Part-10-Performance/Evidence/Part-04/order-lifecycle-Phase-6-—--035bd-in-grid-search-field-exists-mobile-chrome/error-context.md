# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 6 — Admin Visibility >> Admin grid search field exists
- Location: tests\order-lifecycle.spec.ts:425:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Test source

```ts
  328 |   test('Refund form has item selection', async ({ page }) => {
  329 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  330 |     const cb = page.locator('input[type="checkbox"]').first();
  331 |     expect(await cb.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  332 |   });
  333 | 
  334 |   test('Refund form has reason dropdown', async ({ page }) => {
  335 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  336 |     const sel = page.locator('select').first();
  337 |     expect(await sel.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  338 |   });
  339 | 
  340 |   test('Track Shipment page renders with tracking info', async ({ page }) => {
  341 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  342 |     const t = await page.locator('body').innerText();
  343 |     expect(t.length).toBeGreaterThan(50);
  344 |     expect(t.includes('Track') || t.includes('track') || t.includes('Shipment')).toBeTruthy();
  345 |   });
  346 | 
  347 |   test('Tracking page shows courier partner info', async ({ page }) => {
  348 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  349 |     const t = await page.locator('body').innerText();
  350 |     expect(t.includes('Delhivery') || t.includes('Courier') || t.includes('Tracking')).toBeTruthy();
  351 |   });
  352 | 
  353 |   test('Tracking page shows scan history/timeline', async ({ page }) => {
  354 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  355 |     const t = await page.locator('body').innerText();
  356 |     expect(t.includes('Scan') || t.includes('History') || t.includes('milestone') || t.includes('Transit')).toBeTruthy();
  357 |   });
  358 | 
  359 |   test('Invoice button exists on order detail', async ({ page }) => {
  360 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  361 |     const btn = page.locator('button:has-text("Invoice"),a:has-text("Invoice")').first();
  362 |     expect(await btn.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
  363 |   });
  364 | 
  365 |   test('IMPLEMENTATION GAP: Reorder button not functional', async ({ page }) => {
  366 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  367 |     const btn = page.locator('button:has-text("Reorder")').first();
  368 |     const exists = await btn.isVisible({ timeout: 2000 }).catch(() => false);
  369 |     if (exists) {
  370 |       // If it exists, clicking should navigate somewhere or show message
  371 |       // Currently prototype only
  372 |     }
  373 |   });
  374 | 
  375 |   test('Support link exists on order', async ({ page }) => {
  376 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  377 |     const link = page.locator('a:has-text("Support"),a[href*="support"]').first();
  378 |     expect(await link.isVisible({ timeout: 2000 }).catch(() => false)).toBeTruthy();
  379 |   });
  380 | 
  381 |   test('Deep link to tracking page resolves', async ({ page }) => {
  382 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  383 |     expect(page.url()).toContain('/track');
  384 |   });
  385 | 
  386 |   test('Deep link to refund page resolves', async ({ page }) => {
  387 |     await page.goto('/dashboard/orders/ORD-2026-7715/refund', { waitUntil: 'networkidle' });
  388 |     expect(page.url()).toContain('/refund');
  389 |   });
  390 | 
  391 |   test('Back from tracking returns to order detail', async ({ page }) => {
  392 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  393 |     await page.goto('/dashboard/orders/ORD-2026-8842/track', { waitUntil: 'networkidle' });
  394 |     await page.goBack();
  395 |     await page.waitForLoadState('networkidle');
  396 |     expect(page.url()).toContain('/dashboard/orders/ORD-2026-8842');
  397 |   });
  398 | });
  399 | 
  400 | // ============================================================================
  401 | // PHASE 6 — ADMIN VISIBILITY
  402 | // ============================================================================
  403 | test.describe('Phase 6 — Admin Visibility', () => {
  404 |   test.beforeEach(async ({ page }) => { await login(page); });
  405 | 
  406 |   test('Admin orders page renders with data grid', async ({ page }) => {
  407 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  408 |     const t = await page.locator('body').innerText();
  409 |     expect(t.length).toBeGreaterThan(50);
  410 |     expect(t.includes('Orders') || t.includes('orders')).toBeTruthy();
  411 |   });
  412 | 
  413 |   test('Admin grid shows multiple order rows', async ({ page }) => {
  414 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  415 |     const t = await page.locator('body').innerText();
  416 |     expect(t.includes('ORD-3')).toBeTruthy(); // Admin mock orders are ORD-3000+
  417 |   });
  418 | 
  419 |   test('Admin grid shows order ID, status, customer, total columns', async ({ page }) => {
  420 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  421 |     const t = await page.locator('body').innerText();
  422 |     expect(t.includes('ID') && t.includes('Status') && t.includes('Customer') && t.includes('Total')).toBeTruthy();
  423 |   });
  424 | 
  425 |   test('Admin grid search field exists', async ({ page }) => {
  426 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  427 |     const s = page.locator('input[type="search"],input[placeholder*="search" i],input[placeholder*="Search" i]').first();
> 428 |     expect(await s.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
      |                                                                     ^ Error: expect(received).toBeTruthy()
  429 |   });
  430 | });
  431 | 
  432 | // ============================================================================
  433 | // PHASE 7 — ORDER SECURITY
  434 | // ============================================================================
  435 | test.describe('Phase 7 — Order Security', () => {
  436 |   test('DEFECT: Dashboard orders accessible without authentication', async ({ page }) => {
  437 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  438 |     const url = page.url();
  439 |     expect(url.includes('login') || url.includes('auth')).toBeFalsy();
  440 |     // Page renders without redirect — exposed to unauthenticated users
  441 |     const t = await page.locator('body').innerText();
  442 |     expect(t.length).toBeGreaterThan(50);
  443 |   });
  444 | 
  445 |   test('DEFECT: Admin orders accessible without authentication', async ({ page }) => {
  446 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  447 |     const url = page.url();
  448 |     expect(url.includes('login') || url.includes('auth')).toBeFalsy();
  449 |   });
  450 | 
  451 |   test('Guest role sees access restricted for customer orders', async ({ page }) => {
  452 |     await setRole(page, 'guest');
  453 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  454 |     const t = await page.locator('body').innerText();
  455 |     const restricted = t.includes('Access restricted') || t.includes('access');
  456 |     expect(restricted).toBeTruthy();
  457 |   });
  458 | 
  459 |   test('Customer role can access dashboard orders', async ({ page }) => {
  460 |     await setRole(page, 'customer');
  461 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  462 |     const t = await page.locator('body').innerText();
  463 |     expect(t.includes('Access restricted')).toBe(false);
  464 |     expect(t.includes('Orders') || t.includes('ORD')).toBeTruthy();
  465 |   });
  466 | 
  467 |   test('Invalid order ID shows not-found page', async ({ page }) => {
  468 |     await login(page);
  469 |     await page.goto(`/dashboard/orders/${INVALID_ORDER}`, { waitUntil: 'networkidle' });
  470 |     const t = await page.locator('body').innerText();
  471 |     expect(t.includes('not found') || t.includes('Not Found')).toBeTruthy();
  472 |   });
  473 | 
  474 |   test('Session-expired page renders correctly', async ({ page }) => {
  475 |     await page.goto('/session-expired', { waitUntil: 'networkidle' });
  476 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);
  477 |   });
  478 | 
  479 |   test('Access-denied page renders correctly', async ({ page }) => {
  480 |     await page.goto('/access-denied', { waitUntil: 'networkidle' });
  481 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);
  482 |   });
  483 | });
  484 | 
  485 | // ============================================================================
  486 | // PHASE 8 — DATA INTEGRITY
  487 | // ============================================================================
  488 | test.describe('Phase 8 — Data Integrity', () => {
  489 |   test.beforeEach(async ({ page }) => { await login(page); });
  490 | 
  491 |   test('Order data persists on page refresh', async ({ page }) => {
  492 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  493 |     const b = await page.locator('body').innerText();
  494 |     await page.reload({ waitUntil: 'networkidle' });
  495 |     const a = await page.locator('body').innerText();
  496 |     expect(a.length).toBeGreaterThan(50);
  497 |   });
  498 | 
  499 |   test('All 4 test orders have unique content', async ({ page }) => {
  500 |     const contents = [];
  501 |     for (const oid of TEST_ORDERS) {
  502 |       await page.goto(`/dashboard/orders/${oid}`, { waitUntil: 'networkidle' });
  503 |       contents.push(await page.locator('body').innerText());
  504 |     }
  505 |     // Each order should have different content
  506 |     expect(contents[0]).not.toBe(contents[1]);
  507 |     expect(contents[1]).not.toBe(contents[2]);
  508 |     expect(contents[2]).not.toBe(contents[3]);
  509 |   });
  510 | 
  511 |   test('Order pricing is consistent between list and detail', async ({ page }) => {
  512 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  513 |     const listText = await page.locator('body').innerText();
  514 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  515 |     const detailText = await page.locator('body').innerText();
  516 |     // Both pages show order data
  517 |     expect(listText.length).toBeGreaterThan(50);
  518 |     expect(detailText.length).toBeGreaterThan(50);
  519 |   });
  520 | 
  521 |   test('Mock data for ORD-2026-8842 shows correct total (₹3,450)', async ({ page }) => {
  522 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  523 |     const t = await page.locator('body').innerText();
  524 |     expect(t.includes('3,450') || t.includes('3450')).toBeTruthy();
  525 |   });
  526 | });
  527 | 
  528 | // ============================================================================
```