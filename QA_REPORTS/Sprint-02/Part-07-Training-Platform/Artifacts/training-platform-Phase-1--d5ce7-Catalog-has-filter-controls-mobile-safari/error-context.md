# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: training-platform.spec.ts >> Phase 1 — Training Discovery >> Catalog has filter controls
- Location: tests\training-platform.spec.ts:44:7

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
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const BASE = 'http://localhost:5174';
  4   | 
  5   | async function login(page) {
  6   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  7   |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  8   |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  9   |   await inp.fill('9876543210');
  10  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  11  |   await btn.click(); await page.waitForTimeout(2000);
  12  |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  13  |   const n = await otp.count();
  14  |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  15  | }
  16  | 
  17  | async function bodyText(page) { return (await page.locator('body').innerText()); }
  18  | async function hasText(page, t) { return (await bodyText(page)).includes(t); }
  19  | 
  20  | // ====================================================================
  21  | // PHASE 1 — TRAINING DISCOVERY (PUBLIC CATALOG)
  22  | // ====================================================================
  23  | test.describe('Phase 1 — Training Discovery', () => {
  24  |   test('Course catalog page loads', async ({ page }) => {
  25  |     const r = await page.goto('/training/courses', { waitUntil: 'networkidle' });
  26  |     expect(r?.status()).toBeLessThan(400);
  27  |   });
  28  | 
  29  |   test('Catalog has course cards', async ({ page }) => {
  30  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  31  |     const cards = page.locator('[class*="card"],[class*="Card"],[class*="course"],article').first();
  32  |     await expect(cards).toBeVisible({ timeout: 5000 });
  33  |   });
  34  | 
  35  |   test('Catalog has search input', async ({ page }) => {
  36  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  37  |     const s = page.locator('input[type="search"],input[placeholder*="Search"]').first();
  38  |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  39  |       await s.fill('mushroom'); await page.waitForTimeout(300);
  40  |     }
  41  |     expect(true).toBeTruthy();
  42  |   });
  43  | 
  44  |   test('Catalog has filter controls', async ({ page }) => {
  45  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  46  |     const t = await bodyText(page);
> 47  |     expect(t.includes('Filter') || t.includes('Category') || t.includes('Level')).toBeTruthy();
      |                                                                                   ^ Error: expect(received).toBeTruthy()
  48  |   });
  49  | 
  50  |   test('Catalog has category highlights', async ({ page }) => {
  51  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  52  |     const t = await bodyText(page);
  53  |     expect(t.includes('Mushroom') || t.includes('Cultivation') || t.includes('Course')).toBeTruthy();
  54  |   });
  55  | 
  56  |   test('Catalog has grid/list/table view toggles', async ({ page }) => {
  57  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  58  |     const t = await bodyText(page);
  59  |     expect(t.includes('Grid') || t.includes('List') || t.includes('Table')).toBeTruthy();
  60  |   });
  61  | 
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
```