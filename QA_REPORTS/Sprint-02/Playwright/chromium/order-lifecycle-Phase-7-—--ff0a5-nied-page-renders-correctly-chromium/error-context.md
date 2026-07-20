# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 7 — Order Security >> Access-denied page renders correctly
- Location: tests\order-lifecycle.spec.ts:479:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/access-denied
Call log:
  - navigating to "http://localhost:5173/access-denied", waiting until "networkidle"

```

# Test source

```ts
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
  428 |     expect(await s.isVisible({ timeout: 3000 }).catch(() => false)).toBeTruthy();
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
> 480 |     await page.goto('/access-denied', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/access-denied
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
  529 | // PHASE 9 — CROSS-BROWSER
  530 | // ============================================================================
  531 | test.describe('Phase 9 — Cross-Browser', () => {
  532 |   test('Orders dashboard renders at desktop viewport', async ({ page }) => {
  533 |     await login(page);
  534 |     await page.setViewportSize({ width: 1440, height: 900 });
  535 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  536 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  537 |   });
  538 | 
  539 |   test('Orders dashboard renders at tablet viewport', async ({ page }) => {
  540 |     await login(page);
  541 |     await page.setViewportSize({ width: 768, height: 1024 });
  542 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  543 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  544 |   });
  545 | 
  546 |   test('Orders dashboard renders at mobile viewport', async ({ page }) => {
  547 |     await login(page);
  548 |     await page.setViewportSize({ width: 375, height: 667 });
  549 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  550 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  551 |   });
  552 | 
  553 |   test('Order detail renders at all viewports', async ({ page }) => {
  554 |     await login(page);
  555 |     for (const vp of [{ w: 1440, h: 900 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  556 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  557 |       await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  558 |       expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  559 |     }
  560 |   });
  561 | 
  562 |   test('Admin orders renders at desktop viewport', async ({ page }) => {
  563 |     await login(page);
  564 |     await page.setViewportSize({ width: 1440, height: 900 });
  565 |     await page.goto('/admin/orders', { waitUntil: 'networkidle' });
  566 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
  567 |   });
  568 | });
  569 | 
  570 | // ============================================================================
  571 | // PHASE 10 — ACCESSIBILITY
  572 | // ============================================================================
  573 | test.describe('Phase 10 — Accessibility', () => {
  574 |   test.beforeEach(async ({ page }) => { await login(page); });
  575 | 
  576 |   test('Skip to content link exists', async ({ page }) => {
  577 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  578 |     const l = page.locator('a[href="#main-content"],a[href="#content"],a[href="#main"],a:has-text("Skip"),[class*="skip"]');
  579 |     await expect(l.first()).toBeVisible({ timeout: 5000 });
  580 |   });
```