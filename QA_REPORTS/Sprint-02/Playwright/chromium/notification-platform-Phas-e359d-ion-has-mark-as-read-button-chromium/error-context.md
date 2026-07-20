# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 5 — In-App Notifications >> Notification has mark as read button
- Location: tests\notification-platform.spec.ts:224:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
Call log:
  - navigating to "http://localhost:5173/admin", waiting until "networkidle"

```

# Test source

```ts
  125 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: 'test@test.com', subject: 'Test', body: 'Test body', channel: 'EMAIL' }) }).catch(() => null);
  126 |     if (resp) {
  127 |       const status = resp.status;
  128 |       expect(status < 500).toBeTruthy();
  129 |     }
  130 |   });
  131 | 
  132 |   test('IMPLEMENTATION GAP: No dynamic placeholder substitution', async ({ page }) => {
  133 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  134 |     const t = await bodyText(page).catch(() => '');
  135 |     const hasPlaceholders = t.includes('{{') || t.includes('variable') || t.includes('placeholder');
  136 |     expect(hasPlaceholders).toBeTruthy();
  137 |   });
  138 | });
  139 | 
  140 | // ====================================================================
  141 | // PHASE 3 — SMS
  142 | // ====================================================================
  143 | test.describe('Phase 3 — SMS', () => {
  144 |   test('IMPLEMENTATION GAP: SMS notification service not available', async () => {
  145 |     const resp = await fetch(`${BASE}/notifications`).catch(() => null);
  146 |     if (resp) {
  147 |       const data = await resp.json();
  148 |       expect(data).toBeDefined();
  149 |     }
  150 |   });
  151 | 
  152 |   test('IMPLEMENTATION GAP: No SMS template UI', async ({ page }) => {
  153 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  154 |     const t = await bodyText(page).catch(() => '');
  155 |     const hasSMSUI = t.includes('sms') || t.includes('SMS');
  156 |     expect(hasSMSUI).toBeTruthy();
  157 |   });
  158 | 
  159 |   test('IMPLEMENTATION GAP: No SMS sending integration', async () => {
  160 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'OTP', body: 'Your OTP is 123456', channel: 'SMS' }) }).catch(() => null);
  161 |     if (resp) {
  162 |       const status = resp.status;
  163 |       expect(status < 500).toBeTruthy();
  164 |     }
  165 |   });
  166 | });
  167 | 
  168 | // ====================================================================
  169 | // PHASE 4 — WHATSAPP
  170 | // ====================================================================
  171 | test.describe('Phase 4 — WhatsApp', () => {
  172 |   test('IMPLEMENTATION GAP: WhatsApp notification service not available', async () => {
  173 |     const resp = await fetch(`${BASE}/notifications`).catch(() => null);
  174 |     if (resp) {
  175 |       const data = await resp.json();
  176 |       expect(data).toBeDefined();
  177 |     }
  178 |   });
  179 | 
  180 |   test('IMPLEMENTATION GAP: No WhatsApp template UI', async ({ page }) => {
  181 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  182 |     const t = await bodyText(page).catch(() => '');
  183 |     const hasWhatsAppUI = t.includes('whatsapp') || t.includes('WhatsApp');
  184 |     expect(hasWhatsAppUI).toBeTruthy();
  185 |   });
  186 | 
  187 |   test('IMPLEMENTATION GAP: No WhatsApp sending integration', async () => {
  188 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'Order Update', body: 'Your order has been shipped', channel: 'WHATSAPP' }) }).catch(() => null);
  189 |     if (resp) {
  190 |       const status = resp.status;
  191 |       expect(status < 500).toBeTruthy();
  192 |     }
  193 |   });
  194 | });
  195 | 
  196 | // ====================================================================
  197 | // PHASE 5 — IN-APP NOTIFICATIONS
  198 | // ====================================================================
  199 | test.describe('Phase 5 — In-App Notifications', () => {
  200 |   test('Notification bell icon visible in admin nav', async ({ page }) => {
  201 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  202 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  203 |     await expect(bell).toBeVisible({ timeout: 5000 });
  204 |   });
  205 | 
  206 |   test('Notification dropdown opens on bell click', async ({ page }) => {
  207 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  208 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  209 |     await bell.click();
  210 |     await page.waitForTimeout(500);
  211 |     const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
  212 |     await expect(dialog).toBeVisible({ timeout: 3000 });
  213 |   });
  214 | 
  215 |   test('Notification dropdown has notification list', async ({ page }) => {
  216 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  217 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  218 |     await bell.click();
  219 |     await page.waitForTimeout(500);
  220 |     const list = page.locator('[role="dialog"] [role="list"]');
  221 |     await expect(list).toBeVisible({ timeout: 3000 });
  222 |   });
  223 | 
  224 |   test('Notification has mark as read button', async ({ page }) => {
> 225 |     await page.goto('/admin', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
  226 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  227 |     await bell.click();
  228 |     await page.waitForTimeout(500);
  229 |     const markBtn = page.locator('[role="dialog"] button[aria-label="Mark as read"]').first();
  230 |     await expect(markBtn).toBeVisible({ timeout: 3000 });
  231 |   });
  232 | 
  233 |   test('Notification has dismiss button', async ({ page }) => {
  234 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  235 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  236 |     await bell.click();
  237 |     await page.waitForTimeout(500);
  238 |     const dismissBtn = page.locator('[role="dialog"] button[aria-label="Dismiss"]').first();
  239 |     await expect(dismissBtn).toBeVisible({ timeout: 3000 });
  240 |   });
  241 | 
  242 |   test('IMPLEMENTATION GAP: No unread count badge on bell', async ({ page }) => {
  243 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  244 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  245 |     await expect(bell).toBeVisible({ timeout: 3000 });
  246 |   });
  247 | 
  248 |   test('IMPLEMENTATION GAP: No mark all read button', async ({ page }) => {
  249 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  250 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  251 |     await bell.click();
  252 |     await page.waitForTimeout(500);
  253 |     const markAll = page.locator('[role="dialog"] button:has-text("Mark all read")');
  254 |     const exists = await markAll.count();
  255 |     expect(exists).toBe(0);
  256 |   });
  257 | 
  258 |   test('IMPLEMENTATION GAP: No view all notifications link', async ({ page }) => {
  259 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  260 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  261 |     await bell.click();
  262 |     await page.waitForTimeout(500);
  263 |     const viewAll = page.locator('[role="dialog"] button:has-text("View all")');
  264 |     const exists = await viewAll.count();
  265 |     expect(exists).toBe(0);
  266 |   });
  267 | 
  268 |   test('Admin communication notifications page loads', async ({ page }) => {
  269 |     await login(page);
  270 |     const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  271 |     expect(r?.status()).toBeLessThan(400);
  272 |   });
  273 | 
  274 |   test('Admin communication overview page loads', async ({ page }) => {
  275 |     await login(page);
  276 |     const r = await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
  277 |     expect(r?.status()).toBeLessThan(400);
  278 |   });
  279 | 
  280 |   test('Admin communication announcements page loads', async ({ page }) => {
  281 |     await login(page);
  282 |     const r = await page.goto('/admin/training/communication/announcements', { waitUntil: 'networkidle' });
  283 |     expect(r?.status()).toBeLessThan(400);
  284 |   });
  285 | 
  286 |   test('Admin communication scheduled page loads', async ({ page }) => {
  287 |     await login(page);
  288 |     const r = await page.goto('/admin/training/communication/scheduled', { waitUntil: 'networkidle' });
  289 |     expect(r?.status()).toBeLessThan(400);
  290 |   });
  291 | 
  292 |   test('Admin communication templates page loads', async ({ page }) => {
  293 |     await login(page);
  294 |     const r = await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
  295 |     expect(r?.status()).toBeLessThan(400);
  296 |   });
  297 | 
  298 |   test('Admin communication history page loads', async ({ page }) => {
  299 |     await login(page);
  300 |     const r = await page.goto('/admin/training/communication/history', { waitUntil: 'networkidle' });
  301 |     expect(r?.status()).toBeLessThan(400);
  302 |   });
  303 | 
  304 |   test('Admin communication delivery queue page loads', async ({ page }) => {
  305 |     await login(page);
  306 |     const r = await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  307 |     expect(r?.status()).toBeLessThan(400);
  308 |   });
  309 | 
  310 |   test('Admin communication statistics page loads', async ({ page }) => {
  311 |     await login(page);
  312 |     const r = await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
  313 |     expect(r?.status()).toBeLessThan(400);
  314 |   });
  315 | 
  316 |   test('Admin communication channels page loads', async ({ page }) => {
  317 |     await login(page);
  318 |     const r = await page.goto('/admin/training/communication/channels', { waitUntil: 'networkidle' });
  319 |     expect(r?.status()).toBeLessThan(400);
  320 |   });
  321 | 
  322 |   test('Design system NotificationProvider renders toast container', async ({ page }) => {
  323 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  324 |     const container = page.locator('.sk-notification-container');
  325 |     const exists = await container.count();
```