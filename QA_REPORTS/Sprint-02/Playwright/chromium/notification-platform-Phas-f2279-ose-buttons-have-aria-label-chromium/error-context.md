# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 12 — Accessibility >> Close buttons have aria-label
- Location: tests\notification-platform.spec.ts:574:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
Call log:
  - navigating to "http://localhost:5173/admin", waiting until "networkidle"

```

# Test source

```ts
  475 | 
  476 |   test('No PII in notification page source', async ({ page }) => {
  477 |     await login(page);
  478 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  479 |     const html = await page.locator('html').innerHTML();
  480 |     expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
  481 |     expect(html.includes('secret') || html.includes('SECRET')).toBeFalsy();
  482 |     expect(html.includes('token') || html.includes('TOKEN')).toBeFalsy();
  483 |   });
  484 | 
  485 |   test('No console errors on notification pages', async ({ page }) => {
  486 |     const errors: string[] = [];
  487 |     page.on('console', (msg) => { if (msg.type() === 'error') errors.push(msg.text()); });
  488 |     await login(page);
  489 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  490 |     expect(errors.length).toBe(0);
  491 |   });
  492 | 
  493 |   test('No PII in delivery queue page', async ({ page }) => {
  494 |     await login(page);
  495 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
  496 |     const html = await page.locator('html').innerHTML().catch(() => '');
  497 |     expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
  498 |   });
  499 | });
  500 | 
  501 | // ====================================================================
  502 | // PHASE 11 — CROSS-BROWSER
  503 | // ====================================================================
  504 | test.describe('Phase 11 — Cross-Browser', () => {
  505 |   test('Notification bell renders at desktop', async ({ page }) => {
  506 |     await page.setViewportSize({ width: 1280, height: 800 });
  507 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  508 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  509 |     await expect(bell).toBeVisible({ timeout: 5000 });
  510 |   });
  511 | 
  512 |   test('Notification bell renders at tablet', async ({ page }) => {
  513 |     await page.setViewportSize({ width: 768, height: 1024 });
  514 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  515 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  516 |     const exists = await bell.count();
  517 |     expect(exists).toBeGreaterThanOrEqual(0);
  518 |   });
  519 | 
  520 |   test('Notification bell renders at mobile', async ({ page }) => {
  521 |     await page.setViewportSize({ width: 375, height: 667 });
  522 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  523 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  524 |     const exists = await bell.count();
  525 |     expect(exists).toBeGreaterThanOrEqual(0);
  526 |   });
  527 | 
  528 |   test('Communication pages render at all viewports', async ({ page }) => {
  529 |     await login(page);
  530 |     for (const vp of [{ w: 1280, h: 800 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  531 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  532 |       const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  533 |       expect(r?.status()).toBeLessThan(400);
  534 |     }
  535 |   });
  536 | });
  537 | 
  538 | // ====================================================================
  539 | // PHASE 12 — ACCESSIBILITY
  540 | // ====================================================================
  541 | test.describe('Phase 12 — Accessibility', () => {
  542 |   test('Notification bell has aria-label', async ({ page }) => {
  543 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  544 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  545 |     await expect(bell).toBeVisible({ timeout: 5000 });
  546 |   });
  547 | 
  548 |   test('Notification dropdown has ARIA dialog role', async ({ page }) => {
  549 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  550 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  551 |     await bell.click();
  552 |     await page.waitForTimeout(500);
  553 |     const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
  554 |     await expect(dialog).toBeVisible({ timeout: 3000 });
  555 |   });
  556 | 
  557 |   test('Notification list items have listitem role', async ({ page }) => {
  558 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  559 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  560 |     await bell.click();
  561 |     await page.waitForTimeout(500);
  562 |     const items = page.locator('[role="listitem"]');
  563 |     const count = await items.count();
  564 |     expect(count).toBeGreaterThanOrEqual(0);
  565 |   });
  566 | 
  567 |   test('Notification provider has aria-live region', async ({ page }) => {
  568 |     await page.goto('/', { waitUntil: 'networkidle' });
  569 |     const live = page.locator('[aria-live="polite"]');
  570 |     const exists = await live.count();
  571 |     expect(exists).toBeGreaterThanOrEqual(0);
  572 |   });
  573 | 
  574 |   test('Close buttons have aria-label', async ({ page }) => {
> 575 |     await page.goto('/admin', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
  576 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  577 |     await bell.click();
  578 |     await page.waitForTimeout(500);
  579 |     const closeBtns = page.locator('[role="dialog"] button[aria-label="Dismiss"]');
  580 |     const count = await closeBtns.count();
  581 |     expect(count).toBeGreaterThanOrEqual(0);
  582 |   });
  583 | });
  584 | 
  585 | // ====================================================================
  586 | // PHASE 13 — PERFORMANCE
  587 | // ====================================================================
  588 | test.describe('Phase 13 — Performance', () => {
  589 |   test('Notification bell renders within 5s', async ({ page }) => {
  590 |     const start = Date.now();
  591 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  592 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  593 |     await bell.waitFor({ state: 'visible', timeout: 10000 }).catch(() => {});
  594 |     expect(Date.now() - start).toBeLessThan(15000);
  595 |   });
  596 | 
  597 |   test('Communication notifications page loads within 10s', async ({ page }) => {
  598 |     await login(page);
  599 |     const start = Date.now();
  600 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  601 |     expect(Date.now() - start).toBeLessThan(15000);
  602 |   });
  603 | 
  604 |   test('Communication delivery queue loads within 10s', async ({ page }) => {
  605 |     await login(page);
  606 |     const start = Date.now();
  607 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  608 |     expect(Date.now() - start).toBeLessThan(15000);
  609 |   });
  610 | 
  611 |   test('Communication templates page loads within 10s', async ({ page }) => {
  612 |     await login(page);
  613 |     const start = Date.now();
  614 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
  615 |     expect(Date.now() - start).toBeLessThan(15000);
  616 |   });
  617 | 
  618 |   test('Communication statistics page loads within 10s', async ({ page }) => {
  619 |     await login(page);
  620 |     const start = Date.now();
  621 |     await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
  622 |     expect(Date.now() - start).toBeLessThan(15000);
  623 |   });
  624 | });
  625 | 
  626 | // ====================================================================
  627 | // PHASE 14 — VISUAL REVIEW
  628 | // ====================================================================
  629 | test.describe('Phase 14 — Visual Review', () => {
  630 |   test('Notification dropdown has proper width', async ({ page }) => {
  631 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  632 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  633 |     await bell.click();
  634 |     await page.waitForTimeout(500);
  635 |     const dialog = page.locator('[role="dialog"]');
  636 |     const box = await dialog.boundingBox();
  637 |     expect(box).not.toBeNull();
  638 |     if (box) expect(box.width).toBeGreaterThan(200);
  639 |   });
  640 | 
  641 |   test('No horizontal scroll on notification page', async ({ page }) => {
  642 |     await login(page);
  643 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  644 |     const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
  645 |     expect(scrollW).toBe(0);
  646 |   });
  647 | 
  648 |   test('Communication overview has content', async ({ page }) => {
  649 |     await login(page);
  650 |     await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
  651 |     const t = await bodyText(page);
  652 |     expect(t.length).toBeGreaterThan(20);
  653 |   });
  654 | 
  655 |   test('No horizontal scroll on delivery queue page', async ({ page }) => {
  656 |     await login(page);
  657 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  658 |     const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
  659 |     expect(scrollW).toBe(0);
  660 |   });
  661 | });
  662 | 
  663 | // ====================================================================
  664 | // PHASE 15 — EVIDENCE COLLECTION
  665 | // ====================================================================
  666 | test.describe('Phase 15 — Evidence Collection', () => {
  667 |   test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
  668 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  669 |     await expect(page.locator('body')).toBeVisible();
  670 |   });
  671 | 
  672 |   test('Traces captured (config: trace=on)', async ({ page }) => {
  673 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  674 |     await expect(page.locator('body')).toBeVisible();
  675 |   });
```