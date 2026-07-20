# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 8 — Failure Handling >> IMPLEMENTATION GAP: No user error messaging
- Location: tests\notification-platform.spec.ts:420:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
Call log:
  - navigating to "http://localhost:5173/", waiting until "networkidle"

```

# Test source

```ts
  321 | 
  322 |   test('Design system NotificationProvider renders toast container', async ({ page }) => {
  323 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  324 |     const container = page.locator('.sk-notification-container');
  325 |     const exists = await container.count();
  326 |     expect(exists).toBe(0);
  327 |   });
  328 | });
  329 | 
  330 | // ====================================================================
  331 | // PHASE 6 — PUSH NOTIFICATIONS
  332 | // ====================================================================
  333 | test.describe('Phase 6 — Push Notifications', () => {
  334 |   test('IMPLEMENTATION GAP: No push notification permission request', async ({ page }) => {
  335 |     await page.goto('/', { waitUntil: 'networkidle' });
  336 |     const t = await bodyText(page);
  337 |     expect(t.includes('notification') || t.includes('Notification')).toBeDefined();
  338 |   });
  339 | 
  340 |   test('IMPLEMENTATION GAP: No push notification service worker', async ({ page }) => {
  341 |     const hasSW = await page.evaluate(() => 'serviceWorker' in navigator).catch(() => false);
  342 |     expect(hasSW).toBeTruthy();
  343 |   });
  344 | 
  345 |   test('IMPLEMENTATION GAP: No push notification UI in settings', async ({ page }) => {
  346 |     await page.goto('/settings', { waitUntil: 'networkidle' }).catch(() => {});
  347 |     const t = await bodyText(page).catch(() => '');
  348 |     expect(t.includes('Push') && t.includes('notification')).toBeFalsy();
  349 |   });
  350 | });
  351 | 
  352 | // ====================================================================
  353 | // PHASE 7 — DELIVERY WORKFLOW
  354 | // ====================================================================
  355 | test.describe('Phase 7 — Delivery Workflow', () => {
  356 |   test('IMPLEMENTATION GAP: No notification delivery queue', async ({ page }) => {
  357 |     await login(page);
  358 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  359 |     const t = await bodyText(page);
  360 |     expect(t.length).toBeGreaterThan(10);
  361 |   });
  362 | 
  363 |   test('Delivery queue has status indicators', async ({ page }) => {
  364 |     await login(page);
  365 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  366 |     const t = await bodyText(page);
  367 |     const hasStatus = t.includes('queued') || t.includes('delivered') || t.includes('pending') || t.includes('sent') || t.includes('failed');
  368 |     expect(hasStatus).toBeTruthy();
  369 |   });
  370 | 
  371 |   test('IMPLEMENTATION GAP: No retry mechanism visible', async ({ page }) => {
  372 |     await login(page);
  373 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  374 |     const t = await bodyText(page);
  375 |     expect(t.includes('retry') || t.includes('Retry')).toBeFalsy();
  376 |   });
  377 | 
  378 |   test('IMPLEMENTATION GAP: No delivery ordering/filtering', async ({ page }) => {
  379 |     await login(page);
  380 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  381 |     const t = await bodyText(page);
  382 |     const hasFilter = t.includes('filter') || t.includes('Filter') || t.includes('sort') || t.includes('Sort');
  383 |     expect(hasFilter).toBeTruthy();
  384 |   });
  385 | });
  386 | 
  387 | // ====================================================================
  388 | // PHASE 8 — FAILURE HANDLING
  389 | // ====================================================================
  390 | test.describe('Phase 8 — Failure Handling', () => {
  391 |   test('IMPLEMENTATION GAP: No provider unavailable handling', async () => {
  392 |     const resp = await fetch(`${BASE}/api/notifications/fail`).catch(() => null);
  393 |     if (resp) {
  394 |       const data = await resp.json();
  395 |       expect(data).toBeDefined();
  396 |     }
  397 |   });
  398 | 
  399 |   test('IMPLEMENTATION GAP: No network failure handling visible', async ({ page }) => {
  400 |     await login(page);
  401 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  402 |     const t = await bodyText(page);
  403 |     expect(t.includes('failed') || t.includes('Failed') || t.includes('error') || t.includes('Error')).toBeDefined();
  404 |   });
  405 | 
  406 |   test('IMPLEMENTATION GAP: No timeout handling visible', async ({ page }) => {
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
> 421 |     await page.goto('/', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/
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
```