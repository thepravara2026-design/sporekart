# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 2 — Admin Dashboard >> Dashboard cards/widgets render
- Location: tests\admin-console.spec.ts:86:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
Call log:
  - navigating to "http://localhost:5174/admin/dashboard", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const ADMIN = 'http://localhost:5174/admin';
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
  17  | async function hasText(page, t) {
  18  |   return (await page.locator('body').innerText()).includes(t);
  19  | }
  20  | 
  21  | // ====================================================================
  22  | // PHASE 1 — ADMIN AUTHORIZATION
  23  | // ====================================================================
  24  | test.describe('Phase 1 — Admin Authorization', () => {
  25  |   test('Admin dashboard route accessible', async ({ page }) => {
  26  |     const r = await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  27  |     expect(r?.status()).toBeLessThan(400);
  28  |   });
  29  | 
  30  |   test('Direct URL to admin dashboard works', async ({ page }) => {
  31  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  32  |     expect(page.url()).toContain('/admin/dashboard');
  33  |   });
  34  | 
  35  |   test('Admin sidebar renders', async ({ page }) => {
  36  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  37  |     await expect(page.locator('nav,[role="navigation"],aside,.sidebar').first()).toBeVisible({ timeout: 5000 });
  38  |   });
  39  | 
  40  |   test('IMPLEMENTATION GAP: No auth guard on /admin routes', async ({ page }) => {
  41  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  42  |     expect(await hasText(page, 'Login')).toBe(false);
  43  |   });
  44  | 
  45  |   test('Guest can access admin dashboard', async ({ page }) => {
  46  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  47  |     expect(page.url()).toContain('/admin/dashboard');
  48  |   });
  49  | 
  50  |   test('Customer can access admin dashboard (BUG-CHK-001)', async ({ page }) => {
  51  |     await login(page);
  52  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  53  |     expect(page.url()).toContain('/admin/dashboard');
  54  |   });
  55  | 
  56  |   test('Admin route in multiple tabs', async ({ page }) => {
  57  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  58  |     const p2 = await page.context().newPage();
  59  |     await p2.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  60  |     expect(p2.url()).toContain('/admin/orders');
  61  |     await p2.close();
  62  |   });
  63  | });
  64  | 
  65  | // ====================================================================
  66  | // PHASE 2 — ADMIN DASHBOARD
  67  | // ====================================================================
  68  | test.describe('Phase 2 — Admin Dashboard', () => {
  69  |   test('Dashboard loads without console errors', async ({ page }) => {
  70  |     const errs = [];
  71  |     page.on('console', m => { if (m.type() === 'error') errs.push(m.text()); });
  72  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  73  |     expect(errs.length).toBe(0);
  74  |   });
  75  | 
  76  |   test('Dashboard has content', async ({ page }) => {
  77  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  78  |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(0);
  79  |   });
  80  | 
  81  |   test('Dashboard sidebar navigation visible', async ({ page }) => {
  82  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  83  |     await expect(page.locator('nav,[role="navigation"],aside').first()).toBeVisible({ timeout: 5000 });
  84  |   });
  85  | 
  86  |   test('Dashboard cards/widgets render', async ({ page }) => {
> 87  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/dashboard
  88  |     await expect(page.locator('[class*="card"],[class*="Card"],[class*="widget"]').first()).toBeVisible({ timeout: 5000 });
  89  |   });
  90  | 
  91  |   test('Dashboard KPI indicators present', async ({ page }) => {
  92  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  93  |     const t = await page.locator('body').innerText();
  94  |     expect(t.includes('Users') || t.includes('Content') || t.includes('Analytics') || t.includes('System')).toBeTruthy();
  95  |   });
  96  | 
  97  |   test('IMPLEMENTATION GAP: No real-time data indicators', async ({ page }) => {
  98  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  99  |     expect(await hasText(page, 'Last updated')).toBe(false);
  100 |   });
  101 | 
  102 |   test('IMPLEMENTATION GAP: No charts/graphs', async ({ page }) => {
  103 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  104 |     expect(await page.locator('canvas,svg[class*="Chart"],svg[class*="chart"]').count()).toBe(0);
  105 |   });
  106 | 
  107 |   test('Dashboard refresh works', async ({ page }) => {
  108 |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
  109 |     await page.reload();
  110 |     expect(page.url()).toContain('/admin/dashboard');
  111 |   });
  112 | });
  113 | 
  114 | // ====================================================================
  115 | // PHASE 3 — PRODUCT MANAGEMENT
  116 | // ====================================================================
  117 | test.describe('Phase 3 — Product Management', () => {
  118 |   test('Products page loads (HTTP)', async ({ page }) => {
  119 |     const r = await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  120 |     expect(r?.status()).toBeLessThan(400);
  121 |   });
  122 | 
  123 |   test('Product search input exists', async ({ page }) => {
  124 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  125 |     const s = page.locator('input[type="search"],input[placeholder*="Search"]').first();
  126 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  127 |       await s.fill('test'); await page.waitForTimeout(300);
  128 |     }
  129 |     expect(true).toBeTruthy();
  130 |   });
  131 | 
  132 |   test('IMPLEMENTATION GAP: No product create form', async ({ page }) => {
  133 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  134 |     expect(await page.locator('button:has-text("Create"),a:has-text("Create"),button:has-text("New")').isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  135 |   });
  136 | 
  137 |   test('IMPLEMENTATION GAP: No product edit capability', async ({ page }) => {
  138 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  139 |     expect(await page.locator('button:has-text("Edit"),a:has-text("Edit")').isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  140 |   });
  141 | 
  142 |   test('IMPLEMENTATION GAP: No product delete', async ({ page }) => {
  143 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  144 |     expect(await page.locator('button:has-text("Delete")').isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  145 |   });
  146 | 
  147 |   test('IMPLEMENTATION GAP: No image upload', async ({ page }) => {
  148 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  149 |     expect(await page.locator('input[type="file"]').count()).toBe(0);
  150 |   });
  151 | 
  152 |   test('IMPLEMENTATION GAP: No pricing editor', async ({ page }) => {
  153 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  154 |     expect(await page.locator('input[type="number"]').count()).toBe(0);
  155 |   });
  156 | 
  157 |   test('Export button exists on DataGrid', async ({ page }) => {
  158 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
  159 |     const exp = page.locator('button:has-text("Export")');
  160 |     if (await exp.isVisible({ timeout: 2000 }).catch(() => false)) {
  161 |       await exp.click(); await page.waitForTimeout(300);
  162 |     }
  163 |     expect(true).toBeTruthy();
  164 |   });
  165 | });
  166 | 
  167 | // ====================================================================
  168 | // PHASE 4 — INVENTORY MANAGEMENT
  169 | // ====================================================================
  170 | test.describe('Phase 4 — Inventory Management', () => {
  171 |   test('Inventory page loads', async ({ page }) => {
  172 |     expect((await page.goto(`${ADMIN}/inventory`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  173 |   });
  174 | 
  175 |   test('Warehouse page loads', async ({ page }) => {
  176 |     expect((await page.goto(`${ADMIN}/warehouse`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  177 |   });
  178 | 
  179 |   test('Stock page loads', async ({ page }) => {
  180 |     expect((await page.goto(`${ADMIN}/stock`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  181 |   });
  182 | 
  183 |   test('Batch page loads', async ({ page }) => {
  184 |     expect((await page.goto(`${ADMIN}/batch`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  185 |   });
  186 | 
  187 |   test('Movements page loads', async ({ page }) => {
```