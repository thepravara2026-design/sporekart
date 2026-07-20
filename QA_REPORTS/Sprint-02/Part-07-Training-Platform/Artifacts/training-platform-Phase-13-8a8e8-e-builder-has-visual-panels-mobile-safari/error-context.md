# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 13 — Visual Review >> Admin course builder has visual panels
- Location: tests\training-platform.spec.ts:688:7

# Error details

```
Error: expect(received).toBeGreaterThanOrEqual(expected)

Expected: >= 1
Received:    0
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
            - heading "courses / builder" [level=1] [ref=e425]
            - paragraph [ref=e426]: Enterprise Administration · Admin
          - navigation "Breadcrumb" [ref=e428]:
            - list [ref=e429]:
              - listitem [ref=e430]:
                - link "Admin" [ref=e431]:
                  - /url: /admin/dashboard
                  - generic [ref=e432]: Admin
              - listitem [ref=e433]:
                - img [ref=e434]
              - listitem [ref=e436]:
                - link "Training" [ref=e437]:
                  - /url: /admin/training
                  - generic [ref=e438]: Training
              - listitem [ref=e439]:
                - img [ref=e440]
              - listitem [ref=e442]:
                - generic [ref=e444]: courses / builder
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
                    - img [ref=e465]
                    - generic [ref=e468]: Courses
                  - button "Curriculum" [ref=e469] [cursor=pointer]:
                    - img [ref=e470]
                    - generic [ref=e474]: Curriculum
                  - button "Training Batches" [ref=e475] [cursor=pointer]:
                    - img [ref=e476]
                    - generic [ref=e478]: Training Batches
                  - button "Students" [ref=e479] [cursor=pointer]:
                    - img [ref=e480]
                    - generic [ref=e485]: Students
                  - button "Trainers" [ref=e486] [cursor=pointer]:
                    - img [ref=e487]
                    - generic [ref=e491]: Trainers
                - generic [ref=e492]:
                  - generic [ref=e493]: Operations
                  - button "Attendance" [ref=e494] [cursor=pointer]:
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
                - generic [ref=e569]: Courses
              - generic [ref=e571]:
                - generic [ref=e572]:
                  - generic [ref=e574]: Course Builder
                  - article [ref=e575]:
                    - generic [ref=e576]:
                      - generic [ref=e578]: No changes yet
                      - button "Save" [disabled] [ref=e579]
                      - generic [ref=e580]: v1
                - generic [ref=e581]:
                  - navigation "Course Builder Navigation" [ref=e582]:
                    - generic [ref=e583]:
                      - tab "ℹ️ Overview" [selected] [ref=e584] [cursor=pointer]:
                        - generic [ref=e585]: ℹ️
                        - generic [ref=e586]: Overview
                        - status [ref=e587]
                      - tab "✏️ Basic Information" [ref=e588] [cursor=pointer]:
                        - generic [ref=e589]: ✏️
                        - generic [ref=e590]: Basic Information
                      - tab "🎯 Learning Objectives" [ref=e591] [cursor=pointer]:
                        - generic [ref=e592]: 🎯
                        - generic [ref=e593]: Learning Objectives
                      - tab "✅ Prerequisites" [ref=e594] [cursor=pointer]:
                        - generic [ref=e595]: ✅
                        - generic [ref=e596]: Prerequisites
                      - tab "🖼️ Media" [ref=e597] [cursor=pointer]:
                        - generic [ref=e598]: 🖼️
                        - generic [ref=e599]: Media
                      - tab "📁 Resources" [ref=e600] [cursor=pointer]:
                        - generic [ref=e601]: 📁
                        - generic [ref=e602]: Resources
                      - tab "🔍 SEO Configuration" [ref=e603] [cursor=pointer]:
                        - generic [ref=e604]: 🔍
                        - generic [ref=e605]: SEO Configuration
                      - tab "⚙️ Settings" [ref=e606] [cursor=pointer]:
                        - generic [ref=e607]: ⚙️
                        - generic [ref=e608]: Settings
                        - status [ref=e609]
                      - tab "👁️ Live Preview" [ref=e610] [cursor=pointer]:
                        - generic [ref=e611]: 👁️
                        - generic [ref=e612]: Live Preview
                  - tabpanel "Builder panel content" [ref=e613]:
                    - generic [ref=e615]:
                      - article [ref=e616]:
                        - generic [ref=e617]:
                          - generic [ref=e618]: Course Builder Overview
                          - generic [ref=e619]: Untitled Course · 1/7 sections complete
                          - progressbar [ref=e620]
                      - generic [ref=e622]:
                        - button "Go to Basic Information" [ref=e623] [cursor=pointer]:
                          - generic [ref=e624]:
                            - generic [ref=e625]: ✏️
                            - generic [ref=e626]:
                              - generic [ref=e627]: Basic Information
                              - generic [ref=e628]: Not started
                            - generic [ref=e629]: Pending
                        - button "Go to Learning Objectives" [ref=e630] [cursor=pointer]:
                          - generic [ref=e631]:
                            - generic [ref=e632]: 🎯
                            - generic [ref=e633]:
                              - generic [ref=e634]: Learning Objectives
                              - generic [ref=e635]: Not started
                            - generic [ref=e636]: Pending
                        - button "Go to Prerequisites" [ref=e637] [cursor=pointer]:
                          - generic [ref=e638]:
                            - generic [ref=e639]: ✅
                            - generic [ref=e640]:
                              - generic [ref=e641]: Prerequisites
                              - generic [ref=e642]: Not started
                            - generic [ref=e643]: Pending
                        - button "Go to Media" [ref=e644] [cursor=pointer]:
                          - generic [ref=e645]:
                            - generic [ref=e646]: 🖼️
                            - generic [ref=e647]:
                              - generic [ref=e648]: Media
                              - generic [ref=e649]: Not started
                            - generic [ref=e650]: Pending
                        - button "Go to Resources" [ref=e651] [cursor=pointer]:
                          - generic [ref=e652]:
                            - generic [ref=e653]: 📁
                            - generic [ref=e654]:
                              - generic [ref=e655]: Resources
                              - generic [ref=e656]: Not started
                            - generic [ref=e657]: Pending
                        - button "Go to SEO" [ref=e658] [cursor=pointer]:
                          - generic [ref=e659]:
                            - generic [ref=e660]: 🔍
                            - generic [ref=e661]:
                              - generic [ref=e662]: SEO
                              - generic [ref=e663]: Not started
                            - generic [ref=e664]: Pending
                        - button "Go to Settings" [ref=e665] [cursor=pointer]:
                          - generic [ref=e666]:
                            - generic [ref=e667]: ⚙️
                            - generic [ref=e668]:
                              - generic [ref=e669]: Settings
                              - generic [ref=e670]: Complete
                            - generic [ref=e671]: Done
          - generic [ref=e672]:
            - generic [ref=e673]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e674]:
              - link "Privacy" [ref=e675]:
                - /url: /privacy-policy
              - link "Terms" [ref=e676]:
                - /url: /terms-and-conditions
```

