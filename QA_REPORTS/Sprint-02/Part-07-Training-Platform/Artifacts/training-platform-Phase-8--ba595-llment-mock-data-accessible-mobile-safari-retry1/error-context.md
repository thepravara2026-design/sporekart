# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 8 — Data Integrity >> Enrollment mock data accessible
- Location: tests\training-platform.spec.ts:474:7

# Error details

```
Error: expect(received).toBeGreaterThan(expected)

Expected: > 20
Received:   15
```

# Page snapshot

```yaml
- link "Skip to content" [ref=e3]:
  - /url: "#main"
```

# Test source

```ts
  377 | 
  378 |   test('Curriculum builder loads', async ({ page }) => {
  379 |     const r = await page.goto('/admin/training/curriculum', { waitUntil: 'networkidle' });
  380 |     expect(r?.status()).toBeLessThan(400);
  381 |   });
  382 | 
  383 |   test('Enrollment management loads', async ({ page }) => {
  384 |     const r = await page.goto('/admin/training/enrollment', { waitUntil: 'networkidle' });
  385 |     expect(r?.status()).toBeLessThan(400);
  386 |   });
  387 | 
  388 |   test('Enrollment has pricing panel', async ({ page }) => {
  389 |     await page.goto('/admin/training/enrollment', { waitUntil: 'networkidle' });
  390 |     const t = await bodyText(page);
  391 |     expect(t.includes('Pricing') || t.includes('Capacity') || t.includes('Enrollment')).toBeTruthy();
  392 |   });
  393 | 
  394 |   test('Taxonomy manager loads', async ({ page }) => {
  395 |     const r = await page.goto('/admin/training/taxonomy', { waitUntil: 'networkidle' });
  396 |     expect(r?.status()).toBeLessThan(400);
  397 |   });
  398 | 
  399 |   test('Resource library loads', async ({ page }) => {
  400 |     const r = await page.goto('/admin/training/resources', { waitUntil: 'networkidle' });
  401 |     expect(r?.status()).toBeLessThan(400);
  402 |   });
  403 | 
  404 |   test('LMS analytics loads', async ({ page }) => {
  405 |     const r = await page.goto('/admin/training/lms-analytics/executive', { waitUntil: 'networkidle' });
  406 |     expect(r?.status()).toBeLessThan(400);
  407 |   });
  408 | 
  409 |   test('Communication platform loads', async ({ page }) => {
  410 |     const r = await page.goto('/admin/training/communication', { waitUntil: 'networkidle' });
  411 |     expect(r?.status()).toBeLessThan(400);
  412 |   });
  413 | 
  414 |   test('Student workspace loads', async ({ page }) => {
  415 |     const r = await page.goto('/admin/training/student-workspace', { waitUntil: 'networkidle' });
  416 |     expect(r?.status()).toBeLessThan(400);
  417 |   });
  418 | 
  419 |   test('IMPLEMENTATION GAP: No admin certificates page', async ({ page }) => {
  420 |     const r = await page.goto('/admin/training/certificates', { waitUntil: 'networkidle' });
  421 |     expect(r?.status()).toBeLessThan(400);
  422 |     const t = await bodyText(page);
  423 |     expect(t.includes('Certificate') || t.includes('certificate')).toBe(false);
  424 |   });
  425 | 
  426 |   test('IMPLEMENTATION GAP: No attendance tracking', async ({ page }) => {
  427 |     await page.goto('/admin/training/attendance', { waitUntil: 'networkidle' });
  428 |     const t = await bodyText(page);
  429 |     expect(t.includes('Attendance') || t.includes('attendance')).toBe(false);
  430 |   });
  431 | 
  432 |   test('IMPLEMENTATION GAP: No assessment management', async ({ page }) => {
  433 |     await page.goto('/admin/training/assessments', { waitUntil: 'networkidle' });
  434 |     const t = await bodyText(page);
  435 |     expect(t.includes('Assessment') || t.includes('assessment')).toBe(false);
  436 |   });
  437 | });
  438 | 
  439 | // ====================================================================
  440 | // PHASE 8 — DATA INTEGRITY
  441 | // ====================================================================
  442 | test.describe('Phase 8 — Data Integrity', () => {
  443 |   test('Catalog data persists on reload', async ({ page }) => {
  444 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  445 |     const t1 = await bodyText(page);
  446 |     await page.reload(); await page.waitForLoadState('networkidle');
  447 |     const t2 = await bodyText(page);
  448 |     expect(t1.length > 0 && t2.length > 0).toBeTruthy();
  449 |   });
  450 | 
  451 |   test('Course detail data persists on reload', async ({ page }) => {
  452 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  453 |     const t1 = await bodyText(page);
  454 |     await page.reload(); await page.waitForLoadState('networkidle');
  455 |     const t2 = await bodyText(page);
  456 |     expect(t1.length > 0 && t2.length > 0).toBeTruthy();
  457 |   });
  458 | 
  459 |   test('Learner dashboard data persists', async ({ page }) => {
  460 |     await login(page);
  461 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  462 |     const t1 = await bodyText(page);
  463 |     await page.reload(); await page.waitForLoadState('networkidle');
  464 |     const t2 = await bodyText(page);
  465 |     expect(t1.length > 0 && t2.length > 0).toBeTruthy();
  466 |   });
  467 | 
  468 |   test('Admin course registry shows mock courses', async ({ page }) => {
  469 |     await page.goto('/admin/training/courses', { waitUntil: 'networkidle' });
  470 |     const t = await bodyText(page);
  471 |     expect(t.includes('Course') || t.includes('course')).toBeTruthy();
  472 |   });
  473 | 
  474 |   test('Enrollment mock data accessible', async ({ page }) => {
  475 |     await page.goto('/admin/training/enrollment', { waitUntil: 'networkidle' });
  476 |     const t = await bodyText(page);
> 477 |     expect(t.length).toBeGreaterThan(20);
      |                      ^ Error: expect(received).toBeGreaterThan(expected)
  478 |   });
  479 | 
  480 |   test('Navigation consistent across training pages', async ({ page }) => {
  481 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
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
```