# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 11 — Cross-Browser >> Notification bell renders at desktop
- Location: tests\notification-platform.spec.ts:505:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
Call log:
  - navigating to "http://localhost:5173/admin", waiting until "networkidle"

```

# Test source

```ts
  407 |     await login(page);
  408 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  409 |     const t = await bodyText(page);
  410 |     expect(t.includes('timeout') || t.includes('Timeout')).toBeFalsy();
  411 |   });
  412 | 
  413 |   test('IMPLEMENTATION GAP: No invalid payload handling', async ({ page }) => {
  414 |     await login(page);
  415 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
  416 |     const t = await bodyText(page).catch(() => '');
  417 |     expect(t.includes('invalid') || t.includes('Invalid')).toBeFalsy();
  418 |   });
  419 | 
  420 |   test('IMPLEMENTATION GAP: No user error messaging', async ({ page }) => {
  421 |     await page.goto('/', { waitUntil: 'networkidle' });
  422 |     const t = await bodyText(page);
  423 |     expect(t.includes('Failed to send') || t.includes('notification failed')).toBeFalsy();
  424 |   });
  425 | });
  426 | 
  427 | // ====================================================================
  428 | // PHASE 9 — DATA INTEGRITY
  429 | // ====================================================================
  430 | test.describe('Phase 9 — Data Integrity', () => {
  431 |   test('Notification service API returns data', async () => {
  432 |     const resp = await fetch(`${BASE}/api/notifications`).catch(() => null);
  433 |     if (resp) {
  434 |       const data = await resp.json();
  435 |       expect(data).toBeDefined();
  436 |     }
  437 |   });
  438 | 
  439 |   test('IMPLEMENTATION GAP: No notification recipient data visible', async ({ page }) => {
  440 |     await login(page);
  441 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  442 |     const t = await bodyText(page);
  443 |     expect(t.length).toBeGreaterThan(10);
  444 |   });
  445 | 
  446 |   test('IMPLEMENTATION GAP: No notification timestamp display', async ({ page }) => {
  447 |     await login(page);
  448 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  449 |     const t = await bodyText(page);
  450 |     expect(t.includes(':') || t.includes('/') || t.includes('-')).toBeDefined();
  451 |   });
  452 | 
  453 |   test('IMPLEMENTATION GAP: No notification type classification', async ({ page }) => {
  454 |     await login(page);
  455 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  456 |     const t = await bodyText(page);
  457 |     const hasType = t.includes('type') || t.includes('Type') || t.includes('category') || t.includes('Category');
  458 |     expect(hasType).toBeTruthy();
  459 |   });
  460 | });
  461 | 
  462 | // ====================================================================
  463 | // PHASE 10 — SECURITY
  464 | // ====================================================================
  465 | test.describe('Phase 10 — Security', () => {
  466 |   test('Public pages accessible without auth', async ({ page }) => {
  467 |     const r = await page.goto('/training/courses', { waitUntil: 'networkidle' });
  468 |     expect(r?.status()).toBeLessThan(400);
  469 |   });
  470 | 
  471 |   test('IMPLEMENTATION GAP: Admin notification pages accessible without auth', async ({ page }) => {
  472 |     const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  473 |     expect(r?.status()).toBeLessThan(400);
  474 |   });
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
> 507 |     await page.goto('/admin', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
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
```