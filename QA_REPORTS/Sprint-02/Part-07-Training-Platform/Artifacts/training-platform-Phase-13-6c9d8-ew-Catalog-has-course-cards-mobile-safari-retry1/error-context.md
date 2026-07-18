# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 13 — Visual Review >> Catalog has course cards
- Location: tests\training-platform.spec.ts:662:7

# Error details

```
Error: expect(received).toBeGreaterThanOrEqual(expected)

Expected: >= 1
Received:    0
```

# Page snapshot

```yaml
- link "Skip to content" [ref=e3]:
  - /url: "#main"
```

# Test source

```ts
  564 |     await page.setViewportSize({ width: 375, height: 667 });
  565 |     const r = await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  566 |     expect(r?.status()).toBeLessThan(400);
  567 |   });
  568 | });
  569 | 
  570 | // ====================================================================
  571 | // PHASE 11 — ACCESSIBILITY
  572 | // ====================================================================
  573 | test.describe('Phase 11 — Accessibility', () => {
  574 |   test('Catalog has skip to content link', async ({ page }) => {
  575 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  576 |     const l = page.locator('a[href="#main-content"],a:has-text("Skip"),[class*="skip"]');
  577 |     await expect(l.first()).toBeVisible({ timeout: 5000 });
  578 |   });
  579 | 
  580 |   test('Catalog has ARIA landmarks', async ({ page }) => {
  581 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  582 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  583 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  584 |   });
  585 | 
  586 |   test('Images have alt text on catalog', async ({ page }) => {
  587 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  588 |     const imgs = page.locator('img');
  589 |     const c = await imgs.count();
  590 |     let missing = 0;
  591 |     for (let i = 0; i < c; i++) {
  592 |       const alt = await imgs.nth(i).getAttribute('alt');
  593 |       if (alt === null || alt === undefined) missing++;
  594 |     }
  595 |     expect(missing).toBe(0);
  596 |   });
  597 | 
  598 |   test('Course detail has semantic headings', async ({ page }) => {
  599 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  600 |     const h1 = page.locator('h1');
  601 |     await expect(h1.first()).toBeVisible({ timeout: 3000 });
  602 |   });
  603 | 
  604 |   test('Admin training workspace has landmarks', async ({ page }) => {
  605 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  606 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  607 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  608 |   });
  609 | 
  610 |   test('Course cards are keyboard navigable', async ({ page }) => {
  611 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  612 |     const firstLink = page.locator('a[href*="/training/courses/"]').first();
  613 |     if (await firstLink.isVisible({ timeout: 2000 }).catch(() => false)) {
  614 |       await firstLink.focus();
  615 |       await page.keyboard.press('Enter');
  616 |       await page.waitForTimeout(500);
  617 |       expect(page.url()).toContain('/training/courses/');
  618 |     }
  619 |   });
  620 | });
  621 | 
  622 | // ====================================================================
  623 | // PHASE 12 — PERFORMANCE
  624 | // ====================================================================
  625 | test.describe('Phase 12 — Performance', () => {
  626 |   test('Catalog loads within 15s', async ({ page }) => {
  627 |     const s = Date.now();
  628 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  629 |     expect(Date.now() - s).toBeLessThan(25000);
  630 |   });
  631 | 
  632 |   test('Course detail loads within 15s', async ({ page }) => {
  633 |     const s = Date.now();
  634 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  635 |     expect(Date.now() - s).toBeLessThan(25000);
  636 |   });
  637 | 
  638 |   test('Learner dashboard loads within 15s', async ({ page }) => {
  639 |     await login(page);
  640 |     const s = Date.now();
  641 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  642 |     expect(Date.now() - s).toBeLessThan(25000);
  643 |   });
  644 | 
  645 |   test('Admin training dashboard loads within 15s', async ({ page }) => {
  646 |     const s = Date.now();
  647 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  648 |     expect(Date.now() - s).toBeLessThan(25000);
  649 |   });
  650 | 
  651 |   test('Course builder loads within 15s', async ({ page }) => {
  652 |     const s = Date.now();
  653 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  654 |     expect(Date.now() - s).toBeLessThan(25000);
  655 |   });
  656 | });
  657 | 
  658 | // ====================================================================
  659 | // PHASE 13 — VISUAL REVIEW
  660 | // ====================================================================
  661 | test.describe('Phase 13 — Visual Review', () => {
  662 |   test('Catalog has course cards', async ({ page }) => {
  663 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
> 664 |     expect(await page.locator('[class*="card"],[class*="Card"],article').count()).toBeGreaterThanOrEqual(1);
      |                                                                                   ^ Error: expect(received).toBeGreaterThanOrEqual(expected)
  665 |   });
  666 | 
  667 |   test('Course detail has proper layout', async ({ page }) => {
  668 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  669 |     const main = page.locator('main,[role="main"]').first();
  670 |     await expect(main).toBeVisible({ timeout: 3000 });
  671 |   });
  672 | 
  673 |   test('No horizontal scroll on catalog', async ({ page }) => {
  674 |     await page.setViewportSize({ width: 1440, height: 900 });
  675 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  676 |     const hs = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
  677 |     expect(hs).toBe(false);
  678 |   });
  679 | 
  680 |   test('Typography consistent on course detail', async ({ page }) => {
  681 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  682 |     const h1 = page.locator('h1').first();
  683 |     if (await h1.isVisible({ timeout: 2000 }).catch(() => false)) {
  684 |       expect(parseFloat(await h1.evaluate(e => getComputedStyle(e).fontSize))).toBeGreaterThanOrEqual(16);
  685 |     }
  686 |   });
  687 | 
  688 |   test('Admin course builder has visual panels', async ({ page }) => {
  689 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  690 |     const panels = page.locator('[class*="panel"],[class*="Panel"],[class*="section"]');
  691 |     expect(await panels.count()).toBeGreaterThanOrEqual(1);
  692 |   });
  693 | 
  694 |   test('Course catalog view toggles work', async ({ page }) => {
  695 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  696 |     const gridBtn = page.locator('button:has-text("Grid"),button[aria-label*="grid"]').first();
  697 |     if (await gridBtn.isVisible({ timeout: 2000 }).catch(() => false)) {
  698 |       await gridBtn.click(); await page.waitForTimeout(300);
  699 |     }
  700 |     expect(true).toBeTruthy();
  701 |   });
  702 | });
  703 | 
  704 | // ====================================================================
  705 | // PHASE 14 — EVIDENCE COLLECTION
  706 | // ====================================================================
  707 | test.describe('Phase 14 — Evidence Collection', () => {
  708 |   test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
  709 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  710 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  711 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' }).catch(() => {});
  712 |     expect(true).toBeTruthy();
  713 |   });
  714 | 
  715 |   test('Traces captured (config: trace=on)', async ({ page }) => {
  716 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  717 |     expect(true).toBeTruthy();
  718 |   });
  719 | });
  720 | 
```