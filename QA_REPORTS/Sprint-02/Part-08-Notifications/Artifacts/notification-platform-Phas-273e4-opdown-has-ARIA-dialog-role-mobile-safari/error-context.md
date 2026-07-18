# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 12 — Accessibility >> Notification dropdown has ARIA dialog role
- Location: tests\notification-platform.spec.ts:548:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('[role="dialog"][aria-label="Notifications"]')
Expected: visible
Timeout: 3000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" with timeout 3000ms
  - waiting for locator('[role="dialog"][aria-label="Notifications"]')

```

```yaml
- link "Skip to content":
  - /url: "#main"
- banner:
  - button "Open sidebar"
  - text: Admin Search admin... Ctrl+K
  - button "Workspace actions"
  - button "Notifications"
  - button "Toggle theme"
  - navigation "Admin actions":
    - menuitem "Profile"
    - menuitem "Settings"
    - menuitem "Help"
- complementary:
  - navigation "Admin workspace navigation":
    - text: SporeKart Search...
    - button "Collapse sidebar":
      - img
    - menu:
      - menuitem "Dashboard Pin Dashboard":
        - text: Dashboard
        - button "Pin Dashboard":
          - img
      - menuitem "Products Pin Products":
        - text: Products
        - button "Pin Products":
          - img
      - menuitem "Inventory Pin Inventory":
        - text: Inventory
        - button "Pin Inventory":
          - img
      - menuitem "Warehouse Pin Warehouse":
        - text: Warehouse
        - button "Pin Warehouse":
          - img
      - menuitem "Inventory Items Pin Inventory Items":
        - text: Inventory Items
        - button "Pin Inventory Items":
          - img
      - menuitem "Stock Pin Stock":
        - text: Stock
        - button "Pin Stock":
          - img
      - menuitem "Batch Pin Batch":
        - text: Batch
        - button "Pin Batch":
          - img
      - menuitem "Movements Pin Movements":
        - text: Movements
        - button "Pin Movements":
          - img
      - menuitem "Receiving Pin Receiving":
        - text: Receiving
        - button "Pin Receiving":
          - img
      - menuitem "Intelligence Pin Intelligence":
        - text: Intelligence
        - button "Pin Intelligence":
          - img
      - menuitem "Orders Pin Orders":
        - text: Orders
        - button "Pin Orders":
          - img
      - menuitem "Customers Pin Customers":
        - text: Customers
        - button "Pin Customers":
          - img
      - menuitem "CRM Pin CRM":
        - text: CRM
        - button "Pin CRM":
          - img
      - menuitem "Training Pin Training":
        - text: Training
        - button "Pin Training":
          - img
      - menuitem "Shipping Pin Shipping":
        - text: Shipping
        - button "Pin Shipping":
          - img
      - menuitem "Finance Pin Finance":
        - text: Finance
        - button "Pin Finance":
          - img
      - menuitem "Reports Pin Reports":
        - text: Reports
        - button "Pin Reports":
          - img
      - menuitem "Analytics Pin Analytics":
        - text: Analytics
        - button "Pin Analytics":
          - img
      - menuitem "Profile Pin Profile":
        - text: Profile
        - button "Pin Profile":
          - img
      - menuitem "Settings Pin Settings":
        - text: Settings
        - button "Pin Settings":
          - img
      - menuitem "System Pin System":
        - text: System
        - button "Pin System":
          - img
      - menuitem "Help Pin Help":
        - text: Help
        - button "Pin Help":
          - img
      - menuitem "PINNED Pin PINNED" [disabled]:
        - text: PINNED
        - button "Pin PINNED" [disabled]:
          - img
      - menuitem "Pin items for quick access Pin Pin items for quick access" [disabled]:
        - text: Pin items for quick access
        - button "Pin Pin items for quick access" [disabled]:
          - img
      - menuitem "FAVORITES Pin FAVORITES" [disabled]:
        - text: FAVORITES
        - button "Pin FAVORITES" [disabled]:
          - img
      - menuitem "No favorites yet Pin No favorites yet" [disabled]:
        - text: No favorites yet
        - button "Pin No favorites yet" [disabled]:
          - img
      - menuitem "RECENT Pin RECENT" [disabled]:
        - text: RECENT
        - button "Pin RECENT" [disabled]:
          - img
      - menuitem "No recent pages Pin No recent pages" [disabled]:
        - text: No recent pages
        - button "Pin No recent pages" [disabled]:
          - img
      - menuitem "QUICK ACTIONS Pin QUICK ACTIONS" [disabled]:
        - text: QUICK ACTIONS
        - button "Pin QUICK ACTIONS" [disabled]:
          - img
      - menuitem "Cmd+K to search Pin Cmd+K to search" [disabled]:
        - text: Cmd+K to search
        - button "Pin Cmd+K to search" [disabled]:
          - img
    - text: v1.0.0 · Admin Workspace
