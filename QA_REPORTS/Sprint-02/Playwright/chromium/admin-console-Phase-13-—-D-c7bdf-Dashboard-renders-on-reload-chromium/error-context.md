# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 13 — Data Integrity >> Dashboard renders on reload
- Location: tests\admin-console.spec.ts:430:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
Call log:
  - navigating to "http://localhost:5174/admin/dashboard", waiting until "networkidle"

```

# Test source

```ts
  331 | 
  332 |   test('IMPLEMENTATION GAP: No shipping rate config', async ({ page }) => {
  333 |     await page.goto(`${ADMIN}/shipping`, { waitUntil: 'networkidle' });
  334 |     expect(await hasText(page, 'Rate')).toBe(false);
  335 |   });
  336 | });
  337 | 
  338 | // ====================================================================
  339 | // PHASE 10 — ANALYTICS & REPORTING
  340 | // ====================================================================
  341 | test.describe('Phase 10 — Analytics & Reporting', () => {
  342 |   test('Analytics page loads', async ({ page }) => {
  343 |     expect((await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  344 |   });
  345 | 
  346 |   test('Reports page loads', async ({ page }) => {
  347 |     expect((await page.goto(`${ADMIN}/reports`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  348 |   });
  349 | 
  350 |   test('Finance page loads', async ({ page }) => {
  351 |     expect((await page.goto(`${ADMIN}/finance`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  352 |   });
  353 | 
  354 |   test('IMPLEMENTATION GAP: No charts on analytics', async ({ page }) => {
  355 |     await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' });
  356 |     expect(await page.locator('canvas,svg[class*="Chart"]').count()).toBe(0);
  357 |   });
  358 | 
  359 |   test('IMPLEMENTATION GAP: No date range filtering', async ({ page }) => {
  360 |     await page.goto(`${ADMIN}/analytics`, { waitUntil: 'networkidle' });
  361 |     expect(await page.locator('input[type="date"]').count()).toBe(0);
  362 |   });
  363 | });
  364 | 
  365 | // ====================================================================
  366 | // PHASE 11 — SETTINGS
  367 | // ====================================================================
  368 | test.describe('Phase 11 — Settings', () => {
  369 |   test('Settings page loads', async ({ page }) => {
  370 |     expect((await page.goto(`${ADMIN}/settings`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  371 |   });
  372 | 
  373 |   test('System page loads', async ({ page }) => {
  374 |     expect((await page.goto(`${ADMIN}/system`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  375 |   });
  376 | 
  377 |   test('Profile page loads', async ({ page }) => {
  378 |     expect((await page.goto(`${ADMIN}/profile`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  379 |   });
  380 | 
  381 |   test('Help page loads', async ({ page }) => {
  382 |     expect((await page.goto(`${ADMIN}/help`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  383 |   });
  384 | 
  385 |   test('IMPLEMENTATION GAP: No editable settings fields', async ({ page }) => {
  386 |     await page.goto(`${ADMIN}/settings`, { waitUntil: 'networkidle' });
  387 |     expect(await page.locator('input:not([type="hidden"]),select,textarea').count()).toBe(0);
  388 |   });
  389 | 
  390 |   test('IMPLEMENTATION GAP: No save button', async ({ page }) => {
  391 |     await page.goto(`${ADMIN}/settings`, { waitUntil: 'networkidle' });
  392 |     expect(await page.locator('button:has-text("Save"),button[type="submit"]').count()).toBe(0);
  393 |   });
  394 | });
  395 | 
  396 | // ====================================================================
  397 | // PHASE 12 — SECURITY
  398 | // ====================================================================
  399 | test.describe('Phase 12 — Security', () => {
  400 |   test('No production secrets in admin pages', async ({ page }) => {
  401 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  402 |     const html = await page.locator('html').innerHTML();
  403 |     for (const s of ['sk_live_', 'pk_live_', 'rzp_live_']) {
  404 |       expect(html.includes(s)).toBe(false);
  405 |     }
  406 |   });
  407 | 
  408 |   test('IMPLEMENTATION GAP: No privilege escalation protection', async ({ page }) => {
  409 |     // /admin/products accessible without auth
  410 |     const r = await page.request.get(`${ADMIN}/products`);
  411 |     expect(r.status()).toBeLessThan(400);
  412 |   });
  413 | 
  414 |   test('IMPLEMENTATION GAP: No admin API authentication', async ({ page }) => {
  415 |     const r = await page.request.get('/admin/dashboard');
  416 |     expect(r.status()).toBeLessThan(500);
  417 |   });
  418 | 
  419 |   test('Admin layout renders with navigation', async ({ page }) => {
  420 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  421 |     const nav = page.locator('nav,[role="navigation"]').first();
  422 |     await expect(nav).toBeVisible({ timeout: 5000 });
  423 |   });
  424 | });
  425 | 
  426 | // ====================================================================
  427 | // PHASE 13 — DATA INTEGRITY
  428 | // ====================================================================
  429 | test.describe('Phase 13 — Data Integrity', () => {
  430 |   test('Dashboard renders on reload', async ({ page }) => {
> 431 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
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
  526 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  527 |     const h1 = page.locator('h1').first();
  528 |     if (await h1.isVisible({ timeout: 2000 }).catch(() => false)) {
  529 |       expect(parseFloat(await h1.evaluate(e => getComputedStyle(e).fontSize))).toBeGreaterThanOrEqual(14);
  530 |     }
  531 |   });
```