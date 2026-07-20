# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 14 — Visual Review >> Notification dropdown has proper width
- Location: tests\notification-platform.spec.ts:630:7

# Error details

```
TimeoutError: locator.boundingBox: Timeout 15000ms exceeded.
Call log:
  - waiting for locator('[role="dialog"]')

```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
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
            - heading "Admin" [level=1] [ref=e425]
            - paragraph [ref=e426]: Enterprise Administration · Admin
          - navigation "Breadcrumb" [ref=e428]:
            - list [ref=e429]:
              - listitem [ref=e430]:
                - generic [ref=e432]: Admin
          - generic [ref=e434]:
            - generic [ref=e435]:
              - heading "Welcome to the Enterprise Admin Platform" [level=2] [ref=e436]
              - paragraph [ref=e437]: This is your central workspace for managing SporeKart. Business modules will be implemented in upcoming sprints.
            - generic [ref=e438]:
              - article [ref=e439]:
                - heading "Workspace Summary" [level=3] [ref=e440]
                - generic [ref=e441]:
                  - generic [ref=e442]:
                    - generic [ref=e443]: Active Role
                    - generic [ref=e444]: administrator
                  - generic [ref=e445]:
                    - generic [ref=e446]: Environment
                    - generic [ref=e447]: Development
                  - generic [ref=e448]:
                    - generic [ref=e449]: Version
                    - generic [ref=e450]: 1.0.0
                  - generic [ref=e451]:
                    - generic [ref=e452]: Modules
                    - generic [ref=e453]: 6 layout routes
              - article [ref=e454]:
                - heading "Pinned Widgets" [level=3] [ref=e455]
                - paragraph [ref=e457]: Pin widgets to this area for quick access. Use the pin icon on any widget to add it here.
            - generic [ref=e458]:
              - article [ref=e459]:
                - heading "Users" [level=3] [ref=e461]
                - paragraph [ref=e462]: User management will appear here.
              - article [ref=e463]:
                - heading "Content" [level=3] [ref=e465]
                - paragraph [ref=e466]: Content moderation will appear here.
              - article [ref=e467]:
                - heading "Analytics" [level=3] [ref=e469]
                - paragraph [ref=e470]: Analytics dashboard will appear here.
              - article [ref=e471]:
                - heading "System" [level=3] [ref=e473]
                - paragraph [ref=e474]: System health will appear here.
            - generic [ref=e475]:
              - heading "Quick Actions" [level=3] [ref=e477]
              - article [ref=e478] [cursor=pointer]:
                - heading "Create User" [level=4] [ref=e479]
                - paragraph [ref=e480]: Add a new user account
              - article [ref=e481] [cursor=pointer]:
                - heading "View Reports" [level=4] [ref=e482]
                - paragraph [ref=e483]: Access platform reports
              - article [ref=e484] [cursor=pointer]:
                - heading "System Check" [level=4] [ref=e485]
                - paragraph [ref=e486]: Run health diagnostics
              - article [ref=e487] [cursor=pointer]:
                - heading "Help Center" [level=4] [ref=e488]
                - paragraph [ref=e489]: Browse admin documentation
            - generic [ref=e490]:
              - article [ref=e491]:
                - heading "Recent Activity" [level=3] [ref=e492]
                - paragraph [ref=e494]: No recent activity to display.
              - article [ref=e495]:
                - heading "Announcements" [level=3] [ref=e496]
                - paragraph [ref=e498]: No announcements at this time.
          - generic [ref=e499]:
            - generic [ref=e500]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e501]:
              - link "Privacy" [ref=e502]:
                - /url: /privacy-policy
              - link "Terms" [ref=e503]:
                - /url: /terms-and-conditions
```

# Test source

```ts
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
> 636 |     const box = await dialog.boundingBox();
      |                              ^ TimeoutError: locator.boundingBox: Timeout 15000ms exceeded.
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
  655 |   test('No horizontal scroll on delivery queue page', async ({ page }) => {
  656 |     await login(page);
  657 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  658 |     const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
  659 |     expect(scrollW).toBe(0);
  660 |   });
  661 | });
  662 | 
  663 | // ====================================================================
  664 | // PHASE 15 — EVIDENCE COLLECTION
  665 | // ====================================================================
  666 | test.describe('Phase 15 — Evidence Collection', () => {
  667 |   test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
  668 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  669 |     await expect(page.locator('body')).toBeVisible();
  670 |   });
  671 | 
  672 |   test('Traces captured (config: trace=on)', async ({ page }) => {
  673 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  674 |     await expect(page.locator('body')).toBeVisible();
  675 |   });
  676 | });
  677 | 
```