- main:
  - heading "Admin" [level=1]
  - paragraph: Enterprise Administration · Admin
  - navigation "Breadcrumb":
    - list:
      - listitem: Admin
  - heading "Welcome to the Enterprise Admin Platform" [level=2]
  - paragraph: This is your central workspace for managing SporeKart. Business modules will be implemented in upcoming sprints.
  - article:
    - heading "Workspace Summary" [level=3]
    - text: Active Role administrator Environment Development Version 1.0.0 Modules 6 layout routes
  - article:
    - heading "Pinned Widgets" [level=3]
    - paragraph: Pin widgets to this area for quick access. Use the pin icon on any widget to add it here.
  - article:
    - heading "Users" [level=3]
    - paragraph: User management will appear here.
  - article:
    - heading "Content" [level=3]
    - paragraph: Content moderation will appear here.
  - article:
    - heading "Analytics" [level=3]
    - paragraph: Analytics dashboard will appear here.
  - article:
    - heading "System" [level=3]
    - paragraph: System health will appear here.
  - heading "Quick Actions" [level=3]
  - article:
    - heading "Create User" [level=4]
    - paragraph: Add a new user account
  - article:
    - heading "View Reports" [level=4]
    - paragraph: Access platform reports
  - article:
    - heading "System Check" [level=4]
    - paragraph: Run health diagnostics
  - article:
    - heading "Help Center" [level=4]
    - paragraph: Browse admin documentation
  - article:
    - heading "Recent Activity" [level=3]
    - paragraph: No recent activity to display.
  - article:
    - heading "Announcements" [level=3]
    - paragraph: No announcements at this time.
  - text: © 2026 SporeKart. All rights reserved.
  - navigation:
    - link "Privacy":
      - /url: /privacy-policy
    - link "Terms":
      - /url: /terms-and-conditions
```

# Test source

```ts
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
> 554 |     await expect(dialog).toBeVisible({ timeout: 3000 });
      |                          ^ Error: expect(locator).toBeVisible() failed
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
  608 |     expect(Date.now() - start).toBeLessThan(15000);
  609 |   });
  610 | 
  611 |   test('Communication templates page loads within 10s', async ({ page }) => {
  612 |     await login(page);
  613 |     const start = Date.now();
  614 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
  615 |     expect(Date.now() - start).toBeLessThan(15000);
  616 |   });
  617 | 
  618 |   test('Communication statistics page loads within 10s', async ({ page }) => {
  619 |     await login(page);
  620 |     const start = Date.now();
  621 |     await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
  622 |     expect(Date.now() - start).toBeLessThan(15000);
  623 |   });
  624 | });
  625 | 
  626 | // ====================================================================
  627 | // PHASE 14 — VISUAL REVIEW
  628 | // ====================================================================
  629 | test.describe('Phase 14 — Visual Review', () => {
  630 |   test('Notification dropdown has proper width', async ({ page }) => {
  631 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  632 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  633 |     await bell.click();
  634 |     await page.waitForTimeout(500);
  635 |     const dialog = page.locator('[role="dialog"]');
  636 |     const box = await dialog.boundingBox();
  637 |     expect(box).not.toBeNull();
  638 |     if (box) expect(box.width).toBeGreaterThan(200);
  639 |   });
  640 | 
  641 |   test('No horizontal scroll on notification page', async ({ page }) => {
  642 |     await login(page);
  643 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  644 |     const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
  645 |     expect(scrollW).toBe(0);
  646 |   });
  647 | 
  648 |   test('Communication overview has content', async ({ page }) => {
  649 |     await login(page);
  650 |     await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
  651 |     const t = await bodyText(page);
  652 |     expect(t.length).toBeGreaterThan(20);
  653 |   });
  654 | 
```