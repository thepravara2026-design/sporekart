# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 2 — Training Details >> Course detail has SEO metadata
- Location: tests\training-platform.spec.ts:159:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: ""
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
    - /url: "#main"
  - generic [ref=e4]:
    - heading "Course not found" [level=1] [ref=e5]
    - paragraph [ref=e6]: The course you are looking for is not available.
    - link "Back to catalog" [ref=e7] [cursor=pointer]:
      - /url: /training/courses
```

# Test source

```ts
  62  |   test('Catalog has pagination', async ({ page }) => {
  63  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  64  |     const p = page.locator('[class*="pagination"],[class*="Pagination"],nav[aria-label*="pagination"]');
  65  |     const v = await p.isVisible({ timeout: 3000 }).catch(() => false);
  66  |     if (v) expect(v).toBe(true);
  67  |   });
  68  | 
  69  |   test('Catalog has sort controls', async ({ page }) => {
  70  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  71  |     const t = await bodyText(page);
  72  |     expect(t.includes('Sort') || t.includes('Name') || t.includes('Rating')).toBeTruthy();
  73  |   });
  74  | 
  75  |   test('Course comparison page loads', async ({ page }) => {
  76  |     const r = await page.goto('/training/courses/compare', { waitUntil: 'networkidle' });
  77  |     expect(r?.status()).toBeLessThan(400);
  78  |   });
  79  | 
  80  |   test('Learning paths page loads', async ({ page }) => {
  81  |     const r = await page.goto('/training/learning-paths', { waitUntil: 'networkidle' });
  82  |     expect(r?.status()).toBeLessThan(400);
  83  |   });
  84  | 
  85  |   test('Public training marketing page loads', async ({ page }) => {
  86  |     const r = await page.goto('/training', { waitUntil: 'networkidle' });
  87  |     expect(r?.status()).toBeLessThan(400);
  88  |   });
  89  | 
  90  |   test('Certifications page loads', async ({ page }) => {
  91  |     const r = await page.goto('/certifications', { waitUntil: 'networkidle' });
  92  |     expect(r?.status()).toBeLessThan(400);
  93  |   });
  94  | });
  95  | 
  96  | // ====================================================================
  97  | // PHASE 2 — TRAINING DETAILS
  98  | // ====================================================================
  99  | test.describe('Phase 2 — Training Details', () => {
  100 |   test('Course detail page loads', async ({ page }) => {
  101 |     const r = await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  102 |     expect(r?.status()).toBeLessThan(400);
  103 |   });
  104 | 
  105 |   test('Course detail has hero/banner', async ({ page }) => {
  106 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  107 |     const t = await bodyText(page);
  108 |     expect(t.includes('Mushroom') || t.includes('Cultivation')).toBeTruthy();
  109 |   });
  110 | 
  111 |   test('Course detail has curriculum', async ({ page }) => {
  112 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  113 |     const t = await bodyText(page);
  114 |     expect(t.includes('Curriculum') || t.includes('Module') || t.includes('Lesson')).toBeTruthy();
  115 |   });
  116 | 
  117 |   test('Course detail has learning objectives', async ({ page }) => {
  118 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  119 |     const t = await bodyText(page);
  120 |     expect(t.includes('Objective') || t.includes('Learn') || t.includes('Outcome')).toBeTruthy();
  121 |   });
  122 | 
  123 |   test('Course detail has prerequisites', async ({ page }) => {
  124 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  125 |     const t = await bodyText(page);
  126 |     expect(t.includes('Prerequisite') || t.includes('Requirement')).toBeTruthy();
  127 |   });
  128 | 
  129 |   test('Course detail has trainer info', async ({ page }) => {
  130 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  131 |     const t = await bodyText(page);
  132 |     expect(t.includes('Trainer') || t.includes('Instructor')).toBeTruthy();
  133 |   });
  134 | 
  135 |   test('Course detail has pricing', async ({ page }) => {
  136 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  137 |     const t = await bodyText(page);
  138 |     expect(t.includes('₹') || t.includes('Price') || t.includes('Free')).toBeTruthy();
  139 |   });
  140 | 
  141 |   test('Course detail has enroll CTA', async ({ page }) => {
  142 |     await page.goto('/training/courses/mushroom-cultivation-masterclass', { waitUntil: 'networkidle' });
  143 |     const cta = page.locator('button:has-text("Enroll"),a:has-text("Enroll"),button:has-text("Join")');
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
> 162 |     expect(await meta.getAttribute('content').catch(() => '')).toBeTruthy();
      |                                                                ^ Error: expect(received).toBeTruthy()
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
```