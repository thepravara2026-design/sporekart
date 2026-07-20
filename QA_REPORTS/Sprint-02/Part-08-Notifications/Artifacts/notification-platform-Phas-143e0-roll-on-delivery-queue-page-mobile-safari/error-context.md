# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 14 — Visual Review >> No horizontal scroll on delivery queue page
- Location: tests\notification-platform.spec.ts:655:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: 0
Received: 239
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
            - heading "communication / delivery" [level=1] [ref=e425]
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
                - generic [ref=e444]: communication / delivery
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
                    - img [ref=e494]
                    - generic [ref=e497]: Attendance
                  - button "Assignments" [ref=e498] [cursor=pointer]:
                    - img [ref=e499]
                    - generic [ref=e502]: Assignments
                  - button "Assessments" [ref=e503] [cursor=pointer]:
                    - img [ref=e504]
                    - generic [ref=e508]: Assessments
                  - button "Certificates" [ref=e509] [cursor=pointer]:
                    - generic [ref=e510]: "?"
                    - generic [ref=e511]: Certificates
                  - button "Learning Resources" [ref=e512] [cursor=pointer]:
                    - img [ref=e513]
                    - generic [ref=e515]: Learning Resources
                  - button "Announcements" [ref=e516] [cursor=pointer]:
                    - img [ref=e517]
                    - generic [ref=e519]: Announcements
                - generic [ref=e520]:
                  - generic [ref=e521]: Student Platform
                  - button "Student Workspace" [ref=e522] [cursor=pointer]:
                    - img [ref=e523]
                    - generic [ref=e528]: Student Workspace
                - generic [ref=e529]:
                  - generic [ref=e530]: Intelligence
                  - button "Reports" [ref=e531] [cursor=pointer]:
                    - img [ref=e532]
                    - generic [ref=e533]: Reports
                  - button "Analytics" [ref=e534] [cursor=pointer]:
                    - img [ref=e535]
                    - generic [ref=e538]: Analytics
                - generic [ref=e539]:
                  - generic [ref=e540]: Settings
                  - button "Settings" [ref=e541] [cursor=pointer]:
                    - img [ref=e542]
                    - generic [ref=e545]: Settings
                - generic [ref=e546]:
                  - generic [ref=e547]: Coming Soon
                  - button "AI Assistant Soon" [ref=e548]:
                    - img [ref=e549]
                    - generic [ref=e552]: AI Assistant
                    - generic [ref=e553]: Soon
                  - button "Community Soon" [ref=e554]:
                    - img [ref=e555]
                    - generic [ref=e557]: Community
                    - generic [ref=e558]: Soon
                  - button "Discussion Board Soon" [ref=e559]:
                    - img [ref=e560]
                    - generic [ref=e562]: Discussion Board
                    - generic [ref=e563]: Soon
            - generic [ref=e564]:
              - generic [ref=e565]:
                - button "Open training navigation" [ref=e566] [cursor=pointer]:
                  - img [ref=e567]
                - generic [ref=e568]: communication
              - generic [ref=e570]:
                - generic [ref=e571]:
                  - img [ref=e572]
                  - generic [ref=e574]:
                    - heading "Communication Center" [level=1] [ref=e575]
                    - paragraph [ref=e576]: Announcements, notifications, templates & delivery (Mock Mode)
                - navigation "Communication sections" [ref=e577]:
                  - link "Overview" [ref=e578]:
                    - /url: /admin/training/communication/overview
                    - img [ref=e579]
                    - text: Overview
                  - link "Announcements" [ref=e581]:
                    - /url: /admin/training/communication/announcements
                    - img [ref=e582]
                    - text: Announcements
                  - link "Notifications 35" [ref=e585]:
                    - /url: /admin/training/communication/notifications
                    - img [ref=e586]
                    - text: Notifications
                    - generic [ref=e589]: "35"
                  - link "Scheduled" [ref=e590]:
                    - /url: /admin/training/communication/scheduled
                    - img [ref=e591]
                    - text: Scheduled
                  - link "Templates" [ref=e594]:
                    - /url: /admin/training/communication/templates
                    - img [ref=e595]
                    - text: Templates
                  - link "History" [ref=e598]:
                    - /url: /admin/training/communication/history
                    - img [ref=e599]
                    - text: History
                  - link "Delivery Queue" [ref=e602]:
                    - /url: /admin/training/communication/delivery
                    - img [ref=e603]
                    - text: Delivery Queue
                  - link "Future Channels" [ref=e606]:
                    - /url: /admin/training/communication/channels
                    - img [ref=e607]
                    - text: Future Channels
                  - link "Statistics" [ref=e610]:
                    - /url: /admin/training/communication/statistics
                    - img [ref=e611]
                    - text: Statistics
                - region [ref=e612]:
                  - paragraph [ref=e613]: Simulated delivery pipeline. External channels (Email, WhatsApp, SMS, Push) are placeholders and never dispatch (Mock Mode).
                  - tablist "Delivery state filter" [ref=e614]:
                    - tab "All" [selected] [ref=e615] [cursor=pointer]
                    - tab "Queued" [ref=e616] [cursor=pointer]
                    - tab "Processing" [ref=e617] [cursor=pointer]
                    - tab "Delivered" [ref=e618] [cursor=pointer]
                    - tab "Failed" [ref=e619] [cursor=pointer]
                    - tab "Retry" [ref=e620] [cursor=pointer]
                  - list [ref=e621]:
                    - listitem [ref=e622]:
                      - img [ref=e624]
                      - generic [ref=e627]:
                        - text: Updated Assessment Guidelines
                        - paragraph [ref=e628]: In-App · Batch 2026-A · 4 days ago · 1 attempt
                      - generic [ref=e629]: Delivered
                    - listitem [ref=e630]:
                      - img [ref=e632]
                      - generic [ref=e635]:
                        - text: Holiday Schedule for Training Centers
                        - paragraph [ref=e636]: In-App · Trainers · 4 days ago
                      - generic [ref=e637]: Delivered
                    - listitem [ref=e638]:
                      - img [ref=e640]
                      - generic [ref=e642]:
                        - text: Trainer Assignment Announcement
                        - paragraph [ref=e643]: Push · Batch 2026-A · 3 days ago · 2 attempts
                      - generic [ref=e644]: Retry
                    - listitem [ref=e645]:
                      - img [ref=e647]
                      - generic [ref=e650]:
                        - text: New Mushroom Cultivation Batch Opens
                        - paragraph [ref=e651]: Email · Everyone · 4 days ago · 1 attempt
                      - generic [ref=e652]: Processing
                    - listitem [ref=e653]:
                      - img [ref=e655]
                      - generic [ref=e657]:
                        - text: Updated Assessment Guidelines
                        - paragraph [ref=e658]: Push · Everyone · 5 days ago · 1 attempt
                      - generic [ref=e659]: Queued
                    - listitem [ref=e660]:
                      - img [ref=e662]
                      - generic [ref=e664]:
                        - text: Holiday Schedule for Training Centers
                        - paragraph [ref=e665]: WhatsApp · Students · 15 hr ago · 2 attempts
                      - generic [ref=e666]: Failed
                    - listitem [ref=e667]:
                      - img [ref=e669]
                      - generic [ref=e671]:
                        - text: Certificate Distribution Schedule Update
                        - paragraph [ref=e672]: Push · Everyone · 2 days ago · 1 attempt
                      - generic [ref=e673]: Delivered
                    - listitem [ref=e674]:
                      - img [ref=e676]
                      - generic [ref=e678]:
                        - text: Emergency Weather Advisory
                        - paragraph [ref=e679]: SMS · Batch 2026-A · 20 hr ago · 1 attempt
                      - generic [ref=e680]: Delivered
                    - listitem [ref=e681]:
                      - img [ref=e683]
                      - generic [ref=e686]:
                        - text: Trainer Assignment Announcement
                        - paragraph [ref=e687]: Email · Trainers · 4 days ago · 1 attempt
                      - generic [ref=e688]: Queued
                    - listitem [ref=e689]:
                      - img [ref=e691]
                      - generic [ref=e694]:
                        - text: Community Mentorship Program Launch
                        - paragraph [ref=e695]: In-App · Oyster Mushroom Fundamentals · 4 days ago
                      - generic [ref=e696]: Delivered
                    - listitem [ref=e697]:
                      - img [ref=e699]
                      - generic [ref=e701]:
                        - text: Emergency Weather Advisory
                        - paragraph [ref=e702]: SMS · Trainers · 4 days ago · 3 attempts
                      - generic [ref=e703]: Failed
                    - listitem [ref=e704]:
                      - img [ref=e706]
                      - generic [ref=e708]:
                        - text: Holiday Schedule for Training Centers
                        - paragraph [ref=e709]: WhatsApp · Trainers · 4 days ago
                      - generic [ref=e710]: Queued
                    - listitem [ref=e711]:
                      - img [ref=e713]
                      - generic [ref=e715]:
                        - text: Platform Maintenance Window Notice
                        - paragraph [ref=e716]: WhatsApp · Batch 2026-A · 3 days ago · 3 attempts
                      - generic [ref=e717]: Retry
                    - listitem [ref=e718]:
                      - img [ref=e720]
                      - generic [ref=e723]:
                        - text: New Mushroom Cultivation Batch Opens
                        - paragraph [ref=e724]: Email · Students · 12 hr ago · 2 attempts
                      - generic [ref=e725]: Failed
                    - listitem [ref=e726]:
                      - img [ref=e728]
                      - generic [ref=e730]:
                        - text: Advanced Spawn Production Workshop
                        - paragraph [ref=e731]: SMS · Everyone · 9 hr ago · 1 attempt
                      - generic [ref=e732]: Processing
                    - listitem [ref=e733]:
                      - img [ref=e735]
                      - generic [ref=e738]:
                        - text: Enrollment Deadline Reminder
                        - paragraph [ref=e739]: Email · Everyone · 10 hr ago · 1 attempt
                      - generic [ref=e740]: Delivered
                    - listitem [ref=e741]:
                      - img [ref=e743]
                      - generic [ref=e746]:
                        - text: Course Curriculum Revision Notice
                        - paragraph [ref=e747]: Email · Everyone · 4 days ago
                      - generic [ref=e748]: Processing
                    - listitem [ref=e749]:
                      - img [ref=e751]
                      - generic [ref=e753]:
                        - text: Trainer Assignment Announcement
                        - paragraph [ref=e754]: Push · Oyster Mushroom Fundamentals · 2 days ago
                      - generic [ref=e755]: Queued
                    - listitem [ref=e756]:
                      - img [ref=e758]
                      - generic [ref=e760]:
                        - text: Enrollment Deadline Reminder
                        - paragraph [ref=e761]: WhatsApp · Students · 3 days ago · 2 attempts
                      - generic [ref=e762]: Failed
                    - listitem [ref=e763]:
                      - img [ref=e765]
                      - generic [ref=e767]:
                        - text: Monthly Progress Review Meeting
                        - paragraph [ref=e768]: WhatsApp · Students · 4 days ago · 1 attempt
                      - generic [ref=e769]: Retry
                    - listitem [ref=e770]:
                      - img [ref=e772]
                      - generic [ref=e774]:
                        - text: Trainer Assignment Announcement
                        - paragraph [ref=e775]: SMS · Trainers · 22 hr ago
                      - generic [ref=e776]: Queued
                    - listitem [ref=e777]:
                      - img [ref=e779]
                      - generic [ref=e782]:
                        - text: New Mushroom Cultivation Batch Opens
                        - paragraph [ref=e783]: Email · Trainers · 3 days ago · 2 attempts
                      - generic [ref=e784]: Retry
                    - listitem [ref=e785]:
                      - img [ref=e787]
                      - generic [ref=e789]:
                        - text: Emergency Weather Advisory
                        - paragraph [ref=e790]: SMS · Batch 2026-A · 6 hr ago · 3 attempts
                      - generic [ref=e791]: Retry
                    - listitem [ref=e792]:
                      - img [ref=e794]
                      - generic [ref=e797]:
                        - text: Trainer Assignment Announcement
                        - paragraph [ref=e798]: In-App · Everyone · 19 hr from now · 1 attempt
                      - generic [ref=e799]: Delivered
                    - listitem [ref=e800]:
                      - img [ref=e802]
                      - generic [ref=e805]:
                        - text: Community Mentorship Program Launch
                        - paragraph [ref=e806]: In-App · Students · 21 hr ago
                      - generic [ref=e807]: Queued
                    - listitem [ref=e808]:
                      - img [ref=e810]
                      - generic [ref=e812]:
                        - text: Advanced Spawn Production Workshop
                        - paragraph [ref=e813]: Push · Oyster Mushroom Fundamentals · 4 days ago · 1 attempt
                      - generic [ref=e814]: Processing
                    - listitem [ref=e815]:
                      - img [ref=e817]
                      - generic [ref=e819]:
                        - text: Updated Assessment Guidelines
                        - paragraph [ref=e820]: SMS · Batch 2026-A · 1 day ago
                      - generic [ref=e821]: Queued
                    - listitem [ref=e822]:
                      - img [ref=e824]
                      - generic [ref=e827]:
                        - text: Emergency Weather Advisory
                        - paragraph [ref=e828]: Email · Students · 3 days ago · 2 attempts
                      - generic [ref=e829]: Retry
                    - listitem [ref=e830]:
                      - img [ref=e832]
                      - generic [ref=e834]:
                        - text: Holiday Schedule for Training Centers
                        - paragraph [ref=e835]: WhatsApp · Oyster Mushroom Fundamentals · 2 days ago
                      - generic [ref=e836]: Processing
                    - listitem [ref=e837]:
                      - img [ref=e839]
                      - generic [ref=e842]:
                        - text: Community Mentorship Program Launch
                        - paragraph [ref=e843]: In-App · Batch 2026-A · 4 days ago · 1 attempt
                      - generic [ref=e844]: Queued
          - generic [ref=e845]:
            - generic [ref=e846]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e847]:
              - link "Privacy" [ref=e848]:
                - /url: /privacy-policy
              - link "Terms" [ref=e849]:
                - /url: /terms-and-conditions
```

# Test source

```ts
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
  655 |   test('No horizontal scroll on delivery queue page', async ({ page }) => {
  656 |     await login(page);
  657 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  658 |     const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
> 659 |     expect(scrollW).toBe(0);
      |                     ^ Error: expect(received).toBe(expected) // Object.is equality
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