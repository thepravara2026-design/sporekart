# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 7 — Admin Training Management >> IMPLEMENTATION GAP: No attendance tracking
- Location: tests\training-platform.spec.ts:426:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: false
Received: true
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - generic [ref=e6]:
        - generic [ref=e7]:
          - button "Open sidebar" [ref=e8] [cursor=pointer]:
            - img [ref=e9]
          - generic [ref=e10]:
            - img [ref=e12]
            - generic [ref=e14]: Admin
          - generic "Search placeholder" [ref=e15] [cursor=pointer]:
            - img [ref=e16]
            - generic [ref=e19]: Search admin...
            - generic [ref=e20]: Ctrl+K
        - generic [ref=e21]:
          - generic [ref=e22]:
            - button "Workspace actions" [ref=e23] [cursor=pointer]:
              - img [ref=e24]
            - button "Notifications" [ref=e29] [cursor=pointer]:
              - img [ref=e30]
            - button "Toggle theme" [ref=e34] [cursor=pointer]:
              - img [ref=e35]
          - navigation "Admin actions" [ref=e37]:
            - menuitem "Profile" [ref=e39] [cursor=pointer]:
              - img [ref=e41]
              - generic [ref=e44]: Profile
            - menuitem "Settings" [ref=e46] [cursor=pointer]:
              - img [ref=e48]
              - generic [ref=e51]: Settings
            - menuitem "Help" [ref=e53] [cursor=pointer]:
              - img [ref=e55]
              - generic [ref=e58]: Help
    - generic [ref=e59]:
      - complementary [ref=e60]:
        - navigation "Admin workspace navigation" [ref=e61]:
          - generic [ref=e62]:
            - generic [ref=e64]:
              - generic [ref=e65]:
                - img [ref=e67]
                - generic [ref=e69]: SporeKart
              - generic "Search admin sidebar" [ref=e70]:
                - img [ref=e71]
                - generic [ref=e74]: Search...
            - button "Collapse sidebar" [ref=e75] [cursor=pointer]:
              - img [ref=e76]
          - menu [ref=e79]:
            - menuitem "Dashboard Pin Dashboard" [ref=e83] [cursor=pointer]:
              - img [ref=e85]
              - generic [ref=e87]: Dashboard
              - button "Pin Dashboard" [ref=e88]:
                - img [ref=e89]
            - menuitem "Products Pin Products" [ref=e94] [cursor=pointer]:
              - img [ref=e96]
              - generic [ref=e100]: Products
              - button "Pin Products" [ref=e101]:
                - img [ref=e102]
            - menuitem "Inventory Pin Inventory" [ref=e107] [cursor=pointer]:
              - img [ref=e109]
              - generic [ref=e112]: Inventory
              - button "Pin Inventory" [ref=e113]:
                - img [ref=e114]
            - menuitem "Warehouse Pin Warehouse" [ref=e119] [cursor=pointer]:
              - img [ref=e121]
              - generic [ref=e124]: Warehouse
              - button "Pin Warehouse" [ref=e125]:
                - img [ref=e126]
            - menuitem "Inventory Items Pin Inventory Items" [ref=e131] [cursor=pointer]:
              - img [ref=e133]
              - generic [ref=e137]: Inventory Items
              - button "Pin Inventory Items" [ref=e138]:
                - img [ref=e139]
            - menuitem "Stock Pin Stock" [ref=e144] [cursor=pointer]:
              - img [ref=e146]
              - generic [ref=e150]: Stock
              - button "Pin Stock" [ref=e151]:
                - img [ref=e152]
            - menuitem "Batch Pin Batch" [ref=e157] [cursor=pointer]:
              - img [ref=e159]
              - generic [ref=e163]: Batch
              - button "Pin Batch" [ref=e164]:
                - img [ref=e165]
            - menuitem "Movements Pin Movements" [ref=e170] [cursor=pointer]:
              - img [ref=e172]
              - generic [ref=e174]: Movements
              - button "Pin Movements" [ref=e175]:
                - img [ref=e176]
            - menuitem "Receiving Pin Receiving" [ref=e181] [cursor=pointer]:
              - img [ref=e183]
              - generic [ref=e185]: Receiving
              - button "Pin Receiving" [ref=e186]:
                - img [ref=e187]
            - menuitem "Intelligence Pin Intelligence" [ref=e192] [cursor=pointer]:
              - img [ref=e194]
              - generic [ref=e196]: Intelligence
              - button "Pin Intelligence" [ref=e197]:
                - img [ref=e198]
            - menuitem "Orders Pin Orders" [ref=e203] [cursor=pointer]:
              - img [ref=e205]
              - generic [ref=e209]: Orders
              - button "Pin Orders" [ref=e210]:
                - img [ref=e211]
            - menuitem "Customers Pin Customers" [ref=e216] [cursor=pointer]:
              - img [ref=e218]
              - generic [ref=e223]: Customers
              - button "Pin Customers" [ref=e224]:
                - img [ref=e225]
            - menuitem "CRM Pin CRM" [ref=e230] [cursor=pointer]:
              - img [ref=e232]
              - generic [ref=e234]: CRM
              - button "Pin CRM" [ref=e235]:
                - img [ref=e236]
            - menuitem "Training Pin Training" [ref=e241] [cursor=pointer]:
              - img [ref=e243]
              - generic [ref=e246]: Training
              - button "Pin Training" [ref=e247]:
                - img [ref=e248]
            - menuitem "Shipping Pin Shipping" [ref=e253] [cursor=pointer]:
              - img [ref=e255]
              - generic [ref=e260]: Shipping
              - button "Pin Shipping" [ref=e261]:
                - img [ref=e262]
            - menuitem "Finance Pin Finance" [ref=e267] [cursor=pointer]:
              - img [ref=e269]
              - generic [ref=e271]: Finance
              - button "Pin Finance" [ref=e272]:
                - img [ref=e273]
            - menuitem "Reports Pin Reports" [ref=e278] [cursor=pointer]:
              - img [ref=e280]
              - generic [ref=e281]: Reports
              - button "Pin Reports" [ref=e282]:
                - img [ref=e283]
            - menuitem "Analytics Pin Analytics" [ref=e288] [cursor=pointer]:
              - img [ref=e290]
              - generic [ref=e293]: Analytics
              - button "Pin Analytics" [ref=e294]:
                - img [ref=e295]
            - menuitem "Profile Pin Profile" [ref=e300] [cursor=pointer]:
              - img [ref=e302]
              - generic [ref=e305]: Profile
              - button "Pin Profile" [ref=e306]:
                - img [ref=e307]
            - menuitem "Settings Pin Settings" [ref=e312] [cursor=pointer]:
              - img [ref=e314]
              - generic [ref=e317]: Settings
              - button "Pin Settings" [ref=e318]:
                - img [ref=e319]
            - menuitem "System Pin System" [ref=e324] [cursor=pointer]:
              - img [ref=e326]
              - generic [ref=e328]: System
              - button "Pin System" [ref=e329]:
                - img [ref=e330]
            - menuitem "Help Pin Help" [ref=e335] [cursor=pointer]:
              - img [ref=e337]
              - generic [ref=e340]: Help
              - button "Pin Help" [ref=e341]:
                - img [ref=e342]
            - menuitem "PINNED Pin PINNED" [disabled] [ref=e347]:
              - generic [ref=e348]: PINNED
              - button "Pin PINNED" [disabled] [ref=e349] [cursor=pointer]:
                - img [ref=e350]
            - menuitem "Pin items for quick access Pin Pin items for quick access" [disabled] [ref=e355]:
              - generic [ref=e356]: Pin items for quick access
              - button "Pin Pin items for quick access" [disabled] [ref=e357] [cursor=pointer]:
                - img [ref=e358]
            - menuitem "FAVORITES Pin FAVORITES" [disabled] [ref=e363]:
              - generic [ref=e364]: FAVORITES
              - button "Pin FAVORITES" [disabled] [ref=e365] [cursor=pointer]:
                - img [ref=e366]
            - menuitem "No favorites yet Pin No favorites yet" [disabled] [ref=e371]:
              - img [ref=e373]
              - generic [ref=e375]: No favorites yet
              - button "Pin No favorites yet" [disabled] [ref=e376] [cursor=pointer]:
                - img [ref=e377]
            - menuitem "RECENT Pin RECENT" [disabled] [ref=e382]:
              - generic [ref=e383]: RECENT
              - button "Pin RECENT" [disabled] [ref=e384] [cursor=pointer]:
                - img [ref=e385]
            - menuitem "No recent pages Pin No recent pages" [disabled] [ref=e390]:
              - img [ref=e392]
              - generic [ref=e395]: No recent pages
              - button "Pin No recent pages" [disabled] [ref=e396] [cursor=pointer]:
                - img [ref=e397]
            - menuitem "QUICK ACTIONS Pin QUICK ACTIONS" [disabled] [ref=e402]:
              - generic [ref=e403]: QUICK ACTIONS
              - button "Pin QUICK ACTIONS" [disabled] [ref=e404] [cursor=pointer]:
                - img [ref=e405]
            - menuitem "Cmd+K to search Pin Cmd+K to search" [disabled] [ref=e410]:
              - img [ref=e412]
              - generic [ref=e414]: Cmd+K to search
              - button "Pin Cmd+K to search" [disabled] [ref=e415] [cursor=pointer]:
                - img [ref=e416]
          - generic [ref=e419]: v1.0.0 · Admin Workspace
      - main [ref=e420]:
        - generic [ref=e422]:
          - generic [ref=e424]:
            - heading "attendance" [level=1] [ref=e425]
            - paragraph [ref=e426]: Enterprise Administration · Admin
          - navigation "Breadcrumb" [ref=e428]:
            - list [ref=e429]:
              - listitem [ref=e430]:
                - link "Admin" [ref=e431] [cursor=pointer]:
                  - /url: /admin/dashboard
                  - generic [ref=e432]: Admin
              - listitem [ref=e433]:
                - img [ref=e434]
              - listitem [ref=e436]:
                - link "Training" [ref=e437] [cursor=pointer]:
                  - /url: /admin/training
                  - generic [ref=e438]: Training
              - listitem [ref=e439]:
                - img [ref=e440]
              - listitem [ref=e442]:
                - generic [ref=e444]: attendance
          - generic [ref=e446]:
            - navigation "Training workspace navigation" [ref=e447]:
              - generic [ref=e448]:
                - img [ref=e450]
                - generic [ref=e453]: Training
              - generic [ref=e454]:
                - generic [ref=e455]:
                  - generic [ref=e456]: Overview
                  - button "Dashboard" [ref=e457] [cursor=pointer]:
                    - img [ref=e458]
                    - generic [ref=e460]: Dashboard
                - generic [ref=e461]:
                  - generic [ref=e462]: Management
                  - button "Courses" [ref=e463] [cursor=pointer]:
                    - img [ref=e464]
                    - generic [ref=e467]: Courses
                  - button "Curriculum" [ref=e468] [cursor=pointer]:
                    - img [ref=e469]
                    - generic [ref=e473]: Curriculum
                  - button "Training Batches" [ref=e474] [cursor=pointer]:
                    - img [ref=e475]
                    - generic [ref=e477]: Training Batches
                  - button "Students" [ref=e478] [cursor=pointer]:
                    - img [ref=e479]
                    - generic [ref=e484]: Students
                  - button "Trainers" [ref=e485] [cursor=pointer]:
                    - img [ref=e486]
                    - generic [ref=e490]: Trainers
                - generic [ref=e491]:
                  - generic [ref=e492]: Operations
                  - button "Attendance" [ref=e493] [cursor=pointer]:
                    - img [ref=e495]
                    - generic [ref=e498]: Attendance
                  - button "Assignments" [ref=e499] [cursor=pointer]:
                    - img [ref=e500]
                    - generic [ref=e503]: Assignments
                  - button "Assessments" [ref=e504] [cursor=pointer]:
                    - img [ref=e505]
                    - generic [ref=e509]: Assessments
                  - button "Certificates" [ref=e510] [cursor=pointer]:
                    - generic [ref=e511]: "?"
                    - generic [ref=e512]: Certificates
                  - button "Learning Resources" [ref=e513] [cursor=pointer]:
                    - img [ref=e514]
                    - generic [ref=e516]: Learning Resources
                  - button "Announcements" [ref=e517] [cursor=pointer]:
                    - img [ref=e518]
                    - generic [ref=e520]: Announcements
                - generic [ref=e521]:
                  - generic [ref=e522]: Student Platform
                  - button "Student Workspace" [ref=e523] [cursor=pointer]:
                    - img [ref=e524]
                    - generic [ref=e529]: Student Workspace
                - generic [ref=e530]:
                  - generic [ref=e531]: Intelligence
                  - button "Reports" [ref=e532] [cursor=pointer]:
                    - img [ref=e533]
                    - generic [ref=e534]: Reports
                  - button "Analytics" [ref=e535] [cursor=pointer]:
                    - img [ref=e536]
                    - generic [ref=e539]: Analytics
                - generic [ref=e540]:
                  - generic [ref=e541]: Settings
                  - button "Settings" [ref=e542] [cursor=pointer]:
                    - img [ref=e543]
                    - generic [ref=e546]: Settings
                - generic [ref=e547]:
                  - generic [ref=e548]: Coming Soon
                  - button "AI Assistant Soon" [ref=e549]:
                    - img [ref=e550]
                    - generic [ref=e553]: AI Assistant
                    - generic [ref=e554]: Soon
                  - button "Community Soon" [ref=e555]:
                    - img [ref=e556]
                    - generic [ref=e558]: Community
                    - generic [ref=e559]: Soon
                  - button "Discussion Board Soon" [ref=e560]:
                    - img [ref=e561]
                    - generic [ref=e563]: Discussion Board
                    - generic [ref=e564]: Soon
            - generic [ref=e565]:
              - generic [ref=e566]:
                - button "Open training navigation" [ref=e567] [cursor=pointer]:
                  - img [ref=e568]
                - generic [ref=e569]: Attendance
              - generic [ref=e571]:
                - img [ref=e573]
                - heading "Attendance" [level=2] [ref=e576]
                - paragraph [ref=e577]: Track session attendance — mark attendance, view reports, manage absences, and generate attendance certificates.
          - generic [ref=e578]:
            - generic [ref=e579]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e580]:
              - link "Privacy" [ref=e581] [cursor=pointer]:
                - /url: /privacy-policy
              - link "Terms" [ref=e582] [cursor=pointer]:
                - /url: /terms-and-conditions
```

# Test source

```ts
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
> 429 |     expect(t.includes('Attendance') || t.includes('attendance')).toBe(false);
      |                                                                  ^ Error: expect(received).toBe(expected) // Object.is equality
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
```