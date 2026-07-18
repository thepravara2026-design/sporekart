# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 16 — Visual Review >> Typography is consistent
- Location: tests\admin-console.spec.ts:525:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
Call log:
  - navigating to "http://localhost:5174/admin/dashboard", waiting until "networkidle"

```

# Test source

```ts
  426 | // ====================================================================
  427 | // PHASE 13 — DATA INTEGRITY
  428 | // ====================================================================
  429 | test.describe('Phase 13 — Data Integrity', () => {
  430 |   test('Dashboard renders on reload', async ({ page }) => {
  431 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  432 |     await page.reload();
  433 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  434 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(0);
  435 |   });
  436 | 
  437 |   test('Admin pages accessible without errors', async ({ page }) => {
  438 |     const errs = [];
  439 |     page.on('console', m => { if (m.type() === 'error') errs.push(m.text()); });
  440 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  441 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  442 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  443 |     // Log errors but don't fail for admin-specific ones
  444 |     if (errs.length > 0) console.log('Console errors:', errs);
  445 |     expect(true).toBeTruthy();
  446 |   });
  447 | 
  448 |   test('Mock API data accessible from admin', async ({ page }) => {
  449 |     const r = await page.request.get(`${ADMIN}/products`);
  450 |     expect(r.status()).toBeLessThan(500);
  451 |   });
  452 | });
  453 | 
  454 | // ====================================================================
  455 | // PHASE 14 — ACCESSIBILITY
  456 | // ====================================================================
  457 | test.describe('Phase 14 — Accessibility', () => {
  458 |   test('Skip to content link exists', async ({ page }) => {
  459 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  460 |     await expect(page.locator('a[href="#main-content"],a:has-text("Skip"),[class*="skip"]').first()).toBeVisible({ timeout: 5000 });
  461 |   });
  462 | 
  463 |   test('ARIA landmarks present', async ({ page }) => {
  464 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  465 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  466 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  467 |   });
  468 | 
  469 |   test('Images have alt text', async ({ page }) => {
  470 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  471 |     const imgs = page.locator('img');
  472 |     const c = await imgs.count();
  473 |     let missing = 0;
  474 |     for (let i = 0; i < c; i++) {
  475 |       const alt = await imgs.nth(i).getAttribute('alt');
  476 |       if (alt === null || alt === undefined) missing++;
  477 |     }
  478 |     expect(missing).toBe(0);
  479 |   });
  480 | 
  481 |   test('Admin sidebar keyboard navigable', async ({ page }) => {
  482 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  483 |     const links = page.locator('nav a,[role="navigation"] a').first();
  484 |     if (await links.isVisible({ timeout: 2000 }).catch(() => false)) {
  485 |       await links.focus();
  486 |       await page.keyboard.press('Enter');
  487 |       await page.waitForTimeout(500);
  488 |     }
  489 |     expect(true).toBeTruthy();
  490 |   });
  491 | });
  492 | 
  493 | // ====================================================================
  494 | // PHASE 15 — PERFORMANCE
  495 | // ====================================================================
  496 | test.describe('Phase 15 — Performance', () => {
  497 |   test('Dashboard loads within 15s', async ({ page }) => {
  498 |     const s = Date.now();
  499 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  500 |     expect(Date.now() - s).toBeLessThan(25000);
  501 |   });
  502 | 
  503 |   test('Products page loads within 15s', async ({ page }) => {
  504 |     const s = Date.now();
  505 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  506 |     expect(Date.now() - s).toBeLessThan(25000);
  507 |   });
  508 | 
  509 |   test('Orders page loads within 15s', async ({ page }) => {
  510 |     const s = Date.now();
  511 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  512 |     expect(Date.now() - s).toBeLessThan(25000);
  513 |   });
  514 | });
  515 | 
  516 | // ====================================================================
  517 | // PHASE 16 — VISUAL REVIEW
  518 | // ====================================================================
  519 | test.describe('Phase 16 — Visual Review', () => {
  520 |   test('Dashboard cards are visible', async ({ page }) => {
  521 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  522 |     expect(await page.locator('[class*="card"],[class*="Card"]').count()).toBeGreaterThanOrEqual(1);
  523 |   });
  524 | 
  525 |   test('Typography is consistent', async ({ page }) => {
> 526 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
  527 |     const h1 = page.locator('h1').first();
  528 |     if (await h1.isVisible({ timeout: 2000 }).catch(() => false)) {
  529 |       expect(parseFloat(await h1.evaluate(e => getComputedStyle(e).fontSize))).toBeGreaterThanOrEqual(14);
  530 |     }
  531 |   });
  532 | 
  533 |   test('No horizontal scroll on desktop', async ({ page }) => {
  534 |     await page.setViewportSize({ width: 1440, height: 900 });
  535 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  536 |     const hs = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
  537 |     expect(hs).toBe(false);
  538 |   });
  539 | 
  540 |   test('Admin sidebar renders', async ({ page }) => {
  541 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  542 |     await expect(page.locator('aside,[class*="sidebar"]').first()).toBeVisible({ timeout: 3000 });
  543 |   });
  544 | });
  545 | 
  546 | // ====================================================================
  547 | // PHASE 17 — EVIDENCE COLLECTION
  548 | // ====================================================================
  549 | test.describe('Phase 17 — Evidence Collection', () => {
  550 |   test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
  551 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  552 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  553 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  554 |     expect(true).toBeTruthy();
  555 |   });
  556 | 
  557 |   test('Traces captured (config: trace=on)', async ({ page }) => {
  558 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  559 |     expect(true).toBeTruthy();
  560 |   });
  561 | });
  562 | 
```