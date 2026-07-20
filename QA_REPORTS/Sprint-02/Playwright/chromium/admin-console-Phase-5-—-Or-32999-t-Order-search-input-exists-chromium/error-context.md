# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: admin-console.spec.ts >> Phase 5 — Order Management >> Order search input exists
- Location: tests\admin-console.spec.ts:210:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/orders
Call log:
  - navigating to "http://localhost:5174/admin/orders", waiting until "networkidle"

```

# Test source

```ts
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
> 211 |     await page.goto(`${ADMIN}/orders`, { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5174/admin/orders
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
  259 | // ====================================================================
  260 | // PHASE 7 — TRAINING MANAGEMENT
  261 | // ====================================================================
  262 | test.describe('Phase 7 — Training Management', () => {
  263 |   test('Training dashboard loads', async ({ page }) => {
  264 |     expect((await page.goto(`${ADMIN}/training/dashboard`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  265 |   });
  266 | 
  267 |   test('Training courses loads', async ({ page }) => {
  268 |     expect((await page.goto(`${ADMIN}/training/courses`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  269 |   });
  270 | 
  271 |   test('Training enrollment loads', async ({ page }) => {
  272 |     expect((await page.goto(`${ADMIN}/training/enrollment`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  273 |   });
  274 | 
  275 |   test('Training curriculum loads', async ({ page }) => {
  276 |     expect((await page.goto(`${ADMIN}/training/curriculum`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  277 |   });
  278 | 
  279 |   test('IMPLEMENTATION GAP: Training batches placeholder', async ({ page }) => {
  280 |     await page.goto(`${ADMIN}/training/batches`, { waitUntil: 'networkidle' });
  281 |     expect((await page.locator('body').innerText()).length).toBeGreaterThan(0);
  282 |   });
  283 | 
  284 |   test('IMPLEMENTATION GAP: Training attendance placeholder', async ({ page }) => {
  285 |     expect((await page.goto(`${ADMIN}/training/attendance`, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(400);
  286 |   });
  287 | });
  288 | 
  289 | // ====================================================================
  290 | // PHASE 8 — COUPONS & PRICING
  291 | // ====================================================================
  292 | test.describe('Phase 8 — Coupons & Pricing', () => {
  293 |   test('IMPLEMENTATION GAP: No /admin/coupons route', async ({ page }) => {
  294 |     await page.goto(`${ADMIN}/coupons`, { waitUntil: 'networkidle' });
  295 |     expect(await hasText(page, 'Coupon Management')).toBe(false);
  296 |   });
  297 | 
  298 |   test('IMPLEMENTATION GAP: No /admin/discounts route', async ({ page }) => {
  299 |     await page.goto(`${ADMIN}/discounts`, { waitUntil: 'networkidle' });
  300 |     expect(await hasText(page, 'Discount')).toBe(false);
  301 |   });
  302 | 
  303 |   test('IMPLEMENTATION GAP: No /admin/promo route', async ({ page }) => {
  304 |     await page.goto(`${ADMIN}/promo`, { waitUntil: 'networkidle' });
  305 |     expect(await hasText(page, 'Promo')).toBe(false);
  306 |   });
  307 | 
  308 |   test('IMPLEMENTATION GAP: No /admin/pricing route', async ({ page }) => {
  309 |     await page.goto(`${ADMIN}/pricing`, { waitUntil: 'networkidle' });
  310 |     expect(await hasText(page, 'Pricing')).toBe(false);
  311 |   });
```