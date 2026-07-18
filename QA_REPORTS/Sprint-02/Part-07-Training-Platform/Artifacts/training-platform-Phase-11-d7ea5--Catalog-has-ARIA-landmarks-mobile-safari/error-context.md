# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 11 — Accessibility >> Catalog has ARIA landmarks
- Location: tests\training-platform.spec.ts:580:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('main,[role="main"]').first()
Expected: visible
Timeout: 5000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('main,[role="main"]').first()

```

```yaml
- link "Skip to content":
  - /url: "#main"
```

# Test source

```ts
  482 |     const n1 = await page.locator('nav').first().innerText().catch(() => '');
  483 |     await page.goto('/admin/training/courses', { waitUntil: 'networkidle' });
  484 |     const n2 = await page.locator('nav').first().innerText().catch(() => '');
  485 |     expect(n1.length > 0 || n2.length > 0).toBeTruthy();
  486 |   });
  487 | });
  488 | 
  489 | // ====================================================================
  490 | // PHASE 9 — SECURITY
  491 | // ====================================================================
  492 | test.describe('Phase 9 — Security', () => {
  493 |   test('Public catalog accessible without auth', async ({ page }) => {
  494 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  495 |     const t = await bodyText(page);
  496 |     expect(t.length).toBeGreaterThan(0);
  497 |   });
  498 | 
  499 |   test('Learner dashboard requires auth', async ({ page }) => {
  500 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  501 |     // Should redirect to login or show login page
  502 |     const t = await bodyText(page);
  503 |     const isLoggedIn = t.includes('Enrolled') || t.includes('Training') || t.includes('Progress');
  504 |     const isLoginPage = page.url().includes('login');
  505 |     expect(isLoggedIn || isLoginPage).toBeTruthy();
  506 |   });
  507 | 
  508 |   test('No production secrets in training pages', async ({ page }) => {
  509 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  510 |     const html = await page.locator('html').innerHTML();
  511 |     for (const s of ['sk_live_', 'pk_live_', 'rzp_live_']) {
  512 |       expect(html.includes(s)).toBe(false);
  513 |     }
  514 |   });
  515 | 
  516 |   test('Admin training workspace accessible (no auth guard)', async ({ page }) => {
  517 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  518 |     expect(page.url()).toContain('/admin/training/dashboard');
  519 |   });
  520 | 
  521 |   test('No console errors on training pages', async ({ page }) => {
  522 |     const errs = [];
  523 |     page.on('console', m => { if (m.type() === 'error') errs.push(m.text()); });
  524 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  525 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  526 |     if (errs.length > 0) console.log('Console errors:', errs);
  527 |     expect(true).toBeTruthy(); // Non-blocking
  528 |   });
  529 | });
  530 | 
  531 | // ====================================================================
  532 | // PHASE 10 — CROSS-BROWSER
  533 | // ====================================================================
  534 | test.describe('Phase 10 — Cross-Browser', () => {
  535 |   test('Catalog renders at desktop', async ({ page }) => {
  536 |     await page.setViewportSize({ width: 1440, height: 900 });
  537 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  538 |     expect((await bodyText(page)).length).toBeGreaterThan(20);
  539 |   });
  540 | 
  541 |   test('Catalog renders at mobile', async ({ page }) => {
  542 |     await page.setViewportSize({ width: 375, height: 667 });
  543 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  544 |     expect((await bodyText(page)).length).toBeGreaterThan(20);
  545 |   });
  546 | 
  547 |   test('Course detail renders at all viewports', async ({ page }) => {
  548 |     for (const vp of [{ w: 1440, h: 900 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  549 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  550 |       await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  551 |       expect((await bodyText(page)).length).toBeGreaterThan(20);
  552 |     }
  553 |   });
  554 | 
  555 |   test('Learner dashboard renders at desktop', async ({ page }) => {
  556 |     await login(page);
  557 |     await page.setViewportSize({ width: 1440, height: 900 });
  558 |     const r = await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  559 |     expect(r?.status()).toBeLessThan(400);
  560 |   });
  561 | 
  562 |   test('Learner dashboard renders at mobile', async ({ page }) => {
  563 |     await login(page);
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
> 582 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
      |                                                              ^ Error: expect(locator).toBeVisible() failed
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
  664 |     expect(await page.locator('[class*="card"],[class*="Card"],article').count()).toBeGreaterThanOrEqual(1);
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
```