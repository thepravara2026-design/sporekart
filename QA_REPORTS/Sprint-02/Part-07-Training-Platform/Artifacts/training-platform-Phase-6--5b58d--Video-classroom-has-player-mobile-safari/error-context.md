# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 6 — Learner Dashboard >> Video classroom has player
- Location: tests\training-platform.spec.ts:330:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - navigation "Account actions" [ref=e6]:
        - menuitem "Settings" [ref=e8] [cursor=pointer]:
          - img [ref=e10]
          - generic [ref=e13]: Settings
        - menuitem "Help" [ref=e15] [cursor=pointer]:
          - img [ref=e17]
          - generic [ref=e20]: Help
        - menuitem "Logout" [ref=e22] [cursor=pointer]:
          - img [ref=e24]
          - generic [ref=e27]: Logout
    - generic [ref=e28]:
      - complementary [ref=e29]:
        - navigation "Customer workspace navigation" [ref=e30]:
          - generic [ref=e31]:
            - generic [ref=e33]:
              - img [ref=e35]
              - generic [ref=e38]: SporeKart
            - button "Collapse sidebar" [ref=e39] [cursor=pointer]:
              - img [ref=e40]
          - menu [ref=e43]:
            - menuitem "Dashboard" [ref=e47] [cursor=pointer]:
              - img [ref=e49]
              - generic [ref=e51]: Dashboard
            - menuitem "Orders" [ref=e55] [cursor=pointer]:
              - img [ref=e57]
              - generic [ref=e60]: Orders
            - menuitem "Wishlist" [ref=e64] [cursor=pointer]:
              - img [ref=e66]
              - generic [ref=e68]: Wishlist
            - menuitem "Training" [ref=e72] [cursor=pointer]:
              - img [ref=e74]
              - generic [ref=e77]: Training
            - menuitem "Products" [ref=e81] [cursor=pointer]:
              - img [ref=e83]
              - generic [ref=e87]: Products
            - menuitem "Addresses" [ref=e91] [cursor=pointer]:
              - img [ref=e93]
              - generic [ref=e96]: Addresses
            - menuitem "Support" [ref=e100] [cursor=pointer]:
              - img [ref=e102]
              - generic [ref=e105]: Support
            - menuitem "Notifications" [ref=e109] [cursor=pointer]:
              - img [ref=e111]
              - generic [ref=e114]: Notifications
          - generic [ref=e116]: v1.0.0-beta · Customer Workspace
      - main [ref=e117]:
        - generic [ref=e119]:
          - generic [ref=e121]:
            - heading "classroom / crs-001" [level=1] [ref=e122]
            - paragraph [ref=e123]: Dashboard
          - navigation "Breadcrumb" [ref=e124]:
            - list [ref=e125]:
              - listitem [ref=e126]:
                - link "Dashboard" [ref=e127]:
                  - /url: /dashboard
                  - generic [ref=e128]: Dashboard
              - listitem [ref=e129]:
                - img [ref=e130]
              - listitem [ref=e132]:
                - link "Training" [ref=e133]:
                  - /url: /dashboard/training
                  - generic [ref=e134]: Training
              - listitem [ref=e135]:
                - img [ref=e136]
              - listitem [ref=e138]:
                - generic [ref=e140]: classroom / crs-001
          - generic [ref=e142]:
            - heading "Course not found" [level=2] [ref=e143]
            - button "Back to Catalog" [ref=e144]
          - generic [ref=e145]:
            - generic [ref=e146]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e147]:
              - link "Privacy" [ref=e148]:
                - /url: /privacy-policy
              - link "Terms" [ref=e149]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e150]:
                - /url: /support
```

# Test source

```ts
  234 | 
  235 |   test('IMPLEMENTATION GAP: No batch capacity management', async ({ page }) => {
  236 |     await page.goto('/admin/training/batches', { waitUntil: 'networkidle' });
  237 |     const t = await bodyText(page);
  238 |     expect(t.includes('Capacity') || t.includes('capacity')).toBe(false);
  239 |   });
  240 | 
  241 |   test('IMPLEMENTATION GAP: No trainer-to-batch assignment', async ({ page }) => {
  242 |     await page.goto('/admin/training/trainers', { waitUntil: 'networkidle' });
  243 |     const t = await bodyText(page);
  244 |     expect(t.includes('Trainer') || t.includes('trainer')).toBe(false);
  245 |   });
  246 | });
  247 | 
  248 | // ====================================================================
  249 | // PHASE 6 — LEARNER DASHBOARD
  250 | // ====================================================================
  251 | test.describe('Phase 6 — Learner Dashboard', () => {
  252 |   test('Learner dashboard loads', async ({ page }) => {
  253 |     await login(page);
  254 |     const r = await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  255 |     expect(r?.status()).toBeLessThan(400);
  256 |   });
  257 | 
  258 |   test('Learner dashboard has metrics', async ({ page }) => {
  259 |     await login(page);
  260 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  261 |     const t = await bodyText(page);
  262 |     expect(t.includes('Enrolled') || t.includes('Completed') || t.includes('Certificate')).toBeTruthy();
  263 |   });
  264 | 
  265 |   test('Learner dashboard has continue learning', async ({ page }) => {
  266 |     await login(page);
  267 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  268 |     const t = await bodyText(page);
  269 |     expect(t.includes('Continue') || t.includes('Resume') || t.includes('Progress')).toBeTruthy();
  270 |   });
  271 | 
  272 |   test('Learner dashboard has catalog options', async ({ page }) => {
  273 |     await login(page);
  274 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  275 |     const t = await bodyText(page);
  276 |     expect(t.includes('Browse') || t.includes('Courses') || t.includes('Catalog')).toBeTruthy();
  277 |   });
  278 | 
  279 |   test('Course library page loads', async ({ page }) => {
  280 |     await login(page);
  281 |     const r = await page.goto('/dashboard/training/courses', { waitUntil: 'networkidle' });
  282 |     expect(r?.status()).toBeLessThan(400);
  283 |   });
  284 | 
  285 |   test('My learning page loads', async ({ page }) => {
  286 |     await login(page);
  287 |     const r = await page.goto('/dashboard/training/my-learning', { waitUntil: 'networkidle' });
  288 |     expect(r?.status()).toBeLessThan(400);
  289 |   });
  290 | 
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
> 334 |     expect(t.includes('Video') || t.includes('Lesson') || t.includes('Lecture')).toBeTruthy();
      |                                                                                  ^ Error: expect(received).toBeTruthy()
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
  429 |     expect(t.includes('Attendance') || t.includes('attendance')).toBe(false);
  430 |   });
  431 | 
  432 |   test('IMPLEMENTATION GAP: No assessment management', async ({ page }) => {
  433 |     await page.goto('/admin/training/assessments', { waitUntil: 'networkidle' });
  434 |     const t = await bodyText(page);
```