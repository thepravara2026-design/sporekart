# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 5 — Batch Management >> IMPLEMENTATION GAP: No trainer-to-batch assignment
- Location: tests\training-platform.spec.ts:241:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: false
Received: true
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
            - heading "trainers" [level=1] [ref=e425]
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
                - generic [ref=e444]: trainers
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
                - generic [ref=e569]: Trainers
              - generic [ref=e571]:
                - img [ref=e573]
                - heading "Trainers" [level=2] [ref=e577]
                - paragraph [ref=e578]: Onboard and manage trainers — assign qualifications, track certifications, manage schedules, and evaluate performance.
          - generic [ref=e579]:
            - generic [ref=e580]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e581]:
              - link "Privacy" [ref=e582]:
                - /url: /privacy-policy
              - link "Terms" [ref=e583]:
                - /url: /terms-and-conditions
```

# Test source

```ts
  144 |     await expect(cta.first()).toBeVisible({ timeout: 3000 });
  145 |   });
  146 | 
  147 |   test('Course detail has FAQ', async ({ page }) => {
  148 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  149 |     const t = await bodyText(page);
  150 |     expect(t.includes('FAQ') || t.includes('Question')).toBeTruthy();
  151 |   });
  152 | 
  153 |   test('Course detail has related courses', async ({ page }) => {
  154 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  155 |     const t = await bodyText(page);
  156 |     expect(t.includes('Related') || t.includes('Similar')).toBeTruthy();
  157 |   });
  158 | 
  159 |   test('Course detail has SEO metadata', async ({ page }) => {
  160 |     const r = await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  161 |     const meta = page.locator('meta[name="description"]');
  162 |     expect(await meta.getAttribute('content').catch(() => '')).toBeTruthy();
  163 |   });
  164 | });
  165 | 
  166 | // ====================================================================
  167 | // PHASE 3 — ENROLLMENT / REGISTRATION
  168 | // ====================================================================
  169 | test.describe('Phase 3 — Registration', () => {
  170 |   test('Customer course detail with enroll button', async ({ page }) => {
  171 |     await login(page);
  172 |     await page.goto('/dashboard/training/course/crs-001', { waitUntil: 'networkidle' });
  173 |     const t = await bodyText(page);
  174 |     expect(t.includes('Enroll') || t.includes('Course')).toBeTruthy();
  175 |   });
  176 | 
  177 |   test('Enroll button triggers action', async ({ page }) => {
  178 |     await login(page);
  179 |     await page.goto('/dashboard/training/course/crs-001', { waitUntil: 'networkidle' });
  180 |     const enroll = page.locator('button:has-text("Enroll")').first();
  181 |     if (await enroll.isVisible({ timeout: 2000 }).catch(() => false)) {
  182 |       await enroll.click();
  183 |       await page.waitForTimeout(500);
  184 |     }
  185 |     expect(true).toBeTruthy();
  186 |   });
  187 | 
  188 |   test('IMPLEMENTATION GAP: No registration form', async ({ page }) => {
  189 |     await page.goto('/training/enroll', { waitUntil: 'networkidle' });
  190 |     const t = await bodyText(page);
  191 |     expect(t.includes('Enroll') || t.includes('Register')).toBe(false);
  192 |   });
  193 | 
  194 |   test('IMPLEMENTATION GAP: No enrollment form validation', async ({ page }) => {
  195 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  196 |     const inputs = page.locator('input:not([type="hidden"])');
  197 |     expect(await inputs.count()).toBe(0);
  198 |   });
  199 | });
  200 | 
  201 | // ====================================================================
  202 | // PHASE 4 — ELIGIBILITY & APPROVAL
  203 | // ====================================================================
  204 | test.describe('Phase 4 — Eligibility & Approval', () => {
  205 |   test('IMPLEMENTATION GAP: No eligibility check UI', async ({ page }) => {
  206 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  207 |     const t = await bodyText(page);
  208 |     expect(t.includes('Eligible') || t.includes('eligible')).toBe(false);
  209 |   });
  210 | 
  211 |   test('IMPLEMENTATION GAP: No approval workflow UI', async ({ page }) => {
  212 |     await page.goto('/dashboard/training/my-learning', { waitUntil: 'networkidle' });
  213 |     const t = await bodyText(page);
  214 |     expect(t.includes('Approval') || t.includes('approval') || t.includes('Pending')).toBe(false);
  215 |   });
  216 | 
  217 |   test('IMPLEMENTATION GAP: No waitlist mechanism', async ({ page }) => {
  218 |     await page.goto('/dashboard/training/schedule', { waitUntil: 'networkidle' });
  219 |     const t = await bodyText(page);
  220 |     expect(t.includes('Waitlist') || t.includes('waitlist')).toBe(false);
  221 |   });
  222 | });
  223 | 
  224 | // ====================================================================
  225 | // PHASE 5 — BATCH MANAGEMENT
  226 | // ====================================================================
  227 | test.describe('Phase 5 — Batch Management', () => {
  228 |   test('IMPLEMENTATION GAP: No training batch assignment in admin', async ({ page }) => {
  229 |     const r = await page.goto('/admin/training/batches', { waitUntil: 'networkidle' });
  230 |     expect(r?.status()).toBeLessThan(400);
  231 |     const t = await bodyText(page);
  232 |     expect(t.includes('Create Batch') || t.includes('Batch Assignment')).toBe(false);
  233 |   });
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
> 244 |     expect(t.includes('Trainer') || t.includes('trainer')).toBe(false);
      |                                                            ^ Error: expect(received).toBe(expected) // Object.is equality
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
```