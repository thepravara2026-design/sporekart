# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: order-lifecycle.spec.ts >> Phase 9 — Cross-Browser >> Admin orders renders at desktop viewport
- Location: tests\order-lifecycle.spec.ts:562:7

# Error details

```
Error: expect(received).toBeGreaterThan(expected)

Expected: > 50
Received:   0
```

# Test source

```ts
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
> 566 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);
      |                                                             ^ Error: expect(received).toBeGreaterThan(expected)
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
  581 | 
  582 |   test('ARIA landmarks present on orders dashboard', async ({ page }) => {
  583 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  584 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  585 |     await expect(page.locator('footer,[role="contentinfo"]').first()).toBeVisible({ timeout: 5000 });
  586 |   });
  587 | 
  588 |   test('Images have alt text on orders dashboard', async ({ page }) => {
  589 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  590 |     const imgs = page.locator('img');
  591 |     const c = await imgs.count();
  592 |     let missing = 0;
  593 |     for (let i = 0; i < c; i++) {
  594 |       const alt = await imgs.nth(i).getAttribute('alt');
  595 |       if (alt === null || alt === undefined) missing++;
  596 |     }
  597 |     expect(missing).toBe(0);
  598 |   });
  599 | 
  600 |   test('Orders dashboard heading is descriptive', async ({ page }) => {
  601 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  602 |     const h1 = page.locator('h1');
  603 |     expect(await h1.isVisible()).toBeTruthy();
  604 |     const text = await h1.innerText();
  605 |     expect(text.length).toBeGreaterThan(0);
  606 |   });
  607 | 
  608 |   test('Tab order is preserved on orders page', async ({ page }) => {
  609 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  610 |     const focusable = page.locator('button, a, input, select, textarea, [tabindex]:not([tabindex="-1"])');
  611 |     const count = await focusable.count();
  612 |     expect(count).toBeGreaterThan(0);
  613 |   });
  614 | });
  615 | 
  616 | // ============================================================================
  617 | // PHASE 11 — PERFORMANCE
  618 | // ============================================================================
  619 | test.describe('Phase 11 — Performance', () => {
  620 |   test('Order history loads within 15s', async ({ page }) => {
  621 |     const start = Date.now();
  622 |     await login(page);
  623 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  624 |     expect(Date.now() - start).toBeLessThan(20000);
  625 |   });
  626 | 
  627 |   test('Order detail loads within 15s', async ({ page }) => {
  628 |     const start = Date.now();
  629 |     await login(page);
  630 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  631 |     expect(Date.now() - start).toBeLessThan(20000);
  632 |   });
  633 | 
  634 |   test('No console errors on orders dashboard', async ({ page }) => {
  635 |     const errors = [];
  636 |     page.on('console', m => { if (m.type() === 'error') errors.push(m.text()); });
  637 |     await login(page);
  638 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  639 |     expect(errors.length).toBe(0);
  640 |   });
  641 | 
  642 |   test('No console errors on order detail', async ({ page }) => {
  643 |     const errors = [];
  644 |     page.on('console', m => { if (m.type() === 'error') errors.push(m.text()); });
  645 |     await login(page);
  646 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  647 |     expect(errors.length).toBe(0);
  648 |   });
  649 | 
  650 |   test('No failed network requests on orders', async ({ page }) => {
  651 |     const fails = [];
  652 |     page.on('requestfailed', r => fails.push(r.url()));
  653 |     await login(page);
  654 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  655 |     expect(fails.length).toBe(0);
  656 |   });
  657 | 
  658 |   test('Repeated navigation is performant', async ({ page }) => {
  659 |     await login(page);
  660 |     for (let i = 0; i < 3; i++) {
  661 |       const s = Date.now();
  662 |       await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  663 |       await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  664 |       expect(Date.now() - s).toBeLessThan(25000);
  665 |     }
  666 |   });
```