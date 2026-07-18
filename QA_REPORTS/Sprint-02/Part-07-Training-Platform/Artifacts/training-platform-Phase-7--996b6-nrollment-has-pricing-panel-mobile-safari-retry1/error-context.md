# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 7 — Admin Training Management >> Enrollment has pricing panel
- Location: tests\training-platform.spec.ts:388:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- link "Skip to content" [ref=e3]:
  - /url: "#main"
```

# Test source

```ts
  291 |   test('My learning has enrolled courses', async ({ page }) => {
  292 |     await login(page);
  293 |     await page.goto('/dashboard/training/my-learning', { waitUntil: 'networkidle' });
  294 |     const t = await bodyText(page);
  295 |     expect(t.includes('Course') || t.includes('Progress')).toBeTruthy();
  296 |   });
  297 | 
  298 |   test('Certificates page loads', async ({ page }) => {
  299 |     await login(page);
  300 |     const r = await page.goto('/dashboard/training/certificates', { waitUntil: 'networkidle' });
  301 |     expect(r?.status()).toBeLessThan(400);
  302 |   });
  303 | 
  304 |   test('Certificates page shows certificate data', async ({ page }) => {
  305 |     await login(page);
  306 |     await page.goto('/dashboard/training/certificates', { waitUntil: 'networkidle' });
  307 |     const t = await bodyText(page);
  308 |     expect(t.includes('Certificate') || t.includes('certificate')).toBeTruthy();
  309 |   });
  310 | 
  311 |   test('Training schedule page loads', async ({ page }) => {
  312 |     await login(page);
  313 |     const r = await page.goto('/dashboard/training/schedule', { waitUntil: 'networkidle' });
  314 |     expect(r?.status()).toBeLessThan(400);
  315 |   });
  316 | 
  317 |   test('Training schedule has upcoming sessions', async ({ page }) => {
  318 |     await login(page);
  319 |     await page.goto('/dashboard/training/schedule', { waitUntil: 'networkidle' });
  320 |     const t = await bodyText(page);
  321 |     expect(t.includes('Upcoming') || t.includes('Schedule') || t.includes('Webinar')).toBeTruthy();
  322 |   });
  323 | 
  324 |   test('Video classroom page loads', async ({ page }) => {
  325 |     await login(page);
  326 |     const r = await page.goto('/dashboard/training/classroom/crs-001', { waitUntil: 'networkidle' });
  327 |     expect(r?.status()).toBeLessThan(400);
  328 |   });
  329 | 
  330 |   test('Video classroom has player', async ({ page }) => {
  331 |     await login(page);
  332 |     await page.goto('/dashboard/training/classroom/crs-001', { waitUntil: 'networkidle' });
  333 |     const t = await bodyText(page);
  334 |     expect(t.includes('Video') || t.includes('Lesson') || t.includes('Lecture')).toBeTruthy();
  335 |   });
  336 | });
  337 | 
  338 | // ====================================================================
  339 | // PHASE 7 — ADMIN TRAINING MANAGEMENT
  340 | // ====================================================================
  341 | test.describe('Phase 7 — Admin Training Management', () => {
  342 |   test('Admin training dashboard loads', async ({ page }) => {
  343 |     const r = await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  344 |     expect(r?.status()).toBeLessThan(400);
  345 |   });
  346 | 
  347 |   test('Admin training has stats', async ({ page }) => {
  348 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  349 |     const t = await bodyText(page);
  350 |     expect(t.includes('Courses') || t.includes('Students') || t.includes('Trainers')).toBeTruthy();
  351 |   });
  352 | 
  353 |   test('Admin course registry loads', async ({ page }) => {
  354 |     const r = await page.goto('/admin/training/courses', { waitUntil: 'networkidle' });
  355 |     expect(r?.status()).toBeLessThan(400);
  356 |   });
  357 | 
  358 |   test('Course registry has search/filter', async ({ page }) => {
  359 |     await page.goto('/admin/training/courses', { waitUntil: 'networkidle' });
  360 |     const s = page.locator('input[type="search"],input[placeholder*="Search"]').first();
  361 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  362 |       await s.fill('mushroom'); await page.waitForTimeout(300);
  363 |     }
  364 |     expect(true).toBeTruthy();
  365 |   });
  366 | 
  367 |   test('Course builder loads', async ({ page }) => {
  368 |     const r = await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  369 |     expect(r?.status()).toBeLessThan(400);
  370 |   });
  371 | 
  372 |   test('Course builder has form panels', async ({ page }) => {
  373 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  374 |     const t = await bodyText(page);
  375 |     expect(t.includes('Overview') || t.includes('Info') || t.includes('Curriculum')).toBeTruthy();
  376 |   });
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
> 391 |     expect(t.includes('Pricing') || t.includes('Capacity') || t.includes('Enrollment')).toBeTruthy();
      |                                                                                         ^ Error: expect(received).toBeTruthy()
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
  477 |     expect(t.length).toBeGreaterThan(20);
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
```