# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 6 — Learner Dashboard >> Learner dashboard has continue learning
- Location: tests\training-platform.spec.ts:265:7

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
            - heading "Training" [level=1] [ref=e122]
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
                - generic [ref=e134]: Training
          - generic [ref=e136]:
            - generic [ref=e137]:
              - heading "Grower Training Center" [level=2] [ref=e138]
              - paragraph [ref=e139]: Scale up your mushroom cultivation yields and master sterile laboratory techniques.
            - generic [ref=e140]:
              - article [ref=e141]:
                - img [ref=e143]
                - generic [ref=e146]:
                  - generic [ref=e147]: Enrolled Courses
                  - strong [ref=e148]: "2"
              - article [ref=e149]:
                - img [ref=e151]
                - generic [ref=e154]:
                  - generic [ref=e155]: Completed Courses
                  - strong [ref=e156]: "1"
              - article [ref=e157]:
                - generic [ref=e159]: "?"
                - generic [ref=e160]:
                  - generic [ref=e161]: Earned Certificates
                  - strong [ref=e162]: "1"
              - article [ref=e163]:
                - img [ref=e165]
                - generic [ref=e168]:
                  - generic [ref=e169]: Training Hours Logged
                  - strong [ref=e170]: 10.5h
            - generic [ref=e171]:
              - generic [ref=e172]:
                - article [ref=e173]:
                  - generic [ref=e174]: CONTINUE LEARNING
                  - heading "Sterile Lab Techniques & Agar Work" [level=3] [ref=e175]
                  - paragraph [ref=e176]:
                    - text: "Active Module:"
                    - strong [ref=e177]: "Module 2: Agar Formulations & Pouring"
                    - text: "· Current Lesson:"
                    - emphasis [ref=e178]: Sterile Pouring Techniques
                  - generic [ref=e180]:
                    - generic [ref=e181]: Syllabus Completion
                    - generic [ref=e182]: 60% Completed
                  - generic [ref=e185]:
                    - button "Resume Lecture" [ref=e186]
                    - button "View Course Details" [ref=e187]
                - generic [ref=e188]:
                  - heading "Training Catalog Options" [level=3] [ref=e189]
                  - generic [ref=e190]:
                    - button "Browse Courses Find sterile, spawn, and culture modules." [ref=e191] [cursor=pointer]:
                      - generic [ref=e192]:
                        - img [ref=e194]
                        - generic [ref=e197]:
                          - heading "Browse Courses" [level=4] [ref=e198]
                          - paragraph [ref=e199]: Find sterile, spawn, and culture modules.
                    - button "My Enrolled Courses Check current syllabus progress." [ref=e200] [cursor=pointer]:
                      - generic [ref=e201]:
                        - generic [ref=e203]: "?"
                        - generic [ref=e204]:
                          - heading "My Enrolled Courses" [level=4] [ref=e205]
                          - paragraph [ref=e206]: Check current syllabus progress.
                    - button "Earned Certificates Download PDF credentials." [ref=e207] [cursor=pointer]:
                      - generic [ref=e208]:
                        - generic [ref=e210]: "?"
                        - generic [ref=e211]:
                          - heading "Earned Certificates" [level=4] [ref=e212]
                          - paragraph [ref=e213]: Download PDF credentials.
                    - button "Live Webinar Schedule Book interactive Q&A seats." [ref=e214] [cursor=pointer]:
                      - generic [ref=e215]:
                        - img [ref=e217]
                        - generic [ref=e219]:
                          - heading "Live Webinar Schedule" [level=4] [ref=e220]
                          - paragraph [ref=e221]: Book interactive Q&A seats.
              - generic [ref=e222]:
                - article [ref=e223]:
                  - heading "Live Training Schedule" [level=4] [ref=e224]
                  - generic [ref=e225]:
                    - generic [ref=e226]:
                      - generic [ref=e227]: 2026-07-18 · 4:00 PM IST
                      - strong [ref=e228]: "Live Lab Q&A: Troubleshooting Contamination"
                      - generic [ref=e229]: "Trainer: Dr. Anita Rao"
                      - button "Book Seat" [ref=e230]
                    - generic [ref=e231]:
                      - generic [ref=e232]: 2026-07-22 · 11:00 AM IST
                      - strong [ref=e233]: "Commercial Scale-Up: Bags vs. Bottle Cultivation"
                      - generic [ref=e234]: "Trainer: Vijay Kumar"
                      - generic [ref=e235]: ✓ REGISTERED
                - article [ref=e236]:
                  - generic [ref=e237]:
                    - generic [ref=e238]: 🤖
                    - generic [ref=e239]:
                      - heading "AI Learning Assistant" [level=5] [ref=e240]
                      - paragraph [ref=e241]:
                        - text: Based on your purchase history of
                        - strong [ref=e242]: Pink Oyster Grain Spawn
                        - text: ", we recommend enrolling in"
                        - strong [ref=e243]: Commercial Monotub Cultivation
                        - text: to optimize substrate mixes!
                      - button "Explore Course" [ref=e244]
          - generic [ref=e245]:
            - generic [ref=e246]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e247]:
              - link "Privacy" [ref=e248]:
                - /url: /privacy-policy
              - link "Terms" [ref=e249]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e250]:
                - /url: /support
```

# Test source

```ts
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
> 269 |     expect(t.includes('Continue') || t.includes('Resume') || t.includes('Progress')).toBeTruthy();
      |                                                                                      ^ Error: expect(received).toBeTruthy()
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
```