# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 3 — Product Management >> Export button exists on DataGrid
- Location: tests\admin-console.spec.ts:157:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/products
Call log:
  - navigating to "http://localhost:5174/admin/products", waiting until "networkidle"

```

# Test source

```ts
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
  87  |     await page.goto(`${ADMIN}/dashboard`, { waitUntil: 'networkidle' });
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
> 158 |     await page.goto(`${ADMIN}/products`, { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/products
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
  188 |     expect((await page.goto(`${ADMIN}/movements`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  189 |   });
  190 | 
  191 |   test('IMPLEMENTATION GAP: No stock update UI', async ({ page }) => {
  192 |     await page.goto(`${ADMIN}/inventory`, { waitUntil: 'networkidle' });
  193 |     expect(await page.locator('input[type="number"]').count()).toBe(0);
  194 |   });
  195 | 
  196 |   test('IMPLEMENTATION GAP: No inventory history API', async ({ page }) => {
  197 |     const r = await page.request.get('/api/inventory/history');
  198 |     expect((await r.text()).includes('inventory_history')).toBe(false);
  199 |   });
  200 | });
  201 | 
  202 | // ====================================================================
  203 | // PHASE 5 — ORDER MANAGEMENT
  204 | // ====================================================================
  205 | test.describe('Phase 5 — Order Management', () => {
  206 |   test('Orders page loads (HTTP)', async ({ page }) => {
  207 |     expect((await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  208 |   });
  209 | 
  210 |   test('Order search input exists', async ({ page }) => {
  211 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  212 |     const s = page.locator('input[type="search"],input[placeholder*="Search"]').first();
  213 |     if (await s.isVisible({ timeout: 3000 }).catch(() => false)) {
  214 |       await s.fill('ORD-'); await page.waitForTimeout(300);
  215 |     }
  216 |     expect(true).toBeTruthy();
  217 |   });
  218 | 
  219 |   test('IMPLEMENTATION GAP: No order status update', async ({ page }) => {
  220 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  221 |     expect(await page.locator('select,button:has-text("Update Status")').isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  222 |   });
  223 | 
  224 |   test('IMPLEMENTATION GAP: No bulk actions', async ({ page }) => {
  225 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  226 |     expect(await page.locator('button:has-text("Bulk")').isVisible({ timeout: 1000 }).catch(() => false)).toBe(false);
  227 |   });
  228 | 
  229 |   test('IMPLEMENTATION GAP: No order cancellation from admin', async ({ page }) => {
  230 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
  231 |     expect(await page.locator('button:has-text("Cancel")').count()).toBe(0);
  232 |   });
  233 | });
  234 | 
  235 | // ====================================================================
  236 | // PHASE 6 — USER MANAGEMENT
  237 | // ====================================================================
  238 | test.describe('Phase 6 — User Management', () => {
  239 |   test('Customers page loads', async ({ page }) => {
  240 |     expect((await page.goto(`${ADMIN}/customers`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  241 |   });
  242 | 
  243 |   test('IMPLEMENTATION GAP: No /admin/users route with content', async ({ page }) => {
  244 |     await page.goto(`${ADMIN}/users`, { waitUntil: 'networkidle' });
  245 |     expect(await hasText(page, 'User Management')).toBe(false);
  246 |   });
  247 | 
  248 |   test('IMPLEMENTATION GAP: No /admin/roles route', async ({ page }) => {
  249 |     await page.goto(`${ADMIN}/roles`, { waitUntil: 'networkidle' });
  250 |     expect(await hasText(page, 'Role Management')).toBe(false);
  251 |   });
  252 | 
  253 |   test('IMPLEMENTATION GAP: No user deactivate/reactivate', async ({ page }) => {
  254 |     await page.goto(`${ADMIN}/customers`, { waitUntil: 'networkidle' });
  255 |     expect(await page.locator('button:has-text("Deactivate")').count()).toBe(0);
  256 |   });
  257 | });
  258 | 
```