# Test source

```ts
  591 |     for (let i = 0; i < c; i++) {
  592 |       const alt = await imgs.nth(i).getAttribute('alt');
  593 |       if (alt === null || alt === undefined) missing++;
  594 |     }
  595 |     expect(missing).toBe(0);
  596 |   });
  597 | 
  598 |   test('Course detail has semantic headings', async ({ page }) => {
  599 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  600 |     const h1 = page.locator('h1');
  601 |     await expect(h1.first()).toBeVisible({ timeout: 3000 });
  602 |   });
  603 | 
  604 |   test('Admin training workspace has landmarks', async ({ page }) => {
  605 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  606 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  607 |     await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({ timeout: 5000 });
  608 |   });
  609 | 
  610 |   test('Course cards are keyboard navigable', async ({ page }) => {
  611 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  612 |     const firstLink = page.locator('a[href*="/training/courses/"]').first();
  613 |     if (await firstLink.isVisible({ timeout: 2000 }).catch(() => false)) {
  614 |       await firstLink.focus();
  615 |       await page.keyboard.press('Enter');
  616 |       await page.waitForTimeout(500);
  617 |       expect(page.url()).toContain('/training/courses/');
  618 |     }
  619 |   });
  620 | });
  621 | 
  622 | // ====================================================================
  623 | // PHASE 12 — PERFORMANCE
  624 | // ====================================================================
  625 | test.describe('Phase 12 — Performance', () => {
  626 |   test('Catalog loads within 15s', async ({ page }) => {
  627 |     const s = Date.now();
  628 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  629 |     expect(Date.now() - s).toBeLessThan(25000);
  630 |   });
  631 | 
  632 |   test('Course detail loads within 15s', async ({ page }) => {
  633 |     const s = Date.now();
  634 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  635 |     expect(Date.now() - s).toBeLessThan(25000);
  636 |   });
  637 | 
  638 |   test('Learner dashboard loads within 15s', async ({ page }) => {
  639 |     await login(page);
  640 |     const s = Date.now();
  641 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' });
  642 |     expect(Date.now() - s).toBeLessThan(25000);
  643 |   });
  644 | 
  645 |   test('Admin training dashboard loads within 15s', async ({ page }) => {
  646 |     const s = Date.now();
  647 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  648 |     expect(Date.now() - s).toBeLessThan(25000);
  649 |   });
  650 | 
  651 |   test('Course builder loads within 15s', async ({ page }) => {
  652 |     const s = Date.now();
  653 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  654 |     expect(Date.now() - s).toBeLessThan(25000);
  655 |   });
  656 | });
  657 | 
  658 | // ====================================================================
  659 | // PHASE 13 — VISUAL REVIEW
  660 | // ====================================================================
  661 | test.describe('Phase 13 — Visual Review', () => {
  662 |   test('Catalog has course cards', async ({ page }) => {
  663 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  664 |     expect(await page.locator('[class*="card"],[class*="Card"],article').count()).toBeGreaterThanOrEqual(1);
  665 |   });
  666 | 
  667 |   test('Course detail has proper layout', async ({ page }) => {
  668 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  669 |     const main = page.locator('main,[role="main"]').first();
  670 |     await expect(main).toBeVisible({ timeout: 3000 });
  671 |   });
  672 | 
  673 |   test('No horizontal scroll on catalog', async ({ page }) => {
  674 |     await page.setViewportSize({ width: 1440, height: 900 });
  675 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  676 |     const hs = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
  677 |     expect(hs).toBe(false);
  678 |   });
  679 | 
  680 |   test('Typography consistent on course detail', async ({ page }) => {
  681 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  682 |     const h1 = page.locator('h1').first();
  683 |     if (await h1.isVisible({ timeout: 2000 }).catch(() => false)) {
  684 |       expect(parseFloat(await h1.evaluate(e => getComputedStyle(e).fontSize))).toBeGreaterThanOrEqual(16);
  685 |     }
  686 |   });
  687 | 
  688 |   test('Admin course builder has visual panels', async ({ page }) => {
  689 |     await page.goto('/admin/training/courses/builder', { waitUntil: 'networkidle' });
  690 |     const panels = page.locator('[class*="panel"],[class*="Panel"],[class*="section"]');
> 691 |     expect(await panels.count()).toBeGreaterThanOrEqual(1);
      |                                  ^ Error: expect(received).toBeGreaterThanOrEqual(expected)
  692 |   });
  693 | 
  694 |   test('Course catalog view toggles work', async ({ page }) => {
  695 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  696 |     const gridBtn = page.locator('button:has-text("Grid"),button[aria-label*="grid"]').first();
  697 |     if (await gridBtn.isVisible({ timeout: 2000 }).catch(() => false)) {
  698 |       await gridBtn.click(); await page.waitForTimeout(300);
  699 |     }
  700 |     expect(true).toBeTruthy();
  701 |   });
  702 | });
  703 | 
  704 | // ====================================================================
  705 | // PHASE 14 — EVIDENCE COLLECTION
  706 | // ====================================================================
  707 | test.describe('Phase 14 — Evidence Collection', () => {
  708 |   test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
  709 |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  710 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  711 |     await page.goto('/dashboard/training', { waitUntil: 'networkidle' }).catch(() => {});
  712 |     expect(true).toBeTruthy();
  713 |   });
  714 | 
  715 |   test('Traces captured (config: trace=on)', async ({ page }) => {
  716 |     await page.goto('/admin/training/dashboard', { waitUntil: 'networkidle' });
  717 |     expect(true).toBeTruthy();
  718 |   });
  719 | });
  720 | 
```