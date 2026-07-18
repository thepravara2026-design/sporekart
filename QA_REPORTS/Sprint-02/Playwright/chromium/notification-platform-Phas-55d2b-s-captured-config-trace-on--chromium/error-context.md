# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 15 — Evidence Collection >> Traces captured (config: trace=on)
- Location: tests\notification-platform.spec.ts:672:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin/training/communication/notifications
Call log:
  - navigating to "http://localhost:5173/admin/training/communication/notifications", waiting until "networkidle"

```

# Test source

```ts
  573 | 
  574 |   test('Close buttons have aria-label', async ({ page }) => {
  575 |     await page.goto('/admin', { waitUntil: 'networkidle' });
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
> 673 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin/training/communication/notifications
  674 |     await expect(page.locator('body')).toBeVisible();
  675 |   });
  676 | });
  677 | 
```